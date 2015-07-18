package com.ltmonitor.jt808.protocol;

/** 
 事件报告
 
*/
public class JT_0301 implements IMessageBody
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
	public final byte[] WriteToBytes()
	{
		return new byte[] { getEventId() };
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setEventId(bytes[0]);
	}

	@Override
	public String toString()
	{
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(String.format("事件ID：%1$s", getEventId()));
		return sBuilder.toString();
	}
}