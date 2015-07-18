package com.ltmonitor.jt808.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.entity.Alarm;
import com.ltmonitor.entity.AlarmRecord;
import com.ltmonitor.entity.Enclosure;
import com.ltmonitor.entity.GPSRealData;
import com.ltmonitor.entity.StringUtil;
import com.ltmonitor.jt808.service.AlarmItem;
import com.ltmonitor.jt808.service.IAlarmService;
import com.ltmonitor.jt808.service.IAreaAlarmService;
import com.ltmonitor.jt808.service.ITransferGpsService;
import com.ltmonitor.service.ILocationService;
import com.ltmonitor.util.DateUtil;

/**
 * 报警分析服务
 * 
 * @author DELL
 * 
 */
public class AlarmService  {

	private static Logger logger = Logger.getLogger(AlarmService.class);
	private ConcurrentLinkedQueue<GPSRealData> dataQueue = new ConcurrentLinkedQueue();

	public ConcurrentMap<String, GPSRealData> oldRealDataMap = new ConcurrentHashMap<String, GPSRealData>();
	private IBaseDao baseDao;

	private Thread processRealDataThread;

	// 内存中的保留的已经发生的报警
	private ConcurrentHashMap<String, AlarmItem> alarmMap = new ConcurrentHashMap<String, AlarmItem>();
	private Boolean startAnalyze = true;
	// 围栏报警服务
	private IAreaAlarmService areaAlarmService;

	private ITransferGpsService transferGpsService;

	private ILocationService locationService;

	private boolean parkingAlarmEnabled;

	public AlarmService() {
		processRealDataThread = new Thread(new Runnable() {
			public void run() {
				processRealDataThreadFunc();
			}
		});

		processRealDataThread.start();

	}

	public void stopService() {
		startAnalyze = false;
		try {
			processRealDataThread.join(50000);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		// processRealDataThread.stop();
	}

	public void processRealData(GPSRealData rd) {
		// if (processRealDataThread.isAlive() == false) {
		// }
		dataQueue.add(rd);
	}

	private void processRealDataThreadFunc() {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
		while (startAnalyze) {
			try {
				GPSRealData tm = dataQueue.poll();
				final List<GPSRealData> msgList = new ArrayList<GPSRealData>();
				while (tm != null) {
					msgList.add(tm);
					if (msgList.size() > 30)
						break;
					tm = dataQueue.poll();
				}
				if (msgList.size() > 0) {
					fixedThreadPool.execute(new Runnable() {
						@Override
						public void run() {
							analyzeData(msgList);
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e1) {
			}
		}
	}

	private void analyzeData(List<GPSRealData> msgList) {

		for (GPSRealData msg : msgList) {
			try {
				analyzeData(msg);
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}

	}

	private void analyzeData(GPSRealData rd) {
		GPSRealData oldRd = GetOldRealData(rd.getSimNo());
		if (oldRd == null)
			return;

		try {
			String oldStatus = oldRd.getStatus();
			String newStatus = rd.getStatus();
			String oldAlarmState = oldRd.getAlarmState();
			String newAlarmState = rd.getAlarmState();
			createChangeRecord(AlarmRecord.STATE_FROM_TERM, oldStatus,
					newStatus, rd);
			createChangeRecord(AlarmRecord.ALARM_FROM_TERM, oldAlarmState,
					newAlarmState, rd);

			// 停车报警
			if (parkingAlarmEnabled)
				parkingAlarm(rd);
			
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		oldRd.setAlarmState(rd.getAlarmState());
		oldRd.setStatus(rd.getStatus());
		oldRd.setSendTime(rd.getSendTime());
		// 保存在缓存中，等待下一次数据比对
		/**
		 * GPSRealData oldRd = oldRealDataMap.get(rd.getSimNo()); if (oldRd ==
		 * null || oldRd.getOnline() == false) { // 上线记录
		 * this.createOnlineChangeRecord(rd); }
		 */

	}

	private void analyzeAlarm(GPSRealData rd, String alarmType,
			String alarmState) {
		String alarmKey = rd.getPlateNo() + "_" + AlarmRecord.TYPE_PARKING;
		if (alarmState.equals(AlarmRecord.TURN_ON)) {
			// 发生报警
			if (alarmMap.containsKey(alarmKey) == false) {
				// 第一次停车
				//AlarmItem item = new AlarmItem(alarmKey, rd.getSendTime());
				//this.CreateWarnRecord(AlarmRecord.ALARM_FROM_TERM, alarmType,
				//		alarmState, rd);
				//alarmMap.put(alarmKey, item);// 报警标志驻留在内存中，留待下次判断是否已经报警
			}
		} else if (alarmState.equals(AlarmRecord.TURN_OFF)) {
			// 报警结束,关闭报警
			if (alarmMap.containsKey(alarmKey)) {
				AlarmItem item = alarmMap.get(alarmKey); // 如果有产生的报警，则需要消除报警
				//item.setOpen(false);
				alarmMap.remove(alarmKey);
				this.CreateWarnRecord(AlarmRecord.ALARM_FROM_TERM, alarmType,
						alarmState, rd);
			}
		}
	}

	private void parkingAlarm(GPSRealData rd) {
		// 判断是否停车
		String alarmState = rd.getVelocity() < 1 ? AlarmRecord.TURN_ON : AlarmRecord.TURN_OFF;
		analyzeAlarm(rd, AlarmRecord.TYPE_PARKING, alarmState);
	}

	/**
	 * 报警插入到数据库中，等待推送到前台弹出报警
	 * 
	 * @param alarmType
	 * @param alarmSource
	 * @param rd
	 */
	public void insertAlarm(String alarmSource, String alarmType,
			GPSRealData rd) {
		try {
			Alarm ar = new Alarm();
			ar.setVehicleId(rd.getVehicleId());
			ar.setPlateNo(rd.getPlateNo());
			ar.setAlarmTime(rd.getSendTime());
			ar.setAckSn(rd.getResponseSn());// 保留终端消息的流水号，对终端下发报警解除时，需要此流水号解除报警
			ar.setLatitude(rd.getLatitude());
			ar.setLongitude(rd.getLongitude());
			ar.setSpeed(rd.getVelocity());
			ar.setAlarmType(alarmType);
			ar.setAlarmSource(alarmSource);
			ar.setLocation(rd.getLocation());
			this.baseDao.saveOrUpdate(ar);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 创建状态位变化的记录，记录变化的起止时间和间隔，及经纬度坐标
	 * 
	 * @param alarmSource
	 * @param oldStatus
	 * @param newStatus
	 * @param rd
	 */
	private void createChangeRecord(String alarmSource, String oldStatus,
			String newStatus, GPSRealData rd) {

		if (alarmSource.equals(AlarmRecord.ALARM_FROM_TERM)) {
			char[] newChars = (char[]) newStatus.toCharArray();
			for (int m = 0; m < newChars.length; m++) {
				String alarmState = "" + newChars[m];
				int alarmId = 31 - m; // 倒序，转换为部标的报警序号
				String alarmType = "" + alarmId;
				// 转发报警信息
				if (alarmState.equals("1")) {
					// 转发809报警
					if (this.transferGpsService.isTransferTo809Enabled()) {
						transferAlarm(alarmSource, alarmType, rd);
					}
					// 插入新的报警
					insertAlarm(alarmSource, alarmType, rd);
				}

			}
		}

		if (StringUtil.isNullOrEmpty(oldStatus)
				|| StringUtil.isNullOrEmpty(newStatus)
				|| oldStatus.length() != newStatus.length()
				|| oldStatus.equals(newStatus))
			return;

		char[] oldChars = (char[]) oldStatus.toCharArray();

		char[] newChars = (char[]) newStatus.toCharArray();

		for (int m = 0; m < oldChars.length; m++) {
			// 转发报警信息
			if (newChars[m] == 1
					&& alarmSource.equals(AlarmRecord.ALARM_FROM_TERM)
					&& this.transferGpsService.isTransferTo809Enabled())
				transferAlarm("" + m, alarmSource, rd);

			if (oldChars[m] != newChars[m]) {
				int alarmId = 31 - m; // 倒序，转换为部标的报警序号
				String alarmType = "" + alarmId;
				String alarmState = "" + newChars[m];

				if (alarmId == 20) {
					alarmType = rd.getEnclosureAlarm() == 0 ? AlarmRecord.TYPE_IN_AREA
							: AlarmRecord.TYPE_CROSS_BORDER;
					// alarmState = ""+rd.getEnclosureAlarm();
				} else if (alarmId == 21) {
					alarmType = rd.getEnclosureAlarm() == 0 ? AlarmRecord.TYPE_ON_ROUTE
							: AlarmRecord.TYPE_OFFSET_ROUTE;
					// alarmState = ""+rd.getEnclosureAlarm();
				}

				CreateWarnRecord(alarmSource, alarmType, alarmState, rd);
			}
		}
	}

	/**
	 * 创建报警记录 OperateType表示类型，状态位变化(State)还是报警标志位变化(Warn)， childType
	 * 表示标志位的字节32位序号, 如Acc标志位在32个状态位的第一个位置,超速报警在报警位的第二个位置. warnState “1”代表报警 ，
	 * 0代表报警消除
	 */
	private void CreateWarnRecord(String OperateType, String childType,
			String warnState, GPSRealData rd) {
		AlarmRecord sr = CreateRecord(OperateType, childType, warnState, rd);
		if (sr != null)
			getBaseDao().saveOrUpdate(sr);
	}

	/**
	 * 将报警转发给上级平台
	 */
	private void transferAlarm(String alarmSource, String alarmType,
			GPSRealData rd) {
		if (transferGpsService.isTransferTo809Enabled() == false)
			return;
		AlarmRecord ar = new AlarmRecord();
		ar.setPlateNo(rd.getPlateNo());
		ar.setStartTime(rd.getSendTime());
		ar.setChildType(alarmType);
		ar.setType(alarmSource);
		ar.setVehicleId(rd.getVehicleId());
		this.transferGpsService.transfer(ar, rd);
	}

	public AlarmRecord CreateRecord(String alarmSource, String alarmType,
			String alarmState, GPSRealData rd) {
		String hsql = "from AlarmRecord rec where startTime > ? and  rec.plateNo = ? and rec.status = ? and rec.type = ? and rec.childType = ?";
		// 查看是否有未消除的报警记录
		Date startDate = DateUtil.getDate(new Date(), Calendar.DAY_OF_YEAR, -5);
		AlarmRecord sr = (AlarmRecord) getBaseDao().find(
				hsql,
				new Object[] { startDate, rd.getPlateNo(),
						AlarmRecord.STATUS_NEW, alarmSource, alarmType });

		if (sr == null) {
			if (AlarmRecord.TURN_OFF.equals(alarmState))
				return null;

			sr = new AlarmRecord();
			// 停车报警
			if (alarmType.equals("19")) {
				/**
				 * Enclosure ec = IsInEnclosure(rd); if (ec != null) {
				 * sr.Station = ec.Name; sr.Location = ec.Name; }
				 */
			}

			if (rd.getEnclosureId() > 0) {
				String hql = "from Enclosure where enclosureId = ?";
				Enclosure ec = (Enclosure) baseDao.find(hql,
						rd.getEnclosureId());
				if (ec != null) {
					sr.setLocation(ec.getName());
				}

			}
			sr.setVehicleId(rd.getVehicleId());
			sr.setType(alarmSource);
			sr.setPlateNo(rd.getPlateNo());
			sr.setStartTime(rd.getSendTime());
			sr.setStatus(AlarmRecord.STATUS_NEW);
			sr.setEndTime(new Date());
			sr.setLatitude(rd.getLatitude());
			sr.setLongitude(rd.getLongitude());
			String location = rd.getLocation();
			if (StringUtil.isNullOrEmpty(location))
				location = locationService.getLocation(sr.getLatitude(),
						sr.getLongitude());
			sr.setLocation(location);
			sr.setVelocity(rd.getVelocity());
			sr.setChildType(alarmType);
			sr.setResponseSn(rd.getResponseSn());

		} else {
			sr.setEndTime(new Date());
			double minutes = DateUtil.getSeconds(sr.getStartTime(),
					rd.getSendTime()) / 60;
			sr.setTimeSpan(minutes);// 计算出报警时长
			if (alarmState.equals(AlarmRecord.TURN_OFF)) {
				sr.setStatus(AlarmRecord.STATUS_OLD);
				sr.setEndTime(rd.getSendTime());

				sr.setLatitude1(rd.getLatitude());
				sr.setLongitude1(rd.getLongitude());
			} else
				return null;

		}

		sr.setType(alarmSource);
		sr.setChildType(alarmType);
		return sr;
	}

	private GPSRealData getRalData(String plateNo) {
		GPSRealData rd = oldRealDataMap.get(plateNo);
		if (rd == null) {
			rd = new GPSRealData();
			rd.setOnline(false);
		}
		return rd;
	}


	/**
	 * 从缓冲中取出旧的实时数据，进行比对，分析报警状态位和上线状态的变化
	 */
	public GPSRealData GetOldRealData(String simNo) {
		GPSRealData oldRd = null;
		if (oldRealDataMap.containsKey(simNo)) {
			oldRd = (GPSRealData) oldRealDataMap.get(simNo);
		}

		if (oldRd == null) {
			oldRd = new GPSRealData();
			oldRd.setOnline(false);
			oldRd.setAlarmState(String.format("%032d", 0));
			oldRd.setStatus(String.format("%032d", 0));
			oldRd.setSendTime(new Date());
			oldRd.setSimNo(simNo);
			oldRealDataMap.put(simNo, oldRd);
		}

		return oldRd;
	}

	public IBaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(IBaseDao baseDao) {
		this.baseDao = baseDao;
	}


	public ITransferGpsService getTransferGpsService() {
		return transferGpsService;
	}

	public void setTransferGpsService(ITransferGpsService transferGpsService) {
		this.transferGpsService = transferGpsService;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public boolean isParkingAlarmEnabled() {
		return parkingAlarmEnabled;
	}

	public void setParkingAlarmEnabled(boolean parkingAlarmEnabled) {
		this.parkingAlarmEnabled = parkingAlarmEnabled;
	}

	public IAreaAlarmService getAreaAlarmService() {
		return areaAlarmService;
	}

	public void setAreaAlarmService(IAreaAlarmService areaAlarmService) {
		this.areaAlarmService = areaAlarmService;
	}

}
