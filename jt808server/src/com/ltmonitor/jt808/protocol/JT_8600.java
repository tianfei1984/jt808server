package com.ltmonitor.jt808.protocol;

/** 
 设置圆形区域
 
*/
public class JT_8600 implements IMessageBody
{
	/** 
	 设置属性,0：更新区域；  1：追加区域；  2：修改区域
	 
	*/
	private byte settingType;
	public final byte getSettingType()
	{
		return settingType;
	}
	public final void setSettingType(byte value)
	{
		settingType = value;
	}
	/** 
	 区域总数
	 
	*/
	private byte areasCount;
	public final byte getAreasCount()
	{
		return areasCount;
	}
	public final void setAreasCount(byte value)
	{
		areasCount = value;
	}
	/** 
	 区域项
	 
	*/
	private java.util.ArrayList<CircleAreaItem> circleAreas;
	public final java.util.ArrayList<CircleAreaItem> getCircleAreas()
	{
		return circleAreas;
	}
	public final void setCircleAreas(java.util.ArrayList<CircleAreaItem> value)
	{
		circleAreas = value;
	}

	public final byte[] WriteToBytes()
	{
		MyBuffer bytes = new MyBuffer();
		bytes.put(getSettingType());
		bytes.put(getAreasCount());
		for (CircleAreaItem item : getCircleAreas())
		{
			bytes.put((byte)(item.getCircleAreaId() >> 24));
			bytes.put((byte)(item.getCircleAreaId() >> 16));
			bytes.put((byte)(item.getCircleAreaId() >> 8));
			bytes.put((byte)item.getCircleAreaId());
			bytes.put((byte)(item.getCircleAreaProperty() >> 8));
			bytes.put((byte)item.getCircleAreaProperty());
			bytes.put((byte)(item.getCenterLatitude() >> 24));
			bytes.put((byte)(item.getCenterLatitude() >> 16));
			bytes.put((byte)(item.getCenterLatitude() >> 8));
			bytes.put((byte)item.getCenterLatitude());
			bytes.put((byte)(item.getCenterLongitude() >> 24));
			bytes.put((byte)(item.getCenterLongitude() >> 16));
			bytes.put((byte)(item.getCenterLongitude() >> 8));
			bytes.put((byte)item.getCenterLongitude());
			bytes.put((byte)(item.getRadius() >> 24));
			bytes.put((byte)(item.getRadius() >> 16));
			bytes.put((byte)(item.getRadius() >> 8));
			bytes.put((byte)item.getRadius());
			if ((item.getCircleAreaProperty() & 0x0001) == 0x0001)
			{
				byte[] date1 = BitConverter.getBytes(item.getStartTime());
				byte[] date2 = BitConverter.getBytes(item.getEndTime());

				bytes.put(date1);
				bytes.put(date2);
			}
			if ((item.getCircleAreaProperty() & 0x0002) == 0x0002)
			{
				bytes.put((byte)(item.getMaxSpeed() >> 8));
				bytes.put((byte)item.getMaxSpeed());
				bytes.put(item.getOverSpeedLastTime());
			}
			if ((item.getCircleAreaProperty() & 0x0100) == 0x0100)
			{
				bytes.put(item.getPhotoMaxSpeed());
				bytes.put(item.getLastTimeBelowPhotoMaxSpeed());
				bytes.put(item.getFireOnPhotoInterval());
				bytes.put(item.getFireOffPhotoDelay());
			}
			if ((item.getCircleAreaProperty() & 0x8000) == 0x8000)
			{
				byte[] areaNameBytes = BitConverter.getBytes(item.getCircleAreaName());
				bytes.put((byte)(areaNameBytes.length));
				bytes.put(areaNameBytes);
				//bytes.put(0x00);
			}
		}
		return bytes.array();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setSettingType(bytes[0]);
		setAreasCount(bytes[0]);
		setCircleAreas(new java.util.ArrayList<CircleAreaItem>(getAreasCount()));
		int pos = 2;
		while (pos < bytes.length)
		{
			CircleAreaItem item = new CircleAreaItem();
			item.setCircleAreaId((int)((bytes[pos] << 24) + (bytes[pos + 1] << 16) + (bytes[pos + 2] << 8) + bytes[pos + 3]));
			item.setCircleAreaProperty((short)((bytes[pos + 4] << 8) + bytes[pos + 5]));
			item.setCenterLatitude((int)((bytes[pos + 6] << 24) + (bytes[pos + 8] << 16) + (bytes[pos + 8] << 8) + bytes[pos + 9]));
			item.setCenterLongitude((int)((bytes[pos + 10] << 24) + (bytes[pos + 11] << 16) + (bytes[pos + 12] << 8) + bytes[pos + 13]));
			item.setRadius((int)((bytes[pos + 14] << 24) + (bytes[pos + 15] << 16) + (bytes[pos + 16] << 8) + bytes[pos + 17]));
			pos += 18;
			if ((item.getCircleAreaProperty() & 0x0001) == 0x0001)
			{
				item.setStartTime(new java.util.Date(java.util.Date.parse("20" + String.format("%02X", bytes[pos]) + "-" + String.format("%02X", bytes[pos + 1]) + "-" + String.format("%02X", bytes[pos + 2]) + " " + String.format("%02X", bytes[pos + 3]) + ":" + String.format("%02X", bytes[pos + 4]) + ":" + String.format("%02X", bytes[pos + 5]))));
				item.setEndTime(new java.util.Date(java.util.Date.parse("20" + String.format("%02X", bytes[pos + 6]) + "-" + String.format("%02X", bytes[pos + 7]) + "-" + String.format("%02X", bytes[pos + 8]) + " " + String.format("%02X", bytes[pos + 9]) + ":" + String.format("%02X", bytes[pos + 10]) + ":" + String.format("%02X", bytes[pos + 11]))));
				pos += 12;
			}
			if ((item.getCircleAreaProperty() & 0x0002) == 0x0002)
			{
				item.setMaxSpeed((short)((bytes[pos] << 8) + bytes[pos + 1]));
				item.setOverSpeedLastTime(bytes[pos + 2]);
				pos += 3;
			}
			if ((item.getCircleAreaProperty() & 0x0100) == 0x0100)
			{
				item.setPhotoMaxSpeed(bytes[pos]);
				item.setLastTimeBelowPhotoMaxSpeed(bytes[pos + 1]);
				item.setFireOnPhotoInterval(bytes[pos + 2]);
				item.setFireOffPhotoDelay(bytes[pos + 3]);
				pos += 4;
			}
			if ((item.getCircleAreaProperty() & 0x8000) == 0x8000)
			{
				item.setCircleAreaNameLength(bytes[pos]);
				item.setCircleAreaName(BitConverter.getString(bytes, pos + 1, item.getCircleAreaNameLength()));
				pos = pos + 1 + item.getCircleAreaNameLength();
			}
			getCircleAreas().add(item);
		}
	}

	@Override
	public String toString()
	{
		return String.format("设置属性：%1$s,区域总数：%2$s", getSettingType(), getAreasCount());
	}
}