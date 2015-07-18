package com.ltmonitor.jt808.protocol;

import java.io.UnsupportedEncodingException;

/**
 * 事件设置
 */
public class JT_8301 implements IMessageBody {
	/**
	 * 设置类型,0：删除终端现有所有事件，该命令后不带后继字节；1：更新事件；2：追加事件； 3：修改事件；
	 * 4：删除特定几项事件，之后事件项中无需带事件内容
	 */
	private byte settingType;

	public final byte getSettingType() {
		return settingType;
	}

	public final void setSettingType(byte value) {
		settingType = value;
	}

	/**
	 * 事件总数
	 */
	private byte eventsCount;

	public final byte getEventsCount() {
		return eventsCount;
	}

	public final void setEventsCount(byte value) {
		eventsCount = value;
	}

	private java.util.ArrayList<EventSettingItem> events;

	public final java.util.ArrayList<EventSettingItem> getEvents() {
		return events;
	}

	public final void setEvents(java.util.ArrayList<EventSettingItem> value) {
		events = value;
	}

	public final byte[] WriteToBytes() {

		MyBuffer buff = new MyBuffer();

		buff.put(getSettingType());
		if (getEventsCount() > 0 && getEvents() != null
				&& getEvents().size() > 0) {
			buff.put(getEventsCount());
			for (EventSettingItem ei : getEvents()) {
				buff.put(ei.getEventId());
				byte[] textBytes;
				try {
					textBytes = ei.getEventContent().getBytes("gbk");
					buff.put((byte) (textBytes.length));
					buff.put(textBytes);
					//buff.put(0x00);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return buff.array();

	}

	public final void ReadFromBytes(byte[] bytes) {
		
				MyBuffer buff = new MyBuffer(bytes);
			
					setSettingType(buff.get());
					if (bytes.length > 1) {
						setEventsCount(buff.get());
						setEvents(new java.util.ArrayList<EventSettingItem>(
								getEventsCount()));
						int pos = 2;
						while (buff.hasRemain()) {
							EventSettingItem ei = new EventSettingItem();
							ei.setEventId(buff.get());
							ei.setEventLength(buff.get());
							ei.setEventContent(buff.getString(ei.getEventLength()));
							getEvents().add(ei);
							pos = pos + 2 + ei.getEventLength();
						}
					}
				
	}

	@Override
	public String toString() {
		return String.format("事件类型：%1$s,事件总数：%2$s", getSettingType(),
				getEventsCount());
	}
}