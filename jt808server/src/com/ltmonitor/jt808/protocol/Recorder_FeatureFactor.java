package com.ltmonitor.jt808.protocol;

public class Recorder_FeatureFactor implements IRecorderDataBlock
{

	public final byte getCommandWord()
	{
		return 0x04;
	}


//ORIGINAL LINE: public ushort getDataLength()
	public final short getDataLength()
	{
		return 3;
	}

	/** 
	 设定的车辆特征系数 (高中低字节)
	 
	*/

//ORIGINAL LINE: private uint privateFeatureFactor;
	private int privateFeatureFactor;

//ORIGINAL LINE: public uint getFeatureFactor()
	public final int getFeatureFactor()
	{
		return privateFeatureFactor;
	}

//ORIGINAL LINE: public void setFeatureFactor(uint value)
	public final void setFeatureFactor(int value)
	{
		privateFeatureFactor = value;
	}


	public final String toString()
	{
		return "" + getFeatureFactor();
	}

	public final byte[] WriteToBytes()
	{
		byte[] bytes = new byte[3];
		bytes[0] = (byte)(getFeatureFactor() >> 16);
		bytes[1] = (byte)(getFeatureFactor() >> 8);
		bytes[2] = (byte)(getFeatureFactor());
		return bytes;
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setFeatureFactor((int)((bytes[0] << 16) + (bytes[1] << 8) + bytes[2]));
	}
}