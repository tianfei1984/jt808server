package com.ltmonitor.jt808.protocol;

/** 
 行驶记录数据采集命令
 
*/
public class JT_8700 implements IMessageBody
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
	 重传包总数
	 
	*/
	private byte repassPacketsCount;
	public final byte getRepassPacketsCount()
	{
		return repassPacketsCount;
	}
	public final void setRepassPacketsCount(byte value)
	{
		repassPacketsCount = value;
	}
	/** 
	 重传包ID列表
	 
	*/
	private java.util.ArrayList<Short> repassPacketsNos;
	public final java.util.ArrayList<Short> getRepassPacketsNos()
	{
		return repassPacketsNos;
	}

//ORIGINAL LINE: public void setRepassPacketsNos(List<ushort> value)
	public final void setRepassPacketsNos(java.util.ArrayList<Short> value)
	{
		repassPacketsNos = value;
	}
	public final byte[] WriteToBytes()
	{
		if (getCommandWord() == 0x20)
		{
			MyBuffer bytes = new MyBuffer();
			bytes.put(getCommandWord());
			bytes.put(getRepassPacketsCount());

//ORIGINAL LINE: foreach (ushort packetNo in RepassPacketsNos)
			for (short packetNo : getRepassPacketsNos())
			{
				bytes.put((byte)(packetNo >> 8));
				bytes.put((byte)packetNo);
			}
			return bytes.array();
		}
		else
		{
			return new byte[] { getCommandWord() };
		}
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setCommandWord(bytes[0]);
		if (getCommandWord() == 0x20)
		{
			setRepassPacketsCount(bytes[1]);
			setRepassPacketsNos(new java.util.ArrayList<Short>(getRepassPacketsCount()));
			int pos = 2;
			while (pos < bytes.length)
			{

//ORIGINAL LINE: ushort packetNo = (ushort)((bytes[pos] << 8) + bytes[pos + 1]);
				short packetNo = (short)((bytes[pos] << 8) + bytes[pos + 1]);
				getRepassPacketsNos().add(packetNo);
				pos += 2;
			}
		}
	}
}