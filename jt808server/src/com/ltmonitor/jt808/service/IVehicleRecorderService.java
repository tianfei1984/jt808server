package com.ltmonitor.jt808.service;

import com.ltmonitor.jt808.protocol.T808Message;

/**
 * 行车记录仪服务
 * @author tianfei
 *
 */
public interface IVehicleRecorderService {
	
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

}
