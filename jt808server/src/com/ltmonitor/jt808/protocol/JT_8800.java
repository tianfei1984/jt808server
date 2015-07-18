package com.ltmonitor.jt808.protocol;

/** 
 多媒体数据上传应答
 
*/
public class JT_8800 implements IMessageBody
{
	/** 
	 多媒体ID
	 
	*/

	private int multimediaDataId;

	public final int getMultimediaDataId()
	{
		return multimediaDataId;
	}

	public final void setMultimediaDataId(int value)
	{
		multimediaDataId = value;
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
	private java.util.ArrayList<Short> repassPacketsNo;
	public final java.util.ArrayList<Short> getRepassPacketsNo()
	{
		return repassPacketsNo;
	}

//ORIGINAL LINE: public void setRepassPacketsNo(List<ushort> value)
	public final void setRepassPacketsNo(java.util.ArrayList<Short> value)
	{
		repassPacketsNo = value;
	}

	public final byte[] WriteToBytes()
	{
		MyBuffer bytes = new MyBuffer();
		bytes.put((byte)(getMultimediaDataId() >> 24));
		bytes.put((byte)(getMultimediaDataId() >> 16));
		bytes.put((byte)(getMultimediaDataId() >> 8));
		bytes.put((byte)getMultimediaDataId());
		bytes.put(getRepassPacketsCount());

//ORIGINAL LINE: foreach (ushort repassPacketNo in RepassPacketsNo)
		for (short repassPacketNo : getRepassPacketsNo())
		{
			bytes.put((byte)(repassPacketNo >> 8));
			bytes.put((byte)repassPacketNo);
		}
		return bytes.array();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setMultimediaDataId((int)((bytes[0] << 24) + (bytes[1] << 16) + (bytes[2] << 8) + bytes[3]));
		setRepassPacketsCount(bytes[4]);
		setRepassPacketsNo(new java.util.ArrayList<Short>(getRepassPacketsCount()));
		int pos = 5;
		while (pos < bytes.length)
		{
			getRepassPacketsNo().add((short)((bytes[pos] << 8) + bytes[1]));
			pos += 2;
		}
	}
}