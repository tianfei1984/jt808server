package com.ltmonitor.jt808.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.log4j.Logger;
import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.entity.TerminalCommand;
import com.ltmonitor.entity.VehicleData;
import com.ltmonitor.entity.VehicleRecorder;
import com.ltmonitor.jt808.model.VehicleRecorderPacket;
import com.ltmonitor.jt808.protocol.IRecorderDataBlock;
import com.ltmonitor.jt808.protocol.JT_0700;
import com.ltmonitor.jt808.protocol.Recorder_DoubtfulPointData;
import com.ltmonitor.jt808.protocol.Recorder_SpeedIn2Days;
import com.ltmonitor.jt808.protocol.Recorder_SpeedIn360Hours;
import com.ltmonitor.jt808.protocol.Recorder_TiredDrivingRecord;
import com.ltmonitor.jt808.protocol.T808Message;
import com.ltmonitor.jt808.protocol.T808MessageHeader;
import com.ltmonitor.jt808.protocol.TiredDrivingRecordItem;
import com.ltmonitor.jt808.service.ICommandService;
import com.ltmonitor.jt808.service.IMessageSender;
import com.ltmonitor.jt808.service.ITransferGpsService;
import com.ltmonitor.jt808.service.IVehicleRecorderService;
import com.ltmonitor.service.IRealDataService;
import com.ltmonitor.service.IVehicleService;
import com.ltmonitor.util.DateUtil;

public class VehicleRecorderService implements IVehicleRecorderService {

	private static Logger logger = Logger.getLogger(MediaService.class);

	private IBaseDao baseDao;
	/**
	 * 平台指令解析服务
	 */
	private ICommandService commandService;
	/**
	 * 车辆数据服务
	 */
	private IVehicleService vehicleService;

	/**
	 * 检测多媒体分包是否完整的上传
	 */
	private Thread checkPacketThread;
	/**
	 * 上传目录
	 */
	private String uploadDir;
	/**
	 * 上传超时时间,单位秒
	 */
	private int mediaUploadTimeOut = 15;
	/**
	 * 最大重传次数
	 */
	private int maxRetransTimes = 3;

	/**
	 * 消息发送处理
	 */
	private IMessageSender messageSender;
	
	// 数据转发服务,主要用于809转发
	private ITransferGpsService transferGpsService;
	
	/**
	 * 实时数据服务
	 */
	private IRealDataService realDataService;

	private boolean continueCheck = true;

	public static ConcurrentMap<String, VehicleRecorderPacket> msgMap = new ConcurrentHashMap<String, VehicleRecorderPacket>();

	public VehicleRecorderService() {
		checkPacketThread = new Thread(new Runnable() {
			public void run() {
				checkPacketThreadFunc();
			}
		});
		checkPacketThread.start();

	}

	/**
	 * 停止服务
	 */
	public void Stop() {
		continueCheck = false;
		try {
			checkPacketThread.join();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 检测多媒体分包是否完整的上传的线程函数 如果没有完整上传，需要发送重传包的应答
	 */
	private void checkPacketThreadFunc() {
		while (continueCheck) {
			Collection<VehicleRecorderPacket> mpList = msgMap.values();
			for (VehicleRecorderPacket mp : mpList) {
				double seconds = DateUtil.getSeconds(mp.getUpdateDate(),
						new Date());
				if (seconds > mediaUploadTimeOut) {

					if (mp.getRetransCount() > this.maxRetransTimes) {// 删除内存记录不再重传
						msgMap.remove(mp.getKey());
						logger.error(mp.toString() + "超过最大重传次数，直接丢弃");
					} else {
						// 如果超时，还没有收到所有的分包，需要对缺失的分包，要求终端重新上传
						mp.setRetransCount(mp.getRetransCount() + 1);
						sendAck(mp);
					}
				}
			}

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 应答,如果不完整，则要求重传缺失的分包
	 * 
	 * @param mp
	 */
	private void sendAck(VehicleRecorderPacket mp) {
//		ArrayList<Short> packetNos = mp.getNeedReTransPacketNo();
//		JT_8800 echoData = new JT_8800();
//		echoData.setMultimediaDataId(mp.getMediaId());
//		echoData.setRepassPacketsCount((byte) packetNos.size());
//		echoData.setRepassPacketsNo(packetNos);
//
//		T808Message ts = new T808Message();
//		ts.setMessageContents(echoData);
//		ts.setHeader(new T808MessageHeader());
//		ts.getHeader().setMessageType(0x8800);
//		ts.getHeader().setSimId(mp.getT808Message().getSimNo());
//		ts.getHeader().setIsPackage(false);
//
//		getMessageSender().Send808Message(ts);

	}

	/**
	 * 处理多媒体数据包
	 * 
	 * @param msg
	 */
	@Override
	public void processMediaMsg(T808Message msg) {
		T808MessageHeader header = msg.getHeader();
		int packetNo = header.getMessagePacketNo();
		String key = msg.getSimNo() + "_" + msg.getMessageType() + "_"
				+ header.getMessageTotalPacketsCount();
		VehicleRecorderPacket mp = msgMap.get(key);
		// 如果是第一个包
		if (packetNo == 1) {
			if (mp == null) {
				mp = new VehicleRecorderPacket(msg);
				mp.setKey(key);
				msgMap.put(key, mp);
			}
		}

		if (mp != null && mp.containPacket(packetNo) == false) {
			mp.addPacket(packetNo, msg.getChildPacket());
		}
		if (mp.isComplete()) {
			// 如果包已经上传完整，则保存，并删除内存记录
			msgMap.remove(mp.getKey());
			SaveVehicleRecorder(mp);
			this.sendAck(mp);
		}

	}
	
	private void SaveVehicleRecorder(VehicleRecorderPacket vr) {
		T808Message msg = vr.getT808Message();
		
		JT_0700 rd = (JT_0700) msg.getMessageContents();
		rd.ReadFromBytes(vr.getWholePacket());
		VehicleData vd = realDataService.getVehicleData(msg.getSimNo());

		if (vd == null)
			return;

		// 响应流水号
		int resNo = rd.getResponseMessageSerialNo();

		IRecorderDataBlock recorderData = rd.getData();

		// String hsql = "from TerminalCommand where SN = ?";
		TerminalCommand tc = commandService.getCommandBySn(resNo);// (TerminalCommand)this.baseDao.find(hsql,
																	// resNo);
		int commandId = 0;
		if (tc != null) {
			commandId = tc.getEntityId();
			tc = commandService.UpdateStatus(msg.getSimNo(), resNo,
					TerminalCommand.STATUS_SUCCESS);
			if (TerminalCommand.FROM_GOV.equals(tc.getOwner())) {
				// 如果是上级平台下发的指令，则需要转发给上级平台
				this.transferGpsService.transferRecorderData(vd.getPlateNo(),
						vd.getPlateColor(), rd.getCommandWord(),rd.getCmdData());
			}

		}
		List<VehicleRecorder> result = new ArrayList<VehicleRecorder>();
		byte cmdWord = recorderData.getCommandWord();
		logger.info(cmdWord + ',' + recorderData.toString());
		if (cmdWord == 0x05) {
			Recorder_SpeedIn360Hours dv = (Recorder_SpeedIn360Hours) recorderData;
			for (Date key : dv.getSpeedsIn360Hours().keySet()) {
				byte[] values = dv.getSpeedsIn360Hours().get(key);
				StringBuilder sb = new StringBuilder();
				int m = 0;
				for (byte b : values) {
					VehicleRecorder vr1 = new VehicleRecorder();
					vr1.setCmd(cmdWord);
					vr1.setCommandId(commandId);
					vr1.setStartTime(key);
					vr1.setSpeed(b);
					vr1.setSortId(m++);
					vr1.setVehicleId(vd.getEntityId());
					result.add(vr1);
				}
			}

		} else if (cmdWord == 0x07) {
			Recorder_DoubtfulPointData dv = (Recorder_DoubtfulPointData) recorderData;
			for (Date key : dv.getDoubtfulPointData().keySet()) {
				ArrayList<Byte[]> values = dv.getDoubtfulPointData().get(key);
				StringBuilder sb = new StringBuilder();
				int m = 0;
				for (Byte[] b : values) {
					VehicleRecorder vr1 = new VehicleRecorder();
					vr1.setCmd(cmdWord);
					vr1.setCommandId(commandId);
					vr1.setStartTime(key);
					vr1.setSpeed(b[0]);
					vr1.setSignal(b[1]);
					vr1.setSortId(m++);
					vr1.setVehicleId(vd.getEntityId());
					result.add(vr1);
				}
			}

		} else if (cmdWord == 0x09) {
			// 最近两天内的数据
			Recorder_SpeedIn2Days dv = (Recorder_SpeedIn2Days) recorderData;
			for (Date key : dv.getSpeedsIn2Days().keySet()) {
				byte[] values = dv.getSpeedsIn2Days().get(key);
				StringBuilder sb = new StringBuilder();
				int m = 0;
				for (byte b : values) {
					VehicleRecorder vr1 = new VehicleRecorder();
					vr1.setCmd(cmdWord);
					vr1.setCommandId(commandId);
					vr1.setStartTime(key);
					vr1.setSpeed(b);
					vr1.setSortId(m++);
					vr1.setVehicleId(vd.getEntityId());
					result.add(vr1);
				}
			}

		} else if (cmdWord == 0x11) {
			// 疲劳驾驶
			Recorder_TiredDrivingRecord dv = (Recorder_TiredDrivingRecord) recorderData;
			int m = 0;
			for (TiredDrivingRecordItem ti : dv.getRecords()) {
				VehicleRecorder vr1 = new VehicleRecorder();
				vr1.setCmd(cmdWord);
				vr1.setCommandId(commandId);
				vr1.setStartTime(ti.getStartTime());
				vr1.setEndTime(ti.getEndTime());
				// vr1.setCmdData(ti.getDriverLincenseNo())；
				vr1.setSortId(m++);
				vr1.setVehicleId(vd.getEntityId());
				result.add(vr1);
			}
		} else {
			VehicleRecorder vr1 = new VehicleRecorder();
			vr1.setCmd(cmdWord);
			vr1.setCommandId(commandId);
			vr1.setVehicleId(vd.getEntityId());
			vr1.setCmdData(recorderData.toString());
			result.add(vr1);
		}

		this.baseDao.saveOrUpdateAll(result);
	}

	public IVehicleService getVehicleService() {
		return vehicleService;
	}

	public void setVehicleService(IVehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}

	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}

	public IBaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(IBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public ICommandService getCommandService() {
		return commandService;
	}

	public void setCommandService(ICommandService commandService) {
		this.commandService = commandService;
	}

	public IMessageSender getMessageSender() {
		return messageSender;
	}

	public void setMessageSender(IMessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public boolean isContinueCheck() {
		return continueCheck;
	}

	public void setContinueCheck(boolean continueCheck) {
		this.continueCheck = continueCheck;
	}

	public int getMaxRetransTimes() {
		return maxRetransTimes;
	}

	public void setMaxRetransTimes(int maxRetransTimes) {
		this.maxRetransTimes = maxRetransTimes;
	}

	public int getMediaUploadTimeOut() {
		return mediaUploadTimeOut;
	}

	public void setMediaUploadTimeOut(int mediaUploadTimeOut) {
		this.mediaUploadTimeOut = mediaUploadTimeOut;
	}

	public ITransferGpsService getTransferGpsService() {
		return transferGpsService;
	}

	public void setTransferGpsService(ITransferGpsService transferGpsService) {
		this.transferGpsService = transferGpsService;
	}

	public IRealDataService getRealDataService() {
		return realDataService;
	}

	public void setRealDataService(IRealDataService realDataService) {
		this.realDataService = realDataService;
	}

}
