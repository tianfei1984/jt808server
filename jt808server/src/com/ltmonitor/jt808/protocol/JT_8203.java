package com.ltmonitor.jt808.protocol;

/**
 * 人工确认报警
 */
public class JT_8203 implements IMessageBody {

	/**
	 * 人工报警确认的报警消息的流水号
	 */
	private short responseMessageSerialNo;

	public final short getResponseMessageSerialNo() {
		return responseMessageSerialNo;
	}

	public final void setResponseMessageSerialNo(short value) {
		responseMessageSerialNo = value;
	}

	
	/**
	 * 人工确认报警类型
	 */
	private int alarmType;


	@Override
	public String toString() {
		return "人工确认报警类型:"+getAlarmType();
	}

	public final byte[] WriteToBytes() {
		MyBuffer buff = new MyBuffer();

		buff.put(getResponseMessageSerialNo());
		buff.put(this.getAlarmType());

		return buff.array();

	}

	public final void ReadFromBytes(byte[] messageBodyBytes) {

		setResponseMessageSerialNo((short) BitConverter.ToUInt16(
				messageBodyBytes, 0));
		this.setAlarmType((int) BitConverter.ToUInt32(messageBodyBytes, 2));
	}

	public int getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(int alarmType) {
		this.alarmType = alarmType;
	}
}