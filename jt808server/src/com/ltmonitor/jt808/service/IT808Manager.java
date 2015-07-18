package com.ltmonitor.jt808.service;

import java.util.Collection;
import com.ltmonitor.app.GpsConnection;
import com.ltmonitor.jt808.protocol.T808Message;

/**
 * JT808外部接口
 * @author tianfei
 *
 */
public interface IT808Manager {

	/**
	 * 启动808Server监听
	 */
	public abstract boolean StartServer();


	boolean Send(T808Message msg);

	/**
	 * 停止服务
	 */
	public void StopServer();

	/**
	 * 
	 * @return
	 */
	public Collection<GpsConnection> getGpsConnections();


	int getListenPort();


	void setListenPort(int listenPort);

	//public void processMsg(T808Message msgFromTerminal);

}