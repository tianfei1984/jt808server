package com.ltmonitor.jt808.protocol;

import java.io.UnsupportedEncodingException;

/** 
 终端鉴权
 
*/
public class JT_0102 implements IMessageBody
{
	private String registerNo;
	public final String getRegisterNo()
	{
		return registerNo;
	}
	public final void setRegisterNo(String value)
	{
		registerNo = value;
	}
	public final byte[] WriteToBytes()
	{
			return getRegisterNo().getBytes();
	}

	public final void ReadFromBytes(byte[] bytes)
	{
		setRegisterNo(new String(bytes));
	}

	@Override
	public String toString()
	{
		return String.format("鉴权码：%1$s", getRegisterNo());
	}
}