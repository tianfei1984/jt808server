package com.ltmonitor.jt808.service;

import com.ltmonitor.jt808.protocol.T808Message;

public interface IAckService {
	
	public abstract void beginAck(T808Message tm);
	
	public abstract void setMessageSender(IMessageSender handler);

}
