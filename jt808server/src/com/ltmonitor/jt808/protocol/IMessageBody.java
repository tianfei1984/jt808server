package com.ltmonitor.jt808.protocol;

public interface IMessageBody
{
	byte[] WriteToBytes();
	void ReadFromBytes(byte[] messageBodyBytes);
}