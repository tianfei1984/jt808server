package com.ltmonitor.jt808.protocol;



public class PostitionAdditional_Voltage implements IPositionAdditionalItem
{

	public final int getAdditionalId()
	{
		return 0xE1;
	}

	public final byte getAdditionalLength()
	{
		return 0x02;
	}
	/** 
	 电压,单位0.01V
	 
	*/

//ORIGINAL LINE: private ushort privateVoltage;
	private short privateVoltage;

//ORIGINAL LINE: public ushort getVoltage()
	public final short getVoltage()
	{
		return privateVoltage;
	}

//ORIGINAL LINE: public void setVoltage(ushort value)
	public final void setVoltage(short value)
	{
		privateVoltage = value;
	}

	public final byte[] WriteToBytes()
	{
		if (BitConverter.IsLittleEndian)
		{
			byte[] bytes = new byte[2];
			bytes[0] = (byte)(getVoltage() >> 8);
			bytes[1] = (byte)getVoltage();
			return bytes;
		}
		else
		{
			return BitConverter.GetBytes(getVoltage());
		}
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		if (BitConverter.IsLittleEndian)
		{
			setVoltage((short)((bytes[0] << 8) + bytes[1]));
		}
		else
		{
			setVoltage((short)BitConverter.ToUInt16(bytes, 0));
		}
	}
}