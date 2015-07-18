package com.ltmonitor.jt808.protocol;

/**
 * 文本信息上传
 */
public class JT_0300 implements IMessageBody {
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
		buff.put(getText());
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
		sBuilder.append(String
				.format("标志：%1$s,文本内容：%2$s", getFlag(), getText()));
		return sBuilder.toString();
	}
}