package com.ltmonitor.jt808.protocol;

import java.util.Date;

public class Recorder_SpeedIn360Hours implements IRecorderDataBlock {
	public final byte getCommandWord() {
		return 0x05;
	}

	// ORIGINAL LINE: public ushort getDataLength()
	public final short getDataLength() {
		return (short) (65 * getSpeedsIn360Hours().size());
	}

	/**
	 * 360小时速度数据
	 */
	private java.util.TreeMap<java.util.Date, byte[]> speedsIn360Hours;

	public final java.util.TreeMap<java.util.Date, byte[]> getSpeedsIn360Hours() {
		return speedsIn360Hours;
	}

	public final void setSpeedsIn360Hours(
			java.util.TreeMap<java.util.Date, byte[]> value) {
		speedsIn360Hours = value;
	}

	public final byte[] WriteToBytes() {
		MyBuffer buff = new MyBuffer();

		for (java.util.Map.Entry<java.util.Date, byte[]> keyValue :speedsIn360Hours.entrySet()) {
			buff.put(Byte.parseByte((keyValue.getKey().getYear() + "")
					.substring(2, 4), 16));
			buff.put(Byte.parseByte(
					(keyValue.getKey().getMonth() + ""), 16));
			buff.put(Byte.parseByte(
					(keyValue.getKey().getDay() + ""), 16));
			buff.put(Byte.parseByte(
					(keyValue.getKey().getHours() + ""), 16));
			buff.put(Byte.parseByte((keyValue.getKey().getMinutes() + "")
					, 16));

			buff.put(keyValue.getValue());
		}
		return buff.array();
	}

	public final String toString() {
		/**
		java.util.Collection<java.util.Date> keys = getSpeedsIn360Hours()
				.keySet();
		StringBuilder sb = new StringBuilder();
		for (java.util.Date key : keys) {
			byte[] values = getSpeedsIn360Hours().get(key);
			for (byte b : values) {
				sb.append(BitConverter.format(key)).append(",");
				sb.append("" + b).append(",");
				sb.append(';');
			}
		}
		return sb.toString();
		*/
		return "";
	}

	public final void ReadFromBytes(byte[] bytes) {
		setSpeedsIn360Hours(new java.util.TreeMap<java.util.Date, byte[]>());

		MyBuffer buff = new MyBuffer(bytes);

		while (buff.hasRemain()) {
			byte[] timeBytes = buff.gets(5);
			String strDate = "20"
					+ String.format("%02X", timeBytes[0]) + "-"
					+ String.format("%02X", timeBytes[1]) + "-"
					+ String.format("%02X", timeBytes[2]) + " "
					+ String.format("%02X", timeBytes[3]) + ":"
					+ String.format("%02X", timeBytes[4]) + ":00";
			getSpeedsIn360Hours().put(
					new Date(),
					buff.gets(60));
		}

	}
}