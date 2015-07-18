package com.ltmonitor.jt808.protocol.jt2012;

import com.ltmonitor.jt808.protocol.BitConverter;
import com.ltmonitor.jt808.tool.DateUtil;

/** 
 采集指定的位置信息记录
 
*/
public class Recorder_LocationInformation implements IRecorderDataBlock_2012
{
	//一小时的数据
	private java.util.HashMap<Integer, String> privateOneHourPlaceInfo;
	public final java.util.HashMap<Integer, String> getOneHourPlaceInfo()
	{
		return privateOneHourPlaceInfo;
	}
	public final void setOneHourPlaceInfo(java.util.HashMap<Integer, String> value)
	{
		privateOneHourPlaceInfo = value;
	}
	private java.util.HashMap<Integer, String> privateOneSpeedPlaceInfo;
	public final java.util.HashMap<Integer, String> getOneSpeedPlaceInfo()
	{
		return privateOneSpeedPlaceInfo;
	}
	public final void setOneSpeedPlaceInfo(java.util.HashMap<Integer, String> value)
	{
		privateOneSpeedPlaceInfo = value;
	}
	//每一小时的时候据
	private static java.util.HashMap<java.util.Date, java.util.HashMap<Integer, String>> privateHoursPlaceInfo;
	public static java.util.HashMap<java.util.Date, java.util.HashMap<Integer, String>> getHoursPlaceInfo()
	{
		return privateHoursPlaceInfo;
	}
	public static void setHoursPlaceInfo(java.util.HashMap<java.util.Date, java.util.HashMap<Integer, String>> value)
	{
		privateHoursPlaceInfo = value;
	}
	private static java.util.HashMap<java.util.Date, java.util.HashMap<Integer, String>> privateHoursPeedInfo;
	public static java.util.HashMap<java.util.Date, java.util.HashMap<Integer, String>> getHoursPeedInfo()
	{
		return privateHoursPeedInfo;
	}
	public static void setHoursPeedInfo(java.util.HashMap<java.util.Date, java.util.HashMap<Integer, String>> value)
	{
		privateHoursPeedInfo = value;
	}


	/** 
	 命令字
	 
	*/
	public final byte getCommandWord()
	{
		return 0x06;
	}

	/** 
	 数据块长度
	 
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public ushort getDataLength()
	public final short getDataLength()
	{
		return 87;
	}

	private java.util.Date privateBenginTime = new java.util.Date(0);
	public final java.util.Date getBenginTime()
	{
		return privateBenginTime;
	}
	public final void setBenginTime(java.util.Date value)
	{
		privateBenginTime = value;
	}

	private java.util.Date privateEndTime = new java.util.Date(0);
	public final java.util.Date getEndTime()
	{
		return privateEndTime;
	}
	public final void setEndTime(java.util.Date value)
	{
		privateEndTime = value;
	}

	private int privateMaxNumber;
	public final int getMaxNumber()
	{
		return privateMaxNumber;
	}
	public final void setMaxNumber(int value)
	{
		privateMaxNumber = value;
	}

	public final byte[] WriteToBytes()
	{
		byte[] bytes = new byte[14];
		
		String strBeginTime = DateUtil.toStringByFormat(this.getBenginTime(), "yyMMddHHmmss");

		//String begintimeyear = getBenginTime().Year.toString();
		String begintimeyear2 = strBeginTime.substring(0, 2);
		String begintimemonth = strBeginTime.substring(2, 4);
		String begintimeday = strBeginTime.substring(4, 6);
		String begintimehour = strBeginTime.substring(6, 8);
		String begintimeminute = strBeginTime.substring(8, 10);
		String begintimesecond = strBeginTime.substring(10, 12);
		byte[] byteBegintime = Time(begintimeyear2, begintimemonth, begintimeday, begintimehour, begintimeminute, begintimesecond);

		String strEndTime = DateUtil.toStringByFormat(this.getBenginTime(), "yyMMddHHmmss");
		String endtimeyear2 = strEndTime.substring(0, 2);
		String endtimemonth = strEndTime.substring(2, 4);
		String endtimeday = strEndTime.substring(4, 6);
		String endtimehour = strEndTime.substring(6, 8);
		String endtimeminute = strEndTime.substring(8, 10);
		String endtimesecond = strEndTime.substring(10, 12);
		byte[] byteEedTime = Time(endtimeyear2, endtimemonth, endtimeday, endtimehour, endtimeminute, endtimesecond);
		System.arraycopy(byteBegintime, 0, bytes, 0, 6);
		System.arraycopy(byteEedTime, 0, bytes, 6, 6);
		bytes[12] = (byte)(getMaxNumber() >> 8);
		bytes[13] = (byte)(getMaxNumber());

		return bytes;
	}

	public final byte[] Time(String year, String month, String day, String hour, String minte, String second)
	{
		byte years;
		int a, b;
		if (year.length() > 1)
		{
			a = Integer.parseInt(year.substring(0, 1));
			b = Integer.parseInt(year.substring(1, 2));
			years = (byte)((int)(a << 4) + (int)b);
		}
		else
		{
			a = Integer.parseInt(year.substring(0, 1));
			years = (byte)((int)a);
		}

		byte months;
		if (month.length() > 1)
		{
			a = Integer.parseInt(month.substring(0, 1));
			b = Integer.parseInt(month.substring(1, 2));
			months = (byte)((int)(a << 4) + (int)b);
		}
		else
		{
			a = Integer.parseInt(year.substring(0, 1));
			months = (byte)((int)a);
		}

		byte days;
		if (day.length() > 1)
		{
			a = Integer.parseInt(day.substring(0, 1));
			b = Integer.parseInt(day.substring(1, 2));
			days = (byte)((int)(a << 4) + (int)b);
		}
		else
		{
			a = Integer.parseInt(day.substring(0, 1));
			days = (byte)((int)a);
		}

		byte hours;
		if (hour.length() > 1)
		{
			a = Integer.parseInt(hour.substring(0, 1));
			b = Integer.parseInt(hour.substring(1, 2));
			hours = (byte)((int)(a << 4) + (int)b);
		}
		else
		{
			a = Integer.parseInt(hour.substring(0, 1));
			hours = (byte)((int)a);
		}


		byte mintes;
		if (minte.length() > 1)
		{
			a = Integer.parseInt(minte.substring(0, 1));
			b = Integer.parseInt(minte.substring(1, 2));
			mintes = (byte)((int)(a << 4) + (int)b);
		}
		else
		{
			a = Integer.parseInt(minte.substring(0, 1));
			mintes = (byte)((int)a);
		}


		byte seconds;
		if (second.length() > 1)
		{
			a = Integer.parseInt(second.substring(0, 1));
			b = Integer.parseInt(second.substring(1, 2));
			seconds = (byte)((int)(a << 4) + (int)b);
		}
		else
		{
			a = Integer.parseInt(second.substring(0, 1));
			seconds = (byte)((int)a);
		}

		byte[] date = new byte[6];

		date[0] = years;
		date[1] = months;
		date[2] = days;
		date[3] = hours;
		date[4] = mintes;
		date[5] = seconds;

		return date;
	}



	public final void ReadFromBytes(byte[] bytes)
	{
		getHoursPeedInfo().clear();
		getHoursPlaceInfo().clear();
		StringBuilder sb = new StringBuilder();
		if (bytes != null)
		{
			for (int i = 0; i < bytes.length / 666; i++)
			{
				byte[] OneHourDate = new byte[666];
				System.arraycopy(bytes, 0 + 666 * i, OneHourDate, 0, 666);

				byte[] beginTime = new byte[6];
				System.arraycopy(OneHourDate, 0, beginTime, 0, 6);
				java.util.Date time = new java.util.Date(java.util.Date.parse("20" + String.format("%02X", beginTime[0]) + "-" + String.format("%02X", beginTime[1]) + "-" + String.format("%02X", beginTime[2]) + " " + String.format("%02X", beginTime[3]) + ":" + String.format("%02X", beginTime[4]) + ":" + String.format("%02X", beginTime[5])));

				for (int j = 0; j < 60; j++)
				{
					byte[] placeInfo = new byte[10];
					System.arraycopy(OneHourDate, 6 + 11 * j, placeInfo, 0, 10);
					String PlaceInfo = GetPlaceInfo(placeInfo);

					byte minspeed = OneHourDate[16 + 11 * j];
					int speed = BitConverter.ToUInt32(minspeed);

					getOneHourPlaceInfo().put(j, PlaceInfo);
					getOneSpeedPlaceInfo().put(j, (new Integer(speed)).toString());

				}
				getHoursPlaceInfo().put(time, getOneHourPlaceInfo());
				getHoursPeedInfo().put(time, getOneSpeedPlaceInfo());
				setOneHourPlaceInfo(new java.util.HashMap<Integer, String>());
				setOneSpeedPlaceInfo(new java.util.HashMap<Integer, String>());
			}
		}
	}

	/** 
	 获取地点信息
	 
	 @param placeInfo
	 @return 
	*/
	private String GetPlaceInfo(byte[] placeInfo)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("经度为：" + (int)((int)(placeInfo[0] << 24) + (int)(placeInfo[1] << 16) + (int)(placeInfo[2] << 8) + (int)(placeInfo[3])) * 0.0001);

		sb.append("纬度为：" + (int)((int)(placeInfo[4] << 24) + (int)(placeInfo[5] << 16) + (int)(placeInfo[6] << 8) + (int)(placeInfo[7])) * 0.0001);

		sb.append("海拔高度为：" + (int)((int)(placeInfo[8] << 8) + (int)(placeInfo[9])));

		return sb.toString();
	}


}
