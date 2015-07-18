package com.ltmonitor.jt808.protocol;

public class RectangleAreaItem
{
	/** 
	 区域ID
	 
	*/

//ORIGINAL LINE: private uint rectangleAreaId;
	private int rectangleAreaId;

//ORIGINAL LINE: public uint getRectangleAreaId()
	public final int getRectangleAreaId()
	{
		return rectangleAreaId;
	}

//ORIGINAL LINE: public void setRectangleAreaId(uint value)
	public final void setRectangleAreaId(int value)
	{
		rectangleAreaId = value;
	}
	/** 
	 区域属性
	 
	*/

//ORIGINAL LINE: private ushort rectangleAreaProperty;
	private short rectangleAreaProperty;

//ORIGINAL LINE: public ushort getRectangleAreaProperty()
	public final short getRectangleAreaProperty()
	{
		return rectangleAreaProperty;
	}

//ORIGINAL LINE: public void setRectangleAreaProperty(ushort value)
	public final void setRectangleAreaProperty(short value)
	{
		rectangleAreaProperty = value;
	}
	/** 
	 左上点纬度,以度为单位的纬度值乘以10的6次方，精确到百万分之一度
	 
	*/

//ORIGINAL LINE: private uint leftTopLatitude;
	private int leftTopLatitude;

//ORIGINAL LINE: public uint getLeftTopLatitude()
	public final int getLeftTopLatitude()
	{
		return leftTopLatitude;
	}

//ORIGINAL LINE: public void setLeftTopLatitude(uint value)
	public final void setLeftTopLatitude(int value)
	{
		leftTopLatitude = value;
	}
	/** 
	 左上点经度,以度为单位的经度值乘以10的6次方，精确到百万分之一度
	 
	*/

//ORIGINAL LINE: private uint rightBottomLongitude;
	private int rightBottomLongitude;

//ORIGINAL LINE: public uint getRightBottomLongitude()
	public final int getRightBottomLongitude()
	{
		return rightBottomLongitude;
	}

//ORIGINAL LINE: public void setRightBottomLongitude(uint value)
	public final void setRightBottomLongitude(int value)
	{
		rightBottomLongitude = value;
	}
	/** 
	 右下点纬度,以度为单位的纬度值乘以10的6次方，精确到百万分之一度
	 
	*/

//ORIGINAL LINE: private uint rightBottomLatitude;
	private int rightBottomLatitude;

//ORIGINAL LINE: public uint getRightBottomLatitude()
	public final int getRightBottomLatitude()
	{
		return rightBottomLatitude;
	}

//ORIGINAL LINE: public void setRightBottomLatitude(uint value)
	public final void setRightBottomLatitude(int value)
	{
		rightBottomLatitude = value;
	}
	/** 
	 右下点经度,以度为单位的经度值乘以10的6次方，精确到百万分之一度
	 
	*/

//ORIGINAL LINE: private uint leftTopLongitude;
	private int leftTopLongitude;

//ORIGINAL LINE: public uint getLeftTopLongitude()
	public final int getLeftTopLongitude()
	{
		return leftTopLongitude;
	}

//ORIGINAL LINE: public void setLeftTopLongitude(uint value)
	public final void setLeftTopLongitude(int value)
	{
		leftTopLongitude = value;
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
	private byte rectangleAreaNameLength;
	public final byte getRectangleAreaNameLength()
	{
		return rectangleAreaNameLength;
	}
	public final void setRectangleAreaNameLength(byte value)
	{
		rectangleAreaNameLength = value;
	}
	/** 
	 区域名称,经GBK编码 若区域属性15位为0 则没有改字段
	 
	*/
	private String rectangleAreaName;
	public final String getRectangleAreaName()
	{
		return rectangleAreaName;
	}
	public final void setRectangleAreaName(String value)
	{
		rectangleAreaName = value;
	}
}