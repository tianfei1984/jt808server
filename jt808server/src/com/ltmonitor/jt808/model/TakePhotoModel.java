package com.ltmonitor.jt808.model;

import java.util.Date;


//拍照请求和应答数据类
public class TakePhotoModel {
	// 唯一ID
	private int reqId;

	private String vehicle_no;
	private int vehicle_color;
    //镜头ID
	private int lensId;

	//创建时间
	private Date createDate;
	//应答时间
	private Date ReplayDate;

	//请求处理状态, 0 新建 ， 1处理中 2 终端已应答 3 平台已应答  4处理失败 
	private int status;
    //应答结果
	private int replayResult;

	//照片大小
	private int photoSize;

	//大小类型
	private int photoSizeType;

	//照片格式
	private int photoFormat;

	//照片文件路径
	private String filePath;

	//拍照时的定位数据
	private GnssData gnssData;
	
	public TakePhotoModel()
	{
		vehicle_no = ("苏A53251");
		vehicle_color = 1;
		lensId = 1;
		this.replayResult = 1;
		
		filePath = ("testphoto.jpg");
		this.photoSizeType = 1;
		this.photoFormat = 1;
		
		gnssData = new GnssData();
		
	}

	public void setReqId(int reqId) {
		this.reqId = reqId;
	}

	public int getReqId() {
		return reqId;
	}

	public void setVehicle_no(String vehicle_no) {
		this.vehicle_no = vehicle_no;
	}

	public String getVehicle_no() {
		return vehicle_no;
	}

	public void setVehicle_color(int vehicle_color) {
		this.vehicle_color = vehicle_color;
	}

	public int getVehicle_color() {
		return vehicle_color;
	}

	public void setLensId(int lensId) {
		this.lensId = lensId;
	}

	public int getLensId() {
		return lensId;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setReplayDate(Date replayDate) {
		ReplayDate = replayDate;
	}

	public Date getReplayDate() {
		return ReplayDate;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setReplayResult(int replayResult) {
		this.replayResult = replayResult;
	}

	public int getReplayResult() {
		return replayResult;
	}

	public void setPhotoSize(int photoSize) {
		this.photoSize = photoSize;
	}

	public int getPhotoSize() {
		return photoSize;
	}

	public void setPhotoSizeType(int photoSizeType) {
		this.photoSizeType = photoSizeType;
	}

	public int getPhotoSizeType() {
		return photoSizeType;
	}

	public void setPhotoFormat(int photoFormat) {
		this.photoFormat = photoFormat;
	}

	public int getPhotoFormat() {
		return photoFormat;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}

	public GnssData getGnssData() {
		return gnssData;
	}

	public void setGnssData(GnssData gnssData) {
		this.gnssData = gnssData;
	}


}
