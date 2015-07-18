package com.ltmonitor.jt808.protocol;

/** 
 设置矩形区域
 
*/
public class JT_8602 implements IMessageBody
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
	private java.util.ArrayList<RectangleAreaItem> rectangleAreas;
	public final java.util.ArrayList<RectangleAreaItem> getRectangleAreas()
	{
		return rectangleAreas;
	}
	public final void setRectangleAreas(java.util.ArrayList<RectangleAreaItem> value)
	{
		rectangleAreas = value;
	}

	public final byte[] WriteToBytes()
	{
		MyBuffer bytes = new MyBuffer();
		bytes.put(getSettingType());
		bytes.put(getAreasCount());
		for (RectangleAreaItem item : getRectangleAreas())
		{
			bytes.put((byte)(item.getRectangleAreaId() >> 24));
			bytes.put((byte)(item.getRectangleAreaId() >> 16));
			bytes.put((byte)(item.getRectangleAreaId() >> 8));
			bytes.put((byte)item.getRectangleAreaId());
			bytes.put((byte)(item.getRectangleAreaProperty() >> 8));
			bytes.put((byte)item.getRectangleAreaProperty());
			bytes.put((byte)(item.getLeftTopLatitude() >> 24));
			bytes.put((byte)(item.getLeftTopLatitude() >> 16));
			bytes.put((byte)(item.getLeftTopLatitude() >> 8));
			bytes.put((byte)item.getLeftTopLatitude());
			bytes.put((byte)(item.getLeftTopLongitude() >> 24));
			bytes.put((byte)(item.getLeftTopLongitude() >> 16));
			bytes.put((byte)(item.getLeftTopLongitude() >> 8));
			bytes.put((byte)item.getLeftTopLongitude());
			bytes.put((byte)(item.getRightBottomLatitude() >> 24));
			bytes.put((byte)(item.getRightBottomLatitude() >> 16));
			bytes.put((byte)(item.getRightBottomLatitude() >> 8));
			bytes.put((byte)item.getRightBottomLatitude());
			bytes.put((byte)(item.getRightBottomLongitude() >> 24));
			bytes.put((byte)(item.getRightBottomLongitude() >> 16));
			bytes.put((byte)(item.getRightBottomLongitude() >> 8));
			bytes.put((byte)item.getRightBottomLongitude());
			if ((item.getRectangleAreaProperty() & 0x0001) == 0x0001)
			{

				byte[] date1 = BitConverter.getBytes(item.getStartTime());
				byte[] date2 = BitConverter.getBytes(item.getEndTime());

				bytes.put(date1);
				bytes.put(date2);
			}
			if ((item.getRectangleAreaProperty() & 0x0002) == 0x0002)
			{
				bytes.put((byte)(item.getMaxSpeed() >> 8));
				bytes.put((byte)item.getMaxSpeed());
				bytes.put(item.getOverSpeedLastTime());
			}
			if ((item.getRectangleAreaProperty() & 0x0100) == 0x0100)
			{
				bytes.put(item.getPhotoMaxSpeed());
				bytes.put(item.getLastTimeBelowPhotoMaxSpeed());
				bytes.put(item.getFireOnPhotoInterval());
				bytes.put(item.getFireOffPhotoDelay());
			}
			if ((item.getRectangleAreaProperty() & 0x8000) == 0x8000)
			{
				byte[] areaNameBytes = BitConverter.getBytes(item.getRectangleAreaName());
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
		setRectangleAreas(new java.util.ArrayList<RectangleAreaItem>(getAreasCount()));
		int pos = 2;
		while (pos < bytes.length)
		{
			RectangleAreaItem item = new RectangleAreaItem();
			item.setRectangleAreaId((int)((bytes[pos] << 24) + (bytes[pos + 1] << 16) + (bytes[pos + 2] << 8) + bytes[pos + 3]));
			item.setRectangleAreaProperty((short)((bytes[pos + 4] << 8) + bytes[pos + 5]));
			item.setLeftTopLatitude((int)((bytes[pos + 6] << 24) + (bytes[pos + 8] << 16) + (bytes[pos + 8] << 8) + bytes[pos + 9]));
			item.setLeftTopLongitude((int)((bytes[pos + 10] << 24) + (bytes[pos + 11] << 16) + (bytes[pos + 12] << 8) + bytes[pos + 13]));
			item.setRightBottomLatitude((int)((bytes[pos + 14] << 24) + (bytes[pos + 15] << 16) + (bytes[pos + 16] << 8) + bytes[pos + 17]));
			item.setRightBottomLongitude((int)((bytes[pos + 18] << 24) + (bytes[pos + 19] << 16) + (bytes[pos + 20] << 8) + bytes[pos + 21]));
			pos += 22;
			if ((item.getRectangleAreaProperty() & 0x0001) == 0x0001)
			{
				item.setStartTime(new java.util.Date(java.util.Date.parse("20" + String.format("%02X", bytes[pos]) + "-" + String.format("%02X", bytes[pos + 1]) + "-" + String.format("%02X", bytes[pos + 2]) + " " + String.format("%02X", bytes[pos + 3]) + ":" + String.format("%02X", bytes[pos + 4]) + ":" + String.format("%02X", bytes[pos + 5]))));
				item.setEndTime(new java.util.Date(java.util.Date.parse("20" + String.format("%02X", bytes[pos + 6]) + "-" + String.format("%02X", bytes[pos + 7]) + "-" + String.format("%02X", bytes[pos + 8]) + " " + String.format("%02X", bytes[pos + 9]) + ":" + String.format("%02X", bytes[pos + 10]) + ":" + String.format("%02X", bytes[pos + 11]))));
				pos += 12;
			}
			if ((item.getRectangleAreaProperty() & 0x0002) == 0x0002)
			{
				item.setMaxSpeed((short)((bytes[pos] << 8) + bytes[pos + 1]));
				item.setOverSpeedLastTime(bytes[pos + 2]);
				pos += 3;
			}
			if ((item.getRectangleAreaProperty() & 0x0100) == 0x0100)
			{
				item.setPhotoMaxSpeed(bytes[pos]);
				item.setLastTimeBelowPhotoMaxSpeed(bytes[pos + 1]);
				item.setFireOnPhotoInterval(bytes[pos + 2]);
				item.setFireOffPhotoDelay(bytes[pos + 3]);
				pos += 4;
			}
			if ((item.getRectangleAreaProperty() & 0x8000) == 0x8000)
			{
				item.setRectangleAreaNameLength(bytes[pos]);
				item.setRectangleAreaName(BitConverter.getString(bytes, pos + 1, item.getRectangleAreaNameLength()));
				pos = pos + 1 + item.getRectangleAreaNameLength();
			}
			getRectangleAreas().add(item);
		}
	}
}