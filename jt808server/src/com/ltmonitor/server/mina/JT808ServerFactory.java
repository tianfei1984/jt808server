package com.ltmonitor.server.mina;

import com.ltmonitor.jt808.service.IJT808Server;

public class JT808ServerFactory {
	public static String TANSPORT_BY_TCP = "tcp";
	private String transport;
	
	private JT808TcpServer jt808TcpServer;
	
	private JT808UdpServer jt808UdpServer;
	
	
	public IJT808Server getJt808Server()
	{
		if(TANSPORT_BY_TCP.equals(transport))
		{
			return jt808TcpServer;
		}
		return jt808UdpServer;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public JT808TcpServer getJt808TcpServer() {
		return jt808TcpServer;
	}

	public void setJt808TcpServer(JT808TcpServer jt808TcpServer) {
		this.jt808TcpServer = jt808TcpServer;
	}

	public JT808UdpServer getJt808UdpServer() {
		return jt808UdpServer;
	}

	public void setJt808UdpServer(JT808UdpServer jt808UdpServer) {
		this.jt808UdpServer = jt808UdpServer;
	}

}
