package com.ltmonitor.jt808.protocol;

/** 
 设定的驾驶员代码及其对应的机动车驾驶证号码
 
*/
public class Recorder_DriverVehicleCode implements IRecorderDataBlock
{
	public final byte getCommandWord()
	{
		return 0x01;
	}


//ORIGINAL LINE: public ushort getDataLength()
	public final short getDataLength()
	{
		return 21;
	}
	/** 
	 驾驶员代码（高）驾驶员代码（中）驾驶员代码（低）
	 
	*/
	private String driverCode;
	public final String getDriverCode()
	{
		return driverCode;
	}
	public final void setDriverCode(String value)
	{
		driverCode = value;
	}
	/** 
	 机动车驾驶证号,ASCII码
	*/
	private String driverLicenseNo;
	public final String getDriverLicenseNo()
	{
		return driverLicenseNo;
	}
	public final void setDriverLicenseNo(String value)
	{
		driverLicenseNo = value;
	}

	public final String toString()
	{
		return getDriverCode() + "," + getDriverLicenseNo();
	}



	public final byte[] WriteToBytes()
	{
		byte[] bytes = new byte[21];

//ORIGINAL LINE: uint _driverCode = Convert.ToUInt32(DriverCode);
		int _driverCode = Integer.parseInt(getDriverCode());
		bytes[0] = (byte)(_driverCode >> 16);
		bytes[1] = (byte)(_driverCode >> 8);
		bytes[2] = (byte)(_driverCode);
		byte[] dirverLicenseNoBytes = BitConverter.getBytes(getDriverLicenseNo());
		if (dirverLicenseNoBytes.length >= 17)
		{
			System.arraycopy(dirverLicenseNoBytes, 0, bytes, 3, 17);
		}
		else
		{
			System.arraycopy(dirverLicenseNoBytes, 0, bytes, 3, dirverLicenseNoBytes.length);
			int pos = 3 + dirverLicenseNoBytes.length;
			while (pos < 20)
			{
				bytes[pos] = 0x00;
				pos++;
			}
		}
		bytes[20] = 0x00;
		return bytes;
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setDriverCode(""+((bytes[0] << 16) + (bytes[1] << 8) + bytes[2]));
		setDriverLicenseNo(BitConverter.getString(bytes, 3, 18));
	}
}