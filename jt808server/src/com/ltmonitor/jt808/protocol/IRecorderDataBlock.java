package com.ltmonitor.jt808.protocol;

public interface IRecorderDataBlock
{
	byte getCommandWord();

//ORIGINAL LINE: ushort getDataLength();
	short getDataLength();
	byte[] WriteToBytes();
	void ReadFromBytes(byte[] bytes);

	String toString();
}