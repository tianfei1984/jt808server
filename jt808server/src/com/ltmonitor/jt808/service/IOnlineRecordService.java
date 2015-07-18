package com.ltmonitor.jt808.service;

import com.ltmonitor.entity.GPSRealData;

public interface IOnlineRecordService {

	public void checkOnline(GPSRealData rd);

	// 创建上线下线状�?变化的记录，记录变化的起止时间和间隔，及经纬度坐�?
	//public void createOnlineChangeRecord(GPSRealData rd, String alarmType);
	
	public void  stopService();
	

	public void UpdateConnectedState(String simNo, Boolean isConnected);
	
	public Boolean isConnected(String simNo);

}