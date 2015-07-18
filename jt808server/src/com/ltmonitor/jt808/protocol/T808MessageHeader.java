package com.ltmonitor.jt808.protocol;

import org.apache.mina.core.buffer.IoBuffer;

import com.ltmonitor.jt808.tool.Tools;


public class T808MessageHeader {

	public final int getHeaderSize() {
		if (getIsPackage()) { // 有分包
			return 16 + 3; // 两个分隔符，一个校验字节
		} else {
			return 12 + 3; // 两个分隔符，一个校验字节
		}
	}

	/**
	 * 消息ID，2个字节，无符号16位
	 */
	private int messageType;

	public final int getMessageType() {
		return messageType;
	}

	public final void setMessageType(int value) {
		messageType = value;
	}

	/**
	 * 消息体长度
	 */
	public final int getMessageSize() {
		return getMessageBodyProperty() & 0x03FF;
	}

	public final void setMessageSize(int value) {
		boolean res = getIsPackage();
		if (res) {
			setMessageBodyProperty((short) (0x2000 | value));
		} else {
			setMessageBodyProperty((short) (value));
		}
	}

	/**
	 * 消息体属性
	 */
	
	
	// ORIGINAL LINE: private ushort messageBodyProperty;
	private short messageBodyProperty;

	
	
	// ORIGINAL LINE: public ushort getMessageBodyProperty()
	public final short getMessageBodyProperty() {
		return messageBodyProperty;
	}

	
	
	// ORIGINAL LINE: public void setMessageBodyProperty(ushort value)
	public final void setMessageBodyProperty(short value) {
		messageBodyProperty = value;
	}

	/**
	 * 终端手机号
	 */
	private String simId;

	public final String getSimId() {
		
		return simId;
	}

	public final void setSimId(String value) {
		
		simId = value;
	}

	/**
	 * 消息流水号
	 */
	private short messageSerialNo;

	
	
	public final short getMessageSerialNo() {
		return messageSerialNo;
	}

	
	
	public final void setMessageSerialNo(short value) {
		messageSerialNo = value;
	}

	/**
	 * 总包数
	 */
	
	private short messageTotalPacketsCount;

	
	
	public final short getMessageTotalPacketsCount() {
		return messageTotalPacketsCount;
	}

	
	
	public final void setMessageTotalPacketsCount(short value) {
		messageTotalPacketsCount = value;
	}

	/**
	 * 分包号
	 */
	
	
	private short messagePacketNo;

	
	
	public final short getMessagePacketNo() {
		return messagePacketNo;
	}

	
	
	
	public final void setMessagePacketNo(short value) {
		messagePacketNo = value;
	}

	/**
	 * 分包发送
	 */
	public final boolean getIsPackage() {
		return (getMessageBodyProperty() & 0x2000) == 0x2000;
	}

	public final void setIsPackage(boolean value) {
		if (value) {
			messageBodyProperty |= 0x2000;
		} else {
			messageBodyProperty &= 0xDFFF;
		}
	}

	public final byte[] WriteToBytes() {

		MyBuffer buff = new MyBuffer();
			buff.putShort((short) getMessageType());
			buff.putShort(getMessageBodyProperty());
			while(simId.length() < 12)
			{
				simId = "0" + simId;
			}
			byte[] simIdBytes = Tools.HexString2Bytes(simId);
			buff.put(simIdBytes);
			buff.putShort(getMessageSerialNo());
			if (getIsPackage()) {
				buff.putShort(getMessageTotalPacketsCount());
				buff.putShort(getMessagePacketNo());
			}

		return buff.array();

	}

	public final void ReadFromBytes(byte[] headerBytes) {

		if (BitConverter.IsLittleEndian) {
			setMessageType((headerBytes[0] << 8) + headerBytes[1]);
			setMessageBodyProperty((short) ((headerBytes[2] << 8) + headerBytes[3]));
			simId = (String.format("%02X", headerBytes[4])
					+ String.format("%02X", headerBytes[5])
					+ String.format("%02X", headerBytes[6])
					+ String.format("%02X", headerBytes[7])
					+ String.format("%02X", headerBytes[8])
					+ String.format("%02X", headerBytes[9]));
			simId = (simId.substring(1, getSimId().length()));
			short t = (short)BitConverter.ToUInt16(headerBytes, 10);
			setMessageSerialNo(t);
			if (getIsPackage()) {
				setMessageTotalPacketsCount((short) ((headerBytes[12] << 8) + headerBytes[13]));
				setMessagePacketNo((short) ((headerBytes[14] << 8) + headerBytes[15]));
			}
		} else {
			MyBuffer buff = new MyBuffer(headerBytes);
			setMessageType(buff.getShort());
			setMessageBodyProperty(buff.getShort());
			byte[] simIdBytes = buff.gets(6);
			setSimId(String.format("%02X", simIdBytes[0])
					+ String.format("%02X", simIdBytes[1])
					+ String.format("%02X", simIdBytes[2])
					+ String.format("%02X", simIdBytes[3])
					+ String.format("%02X", simIdBytes[4])
					+ String.format("%02X", simIdBytes[5]));
			setSimId(getSimId().substring(1, getSimId().length()));
			setMessageSerialNo(buff.getShort());
			if (getIsPackage()) {
				setMessageTotalPacketsCount(buff.getShort());
				setMessagePacketNo(buff.getShort());
			}
		}
	}

}