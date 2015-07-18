package com.ltmonitor.server.mina;

import java.net.SocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class UdpServerHandler extends IoHandlerAdapter {  
	  
   
  
    @Override  
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {  
        cause.printStackTrace();  
        session.close(true);  
    }  
  
    @Override  
    public void messageReceived(IoSession session, Object message) throws Exception {  
  
        String expression = message.toString();  
        System.out.println("[" + expression + "]");  
  
    }  
  
    @Override  
    public void sessionClosed(IoSession session) throws Exception {  
        System.out.println("Session closed...");  
  
    }  
  
    @Override  
    public void sessionCreated(IoSession session) throws Exception {  
  
        System.out.println("Session created...");  
  
        SocketAddress remoteAddress = session.getRemoteAddress();  
        System.out.println(remoteAddress);  
    }  
  
    @Override  
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {  
        System.out.println("Session idle...");  
    }  
  
    @Override  
    public void sessionOpened(IoSession session) throws Exception {  
        System.out.println("Session Opened...");  
        SocketAddress remoteAddress = session.getRemoteAddress();  
        System.out.println(remoteAddress);  
    }  
}  
