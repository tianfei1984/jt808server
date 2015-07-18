package com.ltmonitor.jt808.protocol;

/** 
 设置多边形区域
 
*/
public class JT_8604 implements IMessageBody
{
	/** 
	 区域ID
	 
	*/

//ORIGINAL LINE: private uint polygonAreaId;
	private int polygonAreaId;

//ORIGINAL LINE: public uint getPolygonAreaId()
	public final int getPolygonAreaId()
	{
		return polygonAreaId;
	}

//ORIGINAL LINE: public void setPolygonAreaId(uint value)
	public final void setPolygonAreaId(int value)
	{
		polygonAreaId = value;
	}
	/** 
	 区域属性
	 
	*/

//ORIGINAL LINE: private ushort polygonAreaProperty;
	private short polygonAreaProperty;

//ORIGINAL LINE: public ushort getPolygonAreaProperty()
	public final short getPolygonAreaProperty()
	{
		return polygonAreaProperty;
	}

//ORIGINAL LINE: public void setPolygonAreaProperty(ushort value)
	public final void setPolygonAreaProperty(short value)
	{
		polygonAreaProperty = value;
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
	 区域总顶点数
	 
	*/

//ORIGINAL LINE: private ushort areaNodesCount;
	private short areaNodesCount;

//ORIGINAL LINE: public ushort getAreaNodesCount()
	public final short getAreaNodesCount()
	{
		return areaNodesCount;
	}

//ORIGINAL LINE: public void setAreaNodesCount(ushort value)
	public final void setAreaNodesCount(short value)
	{
		areaNodesCount = value;
	}

	/** 
	 区域名称长度,若区域属性15位为0 则没有该字段
	 
	*/
	private byte polygonAreaNameLength;
	public final byte getPolygonAreaNameLength()
	{
		return polygonAreaNameLength;
	}
	public final void setPolygonAreaNameLength(byte value)
	{
		polygonAreaNameLength = value;
	}
	/** 
	 区域名称,经GBK编码 若区域属性15位为0 则没有改字段
	 
	*/
	private String polygonAreaName;
	public final String getPolygonAreaName()
	{
		return polygonAreaName;
	}
	public final void setPolygonAreaName(String value)
	{
		polygonAreaName = value;
	}
	/** 
	 顶点项
	 
	*/
	private java.util.ArrayList<PolygonNodeItem> privateNodes;
	public final java.util.ArrayList<PolygonNodeItem> getNodes()
	{
		return privateNodes;
	}
	public final void setNodes(java.util.ArrayList<PolygonNodeItem> value)
	{
		privateNodes = value;
	}

	public final byte[] WriteToBytes()
	{
		MyBuffer bytes = new MyBuffer();
		bytes.put((byte)(getPolygonAreaId() >> 24));
		bytes.put((byte)(getPolygonAreaId() >> 16));
		bytes.put((byte)(getPolygonAreaId() >> 8));
		bytes.put((byte)getPolygonAreaId());
		bytes.put((byte)(getPolygonAreaProperty() >> 8));
		bytes.put((byte)getPolygonAreaProperty());
		if ((getPolygonAreaProperty() & 0x0001) == 0x0001)
		{
			byte[] date1 = BitConverter.getBytes(getStartTime());
			byte[] date2 = BitConverter.getBytes(getEndTime());

			bytes.put(date1);
			bytes.put(date2);
		
		}
		if ((getPolygonAreaProperty() & 0x0002) == 0x0002)
		{
			bytes.put((byte)(getMaxSpeed() >> 8));
			bytes.put((byte)getMaxSpeed());
			bytes.put(getOverSpeedLastTime());
		}
		if ((getPolygonAreaProperty() & 0x0100) == 0x0100)
		{
			bytes.put(getPhotoMaxSpeed());
			bytes.put(getLastTimeBelowPhotoMaxSpeed());
			bytes.put(getFireOnPhotoInterval());
			bytes.put(getFireOffPhotoDelay());
		}
		bytes.put((byte)(getAreaNodesCount() >> 8));
		bytes.put((byte)getAreaNodesCount());
		/**
		if ((getPolygonAreaProperty() & 0x8000) == 0x8000)
		{
			byte[] areaNameBytes = BitConverter.getBytes(getPolygonAreaName());
			bytes.put((byte)(areaNameBytes.length));
			bytes.put(areaNameBytes);
			//bytes.put(0x00);
		}*/
		for (PolygonNodeItem ni : this.privateNodes)
		{
			bytes.put((byte)(ni.getLatitude() >> 24));
			bytes.put((byte)(ni.getLatitude() >> 16));
			bytes.put((byte)(ni.getLatitude() >> 8));
			bytes.put((byte)ni.getLatitude());
			bytes.put((byte)(ni.getLongitude() >> 24));
			bytes.put((byte)(ni.getLongitude() >> 16));
			bytes.put((byte)(ni.getLongitude() >> 8));
			bytes.put((byte)ni.getLongitude());
		}
		return bytes.array();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setPolygonAreaId((int)((bytes[0] << 24) + (bytes[1] << 16) + (bytes[2] << 8) + bytes[3]));
		setPolygonAreaProperty((short)((bytes[4] << 8) + bytes[5]));
		int pos = 6;
		if ((getPolygonAreaProperty() & 0x0001) == 0x0001)
		{
			setStartTime(new java.util.Date(java.util.Date.parse("20" + String.format("%02X", bytes[pos]) + "-" + String.format("%02X", bytes[pos + 1]) + "-" + String.format("%02X", bytes[pos + 2]) + " " + String.format("%02X", bytes[pos + 3]) + ":" + String.format("%02X", bytes[pos + 4]) + ":" + String.format("%02X", bytes[pos + 5]))));
			setEndTime(new java.util.Date(java.util.Date.parse("20" + String.format("%02X", bytes[pos + 6]) + "-" + String.format("%02X", bytes[pos + 7]) + "-" + String.format("%02X", bytes[pos + 8]) + " " + String.format("%02X", bytes[pos + 9]) + ":" + String.format("%02X", bytes[pos + 10]) + ":" + String.format("%02X", bytes[pos + 11]))));
			pos += 12;
		}
		if ((getPolygonAreaProperty() & 0x0002) == 0x0002)
		{
			setMaxSpeed((short)((bytes[pos] << 8) + bytes[pos + 1]));
			setOverSpeedLastTime(bytes[pos + 2]);
			pos += 3;
		}
		if ((getPolygonAreaProperty() & 0x0100) == 0x0100)
		{
			setPhotoMaxSpeed(bytes[pos]);
			setLastTimeBelowPhotoMaxSpeed(bytes[pos + 1]);
			setFireOnPhotoInterval(bytes[pos + 2]);
			setFireOffPhotoDelay(bytes[pos + 3]);
			pos += 4;
		}
		setAreaNodesCount((short)((bytes[pos] << 8) + bytes[pos + 1]));
		pos += 2;
		if ((getPolygonAreaProperty() & 0x8000) == 0x8000)
		{
			setPolygonAreaNameLength(bytes[pos]);
			setPolygonAreaName(BitConverter.getString(bytes, pos + 1, getPolygonAreaNameLength()));
			pos = pos + 1 + getPolygonAreaNameLength();
		}
		setNodes(new java.util.ArrayList<PolygonNodeItem>(getAreaNodesCount()));
		while (pos < bytes.length)
		{
			PolygonNodeItem pi = new PolygonNodeItem();
			pi.setLatitude((int)((bytes[pos] << 24) + (bytes[pos + 1] << 16) + (bytes[pos + 2]) + bytes[pos + 3]));
			pi.setLongitude((int)((bytes[pos + 4] << 24) + (bytes[pos + 5] << 16) + (bytes[pos + 6]) + bytes[pos + 7]));
			getNodes().add(pi);
			pos += 8;
		}
	}
}