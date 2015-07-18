package com.ltmonitor.jt808.protocol;

/** 
 多媒体事件信息上传
 
*/
public class JT_0800 implements IMessageBody
{
	/** 
	 多媒体数据ID
	 
	*/

//ORIGINAL LINE: private uint multimediaDataId;
	private int multimediaDataId;

//ORIGINAL LINE: public uint getMultimediaDataId()
	public final int getMultimediaDataId()
	{
		return multimediaDataId;
	}

//ORIGINAL LINE: public void setMultimediaDataId(uint value)
	public final void setMultimediaDataId(int value)
	{
		multimediaDataId = value;
	}
	/** 
	 多媒体类型
	 
	*/
	private byte multimediaType;
	public final byte getMultimediaType()
	{
		return multimediaType;
	}
	public final void setMultimediaType(byte value)
	{
		multimediaType = value;
	}
	/** 
	 多媒体格式编码
	 
	*/
	private byte multidediaCodeFormat;
	public final byte getMultidediaCodeFormat()
	{
		return multidediaCodeFormat;
	}
	public final void setMultidediaCodeFormat(byte value)
	{
		multidediaCodeFormat = value;
	}
	/** 
	 事件项编码
	 
	*/
	private byte eventCode;
	public final byte getEventCode()
	{
		return eventCode;
	}
	public final void setEventCode(byte value)
	{
		eventCode = value;
	}
	/** 
	 通道ID
	 
	*/
	private byte channelId;
	public final byte getChannelId()
	{
		return channelId;
	}
	public final void setChannelId(byte value)
	{
		channelId = value;
	}

	public final byte[] WriteToBytes()
	{
		byte[] bytes = new byte[8];
		bytes[0] = (byte)(getMultimediaDataId() >> 24);
		bytes[1] = (byte)(getMultimediaDataId() >> 16);
		bytes[2] = (byte)(getMultimediaDataId() >> 8);
		bytes[3] = (byte)(getMultimediaDataId());
		bytes[4] = getMultimediaType();
		bytes[5] = getMultidediaCodeFormat();
		bytes[6] = getEventCode();
		bytes[7] = getChannelId();
		return bytes;
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setMultimediaDataId((int)((bytes[0] << 24) + (bytes[1] << 16) + (bytes[2] << 8) + bytes[3]));
		setMultimediaType(bytes[4]);
		setMultidediaCodeFormat(bytes[5]);
		setEventCode(bytes[6]);
		setChannelId(bytes[7]);
	}

	@Override
	public String toString()
	{
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(String.format("数据ID：%1$s,多媒体类型：%2$s,编码格式：%3$s,事件编码：%4$s,通道ID：%5$s", getMultimediaDataId(), getMultimediaType(), getMultidediaCodeFormat(), getEventCode(), getChannelId()));
		return sBuilder.toString();
	}
}