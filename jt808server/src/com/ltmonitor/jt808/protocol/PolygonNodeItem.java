package com.ltmonitor.jt808.protocol;

public class PolygonNodeItem
{
	/** 
	 顶点纬度
	 
	*/

//ORIGINAL LINE: private uint latitude;
	private int latitude;

//ORIGINAL LINE: public uint getLatitude()
	public final int getLatitude()
	{
		return latitude;
	}

//ORIGINAL LINE: public void setLatitude(uint value)
	public final void setLatitude(int value)
	{
		latitude = value;
	}
	/** 
	 顶点经度
	 
	*/

//ORIGINAL LINE: private uint longitude;
	private int longitude;

//ORIGINAL LINE: public uint getLongitude()
	public final int getLongitude()
	{
		return longitude;
	}

//ORIGINAL LINE: public void setLongitude(uint value)
	public final void setLongitude(int value)
	{
		longitude = value;
	}
}