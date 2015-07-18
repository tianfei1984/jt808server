package com.ltmonitor.jt808.protocol;

/** 
 数据上行透传
 
*/
public class JT_0900 implements IMessageBody
{
	/** 
	 透传消息类型
	 
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
	 透传消息内容
	 
	*/
	private byte[] messageContent;
	public final byte[] getMessageContent()
	{
		return messageContent;
	}
	public final void setMessageContent(byte[] value)
	{
		messageContent = value;
	}
	public final byte[] WriteToBytes()
	{
		byte[] bytes = new byte[getMessageContent().length+1];
		bytes[0] = getMessageType();
		System.arraycopy(getMessageContent(), 0, bytes, 1, getMessageContent().length);
		return bytes;
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setMessageType(bytes[0]);
		setMessageContent(new byte[bytes.length - 1]);
		System.arraycopy(bytes, 1, getMessageContent(), 0, bytes.length - 1);
	}
}