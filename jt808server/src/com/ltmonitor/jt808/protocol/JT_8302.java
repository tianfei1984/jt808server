package com.ltmonitor.jt808.protocol;

import java.io.UnsupportedEncodingException;

/**
 * 提问下发
 */
public class JT_8302 implements IMessageBody {
	/**
	 * 标志
	 */
	private byte privateFlag;

	public final byte getFlag() {
		return privateFlag;
	}

	public final void setFlag(byte value) {
		privateFlag = value;
	}

	/**
	 * 问题内容长度
	 */
	private byte privateQuestionLength;

	public final byte getQuestionLength() {
		return privateQuestionLength;
	}

	public final void setQuestionLength(byte value) {
		privateQuestionLength = value;
	}

	/**
	 * 问题
	 */
	private String privateQuestion;

	public final String getQuestion() {
		return privateQuestion;
	}

	public final void setQuestion(String value) {
		privateQuestion = value;
	}

	/**
	 * 候选答案列表
	 */
	private java.util.ArrayList<AnswerItem> candidateAnswers;

	public final java.util.ArrayList<AnswerItem> getCandidateAnswers() {
		return candidateAnswers;
	}

	public final void setCandidateAnswers(java.util.ArrayList<AnswerItem> value) {
		candidateAnswers = value;
	}

	public final byte[] WriteToBytes() {

		MyBuffer buff = new MyBuffer();

		buff.put(getFlag());
		//buff.put(candidateAnswers.size());
		byte[] questionBytes;
		try {
			questionBytes = BitConverter.getBytes(this.privateQuestion);
			buff.put((byte) (questionBytes.length+1));
			buff.put(questionBytes);
			buff.put((byte) 0x00);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (getCandidateAnswers() != null && getCandidateAnswers().size() > 0) {
			for (AnswerItem ai : getCandidateAnswers()) {
				buff.put(ai.getAnswerId());
				byte[] answerBytes = BitConverter.getBytes(ai
						.getAnswerContent());
				buff.put((short) (answerBytes.length+1));
				buff.put(answerBytes);
				buff.put((byte) 0x00);
			}
		}

		return buff.array();

	}

	public final void ReadFromBytes(byte[] bytes) {

		MyBuffer buff = new MyBuffer(bytes);

		setFlag(buff.get());
		setQuestionLength(buff.get());
		setQuestion(buff.getString(getQuestionLength()));
		int pos = getQuestionLength() + 2;
		setCandidateAnswers(new java.util.ArrayList<AnswerItem>());
		while (buff.hasRemain()) {
			AnswerItem ai = new AnswerItem();
			ai.setAnswerId(buff.get());
			ai.setAnswerLength(buff.getShort());
			ai.setAnswerContent(buff.getString());
			getCandidateAnswers().add(ai);
			pos = pos + 3 + ai.getAnswerLength();
		}

	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(String.format("问题：%1$s,答案列表：%2$s,标志：%3$s,答案内容长度：%4$s",
				getQuestion(), "", getFlag(), getQuestionLength()));
		return sBuilder.toString();
	}
}