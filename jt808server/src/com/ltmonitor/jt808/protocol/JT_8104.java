package com.ltmonitor.jt808.protocol;

/**
 * 查询终端参数,查询终端参数消息体为空。则返回主要的参数。也可下发查询具体的几个参数；
 */
public class JT_8104 implements IMessageBody {
	/**
	 * 要查询的参数个数
	 */
	private byte parametersCount;

	public final byte getParametersCount() {
		return parametersCount;
	}

	public final void setParametersCount(byte value) {
		parametersCount = value;
	}

	/**
	 * 要查询的参数列表
	 */
	private java.util.ArrayList<Integer> parametersIDs;

	public final java.util.ArrayList<Integer> getParametersIDs() {
		return parametersIDs;
	}

	// ORIGINAL LINE: public void setParametersIDs(List<uint> value)
	public final void setParametersIDs(java.util.ArrayList<Integer> value) {
		parametersIDs = value;
	}

	public final byte[] WriteToBytes() {
		if (getParametersCount() > 0 && getParametersIDs() != null
				&& getParametersIDs().size() > 0) {

			MyBuffer buff = new MyBuffer();

			buff.put(getParametersCount());
			if (getParametersCount() > 0 && getParametersIDs() != null
					&& getParametersIDs().size() > 0) {

				// ORIGINAL LINE: foreach (uint parameterId in ParametersIDs)
				for (int parameterId : getParametersIDs()) {
					buff.put(parameterId);
				}
			}

			return buff.array();

		}

		return null;

	}

	public final void ReadFromBytes(byte[] bytes) {

		if (bytes != null && bytes.length > 0) {

			MyBuffer buff = new MyBuffer(bytes);

			setParametersCount(buff.get());
			setParametersIDs(new java.util.ArrayList<Integer>(
					getParametersCount()));
			int pos = 1;
			while (buff.hasRemain()) {
				getParametersIDs().add(buff.getLong());
				pos += 4;
			}

		}

	}
}