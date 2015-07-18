package com.ltmonitor.jt808.protocol;

public class Recorder_TiredDrivingRecord implements IRecorderDataBlock {
	public final byte getCommandWord() {
		return 0x11;
	}

	// ORIGINAL LINE: public ushort getDataLength()
	public final short getDataLength() {
		return (short) (28 * getRecords().size());
	}

	/**
	 * 疲劳驾驶记录
	 */
	private java.util.ArrayList<TiredDrivingRecordItem> records;

	public final java.util.ArrayList<TiredDrivingRecordItem> getRecords() {
		return records;
	}

	public final void setRecords(
			java.util.ArrayList<TiredDrivingRecordItem> value) {
		records = value;
	}

	public final String toString() {
		StringBuilder sb = new StringBuilder();
		for (TiredDrivingRecordItem ti : getRecords()) {
			sb.append(ti.getStartTime()).append(",").append(ti.getEndTime())
					.append(",").append(ti.getDriverLincenseNo()).append(";");
		}
		return sb.toString();
	}

	public final byte[] WriteToBytes() {
		MyBuffer buff = new MyBuffer();
		for (TiredDrivingRecordItem item : getRecords()) {
			byte[] bytes = new byte[18];
			byte[] licenseNoBytes = BitConverter.getBytes(item
					.getDriverLincenseNo());
			if (licenseNoBytes.length >= 17) {
				System.arraycopy(licenseNoBytes, 0, bytes, 0, 17);
			} else {
				System.arraycopy(licenseNoBytes, 0, bytes, 0,
						licenseNoBytes.length);
				int pos = licenseNoBytes.length;
				while (pos < 17) {
					bytes[pos] = 0x00;
					pos++;
				}
			}
			bytes[17] = 0x00;
			buff.put(bytes);
			byte[] date1 = BitConverter.getBytes(item.getStartTime());
			byte[] date2 = BitConverter.getBytes(item.getEndTime());

			buff.put(date1);
			buff.put(date2);
		}

		return buff.array();

	}

	public final void ReadFromBytes(byte[] bytes) {
		try {
			setRecords(new java.util.ArrayList<TiredDrivingRecordItem>());
			int pos = 0;
			while (pos < bytes.length) {
				TiredDrivingRecordItem item = new TiredDrivingRecordItem();
				item.setDriverLincenseNo(BitConverter.getString(bytes, pos, 18));
				item.setStartTime(new java.util.Date(java.util.Date.parse("20"
						+ String.format("%02X", bytes[pos + 18]) + "-"
						+ String.format("%02X", bytes[pos + 19]) + "-"
						+ String.format("%02X", bytes[pos + 20]) + " "
						+ String.format("%02X", bytes[pos + 21]) + ":"
						+ String.format("%02X", bytes[pos + 22]) + ":00")));
				item.setEndTime(new java.util.Date(java.util.Date.parse("20"
						+ String.format("%02X", bytes[pos + 23]) + "-"
						+ String.format("%02X", bytes[pos + 24]) + "-"
						+ String.format("%02X", bytes[pos + 25]) + " "
						+ String.format("%02X", bytes[pos + 26]) + ":"
						+ String.format("%02X", bytes[pos + 27]) + ":00")));
				getRecords().add(item);
				pos += 28;
			}
		} catch (RuntimeException ex) {
		}
	}
}