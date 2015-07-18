package com.ltmonitor.jt808.protocol.jt2012;

import java.util.Calendar;

import com.ltmonitor.jt808.tool.DateUtil;

/** 
 采集实时时间
 
*/
public class Recorder_RealTime implements IRecorderDataBlock_2012
{
	/** 
	 命令字
	 
	*/
	public final byte getCommandWord()
	{
		return 0x02;
	}

	/** 
	 数据块长度
	 
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public ushort getDataLength()
	public final short getDataLength()
	{
		return 6;
	}

	/** 
	 实时时间
	 
	*/
	private java.util.Date privateRealTimeClock = new java.util.Date(0);
	public final java.util.Date getRealTimeClock()
	{
		return privateRealTimeClock;
	}
	public final void setRealTimeClock(java.util.Date value)
	{
		privateRealTimeClock = value;
	}

	public final byte[] WriteToBytes()
	{
		byte[] bytes = new byte[6];
		Calendar calendar = Calendar.getInstance();
		
		String strDate = DateUtil.toStringByFormat(getRealTimeClock(), "yyMMddHHmmss");
		bytes[0] = Byte.parseByte(strDate.substring(0, 2), 16);
		bytes[1] = Byte.parseByte(strDate.substring(2, 4), 16);
		bytes[2] = Byte.parseByte(strDate.substring(4, 6), 16);
		bytes[3] = Byte.parseByte(strDate.substring(6, 8), 16);
		bytes[4] = Byte.parseByte(strDate.substring(8, 10), 16);
		bytes[5] = Byte.parseByte(strDate.substring(10, 12), 16);
		return bytes;
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setRealTimeClock(new java.util.Date(java.util.Date.parse("20" + String.format("%02X", bytes[0]) + "-" + String.format("%02X", bytes[1]) + "-" + String.format("%02X", bytes[2]) + " " + String.format("%02X", bytes[3]) + ":" + String.format("%02X", bytes[4]) + ":" + String.format("%02X", bytes[5]))));
	}


}



