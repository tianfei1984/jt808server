package com.ltmonitor.jt808.protocol;



public class PostitionAdditional_OverSpeedAlarmAdditional implements IPositionAdditionalItem
{

	public final int getAdditionalId()
	{
		return 0x11;
	}

	public final byte getAdditionalLength()
	{
		if (getPositionType() == 0)
		{
			return 0x01;
		}
		else
		{
			return 0x05;
		}
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



	public final byte[] WriteToBytes()
	{
		if (BitConverter.IsLittleEndian)
		{
			if (getPositionType() == 0)
			{
				return new byte[] { getPositionType() };
			}
			else
			{
				byte[] bytes = new byte[5];
				bytes[0] = getPositionType();
				bytes[1] = (byte)(getAreaId() >> 24);
				bytes[2] = (byte)(getAreaId() >> 16);
				bytes[3] = (byte)(getAreaId() >> 8);
				bytes[4] = (byte)getAreaId();
				return bytes;
			}
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
					if (getPositionType() != 0)
					{
						buff.put(getAreaId());
					}
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
			if (getPositionType() != 0)
			{
				setAreaId((int)((bytes[1] << 24) + (bytes[2] << 16) + (bytes[3] << 8) + bytes[4]));
			}
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
					if (getPositionType() != 0)
					{
						setAreaId(buff.getLong());
					}
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