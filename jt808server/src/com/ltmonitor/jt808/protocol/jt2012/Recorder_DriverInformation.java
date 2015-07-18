package com.ltmonitor.jt808.protocol.jt2012;

import com.ltmonitor.jt808.protocol.BitConverter;


/** 
 采集驾驶人信息
 
*/
public class Recorder_DriverInformation implements IRecorderDataBlock_2012
{
	/** 
	 命令字
	 
	*/
	public final byte getCommandWord()
	{
		return 0x01;
	}

	/** 
	 数据块长度
	 
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public ushort getDataLength()
	public final short getDataLength()
	{
		return 18;
	}

	public final byte[] WriteToBytes()
	{
		byte[] bytes = null;
		return bytes;
	}

	/** 
	 机动车驾驶证号码 （未知为 00H）
	 
	*/
	private String privateDriverLicenseNo;
	public final String getDriverLicenseNo()
	{
		return privateDriverLicenseNo;
	}
	public final void setDriverLicenseNo(String value)
	{
		privateDriverLicenseNo = value;
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		String license = BitConverter.getString(bytes);
		//setDriverLicenseNo(Encoding.GetEncoding("ascii").GetString(bytes));
		this.privateDriverLicenseNo = license;
		if (getDriverLicenseNo().length() == 15)
		{
			String add = "00H";
			setDriverLicenseNo(getDriverLicenseNo() + add);
		}
	}
}

