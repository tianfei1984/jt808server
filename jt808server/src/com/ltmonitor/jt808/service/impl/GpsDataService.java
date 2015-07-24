package com.ltmonitor.jt808.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import com.ltmonitor.app.StringHelper;
import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.dao.impl.DaoIbatisImpl;
import com.ltmonitor.entity.BasicData;
import com.ltmonitor.entity.DriverCardRecord;
import com.ltmonitor.entity.DriverInfo;
import com.ltmonitor.entity.EWayBill;
import com.ltmonitor.entity.GPSRealData;
import com.ltmonitor.entity.GnssData;
import com.ltmonitor.entity.GpsInfo;
import com.ltmonitor.entity.MediaItem;
import com.ltmonitor.entity.StringUtil;
import com.ltmonitor.entity.TakePhotoModel;
import com.ltmonitor.entity.TerminalCommand;
import com.ltmonitor.entity.TerminalParam;
import com.ltmonitor.entity.VehicleData;
import com.ltmonitor.entity.VehicleRecorder;
import com.ltmonitor.jt808.protocol.IRecorderDataBlock;
import com.ltmonitor.jt808.protocol.JT_0104;
import com.ltmonitor.jt808.protocol.JT_0200;
import com.ltmonitor.jt808.protocol.JT_0201;
import com.ltmonitor.jt808.protocol.JT_0301;
import com.ltmonitor.jt808.protocol.JT_0302;
import com.ltmonitor.jt808.protocol.JT_0303;
import com.ltmonitor.jt808.protocol.JT_0700;
import com.ltmonitor.jt808.protocol.JT_0701;
import com.ltmonitor.jt808.protocol.JT_0702;
import com.ltmonitor.jt808.protocol.JT_0702_old;
import com.ltmonitor.jt808.protocol.JT_0704;
import com.ltmonitor.jt808.protocol.JT_0800;
import com.ltmonitor.jt808.protocol.JT_0801;
import com.ltmonitor.jt808.protocol.JT_0802;
import com.ltmonitor.jt808.protocol.JT_0900;
import com.ltmonitor.jt808.protocol.MuldimediaSearchDataItem;
import com.ltmonitor.jt808.protocol.ParameterItem;
import com.ltmonitor.jt808.protocol.PostitionAdditional_InOutAreaAlarmAdditional;
import com.ltmonitor.jt808.protocol.PostitionAdditional_OverSpeedAlarmAdditional;
import com.ltmonitor.jt808.protocol.PostitionAdditional_RouteDriveTimeAlarmAdditional;
import com.ltmonitor.jt808.protocol.Recorder_DoubtfulPointData;
import com.ltmonitor.jt808.protocol.Recorder_SpeedIn2Days;
import com.ltmonitor.jt808.protocol.Recorder_SpeedIn360Hours;
import com.ltmonitor.jt808.protocol.Recorder_TiredDrivingRecord;
import com.ltmonitor.jt808.protocol.T808Message;
import com.ltmonitor.jt808.protocol.T808MessageHeader;
import com.ltmonitor.jt808.protocol.TiredDrivingRecordItem;
import com.ltmonitor.jt808.service.IAlarmService;
import com.ltmonitor.jt808.service.ICommandService;
import com.ltmonitor.jt808.service.IGpsDataService;
import com.ltmonitor.jt808.service.IMediaService;
import com.ltmonitor.jt808.service.ITransferGpsService;
import com.ltmonitor.jt808.tool.Tools;
import com.ltmonitor.jt809.entity.DriverModel;
import com.ltmonitor.service.IBaseService;
import com.ltmonitor.service.ILocationService;
import com.ltmonitor.service.IRealDataService;
import com.ltmonitor.service.JT808Constants;
import com.ltmonitor.util.DateUtil;
/**
 * 终端上行数据处理实现类
 * @author tianfei
 *
 */
public class GpsDataService implements IGpsDataService {
	public static String TURN_ON = "1"; // 报警开
	public static String TURN_OFF = "0"; // 报警关闭

	private static Logger logger = Logger.getLogger(GpsDataService.class);
	private ConcurrentLinkedQueue<T808Message> dataQueue = new ConcurrentLinkedQueue<T808Message>();

	/**
	 * 线程池，默认是50个
	 */
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);
	// 实时数据缓存
	public ConcurrentMap<String, GPSRealData> realDataMap = new ConcurrentHashMap<String, GPSRealData>();

	/*
	 * 信息点播，存放终端卡号和点播类型ID的映射关系 系统定时调取更信息类的信息，下发给请求点播的终端
	 * key:SimNo   value:0取消， 1:点播
	 */
	private ConcurrentHashMap<String, Integer> inforIdMap = new ConcurrentHashMap<String, Integer>();

	private IBaseDao baseDao;

	private DaoIbatisImpl queryDao;

	private Boolean enableSaveDb;

	private Boolean startProcess = true;

	private ICommandService commandService;
	// 数据转发服务,主要用于809转发
	private ITransferGpsService transferGpsService;

	private IMediaService mediaService;
	/**
	 * 实时数据服务
	 */
	private IRealDataService realDataService;
	/**
	 * 根据坐标获取地址信息的服务
	 */
	private ILocationService locationService;

	/**
	 * 信息点播线程，不断的根据终端点播的信息类型，将数据库对应类型的信息，下发给终端
	 */
	private Thread vodThread;
	/**
	 * 实时数据数据处理线程
	 */
	private Thread processRealDataThread;
	// 报警分析服务，分析定位数据，生成报警记录
	private IAlarmService alarmService;
	/**
	 * 基础服务，主要是获得要给终端发布的信息数据
	 */
	private IBaseService baseService;
	/**
	 * 行驶记录仪版本 2003,2012两种,默认是2003
	 */
	private String vehicleRecorderVersion;

	public GpsDataService() {

	}
	
	/**
	 * 启动数据服务
	 */
	public void start()
	{
		//数据包处理线程
		processRealDataThread = new Thread(new Runnable() {
			public void run() {
				ProcessRealDataThreadFunc();
			}
		});
		processRealDataThread.start();
		//点播处理线程
		vodThread = new Thread(new Runnable() {
			public void run() {
				vodThreadFunc();
			}
		});
		vodThread.start();
	}

	public void stopService() {
		startProcess = false;
		try {
			processRealDataThread.join(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		getAlarmService().stopService();
	}

	public final IBaseDao getBaseDao() {
		return baseDao;
	}

	public final void setBaseDao(IBaseDao value) {
		baseDao = value;
	}

	private String uploadDir;

	public final String getUploadDir() {
		return uploadDir;
	}

	public final void setUploadDir(String value) {
		uploadDir = value;
	}

	// 是否是新808
	private String isNew808;

	public final String getIsNew808() {
		return isNew808;
	}

	public final void setIsNew808(String value) {
		isNew808 = value;
	}

	/**
	 * 加载实时数据
	 * 
	 * @return
	 */
	public List getAllRealData() {
		String hsql = "from GPSRealData ";

		List result = baseDao.query(hsql, new Object[] {});

		return result;
	}

	/**
	 * 在spring初始化时调用，在applicationContext-jt808.xml中配置此方法
	 */
	public void init() {
		//this.resetGpsOnlineState();
	}

	// 将所有车辆的状态设置为false
	public final void resetGpsOnlineState() {
		try {
			this.queryDao.update("resetRealDataState", new HashMap());
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	// 获取实时数据

	// 保存终端参数
	private void SaveTerminalParam(T808Message msg) {
		JT_0104 ParamMessage = (JT_0104) msg.getMessageContents();
		int sn = ParamMessage.getResponseMessageSerialNo();
		TerminalCommand tc = commandService.getCommandBySn(sn);// (TerminalCommand)this.baseDao.find(hsql,
																// resNo);
		int commandId = 0;
		if (tc != null) {
			commandId = tc.getEntityId();
		}

		java.util.ArrayList<TerminalParam> result = new java.util.ArrayList<TerminalParam>();

		for (ParameterItem pi : ParamMessage.getParameters()) {
			try {
				String paramCode = "0x"
						+ Tools.ToHexString(pi.getParameterId(), 2)
								.toUpperCase();
				String hql = "from TerminalParam where simNo = ? and code = ?";
				TerminalParam tp = (TerminalParam) getBaseDao().find(hql,
						new Object[] { msg.getHeader().getSimId(), paramCode });
				if (tp == null) {
					tp = new TerminalParam();
				}
				tp.setSimNo(msg.getHeader().getSimId());
				tp.setCode(paramCode);
				tp.setValue("" + pi.getParameterValue());
				tp.setFieldType("" + pi.getParameterLength());
				tp.setUpdateDate(new java.util.Date());
				tp.setCommandId(commandId);
				tp.setSN(ParamMessage.getResponseMessageSerialNo()); // 对应平台流水号
				getBaseDao().saveOrUpdate(tp);
				// result.Add(tp);
			} catch (RuntimeException ex) {
				logger.error(ex.getMessage());
				logger.error(ex.getStackTrace());
			}

		}

		if (tc != null) {
			//更新查询终端参数命令
			commandId = tc.getEntityId();
			commandService.UpdateStatus(msg.getSimNo(), sn,
					TerminalCommand.STATUS_SUCCESS);
		}
	}

	/**
	 * 转发实时数据
	 * 
	 * @param rd
	 * @param gi
	 */
	public void transfer(GPSRealData rd, GpsInfo gi) {
		if (transferGpsService == null
				|| this.transferGpsService.isTransferTo809Enabled() == false)
			return;

		GnssData gd = new GnssData();
		gd.setAltitude((int) (rd.getAltitude() * 10));
		gd.setDirection(rd.getDirection());
		gd.setGpsSpeed((int) (rd.getVelocity() * 10));
		gd.setLatitude((int) (rd.getLatitude() * 1000000));
		gd.setLongitude((int) (rd.getLongitude() * 1000000));
		gd.setPlateNo(rd.getPlateNo());
		gd.setTotalMileage((int) (rd.getMileage() * 10));
		gd.setDirection(rd.getDirection());
		// long test = Long.valueOf(rd.getAlarmState(), 2);
		long alarmState = Long.valueOf(rd.getAlarmState(), 2);
		// gd.setAlarmState(alarmState);
		gd.setAlarmState(gi.getAlarmState());
		// gd.setAlarmState(2);
		long intStatus = Long.valueOf(rd.getStatus(), 2);
		gd.setVehicleState(intStatus);
		gd.setVehicleState(gi.getStatus());
		gd.setPosTime(rd.getSendTime());

		VehicleData vd = realDataService.getVehicleData(rd.getSimNo());
		if (vd != null)
			gd.setPlateColor(vd.getPlateColor());

		transferGpsService.transfer(gd);
	}

	// 处理GPS数据
	public final void ProcessMessage(T808Message message) {
		String simNo = message.getSimNo();
		// this.realDataService.updateOnlineTime(simNo);// 更新在线时间，用于生成在线记录
		//消息类型
		int headerType = message.getHeader().getMessageType();
		if (headerType == 0x0002 || headerType == 0x0001 // 心跳 -
				|| headerType == 0x0000 || headerType == 0x0100 // 注册
				|| headerType == 0x0003 // 终端注销
				|| headerType == 0x0102) // 鉴权 - -
		{
			// 非数据包，不进行处理
			return;
		}

		dataQueue.add(message);
	}

	/**
	 * 信息点播线程，根据终端上传的点播请求，从数据库中读取对应信息类型的内容，不断的下发给请求的终端。
	 */
	private void vodThreadFunc() {
		while (true) {
			Set<String> keys = this.inforIdMap.keySet();
			for (String simNo : keys) {
				VehicleData vd = this.realDataService.getVehicleData(simNo);
				int inforId = inforIdMap.get(simNo);
				List<BasicData> bdList = baseService.getInformationByType("" + inforId);
				for (BasicData information : bdList) {
					// 信息下发
					TerminalCommand tc = new TerminalCommand();
					tc.setCmdType(JT808Constants.CMD_INFORMATION);
					// int configType = info;
					String content = "";
					tc.setCmdData(inforId + ";" + information.getName());
					tc.setSimNo(simNo);
					tc.setPlateNo(vd.getPlateNo());
					tc.setOwner("timer");
					tc.setCmd("" + inforId);
					this.baseDao.save(tc);
					try {
						Thread.sleep(1000 * 10 * 1);
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
				}
			}
			try {
				Thread.sleep(1000 * 60 * 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	private void ProcessRealDataThreadFunc() {

		while (queryDao == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		//创建GPS信息表按天生成
		try {
			for (int m = 0; m < 4; m++) {
				Date nextDay = DateUtil.getDate(new Date(),Calendar.DAY_OF_YEAR, m);
				String tableName = "gpsInfo"
						+ DateUtil.toStringByFormat(nextDay, "yyyyMMdd");
				queryDao.createGpsInfoTableIfNotExist(tableName);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		while (true) {
			try {
				T808Message tm = dataQueue.poll();
				final List<T808Message> msgList = new ArrayList<T808Message>();
				while (tm != null) {
					msgList.add(tm);
					if (msgList.size() > 100) {
						break;
					}
					tm = dataQueue.poll();
				}

				if (msgList.size() > 0) {
					fixedThreadPool.execute(new Runnable() {
						@Override
						public void run() {
							processData(msgList);
						}
					});
				}
			} catch (Exception e) {

			}
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e1) {
			}
		}
	}
	
	/**
	 * 批量保存轨迹
	 * @param gpsInfoList
	 */
	public void batchInsert(List<GpsInfo> gpsInfoList) {
		Date today = new Date();
		String strToday = DateUtil.toStringByFormat(today, "yyyyMMdd");
		try {

			if (gpsInfoList.size() > 0) {
				for (GpsInfo g : gpsInfoList) {
					String tableName = DateUtil.toStringByFormat(
							g.getSendTime(), "yyyyMMdd");
					if (tableName.compareTo(strToday) > 0)
						continue;// 如果gps时间大于当前日期，则表明是非法的GPS时间或数据，无法入库
					tableName = "gpsInfo" + tableName;
					g.setTableName(tableName);
				}
				this.queryDao.batchInsert("insertGpsInfo", gpsInfoList);
			}

			// Date end = new Date();

			// double seconds = DateUtil.getSeconds(start, end);
			// logger.error("总入库耗时:" + seconds + ",条数：" + gpsInfoList.size());

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	private void processData(List<T808Message> msgList) {

		final ArrayList<GpsInfo> gpsInfoList = new ArrayList<GpsInfo>();
		// List realDataList = new ArrayList();
		Date start = new Date();
		for (T808Message msg : msgList) {
			try {
				int headerType = msg.getMessageType();
				// 定位数据
				if (headerType == 0x0200 || headerType == 0x0201) {
					this.processLocationPacket(msg, gpsInfoList);
				} else {
					processData(msg);
				}

			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}

		// Date end1 = new Date();
		// double seconds1 = DateUtil.getSeconds(start, end1);
		// logger.error("处理耗时:"+seconds1);

		if (this.enableSaveDb && gpsInfoList.size() > 0) {
			fixedThreadPool.execute(new Runnable() {
				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					batchInsert(gpsInfoList);
				}
			});
		}

		// Date end = new Date();

		// double seconds = DateUtil.getSeconds(start, end);
		// logger.error("总入库耗时:"+seconds+",条数："+gpsInfoList.size());
	}

	/**
	 * 收到808消息后，根据消息的不同，进行不同的处理
	 * 
	 * @param message
	 */
	private void processData(T808Message message) {
		int headerType = message.getHeader().getMessageType();
		String simNo = message.getSimNo();
		if (headerType == 0x0104) {
			// 查询参数返回的应答参数数据存入数据库中
			SaveTerminalParam(message);
			return;
		} else if (headerType == 0x0801) {
			// 媒体上传数据
			// SaveMedia(message);
			// 交给媒体上传服务处理
			mediaService.processMediaMsg(message);
		} else if (headerType == 0x0800) {
			// 媒体信息上传数据
			SaveMediaInfo(message);
		} else if (headerType == 0x0802) {
			// 媒体检索数据
			this.SaveMediaSearchItem(message);
		} else if (headerType == 0x0700) {
			// 行车记录仪数据
			SaveVehicleRecorder(message);
		} else if (headerType == 0x0704) {
			// 定位补报，需要将补报的定位信息批量入库
			JT_0704 rd = (JT_0704) message.getMessageContents();
			List<JT_0200> packetList = rd.getPositionReportList();
			List<GpsInfo> gpsInfoList = new ArrayList<GpsInfo>();
			for (JT_0200 p : packetList) {
				this.processBatchLocationPacket(simNo, p, gpsInfoList);
			}
			if (gpsInfoList.size() > 0) {
				batchInsert(gpsInfoList);
			}
		} else if (headerType == 0x0701) {
			// 电子运单数据
			saveEWayBill(message);
			//添加电子运单命令
			JT_0701 rd = (JT_0701) message.getMessageContents();
			TerminalCommand tc = new TerminalCommand();
			tc.setCmdType(headerType);
			tc.setSN(message.getHeader().getMessageSerialNo());
			tc.setSimNo(message.getSimNo());
			VehicleData vd = realDataService.getVehicleData(tc.getSimNo());
			if (vd != null) {
				tc.setPlateNo(vd.getPlateNo());
				tc.setVehicleId(vd.getEntityId());
			}
			tc.setCmdData(rd.getElectronicFreightContent());
			tc.setOwner(TerminalCommand.FROM_TERMINAL);
			tc.setUpdateDate(new Date());
			tc.setStatus(TerminalCommand.STATUS_SUCCESS);

			this.baseDao.save(tc);
		} else if (headerType == 0x0702) {
			// 驾驶员主动上报信息
			this.saveDriver(message);
			JT_0702 rd = (JT_0702) message.getMessageContents();
			if (rd == null)
				return;
			TerminalCommand tc = new TerminalCommand();
			tc.setCmdType(headerType);
			tc.setSN(message.getHeader().getMessageSerialNo());
			tc.setSimNo(message.getSimNo());
			VehicleData vd = realDataService.getVehicleData(tc.getSimNo());
			if (vd != null) {
				tc.setPlateNo(vd.getPlateNo());
				tc.setVehicleId(vd.getEntityId());
			}
			tc.setCmdData(rd.toString());
			tc.setOwner(TerminalCommand.FROM_TERMINAL);
			tc.setUpdateDate(new Date());
			tc.setStatus(TerminalCommand.STATUS_SUCCESS);

			this.baseDao.save(tc);
		} else if (headerType == 0x0302) {
			// 终端对提问下发指令的应答
			JT_0302 ack = (JT_0302) message.getMessageContents();
			int responseNo = ack.getResponseMessageSerialNo();
			int answerId = ack.getAnswerId();
			String answer = "";

			String hsql = "from TerminalCommand where SN = ? and createDate > ? order by createDate desc";
			Date startDate = DateUtil.getDate(new Date(), Calendar.HOUR_OF_DAY, -1);
			TerminalCommand oldTc = (TerminalCommand) this.baseDao.find(hsql,
					new Object[] { responseNo, startDate });

			// 需要找到以前提问下发的候选答案，来判断提问应答的是否正确
			if (oldTc != null && oldTc.getCmdData() != null) {
				String[] fields = oldTc.getCmdData().split("[;]", -1);
				for (String field : fields) {
					if (StringHelper.isNullOrEmpty(field)) {
						continue;
					}
					String[] items = field.split("[,]", -1);
					int id = Integer.parseInt(items[0]);
					String answerOption = items[1];
					if (answerId == id) {
						answer = answerOption;
					}
				}
			}
			String answerResult = StringUtil.isNullOrEmpty(answer) ? "错误"
					: "正确";
			TerminalCommand tc = new TerminalCommand();
			tc.setCmdType(headerType);
			tc.setSN(message.getHeader().getMessageSerialNo());
			tc.setSimNo(message.getSimNo());
			VehicleData vd = realDataService.getVehicleData(tc.getSimNo());
			if (vd != null) {
				tc.setPlateNo(vd.getPlateNo());
				tc.setVehicleId(vd.getEntityId());
			}
			tc.setCmdData("选择ID是:" + answerId + ",答案内容是:" + answer + ",回答:"
					+ answerResult);
			tc.setOwner(TerminalCommand.FROM_TERMINAL);
			tc.setUpdateDate(new Date());
			tc.setStatus(TerminalCommand.STATUS_SUCCESS);

			this.baseDao.save(tc);

		} else if (headerType == 0x0303) {
			// 信息点播/取消
			JT_0303 ack = (JT_0303) message.getMessageContents();
			int inforType = ack.getMessageType(); // 点播的信息类型
			int req = ack.getPointResult();// 0：取消；1：点播

			if (req == 0) {
				// 取消点播，需要在去除缓存，避免再次发送信息
				this.inforIdMap.remove(message.getSimNo());
			} else {
				// 放于内存中，进行定时下发点播信息
				this.inforIdMap.put(message.getSimNo(), req);
			}
			try {
				TerminalCommand tc = new TerminalCommand();
				tc.setCmdType(headerType);
				tc.setSN(message.getHeader().getMessageSerialNo());
				tc.setSimNo(message.getSimNo());
				VehicleData vd = realDataService.getVehicleData(tc.getSimNo());
				if (vd != null) {
					tc.setPlateNo(vd.getPlateNo());
					tc.setVehicleId(vd.getEntityId());
				}
				String strReq = req == 0 ? "终端取消点播" : "终端请求点播";
				tc.setCmdData(strReq + ",信息类型ID是:" + inforType);
				tc.setOwner(TerminalCommand.FROM_TERMINAL);
				tc.setUpdateDate(new Date());
				tc.setStatus(TerminalCommand.STATUS_SUCCESS);

				this.baseDao.save(tc);
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		} else if (headerType == 0x0301) {
			// 终端事件上报
			JT_0301 ack = (JT_0301) message.getMessageContents();
			int eventId = ack.getEventId();

			TerminalCommand tc = new TerminalCommand();
			tc.setCmdType(headerType);
			tc.setSN(message.getHeader().getMessageSerialNo());
			tc.setSimNo(message.getSimNo());
			tc.setUpdateDate(new Date());
			VehicleData vd = realDataService.getVehicleData(tc.getSimNo());
			if (vd != null) {
				tc.setPlateNo(vd.getPlateNo());
				tc.setVehicleId(vd.getEntityId());
			}
			String eventContent = this.commandService.getEventContent(eventId);
			if (eventContent == null) {
				eventContent = "该事件ID不存在";
			} else
				eventContent = "事件内容:" + eventContent;
			tc.setCmdData("事件ID是:" + eventId + "," + eventContent);
			tc.setOwner(TerminalCommand.FROM_TERMINAL);
			tc.setStatus(TerminalCommand.STATUS_SUCCESS);

			this.baseDao.save(tc);
		} else if (headerType == 0x0900) {
			// 终端对平台的上行透传
			JT_0900 ack = (JT_0900) message.getMessageContents();
			int msgType = ack.getMessageType();
			String msg = new String(ack.getMessageContent());

			TerminalCommand tc = new TerminalCommand();
			tc.setCmdType(0x0900);
			tc.setSN(message.getHeader().getMessageSerialNo());
			tc.setSimNo(message.getSimNo());
			tc.setUpdateDate(new Date());
			VehicleData vd = realDataService.getVehicleData(tc.getSimNo());
			if (vd != null) {
				tc.setPlateNo(vd.getPlateNo());
				tc.setVehicleId(vd.getEntityId());
			}
			tc.setCmdData("透传类型是:" + msgType + ",透传数据：" + msg);
			tc.setOwner("");
			tc.setStatus(TerminalCommand.STATUS_SUCCESS);

			this.baseDao.save(tc);

		} else {
			logger.error("未知的处理类型:" + Integer.toHexString(headerType));
		}

	}

	/**
	 * 位置信息包处理
	 * @param message
	 * @param gpsInfoList
	 */
	private void processLocationPacket(T808Message message, List<GpsInfo> gpsInfoList) {
		int headerType = message.getHeader().getMessageType();
		JT_0200 jvi = null;
		if (headerType == 0x0200) {
			jvi = (JT_0200) message.getMessageContents();
		} else if (headerType == 0x0201) {
			JT_0201 jt = (JT_0201) message.getMessageContents();
			jvi = jt.getPositionReport();
		}
		//位置信息
		GpsInfo gi = new GpsInfo();
		gi.setPlateNo(message.getPlateNo());
		gi.setSimNo(message.getHeader().getSimId());
		gi.setLatitude(0.000001 * jvi.getLatitude());
		gi.setLongitude(0.000001 * jvi.getLongitude());
		short speed = jvi.getSpeed();
		gi.setVelocity(0.1 * speed);
		short direction = jvi.getCourse();
		gi.setDirection(direction);
		gi.setStatus(jvi.getStatus());
		gi.setAlarmState(jvi.getAlarmFlag());
		gi.setMileage(0.1 * jvi.getMileage());
		gi.setGas(0.1 * jvi.getOil());
		gi.setRecordVelocity(0.1 * jvi.getRecorderSpeed());
		gi.setValid(jvi.isValid());
		gi.setAltitude(jvi.getAltitude());
		gi.setRecordVelocity(0.1 * jvi.getRecorderSpeed());
		gi.setSignal(jvi.getSignal());

		Date dt = DateUtil.stringToDatetime(jvi.getTime(), "yyyy-MM-dd HH:mm:ss");
		if (dt == null) {
			logger.error(gi.getSimNo() + "," + message.getPlateNo()
					+ "定位包无效的日期:" + jvi.getTime());
			return; // 对于无效日期的包，是否丢弃?
		}
		gi.setSendTime(dt);
		Date now = new Date();
		// 发送的旧数据,无效乱数据
		if (gi.getSendTime().getYear() != now.getYear()) // vi.SendTime.CompareTo(rd.SendTime)
		// <= 0
		{
			// logger.error("接受到发送的旧数据:" + rd.getPlateNo() + ",发送时间:" +
			// vi.SendTime.ToString("yyyy-MM-dd hh:mm:ss"));
			return;
		}
		GPSRealData rd = realDataService.get(gi.getSimNo());// getRealDataBySimNo(gi.getSimNo());
		if (rd == null) {
			// logger.error("发现没有注册的车辆, simNo:" + gi.getSimNo());
			return;
		}
		rd.setResponseSn((int) (message.getHeader().getMessageSerialNo()));

		// 附加扩展位的解析
		if (jvi.getInOutAreaAlarmAdditional() != null) {
			// 进出区域报警
			PostitionAdditional_InOutAreaAlarmAdditional additional = jvi
					.getInOutAreaAlarmAdditional();
			rd.setEnclosureAlarm(additional.getDirection());
			rd.setEnclosureId(additional.getAreaId());
			rd.setEnclosureType(additional.getPositionType());
		} else {
			// 区域报警清空
			rd.setEnclosureAlarm(0);
			rd.setEnclosureId(0);
			rd.setEnclosureType(0);
		}
		if (jvi.getOverSpeedAlarmAdditional() != null) {
			// 超速报警
			PostitionAdditional_OverSpeedAlarmAdditional additional = jvi
					.getOverSpeedAlarmAdditional();
			rd.setOverSpeedAreaId(additional.getAreaId());
			rd.setOverSpeedAreaType(additional.getPositionType());
		} else {
			// 区域报警清空
			rd.setOverSpeedAreaId(0);
			rd.setOverSpeedAreaId(0);
		}
		if (jvi.getRouteTimeAlarmAdditional() != null) {
			// 路段报警
			PostitionAdditional_RouteDriveTimeAlarmAdditional additional = jvi
					.getRouteTimeAlarmAdditional();
			rd.setRouteAlarmType(additional.getResult());
			rd.setRouteSegmentId(additional.getRouteId());
			rd.setRunTimeOnRoute(additional.getDriveTime());
		} else {
			// 路段报警清空
			rd.setRouteAlarmType(0);
			rd.setRouteSegmentId(0);
			rd.setRunTimeOnRoute(0);
		}
		gi.setPlateNo(rd.getPlateNo());
		gi.setLocation(rd.getLocation());
		message.setPlateNo(rd.getPlateNo());
		rd.setSendTime(gi.getSendTime());
		String oldStatus = rd.getStatus();
		// String newStatus = vi.getStatus();
		String oldAlarmState = rd.getAlarmState();
		// String newAlarmState = vi.getAlarmState();
		try {
			if (gi.getLatitude() > 0 && gi.getLongitude() > 0) {
				// 保证是有效坐标
				rd.setLatitude(gi.getLatitude());
				rd.setLongitude(gi.getLongitude());
			}
			rd.setVelocity(gi.getVelocity());
			rd.setSignal(gi.getSignal());
			rd.setDirection(gi.getDirection());
			rd.setStatus(jvi.getStrStatus());
			rd.setAlarmState(jvi.getStrWarn());
			rd.setMileage(gi.getMileage());
			rd.setVelocity(gi.getVelocity());
			rd.setRecordVelocity(gi.getRecordVelocity());
			rd.setSendTime(gi.getSendTime());
			rd.setAltitude(gi.getAltitude());
			rd.setValid(jvi.isValid());
			rd.setGas(gi.getGas());
			rd.setOnline(true);	//设置车辆在线
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		try {
			transfer(rd, gi); // 转发服务
			alarmService.processRealData(rd);// 报警服务
			if (Tools.isNullOrEmpty(rd.getPlateNo()) == false) {
				//	更新车辆GPS实时信息
				realDataService.update(rd);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		// 保存定位数据到历史轨迹表中
		try {
			gi.setPlateNo(rd.getPlateNo()); // 保持车牌号一致
			// getBaseDao().save(gi);

			// 非法日期需要过滤掉，因为系统还没生成这个表
			Date today = new Date();
			Date nextDay = DateUtil.getDate(today, 1);
			if (gi.getSendTime().compareTo(nextDay) < 0) {
				gpsInfoList.add(gi);
			}

		} catch (RuntimeException ex) {
			logger.error(ex.getMessage());
			logger.error(ex.getStackTrace());
		}
	}

	private void processBatchLocationPacket(String simNo, JT_0200 jvi,
			List<GpsInfo> gpsInfoList) {
		GpsInfo gi = new GpsInfo();

		gi.setSimNo(simNo);
		gi.setLatitude(0.000001 * jvi.getLatitude());
		gi.setLongitude(0.000001 * jvi.getLongitude());
		short speed = jvi.getSpeed();
		gi.setVelocity(0.1 * speed);
		short direction = jvi.getCourse();
		gi.setDirection(direction);
		gi.setStatus(jvi.getStatus());
		gi.setAlarmState(jvi.getAlarmFlag());
		gi.setMileage(0.1 * jvi.getMileage());
		gi.setGas(0.1 * jvi.getOil());
		gi.setRecordVelocity(0.1 * jvi.getRecorderSpeed());
		gi.setValid(jvi.isValid());
		gi.setAltitude(jvi.getAltitude());
		gi.setRecordVelocity(0.1 * jvi.getRecorderSpeed());

		Date dt = DateUtil.stringToDatetime(jvi.getTime(),
				"yyyy-MM-dd HH:mm:ss");
		if (dt == null) {
			// logger.error(gi.getSimNo() + "," + message.getPlateNo()
			// + "定位包无效的日期:" + jvi.getTime());
			return; // 对于无效日期的包，是否丢弃?
		}

		gi.setSendTime(dt);

		java.util.Date now = new java.util.Date();

		GPSRealData rd = realDataService.get(gi.getSimNo());// getRealDataBySimNo(gi.getSimNo());
		if (rd == null) {
			// logger.error("发现没有注册的车辆, simNo:" + gi.getSimNo());
			// return;
		} else
			gi.setPlateNo(rd.getPlateNo());
		// message.setPlateNo(rd.getPlateNo());

		// 保存定位数据到历史轨迹表中
		try {
			Date today = new Date();
			Date nextDay = DateUtil.getDate(today, 1);
			if (gi.getSendTime().compareTo(nextDay) < 0) {
				gpsInfoList.add(gi);

			}
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage());
			logger.error(ex.getStackTrace());
		}
	}

	/**
	 * 电子运单数据入库
	 * 
	 * @param msg
	 */
	private void saveEWayBill(T808Message msg) {

		JT_0701 rd = (JT_0701) msg.getMessageContents();
		VehicleData vd = realDataService.getVehicleData(msg.getSimNo());
		if (vd != null) {
			EWayBill ebill = new EWayBill();
			ebill.seteContent(rd.getElectronicFreightContent());
			ebill.setPlateNo(vd.getPlateNo());
			ebill.setVehicleId(vd.getEntityId());

			// 转发电子运单
			this.transferGpsService.transfer(ebill);
			this.baseDao.save(ebill);
		}

	}

	/**
	 * 驾驶员上报信息，入库，同时转发给上级平台
	 * 
	 * @param msg
	 */
	private void saveDriver(T808Message msg) {

		JT_0702 driverData = (JT_0702) msg.getMessageContents();
		JT_0702_old rd = driverData.getOldDriverData();
		VehicleData vd = realDataService.getVehicleData(msg.getSimNo());
		if(vd == null)
			return;

		String hql = "from DriverInfo where vehicleId = ? and deleted = ?";
		DriverInfo driver = (DriverInfo) this.baseDao.find(hql, new Object[] {
				vd.getEntityId(), false });
		if (driver == null) {
			driver = new DriverInfo();
			driver.setVehicleId(vd.getEntityId());
//			driver.setVehicleId(vd.getEntityId());
		}
		if (vd != null) {
			if (rd != null) {
				try {
					driver.setDriverName(rd.getDriverName());
					driver.setLicenseAgency(rd.getAgencyName());// 发证机构
					driver.setDriverLicence(rd.getCertificationCode());// 从业资格证编码
					driver.setIdentityCard(rd.getDriverID());// 身份证编码
					this.baseDao.saveOrUpdate(driver);
				} catch (Exception ex) {
					logger.error(ex.getMessage(), ex);
				}

				DriverModel d = new DriverModel();
				d.setPlateNo(vd.getPlateNo());
				d.setPlateColor(vd.getPlateColor());
				d.setDriverId(rd.getDriverID());
				d.setDriverName(rd.getDriverName());
				d.setLicense(rd.getCertificationCode());
				d.setOrgName(rd.getAgencyName());
				// 主动上报驾驶员信息
				this.transferGpsService.transferDriverInfo(d);
			} else {
				try {
					driver.setDriverName(driverData.getDriverName());
					driver.setLicenseAgency(driverData.getAgencyName());// 发证机构
					driver.setDriverLicence(driverData.getCertificationCode());// 从业资格证编码
					// driver.setIdentityCard(driverData.getDriverID());// 身份证编码
					this.baseDao.saveOrUpdate(driver);
				} catch (Exception ex) {
					logger.error(ex.getMessage(), ex);
				}

				try {
					DriverCardRecord r = new DriverCardRecord();
					r.setAgencyName(driverData.getAgencyName());
					r.setCardState(driverData.getCardState());
					r.setCertificationCode(driverData.getCertificationCode());
					r.setDriverName(driverData.getDriverName());
					r.setReadResult(driverData.getReadResult());
					r.setVehicleId(vd.getEntityId());
					Date operTime = DateUtil.toDateByFormat(
							driverData.getOperTime(), "yyyyMMddhhmmss");
					r.setOperTime(operTime);
					r.setValidateDate(driverData.getValidateDate());
					this.baseDao.saveOrUpdate(r);
				} catch (Exception ex) {
					logger.error(ex.getMessage(), ex);
				}

				DriverModel d = new DriverModel();
				d.setPlateNo(vd.getPlateNo());
				d.setPlateColor(vd.getPlateColor());
				d.setDriverId("410105197709212130");
				d.setDriverName(driverData.getDriverName());
				d.setLicense(driverData.getCertificationCode());
				d.setOrgName(driverData.getAgencyName());
				// 主动上报驾驶员信息
				this.transferGpsService.transferDriverInfo(d);

			}
			// this.baseDao.save(rd);
		}

	}

	// *
	// * 保存行车记录仪采集上来的数据
	//
	// private HashMap msgMap = new HashMap();
	// T808Message mymsg = null;
	// List<Byte> bytes = new ArrayList<Byte>();

	private void SaveVehicleRecorder(T808Message msg) {
		logger.error("是否分包:" + msg.getHeader().getIsPackage() + ",分包号:"
				+ msg.getHeader().getMessagePacketNo() + ",分包:"
				+ msg.getHeader().getMessageTotalPacketsCount());

		JT_0700 rd = (JT_0700) msg.getMessageContents();
//		T808MessageHeader th = msg.getHeader();
		VehicleData vd = realDataService.getVehicleData(msg.getSimNo());

		if (vd == null)
			return;
		if (rd == null)
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
		logger.error(cmdWord + ',' + recorderData.toString());
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
		// if (cmdWord == 0x01)
		// {
		// Recorder_DriverVehicleCode dv =
		// (Recorder_DriverVehicleCode)recorderData;
		// vr.DriverCode = dv.DriverCode;
		// vr.DriverLicense = dv.DriverLicenseNo;
		// }
		// else if (cmdWord == 0x02)
		// {
		// Recorder_RealTimeClock dv = (Recorder_RealTimeClock)recorderData;
		// vr.DeviceClock = dv.RealTimeClock;
		// }
		// else if (cmdWord == 0x03)
		// {
		// Recorder_MileageIn360Hours dv =
		// (Recorder_MileageIn360Hours)recorderData;
		// vr.MileageIn360h = dv.Mileage;
		// }
		// else if (cmdWord == 0x04)
		// {
		// Recorder_FeatureFactor dv = (Recorder_FeatureFactor)recorderData;
		//
		// }
		// else if (cmdWord == 0x05)
		// {
		// Recorder_SpeedIn360Hours dv = (Recorder_SpeedIn360Hours)recorderData;
		// //
		// }
		//
		// else if (cmdWord == 0x07)
		// {
		// Recorder_DoubtfulPointData dv =
		// (Recorder_DoubtfulPointData)recorderData;
		// //
		// }
		// else if (cmdWord == 0x08)
		// {
		// Recorder_VehicleLicenseInfo dv =
		// (Recorder_VehicleLicenseInfo)recorderData;
		// //
		// }
		//
		//
	}

	// *
	// * 接收上传的媒体数据
	//
	// private String mediaFileName = "";
	// private int offset = 0;

	private void SaveMedia(T808Message msg) {
		T808MessageHeader header = msg.getHeader();
		logger.error("收到数据包,流水号:" + msg.getHeader().getMessageSerialNo()
				+ "，包号:" + header.getMessagePacketNo() + ",总包数:"
				+ msg.getHeader().getMessageTotalPacketsCount() + ",长度:"
				+ msg.getHeader().getMessageSize());
		if (msg.getMessageContents() == null)
			return;

		// 如果是第一个分包就正常解析;
		JT_0801 uploadMediaMsg = (JT_0801) msg.getMessageContents();
		if (uploadMediaMsg == null) {
			logger.error("媒体数据解析错误，没有收到数据包");
			return;
		}
		// 查询是否有最近的命令下发
		TerminalCommand tc = this.commandService.getLatestCommand(
				JT808Constants.CMD_TAKE_PHOTO, msg.getSimNo());
		if (tc != null) {
			tc.setStatus(TerminalCommand.STATUS_SUCCESS);
			baseDao.saveOrUpdate(tc);
		} else {
			logger.error("拍照应答没找到下发指令,");
		}
		VehicleData vd = realDataService.getVehicleData(msg.getSimNo());
		if (tc != null
				&& TerminalCommand.FROM_GOV.equals(tc.getOwner()) == true) {
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
				// Image im = ImageIO.read(bs);
				// ImageObserver io = new ImageObserver();
				// int height = im.getHeight(null);
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}

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

		if (vd != null)
			mi.setPlateNo(vd.getPlateNo());
		else
			mi.setPlateNo(msg.getPlateNo());

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
		WriteFile(fullFileName, uploadMediaMsg.getMultimediaData(),
				uploadMediaMsg.getMultimediaData().length);

	}

	private void WriteFile(String fileName, byte[] mediaData, int length) {
		FileOutputStream pFileStream = null;
		try {
			pFileStream = new FileOutputStream(fileName, true);
			pFileStream.write(mediaData, 0, length);
			// offset += length;
			// mediaFileName = fileName;
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

	// *
	// * 接收上传的媒体信息数据，是终端自动发送
	//
	private void SaveMediaInfo(T808Message msg) {
		JT_0800 uploadMediaMsg = (JT_0800) msg.getMessageContents();

		MediaItem mi = new MediaItem();
		mi.setCodeFormat(uploadMediaMsg.getMultidediaCodeFormat());
		mi.setMediaDataId((int) uploadMediaMsg.getMultimediaDataId());
		mi.setMediaType(uploadMediaMsg.getMultimediaType());
		mi.setChannelId(uploadMediaMsg.getChannelId());
		mi.setEventCode(uploadMediaMsg.getEventCode());
		mi.setSimNo(msg.getHeader().getSimId());
		mi.setPlateNo(msg.getPlateNo());
		mi.setCommandType(MediaItem.INFO); // 根据上传命令的应答

		try {
			// 记录下上传的文件列表，便于检索
			getBaseDao().saveOrUpdate(mi);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage());
			logger.error(ex.getStackTrace());
		}
	}

	// *
	// * 接收终端返回的媒体媒体检索数据
	//
	private void SaveMediaSearchItem(T808Message msg) {
		JT_0802 msgData = (JT_0802) msg.getMessageContents();
		// 得到返回的响应号
		int sn = msgData.getResponseMessageSerialNo();
		// 更新下发命令的返回状态，确定终端已成功应答
		TerminalCommand tc = commandService.getCommandBySn(sn);// (TerminalCommand)this.baseDao.find(hsql,
																// resNo);
		int commandId = 0;
		if (tc != null) {
			commandId = tc.getEntityId();
			commandService.UpdateStatus(msg.getSimNo(), sn,
					TerminalCommand.STATUS_SUCCESS);
		}

		for (MuldimediaSearchDataItem uploadMediaMsg : msgData.getDataItems()) {
			MediaItem mi = new MediaItem();
			mi.setMediaDataId((int) uploadMediaMsg.getMultimediaId());
			mi.setMediaType(uploadMediaMsg.getMultimediaType());
			mi.setChannelId(uploadMediaMsg.getChannelId());
			mi.setEventCode(uploadMediaMsg.getEventCode());
			mi.setSimNo(msg.getHeader().getSimId());
			mi.setCommandId(commandId);
			mi.setPlateNo(msg.getPlateNo());
			// mi.setCommandId(tc.getEntityId()); //记录下命令ID，可以根据命令来查询应答
			Date dt = DateUtil.stringToDatetime(uploadMediaMsg.getPosition()
					.getTime(), "yyyyMMddHHmmss");
			if (dt == null) {
				dt = new Date();
				logger.error("媒体数据时间解析错误"
						+ uploadMediaMsg.getPosition().getTime());
			}
			mi.setSendTime(dt); // 取自位置时间

			mi.setCommandType(MediaItem.UPLOAD); // 根据上传命令的应答
			try {
				// 记录下上传的文件列表，便于检索
				getBaseDao().saveOrUpdate(mi);
			} catch (RuntimeException ex) {
				logger.error(ex.getMessage());
				logger.error(ex.getStackTrace());
			}
		}
	}

	public IAlarmService getAlarmService() {
		return alarmService;
	}

	public void setAlarmService(IAlarmService alarmService) {
		this.alarmService = alarmService;
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

	public IBaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(IBaseService baseService) {
		this.baseService = baseService;
	}

	public DaoIbatisImpl getQueryDao() {
		return queryDao;
	}

	public void setQueryDao(DaoIbatisImpl queryDao) {
		this.queryDao = queryDao;
	}

	public IRealDataService getRealDataService() {
		return realDataService;
	}

	public void setRealDataService(IRealDataService realDataService) {
		this.realDataService = realDataService;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public Boolean getEnableSaveDb() {
		return enableSaveDb;
	}

	public void setEnableSaveDb(Boolean enableSaveDb) {
		this.enableSaveDb = enableSaveDb;
	}

	public IMediaService getMediaService() {
		return mediaService;
	}

	public void setMediaService(IMediaService mediaService) {
		this.mediaService = mediaService;
	}

}