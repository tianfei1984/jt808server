package com.ltmonitor.jt808.protocol;

/** 
 车辆控制
 
*/
public class JT_8500 implements IMessageBody
{
	/** 
	 控制标志
	 
	*/
	private byte privateFlag;
	public final byte getFlag()
	{
		return privateFlag;
	}
	public final void setFlag(byte value)
	{
		privateFlag = value;
	}

	public final byte[] WriteToBytes()
	{
		return new byte[] { getFlag() };
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setFlag(bytes[0]);
	}

	@Override
	public String toString()
	{
		return String.format("控制标志：%1$s", (new Byte(getFlag())).toString());
	}
}