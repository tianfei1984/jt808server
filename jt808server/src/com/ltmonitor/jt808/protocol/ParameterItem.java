package com.ltmonitor.jt808.protocol;


public class ParameterItem
{
	/** 
	 参数ID
	 
	*/

//ORIGINAL LINE: private uint parameterId;
	private int parameterId;

//ORIGINAL LINE: public uint getParameterId()
	public final int getParameterId()
	{
		return parameterId;
	}

//ORIGINAL LINE: public void setParameterId(uint value)
	public final void setParameterId(int value)
	{
		parameterId = value;
	}
	/** 
	 参数类型，0:Byte,1:ushort,2:uint,N:string以0x00结尾
	 
	*/
	private byte parameterLength;
	public final byte getParameterLength()
	{
		return parameterLength;
	}
	public final void setParameterLength(byte value)
	{
		parameterLength = value;
	}
	/** 
	 参数值
	 
	*/
	private Object parameterValue;
	public final Object getParameterValue()
	{
		return parameterValue;
	}
	public final void setParameterValue(Object value)
	{
		parameterValue = value;
	}
}