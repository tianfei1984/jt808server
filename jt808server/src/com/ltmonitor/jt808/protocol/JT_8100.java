package com.ltmonitor.jt808.protocol;

/**
 * 终端注册应答
 */
public class JT_8100 implements IMessageBody {
	/**
	 * 对应的终端注册消息的流水号
	 */

	// ORIGINAL LINE: private ushort registerResponseMessageSerialNo;
	private short registerResponseMessageSerialNo;

	// ORIGINAL LINE: public ushort getRegisterResponseMessageSerialNo()
	public final short getRegisterResponseMessageSerialNo() {
		return registerResponseMessageSerialNo;
	}

	// ORIGINAL LINE: public void setRegisterResponseMessageSerialNo(ushort
	// value)
	public final void setRegisterResponseMessageSerialNo(short value) {
		registerResponseMessageSerialNo = value;
	}

	/**
	 * 注册结果,0：成功；1：车辆已被注册；2：数据库中无该车辆；3：终端已被注册；4：数据库中无该终端
	 */
	private byte registerResponseResult;

	public final byte getRegisterResponseResult() {
		return registerResponseResult;
	}

	public final void setRegisterResponseResult(byte value) {
		registerResponseResult = value;
	}

	/**
	 * 鉴权码,只有在成功后才有该字段
	 */
	private String registerNo;

	public final String getRegisterNo() {
		return registerNo;
	}

	public final void setRegisterNo(String value) {
		registerNo = value;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("注册结果:").append(registerResponseResult).append(",鉴权码:").append(registerNo);
		return sb.toString();
	}

	public final byte[] WriteToBytes() {

		MyBuffer buff = new MyBuffer();

		buff.put(getRegisterResponseMessageSerialNo());
		buff.put(getRegisterResponseResult());
		if (getRegisterResponseResult() == 0) {
			buff.put(getRegisterNo());
		}
		return buff.array();
	}

	public final void ReadFromBytes(byte[] bytes) {

		MyBuffer buff = new MyBuffer(bytes);

		setRegisterResponseMessageSerialNo(buff.getShort());
		setRegisterResponseResult(buff.get());
		if (getRegisterResponseResult() == 0) {
			setRegisterNo(buff.getString());
		}
	}
}