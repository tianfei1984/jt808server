package com.ltmonitor.jt808.protocol;

/** 
 设置路线
 
*/
public class JT_8606 implements IMessageBody
{
	/** 
	 路线ID
	 
	*/

//ORIGINAL LINE: private uint routeId;
	private int routeId;

//ORIGINAL LINE: public uint getRouteId()
	public final int getRouteId()
	{
		return routeId;
	}

//ORIGINAL LINE: public void setRouteId(uint value)
	public final void setRouteId(int value)
	{
		routeId = value;
	}
	/** 
	 路线属性
	 
	*/

//ORIGINAL LINE: private ushort routeProperty;
	private short routeProperty;

//ORIGINAL LINE: public ushort getRouteProperty()
	public final short getRouteProperty()
	{
		return routeProperty;
	}

//ORIGINAL LINE: public void setRouteProperty(ushort value)
	public final void setRouteProperty(short value)
	{
		routeProperty = value;
	}
	/** 
	 起始时间
	 
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
	 结束时间
	 
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
	 路线总拐点数
	 
	*/

//ORIGINAL LINE: private ushort routePointsCount;
	private short routePointsCount;

//ORIGINAL LINE: public ushort getRoutePointsCount()
	public final short getRoutePointsCount()
	{
		return routePointsCount;
	}

//ORIGINAL LINE: public void setRoutePointsCount(ushort value)
	public final void setRoutePointsCount(short value)
	{
		routePointsCount = value;
	}
	/** 
	 路线名称长度,若区域属性15位为0 则没有该字段
	 
	*/
	private byte routeNameLength;
	public final byte getRouteNameLength()
	{
		return routeNameLength;
	}
	public final void setRouteNameLength(byte value)
	{
		routeNameLength = value;
	}
	/** 
	 路线名称,经GBK编码, 长度n 若路线属性15位为0 则没有该字段
	 
	*/
	private String routeName;
	public final String getRouteName()
	{
		return routeName;
	}
	public final void setRouteName(String value)
	{
		routeName = value;
	}
	/** 
	 拐点项
	 
	*/
	private java.util.ArrayList<RouteTurnPointItem> turnPoints;
	public final java.util.ArrayList<RouteTurnPointItem> getTurnPoints()
	{
		return turnPoints;
	}
	public final void setTurnPoints(java.util.ArrayList<RouteTurnPointItem> value)
	{
		turnPoints = value;
	}

	public final byte[] WriteToBytes()
	{
		MyBuffer bytes = new MyBuffer();
		bytes.put((byte)(getRouteId() >> 24));
		bytes.put((byte)(getRouteId() >> 16));
		bytes.put((byte)(getRouteId() >> 8));
		bytes.put((byte)getRouteId());
		bytes.put((byte)(getRouteProperty() >> 8));
		bytes.put((byte)getRouteProperty());
		if ((getRouteProperty() & 0x0001) == 0x0001)
		{
			bytes.put(BitConverter.getBytes(getStartTime()));
			bytes.put(BitConverter.getBytes(getEndTime()));
		}
		bytes.put((byte)(getRoutePointsCount() >> 8));
		bytes.put((byte)getRoutePointsCount());
		/**
		if ((getRouteProperty() & 0x8000) == 0x8000)
		{
			byte[] routeNameBytes = BitConverter.getBytes(getRouteName());
			bytes.put((byte)(routeNameBytes.length));
			bytes.put(routeNameBytes);
			//bytes.put(0x00);
		}*/
		for (RouteTurnPointItem ri : getTurnPoints())
		{
			bytes.put((byte)(ri.getRoutePointId() >> 24));
			bytes.put((byte)(ri.getRoutePointId() >> 16));
			bytes.put((byte)(ri.getRoutePointId() >> 8));
			bytes.put((byte)ri.getRoutePointId());
			bytes.put((byte)(ri.getRouteSegmentId() >> 24));
			bytes.put((byte)(ri.getRouteSegmentId() >> 16));
			bytes.put((byte)(ri.getRouteSegmentId() >> 8));
			bytes.put((byte)ri.getRouteSegmentId());
			bytes.put((byte)(ri.getTurnPointLatitude() >> 24));
			bytes.put((byte)(ri.getTurnPointLatitude() >> 16));
			bytes.put((byte)(ri.getTurnPointLatitude() >> 8));
			bytes.put((byte)ri.getTurnPointLatitude());
			bytes.put((byte)(ri.getTurnPointLongitude() >> 24));
			bytes.put((byte)(ri.getTurnPointLongitude() >> 16));
			bytes.put((byte)(ri.getTurnPointLongitude() >> 8));
			bytes.put((byte)ri.getTurnPointLongitude());
			bytes.put(ri.getRouteSegmentWidth());
			bytes.put(ri.getRouteSegmentProperty());
			if ((ri.getRouteSegmentProperty() & 0x01) == 0x01)
			{
				bytes.put((byte)(ri.getMaxDriveTimeLimited() >> 8));
				bytes.put((byte)ri.getMaxDriveTimeLimited());
				bytes.put((byte)(ri.getMinDriveTimeLimited() >> 8));
				bytes.put((byte)ri.getMinDriveTimeLimited());
			}
			if ((ri.getRouteSegmentProperty() & 0x02) == 0x02)
			{
				bytes.put((byte)(ri.getMaxSpeedLimited() >> 8));
				bytes.put((byte)ri.getMaxSpeedLimited());
				bytes.put(ri.getOverMaxSpeedLastTime());
			}
			if ((ri.getRouteSegmentProperty() & 0x80) == 0x80)
			{
				bytes.put((byte)(ri.getRouteStationPointLatitude() >> 24));
				bytes.put((byte)(ri.getRouteStationPointLatitude() >> 16));
				bytes.put((byte)(ri.getRouteStationPointLatitude() >> 8));
				bytes.put((byte)ri.getRouteStationPointLatitude());
				bytes.put((byte)(ri.getRouteStationPointLongitude() >> 24));
				bytes.put((byte)(ri.getRouteStationPointLongitude() >> 16));
				bytes.put((byte)(ri.getRouteStationPointLongitude() >> 8));
				bytes.put((byte)ri.getRouteStationPointLongitude());
				byte[] stationNameBytes = BitConverter.getBytes(ri.getRouteStationName());
				bytes.put((byte)(stationNameBytes.length));
				bytes.put(stationNameBytes);
				//bytes.put(0x00);
			}
		}
		return bytes.array();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setRouteId((int)((bytes[0] << 24) + (bytes[1] << 16) + (bytes[2] << 8) + bytes[3]));
		setRouteProperty((short)((bytes[4] << 8) + bytes[5]));
		int pos = 6;
		if ((getRouteProperty() & 0x0001) == 0x0001)
		{
			setStartTime(new java.util.Date(java.util.Date.parse("20" + String.format("%02X", bytes[pos]) + "-" + String.format("%02X", bytes[pos + 1]) + "-" + String.format("%02X", bytes[pos + 2]) + " " + String.format("%02X", bytes[pos + 3]) + ":" + String.format("%02X", bytes[pos + 4]) + ":" + String.format("%02X", bytes[pos + 5]))));
			setEndTime(new java.util.Date(java.util.Date.parse("20" + String.format("%02X", bytes[pos + 6]) + "-" + String.format("%02X", bytes[pos + 7]) + "-" + String.format("%02X", bytes[pos + 8]) + " " + String.format("%02X", bytes[pos + 9]) + ":" + String.format("%02X", bytes[pos + 10]) + ":" + String.format("%02X", bytes[pos + 11]))));
			pos += 12;
		}
		setRoutePointsCount((short)((bytes[pos] << 8) + bytes[pos + 1]));
		pos += 2;
		if ((getRouteProperty() & 0x8000) == 0x8000)
		{
			setRouteNameLength(bytes[pos]);
			setRouteName(new String(bytes, pos + 1, getRouteNameLength()));
			pos = pos + 1 + getRouteNameLength();
		}
		setTurnPoints(new java.util.ArrayList<RouteTurnPointItem>(getRoutePointsCount()));
		while (pos < bytes.length)
		{
			RouteTurnPointItem ri = new RouteTurnPointItem();
			ri.setRoutePointId((int)((bytes[pos] << 24) + (bytes[pos + 1] << 16) + (bytes[pos + 2] << 8) + bytes[pos + 3]));
			ri.setRouteSegmentId((int)((bytes[pos + 4] << 24) + (bytes[pos + 5] << 16) + (bytes[pos + 6] << 8) + bytes[pos + 7]));
			ri.setTurnPointLatitude((int)((bytes[pos + 8] << 24) + (bytes[pos + 9] << 16) + (bytes[pos + 10] << 8) + bytes[pos + 11]));
			ri.setTurnPointLongitude((int)((bytes[pos + 12] << 24) + (bytes[pos + 13] << 16) + (bytes[pos + 14] << 8) + bytes[pos + 15]));
			ri.setRouteSegmentWidth(bytes[pos + 16]);
			ri.setRouteSegmentProperty(bytes[pos + 17]);
			pos += 18;
			if ((ri.getRouteSegmentProperty() & 0x01) == 0x01)
			{
				ri.setMaxDriveTimeLimited((short)((bytes[pos] << 8) + bytes[pos + 1]));
				ri.setMinDriveTimeLimited((short)((bytes[pos + 2] << 8) + bytes[pos + 3]));
				pos += 4;
			}
			if ((ri.getRouteSegmentProperty() & 0x02) == 0x02)
			{
				ri.setMaxSpeedLimited((short)((bytes[pos] << 8) + bytes[pos + 1]));
				ri.setOverMaxSpeedLastTime(bytes[pos + 2]);
				pos += 3;
			}
			if ((ri.getRouteSegmentProperty() & 0x80) == 0x80)
			{
				ri.setRouteStationPointLatitude((int)((bytes[pos] << 24) + (bytes[pos + 1] << 16) + (bytes[pos + 2] << 8) + bytes[pos + 3]));
				ri.setRouteStationPointLongitude((int)((bytes[pos + 4] << 24) + (bytes[pos + 5] << 16) + (bytes[pos + 6] << 8) + bytes[pos + 7]));
				ri.setRouteStationNameLength(bytes[pos + 8]);
				ri.setRouteStationName(new String(bytes, pos + 9, ri.getRouteStationNameLength()));
				pos = pos + 9 + ri.getRouteStationNameLength();
			}
			getTurnPoints().add(ri);
		}
	}
}