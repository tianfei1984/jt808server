package com.ltmonitor.jt808.protocol;

import java.text.SimpleDateFormat;

public class Recorder_SpeedIn2Days implements IRecorderDataBlock {
	public final byte getCommandWord() {
		return 0x09;
	}

	// ORIGINAL LINE: public ushort getDataLength()
	public final short getDataLength() {
		return (short) (getSpeedsIn2Days().size() * 65);
	}

	/**
	 * 2天内的速度数据
	 */
	private java.util.TreeMap<java.util.Date, byte[]> speedsIn2Days;

	public final java.util.TreeMap<java.util.Date, byte[]> getSpeedsIn2Days() {
		return speedsIn2Days;
	}

	public final void setSpeedsIn2Days(
			java.util.TreeMap<java.util.Date, byte[]> value) {
		speedsIn2Days = value;
	}

	public final String toString() {
		java.util.Collection<java.util.Date> keys = getSpeedsIn2Days().keySet();
		StringBuilder sb = new StringBuilder();
		for (java.util.Date key : keys) {
			byte[] values = getSpeedsIn2Days().get(key);
			for (byte b : values) {
				sb.append(BitConverter.format(key)).append(",");
				sb.append("" + b).append(",");
				sb.append(';');
			}
		}
		return sb.toString();
	}

	public final byte[] WriteToBytes() {
		MyBuffer buff = new MyBuffer();

		for (java.util.Map.Entry<java.util.Date, byte[]> keyValue : getSpeedsIn2Days()
				.entrySet()) {
			buff.put(Byte.parseByte(
					(keyValue.getKey().getYear() + "").substring(2, 4), 16));
			buff.put(Byte.parseByte((keyValue.getKey().getMonth() + ""), 16));
			buff.put(Byte.parseByte((keyValue.getKey().getDay() + ""), 16));
			buff.put(Byte.parseByte((keyValue.getKey().getHours() + ""), 16));
			buff.put(Byte.parseByte((keyValue.getKey().getMinutes() + ""), 16));
			buff.put(keyValue.getValue());
		}
		return buff.array();
	}

	public final void ReadFromBytes(byte[] bytes) {
		setSpeedsIn2Days(new java.util.TreeMap<java.util.Date, byte[]>());
		try {
			// C# TO JAVA CONVERTER NOTE: The following 'using' block is
			// replaced by its Java equivalent:
			// using (MemoryStream ms = new MemoryStream(bytes))

			try {
				// C# TO JAVA CONVERTER NOTE: The following 'using' block is
				// replaced by its Java equivalent:
				// using (MyBuffer buff = new MyBuffer(bytes))
				MyBuffer buff = new MyBuffer(bytes);
				try {
					while (buff.hasRemain()) {
						byte[] timeBytes = buff.gets(5);
						getSpeedsIn2Days().put(
								new java.util.Date(java.util.Date.parse("20"
										+ String.format("%02X", timeBytes[0])
										+ "-"
										+ String.format("%02X", timeBytes[1])
										+ "-"
										+ String.format("%02X", timeBytes[2])
										+ " "
										+ String.format("%02X", timeBytes[3])
										+ ":"
										+ String.format("%02X", timeBytes[4])
										+ ":00")), buff.gets(60));
					}
				} finally {

				}
			} finally {

			}
		} catch (RuntimeException ex) {
		}
	}
}