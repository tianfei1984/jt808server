package com.ltmonitor.jt808.protocol;

public class AnswerItem
{
	/** 
	 答案ID
	 
	*/
	private byte answerId;
	public final byte getAnswerId()
	{
		return answerId;
	}
	public final void setAnswerId(byte value)
	{
		answerId = value;
	}
	/** 
	 答案内容长度
	 
	*/

//ORIGINAL LINE: private ushort answerLength;
	private short answerLength;

//ORIGINAL LINE: public ushort getAnswerLength()
	public final short getAnswerLength()
	{
		return answerLength;
	}

//ORIGINAL LINE: public void setAnswerLength(ushort value)
	public final void setAnswerLength(short value)
	{
		answerLength = value;
	}
	/** 
	 答案内容
	 
	*/
	private String answerContent;
	public final String getAnswerContent()
	{
		return answerContent;
	}
	public final void setAnswerContent(String value)
	{
		answerContent = value;
	}
}