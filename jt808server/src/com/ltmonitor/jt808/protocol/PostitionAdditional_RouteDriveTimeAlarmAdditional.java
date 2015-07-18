package com.ltmonitor.jt808.protocol;



public class PostitionAdditional_RouteDriveTimeAlarmAdditional implements IPositionAdditionalItem
{

	public final int getAdditionalId()
	{
		return 0x13;
	}

	public final byte getAdditionalLength()
	{
		return 0x07;
	}

	/** 
	 路段ID
	 
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
	 行驶时间,单位为秒(s)
	 
	*/

//ORIGINAL LINE: private ushort driveTime;
	private short driveTime;

//ORIGINAL LINE: public ushort getDriveTime()
	public final short getDriveTime()
	{
		return driveTime;
	}

//ORIGINAL LINE: public void setDriveTime(ushort value)
	public final void setDriveTime(short value)
	{
		driveTime = value;
	}
	/** 
	 结果,0: 不足,1:过长
	 
	*/
	private byte result;
	public final byte getResult()
	{
		return result;
	}
	public final void setResult(byte value)
	{
		result = value;
	}



	public final byte[] WriteToBytes()
	{
		if (BitConverter.IsLittleEndian)
		{
			byte[] bytes = new byte[7];
			bytes[0] = (byte)(getRouteId() >> 24);
			bytes[1] = (byte)(getRouteId() >> 16);
			bytes[2] = (byte)(getRouteId() >> 8);
			bytes[3] = (byte)getRouteId();
			bytes[4] = (byte)(getDriveTime() >> 8);
			bytes[5] = (byte)getDriveTime();
			bytes[6] = getResult();
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
					buff.put(getRouteId());
					buff.put(getDriveTime());
					buff.put(getResult());
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
			setRouteId((int)((bytes[0] << 24) + (bytes[1] << 16) + (bytes[2] << 8) + bytes[3]));
			setDriveTime((short)((bytes[4] << 8) + bytes[5]));
			setResult(bytes[6]);
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
					setRouteId(buff.getLong());
					setDriveTime(buff.getShort());
					setResult(buff.get());
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