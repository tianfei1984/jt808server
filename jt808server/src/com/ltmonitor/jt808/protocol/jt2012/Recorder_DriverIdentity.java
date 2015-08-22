package com.ltmonitor.jt808.protocol.jt2012;

import java.util.ArrayList;
import java.util.List;

import com.ltmonitor.entity.VehicleRecorder;
import com.ltmonitor.jt808.protocol.BitConverter;
import com.ltmonitor.jt808.tool.DateUtil;

/**
 * 采集指定的驾驶人身份记录 0x12H
 */
public class Recorder_DriverIdentity implements IRecorderDataBlock_2012 {
	// 每次采集指定的驾驶人身份记录
	private List<VehicleRecorder> eventList = new ArrayList<VehicleRecorder>();

	/**
	 * 命令字
	 */
	public final byte getCommandWord() {
		return 0x12;
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
			for (int i = 0; i < bytes.length / 25; i++) {
				VehicleRecorder vr = new VehicleRecorder();
				// 获取事件发生时间并添加到虚拟表
				byte[] EventOccursTime = new byte[6];
				System.arraycopy(bytes, 0 + 25 * i, EventOccursTime, 0, 6);
				String time = new java.util.Date(java.util.Date.parse("20"
						+ String.format("%02X", EventOccursTime[0]) + "-"
						+ String.format("%02X", EventOccursTime[1]) + "-"
						+ String.format("%02X", EventOccursTime[2]) + " "
						+ String.format("%02X", EventOccursTime[3]) + ":"
						+ String.format("%02X", EventOccursTime[4]) + ":"
						+ String.format("%02X", EventOccursTime[5])))
						.toString();
				vr.setStartTime(DateUtil.stringToDate(time));
				// 获取机动车驾驶证号码
				byte[] nub = new byte[18];
				System.arraycopy(bytes, 6 + 25 * i, nub, 0, 18);

				String driverNub = BitConverter.getString(nub);
				if (driverNub.length() == 15) {
					String add = "00H";
					driverNub = driverNub + add;
				}
				vr.setDriverLicense(driverNub);
				
				// 获取事件类型
				String bu = String.format("%02X", bytes[24 + 25 * i]);
				String strEventType = "其他";
				if (String.format("%02X", bytes[24 + 25 * i]).equals("01")) {
					strEventType = "登陆";
				} else if (String.format("%02X", bytes[24 + 25 * i]).equals(
						"02")) {
					strEventType = "退出";
				}
				vr.setRemark(strEventType);
				eventList.add(vr);
			}
		}
	}

	public List<VehicleRecorder> getEventList() {
		return eventList;
	}

	public void setEventList(List<VehicleRecorder> eventList) {
		this.eventList = eventList;
	}

}
