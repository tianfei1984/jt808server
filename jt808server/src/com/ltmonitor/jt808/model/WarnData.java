package com.ltmonitor.jt808.model;

import java.util.Date;

/**
 * 上传的报警数据 和下发的报警督办消息 共用 
 * @author DELL
 *
 */
public class WarnData {
	//车牌号
	private String plateNo;
	private int plateColor;
	//报警信息来源
	private int src;
	//报警类型
	private int type;
	//报警时间
	private Date warnTime;
	//报警督办Id
	private long infoId;
	//报警内容
	private String content;
	//报警截至时间
	private Date supervisionEndTime;
	//督办级别 0 紧急 1 一般
	private byte supervisionLevel;
	//报警督办人
	private String supervisor;
	//督办人电话
	private String supervisionTel;
	//督办人邮箱
	private String supervisionEmail;
	//报警处理结果
	private int result;
	public int getSrc() {
		return src;
	}
	public void setSrc(int src) {
		this.src = src;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getWarnTime() {
		return warnTime;
	}
	public void setWarnTime(Date warnTime) {
		this.warnTime = warnTime;
	}
	public long getInfoId() {
		return infoId;
	}
	public void setInfoId(long infoId) {
		this.infoId = infoId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getPlateColor() {
		return plateColor;
	}
	public void setPlateColor(int plateColor) {
		this.plateColor = plateColor;
	}
	public Date getSupervisionEndTime() {
		return supervisionEndTime;
	}
	public void setSupervisionEndTime(Date supervisionEndTime) {
		this.supervisionEndTime = supervisionEndTime;
	}
	public byte getSupervisionLevel() {
		return supervisionLevel;
	}
	public void setSupervisionLevel(byte supervisionLevel) {
		this.supervisionLevel = supervisionLevel;
	}
	public String getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
	public String getSupervisionTel() {
		return supervisionTel;
	}
	public void setSupervisionTel(String supervisionTel) {
		this.supervisionTel = supervisionTel;
	}
	public String getSupervisionEmail() {
		return supervisionEmail;
	}
	public void setSupervisionEmail(String supervisionEmail) {
		this.supervisionEmail = supervisionEmail;
	}
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getResult() {
		return result;
	}

}
