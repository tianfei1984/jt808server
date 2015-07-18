package com.ltmonitor.jt808.protocol;

/** 
 多媒体数据上传
 
*/
public class JT_0801 implements IMessageBody
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
	/** 
	 位置信息汇报(0x0200)消息体
	 
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
	/** 
	 多媒体数据包
	 
	*/
	private byte[] multimediaData;
	public final byte[] getMultimediaData()
	{
		return multimediaData;
	}
	public final void setMultimediaData(byte[] value)
	{
		multimediaData = value;
	}


	@Override
	public String toString()
	{
		if (position == null)
		{
			setPosition(new JT_0200());
		}
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(String.format("多媒体数据ID：%1$s,多媒体类型：%2$s,经度：%3$s，纬度：%4$s，数据包长度：%5$s", getMultimediaDataId(), getMultimediaType(), getPosition().getLatitude(), getPosition().getLongitude(), getMultimediaData().length));
		return sBuilder.toString();
	}

	public final byte[] WriteToBytes()
	{
		MyBuffer buff = new MyBuffer();
		buff.put((byte)(getMultimediaDataId() >> 24));
		buff.put((byte)(getMultimediaDataId() >> 16));
		buff.put((byte)(getMultimediaDataId() >> 8));
		buff.put((byte)(getMultimediaDataId()));
		buff.put(getMultimediaType());
		buff.put(getMultidediaCodeFormat());
		buff.put(getEventCode());
		buff.put(getChannelId());
		buff.put(getPosition().WriteToBytes());
		buff.put(getMultimediaData());
		return buff.array();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setMultimediaDataId((int)((bytes[0] << 24) + (bytes[1] << 16) + (bytes[2] << 8) + bytes[3]));
		setMultimediaType(bytes[4]);
		setMultidediaCodeFormat(bytes[5]);
		setEventCode(bytes[6]);
		setChannelId(bytes[7]);
		JT808Common.setNew808Protocol(true);
		//终端发送的协议是否是补充协议
		if (JT808Common.isNew808Protocol())
		{
			byte[] positionData = new byte[28];
			System.arraycopy(bytes, 8, positionData, 0, 28);
			position = (new JT_0200());
			position.ReadFromBytes(positionData);
			byte[] multimedialData = new byte[bytes.length - 36];
			System.arraycopy(bytes, 36, multimedialData, 0, bytes.length - 36);
			setMultimediaData(multimedialData);
		}
		else
		{
			byte[] multimedialData = new byte[bytes.length - 8];
			System.arraycopy(bytes, 8, multimedialData, 0, bytes.length - 8);
			setMultimediaData(multimedialData);
		}
	}
}