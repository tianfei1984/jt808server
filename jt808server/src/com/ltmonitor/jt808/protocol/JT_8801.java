package com.ltmonitor.jt808.protocol;


/** 
 摄像头立即拍摄命令
 
*/
public class JT_8801 implements IMessageBody
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
	 拍摄命令 0表示停止拍摄；0xFFFF表示录像；其它表示拍照张数 
	 
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

	public final byte[] WriteToBytes()
	{
		byte[] bytes = new byte[12];
		bytes[0] = getChannelId();
		bytes[1] = (byte)(getPhotoCommand() >> 8);
		bytes[2] = (byte)getPhotoCommand();
		bytes[3] = (byte)(getPhotoTimeInterval() >> 8);
		bytes[4] = (byte)getPhotoTimeInterval();
		bytes[5] = getStoreFlag();
		bytes[6] = getResolution();
		bytes[7] = getQuality();
		bytes[8] = getBrightness();
		bytes[9] = getContrast();
		bytes[10] = getSaturation();
		bytes[11] = getChroma();
		return bytes;
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		try
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
		}
		catch (RuntimeException ex)
		{
			StringBuilder sb = new StringBuilder();
			for (byte bt : bytes)
			{
				sb.append(String.format("%02X", bt) + " ");
			}
		}
	}
}