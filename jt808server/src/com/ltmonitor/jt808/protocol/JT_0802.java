package com.ltmonitor.jt808.protocol;

import com.ltmonitor.jt808.tool.Tools;

/**
 * 存储多媒体数据检索应带
 */
public class JT_0802 implements IMessageBody {
	/**
	 * 应答流水号
	 */

	private short responseMessageSerialNo;

	public final short getResponseMessageSerialNo() {
		return responseMessageSerialNo;
	}

	public final void setResponseMessageSerialNo(short value) {
		responseMessageSerialNo = value;
	}

	/**
	 * 多媒体数据总项数
	 */

	private short multimediaSearchDataItemsCount;

	public final short getMultimediaSearchDataItemsCount() {
		return multimediaSearchDataItemsCount;
	}

	public final void setMultimediaSearchDataItemsCount(short value) {
		multimediaSearchDataItemsCount = value;
	}

	/**
	 * 检索项
	 */
	private java.util.ArrayList<MuldimediaSearchDataItem> dataItems;

	public final java.util.ArrayList<MuldimediaSearchDataItem> getDataItems() {
		return dataItems;
	}

	public final void setDataItems(
			java.util.ArrayList<MuldimediaSearchDataItem> value) {
		dataItems = value;
	}

	public final byte[] WriteToBytes() {

		MyBuffer buff = new MyBuffer();

		buff.put((byte) (getResponseMessageSerialNo() >> 8));
		buff.put((byte) getResponseMessageSerialNo());
		buff.put((byte) (getMultimediaSearchDataItemsCount() >> 8));
		buff.put((byte) (getMultimediaSearchDataItemsCount()));
		for (MuldimediaSearchDataItem item : getDataItems()) {
			buff.put((byte) (item.getMultimediaId() >> 24));
			buff.put((byte) (item.getMultimediaId() >> 16));
			buff.put((byte) (item.getMultimediaId() >> 8));
			buff.put((byte) (item.getMultimediaId()));
			buff.put(item.getMultimediaType());
			buff.put(item.getChannelId());
			buff.put(item.getEventCode());
			buff.put(item.getPosition().WriteToBytes());
		}
		return buff.array();
	}

	public final void ReadFromBytes(byte[] bytes) {
		//short sn = (short)((bytes[0] << 8) + bytes[1] & 0xff);
		//short b = -65;
		//long x = b & 0xffff;
		//String hexString = Tools.ToHexString(bytes);
		short sn = (short) BitConverter.ToUInt16(
				bytes, 0);
		setResponseMessageSerialNo(sn);
		short count = (short) BitConverter.ToUInt16(
				bytes, 2);
		
		setMultimediaSearchDataItemsCount((short) ((bytes[2] << 8) + bytes[3]));
		setDataItems(new java.util.ArrayList<MuldimediaSearchDataItem>());
		int pos = 4;
		while (pos < bytes.length) {
			MuldimediaSearchDataItem item = new MuldimediaSearchDataItem();
			// 终端发送的协议是否是补充协议
			if (JT808Common.isNew808Protocol()) {
				item.setMultimediaId((int) ((bytes[pos + 0] << 24)
						+ (bytes[pos + 1] << 16) + (bytes[pos + 2] << 8) + bytes[pos + 3]));

				item.setMultimediaType(bytes[pos + 4]);
				item.setChannelId(bytes[pos + 5]);
				item.setEventCode(bytes[pos + 6]);

				byte[] positionBytes = new byte[28];
				System.arraycopy(bytes, pos + 7, positionBytes, 0, 28);
				item.setPosition(new JT_0200());
				item.getPosition().ReadFromBytes(positionBytes);
				pos += 35; // 每个媒体检索项35个字节
			}else
			{
				item.setMultimediaType(bytes[pos + 0]);
				item.setChannelId(bytes[pos + 1]);
				item.setEventCode(bytes[pos + 2]);

				byte[] positionBytes = new byte[28];
				System.arraycopy(bytes, pos + 3, positionBytes, 0, 28);
				item.setPosition(new JT_0200());
				item.getPosition().ReadFromBytes(positionBytes);
				pos += 31; // 每个媒体检索项31个字节
			}
			getDataItems().add(item);

		}
	}
}