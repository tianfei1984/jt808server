package com.ltmonitor.jt808.protocol;

public class TiredDrivingRecordItem
{
	/** 
	 机动车驾驶证号码
	 
	*/
	private String driverLincenseNo;
	public final String getDriverLincenseNo()
	{
		return driverLincenseNo;
	}
	public final void setDriverLincenseNo(String value)
	{
		driverLincenseNo = value;
	}
	/** 
	 疲劳开始时间YY-MM-DD-hh-mm
	 
	*/
	private java.util.Date startTime = new java.util.Date(0);
	public final java.util.Date getStartTime()
	{
		return startTime;
	}
	public final void setStartTime(java.util.Date value)
	{
		startTime = value;
	}
	/** 
	 疲劳结束时间YY-MM-DD-hh-mm
	 
	*/
	private java.util.Date endTime = new java.util.Date(0);
	public final java.util.Date getEndTime()
	{
		return endTime;
	}
	public final void setEndTime(java.util.Date value)
	{
		endTime = value;
	}
}