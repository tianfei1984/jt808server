package com.ltmonitor.jt808.protocol;

public class CircleAreaItem
{
	/** 
	 区域ID
	 
	*/

//ORIGINAL LINE: private uint circleAreaId;
	private int circleAreaId;

//ORIGINAL LINE: public uint getCircleAreaId()
	public final int getCircleAreaId()
	{
		return circleAreaId;
	}

//ORIGINAL LINE: public void setCircleAreaId(uint value)
	public final void setCircleAreaId(int value)
	{
		circleAreaId = value;
	}
	/** 
	 区域属性
	 
	*/

//ORIGINAL LINE: private ushort circleAreaProperty;
	private short circleAreaProperty;

//ORIGINAL LINE: public ushort getCircleAreaProperty()
	public final short getCircleAreaProperty()
	{
		return circleAreaProperty;
	}

//ORIGINAL LINE: public void setCircleAreaProperty(ushort value)
	public final void setCircleAreaProperty(short value)
	{
		circleAreaProperty = value;
	}
	/** 
	 中心点纬度,以度为单位的纬度值乘以10的6次方，精确到百万分之一度
	 
	*/

//ORIGINAL LINE: private uint centerLatitude;
	private int centerLatitude;

//ORIGINAL LINE: public uint getCenterLatitude()
	public final int getCenterLatitude()
	{
		return centerLatitude;
	}

//ORIGINAL LINE: public void setCenterLatitude(uint value)
	public final void setCenterLatitude(int value)
	{
		centerLatitude = value;
	}
	/** 
	 中心点经度,以度为单位的经度值乘以10的6次方，精确到百万分之一度
	 
	*/

//ORIGINAL LINE: private uint centerLongitude;
	private int centerLongitude;

//ORIGINAL LINE: public uint getCenterLongitude()
	public final int getCenterLongitude()
	{
		return centerLongitude;
	}

//ORIGINAL LINE: public void setCenterLongitude(uint value)
	public final void setCenterLongitude(int value)
	{
		centerLongitude = value;
	}
	/** 
	 半径,单位为米（m），路段为该拐点到下一拐点
	 
	*/

//ORIGINAL LINE: private uint radius;
	private int radius;

//ORIGINAL LINE: public uint getRadius()
	public final int getRadius()
	{
		return radius;
	}

//ORIGINAL LINE: public void setRadius(uint value)
	public final void setRadius(int value)
	{
		radius = value;
	}
	/** 
	 起始时间,YY-MM-DD-hh-mm-ss，若区域属性0位为0则没有该字段
	 
	*/
	private java.util.Date startTime = new java.util.Date(0);
	public final java.util.Date getStartTime()
	{
		return startTime;
	}
	public final void setStartTime(java.util.Date value)
	{
		startTime = value;
	}
	/** 
	 结束时间,YY-MM-DD-hh-mm-ss，若区域属性0位为0则没有该字段
	 
	*/
	private java.util.Date endTime = new java.util.Date(0);
	public final java.util.Date getEndTime()
	{
		return endTime;
	}
	public final void setEndTime(java.util.Date value)
	{
		endTime = value;
	}
	/** 
	 最高速度,Km/h，若区域属性1位为0则没有该字段
	 
	*/

//ORIGINAL LINE: private ushort maxSpeed;
	private short maxSpeed;

//ORIGINAL LINE: public ushort getMaxSpeed()
	public final short getMaxSpeed()
	{
		return maxSpeed;
	}

//ORIGINAL LINE: public void setMaxSpeed(ushort value)
	public final void setMaxSpeed(short value)
	{
		maxSpeed = value;
	}
	/** 
	 超速持续时间,单位秒(s),若区域属性1位为0则没有该字段
	 
	*/
	private byte overSpeedLastTime;
	public final byte getOverSpeedLastTime()
	{
		return overSpeedLastTime;
	}
	public final void setOverSpeedLastTime(byte value)
	{
		overSpeedLastTime = value;
	}
	/** 
	 拍照启动最高速度,单位为公里每小时(km/h)，当速度降到最高速度以下就启动拍照,若区域属性8位为0则没有该字段
	 
	*/
	private byte photoMaxSpeed;
	public final byte getPhotoMaxSpeed()
	{
		return photoMaxSpeed;
	}
	public final void setPhotoMaxSpeed(byte value)
	{
		photoMaxSpeed = value;
	}
	/** 
	 速度降到最高速度以下继续时间,单位为秒(S),若区域属性8位为0则没有该字段
	 
	*/
	private byte lastTimeBelowPhotoMaxSpeed;
	public final byte getLastTimeBelowPhotoMaxSpeed()
	{
		return lastTimeBelowPhotoMaxSpeed;
	}
	public final void setLastTimeBelowPhotoMaxSpeed(byte value)
	{
		lastTimeBelowPhotoMaxSpeed = value;
	}
	/** 
	 点火拍照时间间隔,单位5秒，如果为0关闭点火拍照,若区域属性8位为0则没有该字段
	 
	*/
	private byte privateFireOnPhotoInterval;
	public final byte getFireOnPhotoInterval()
	{
		return privateFireOnPhotoInterval;
	}
	public final void setFireOnPhotoInterval(byte value)
	{
		privateFireOnPhotoInterval = value;
	}
	/** 
	 熄火拍照延时时间,单位分钟，若区域属性8位为0则没有
	 
	*/
	private byte privateFireOffPhotoDelay;
	public final byte getFireOffPhotoDelay()
	{
		return privateFireOffPhotoDelay;
	}
	public final void setFireOffPhotoDelay(byte value)
	{
		privateFireOffPhotoDelay = value;
	}
	/** 
	 区域名称长度,若区域属性15位为0 则没有该字段
	 
	*/
	private byte circleAreaNameLength;
	public final byte getCircleAreaNameLength()
	{
		return circleAreaNameLength;
	}
	public final void setCircleAreaNameLength(byte value)
	{
		circleAreaNameLength = value;
	}
	/** 
	 区域名称,经GBK编码 若区域属性15位为0 则没有改字段
	 
	*/
	private String circleAreaName;
	public final String getCircleAreaName()
	{
		return circleAreaName;
	}
	public final void setCircleAreaName(String value)
	{
		circleAreaName = value;
	}
}