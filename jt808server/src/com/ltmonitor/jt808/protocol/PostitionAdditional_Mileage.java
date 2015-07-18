package com.ltmonitor.jt808.protocol;



public class PostitionAdditional_Mileage implements IPositionAdditionalItem
{

	public final int getAdditionalId()
	{
		return 0x01;
	}

	public final byte getAdditionalLength()
	{
		return 0x04;
	}

	/** 
	 里程，DWORD，1/10km，对应车上里程表读数
	 
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

	public final byte[] WriteToBytes()
	{
		if (BitConverter.IsLittleEndian)
		{
			byte[] bytes = new byte[4];
			bytes[0] = (byte)(getMileage() >> 24);
			bytes[1] = (byte)(getMileage() >> 16);
			bytes[2] = (byte)(getMileage() >> 8);
			bytes[3] = (byte)getMileage();
			return bytes;
		}
		else
		{
			return BitConverter.GetBytes(getMileage());
		}
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		if (BitConverter.IsLittleEndian)
		{
			setMileage((int)((bytes[0] << 24) + (bytes[1] << 16) + (bytes[2] << 8) + bytes[3]));
		}
		else
		{
			setMileage(BitConverter.ToUInt32(bytes, 0));
		}
	}
}