package com.ltmonitor.jt808.protocol.jt2012;

import com.ltmonitor.jt808.protocol.BitConverter;
import com.ltmonitor.jt808.tool.DateUtil;


/** 
 采集脉冲系数 0x04H
*/
public class Recorder_PulseFactor implements IRecorderDataBlock_2012
{
	/** 
	 命令字
	*/
	public final byte getCommandWord()
	{
		return 0x04;
	}

	/** 
	 数据块长度
	*/
	public final short getDataLength()
	{
		return 8;
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
	 脉冲系数
	*/
	private short privatePulseFactor;
	public final short getPulseFactor()
	{
		return privatePulseFactor;
	}
	public final void setPulseFactor(short value) {
		privatePulseFactor = value;
	}

	public final byte[] WriteToBytes()
	{
		byte[] bytes = new byte[2];
		bytes[0] = (byte)(getPulseFactor() >> 8);
		bytes[1] = (byte)(getPulseFactor());
		return bytes;
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setRealTime(new java.util.Date(java.util.Date.parse("20" + String.format("%02X", bytes[0]) + "-" + String.format("%02X", bytes[1]) + "-" + String.format("%02X", bytes[2]) + " " + String.format("%02X", bytes[3]) + ":" + String.format("%02X", bytes[4]) + ":" + String.format("%02X", bytes[5]))));

		this.privatePulseFactor = (short)BitConverter.ToUInt16(bytes, 6);
		//setPulseFactor((short)(Integer.parseInt(bytes[6] << 8) + Integer.parseInt(bytes[7])));
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return DateUtil.datetimeToString(getRealTime())+","+getPulseFactor();
	}
}
