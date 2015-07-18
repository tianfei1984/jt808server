package com.ltmonitor.jt808.protocol;

public class PointcastMessageItem
{
	/** 
	 信息类型
	 
	*/
	private byte messageType;
	public final byte getMessageType()
	{
		return messageType;
	}
	public final void setMessageType(byte value)
	{
		messageType = value;
	}
	/** 
	 信息名称长度
	 
	*/

//ORIGINAL LINE: private ushort messageLength;
	private short messageLength;

//ORIGINAL LINE: public ushort getMessageLength()
	public final short getMessageLength()
	{
		return messageLength;
	}

//ORIGINAL LINE: public void setMessageLength(ushort value)
	public final void setMessageLength(short value)
	{
		messageLength = value;
	}
	/** 
	 信息名称
	 
	*/
	private String message;
	public final String getMessage()
	{
		return message;
	}
	public final void setMessage(String value)
	{
		message = value;
	}
}