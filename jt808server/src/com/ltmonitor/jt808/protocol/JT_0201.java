package com.ltmonitor.jt808.protocol;

/**
 * 位置信息查询应答
 */
public class JT_0201 implements IMessageBody {
	/**
	 * 对应的位置信息查询消息的流水号
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
	 * 位置信息汇报
	 */
	private JT_0200 positionReport;

	public final JT_0200 getPositionReport() {
		return positionReport;
	}

	public final void setPositionReport(JT_0200 value) {
		positionReport = value;
	}

	public final byte[] WriteToBytes() {

		MyBuffer buff = new MyBuffer();

		buff.put(getResponseMessageSerialNo());
		buff.put(getPositionReport().WriteToBytes());
		return buff.array();

	}

	public final void ReadFromBytes(byte[] bytes) {
		MyBuffer buff = new MyBuffer(bytes);
		setResponseMessageSerialNo(buff.getShort());
		setPositionReport(new JT_0200());
		getPositionReport().ReadFromBytes(
				buff.gets(bytes.length - 2));

	}

	@Override
	public String toString() {
		return String.format("流水号:%1$s,%2$s", getResponseMessageSerialNo(),
				getPositionReport().toString());
	}
}