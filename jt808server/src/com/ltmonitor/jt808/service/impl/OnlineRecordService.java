package com.ltmonitor.jt808.service.impl;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.entity.Alarm;
import com.ltmonitor.entity.AlarmRecord;
import com.ltmonitor.entity.GPSRealData;
import com.ltmonitor.entity.OnlineRecord;
import com.ltmonitor.entity.StringUtil;
import com.ltmonitor.jt808.service.IAlarmService;
import com.ltmonitor.jt808.service.INewAlarmService;
import com.ltmonitor.jt808.service.IOnlineRecordService;
import com.ltmonitor.service.ILocationService;
import com.ltmonitor.util.DateUtil;

/**
 * 上线记录和下线记录
 * 
 * @author DELL
 * 
 */
public class OnlineRecordService implements IOnlineRecordService {
	private IBaseDao baseDao;
	private ILocationService locationService;

	private Thread processRealDataThread;
	private IAlarmService alarmService;
	private static Logger logger = Logger.getLogger(OnlineRecordService.class);
	private ConcurrentLinkedQueue<OnlineRecord> dataQueue = new ConcurrentLinkedQueue<OnlineRecord>();
	/** 
	 * 终端在线集合:simNo
	 */
	private ConcurrentMap<String, Boolean> onlineMap = new ConcurrentHashMap<String, Boolean>();
	/**
	 * 服务器各个连接的状态
	 */
	private ConcurrentMap<String, Boolean> connectedStateMap = new ConcurrentHashMap<String, Boolean>();

	private INewAlarmService newAlarmService;

	private boolean startAnalyze = true;

	public OnlineRecordService() {
		processRealDataThread = new Thread(new Runnable() {
			public void run() {
				processRealDataThreadFunc();
			}
		});

		processRealDataThread.start();

	}

	@Override
	public void stopService() {
		startAnalyze = false;
		try {
			processRealDataThread.join(50000);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		// processRealDataThread.stop();
	}

	private void processRealDataThreadFunc() {
		while (startAnalyze) {
			try {
				OnlineRecord tm = dataQueue.poll();
				while (tm != null) {
					try {
						Date start = new Date();
						createOnlineChangeRecord(tm);
						String alarmType = tm.getChildType().equals(
								OnlineRecord.TYPE_ONLINE) ? OnlineRecord.TYPE_OFFLINE
								: OnlineRecord.TYPE_ONLINE;
						tm.setChildType(alarmType);
						tm.setStatus(OnlineRecord.STATUS_OLD);
						createOnlineChangeRecord(tm);

						Date end = new Date();
						double seconds = DateUtil.getSeconds(start, end);
						if (seconds > 2)
							logger.error("创建记录耗时:" + seconds);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}

					tm = dataQueue.poll();
				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			try {
				Thread.sleep(500L);
			} catch (InterruptedException e1) {
			}
		}
	}

	public void UpdateConnectedState(String simNo, Boolean isConnected) {
		this.connectedStateMap.put(simNo, isConnected);
	}

	public Boolean isConnected(String simNo) {
		if (connectedStateMap.containsKey(simNo)) {
			return connectedStateMap.get(simNo);
		}
		return false;
	}

	public void checkOnline(GPSRealData rd) {
		boolean oldState = false;
		if (onlineMap.containsKey(rd.getSimNo())) {
			oldState = onlineMap.get(rd.getSimNo());
		}
		boolean online = rd.getOnline();
		OnlineRecord r = new OnlineRecord();
		r.setPlateNo(rd.getPlateNo());
		r.setLatitude(rd.getLatitude());
		r.setLongitude(rd.getLongitude());
		r.setStartTime(rd.getOnlineDate());
		r.setLocation(rd.getLocation());
		r.setVehicleId(rd.getVehicleId());
		r.setVelocity(rd.getVelocity());
		String alarmType = rd.getOnline() ? OnlineRecord.TYPE_ONLINE : OnlineRecord.TYPE_OFFLINE;
		r.setChildType(alarmType);
		//终端上线、下线报警
		if (alarmService.isAlarmEnabled(alarmType,AlarmRecord.ALARM_FROM_PLATFORM)) {
			this.newAlarmService.insertAlarm(AlarmRecord.ALARM_FROM_PLATFORM,alarmType, rd);
			String hsql = "from AlarmRecord where vehicleId = ? and childType = ? and status = ?";
			AlarmRecord ar = (AlarmRecord) getBaseDao().find(hsql,new Object[]{rd.getVehicleId(),
					rd.getOnline() ? OnlineRecord.TYPE_OFFLINE : OnlineRecord.TYPE_ONLINE,
					OnlineRecord.STATUS_NEW});
			if(ar != null){
				//增加离线报警
				ar.setStatus(AlarmRecord.STATUS_OLD);
				ar.setEndTime(rd.getSendTime());
				ar.setLatitude1(rd.getLatitude());
				ar.setLongitude1(rd.getLongitude());
				double minutes = 0.1 * DateUtil.getSeconds(ar.getStartTime(),ar.getEndTime()) / 6;
				ar.setTimeSpan(minutes);// 计算出报警时长
				getBaseDao().saveOrUpdate(ar);
			} 
			ar = new AlarmRecord(rd, alarmType, OnlineRecord.ALARM_FROM_PLATFORM);
			getBaseDao().saveOrUpdate(ar);
		}

		if (dataQueue.size() > 2000) {
			logger.error("上线记录队列堵塞，堵塞队列:" + dataQueue.size());
			dataQueue.clear();
		}
		dataQueue.add(r);
		onlineMap.put(rd.getSimNo(), online);
		// }
	}

	/**
	 * 记录上线和下线的起止时间
	 */
	public void createOnlineChangeRecord(OnlineRecord rd) {
		String alarmType = rd.getChildType();
		String hsql = "from OnlineRecord rec where rec.plateNo = ? and rec.status = ? and rec.childType = ?";
		OnlineRecord sr = (OnlineRecord) getBaseDao().find(
				hsql,
				new String[] { rd.getPlateNo(), OnlineRecord.STATUS_NEW,
						alarmType });

		if (sr == null && OnlineRecord.STATUS_NEW.equals(rd.getStatus())) {
			// this.insertAlarm(OnlineRecord.ALARM_FROM_PLATFORM, alarmType,
			// rd);

			sr = new OnlineRecord();
			sr.setVehicleId(rd.getVehicleId());
			sr.setPlateNo(rd.getPlateNo());
			sr.setStartTime(rd.getStartTime());
			sr.setStatus(OnlineRecord.STATUS_NEW);
			sr.setEndTime(new Date());
			sr.setLatitude(rd.getLatitude());
			sr.setLongitude(rd.getLongitude());
			sr.setType(OnlineRecord.ALARM_FROM_PLATFORM);// 骞冲彴鎶ヨ

			String location = rd.getLocation();
			if (StringUtil.isNullOrEmpty(location))
				location = locationService.getLocation(sr.getLatitude(),
						sr.getLongitude());
			sr.setLocation(location);
			sr.setVelocity(rd.getVelocity());
			sr.setChildType(alarmType);
			getBaseDao().saveOrUpdate(sr);
		} else if (OnlineRecord.STATUS_OLD.equals(rd.getStatus()) && sr != null) {
			sr.setEndTime(rd.getStartTime());
			double t = DateUtil.getSeconds(sr.getStartTime(), sr.getEndTime()) / 60;
			sr.setTimeSpan(t);

			sr.setStatus(OnlineRecord.STATUS_OLD);
			// sr.setEndTime(new Date());// rd.SendTime;
			sr.setLatitude1(rd.getLatitude());
			sr.setLongitude1(rd.getLongitude());

			sr.setChildType(alarmType);
			getBaseDao().saveOrUpdate(sr);
		}
	}

	public IBaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(IBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public IAlarmService getAlarmService() {
		return alarmService;
	}

	public void setAlarmService(IAlarmService alarmService) {
		this.alarmService = alarmService;
	}

	public INewAlarmService getNewAlarmService() {
		return newAlarmService;
	}

	public void setNewAlarmService(INewAlarmService newAlarmService) {
		this.newAlarmService = newAlarmService;
	}

}
