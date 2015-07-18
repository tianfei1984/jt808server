package com.ltmonitor.jt808.protocol.jt2012;

import com.ltmonitor.jt808.protocol.BitConverter;


/** 
 采集车辆信息
 
*/
public class Recorder_VehicleInformation implements IRecorderDataBlock_2012
{
	/** 
	 命令字
	 
	*/
	public final byte getCommandWord()
	{
		return 0x05;
	}

	/** 
	 数据块长度
	 
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public ushort getDataLength()
	public final short getDataLength()
	{
		return 41;
	}

	/** 
	 车辆识别代号
	 
	*/
	private String privateVIN;
	public final String getVIN()
	{
		return privateVIN;
	}
	public final void setVIN(String value)
	{
		privateVIN = value;
	}

	/** 
	 机动车号牌号码
	 
	*/
	private String privateVehicleNumber;
	public final String getVehicleNumber()
	{
		return privateVehicleNumber;
	}
	public final void setVehicleNumber(String value)
	{
		privateVehicleNumber = value;
	}

	/** 
	 机动车号牌分类
	 
	*/
	private String classificationOfVehicle;
	public final String getClassificationOfVehicle()
	{
		return classificationOfVehicle;
	}
	public final void setClassificationOfVehicle(String value)
	{
		classificationOfVehicle = value;
	}

	public final byte[] WriteToBytes()
	{
		byte[] bytesVIN;
		byte[] bytesFirstWord;
		byte[] bytesVnub;
		byte[] bytesTclass;
		bytesVIN = BitConverter.getBytes(getVIN());
		bytesFirstWord = BitConverter.getBytes(getVehicleNumber().substring(0, 1));
		bytesVnub = BitConverter.getBytes(getVehicleNumber().substring(1, getVehicleNumber().length()));
		bytesTclass = BitConverter.getBytes(getClassificationOfVehicle());
		byte[] bytes = new byte[41];
		if (bytesVIN.length>17)
		{
			System.arraycopy(bytesVIN, 0, bytes, 0,17);
		}
		else
		{
			System.arraycopy(bytesVIN, 0, bytes, 0, bytesVIN.length);
		}
		if (bytesFirstWord.length>2)
		{
			System.arraycopy(bytesFirstWord, 0, bytes, 17, 2);
		}
		else
		{
			System.arraycopy(bytesFirstWord, 0, bytes, 17, bytesFirstWord.length);
		}
		if (bytesVnub.length>10)
		{
			System.arraycopy(bytesVnub, 0, bytes, 19, 10);
		}
		else
		{
			System.arraycopy(bytesVnub, 0, bytes, 19, bytesVnub.length);
		}
		if (bytesTclass.length>12)
		{
			System.arraycopy(bytesTclass, 0, bytes, 30, 12);
		}
		else
		{
			System.arraycopy(bytesTclass, 0, bytes, 30, bytesTclass.length);
		}

		return bytes;

	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setVIN(BitConverter.getString(bytes, 0, 17));

		setVehicleNumber(BitConverter.getString(bytes, 17, 2) + BitConverter.getString(bytes, 19, 10));

		setClassificationOfVehicle(BitConverter.getString(bytes, 29, 12));
	}
}
