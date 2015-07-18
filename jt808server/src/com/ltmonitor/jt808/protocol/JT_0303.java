package com.ltmonitor.jt808.protocol;

/** 
 信息点播/取消
 
*/
public class JT_0303 implements IMessageBody
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
	 点播/取消标志,0：取消；1：点播
	 
	*/
	private byte pointResult;
	public final byte getPointResult()
	{
		return pointResult;
	}
	public final void setPointResult(byte value)
	{
		pointResult = value;
	}
	public final byte[] WriteToBytes()
	{
		return new byte[] { getMessageType(), getPointResult() };
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setMessageType(bytes[0]);
		setPointResult(bytes[1]);
	}

	@Override
	public String toString()
	{
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(String.format("信息类型：%1$s,标志：%2$s",getMessageType(),getPointResult()));
		return sBuilder.toString();
	}
}