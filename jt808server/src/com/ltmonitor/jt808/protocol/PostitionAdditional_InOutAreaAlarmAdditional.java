package com.ltmonitor.jt808.protocol;



public class PostitionAdditional_InOutAreaAlarmAdditional implements IPositionAdditionalItem
{

	public final int getAdditionalId()
	{
		return 0x12;
	}

	public final byte getAdditionalLength()
	{
		return 0x06;
	}
	/** 
	 位置类型
	 
	*/
	private byte positionType;
	public final byte getPositionType()
	{
		return positionType;
	}
	public final void setPositionType(byte value)
	{
		positionType = value;
	}
	/** 
	 区域或路段ID
	 
	*/

//ORIGINAL LINE: private uint areaId;
	private int areaId;

//ORIGINAL LINE: public uint getAreaId()
	public final int getAreaId()
	{
		return areaId;
	}

//ORIGINAL LINE: public void setAreaId(uint value)
	public final void setAreaId(int value)
	{
		areaId = value;
	}
	/** 
	 方向,0:进,1:出
	 
	*/
	private byte direction;
	public final byte getDirection()
	{
		return direction;
	}
	public final void setDirection(byte value)
	{
		direction = value;
	}



	public final byte[] WriteToBytes()
	{
		if (BitConverter.IsLittleEndian)
		{
			byte[] bytes = new byte[6];
			bytes[0] = getPositionType();
			bytes[1] = (byte)(getAreaId() >> 24);
			bytes[2] = (byte)(getAreaId() >> 16);
			bytes[3] = (byte)(getAreaId() >> 8);
			bytes[4] = (byte)getAreaId();
			bytes[5] = getDirection();
			return bytes;
		}
		else
		{
//C# TO JAVA CONVERTER NOTE: The following 'using' block is replaced by its Java equivalent:
//			using (MemoryStream ms = new MemoryStream())
			
			try
			{
//C# TO JAVA CONVERTER NOTE: The following 'using' block is replaced by its Java equivalent:
//				using (MyBuffer buff = new MyBuffer(bytes))
				MyBuffer buff = new MyBuffer();
				try
				{
					buff.put(getPositionType());
					buff.put(getAreaId());
					buff.put(getDirection());
				}
				finally
				{
					
				}
				return buff.array();
			}
			finally
			{
				
			}
		}
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		if (BitConverter.IsLittleEndian)
		{
			setPositionType(bytes[0]);
			setAreaId((int)((bytes[1] << 24) + (bytes[2] << 16) + (bytes[3] << 8) + bytes[4]));
			setDirection(bytes[5]);
		}
		else
		{
//C# TO JAVA CONVERTER NOTE: The following 'using' block is replaced by its Java equivalent:
//			using (MemoryStream ms = new MemoryStream(bytes))
			
			try
			{
//C# TO JAVA CONVERTER NOTE: The following 'using' block is replaced by its Java equivalent:
//				using (MyBuffer buff = new MyBuffer(bytes))
				MyBuffer buff = new MyBuffer(bytes);
				try
				{
					setPositionType(buff.get());
					setAreaId(buff.getLong());
					setDirection(buff.get());
				}
				finally
				{
					
				}
			}
			finally
			{
				
			}
		}
	}
}