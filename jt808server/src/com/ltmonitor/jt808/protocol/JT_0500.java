package com.ltmonitor.jt808.protocol;

/** 
 车辆控制应答
 
*/
public class JT_0500 implements IMessageBody
{
	/** 
	 应答流水号
	 
	*/

//ORIGINAL LINE: private ushort responseMessageSerialNo;
	private short responseMessageSerialNo;

//ORIGINAL LINE: public ushort getResponseMessageSerialNo()
	public final short getResponseMessageSerialNo()
	{
		return responseMessageSerialNo;
	}

//ORIGINAL LINE: public void setResponseMessageSerialNo(ushort value)
	public final void setResponseMessageSerialNo(short value)
	{
		responseMessageSerialNo = value;
	}
	/** 
	 位置信息汇报消息体,根据对应的状态位判断控制成功与否
	 
	*/
	private JT_0200 positionReport;
	public final JT_0200 getPositionReport()
	{
		return positionReport;
	}
	public final void setPositionReport(JT_0200 value)
	{
		positionReport = value;
	}
	public final byte[] WriteToBytes()
	{
		MyBuffer buff = new MyBuffer();
		buff.put((byte)(getResponseMessageSerialNo() >> 8));
		buff.put((byte)getResponseMessageSerialNo());
		buff.put(getPositionReport().WriteToBytes());
		return buff.array();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setResponseMessageSerialNo((short)((bytes[0] << 8) + bytes[1]));
		if (bytes.length > 2)
		{
			setPositionReport(new JT_0200());
			byte[] posBytes = new byte[bytes.length - 2];
			System.arraycopy(bytes, 2, posBytes, 0, bytes.length - 2);
			getPositionReport().ReadFromBytes(posBytes);
		}
	}

	@Override
	public String toString()
	{
		return String.format("流水号：%1$s,%2$s", getResponseMessageSerialNo(), getPositionReport().toString());
	}
}