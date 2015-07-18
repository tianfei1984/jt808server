package com.ltmonitor.jt808.protocol.jt2012;

import com.ltmonitor.jt808.protocol.BitConverter;


/** 
 采集累计行驶里程
 
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
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public ushort getDataLength()
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
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: private ulong privateBeginMileage;
	private long privateBeginMileage;
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public ulong getBeginMileage()
	public final long getBeginMileage()
	{
		return privateBeginMileage;
	}
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public void setBeginMileage(ulong value)
	public final void setBeginMileage(long value)
	{
		privateBeginMileage = value;
	}

	/** 
	 累计行驶里程
	 
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: private ulong privateEndMileage;
	private long privateEndMileage;
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public ulong getEndMileage()
	public final long getEndMileage()
	{
		return privateEndMileage;
	}
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public void setEndMileage(ulong value)
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
		setRealTime(new java.util.Date(java.util.Date.parse("20" + String.format("%02X", bytes[0]) + "-" + String.format("%02X", bytes[1]) + "-" + String.format("%02X", bytes[2]) + " " + String.format("%02X", bytes[3]) + ":" + String.format("%02X", bytes[4]) + ":" + String.format("%02X", bytes[5]))));

		setSetupDateTime(new java.util.Date(java.util.Date.parse("20" + String.format("%02X", bytes[6]) + "-" + String.format("%02X", bytes[7]) + "-" + String.format("%02X", bytes[8]) + " " + String.format("%02X", bytes[9]) + ":" + String.format("%02X", bytes[10]) + ":" + String.format("%02X", bytes[11]))));

		byte[] beginMileage = new byte[4];
		System.arraycopy(bytes, 12, beginMileage, 0, 4);

		setBeginMileage(GetMileage(beginMileage));

		byte[] endMileage = new byte[4];
		System.arraycopy(bytes, 16, endMileage, 0, 4);

		setEndMileage(GetMileage(endMileage));
	}

//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: private ulong GetMileage(byte[] mileageBytes)
	private long GetMileage(byte[] mileageBytes)
	{
		long mile =  BitConverter.ToUInt32(mileageBytes, 0);
		return mile;
		/**
		long mile = 0;
		//获取2进制字符串
		String t1 = String.valueOf(mileageBytes[0], 2);
		if (t1.length() < 8)
		{
			t1 = t1.PadLeft(8, '0');
		}
		//对2进制字符串高地位解析
		int nub1 = Integer.parseInt(t1.substring(0, 4), 2);
		int nub2 = Integer.parseInt(t1.substring(4, 8), 2);

		String t2 = String.valueOf(mileageBytes[1], 2);
		if (t2.length() < 8)
		{
			t2 = t2.PadLeft(8, '0');
		}
		int nub3 = Integer.parseInt(t2.substring(0, 4), 2);
		int nub4 = Integer.parseInt(t2.substring(4, 8), 2);

		String t3 = String.valueOf(mileageBytes[2], 2);
		if (t3.length() < 8)
		{
			t3 = t3.PadLeft(8, '0');
		}
		int nub5 = Integer.parseInt(t3.substring(0, 4), 2);
		int nub6 = Integer.parseInt(t3.substring(4, 8), 2);

		String t4 = String.valueOf(mileageBytes[3], 2);
		if (t4.length() < 8)
		{
			t4 = t4.PadLeft(8, '0');
		}
		int nub7 = Integer.parseInt(t4.substring(0, 4), 2);
		int nub8 = Integer.parseInt(t4.substring(4, 8), 2);

		mile = Long.Parse((new Integer(nub1)).toString() + (new Integer(nub2)).toString() + (new Integer(nub3)).toString() + (new Integer(nub4)).toString() + (new Integer(nub5)).toString() + (new Integer(nub6)).toString() + (new Integer(nub7)).toString() + (new Integer(nub8)).toString());

		return mile;
		*/
	}
}

