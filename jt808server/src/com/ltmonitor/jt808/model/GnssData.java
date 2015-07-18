package com.ltmonitor.jt808.model;

import java.util.Date;

/**
 * 定位数据
 * 适用于 定位数据交换 和拍照数据
 * @author DELL
 * 
 */
public class GnssData {
	//车牌号
	private String plateNo;
	//车牌颜色
	private int plateColor;
	// 经纬度
	private int latitude;

	private int longitude;

	// GPS速度
	private int gpsSpeed;

	// 行车记录仪速度
	private int recSpeed;

	// 总里程
	private int totalMileage;
	// 方向
	private int direction;

	// 海拔
	private int altitude;

	// 车辆状态
	private int vehicleState;
	// 报警状态
	private int alarmState;
	// 定位时间
	private Date PosTime;

	// 是否纠偏
	private int PosEncrypt;

	public GnssData() {
		plateNo = "苏A53251";
		plateColor = 1;
		// 定位数据
		this.PosEncrypt = 0;
		PosTime = new Date();
		this.longitude = 121123456;
		this.latitude = 34123456;
		this.gpsSpeed = 20;
		this.recSpeed = 23;
		this.totalMileage = 123456;
		this.direction = 361;
		this.altitude = 256;
		this.vehicleState = 32;
		this.alarmState = 121;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setGpsSpeed(int gpsSpeed) {
		this.gpsSpeed = gpsSpeed;
	}

	public int getGpsSpeed() {
		return gpsSpeed;
	}

	public void setRecSpeed(int recSpeed) {
		this.recSpeed = recSpeed;
	}

	public int getRecSpeed() {
		return recSpeed;
	}

	public void setTotalMileage(int totalMileage) {
		this.totalMileage = totalMileage;
	}

	public int getTotalMileage() {
		return totalMileage;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getDirection() {
		return direction;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setVehicleState(int vehicleState) {
		this.vehicleState = vehicleState;
	}

	public int getVehicleState() {
		return vehicleState;
	}

	public void setAlarmState(int alarmState) {
		this.alarmState = alarmState;
	}

	public int getAlarmState() {
		return alarmState;
	}

	public void setPosTime(Date posTime) {
		PosTime = posTime;
	}

	public Date getPosTime() {
		return PosTime;
	}

	public void setPosEncrypt(int posEncrypt) {
		PosEncrypt = posEncrypt;
	}

	public int getPosEncrypt() {
		return PosEncrypt;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public int getPlateColor() {
		return plateColor;
	}

	public void setPlateColor(int plateColor) {
		this.plateColor = plateColor;
	}
}
