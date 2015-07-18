package com.ltmonitor.jt808.protocol;

import java.text.ParseException;
import java.text.SimpleDateFormat;

//停车前20s的停车数据，共采集10次停车
public class Recorder_DoubtfulPointData implements IRecorderDataBlock {
	public final byte getCommandWord() {
		return 0x07;
	}

	// ORIGINAL LINE: public ushort getDataLength()
	public final short getDataLength() {
		return (short) (getDoubtfulPointData().size() * 206);
	}

	private java.util.TreeMap<java.util.Date, java.util.ArrayList<Byte[]>> doubtfulPointData;

	public final java.util.TreeMap<java.util.Date, java.util.ArrayList<Byte[]>> getDoubtfulPointData() {
		return doubtfulPointData;
	}

	public final void setDoubtfulPointData(
			java.util.TreeMap<java.util.Date, java.util.ArrayList<Byte[]>> value) {
		doubtfulPointData = value;
	}

	public final String toString() {
		java.util.Collection<java.util.Date> keys = getDoubtfulPointData()
				.keySet();
		StringBuilder sb = new StringBuilder();
		for (java.util.Date key : keys) {
			java.util.Collection<Byte[]> values = getDoubtfulPointData().get(
					key);

			sb.append(BitConverter.format(key)).append(",");
			for (Byte[] b : values) {
				sb.append("" + b[0]).append(',').append("" + b[1]).append(',');
			}
			sb.append(';');
		}
		return sb.toString();
	}

	public final byte[] WriteToBytes() {
		MyBuffer bytes = new MyBuffer();
		for (java.util.Map.Entry<java.util.Date, java.util.ArrayList<Byte[]>> onceDoubtfulPointData : getDoubtfulPointData()
				.entrySet()) {
			byte[] date1 = BitConverter
					.getBytes(onceDoubtfulPointData.getKey());
			bytes.put(date1);
			for (Byte[] data : onceDoubtfulPointData.getValue()) {
				for (Byte b : data) {
					bytes.put(b);
				}
			}
		}
		return bytes.array();
	}

	public final void ReadFromBytes(byte[] bytes) {
		setDoubtfulPointData(new java.util.TreeMap<java.util.Date, java.util.ArrayList<Byte[]>>());
		int pos = 0;
		while (pos < bytes.length) {
			java.util.Date stopTime;
			stopTime = BitConverter.getDate(bytes, 0);

			pos += 6;
			java.util.ArrayList<Byte[]> speedAndSwitches = new java.util.ArrayList<Byte[]>(
					100);
			for (int i = 0; i < 100; i++) {
				Byte[] speedAndSwitch = new Byte[2];
				speedAndSwitch[0] = bytes[pos + i * 2];
				speedAndSwitch[1] = bytes[pos + 1 + i * 2];
				speedAndSwitches.add(speedAndSwitch);
			}
			pos += 200;
			getDoubtfulPointData().put(stopTime, speedAndSwitches);
		}
	}
}