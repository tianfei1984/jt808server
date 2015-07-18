package com.ltmonitor.jt808.protocol;

public class MuldimediaSearchDataItem
{
	//媒体ID

//ORIGINAL LINE: private uint multimediaId;
	private int multimediaId;

//ORIGINAL LINE: public uint getMultimediaId()
	public final int getMultimediaId()
	{
		return multimediaId;
	}

//ORIGINAL LINE: public void setMultimediaId(uint value)
	public final void setMultimediaId(int value)
	{
		multimediaId = value;
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
	 位置信息汇报
	 
	*/
	private JT_0200 position;
	public final JT_0200 getPosition()
	{
		return position;
	}
	public final void setPosition(JT_0200 value)
	{
		position = value;
	}
}