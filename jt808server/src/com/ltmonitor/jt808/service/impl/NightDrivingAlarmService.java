package com.ltmonitor.jt808.service.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.entity.Alarm;
import com.ltmonitor.entity.AlarmRecord;
import com.ltmonitor.entity.GPSRealData;
import com.ltmonitor.entity.VehicleData;
import com.ltmonitor.jt808.service.AlarmItem;
import com.ltmonitor.jt808.service.IAlarmService;
import com.ltmonitor.jt808.service.INightDrivingAlarmService;
import com.ltmonitor.service.IRealDataService;
import com.ltmonitor.service.IVehicleService;

public class NightDrivingAlarmService implements INightDrivingAlarmService {
	
	private IRealDataService realDataService;

	private IVehicleService vehicleService;
	/**
	 * 最低行驶速度，超过此速度视为行驶，低于此速度，视为停车
	 */
	private int minSpeed = 2;
	/**
	 * 只检测某一车型的车辆
	 */
	private String vehicleType;

	private IBaseDao baseDao;

	private IAlarmService alarmService;
	

	private ConcurrentHashMap<String, AlarmItem> alarmMap = new ConcurrentHashMap<String, AlarmItem>();

	private static Logger logger = Logger.getLogger(AlarmService.class);

	public void checkNightDrivingVehicle() {
		logger.error("检测夜间行驶");
		try {
			List<GPSRealData> ls = realDataService.getOnlineRealDataList();
			for (GPSRealData rd : ls) {
				String alarmKey = rd.getPlateNo() + "_"
						+ AlarmRecord.TYPE_NIGHT_DRIVING;

				if (rd.getVelocity() < minSpeed || rd.getOnline() == false) {
					if (alarmMap.containsKey(alarmKey))
					{
						this.alarmService.CreateRecord(
								AlarmRecord.ALARM_FROM_PLATFORM,
								AlarmRecord.TYPE_NIGHT_DRIVING, AlarmRecord.TURN_OFF, rd);
					}
					continue;// 在停车中，不进行报警分析
				}
				VehicleData vd = vehicleService.getVehicleData(rd.getVehicleId());
				if (vd != null && getVehicleType().equals(getVehicleType())) {
					insertAlarm(AlarmRecord.ALARM_FROM_PLATFORM,
							AlarmRecord.TYPE_NIGHT_DRIVING,  rd);
					// 发生报警
					if (alarmMap.containsKey(alarmKey) == false) {
						this.alarmService.CreateRecord(
								AlarmRecord.ALARM_FROM_PLATFORM,
								AlarmRecord.TYPE_NIGHT_DRIVING, AlarmRecord.TURN_ON, rd);

					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	/**
	 * 报警插入到数据库中，等待推送到前台弹出报警
	 * 
	 * @param alarmType
	 * @param alarmSource
	 * @param rd
	 */
	private void insertAlarm(String alarmSource, String alarmType,
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

	public IVehicleService getVehicleService() {
		return vehicleService;
	}

	public void setVehicleService(IVehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public int getMinSpeed() {
		return minSpeed;
	}

	public void setMinSpeed(int minSpeed) {
		this.minSpeed = minSpeed;
	}

	public IBaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(IBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public IAlarmService getAlarmService() {
		return alarmService;
	}

	public void setAlarmService(IAlarmService alarmService) {
		this.alarmService = alarmService;
	}

	public IRealDataService getRealDataService() {
		return realDataService;
	}

	public void setRealDataService(IRealDataService realDataService) {
		this.realDataService = realDataService;
	}

}
