package com.ltmonitor.jt808.protocol;

/** 
 删除矩形区域
 
*/
public class JT_8603 implements IMessageBody
{
	/** 
	 区域数,本条消息中包含的区域数，不超过125个，多于125个建议用多条消息，0为删除所有矩形区域
	 
	*/
	private byte circleAreasCount;
	public final byte getCircleAreasCount()
	{
		return circleAreasCount;
	}
	public final void setCircleAreasCount(byte value)
	{
		circleAreasCount = value;
	}
	/** 
	 要删除矩形区域ID列表
	 
	*/
	private java.util.ArrayList<Integer> circleAreaIDs;
	public final java.util.ArrayList<Integer> getCircleAreaIDs()
	{
		return circleAreaIDs;
	}

//ORIGINAL LINE: public void setCircleAreaIDs(List<uint> value)
	public final void setCircleAreaIDs(java.util.ArrayList<Integer> value)
	{
		circleAreaIDs = value;
	}
	public final byte[] WriteToBytes()
	{
		MyBuffer bytes = new MyBuffer();
		bytes.put(getCircleAreasCount());
		if (getCircleAreasCount() > 0 && getCircleAreaIDs() != null && getCircleAreaIDs().size() > 0)
		{

//ORIGINAL LINE: foreach (uint areaId in CircleAreaIDs)
			for (int areaId : getCircleAreaIDs())
			{
				bytes.put((byte)(areaId >> 24));
				bytes.put((byte)(areaId >> 16));
				bytes.put((byte)(areaId >> 8));
				bytes.put((byte)areaId);
			}
		}
		return bytes.array();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setCircleAreasCount(bytes[0]);
		setCircleAreaIDs(new java.util.ArrayList<Integer>(getCircleAreasCount()));
		int pos = 1;
		while (pos < bytes.length)
		{

//ORIGINAL LINE: uint areaId = (uint)((bytes[pos] << 24) + (bytes[pos + 1] << 16) + (bytes[pos + 2]) << 8 + bytes[pos + 3]);
			int areaId = (int)((bytes[pos] << 24) + (bytes[pos + 1] << 16) + (bytes[pos + 2]) << 8 + bytes[pos + 3]);
			getCircleAreaIDs().add(areaId);
			pos += 4;
		}
	}
}