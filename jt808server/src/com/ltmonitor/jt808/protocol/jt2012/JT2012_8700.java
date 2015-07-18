package com.ltmonitor.jt808.protocol.jt2012;

import com.ltmonitor.jt808.protocol.IMessageBody;

/** 
 行驶记录数据上传(0x0710)  (仅适用于GB/T1905-2012)
 
*/
public class JT2012_8700 implements IMessageBody
{
	/** 
	 应答流水号
	 
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: private ushort privateResponseMessageSerialNo;
	private short privateResponseMessageSerialNo;
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public ushort getResponseMessageSerialNo()
	public final short getResponseMessageSerialNo()
	{
		return privateResponseMessageSerialNo;
	}
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public void setResponseMessageSerialNo(ushort value)
	public final void setResponseMessageSerialNo(short value)
	{
		privateResponseMessageSerialNo = value;
	}
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

	private byte[] _data_1905_2012;
	/** 
	 数据块原始数据
	 
	*/
	public final byte[] getData_1905_2012()
	{
		return _data_1905_2012;
	}
	public final void setData_1905_2012(byte[] value)
	{
		_data_1905_2012 = value;
	}

	/** 
	 行驶记录仪信息
	 
	*/
	private IRecorderDataBlock_2012 privateData;
	public final IRecorderDataBlock_2012 getData()
	{
		return privateData;
	}
	public final void setData(IRecorderDataBlock_2012 value)
	{
		privateData = value;
	}

	/** 
	 总包数
	 
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: ushort _totalPacketsCount=1;
	private short _totalPacketsCount=1;
	/** 
	 总包数
	 
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public ushort getTotalPacketsCount()
	public final short getTotalPacketsCount()
	{
		return _totalPacketsCount;
	}

//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: ushort _packetNo = 1;
	private short _packetNo = 1;
	/** 
	 包序号
	 
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public ushort getPacketNo()
	public final short getPacketNo()
	{
		return _packetNo;
	}

	/** 
	 行驶记录数据下传命令
	 
	*/
	public JT2012_8700()
	{
		_totalPacketsCount = 1;
		_packetNo = 1;
	}

	/** 
	 行驶记录数据下传命令自定义包数
	 
	 @param totalPacketsCount
	 @param packetNo
	*/
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public JT_0x0710(ushort totalPacketsCount, ushort packetNo)
	public JT2012_8700(short totalPacketsCount, short packetNo)
	{
		_totalPacketsCount = totalPacketsCount;
		_packetNo = packetNo;
	}

	/** 
	 下行命令
	 
	 @return 
	*/
	public final byte[] WriteToBytes()
	{
		byte[] bytesHead = new byte[3];
		bytesHead[0] = ((byte)(getResponseMessageSerialNo() >> 8));
		bytesHead[1]=((byte)getResponseMessageSerialNo());
		bytesHead[2] = getCommandWord();
		byte[] bytes = bytesHead;
		if (getCommandWord() == 0x82 || getCommandWord() == 0x83 || getCommandWord() == 0x84 || getCommandWord() == 0xC2 || getCommandWord() == 0xC3 || getCommandWord() == 0xC4 || getCommandWord() == 0x08 || getCommandWord() == 0x09 || getCommandWord() == 0x10 || getCommandWord() == 0x11 || getCommandWord() == 0x12 || getCommandWord() == 0x13 || getCommandWord() == 0x14 || getCommandWord() == 0x15)
		{
			byte[] temp = getData().WriteToBytes();
			bytes = new byte[3 + temp.length];
			System.arraycopy(bytesHead, 0, bytes, 0, 3);
			System.arraycopy(temp, 0, bytes, 3, temp.length);
		}
		return bytes;
	}

	/** 
	 上传数据
	 
	 @param bytes    
	*/
	public final void ReadFromBytes(byte[] bytes)
	{
		setResponseMessageSerialNo((short)((bytes[0] << 8) + bytes[1]));
		setCommandWord(bytes[2]);
		byte[] blockBytes = new byte[bytes.length - 3];
		System.arraycopy(bytes, 3, blockBytes, 0, bytes.length - 3);
		setData(RecorderDataBlockFactory_2012.Create(getCommandWord()));
		getData().ReadFromBytes(blockBytes);

	}
}
