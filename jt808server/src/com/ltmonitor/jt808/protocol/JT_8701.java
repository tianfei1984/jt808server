package com.ltmonitor.jt808.protocol;

/** 
 行驶记录参数下传命令
 
*/
public class JT_8701 implements IMessageBody
{
	/** 
	 命令字
	 
	*/
	private byte commandWord;
	public final byte getCommandWord()
	{
		return commandWord;
	}
	public final void setCommandWord(byte value)
	{
		commandWord = value;
	}
	/** 
	 数据块
	 
	*/
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
		bytes.put(getCommandWord());
		bytes.put(getData().WriteToBytes());
		return bytes.array();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setCommandWord(bytes[0]);
		byte[] blockBytes = new byte[bytes.length - 1];
		System.arraycopy(bytes, 1, blockBytes, 0, bytes.length - 1);
		setData(RecorderDataBlockFactory.Create(getCommandWord()));
		getData().ReadFromBytes(blockBytes);
	}
}