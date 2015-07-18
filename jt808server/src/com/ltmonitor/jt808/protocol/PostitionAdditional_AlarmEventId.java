package com.ltmonitor.jt808.protocol;



public class PostitionAdditional_AlarmEventId implements IPositionAdditionalItem
{

	public final int getAdditionalId()
	{
		return 0x04;
	}

	public final byte getAdditionalLength()
	{
		return 0x02;
	}

	/** 
	 需要人工确认报警事件的ID，WORD，从1开始计数
	 
	*/

//ORIGINAL LINE: private ushort alarmEventId;
	private short alarmEventId;

//ORIGINAL LINE: public ushort getAlarmEventId()
	public final short getAlarmEventId()
	{
		return alarmEventId;
	}

//ORIGINAL LINE: public void setAlarmEventId(ushort value)
	public final void setAlarmEventId(short value)
	{
		alarmEventId = value;
	}

	public final byte[] WriteToBytes()
	{
		if (BitConverter.IsLittleEndian)
		{
			byte[] bytes = new byte[2];
			bytes[0] = (byte)(getAlarmEventId() >> 8);
			bytes[1] = (byte)getAlarmEventId();
			return bytes;
		}
		else
		{
			return BitConverter.GetBytes(getAlarmEventId());
		}
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		if (BitConverter.IsLittleEndian)
		{
			setAlarmEventId((short)((bytes[0] << 8) + bytes[1]));
		}
		else
		{
			setAlarmEventId((short)BitConverter.ToUInt16(bytes, 0));
		}
	}
}