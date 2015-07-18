package com.ltmonitor.jt808.protocol;



public class PostitionAdditional_FollowedBytesLength implements IPositionAdditionalItem
{

	public final int getAdditionalId()
	{
		return 0xE0;
	}

	private byte additionalLength;
	public final byte getAdditionalLength()
	{
		return additionalLength;
	}
	public final void setAdditionalLength(byte value)
	{
		additionalLength = value;
	}
	/** 
	 后续信息长度
	 
	*/

//ORIGINAL LINE: private ushort privateFollowedBytesLength;
	private short privateFollowedBytesLength;

//ORIGINAL LINE: public ushort getFollowedBytesLength()
	public final short getFollowedBytesLength()
	{
		return privateFollowedBytesLength;
	}

//ORIGINAL LINE: public void setFollowedBytesLength(ushort value)
	public final void setFollowedBytesLength(short value)
	{
		privateFollowedBytesLength = value;
	}

	public final byte[] WriteToBytes()
	{
		if (getAdditionalLength() == 1)
		{
			return new byte[] { (byte)(getFollowedBytesLength() >> 8) };
		}
		else if (getAdditionalLength() == 2)
		{
			return new byte[] { (byte)(getFollowedBytesLength() >> 8), (byte)getFollowedBytesLength() };
		}
		else
		{
			byte[] bytes = new byte[getAdditionalLength()];
			for (int i = 0; i < getAdditionalLength(); i++)
			{
				bytes[i] = 0x00;
			}
			return bytes;
		}
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		if (getAdditionalLength() == 1)
		{
			setFollowedBytesLength((short)bytes[0]);
		}
		else if (getAdditionalLength() == 2)
		{
			setFollowedBytesLength((short)((bytes[0] << 8) + bytes[1]));
		}
	}
}