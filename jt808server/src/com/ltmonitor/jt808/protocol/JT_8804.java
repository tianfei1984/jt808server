package com.ltmonitor.jt808.protocol;

/** 
 录音开始命令
 
*/
public class JT_8804 implements IMessageBody
{
	/** 
	 录音命令
	 
	*/
	private byte recordCommad;
	public final byte getRecordCommad()
	{
		return recordCommad;
	}
	public final void setRecordCommad(byte value)
	{
		recordCommad = value;
	}
	/** 
	 录音时间
	 
	*/

//ORIGINAL LINE: private ushort recordTimePeriod;
	private short recordTimePeriod;

//ORIGINAL LINE: public ushort getRecordTimePeriod()
	public final short getRecordTimePeriod()
	{
		return recordTimePeriod;
	}

//ORIGINAL LINE: public void setRecordTimePeriod(ushort value)
	public final void setRecordTimePeriod(short value)
	{
		recordTimePeriod = value;
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
	 音频采样率
	 
	*/
	private byte privateFrequency;
	public final byte getFrequency()
	{
		return privateFrequency;
	}
	public final void setFrequency(byte value)
	{
		privateFrequency = value;
	}

	public final byte[] WriteToBytes()
	{
		byte[] bytes = new byte[5];
		bytes[0] = getRecordCommad();
		bytes[1] = (byte)(getRecordTimePeriod() >> 8);
		bytes[2] = (byte)getRecordTimePeriod();
		bytes[3] = getStoreFlag();
		bytes[4] = getFrequency();
		return bytes;
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setRecordCommad(bytes[0]);
		setRecordTimePeriod((short)((bytes[1] << 8) + bytes[2]));
		setStoreFlag(bytes[3]);
		setFrequency(bytes[4]);
	}
}