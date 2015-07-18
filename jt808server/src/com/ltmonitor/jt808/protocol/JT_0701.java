package com.ltmonitor.jt808.protocol;

/** 
 电子运单上报
 
*/
public class JT_0701 implements IMessageBody
{
	/** 
	 电子运单上报
	 
	*/

//ORIGINAL LINE: private uint electronicFreightLength;
	private int electronicFreightLength;

//ORIGINAL LINE: public uint getElectronicFreightLength()
	public final int getElectronicFreightLength()
	{
		return electronicFreightLength;
	}

//ORIGINAL LINE: public void setElectronicFreightLength(uint value)
	public final void setElectronicFreightLength(int value)
	{
		electronicFreightLength = value;
	}
	/** 
	 电子运单内容
	 
	*/
	private String electronicFreightContent;
	public final String getElectronicFreightContent()
	{
		return electronicFreightContent;
	}
	public final void setElectronicFreightContent(String value)
	{
		electronicFreightContent = value;
	}

	public final byte[] WriteToBytes()
	{
		MyBuffer bytes = new MyBuffer();
		byte[] contentBytes = BitConverter.getBytes(getElectronicFreightContent());
		setElectronicFreightLength((int)(contentBytes.length));
		bytes.put((byte)(getElectronicFreightLength() >> 24));
		bytes.put((byte)(getElectronicFreightLength() >> 16));
		bytes.put((byte)(getElectronicFreightLength() >> 8));
		bytes.put((byte)getElectronicFreightLength());
		bytes.put(contentBytes);
		//bytes.put(0x00);
		return bytes.array();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setElectronicFreightLength((int)((bytes[0] << 24) + (bytes[1] << 16) + (bytes[2] << 8) + bytes[3]));
		setElectronicFreightContent(BitConverter.getString(bytes, 4, (int)getElectronicFreightLength()));
	}

	@Override
	public String toString()
	{
		return String.format("电子运单内容：%1$s", getElectronicFreightContent());
	}
}