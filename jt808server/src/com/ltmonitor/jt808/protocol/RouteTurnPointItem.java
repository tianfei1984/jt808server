package com.ltmonitor.jt808.protocol;

public class RouteTurnPointItem
{
	/** 
	 拐点ID
	 
	*/

//ORIGINAL LINE: private uint routePointId;
	private int routePointId;

//ORIGINAL LINE: public uint getRoutePointId()
	public final int getRoutePointId()
	{
		return routePointId;
	}

//ORIGINAL LINE: public void setRoutePointId(uint value)
	public final void setRoutePointId(int value)
	{
		routePointId = value;
	}
	/** 
	 路段ID
	 
	*/

//ORIGINAL LINE: private uint routeSegmentId;
	private int routeSegmentId;

//ORIGINAL LINE: public uint getRouteSegmentId()
	public final int getRouteSegmentId()
	{
		return routeSegmentId;
	}

//ORIGINAL LINE: public void setRouteSegmentId(uint value)
	public final void setRouteSegmentId(int value)
	{
		routeSegmentId = value;
	}
	/** 
	 拐点纬度,以度为单位纬度值乘以10的6次方，精确到百万分之一度
	 
	*/

//ORIGINAL LINE: private uint turnPointLatitude;
	private int turnPointLatitude;

//ORIGINAL LINE: public uint getTurnPointLatitude()
	public final int getTurnPointLatitude()
	{
		return turnPointLatitude;
	}

//ORIGINAL LINE: public void setTurnPointLatitude(uint value)
	public final void setTurnPointLatitude(int value)
	{
		turnPointLatitude = value;
	}
	/** 
	 拐点经度,以度为单位经度值乘以10的6次方，精确到百万分之一度
	 
	*/

//ORIGINAL LINE: private uint turnPointLongitude;
	private int turnPointLongitude;

//ORIGINAL LINE: public uint getTurnPointLongitude()
	public final int getTurnPointLongitude()
	{
		return turnPointLongitude;
	}

//ORIGINAL LINE: public void setTurnPointLongitude(uint value)
	public final void setTurnPointLongitude(int value)
	{
		turnPointLongitude = value;
	}
	/** 
	 路段宽度,单位为米（m），路段为该拐点到下一拐点
	 
	*/
	private byte routeSegmentWidth;
	public final byte getRouteSegmentWidth()
	{
		return routeSegmentWidth;
	}
	public final void setRouteSegmentWidth(byte value)
	{
		routeSegmentWidth = value;
	}
	/** 
	 路段属性
	 
	*/
	private byte routeSegmentProperty;
	public final byte getRouteSegmentProperty()
	{
		return routeSegmentProperty;
	}
	public final void setRouteSegmentProperty(byte value)
	{
		routeSegmentProperty = value;
	}
	/** 
	 路段行驶过长阈值,单位为秒（s），若路段属性0位为0则没有该字段
	 
	*/

//ORIGINAL LINE: private ushort maxDriveTimeLimited;
	private short maxDriveTimeLimited;

//ORIGINAL LINE: public ushort getMaxDriveTimeLimited()
	public final short getMaxDriveTimeLimited()
	{
		return maxDriveTimeLimited;
	}

//ORIGINAL LINE: public void setMaxDriveTimeLimited(ushort value)
	public final void setMaxDriveTimeLimited(short value)
	{
		maxDriveTimeLimited = value;
	}
	/** 
	 路段行驶不足阈值,单位为秒（s），若路段属性0位为0则没有该字段
	 
	*/

//ORIGINAL LINE: private ushort minDriveTimeLimited;
	private short minDriveTimeLimited;

//ORIGINAL LINE: public ushort getMinDriveTimeLimited()
	public final short getMinDriveTimeLimited()
	{
		return minDriveTimeLimited;
	}

//ORIGINAL LINE: public void setMinDriveTimeLimited(ushort value)
	public final void setMinDriveTimeLimited(short value)
	{
		minDriveTimeLimited = value;
	}
	/** 
	 路段最高速度,单位为公里每小时（km/h），若路段属性1位为0则没有该字段
	 
	*/

//ORIGINAL LINE: private ushort maxSpeedLimited;
	private short maxSpeedLimited;

//ORIGINAL LINE: public ushort getMaxSpeedLimited()
	public final short getMaxSpeedLimited()
	{
		return maxSpeedLimited;
	}

//ORIGINAL LINE: public void setMaxSpeedLimited(ushort value)
	public final void setMaxSpeedLimited(short value)
	{
		maxSpeedLimited = value;
	}
	/** 
	 路段超速持续时间,单位为秒（s），若路段属性1位为0则没有该字段
	 
	*/
	private byte overMaxSpeedLastTime;
	public final byte getOverMaxSpeedLastTime()
	{
		return overMaxSpeedLastTime;
	}
	public final void setOverMaxSpeedLastTime(byte value)
	{
		overMaxSpeedLastTime = value;
	}
	/** 
	 站点纬度,以度为单位纬度值乘以10的6次方，精确到百万分之一度；若路段属性7位为0则没有该字段
	 
	*/

//ORIGINAL LINE: private uint routeStationPointLatitude;
	private int routeStationPointLatitude;

//ORIGINAL LINE: public uint getRouteStationPointLatitude()
	public final int getRouteStationPointLatitude()
	{
		return routeStationPointLatitude;
	}

//ORIGINAL LINE: public void setRouteStationPointLatitude(uint value)
	public final void setRouteStationPointLatitude(int value)
	{
		routeStationPointLatitude = value;
	}
	/** 
	 站点经度,以度为单位经度值乘以10的6次方，精确到百万分之一度；若路段属性7位为0则没有该字段
	 
	*/

//ORIGINAL LINE: private uint routeStationPointLongitude;
	private int routeStationPointLongitude;

//ORIGINAL LINE: public uint getRouteStationPointLongitude()
	public final int getRouteStationPointLongitude()
	{
		return routeStationPointLongitude;
	}

//ORIGINAL LINE: public void setRouteStationPointLongitude(uint value)
	public final void setRouteStationPointLongitude(int value)
	{
		routeStationPointLongitude = value;
	}
	/** 
	 站点名称长度,若路段属性7位为0则没有该字段
	 
	*/
	private byte routeStationNameLength;
	public final byte getRouteStationNameLength()
	{
		return routeStationNameLength;
	}
	public final void setRouteStationNameLength(byte value)
	{
		routeStationNameLength = value;
	}
	/** 
	 站点名称,经GBK编码, 长度n若路段属性7位为0则没有该字段
	 
	*/
	private String routeStationName;
	public final String getRouteStationName()
	{
		return routeStationName;
	}
	public final void setRouteStationName(String value)
	{
		routeStationName = value;
	}


}