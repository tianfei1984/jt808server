package com.ltmonitor.jt808.protocol;

public class Recorder_VehicleLicenseInfo implements IRecorderDataBlock
{

	public final byte getCommandWord()
	{
		return 0x06;
	}


//ORIGINAL LINE: public ushort getDataLength()
	public final short getDataLength()
	{
		return 41;
	}
	/** 
	 存储的车辆VIN号
	 
	*/
	private String privateVinNo;
	public final String getVinNo()
	{
		return privateVinNo;
	}
	public final void setVinNo(String value)
	{
		privateVinNo = value;
	}
	/** 
	 车牌号码
	 
	*/
	private String privateVehicleLicenseNo;
	public final String getVehicleLicenseNo()
	{
		return privateVehicleLicenseNo;
	}
	public final void setVehicleLicenseNo(String value)
	{
		privateVehicleLicenseNo = value;
	}
	/** 
	 车牌分类
	 
	*/
	private String plateType;
	public final String getPlateType()
	{
		return plateType;
	}
	public final void setPlateType(String value)
	{
		plateType = value;
	}


	public  String toString()
	{
		return "" + getVinNo() + "," + getVehicleLicenseNo() + "," + getPlateType();
	}


	public final byte[] WriteToBytes()
	{
		byte [] bytes = new byte[41];
		byte[] vinBytes = BitConverter.getBytes(getVinNo());
		if (vinBytes.length >= 16)
		{
			System.arraycopy(vinBytes, 0, bytes, 0, 16);
		}
		else
		{
			System.arraycopy(vinBytes, 0, bytes, 0, vinBytes.length);
			int pos = vinBytes.length;
			while (pos < 16)
			{
				bytes[pos] = 0x00;
				pos++;
			}
		}
		bytes[16] = 0x00;
		byte[] licenceNoBytes = BitConverter.getBytes(getVehicleLicenseNo());
		if (licenceNoBytes.length >= 11)
		{
			System.arraycopy(licenceNoBytes, 0, bytes, 17, 11);
		}
		else
		{
			System.arraycopy(licenceNoBytes, 0, bytes, 17, licenceNoBytes.length);
			int pos = licenceNoBytes.length;
			while (pos < 11)
			{
				bytes[pos + 17] = 0x00;
				pos++;
			}
		}
		bytes[28] = 0x00;
		byte[] plateTypeBytes = BitConverter.getBytes(getPlateType());
		if (plateTypeBytes.length >= 7)
		{
			System.arraycopy(plateTypeBytes, 0, bytes, 29, 11);
		}
		else
		{
			System.arraycopy(plateTypeBytes, 0, bytes, 29, plateTypeBytes.length);
			int pos = plateTypeBytes.length;
			while (pos < 7)
			{
				bytes[pos + 29] = 0x00;
				pos++;
			}
		}
		bytes[36] = 0x00;
		bytes[37] = 0x00;
		bytes[38] = 0x00;
		bytes[39] = 0x00;
		bytes[40] = 0x00;
		return bytes;
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setVinNo(BitConverter.getString(bytes,0, 17));
		setVehicleLicenseNo(BitConverter.getString(bytes, 17, 8));
		setPlateType(BitConverter.getString(bytes, 29, 8));
	}
}