package com.ltmonitor.jt808.protocol;

import org.apache.mina.util.Base64;

/** 
 信息服务
 
*/
public class JT_8304 implements IMessageBody
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
	 信息长度
	 
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
	 信息内容
	 
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
	public final byte[] WriteToBytes()
	{
		MyBuffer bytes = new MyBuffer();
		bytes.put(this.messageType);
		byte[] messageBytes = BitConverter.getBytes(getMessage());

		//messageBytes = Base64.encodeBase64(messageBytes);
		//messageBytes = Base64.decodeBase64(messageBytes);
		this.messageLength = (short)(messageBytes.length);
		//bytes.put((byte)(messageLength >> 8));
		bytes.put(messageLength);
		bytes.put(messageBytes);
		//bytes.put(0x00);
		return bytes.array();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		MyBuffer buff = new MyBuffer(bytes);
		setMessageType(buff.get());
		setMessageLength(buff.getShort());
		setMessage(buff.getString());
	}

	@Override
	public String toString()
	{
		return String.format("信息类型：%1$s,信息长度：%2$s,信息内容：%3$s", getMessageType(), getMessageLength(), getMessage());
	}
}