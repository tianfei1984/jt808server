package com.ltmonitor.jt808.protocol;

/** 
 评价器数据上传
 
*/
public class JT_0782 implements IMessageBody
{
	/** 
	 时间
	 
	*/
	private java.util.Date time = new java.util.Date(0);
	public final java.util.Date getTime()
	{
		return time;
	}
	public final void setTime(java.util.Date value)
	{
		time = value;
	}
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
	 评分
	 
	*/
	private byte privateGrade;
	public final byte getGrade()
	{
		return privateGrade;
	}
	public final void setGrade(byte value)
	{
		privateGrade = value;
	}
	public final byte[] WriteToBytes()
	{
		MyBuffer bytes = new MyBuffer();
		byte[] date1 = BitConverter.getBytes(getTime());
		bytes.put(date1);
		byte[] driverNameBytes = BitConverter.getBytes(getDriverName());
		bytes.put((byte)(driverNameBytes.length));
		bytes.put(driverNameBytes);
		//bytes.put(0x00);
		byte[] temp = new byte[19];
		byte[] driverIdBytes = BitConverter.getBytes(getDriverID());
		if (driverIdBytes.length >= 19)
		{
			System.arraycopy(driverIdBytes, 0, temp, 0, 19);
		}
		else
		{
			System.arraycopy(driverIdBytes, 0, temp, 0, driverIdBytes.length);
			int pos = driverIdBytes.length;
			while (pos < 19)
			{
				temp[pos] = 0x00;
				pos++;
			}
		}
		bytes.put(temp);
		bytes.put(0x00);
		bytes.put(getGrade());
		return bytes.array();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setTime(new java.util.Date(java.util.Date.parse("20" + String.format("%02X", bytes[0]) + "-" + String.format("%02X", bytes[1]) + "-" + String.format("%02X", bytes[2]) + " " + String.format("%02X", bytes[3]) + ":" + String.format("%02X", bytes[4]) + ":" + String.format("%02X", bytes[5]))));
		setDriverNameLength(bytes[6]);
		setDriverName(BitConverter.getString(bytes, 7, getDriverNameLength()));
		setDriverID(BitConverter.getString(bytes, 7 + getDriverNameLength(), 20));
		setGrade(bytes[27 + getDriverNameLength()]);
	}

	@Override
	public String toString()
	{
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(String.format("司机名称：%1$s,司机ID：%2$s,时间：%3$s,评分：%4$s", getDriverName(), getDriverID(), getTime(), getGrade()));
		return sBuilder.toString();
	}
}