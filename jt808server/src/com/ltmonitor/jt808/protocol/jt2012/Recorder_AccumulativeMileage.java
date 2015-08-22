package com.ltmonitor.jt808.protocol.jt2012;

import com.ltmonitor.jt808.protocol.BitConverter;
import com.ltmonitor.jt808.tool.DateUtil;


/** 
 采集累计行驶里程 03H
 
*/
public class Recorder_AccumulativeMileage implements IRecorderDataBlock_2012
{
	/** 
	 命令字
	*/
	public final byte getCommandWord()
	{
		return 0x03;
	}

	/** 
	 数据块长度
	*/
	public final short getDataLength()
	{
		return 20;
	}

	/** 
	 实时时间
	 
	*/
	private java.util.Date privateRealTime = new java.util.Date(0);
	public final java.util.Date getRealTime()
	{
		return privateRealTime;
	}
	public final void setRealTime(java.util.Date value)
	{
		privateRealTime = value;
	}

	/** 
	 初次安装时间
	 
	*/
	private java.util.Date privateSetupDateTime = new java.util.Date(0);
	public final java.util.Date getSetupDateTime()
	{
		return privateSetupDateTime;
	}
	public final void setSetupDateTime(java.util.Date value)
	{
		privateSetupDateTime = value;
	}

	/** 
	 初始里程
	*/
	private long privateBeginMileage;
	public final long getBeginMileage()
	{
		return privateBeginMileage;
	}
	public final void setBeginMileage(long value)
	{
		privateBeginMileage = value;
	}

	/** 
	 累计行驶里程
	*/
	private long privateEndMileage;
	public final long getEndMileage()
	{
		return privateEndMileage;
	}
	
	public final void setEndMileage(long value)
	{
		privateEndMileage = value;
	}

	public final byte[] WriteToBytes()
	{
		byte[] bytes = new byte[4];
		String Mileage=(new Long(getBeginMileage())).toString();
		String s1 = Mileage.substring(0, 2);

		int a, b;
		a = Integer.parseInt(s1.substring(0, 1));
		b = Integer.parseInt(s1.substring(1, 2));
		bytes[0] = (byte)((int)(a << 4) + (int)b);

		String s2 = Mileage.substring(2, 4);
		a = Integer.parseInt(s2.substring(0, 1));
		b = Integer.parseInt(s2.substring(1, 2));
		bytes[1] = (byte)((int)(a << 4) + (int)b);

		String s3 = Mileage.substring(4, 6);
		a = Integer.parseInt(s3.substring(0, 1));
		b = Integer.parseInt(s3.substring(1, 2));
		bytes[2] = (byte)((int)(a << 4) + (int)b);

		String s4 = Mileage.substring(6, 8);
		a = Integer.parseInt(s4.substring(0, 1));
		b = Integer.parseInt(s4.substring(1, 2));
		bytes[3] = (byte)((int)(a << 4) + (int)b);
		return bytes;
	}

	public final void ReadFromBytes(byte[] bytes)
	{	
		//实时时间 
		setRealTime(new java.util.Date(java.util.Date.parse("20" + String.format("%02X", bytes[0]) + "-" + String.format("%02X", bytes[1]) + "-" + String.format("%02X", bytes[2]) + " " + String.format("%02X", bytes[3]) + ":" + String.format("%02X", bytes[4]) + ":" + String.format("%02X", bytes[5]))));
		//安装时间 
		setSetupDateTime(new java.util.Date(java.util.Date.parse("20" + String.format("%02X", bytes[6]) + "-" + String.format("%02X", bytes[7]) + "-" + String.format("%02X", bytes[8]) + " " + String.format("%02X", bytes[9]) + ":" + String.format("%02X", bytes[10]) + ":" + String.format("%02X", bytes[11]))));
		//初始里程
		byte[] beginMileage = new byte[4];
		System.arraycopy(bytes, 12, beginMileage, 0, 4);

		setBeginMileage(GetMileage(beginMileage));
		//累计里程
		byte[] endMileage = new byte[4];
		System.arraycopy(bytes, 16, endMileage, 0, 4);

		setEndMileage(GetMileage(endMileage));
	}

	private long GetMileage(byte[] mileageBytes)
	{
		long mile =  BitConverter.ToUInt32(mileageBytes, 0);
		return mile;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return DateUtil.datetimeToString(getRealTime())+","+DateUtil.datetimeToString(getSetupDateTime())+","+getBeginMileage()+","+getEndMileage();
	}
}

