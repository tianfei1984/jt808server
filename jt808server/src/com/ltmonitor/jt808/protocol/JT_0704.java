package com.ltmonitor.jt808.protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * 定位信息补发
 */
public class JT_0704 implements IMessageBody {
	/**
	 * 定位包数量
	 */
	private short positionPacketNum;
	/**
	 * 汇报数据类型，0 正常批量位置汇报 1 盲区补报
	 */
	private byte reportType;

	/**
	 * 补发的定位包
	 */
	private List<JT_0200> positionReportList;

	public final byte[] WriteToBytes() {

		MyBuffer buff = new MyBuffer();

		// buff.put(getResponseMessageSerialNo());
		// buff.put(getPositionReport().WriteToBytes());
		return buff.array();

	}

	public final void ReadFromBytes(byte[] bytes) {
		MyBuffer buff = new MyBuffer(bytes);
		this.positionPacketNum = (buff.getShort());
		this.reportType = buff.get();
		positionReportList = new ArrayList<JT_0200>();
		for (int m = 0; m < this.positionPacketNum; m++) {
			JT_0200 jt = new JT_0200();
			int dataLength = buff.getShort();
			jt.ReadFromBytes(buff.gets(dataLength));
			positionReportList.add(jt);
		}

	}

	@Override
	public String toString() {
		//String str
		return String.format("补发类型:%1$s,包数：%2$s", this.reportType,
				this.positionPacketNum);
	}

	public List<JT_0200> getPositionReportList() {
		return positionReportList;
	}

	public void setPositionReportList(List<JT_0200> positionReportLIst) {
		this.positionReportList = positionReportLIst;
	}

	public short getPositionPacketNum() {
		return positionPacketNum;
	}

	public void setPositionPacketNum(short positionPacketNum) {
		this.positionPacketNum = positionPacketNum;
	}

	public byte getReportType() {
		return reportType;
	}

	public void setReportType(byte reportType) {
		this.reportType = reportType;
	}
}