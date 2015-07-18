package com.ltmonitor.jt808.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.entity.GnssData;
import com.ltmonitor.entity.MediaItem;
import com.ltmonitor.entity.TakePhotoModel;
import com.ltmonitor.entity.TerminalCommand;
import com.ltmonitor.entity.VehicleData;
import com.ltmonitor.jt808.model.MediaPacket;
import com.ltmonitor.jt808.protocol.JT_0200;
import com.ltmonitor.jt808.protocol.JT_0801;
import com.ltmonitor.jt808.protocol.JT_8800;
import com.ltmonitor.jt808.protocol.T808Message;
import com.ltmonitor.jt808.protocol.T808MessageHeader;
import com.ltmonitor.jt808.service.ICommandService;
import com.ltmonitor.jt808.service.IMediaService;
import com.ltmonitor.jt808.service.IMessageSender;
import com.ltmonitor.jt808.service.ITransferGpsService;
import com.ltmonitor.service.ILocationService;
import com.ltmonitor.service.IVehicleService;
import com.ltmonitor.service.JT808Constants;
import com.ltmonitor.util.DateUtil;
/**
 * 多媒体服务
 * @author tianfei
 *
 */
public class MediaService implements IMediaService {
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
	 * 根据坐标获取地址信息的服务
	 */
	private ILocationService locationService;

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

	private boolean continueCheck = true;

	public static ConcurrentMap<String, MediaPacket> msgMap = new ConcurrentHashMap<String, MediaPacket>();

	public MediaService() {
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
			Collection<MediaPacket> mpList = msgMap.values();
			for (MediaPacket mp : mpList) {
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
	private void sendAck(MediaPacket mp) {
		ArrayList<Short> packetNos = mp.getNeedReTransPacketNo();
		JT_8800 echoData = new JT_8800();
		echoData.setMultimediaDataId(mp.getMediaId());
		echoData.setRepassPacketsCount((byte) packetNos.size());
		echoData.setRepassPacketsNo(packetNos);

		T808Message ts = new T808Message();
		ts.setMessageContents(echoData);
		ts.setHeader(new T808MessageHeader());
		ts.getHeader().setMessageType(0x8800);
		ts.getHeader().setSimId(mp.getT808Message().getSimNo());
		ts.getHeader().setIsPackage(false);

		getMessageSender().Send808Message(ts);

	}

	/**
	 * 处理多媒体数据包
	 * 
	 * @param msg
	 */
	public void processMediaMsg(T808Message msg) {
		T808MessageHeader header = msg.getHeader();
		int packetNo = header.getMessagePacketNo();
		String key = msg.getSimNo() + "_" + msg.getMessageType() + "_"
				+ header.getMessageTotalPacketsCount();
		MediaPacket mp = msgMap.get(key);
		// 如果是第一个包
		if (packetNo == 1) {
			if (mp == null) {
				mp = new MediaPacket(msg);
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
			saveMedia(mp);
			this.sendAck(mp);
		}

	}
	
	public final TerminalCommand getLatestCommand(String simNo) {
		try {
			String hsql = "from TerminalCommand where (cmdType = ? or cmdType= ? or cmdType= ? or cmdType = ? ) and simNo = ? and createDate > ? order by createDate desc";
			TerminalCommand tc = (TerminalCommand) getBaseDao()
					.find(hsql,
							new Object[] {
							JT808Constants.CMD_TAKE_PHOTO,
							JT808Constants.CMD_AUDIO_RECORDER,JT808Constants.CMD_MEDIA_UPLOAD_SINGLE,
							JT808Constants.CMD_MEDIA_UPLOAD,simNo,
									DateUtil.getDate(DateUtil.now(),
											Calendar.HOUR, -1) });
			return tc;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return null;
	}
	

	/**
	 * 保存多媒体数据
	 * 
	 * @param mp
	 */
	private void saveMedia(MediaPacket mp) {
		T808Message msg = mp.getT808Message();
		T808MessageHeader header = msg.getHeader();
		logger.error("收到数据包:" + mp.toString());
		if (msg.getMessageContents() == null) {
			return;
		}

		// 如果是第一个分包就正常解析;
		JT_0801 uploadMediaMsg = (JT_0801) msg.getMessageContents();
		// 查询是否有最近的命令下发
		
		TerminalCommand tc = this
				.getLatestCommand(msg.getSimNo());
		if (tc != null) {
			tc.setStatus(TerminalCommand.STATUS_UPLOADED);
			baseDao.saveOrUpdate(tc);
		} else {
			logger.error("拍照应答没找到下发指令,");
		}

		MediaItem mi = new MediaItem();
		if (tc != null)
			mi.setCommandId(tc.getEntityId());// 和下发的拍照命令ID 关联上，便于查询
		mi.setCodeFormat(uploadMediaMsg.getMultidediaCodeFormat());
		mi.setMediaDataId((int) uploadMediaMsg.getMultimediaDataId());
		mi.setMediaType(uploadMediaMsg.getMultimediaType());
		mi.setChannelId(uploadMediaMsg.getChannelId());
		mi.setEventCode(uploadMediaMsg.getEventCode());
		mi.setSimNo(msg.getHeader().getSimId());

		VehicleData vd = vehicleService.getVehicleBySimNo(msg.getSimNo());
		if (vd != null)
			mi.setPlateNo(vd.getPlateNo());
		else
			mi.setPlateNo(msg.getPlateNo());
		if (tc != null
				&& TerminalCommand.FROM_GOV.equals(tc.getOwner()) == true) {
			transferTo809(tc, vd, uploadMediaMsg);
		}

		try {
			JT_0200 position = uploadMediaMsg.getPosition();
			if (position != null) {
				mi.setLatitude(position.getLatitude() / 1000000);
				mi.setLongitude(position.getLongitude() / 1000000);
				mi.setSpeed(position.getSpeed() / 10);
				if (position.getTime() != null) {
					Date dt = DateUtil.stringToDatetime(position.getTime(),
							"yyyy-MM-dd HH:mm:ss");
					mi.setSendTime(dt); // 取自位置时间
				}
				if (locationService != null) {
					// 根据坐标获取定位位置
					String location = locationService.getLocation(
							mi.getLatitude(), mi.getLongitude());
					mi.setLocation(location);
				}
			}
		} catch (Exception ex) {
			mi.setSendTime(new java.util.Date());
			logger.error("媒体数据时间解析错误" + ex.getMessage());
		}
		mi.setCommandType(MediaItem.UPLOAD); // 根据上传命令的应答

		String extention = "";
		// 0：JPEG；1：TIF；2：MP3；3：WAV；4：WMV
		String[] format = new String[] { "jpg", "tif", "mp3", "wav", "wmv",
				"wma" };

		File Directory = new File(getUploadDir());

		if (Directory.exists() == false) {
			Directory.mkdirs();
		}
		String fileName = mi.getPlateNo() + "_"
				+ DateUtil.toStringByFormat(new Date(), "yyyy-MM-dd-HH-mm-ss")
				+ "." + format[uploadMediaMsg.getMultidediaCodeFormat()];
		String fullFileName = getUploadDir() + "\\" + fileName;
		mi.setFileName(fileName);

		try {
			// 记录下上传的文件列表，便于检索
			getBaseDao().saveOrUpdate(mi);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
		}
		List<byte[]> mediaData = mp.getWholePacket();
		FileOutputStream pFileStream = null;
		try {
			pFileStream = new FileOutputStream(fullFileName, true);
			//int offset = 0;
			for (byte[] b : mediaData) {
				pFileStream.write(b, 0, b.length);
				//offset += b.length;
			}
		} catch (IOException ex) {
			logger.error(ex.getMessage(), ex);
		} finally {
			if (pFileStream != null) {
				try {
					pFileStream.close();
				} catch (IOException ex) {
					logger.error(ex.getMessage(), ex);
				}
			}
		}

	}

	private void WriteFile(String fileName, byte[] mediaData, int length) {

	}

	/**
	 * 转发到809平台
	 * 
	 * @param tc
	 * @param vd
	 * @param uploadMediaMsg
	 */
	private void transferTo809(TerminalCommand tc, VehicleData vd,
			JT_0801 uploadMediaMsg) {

		// 如果是上级下级下发的拍照命令，需要将拍照数据转发给上级平台
		try {
			TakePhotoModel tp = new TakePhotoModel();
			tp.setPlateNo(vd.getPlateNo());
			tp.setPlateColor(vd.getPlateColor());
			tp.setReplayResult(1); // 完成拍照
			tp.setLensId(uploadMediaMsg.getChannelId());
			tp.setPhotoSizeType(1);
			tp.setPhotoFormat(uploadMediaMsg.getMultimediaType());
			GnssData g = new GnssData();
			JT_0200 p = uploadMediaMsg.getPosition();
			if (p != null) {
				g.setGpsSpeed(p.getSpeed());
				short direction = 0;
				if (p.getCourse() > 0)
					direction = p.getCourse();
				g.setDirection(direction);
				g.setAltitude(p.getAltitude());
				g.setLatitude(p.getLatitude());
				g.setLongitude(p.getLongitude());
				g.setAlarmState(p.getAlarmFlag());
				g.setRecSpeed(p.getRecorderSpeed());
				g.setPosEncrypt(0);
				g.setTotalMileage(p.getMileage());
				g.setVehicleState(p.getStatus());
				g.setPlateColor(vd.getPlateColor());
				g.setPlateNo(vd.getPlateNo());
				if (p.getTime() != null) {
					Date dt = DateUtil.stringToDatetime(p.getTime(),
							"yyyy-MM-dd HH:mm:ss");

					g.setPosTime(dt);// 取自位置时间
				}
			}
			tp.setGnssData(g);
			tp.setPhotoFormat(1);
			byte[] photoData = uploadMediaMsg.getMultimediaData();
			tp.setPhotoData(new byte[photoData.length]);
			tp.setPhotoSize(photoData.length);
			System.arraycopy(photoData, 0, tp.getPhotoData(), 0,
					photoData.length);
			this.transferGpsService.transfer(tp); // 转发实时定位数据
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

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

	public ITransferGpsService getTransferGpsService() {
		return transferGpsService;
	}

	public void setTransferGpsService(ITransferGpsService transferGpsService) {
		this.transferGpsService = transferGpsService;
	}

	public int getMediaUploadTimeOut() {
		return mediaUploadTimeOut;
	}

	public void setMediaUploadTimeOut(int mediaUploadTimeOut) {
		this.mediaUploadTimeOut = mediaUploadTimeOut;
	}

}
