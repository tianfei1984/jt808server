package com.ltmonitor.jt808.protocol;

/**
 * 平台通用应答
 */
public class JT_8001 implements IMessageBody {

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
	 * 应答结果，0：成功/确认；1：失败；2：消息有误；3：不支持；4：报警处理确认；
	 */
	private byte responseResult;

	public final byte getResponseResult() {
		return responseResult;
	}

	public final void setResponseResult(byte value) {
		responseResult = value;
	}

	@Override
	public String toString() {
		return "应答结果:"+responseResult;
	}

	public final byte[] WriteToBytes() {
		MyBuffer buff = new MyBuffer();

		buff.put(getResponseMessageSerialNo());
		buff.put(getResponseMessageId());
		buff.put(getResponseResult());

		return buff.array();

	}

	public final void ReadFromBytes(byte[] messageBodyBytes) {

		setResponseMessageSerialNo((short) BitConverter.ToUInt16(
				messageBodyBytes, 0));
		setResponseMessageId((short) BitConverter.ToUInt16(messageBodyBytes, 2));
		setResponseResult(messageBodyBytes[4]);
	}
}