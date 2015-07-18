package com.ltmonitor.jt808.protocol;

public final class RecorderDataBlockFactory
{
	public static IRecorderDataBlock Create(byte command)
	{
		switch ((int)command)
		{
			case 0x01:
			case 0x81: //设定的驾驶员代码及其对应的机动车驾驶证号码：
				return new Recorder_DriverVehicleCode();
			case 0x02:
			case 0xC2: //记录仪的实时时钟 YY-MM-DD-hh-mm-ss
				return new Recorder_RealTimeClock();
			case 0x03:
				return new Recorder_MileageIn360Hours();
			case 0x04:
			case 0xC3: //设定的车辆特征系数 (高中低字节)，对应车辆车速传感器系数设置
				return new Recorder_FeatureFactor();
			case 0x05:
				return new Recorder_SpeedIn360Hours();
			case 0x06:
			case 0x82: //存储的车辆VIN号、车牌号码、分类
				return new Recorder_VehicleLicenseInfo();
			case 0x07: //停车前20s的速度数据
				return new Recorder_DoubtfulPointData();
			case 0x08:
				return new Recorder_MileageIn2Days();
			case 0x09:
				return new Recorder_SpeedIn2Days();
			case 0x11:
				return new Recorder_TiredDrivingRecord();
		}
		return null;
	}
}