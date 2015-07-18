package com.ltmonitor.jt808.protocol.jt2012;

import com.ltmonitor.jt808.protocol.BitConverter;


public final class RecorderDataBlockFactory_2012
{
	public static IRecorderDataBlock_2012 Create(byte command)
	{
		int cmd = BitConverter.ToUInt32(command);
		switch (cmd)
		{
			case 0x00:
				return new Recorder_2012_Version();
			case 0x01:
				return new Recorder_DriverInformation();
			case 0xC2:
			case 0x83:
			case 0x02:
				return new Recorder_RealTime();
			case 0x03:
			case 0xC4:
				return new Recorder_AccumulativeMileage();
			case 0x04:
			case 0xC3:
				return new Recorder_PulseFactor();
			case 0x05:
			case 0x82:
				return new Recorder_VehicleInformation();
			case 0x06:
			case 0x84:
				return new Recorder_StateInformation();
			case 0x07:
				return new Recorder_UniqueNumber();
			case 0x08:
				return new Recorder_Speed();
			case 0x09:
				return new Recorder_LocationInformation();
			case 0x10:
				return new Recorder_AccidentRecordsOfDoubt();
			case 0x11:
				return new Recorder_TimeOutDrivingRecord();
			case 0x12:
				return new Recorder_DriverIdentity();
			case 0x13:
				return new Recorder_ExternalPowerSupply();
			case 0x14:
				return new Recorder_ParameterChange();
			case 0x15:
				return new Recorder_SpeedStatusLog();

		}
		return null;
	}
}
