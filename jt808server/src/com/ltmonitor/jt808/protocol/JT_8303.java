package com.ltmonitor.jt808.protocol;

/** 
 信息点播菜单设置
 
*/
public class JT_8303 implements IMessageBody
{
	/** 
	 设置类型,0：删除终端全部信息项；  1：更新菜单；  2：追加菜单；  3：修改菜单
	 
	*/
	private byte settingType;
	public final byte getSettingType()
	{
		return settingType;
	}
	public final void setSettingType(byte value)
	{
		settingType = value;
	}
	/** 
	 信息项总数
	 
	*/
	private byte privateInfoItemsCount;
	public final byte getInfoItemsCount()
	{
		return privateInfoItemsCount;
	}
	public final void setInfoItemsCount(byte value)
	{
		privateInfoItemsCount = value;
	}
	/** 
	 信息项列表
	 
	*/
	private java.util.ArrayList<PointcastMessageItem> messages;
	public final java.util.ArrayList<PointcastMessageItem> getMessages()
	{
		return messages;
	}
	public final void setMessages(java.util.ArrayList<PointcastMessageItem> value)
	{
		messages = value;
	}
	public final byte[] WriteToBytes()
	{
		MyBuffer bytes = new MyBuffer();
		bytes.put(getSettingType());
		if (getInfoItemsCount() > 0 && getMessages() != null && getMessages().size() > 0)
		{
			bytes.put(getInfoItemsCount());
			for (PointcastMessageItem item : getMessages())
			{
				bytes.put(item.getMessageType());
				byte[] messageBytes = BitConverter.getBytes(item.getMessage());
				item.setMessageLength((short)(messageBytes.length));
				bytes.put((byte)(item.getMessageLength() >> 8));
				bytes.put((byte)item.getMessageLength());
				bytes.put(messageBytes);
				//bytes.put(0x00);
			}
		}
		return bytes.array();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		MyBuffer buff = new MyBuffer(bytes);
		setSettingType(buff.get());
		if (bytes.length > 1)
		{
			setInfoItemsCount(buff.get());
			setMessages(new java.util.ArrayList<PointcastMessageItem>(getInfoItemsCount()));
			int pos = 2;
			while (pos < bytes.length)
			{
				PointcastMessageItem item = new PointcastMessageItem();
				item.setMessageType(buff.get());
				item.setMessageLength(buff.getShort());
				item.setMessage(buff.getString());
				getMessages().add(item);
				pos = pos + 3 + item.getMessageLength();
			}
		}
	}

	@Override
	public String toString()
	{
		return String.format("设置类型:%1$s,信息项总数：%2$s", getSettingType(), getInfoItemsCount());
	}
}