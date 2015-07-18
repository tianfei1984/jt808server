package com.ltmonitor.jt808.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.entity.Terminal;
import com.ltmonitor.entity.TerminalCommand;
import com.ltmonitor.entity.VehicleData;
import com.ltmonitor.jt808.protocol.JT_0001;
import com.ltmonitor.jt808.protocol.JT_0100;
import com.ltmonitor.jt808.protocol.JT_0102;
import com.ltmonitor.jt808.protocol.JT_0201;
import com.ltmonitor.jt808.protocol.JT_0500;
import com.ltmonitor.jt808.protocol.JT_8001;
import com.ltmonitor.jt808.protocol.JT_8100;
import com.ltmonitor.jt808.protocol.T808Message;
import com.ltmonitor.jt808.protocol.T808MessageHeader;
import com.ltmonitor.jt808.service.IAckService;
import com.ltmonitor.jt808.service.ICommandService;
import com.ltmonitor.jt808.service.IMessageSender;
import com.ltmonitor.jt808.service.ITransferGpsService;
import com.ltmonitor.jt809.entity.VehicleRegisterInfo;
import com.ltmonitor.service.IRealDataService;
import com.ltmonitor.service.IVehicleService;
import com.ltmonitor.service.JT808Constants;

/**
 * 终端上行数据应答服务
 * @author tianfei
 *
 */
public class AckService implements IAckService {

	private static Logger logger = Logger.getLogger(AckService.class);
	private ConcurrentLinkedQueue<T808Message> dataQueue = new ConcurrentLinkedQueue<T808Message>();

	private ICommandService commandService;
	private Thread processRealDataThread;
	private IMessageSender messageSender;

	// 实时数据服务
	private IRealDataService realDataService;

	private IVehicleService vehicleService;

	private String authencateNo = "1234567890A"; // 鉴权码
	//注册车辆集合：key:车牌号，value:SimNo
	private ConcurrentHashMap<String, String> vehicleRegisterMap = new ConcurrentHashMap<String, String>();
	//注册车辆集合：key:终端ID，value:SimNo
	private ConcurrentHashMap<String, String> terminalRegisterMap = new ConcurrentHashMap<String, String>();
	//车辆、终端注册关系集合：key:SIMNO  value:车牌号,终端ID
	private ConcurrentHashMap<String, Object> registerMap = new ConcurrentHashMap<String, Object>();

	// 数据转发服务,主要用于809转发
	private ITransferGpsService transferGpsService;

	private boolean checkRegister;

	private IBaseDao BaseDao;

	private boolean transferTo809Enabled;
	/**
	 * 对于0200的定位包是否进行应答
	 */
	private boolean ack0200PacketDisabled;

	public AckService() {
		processRealDataThread = new Thread(new Runnable() {
			public void run() {
				ProcessRealDataThreadFunc();
			}
		});
		processRealDataThread.start();
	}

	public void beginAck(T808Message tm) {
		int msgType = tm.getMessageType();
		// 0200的包不进行应答
		if (msgType == 0x0200 && ack0200PacketDisabled)
			return;

		// 注销请求
		if (msgType == 0x0003) {
			String simNo = tm.getSimNo();
			if (registerMap.containsKey(simNo)) {
				// 如果以前注册成功过，需要清除注册状态
				String registerData = "" + registerMap.get(simNo);
				if (registerData != null) {
					String[] temp = registerData.split(",");
					String plateNo = temp[0];
					String terminalId = temp[1];

					vehicleRegisterMap.remove(plateNo);
					terminalRegisterMap.remove(terminalId);
					registerMap.remove(simNo);
				}
			}
			return;
		}
		dataQueue.add(tm);
	}

	private void ProcessRealDataThreadFunc() {

		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
		int times = 0;
		while (true) {
			try {
				if (times > 0 && times % 50 == 0 && dataQueue.size() > 50) {
					logger.error("等待应答队列数:" + dataQueue.size());

					if (dataQueue.size() > 3000)
						dataQueue.clear();
				}

				T808Message tm = dataQueue.poll();
				final List<T808Message> msgList = new ArrayList<T808Message>();
				while (tm != null) {
					msgList.add(tm);
					if (msgList.size() > 100)
						break;
					tm = dataQueue.poll();
				}
				if (msgList.size() > 0) {
					fixedThreadPool.execute(new Runnable() {
						@Override
						public void run() {
							SendGeneralAck(msgList);
						}
					});
				}

			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}

			times++;
			try {
				Thread.sleep(200L);
			} catch (InterruptedException e1) {
			}
		}
	}

	private void SendGeneralAck(List<T808Message> msgList) {
		try {
			for (T808Message tm : msgList) {
				SendGeneralAck(tm);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 对终端发送上来的消息进行通用应答
	 * 
	 * @param msgFromTerminal
	 *            终端消息
	 */
	private void SendGeneralAck(T808Message msgFromTerminal) {
		int msgType = msgFromTerminal.getMessageType();
		if (msgType == 0)
			return;

		String simNo = msgFromTerminal.getSimNo();
		// 终端注销
		if (msgType == 0x0003) {
			if (registerMap.contains(simNo)) {
				// 如果以前注册成功过，需要清除注册状态
				String registerData = "" + registerMap.get(simNo);
				if (registerData != null) {
					String[] temp = registerData.split(",");
					String plateNo = temp[0];
					String terminalId = temp[1];

					vehicleRegisterMap.remove(plateNo);
					terminalRegisterMap.remove(terminalId);
					registerMap.remove(simNo);
				}
			}
			return;
		}
		// 终端注册
		else if (msgType == 0x0100) {
			// 对终端注册需要由平台进行应答
			JT_0100 registerData = (JT_0100) msgFromTerminal
					.getMessageContents();
			if (registerData == null) {
				return;
			}
			msgFromTerminal.setPlateNo(registerData.getPlateNo());
			int result = 0;// 注册成功
			if (checkRegister) {
				transferRegiser(simNo, registerData);
				// vi.set
				// 0：成功；1：车辆已被注册；2：数据库中无该车辆；3：终端已被注册；4：数据库中无该终端
				VehicleData vd = vehicleService
						.getVehicleByPlateNo(msgFromTerminal.getPlateNo());

				if (vd != null) {
					if (vehicleRegisterMap.containsKey(vd.getPlateNo())) {
						result = 1;// 车辆已被注册
					} else {
						Terminal term = vehicleService.getTerminalByTermNo(registerData
										.getTerminalId());
						if (term == null) {
							result = 4;// 数据库中无该终端
						} else {
							if (terminalRegisterMap.containsKey(term
									.getTermNo())) {
								result = 3;// 终端已被注册
							}
						}
					}

				} else {
					result = 2;// 数据库中无该车辆；
				}
				if (result == 0) {
					terminalRegisterMap.put(registerData.getTerminalId(), simNo);
					vehicleRegisterMap.put(vd.getPlateNo(), simNo);
					registerMap.put(simNo, registerData.getPlateNo() + ","
							+ registerData.getTerminalId());
				}
			}

			JT_8100 echoData = new JT_8100();
			echoData.setRegisterResponseMessageSerialNo(msgFromTerminal
					.getHeader().getMessageSerialNo());
			echoData.setRegisterResponseResult((byte) result);
			echoData.setRegisterNo(authencateNo);

			T808Message ts = new T808Message();
			ts.setMessageContents(echoData);
			ts.setHeader(new T808MessageHeader());
			ts.getHeader().setMessageType(0x8100);
			ts.getHeader().setSimId(simNo);
			// ts.Header.MessageSize = echoData.WriteToBytes().Length;
			ts.getHeader().setIsPackage(false);

			getMessageSender().Send808Message(ts);
			// 转发给809服务器
			if (transferTo809Enabled) {
				this.transferRegister(simNo);
			}

		} else if (msgType == 0x0001) {
			// 如果是终端通用应答，就更新数据库的指令状态为已应答
			JT_0001 answerData = (JT_0001) msgFromTerminal.getMessageContents();
			short platformSn = answerData.getResponseMessageSerialNo();
			String cmdStatus = answerData.getResponseResult() == 0 ? TerminalCommand.STATUS_SUCCESS
					: TerminalCommand.STATUS_FAILED;
			if (answerData.getResponseResult() >= 2) {
				cmdStatus = TerminalCommand.STATUS_NOT_SUPPORT;
			}

			TerminalCommand tc = this.commandService.UpdateStatus(
					msgFromTerminal.getHeader().getSimId(), platformSn,cmdStatus);
			if (tc != null && TerminalCommand.FROM_GOV.equals(tc.getOwner())) {
				VehicleData vd = realDataService.getVehicleData(tc.getSimNo());
				// 如果是上级平台下发的指令，则需要转发给上级平台
				int result = answerData.getResponseResult() == 0 ? 0 : 1;
				if (tc.getCmdType() == JT808Constants.CMD_DIAL_BACK) {
					// 单向监听应答
					this.transferGpsService.transferListenTerminalAck(
							vd.getPlateNo(), vd.getPlateColor(), (byte) result);
				} else if (tc.getCmdType() == JT808Constants.CMD_CONTROL_TERMINAL) {
					// 紧急接入应答
					this.transferGpsService.transferEmergencyAccessAck(
							vd.getPlateNo(), vd.getPlateColor(), (byte) result);
				} else if (tc.getCmdType() == JT808Constants.CMD_SEND_TEXT) {
					int msgId = Integer.parseInt(tc.getCmd());
					// 文本信息下发应答
					this.transferGpsService.transferTextInfoAck(
							vd.getPlateNo(), vd.getPlateColor(), msgId,(byte) result);
				}
			}
		} else if (msgType == 0x0201) {
			// 终端对位置查询（点名）的应答
			JT_0201 answerData = (JT_0201) msgFromTerminal.getMessageContents();
			short platformSn = answerData.getResponseMessageSerialNo();
			String cmdStatus = TerminalCommand.STATUS_SUCCESS;
			commandService.UpdateStatus(msgFromTerminal.getHeader().getSimId(),
					platformSn, cmdStatus);
		} else if (msgType == 0x0500) {
			// 终端对车门控制的应答
			JT_0500 answerData = (JT_0500) msgFromTerminal.getMessageContents();
			short platformSn = answerData.getResponseMessageSerialNo();
			String cmdStatus = TerminalCommand.STATUS_SUCCESS;
			commandService.UpdateStatus(msgFromTerminal.getHeader().getSimId(),
					platformSn, cmdStatus);
		} else if (msgType == 0x0104) {
			// 查询终端参数应答
			JT_8001 echoData = new JT_8001();
			echoData.setResponseMessageSerialNo(msgFromTerminal.getHeader()
					.getMessageSerialNo());
			echoData.setResponseMessageId((short) msgFromTerminal.getHeader()
					.getMessageType());
			echoData.setResponseResult((byte) 0);

			T808Message ts = new T808Message();
			ts.setMessageContents(echoData);
			ts.setHeader(new T808MessageHeader());
			ts.getHeader().setMessageType(0x8001);
			ts.getHeader().setSimId(msgFromTerminal.getHeader().getSimId());
			// ts.Header.MessageSize = echoData.WriteToBytes().Length;
			ts.getHeader().setIsPackage(false);
			getMessageSender().Send808Message(ts);

		} else {
			int ackResult = 0;// 通用应答，成功标志
			if (msgType == 0x0102) {
				// 终端鉴权，要向上级平台注册信息
				JT_0102 cmdData = (JT_0102) msgFromTerminal
						.getMessageContents();
				// 转发注册信息
				transferRegister(simNo); 

				if (cmdData != null && checkRegister) {
					ackResult = this.authencateNo.equals(cmdData.getRegisterNo()) ? 0 : 1; // 鉴权成功或失败
				}
				if(ackResult == 0){
					//设置终端上线
					realDataService.updateOnlineStatus(simNo, true);
				}
			} else if (msgType == 0x0200 && ack0200PacketDisabled) {
				// return;
			}
			// 对于终端发送的其他命令，平台一律进行通用应答
			JT_8001 echoData = new JT_8001();
			echoData.setResponseMessageSerialNo(msgFromTerminal.getHeader()
					.getMessageSerialNo());
			echoData.setResponseMessageId((short) msgType);
			echoData.setResponseResult((byte) ackResult); // 应答成功

			T808Message ts = new T808Message();
			ts.setMessageContents(echoData);
			ts.setHeader(new T808MessageHeader());
			ts.getHeader().setMessageType(0x8001);
			ts.getHeader().setSimId(simNo);
			ts.getHeader().setIsPackage(false);
			getMessageSender().Send808Message(ts);
		}

	}

	private void transferRegiser(String simNo, JT_0100 registerData) {
		try {

			VehicleRegisterInfo vi = this.getVehicleRegisterInfo(simNo);
			vi.setTerminalId(registerData.getTerminalId());
			vi.setTerminalModel(registerData.getTerminalModelNo());
			vi.setTerminalVendorId(registerData.getManufactureId());
			vi.setPlateColor(registerData.getPlateColor());

			VehicleData vd = realDataService.getVehicleData(simNo);
			if (vd != null) {
				vi.setDepId(vd.getDepId());
				vi.setPlateNo(vd.getPlateNo());
			} else {
				vi.setPlateNo(registerData.getPlateNo());
			}
			vi.setUpdateDate(new Date());
			BaseDao.save(vi);

			if (transferTo809Enabled == false)
				return;

//			this.transferGpsService.transferRegisterInfo(vi); // 终端鉴权后，上传注册信息
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			logger.error(ex.getStackTrace());
		}
	}

	/**
	 * 向上级平台注册车辆信息
	 * 
	 * @param simNo
	 */
	private void transferRegister(String simNo) {
		try {
			VehicleRegisterInfo vi = this.getVehicleRegisterInfo(simNo);
			if (vi.getEntityId() == 0) {
				VehicleData vd = realDataService.getVehicleData(simNo);
				if (vd != null && vd.getTermId() > 0) {
					Terminal terminal = vehicleService.getTerminal(vd
							.getTermId());

					if (terminal != null) {
						vi.setPlateNo(vd.getPlateNo());
						vi.setPlateColor(vd.getPlateColor());
						vi.setTerminalId(terminal.getTermNo()); // 终端编号
						vi.setTerminalModel(terminal.getTermType());
						vi.setTerminalVendorId(terminal.getMakeNo());// 厂商编号
						vi.setDepId(vd.getDepId());
						vi.setSimNo(vd.getSimNo());

					}
				}
			}
			if (vi.getPlateNo() != null) {
				vi.setUpdateDate(new Date());
				BaseDao.save(vi);
			}

			if (transferTo809Enabled == false)
				return;
			this.transferGpsService.transferRegisterInfo(vi); // 终端鉴权后，上传注册信息
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			logger.error(ex.getStackTrace());
		}

	}

	private VehicleRegisterInfo getVehicleRegisterInfo(String simNo) {
		String hql = "from VehicleRegisterInfo where simNo = ?";

		VehicleRegisterInfo vi = (VehicleRegisterInfo) this.BaseDao.find(hql,
				simNo);
		if (vi == null) {
			vi = new VehicleRegisterInfo();
			vi.setSimNo(simNo);
		}
		return vi;
	}

	public void setMessageSender(IMessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public IMessageSender getMessageSender() {
		return messageSender;
	}

	public ICommandService getCommandService() {
		return commandService;
	}

	public void setCommandService(ICommandService commandService) {
		this.commandService = commandService;
	}

	public ITransferGpsService getTransferGpsService() {
		return transferGpsService;
	}

	public void setTransferGpsService(ITransferGpsService transferGpsService) {
		this.transferGpsService = transferGpsService;
	}

	public IVehicleService getVehicleService() {
		return vehicleService;
	}

	public void setVehicleService(IVehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}

	public String getAuthencateNo() {
		return authencateNo;
	}

	public void setAuthencateNo(String authencateNo) {
		this.authencateNo = authencateNo;
	}

	public boolean isCheckRegister() {
		return checkRegister;
	}

	public void setCheckRegister(boolean checkRegister) {
		this.checkRegister = checkRegister;
	}

	public boolean isTransferTo809Enabled() {
		return transferTo809Enabled;
	}

	public void setTransferTo809Enabled(boolean transferTo809Enabled) {
		this.transferTo809Enabled = transferTo809Enabled;
	}

	public boolean isAck0200PacketDisabled() {
		return ack0200PacketDisabled;
	}

	public void setAck0200PacketDisabled(boolean ack0200PacketDisabled) {
		this.ack0200PacketDisabled = ack0200PacketDisabled;
	}

	public IRealDataService getRealDataService() {
		return realDataService;
	}

	public void setRealDataService(IRealDataService realDataService) {
		this.realDataService = realDataService;
	}

	public IBaseDao getBaseDao() {
		return BaseDao;
	}

	public void setBaseDao(IBaseDao baseDao) {
		BaseDao = baseDao;
	}

}
