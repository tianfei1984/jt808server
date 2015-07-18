package com.ltmonitor.jt808.service;

import com.ltmonitor.jt808.protocol.T808Message;

public interface IMessageSender {
	
	public void Send808Message(T808Message tm);

}
