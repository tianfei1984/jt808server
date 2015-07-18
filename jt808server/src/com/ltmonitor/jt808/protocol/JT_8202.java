package com.ltmonitor.jt808.protocol;

/**
 * 临时位置跟踪控制
 */
public class JT_8202 implements IMessageBody {
	/**
	 * 时间间隔,单位为秒（s），0则停止跟踪。停止跟踪无需带后继字段
	 */

	// ORIGINAL LINE: private ushort timeInterval;
	private short timeInterval;

	// ORIGINAL LINE: public ushort getTimeInterval()
	public final short getTimeInterval() {
		return timeInterval;
	}

	// ORIGINAL LINE: public void setTimeInterval(ushort value)
	public final void setTimeInterval(short value) {
		timeInterval = value;
	}

	/**
	 * 位置跟踪有效期,单位为秒（s），终端在接收到位置跟踪控制消息后，在有效期截止时间之前，依据消息中的时间间隔发送位置汇报
	 */

	// ORIGINAL LINE: private uint trackExpire;
	private int trackExpire;

	// ORIGINAL LINE: public uint getTrackExpire()
	public final int getTrackExpire() {
		return trackExpire;
	}

	// ORIGINAL LINE: public void setTrackExpire(uint value)
	public final void setTrackExpire(int value) {
		trackExpire = value;
	}

	public final byte[] WriteToBytes() {
		MyBuffer buff = new MyBuffer();

		buff.put(getTimeInterval());
		buff.put(getTrackExpire());
		
		return buff.array();
	}

	public final void ReadFromBytes(byte[] bytes) {

		MyBuffer buff = new MyBuffer(bytes);

		setTimeInterval(buff.getShort());
		setTrackExpire(buff.getLong());

	}

	@Override
	public String toString() {
		return String.format("时间间隔:%1$s秒,有效期：%2$s秒", getTimeInterval(),
				getTrackExpire());
	}
}