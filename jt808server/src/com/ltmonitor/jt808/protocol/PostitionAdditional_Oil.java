package com.ltmonitor.jt808.protocol;



public class PostitionAdditional_Oil implements IPositionAdditionalItem
{

	public final int getAdditionalId()
	{
		return 0x02;
	}

	public final byte getAdditionalLength()
	{
		return 0x02;
	}

	/** 
	 油量，WORD，1/10L，对应车上油量表读数
	 
	*/

//ORIGINAL LINE: private ushort oil;
	private short oil;

//ORIGINAL LINE: public ushort getOil()
	public final short getOil()
	{
		return oil;
	}

//ORIGINAL LINE: public void setOil(ushort value)
	public final void setOil(short value)
	{
		oil = value;
	}

	public final byte[] WriteToBytes()
	{
		if (BitConverter.IsLittleEndian)
		{
			byte[] bytes = new byte[2];
			bytes[0] = (byte)(getOil() >> 8);
			bytes[1] = (byte)getOil();
			return bytes;
		}
		else
		{
			return BitConverter.GetBytes(getOil());
		}
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		if (BitConverter.IsLittleEndian)
		{
			setOil((short)((bytes[0] << 8) + bytes[1]));
		}
		else
		{
			setOil((short)BitConverter.ToUInt16(bytes, 0));
		}
	}
}