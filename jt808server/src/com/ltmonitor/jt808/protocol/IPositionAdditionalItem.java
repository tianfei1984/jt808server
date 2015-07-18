package com.ltmonitor.jt808.protocol;


public interface IPositionAdditionalItem
{
	int getAdditionalId();
	byte getAdditionalLength();
	byte[] WriteToBytes();
	void ReadFromBytes(byte[] bytes);
}