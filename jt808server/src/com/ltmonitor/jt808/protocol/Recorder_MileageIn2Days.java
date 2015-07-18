package com.ltmonitor.jt808.protocol;

import java.util.Date;

import com.ltmonitor.jt808.tool.DateUtil;

public class Recorder_MileageIn2Days implements IRecorderDataBlock
{
	public final byte getCommandWord()
	{
		return 0x08;
	}


//ORIGINAL LINE: public ushort getDataLength()
	public final short getDataLength()
	{
		return 8;
	}

	/** 
	 累计行驶里程值（表示单位0.1km）范围00～999999
	 
	*/

//ORIGINAL LINE: private uint mileage;
	private int mileage;

//ORIGINAL LINE: public uint getMileage()
	public final int getMileage()
	{
		return mileage;
	}

//ORIGINAL LINE: public void setMileage(uint value)
	public final void setMileage(int value)
	{
		mileage = value;
	}
	/** 
	 读出时刻 YY-MM-DD-hh-mm
	 
	*/
	private java.util.Date readingTime = new java.util.Date(0);
	public final java.util.Date getReadingTime()
	{
		return readingTime;
	}
	public final void setReadingTime(java.util.Date value)
	{
		readingTime = value;
	}

	public final String toString()
	{
		return getMileage() + "," +BitConverter.format(getReadingTime());
	}
	public final byte[] WriteToBytes()
	{
		byte[] bytes = new byte[8];
		bytes[0] = (byte)(getMileage() >> 16);
		bytes[1] = (byte)(getMileage() >> 8);
		bytes[2] = (byte)(getMileage());
		byte[] date1 = BitConverter.getBytes(this.getReadingTime());
		System.arraycopy(date1, 0, bytes, 3, 6);
		return bytes;
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setMileage((int)((bytes[0] << 16) + (bytes[1] << 8) + bytes[2]));
		String strDate = "20" + String.format("%02X", bytes[3]) + "-" + String.format("%02X", bytes[4]) 
				+ "-" + String.format("%02X", bytes[5]) + " " + String.format("%02X", bytes[6]) 
				+ ":" + String.format("%02X", bytes[7]) + ":" + String.format("%02X", bytes[8]);
		
		Date d = DateUtil.stringToDatetime(strDate, "yyyy-MM-dd HH:mm:ss");
		
		setReadingTime(d);
	}
}