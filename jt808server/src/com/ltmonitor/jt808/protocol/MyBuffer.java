package com.ltmonitor.jt808.protocol;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

public class MyBuffer {
	IoBuffer buff;
	
	public MyBuffer()
	{
		buff = IoBuffer.allocate(4024);
		buff.mark();
	}
	
	public MyBuffer(byte[] bytes)
	{
		if(bytes.length > 4024)
			buff = IoBuffer.allocate(bytes.length+100);
		else
		    buff = IoBuffer.allocate(4024);
		buff.mark();
		buff.put(bytes);
		buff.limit(bytes.length);
		buff.reset();
	}
	
	public void clear()
	{
		buff.clear();
		buff.mark();
	}
	
	public void put(byte a)
	{
		buff.put(a);
	}
	
	public void put(short a)
	{
		buff.putShort(a);
	}
	public void put(byte[] a)
	{
		buff.put(a);
	}
	
	public boolean hasRemain()
	{
		return buff.remaining() > 0;
	}
	
	
	public void put(int a)
	{
		buff.putInt(a);
	}
	
	public void putShort(int a)
	{
		buff.putShort((short)a);
	}
	
	public void put(String str)
	{
		//US-ASCII

		try {
			byte[] b = str.getBytes("gbk");
			buff.put(b);			
			
		} catch (UnsupportedEncodingException e) {
			System.out.println("异常信息(ControllerReport TurnISN)"
					+ e.getMessage());
		}
	}
	
	public void put(String str, int len)
	{
		byte[] result = new byte[len];
		try {
			byte[] b = str.getBytes("gbk");
			
			System.arraycopy(b, 0, result, 0, b.length);
			
			for(int m = b.length; m < len; m++)
			{
				result[m] = 0;
			}
			buff.put(result);
			
		} catch (UnsupportedEncodingException e) {
			System.out.println("异常信息(ControllerReport TurnISN)"
					+ e.getMessage());
		}
	}
	
	public byte get()
	{
		return buff.get();
	}
	
	
	
	public byte[] gets(int len)
	{
		byte[] data = new byte[len];
		buff.get(data);
		return data;
	}
	
	public int getLong()
	{
		return buff.getInt();
	}
	
	public short getShort()
	{
		//byte b1 = buff.get();
		//byte b2 = buff.get();
		//short x = (short)(b2 << 8 + b1);
		//return x;
		return buff.getShort();
	}
	
	public String getString()
	{
		try {
			String strTemp = buff.getString(Charset.forName("GBK").newDecoder());
			return strTemp;
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String getString(int len)
	{
		try {
			String strTemp = buff.getString(len, Charset.forName("GBK").newDecoder());
			//String strTemp = buff.getString(len, Charset.forName("ASCII").newDecoder());
			return strTemp;
		} catch (CharacterCodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			gets(len);
		}
		return "";
	}
	
	public byte[] array()
	{
		int pos = buff.position();
		byte[] data = new byte[pos];
		buff.reset();
		buff.get(data);
		return data;
	}

	public static void main(String[] args) {
		
		IoBuffer ib = IoBuffer.allocate(1024);
		ib.mark();
		ib.put((byte)128);
		ib.reset();
		//byte b = ib.get();
		//int x = b& 0xff;
		short x = ib.getUnsigned();
		
		short y = ib.getUnsigned(0);
		
		System.out.println(""+x + "," + y);
	}
	

}
