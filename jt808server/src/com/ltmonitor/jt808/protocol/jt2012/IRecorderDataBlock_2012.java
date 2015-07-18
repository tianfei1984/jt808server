package com.ltmonitor.jt808.protocol.jt2012;

public interface IRecorderDataBlock_2012
{
	byte getCommandWord();
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: ushort getDataLength();
	short getDataLength();
	byte[] WriteToBytes();
	void ReadFromBytes(byte[] bytes);
}
