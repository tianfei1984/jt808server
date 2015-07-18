package com.ltmonitor.app;

import java.util.Date;
/**
 * 连接参数类
 * @author tianfei
 *
 */
public class GpsConnection {
	//连接Id
	private long sessionId;
	//终端Sim卡号
	private String simNo;
	//车牌号
	private String plateNo;
	//连接时间
	private Date createDate;
	//最新在线时间
	private Date onlineDate;
	//收到的包的数量
	private int packageNum;
	//定位包数量
	private int positionPackageNum;
	//断开次数
	private int disconnectTimes;
	//错误包数
	private int errorPacketNum;
	//是否已连接
	private boolean connected;
	//是否定位
	private boolean located;
	
	
	public GpsConnection(String _simNo, long sId)
	{
		setSimNo(_simNo);
		setSessionId(sId);
		setCreateDate(new Date());
		setOnlineDate(new Date());
	}

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	public String getSimNo() {
		return simNo;
	}

	public void setSimNo(String simNo) {
		this.simNo = simNo;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getOnlineDate() {
		return onlineDate;
	}

	public void setOnlineDate(Date onlineDate) {
		this.onlineDate = onlineDate;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public int getErrorPacketNum() {
		return errorPacketNum;
	}

	public void setErrorPacketNum(int errorPacketNum) {
		this.errorPacketNum = errorPacketNum;
	}

	public int getDisconnectTimes() {
		return disconnectTimes;
	}

	public void setDisconnectTimes(int disconnectTimes) {
		this.disconnectTimes = disconnectTimes;
	}

	public int getPositionPackageNum() {
		return positionPackageNum;
	}

	public void setPositionPackageNum(int positionPackageNum) {
		this.positionPackageNum = positionPackageNum;
	}

	public int getPackageNum() {
		return packageNum;
	}

	public void setPackageNum(int packageNum) {
		this.packageNum = packageNum;
	}

	public boolean isLocated() {
		return located;
	}

	public void setLocated(boolean located) {
		this.located = located;
	}

}
