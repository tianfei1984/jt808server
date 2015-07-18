package com.ltmonitor.jt808.protocol;

import java.io.UnsupportedEncodingException;

/**
 * 查询终端参数应答
 */
public class JT_0104 implements IMessageBody {
	/**
	 * 应答流水号,对应的终端参数查询消息的流水号
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
	 * 应答参数个数
	 */
	private byte parametersCount;

	public final byte getParametersCount() {
		return parametersCount;
	}

	public final void setParametersCount(byte value) {
		parametersCount = value;
	}

	/**
	 * 参数项列表
	 */
	private java.util.ArrayList<ParameterItem> parameters;

	public final java.util.ArrayList<ParameterItem> getParameters() {
		return parameters;
	}

	public final void setParameters(java.util.ArrayList<ParameterItem> value) {
		parameters = value;
	}

	public final byte[] WriteToBytes() {
		MyBuffer buff = new MyBuffer();

		buff.put(getResponseMessageSerialNo());
		buff.put(getParametersCount());
		for (ParameterItem item : getParameters()) {
			buff.put(item.getParameterId());
			// buff.put(item.ParameterLength);
			switch (item.getParameterLength()) {
			case 1: // 参数值类型为byte
				buff.put(item.getParameterLength());
				buff.put(Byte.parseByte("" + item.getParameterValue()));
				break;
			case 2: // 参数值类型为16位无符号整形数值
				buff.put(item.getParameterLength());
				buff.put(Short.parseShort("" + item.getParameterValue()));
				break;
			case 4: // 参数值类型为32位无符号整形数值
				buff.put(item.getParameterLength());
				buff.put(Integer.parseInt(""+ item.getParameterValue()));
				break;
			default: // 参数值类型为字符串
			{
				byte[] strBytes = item.getParameterValue().toString().getBytes();
				item.setParameterLength((byte) (strBytes.length));
				buff.put(item.getParameterLength());
				buff.put(strBytes);
				//buff.put((byte) 0x00);
			}
				break;
			}
		}

		return buff.array();

	}

	public final void ReadFromBytes(byte[] bytes) {

		MyBuffer buff = new MyBuffer(bytes);

		setResponseMessageSerialNo(buff.getShort());
		setParametersCount(buff.get());
		setParameters(new java.util.ArrayList<ParameterItem>());
		int pos = 3;
		while ( buff.hasRemain()) {
			ParameterItem item = new ParameterItem();
			item.setParameterId(buff.getLong());
			pos += 4;
			item.setParameterLength(buff.get());
			pos += 1;
			switch (item.getParameterLength()) {
			case 1: // 参数值为byte类型
				item.setParameterValue(buff.get());
				break;
			case 2: // 参数值为ushort类型
				item.setParameterValue(buff.getShort());
				break;
			case 4: // 参数值为uint类型
				item.setParameterValue(buff.getLong());
				break;
			default:
				byte[] strBytes = buff.gets(item.getParameterLength());
				String strValue;
				try {
					strValue = new String(strBytes, "gbk");
					item.setParameterValue(strValue);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			this.parameters.add(item);
			pos += item.getParameterLength();
		}
	}

}