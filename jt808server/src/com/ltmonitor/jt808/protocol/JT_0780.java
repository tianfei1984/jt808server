package com.ltmonitor.jt808.protocol;

/** 
 驾驶员身份信息采集上报(退签)
 
*/
public class JT_0780 implements IMessageBody
{
	/** 
	 驾驶员姓名长度
	 
	*/
	private byte driverNameLength;
	public final byte getDriverNameLength()
	{
		return driverNameLength;
	}
	public final void setDriverNameLength(byte value)
	{
		driverNameLength = value;
	}
	/** 
	 驾驶员姓名
	 
	*/
	private String driverName;
	public final String getDriverName()
	{
		return driverName;
	}
	public final void setDriverName(String value)
	{
		driverName = value;
	}
	/** 
	 驾驶员身份证编码
	 
	*/
	private String driverID;
	public final String getDriverID()
	{
		return driverID;
	}
	public final void setDriverID(String value)
	{
		driverID = value;
	}
	/** 
	 从业资格证编码
	 
	*/
	private String certificationCode;
	public final String getCertificationCode()
	{
		return certificationCode;
	}
	public final void setCertificationCode(String value)
	{
		certificationCode = value;
	}
	/** 
	 发证机构名称长度
	 
	*/
	private byte agencyNameLength;
	public final byte getAgencyNameLength()
	{
		return agencyNameLength;
	}
	public final void setAgencyNameLength(byte value)
	{
		agencyNameLength = value;
	}
	/** 
	 发证机构名称
	 
	*/
	private String agencyName;
	public final String getAgencyName()
	{
		return agencyName;
	}
	public final void setAgencyName(String value)
	{
		agencyName = value;
	}


	public final byte[] WriteToBytes()
	{
		MyBuffer bytes = new MyBuffer();
		byte[] driverNameBytes = BitConverter.getBytes(getDriverName());
		bytes.put((byte)(driverNameBytes.length));
		bytes.put(driverNameBytes);
		//bytes.put(0x00);
		byte[] driverIdBytes = BitConverter.getBytes(getDriverID());
		byte[] temp = new byte[19];
		if (driverNameBytes.length >= 19)
		{
			System.arraycopy(driverNameBytes, 0, temp, 0, 19);
		}
		else
		{
			System.arraycopy(driverNameBytes, 0, temp, 0, driverNameBytes.length);
			int pos = driverNameBytes.length;
			while (pos < 19)
			{
				temp[pos] = 0x00;
				pos++;
			}
		}
		bytes.put(temp);
		bytes.put(0x00);
		byte[] certiBytes = BitConverter.getBytes(getCertificationCode());
		byte[] temp2 = new byte[39];
		if (certiBytes.length >= 39)
		{
			System.arraycopy(certiBytes, 0, temp2, 0, 39);
		}
		else
		{
			System.arraycopy(certiBytes, 0, temp2, 0, certiBytes.length);
			int pos = certiBytes.length;
			while (pos < 39)
			{
				temp2[pos] = 0x00;
				pos++;
			}
		}
		bytes.put(temp2);
		bytes.put(0x00);
		byte[] agencyNameBytes = BitConverter.getBytes(getAgencyName());
		bytes.put((byte)(getAgencyNameLength() + 1));
		bytes.put(agencyNameBytes);
		return bytes.array();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setDriverNameLength(bytes[0]);
		setDriverName(BitConverter.getString(bytes, 1, getDriverNameLength()));
		setDriverID(BitConverter.getString(bytes, getDriverNameLength() + 1, 20));
		setCertificationCode(BitConverter.getString(bytes, getDriverNameLength() + 21, 40));
		setAgencyNameLength(bytes[getDriverNameLength() + 61]);
		setAgencyName(BitConverter.getString(bytes, getDriverNameLength() + 62, getAgencyNameLength()));
		int pos = getDriverNameLength() + 62 + getAgencyNameLength();
	}

	@Override
	public String toString()
	{
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(String.format("司机名称：%1$s,司机ID：%2$s,从业资格证：%3$s,发证机构：%4$s", getDriverName(), getDriverID(), getCertificationCode(), getAgencyName()));
		return sBuilder.toString();
	}
}