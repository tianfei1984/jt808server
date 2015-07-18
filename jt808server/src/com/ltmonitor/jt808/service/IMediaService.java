package com.ltmonitor.jt808.service;

import com.ltmonitor.jt808.protocol.T808Message;

public interface IMediaService {

	/**
	 * 停止服务
	 */
	public void Stop();

	/**
	 * 处理多媒体数据包
	 * 
	 * @param msg
	 */
	public void processMediaMsg(T808Message msg);

	public void setMessageSender(IMessageSender messageSender);

	public void setTransferGpsService(ITransferGpsService transferGpsService);

}