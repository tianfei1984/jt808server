package com.ltmonitor.jt808.protocol;

import java.io.UnsupportedEncodingException;

/**
 * 文本信息下发(0x8300)
 * 
 * @author DELL
 * 
 */

public class JT_8300 implements IMessageBody {
	/**
	 * 标志
	 */
	private byte privateFlag;

	public final byte getFlag() {
		return privateFlag;
	}

	public final void setFlag(byte value) {
		privateFlag = value;
	}

	/**
	 * 文本信息
	 */
	private String text;

	public final String getText() {
		return text;
	}

	public final void setText(String value) {
		text = value;
	}

	public final byte[] WriteToBytes() {

		MyBuffer buff = new MyBuffer();

		buff.put(getFlag());
		try {
			buff.put(getText().getBytes("gbk"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//buff.put(0x00);
		return buff.array();

	}

	public final void ReadFromBytes(byte[] bytes) {

		MyBuffer buff = new MyBuffer(bytes);

		setFlag(buff.get());
		setText(buff.getString());

	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(String.format("标志：%1$s,文本：%2$s", getFlag(), getText()));
		return sBuilder.toString();
	}
}
