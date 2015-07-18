package com.ltmonitor.jt808.protocol;

/** 
 设置电话本
 
*/
public class JT_8401 implements IMessageBody
{
	/** 
	 设置类型,0：删除终端上所有存储的联系人；  1：表示更新电话本（删除终端中已有全部联系人并追加消息中的联系人）； 2：表示追加电话本； 3：表示修改电话本（以联系人为索引）
	 
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
	 联系人总数
	 
	*/
	private byte phoneNosCount;
	public final byte getPhoneNosCount()
	{
		return phoneNosCount;
	}
	public final void setPhoneNosCount(byte value)
	{
		phoneNosCount = value;
	}
	/** 
	 联系人项
	 
	*/
	private java.util.ArrayList<PhoneNoItem> contacts;
	public final java.util.ArrayList<PhoneNoItem> getContacts()
	{
		return contacts;
	}
	public final void setContacts(java.util.ArrayList<PhoneNoItem> value)
	{
		contacts = value;
	}

	public final byte[] WriteToBytes()
	{
		MyBuffer bytes = new MyBuffer();
		bytes.put(getSettingType());
		if (getPhoneNosCount() > 0 && getContacts() != null && getContacts().size() > 0)
		{
			bytes.put(getPhoneNosCount());
			for (PhoneNoItem item : getContacts())
			{
				bytes.put(item.getDailFlag());
				byte[] phoneNoBytes = BitConverter.getBytes(item.getPhoneNo());
				bytes.put((byte)(phoneNoBytes.length));
				bytes.put(phoneNoBytes);
				byte[] contactBytes = BitConverter.getBytes(item.getContact());
				bytes.put((byte)(contactBytes.length));
				bytes.put(contactBytes);
			}
		}
		return bytes.array();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		MyBuffer buff = new MyBuffer(bytes);
		setSettingType(buff.get());
		setPhoneNosCount(buff.get());
		setContacts(new java.util.ArrayList<PhoneNoItem>(getPhoneNosCount()));
		int pos = 2;
		while (pos < bytes.length)
		{
			PhoneNoItem item = new PhoneNoItem();
			item.setDailFlag(buff.get());
			item.setPhoneNoLength(buff.get());
			item.setPhoneNo(buff.getString(item.getPhoneNoLength()));
			item.setContactLength(bytes[pos + 2 + item.getPhoneNoLength()]);
			item.setContact(buff.getString(item.getContactLength()));
			getContacts().add(item);
			pos = pos + 3 + item.getPhoneNoLength() + item.getContactLength();
		}
	}

	@Override
	public String toString()
	{
		String linkInfo = "";
		if (getContacts() != null)
		{
			for (int i = 0; i < getContacts().size(); i++)
			{
				PhoneNoItem item = getContacts().get(i);
				linkInfo += String.format("标志：%1$s,电话号码：%2$s,联系人：%3$s", item.getDailFlag(), item.getPhoneNo(), item.getContact());
			}
		}
		return String.format("设置类型：%1$s,联系人总数：%2$s,%3$s", getSettingType(), getPhoneNosCount(), linkInfo);
	}
}