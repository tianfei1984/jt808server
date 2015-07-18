package com.ltmonitor.jt808.protocol;

/** 
 自动拍摄拍照命令
 
*/
public class JT_8880 implements IMessageBody
{
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
	 拍摄命令
	 
	*/

//ORIGINAL LINE: private ushort photoCommand;
	private short photoCommand;

//ORIGINAL LINE: public ushort getPhotoCommand()
	public final short getPhotoCommand()
	{
		return photoCommand;
	}

//ORIGINAL LINE: public void setPhotoCommand(ushort value)
	public final void setPhotoCommand(short value)
	{
		photoCommand = value;
	}
	/** 
	 拍照间隔/录像时间
	 
	*/

//ORIGINAL LINE: private ushort photoTimeInterval;
	private short photoTimeInterval;

//ORIGINAL LINE: public ushort getPhotoTimeInterval()
	public final short getPhotoTimeInterval()
	{
		return photoTimeInterval;
	}

//ORIGINAL LINE: public void setPhotoTimeInterval(ushort value)
	public final void setPhotoTimeInterval(short value)
	{
		photoTimeInterval = value;
	}
	/** 
	 保存标志
	 
	*/
	private byte storeFlag;
	public final byte getStoreFlag()
	{
		return storeFlag;
	}
	public final void setStoreFlag(byte value)
	{
		storeFlag = value;
	}
	/** 
	 分辨率
	 
	*/
	private byte resolution;
	public final byte getResolution()
	{
		return resolution;
	}
	public final void setResolution(byte value)
	{
		resolution = value;
	}
	/** 
	 图像/视频质量
	 
	*/
	private byte privateQuality;
	public final byte getQuality()
	{
		return privateQuality;
	}
	public final void setQuality(byte value)
	{
		privateQuality = value;
	}
	/** 
	 亮度
	 
	*/
	private byte brightness;
	public final byte getBrightness()
	{
		return brightness;
	}
	public final void setBrightness(byte value)
	{
		brightness = value;
	}
	/** 
	 对比度
	 
	*/
	private byte contrast;
	public final byte getContrast()
	{
		return contrast;
	}
	public final void setContrast(byte value)
	{
		contrast = value;
	}
	/** 
	 饱和度
	 
	*/
	private byte saturation;
	public final byte getSaturation()
	{
		return saturation;
	}
	public final void setSaturation(byte value)
	{
		saturation = value;
	}
	/** 
	 色度
	 
	*/
	private byte chroma;
	public final byte getChroma()
	{
		return chroma;
	}
	public final void setChroma(byte value)
	{
		chroma = value;
	}
	/** 
	 时间段数,最大四段
	 
	*/
	private byte timePeriodCount;
	public final byte getTimePeriodCount()
	{
		return timePeriodCount;
	}
	public final void setTimePeriodCount(byte value)
	{
		timePeriodCount = value;
	}
	/** 
	 第1时间段起始时间,hh-mm如果不分时间段，则没该字段
	 
	*/
	private String startTime1;
	public final String getStartTime1()
	{
		return startTime1;
	}
	public final void setStartTime1(String value)
	{
		startTime1 = value;
	}
	/** 
	 第1时间段结束时间,hh-mm如果不分时间段，则没该字段
	 
	*/
	private String endTime1;
	public final String getEndTime1()
	{
		return endTime1;
	}
	public final void setEndTime1(String value)
	{
		endTime1 = value;
	}
	/** 
	 第2时间段起始时间,hh-mm    如果少于2个时间段，则没该字段
	 
	*/
	private String startTime2;
	public final String getStartTime2()
	{
		return startTime2;
	}
	public final void setStartTime2(String value)
	{
		startTime2 = value;
	}
	/** 
	 第2时间段结束时间,hh-mm    如果少于2个时间段，则没该字段
	 
	*/
	private String endTime2;
	public final String getEndTime2()
	{
		return endTime2;
	}
	public final void setEndTime2(String value)
	{
		endTime2 = value;
	}
	/** 
	 第3时间段起始时间,hh-mm    如果少于3个时间段，则没该字段
	 
	*/
	private String startTime3;
	public final String getStartTime3()
	{
		return startTime3;
	}
	public final void setStartTime3(String value)
	{
		startTime3 = value;
	}
	/** 
	 第3时间段结束时间,hh-mm    如果少于3个时间段，则没该字段
	 
	*/
	private String endTime3;
	public final String getEndTime3()
	{
		return endTime3;
	}
	public final void setEndTime3(String value)
	{
		endTime3 = value;
	}
	/** 
	 第4时间段起始时间,hh-mm    如果少于4个时间段，则没该字段
	 
	*/
	private String startTime4;
	public final String getStartTime4()
	{
		return startTime4;
	}
	public final void setStartTime4(String value)
	{
		startTime4 = value;
	}
	/** 
	 第4时间段结束时间,hh-mm    如果少于4个时间段，则没该字段
	 
	*/
	private String endTime4;
	public final String getEndTime4()
	{
		return endTime4;
	}
	public final void setEndTime4(String value)
	{
		endTime4 = value;
	}

	public final byte[] WriteToBytes()
	{
		MyBuffer bytes = new MyBuffer();
		bytes.put(getChannelId());
		bytes.put((byte)(getPhotoCommand() >> 8));
		bytes.put((byte)getPhotoCommand());
		bytes.put((byte)(getPhotoTimeInterval() >> 8));
		bytes.put((byte)getPhotoTimeInterval());
		bytes.put(getStoreFlag());
		bytes.put(getResolution());
		bytes.put(getQuality());
		bytes.put(getBrightness());
		bytes.put(getContrast());
		bytes.put(getSaturation());
		bytes.put(getChroma());
		bytes.put(getTimePeriodCount());
		if (getTimePeriodCount() > 0)
		{
			bytes.put(Byte.parseByte(getStartTime1().substring(0, 2), 16));
			bytes.put(Byte.parseByte(getStartTime1().substring(2, 4), 16));

			bytes.put(Byte.parseByte(getEndTime1().substring(0, 2), 16));
			bytes.put(Byte.parseByte(getEndTime1().substring(2, 4), 16));

			if (getTimePeriodCount() > 1)
			{
				bytes.put(Byte.parseByte(getStartTime2().substring(0, 2), 16));
				bytes.put(Byte.parseByte(getStartTime2().substring(2, 4), 16));

				bytes.put(Byte.parseByte(getEndTime2().substring(0, 2), 16));
				bytes.put(Byte.parseByte(getEndTime2().substring(2, 4), 16));

				if (getTimePeriodCount() > 2)
				{
					bytes.put(Byte.parseByte(getStartTime3().substring(0, 2), 16));
					bytes.put(Byte.parseByte(getStartTime3().substring(2, 4), 16));

					bytes.put(Byte.parseByte(getEndTime3().substring(0, 2), 16));
					bytes.put(Byte.parseByte(getEndTime3().substring(2, 4), 16));

					if (getTimePeriodCount() > 3)
					{
						bytes.put(Byte.parseByte(getStartTime4().substring(0, 2), 16));
						bytes.put(Byte.parseByte(getStartTime4().substring(2, 4), 16));

						bytes.put(Byte.parseByte(getEndTime4().substring(0, 2), 16));
						bytes.put(Byte.parseByte(getEndTime4().substring(2, 4), 16));
					}
				}
			}
		}
		return bytes.array();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setChannelId(bytes[0]);
		setPhotoCommand((short)((bytes[1] << 8) + bytes[2]));
		setPhotoTimeInterval((short)((bytes[3] << 8) + bytes[4]));
		setStoreFlag(bytes[5]);
		setResolution(bytes[6]);
		setQuality(bytes[7]);
		setBrightness(bytes[8]);
		setContrast(bytes[9]);
		setSaturation(bytes[10]);
		setChroma(bytes[11]);
		setTimePeriodCount(bytes[12]);
		int pos =13;
		if (getTimePeriodCount() > 0)
		{
			setStartTime1(String.format("%02X", bytes[pos]) + String.format("%02X", bytes[pos + 1]));
			setEndTime1(String.format("%02X", bytes[pos + 2]) + String.format("%02X", bytes[pos + 3]));
			pos += 4;
			if (getTimePeriodCount() > 1)
			{
				setStartTime2(String.format("%02X", bytes[pos]) + String.format("%02X", bytes[pos + 1]));
				setEndTime2(String.format("%02X", bytes[pos + 2]) + String.format("%02X", bytes[pos + 3]));
				pos += 4;

				if (getTimePeriodCount() > 2)
				{
					setStartTime3(String.format("%02X", bytes[pos]) + String.format("%02X", bytes[pos + 1]));
					setEndTime3(String.format("%02X", bytes[pos + 2]) + String.format("%02X", bytes[pos + 3]));
					pos += 4;
					if (getTimePeriodCount() > 3)
					{
						setStartTime4(String.format("%02X", bytes[pos]) + String.format("%02X", bytes[pos + 1]));
						setEndTime4(String.format("%02X", bytes[pos + 2]) + String.format("%02X", bytes[pos + 3]));
					}
				}
			}
		}
	}
}