package com.ltmonitor.jt808.service;

import com.ltmonitor.entity.AlarmRecord;
import com.ltmonitor.entity.GPSRealData;

public interface IAlarmService {

	//public abstract void updateOnlineTime(String simNo);
	boolean isAlarmEnabled(String alarmType, String alarmSource);

	public abstract void processRealData(GPSRealData rd);

	public abstract void stopService();
	
	public AlarmRecord CreateRecord(String alarmSource, String alarmType,
			String alarmState, GPSRealData rd);

}