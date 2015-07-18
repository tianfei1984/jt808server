package com.ltmonitor.jt808.protocol;



public final class PositionAdditionalFactory
{
	public static IPositionAdditionalItem CreatePositionalFactory(int additionalId, byte length, byte[] bytes)
	{
		IPositionAdditionalItem additional = null;
		switch ((int)additionalId)
		{
			case 0x01:
				additional = new PostitionAdditional_Mileage();
				additional.ReadFromBytes(bytes);
				break;
			case 0x02:
				additional = new PostitionAdditional_Oil();
				additional.ReadFromBytes(bytes);
				break;
			case 0x03:
				additional = new PostitionAdditional_RecorderSpeed();
				additional.ReadFromBytes(bytes);
				break;
			case 0x04:
				additional = new PostitionAdditional_AlarmEventId();
				additional.ReadFromBytes(bytes);
				break;
			case 0x11:
				additional = new PostitionAdditional_OverSpeedAlarmAdditional();
				additional.ReadFromBytes(bytes);
				break;
			case 0x12:
				additional = new PostitionAdditional_InOutAreaAlarmAdditional();
				additional.ReadFromBytes(bytes);
				break;
			case 0x13:
				additional = new PostitionAdditional_RouteDriveTimeAlarmAdditional();
				additional.ReadFromBytes(bytes);
			case 0x25:
				additional = new PostitionAdditional_Signal();
				additional.ReadFromBytes(bytes);
				break;
			case 0xE0:
				PostitionAdditional_FollowedBytesLength folloedBytesLength = new PostitionAdditional_FollowedBytesLength();
				folloedBytesLength.setAdditionalLength(length);
				folloedBytesLength.ReadFromBytes(bytes);
				additional = folloedBytesLength;
				break;
			case 0xE1:
				additional = new PostitionAdditional_Voltage();
				additional.ReadFromBytes(bytes);
				break;
			case 0xE2:
				additional = new OBDExtensions();
				additional.ReadFromBytes(bytes);
				break;
		}
		return additional;
	}
}