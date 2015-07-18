package com.ltmonitor.jt808.protocol.jt2012;

public class RecorderEvent {
	
	private String eventTime;
	
	private String type;
	
	private String driverLicense;
	
	public RecorderEvent(String _time, String _type)
	{
		this.eventTime = _time;
		this.type = _type;
	}
	
	public RecorderEvent(String _time, String _type,String _license)
	{
		this.eventTime = _time;
		this.type = _type;
		this.driverLicense = _license;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public String getDriverLicense() {
		return driverLicense;
	}

	public void setDriverLicense(String driverLicense) {
		this.driverLicense = driverLicense;
	}

}
