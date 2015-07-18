package com.ltmonitor.server.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class MinaClientHandler extends IoHandlerAdapter{
	
	  private String simNo;
	  
	  public MinaClientHandler()
	  {
		  
	  }
	  
	  public MinaClientHandler(String _simNo)
	  {
		  simNo = _simNo;
	  }
	  
	  
	  
	  public void sessionOpened(IoSession session) throws Exception {
	    }
	  
	  @Override
	  public void messageReceived(IoSession session, Object message)
	      throws Exception {
		  byte[] dataRecved = (byte[])message;
		  JT808TransferQueue.down(simNo, dataRecved);
	  }
	  
	  @Override
	  public void sessionClosed(IoSession session) throws Exception {
	    session.close();
	  }

	public String getSimNo() {
		return simNo;
	}

	public void setSimNo(String simNo) {
		this.simNo = simNo;
	}
	}
