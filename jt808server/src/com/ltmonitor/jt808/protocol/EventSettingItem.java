package com.ltmonitor.jt808.protocol;

public class EventSettingItem
{
	/** 
	 事件ID
	 
	*/
	private byte eventId;
	public final byte getEventId()
	{
		return eventId;
	}
	public final void setEventId(byte value)
	{
		eventId = value;
	}
	/** 
	 事件内容长度
	 
	*/
	private byte eventLength;
	public final byte getEventLength()
	{
		return eventLength;
	}
	public final void setEventLength(byte value)
	{
		eventLength = value;
	}
	/** 
	 事件内容
	 
	*/
	private String eventContent;
	public final String getEventContent()
	{
		return eventContent;
	}
	public final void setEventContent(String value)
	{
		eventContent = value;
	}
}