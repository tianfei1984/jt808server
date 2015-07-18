package com.ltmonitor.server.mina;

import com.ltmonitor.jt808.service.IJT808Server;

public interface IJT808TransferService {

	public void startTransfer();

	public void stop();

	public void setJt808Server(IJT808Server jt808Server);

}