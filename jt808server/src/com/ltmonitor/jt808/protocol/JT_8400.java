package com.ltmonitor.jt808.protocol;

/** 
 电话回拨
 
*/
public class JT_8400 implements IMessageBody
{
	/** 
	 标志,0:普通通话；1:监听
	 
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
	/** 
	 电话号码
	 
	*/
	private String phoneNo;
	public final String getPhoneNo()
	{
		return phoneNo;
	}
	public final void setPhoneNo(String value)
	{
		phoneNo = value;
	}
	public final byte[] WriteToBytes()
	{
		MyBuffer bytes = new MyBuffer();
		bytes.put(getFlag());
		bytes.put(getPhoneNo());
		bytes.put(0x00);
		return bytes.array();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		
			setFlag(bytes[0]);
			setPhoneNo(new String(bytes, 1, bytes.length - 1));
		
	}

	@Override
	public String toString()
	{
		return String.format("标志：%1$s,电话号码:%2$s", getFlag(), getPhoneNo());
	}
}