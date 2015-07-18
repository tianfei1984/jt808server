package com.ltmonitor.jt808.protocol;

/**
 * 终端通用应答
 */
public class JT_0001 implements IMessageBody {
	/**
	 * 应答消息流水号
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
	 * 应答消息ID
	 */

	// ORIGINAL LINE: private ushort responseMessageId;
	private short responseMessageId;

	// ORIGINAL LINE: public ushort getResponseMessageId()
	public final short getResponseMessageId() {
		return responseMessageId;
	}

	// ORIGINAL LINE: public void setResponseMessageId(ushort value)
	public final void setResponseMessageId(short value) {
		responseMessageId = value;
	}

	/**
	 * 应答结果，0：成功/确认；1：失败；2：消息有误；3：不支持
	 */
	private byte responseResult;

	public final byte getResponseResult() {
		return responseResult;
	}

	public final void setResponseResult(byte value) {
		responseResult = value;
	}

	public final byte[] WriteToBytes() {
		MyBuffer buff = new MyBuffer();
		try {
			buff.put(getResponseMessageSerialNo());
			buff.put(getResponseMessageId());
			buff.put(getResponseResult());
		} finally {

		}
		return buff.array();

	}

	public final void ReadFromBytes(byte[] messageBodyBytes) {
		MyBuffer buff = new MyBuffer(messageBodyBytes);

		setResponseMessageSerialNo(buff.getShort());
		setResponseMessageId(buff.getShort());
		setResponseResult(buff.get());
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(String.format("SN：%1$s,ID：%2$s",
				getResponseMessageSerialNo(), getResponseMessageId()));
		return sBuilder.toString();
	}
}