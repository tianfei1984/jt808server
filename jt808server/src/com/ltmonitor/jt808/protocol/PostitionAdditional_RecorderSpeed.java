package com.ltmonitor.jt808.protocol;



public class PostitionAdditional_RecorderSpeed implements IPositionAdditionalItem
{
	public final int getAdditionalId()
	{
		return 0x03;
	}

	public final byte getAdditionalLength()
	{
		return 0x02;
	}

	/** 
	 行驶记录功能获取的速度，WORD，1/10km/h
	 
	*/

//ORIGINAL LINE: private ushort recorderSpeed;
	private short recorderSpeed;

//ORIGINAL LINE: public ushort getRecorderSpeed()
	public final short getRecorderSpeed()
	{
		return recorderSpeed;
	}

//ORIGINAL LINE: public void setRecorderSpeed(ushort value)
	public final void setRecorderSpeed(short value)
	{
		recorderSpeed = value;
	}

	public final byte[] WriteToBytes()
	{
		if (BitConverter.IsLittleEndian)
		{
			byte[] bytes = new byte[2];
			bytes[0] = (byte)(getRecorderSpeed() >> 8);
			bytes[1] = (byte)getRecorderSpeed();
			return bytes;
		}
		else
		{
			return BitConverter.GetBytes(getRecorderSpeed());
		}
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		if (BitConverter.IsLittleEndian)
		{
			setRecorderSpeed((short)((bytes[0] << 8) + bytes[1]));
		}
		else
		{
			setRecorderSpeed((short)BitConverter.ToUInt16(bytes, 0));
		}
	}
}