package com.ltmonitor.jt808.protocol;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * 终端注册
 */
public class JT_0100 implements IMessageBody {
	/**
	 * 省域ID
	 */
	private short provinceId;

	public final short getProvinceId() {
		return provinceId;
	}

	public final void setProvinceId(short value) {
		provinceId = value;
	}

	/**
	 * 市、县域ID
	 */
	private short cityId;

	public final short getCityId() {
		return cityId;
	}

	public final void setCityId(short value) {
		cityId = value;
	}

	/**
	 * 制造商ID
	 */
	private String manufactureId;

	public final String getManufactureId() {
		return manufactureId;
	}

	public final void setManufactureId(String value) {
		manufactureId = value;
	}

	/**
	 * 终端型号
	 */
	private String terminalModelNo;

	public final String getTerminalModelNo() {
		return terminalModelNo;
	}

	public final void setTerminalModelNo(String value) {
		terminalModelNo = value;
	}

	/**
	 * 终端ID
	 */
	private String terminalId;

	public final String getTerminalId() {
		return terminalId;
	}

	public final void setTerminalId(String value) {
		terminalId = value;
	}

	/**
	 * 车牌颜色
	 */
	private byte plateColor = 0;

	public final byte getPlateColor() {
		return plateColor;
	}

	public final void setPlateColor(byte value) {
		plateColor = value;
	}

	/**
	 * 车牌号码
	 */
	private String plateNo;

	public final String getPlateNo() {
		return plateNo;
	}

	public final void setPlateNo(String value) {
		plateNo = value;
	}

	public final byte[] WriteToBytes() {
		MyBuffer buff = new MyBuffer();
		if (BitConverter.IsLittleEndian) {
			java.util.ArrayList<Byte> bytes = new java.util.ArrayList<Byte>(50);
			buff.put((byte) (getProvinceId() >> 8));
			buff.put((byte) getProvinceId());
			buff.put((byte) (getCityId() >> 8));
			buff.put((byte) getCityId());
			buff.put(getManufactureId(), 5);
			buff.put(getTerminalModelNo(), 20);
			buff.put(Byte.parseByte(getTerminalId().substring(0, 2), 16));
			buff.put(Byte.parseByte(getTerminalId().substring(2, 4), 16));
			buff.put(Byte.parseByte(getTerminalId().substring(4, 6), 16));
			buff.put(Byte.parseByte(getTerminalId().substring(6, 8), 16));
			buff.put(Byte.parseByte(getTerminalId().substring(8, 10), 16));
			buff.put(Byte.parseByte(getTerminalId().substring(10, 12), 16));
			buff.put((byte) 0xFF);
			// buff.putRange(GetFixedSizedBytes(TerminalId, 7, 0x00));
			buff.put((byte) getPlateColor());
			buff.put(getPlateNo());
			buff.put((byte) 0x00);
			return buff.array();
		} else {

			try {
				try {
					buff.put(getProvinceId());
					buff.put(getCityId());
					buff.put(getManufactureId(), 5);
					buff
							.put(getTerminalModelNo(), 20);
					buff.put(Byte
							.parseByte(getTerminalId().substring(0, 2), 16));
					buff.put(Byte
							.parseByte(getTerminalId().substring(2, 4), 16));
					buff.put(Byte
							.parseByte(getTerminalId().substring(4, 6), 16));
					buff.put(Byte
							.parseByte(getTerminalId().substring(6, 8), 16));
					buff.put(Byte.parseByte(getTerminalId().substring(8, 10),
							16));
					buff.put(Byte.parseByte(getTerminalId().substring(10, 12),
							16));
					buff.put((byte) 0xFF);
					// buff.put(GetFixedSizedBytes(TerminalId, 7, 0x00));
					buff.put((byte) getPlateColor());
					buff.put(getPlateNo());
					buff.put((byte) 0x00);
				} finally {

				}
				return buff.array();
			} finally {

			}
		}
	}

	public final void ReadFromBytes(byte[] bytes) {
		MyBuffer buff = new MyBuffer(bytes);
		if (BitConverter.IsLittleEndian) {
			setProvinceId((short) ((bytes[0] << 8) + bytes[1]));
			setCityId((short) ((bytes[2] << 8) + bytes[3]));

			setManufactureId(buff.getString(5));

			if (bytes.length < 36) {
				// 没有实现808补充协议
				setTerminalModelNo(buff.getString(8));
				setTerminalId(buff.getString(7));
				setPlateColor(buff.get());
				setPlateNo(buff.getString(bytes.length - 25));
			} else {
				// 808补充协议 终端型号是20个字节
				setTerminalModelNo(buff.getString(20));
				setTerminalId(buff.getString(7));
				setPlateColor(buff.get());
				setPlateNo(buff.getString(bytes.length - 37));
			}

		} else {
			setProvinceId(buff.getShort());
			setCityId(buff.getShort());
			this.manufactureId = (buff.getString(5));

			int dataLen = 37;
			if (bytes.length < 36) {
				setTerminalModelNo(buff.getString(8));
				dataLen = 25;
			}else
			    this.terminalModelNo = (buff.getString(20));
			//byte[] bcdBytes = buff.gets(7);
			this.terminalId = (buff.getString(7));
			// TerminalId = buff.getString(7));
			this.plateColor = (buff.get());
			this.plateNo = buff.getString(bytes.length - dataLen);

		}
	}


	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(String.format(
				"省：%1$s,市：%2$s,制造商：%3$s，型号：%4$s，终端：%5$s，车牌颜色：%6$s，车牌号:%7$s",
				getProvinceId(), getCityId(), getManufactureId(),
				getTerminalModelNo(), getTerminalId(), getPlateColor(),
				getPlateNo()));
		return sBuilder.toString();
	}

}