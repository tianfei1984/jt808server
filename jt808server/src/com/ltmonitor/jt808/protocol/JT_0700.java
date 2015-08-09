package com.ltmonitor.jt808.protocol;

import org.apache.log4j.Logger;

/** 
 行驶记录数据上传
 
*/
public class JT_0700 implements IMessageBody
{
	protected Logger logger = Logger.getLogger(getClass());
	private byte[] cmdData ; //命令字的数据用于转发
	private short responseMessageSerialNo;

	public final short getResponseMessageSerialNo()
	{
		return responseMessageSerialNo;
	}

	public final void setResponseMessageSerialNo(short value)
	{
		responseMessageSerialNo = value;
	}
	private byte commandWord;
	public final byte getCommandWord()
	{
		return commandWord;
	}
	public final void setCommandWord(byte value)
	{
		commandWord = value;
	}
	private IRecorderDataBlock data;
	public final IRecorderDataBlock getData()
	{
		return data;
	}
	public final void setData(IRecorderDataBlock value)
	{
		data = value;
	}
	public final byte[] WriteToBytes()
	{
		MyBuffer bytes = new MyBuffer();
		bytes.put((byte)(getResponseMessageSerialNo() >> 8));
		bytes.put((byte)getResponseMessageSerialNo());
		bytes.put(getCommandWord());
		bytes.put(getData().WriteToBytes());
		return bytes.array();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		short t = (short)BitConverter.ToUInt16(bytes, 0);
		//setResponseMessageSerialNo((short)((bytes[0] << 8) + bytes[1]));
		this.responseMessageSerialNo = t;
		this.commandWord = (bytes[2]);
		this.cmdData = new byte[bytes.length];
		//将命令字的数据填充到缓冲中，便于转发
		System.arraycopy(bytes, 0, cmdData, 0, cmdData.length); 
		
		byte h1 = bytes[3];
		byte h2= bytes[4];
		int recordWord = bytes[5];
		int dataLength = (int)((bytes[6] << 8) + bytes[7]);
		int resved = bytes[8];
		byte[] blockBytes = new byte[bytes.length - 9];
		System.arraycopy(bytes, 9, blockBytes, 0, blockBytes.length);
		
		this.data = RecorderDataBlockFactory.Create(getCommandWord());
		if(data != null)
		  data.ReadFromBytes(blockBytes);
		else
		{
			logger.error("记录仪返回非法的命令字：" + this.getCommandWord());
		}
	}

	public byte[] getCmdData() {
		return cmdData;
	}

	public void setCmdData(byte[] cmdData) {
		this.cmdData = cmdData;
	}
}