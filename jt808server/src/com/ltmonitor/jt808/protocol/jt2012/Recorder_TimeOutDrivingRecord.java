package com.ltmonitor.jt808.protocol.jt2012;

import java.util.ArrayList;
import java.util.List;

import com.ltmonitor.jt808.protocol.BitConverter;

/**
 * 采集指定的超时驾驶记录
 */
public class Recorder_TimeOutDrivingRecord implements IRecorderDataBlock_2012 {
	private List<TiredDrivingRecord> drivingRecordList = new ArrayList<TiredDrivingRecord>();

	/**
	 * 命令字
	 */
	public final byte getCommandWord() {
		return 0x06;
	}

	/**
	 * 数据块长度
	 */
	// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct
	// equivalent in Java:
	// ORIGINAL LINE: public ushort getDataLength()
	public final short getDataLength() {
		return 87;
	}

	public final byte[] WriteToBytes() {
		byte[] bytes = null;
		return bytes;
	}

	public final void ReadFromBytes(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		if (bytes != null) {
			for (int i = 0; i < bytes.length / 50; i++) {
				// 获取机动车驾驶证号码
				byte[] nub = new byte[18];
				System.arraycopy(bytes, 0 + 50 * i, nub, 0, 18);

				String driverNub = BitConverter.getString(nub);
				if (driverNub.length() == 15) {
					String add = "00H";
					driverNub = driverNub + add;
				}

				String licenseNo = driverNub;
				// 获取开始时间
				byte[] ContinuousDrivingBeginTime = new byte[6];
				System.arraycopy(bytes, 18 + 50 * i,
						ContinuousDrivingBeginTime, 0, 6);
				String beginTime = new java.util.Date(java.util.Date.parse("20"
						+ String.format("%02X", ContinuousDrivingBeginTime[0])
						+ "-"
						+ String.format("%02X", ContinuousDrivingBeginTime[1])
						+ "-"
						+ String.format("%02X", ContinuousDrivingBeginTime[2])
						+ " "
						+ String.format("%02X", ContinuousDrivingBeginTime[3])
						+ ":"
						+ String.format("%02X", ContinuousDrivingBeginTime[4])
						+ ":"
						+ String.format("%02X", ContinuousDrivingBeginTime[5])))
						.toString();
				String strStartTime = beginTime;
				// 获取结束时间
				byte[] ContinuousDrivingEndTime = new byte[6];
				System.arraycopy(bytes, 24 + 50 * i, ContinuousDrivingEndTime,
						0, 6);
				String endTime = new java.util.Date(java.util.Date.parse("20"
						+ String.format("%02X", ContinuousDrivingEndTime[0])
						+ "-"
						+ String.format("%02X", ContinuousDrivingEndTime[1])
						+ "-"
						+ String.format("%02X", ContinuousDrivingEndTime[2])
						+ " "
						+ String.format("%02X", ContinuousDrivingEndTime[3])
						+ ":"
						+ String.format("%02X", ContinuousDrivingEndTime[4])
						+ ":"
						+ String.format("%02X", ContinuousDrivingEndTime[5])))
						.toString();
				String strEndTime = endTime;
				// 获取开始时间有效位置
				byte[] BeginTimePlace = new byte[10];
				System.arraycopy(bytes, 30 + 50 * i, BeginTimePlace, 0, 10);
				String beginPlace = GetPlaceInfo(BeginTimePlace);
				String strStartLocation = beginPlace;
				// 获取结束时间有效位置
				byte[] EndTimePlace = new byte[10];
				System.arraycopy(bytes, 40 + 50 * i, EndTimePlace, 0, 10);
				String endPlace = GetPlaceInfo(EndTimePlace);
				String strEndLocation = endPlace;
				this.drivingRecordList.add(new TiredDrivingRecord(licenseNo,
						strStartTime, strEndTime, strStartLocation,
						strEndLocation));

			}
		}

	}

	/**
	 * 获取地点信息
	 * 
	 * @param placeInfo
	 * @return
	 */
	private String GetPlaceInfo(byte[] placeInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append("经度为："
				+ (int) ((int) (placeInfo[0] << 24)
						+ (int) (placeInfo[1] << 16)
						+ (int) (placeInfo[2] << 8) + (int) (placeInfo[3]))
				* 0.0001);

		sb.append("纬度为："
				+ (int) ((int) (placeInfo[4] << 24)
						+ (int) (placeInfo[5] << 16)
						+ (int) (placeInfo[6] << 8) + (int) (placeInfo[7]))
				* 0.0001);

		sb.append("海拔高度为："
				+ (int) ((int) (placeInfo[8] << 8) + (int) (placeInfo[9])));

		return sb.toString();
	}

	public List<TiredDrivingRecord> getDrivingRecordList() {
		return drivingRecordList;
	}

	public void setDrivingRecordList(List<TiredDrivingRecord> drivingRecordList) {
		this.drivingRecordList = drivingRecordList;
	}
}
