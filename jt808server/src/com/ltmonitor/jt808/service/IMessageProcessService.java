package com.ltmonitor.jt808.service;

import com.ltmonitor.jt808.protocol.T808Message;

public interface IMessageProcessService {

	/**
	 * 对终端发送上来的消息进行通用应答
	 * 
	 * @param msgFromTerminal  终端消息
	 */
	public abstract void processMsg(T808Message msgFromTerminal);

}