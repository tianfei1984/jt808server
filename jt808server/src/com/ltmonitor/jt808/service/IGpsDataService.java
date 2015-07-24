package com.ltmonitor.jt808.service;

import com.ltmonitor.jt808.protocol.T808Message;


public interface IGpsDataService
{
	void ProcessMessage(T808Message message);

	//重置GPS上线状态
	void resetGpsOnlineState();

}