package com.ltmonitor.jt808.protocol;

/** 
 数据压缩上报
 
*/
public class JT_0901 implements IMessageBody
{
	/** 
	 压缩消息长度
	 
	*/

//ORIGINAL LINE: private uint pressDataLength;
	private int pressDataLength;

//ORIGINAL LINE: public uint getPressDataLength()
	public final int getPressDataLength()
	{
		return pressDataLength;
	}

//ORIGINAL LINE: public void setPressDataLength(uint value)
	public final void setPressDataLength(int value)
	{
		pressDataLength = value;
	}
	/** 
	 压缩消息体
	 
	*/
	private byte[] pressData;
	public final byte[] getPressData()
	{
		return pressData;
	}
	public final void setPressData(byte[] value)
	{
		pressData = value;
	}

	public final byte[] WriteToBytes()
	{
		byte [] bytes = new byte[getPressData().length + 4];
		bytes[0] = (byte)(getPressDataLength() >> 24);
		bytes[1] = (byte)(getPressDataLength() >> 16);
		bytes[2] = (byte)(getPressDataLength() >> 8);
		bytes[3] = (byte)(getPressDataLength());
		System.arraycopy(getPressData(), 0, bytes, 4, getPressData().length);
		return bytes;
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setPressDataLength((int)((bytes[0] << 24) + (bytes[1] << 16) + (bytes[2] << 8) + bytes[3]));
		setPressData(new byte[bytes.length - 4]);
		System.arraycopy(bytes, 4, getPressData(), 0, bytes.length - 4);
	}
}