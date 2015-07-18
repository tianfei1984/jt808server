package com.ltmonitor.jt808.model;

import java.util.Date;

/**
 * 上级平台下发的应急接入信息,终端获取此信息进行接入到上级监管平台
 * @author Administrator
 *
 */
public class EmergencyAccessInfo {
	
	private String authenticatetionCode ;
	
	private String accessPointName;
	
	private String userName;
	
	private String password;
	
	private String serverIp;
	
	private int tcpPort;
	
	private int udpPort;
	
	private Date utcTime;
	
	public String ToString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.authenticatetionCode).append(",")
		.append(this.accessPointName).append(",")
		.append(this.userName).append(",")
		.append(this.password).append(",")
		.append(this.serverIp).append(",")
		.append(this.tcpPort).append(",")
		.append(this.udpPort).append(",")
		.append(this.utcTime);
		return sb.toString();
	}

	public void setAuthenticatetionCode(String authenticatetionCode) {
		this.authenticatetionCode = authenticatetionCode;
	}

	public String getAuthenticatetionCode() {
		return authenticatetionCode;
	}

	public void setAccessPointName(String accessPointName) {
		this.accessPointName = accessPointName;
	}

	public String getAccessPointName() {
		return accessPointName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	public int getTcpPort() {
		return tcpPort;
	}

	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	public int getUdpPort() {
		return udpPort;
	}

	public void setUtcTime(Date utcTime) {
		this.utcTime = utcTime;
	}

	public Date getUtcTime() {
		return utcTime;
	}

}
