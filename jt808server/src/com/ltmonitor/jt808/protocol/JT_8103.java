package com.ltmonitor.jt808.protocol;

import com.ltmonitor.entity.StringUtil;

/**
 * 设置终端参数
 */
public class JT_8103 implements IMessageBody {
	/**
	 * 参数个数
	 */
	private byte parametersCount;

	public final byte getParametersCount() {
		return parametersCount;
	}

	public final void setParametersCount(byte value) {
		parametersCount = value;
	}

	/**
	 * 参数列表
	 */
	private java.util.ArrayList<ParameterItem> parameters;

	public final java.util.ArrayList<ParameterItem> getParameters() {
		return parameters;
	}

	public final void setParameters(java.util.ArrayList<ParameterItem> value) {
		parameters = value;
	}

	private String GetFieldType(int paramField) {
		return "" + JT808Common.GetParamType(paramField);
	}

	public final byte[] WriteToBytes() {

		MyBuffer buff = new MyBuffer();

		buff.put(getParametersCount());
		for (ParameterItem item : getParameters()) {
			buff.put(item.getParameterId());
			// buff.put(item.ParameterLength);

			String fieldType = GetFieldType((int) item.getParameterId());
			String strParamValue = ""+item.getParameterValue();
			if (fieldType.equals("BYTE") && StringUtil.isNullOrEmpty(strParamValue) == false) // 参数值类型为byte
			{
				item.setParameterLength((byte)1);
				buff.put(item.getParameterLength());
				buff.put(Byte.parseByte(strParamValue));
			}
			// ORIGINAL LINE: case "WORD":
			else if (fieldType.equals("WORD") && StringUtil.isNullOrEmpty(strParamValue) == false) // 参数值类型为16位无符号整形数值
			{
				item.setParameterLength((byte)2);
				buff.put(item.getParameterLength());
				buff.put(Short.parseShort(strParamValue));
			}
			// ORIGINAL LINE: case "DWORD":
			else if (fieldType.equals("DWORD") && StringUtil.isNullOrEmpty(strParamValue) == false) // 参数值类型为32位无符号整形数值
			{
				item.setParameterLength((byte)4);
				buff.put(item.getParameterLength());
				int paramValue1 = Integer.parseInt(strParamValue);
				buff.put(paramValue1);
				//buff.put(((Integer) item.getParameterValue()).intValue());
			} else // 参数值类型为字符串
			{
				byte[] strBytes =BitConverter.getBytes(item
						.getParameterValue().toString());
				item.setParameterLength((byte) (strBytes.length));
				buff.put(item.getParameterLength());
				buff.put(strBytes);
				//buff.put((byte) 0x00);
			}
		}
		return buff.array();
	}

	public final void ReadFromBytes(byte[] bytes) {

		MyBuffer buff = new MyBuffer(bytes);

		setParametersCount(buff.get());
		setParameters(new java.util.ArrayList<ParameterItem>(
				getParametersCount()));
		int pos = 1;
		while (buff.hasRemain()) {
			ParameterItem item = new ParameterItem();
			item.setParameterId(buff.getLong());
			pos += 4;
			item.setParameterLength(buff.get());
			pos += 1;
			String fieldType = GetFieldType((int) item.getParameterId());
			// C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a
			// string member and was converted to Java 'if-else' logic:
			// switch (fieldType)
			// ORIGINAL LINE: case "BYTE":
			if (fieldType.equals("BYTE")) // 参数值为byte类型
			{
				item.setParameterValue(buff.get());
			}
			// ORIGINAL LINE: case "WORD":
			else if (fieldType.equals("WORD")) // 参数值为ushort类型
			{
				item.setParameterValue(buff.getShort());
			}
			// ORIGINAL LINE: case "DWORD":
			else if (fieldType.equals("DWORD")) // 参数值为uint类型
			{
				item.setParameterValue(buff.getLong());
			} else {
				byte[] strBytes = buff.gets(item.getParameterLength());
				String strValue = BitConverter.getString(strBytes, 0,
						strBytes.length);
				item.setParameterValue(strValue);
			}
			getParameters().add(item);
			pos += item.getParameterLength();
		}

	}
}