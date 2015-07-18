package com.ltmonitor.jt808.protocol;

/** 
 单条存储多媒体数据检索上传命令
 
*/
public class JT_8805 implements IMessageBody
{
	/** 
	 多媒体ID
	 
	*/

//ORIGINAL LINE: private uint multimediaId;
	private int multimediaId;

//ORIGINAL LINE: public uint getMultimediaId()
	public final int getMultimediaId()
	{
		return multimediaId;
	}

//ORIGINAL LINE: public void setMultimediaId(uint value)
	public final void setMultimediaId(int value)
	{
		multimediaId = value;
	}
	/** 
	 删除标志
	 
	*/
	private byte deleteFlag;
	public final byte getDeleteFlag()
	{
		return deleteFlag;
	}
	public final void setDeleteFlag(byte value)
	{
		deleteFlag = value;
	}
	public final byte[] WriteToBytes()
	{
		byte[] bytes = new byte[5];
		bytes[0] = (byte)(getMultimediaId() >> 24);
		bytes[1] = (byte)(getMultimediaId() >> 16);
		bytes[2] = (byte)(getMultimediaId() >> 8);
		bytes[3] = (byte)(getMultimediaId());
		bytes[4] = getDeleteFlag();
		return bytes;
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setMultimediaId((int)((bytes[0] << 24) + (bytes[1] << 16) + (bytes[2] << 8) + bytes[3]));
		setDeleteFlag(bytes[4]);
	}
}