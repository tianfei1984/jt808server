package com.ltmonitor.jt808.protocol;

/**
 * 提问应答
 */
public class JT_0302 implements IMessageBody {
	/**
	 * 对应的提问下发消息的流水号
	 */

	// ORIGINAL LINE: private ushort responseMessageSerialNo;
	private short responseMessageSerialNo;

	// ORIGINAL LINE: public ushort getResponseMessageSerialNo()
	public final short getResponseMessageSerialNo() {
		return responseMessageSerialNo;
	}

	// ORIGINAL LINE: public void setResponseMessageSerialNo(ushort value)
	public final void setResponseMessageSerialNo(short value) {
		responseMessageSerialNo = value;
	}

	/**
	 * 提问下发中附带的答案ID
	 */
	private byte answerId;

	public final byte getAnswerId() {
		return answerId;
	}

	public final void setAnswerId(byte value) {
		answerId = value;
	}

	public final byte[] WriteToBytes() {
		MyBuffer buff = new MyBuffer();
		buff.put(BitConverter.GetBytes(getResponseMessageSerialNo()));
		buff.put(getAnswerId());
		return buff.array();

	}

	public final void ReadFromBytes(byte[] bytes) {

		setResponseMessageSerialNo((short) BitConverter.ToUInt16(bytes, 0));
		setAnswerId(bytes[2]);
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(String.format("流水号：%1$s,答案ID：%2$s",
				getResponseMessageSerialNo(), getAnswerId()));
		return sBuilder.toString();
	}
}