package com.ltmonitor.jt808.protocol;

import java.util.Date;

import com.ltmonitor.jt808.tool.DateUtil;

public class Recorder_RealTimeClock implements IRecorderDataBlock {

	public final byte getCommandWord() {
		return 0x02;
	}

	// ORIGINAL LINE: public ushort getDataLength()
	public final short getDataLength() {
		return 6;
	}

	private java.util.Date realTimeClock = new java.util.Date(0);

	public final java.util.Date getRealTimeClock() {
		return realTimeClock;
	}

	public final void setRealTimeClock(java.util.Date value) {
		realTimeClock = value;
	}

	public final String toString() {
		return DateUtil.toStringByFormat(getRealTimeClock(),"yyyy-MM-dd HH:mm:ss");
	}

	public final byte[] WriteToBytes() {
		return BitConverter.getBytes(getRealTimeClock());
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		String strDate = "20" + String.format("%02X", bytes[0]) + "-" + String.format("%02X", bytes[1]) 
				+ "-" + String.format("%02X", bytes[2]) + " " + String.format("%02X", bytes[3]) 
				+ ":" + String.format("%02X", bytes[4]) + ":" + String.format("%02X", bytes[5]);
		
		Date d = DateUtil.stringToDatetime(strDate, "yyyy-MM-dd HH:mm:ss");
		setRealTimeClock(d);
	}
}