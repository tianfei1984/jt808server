package com.ltmonitor.server.mina;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.ltmonitor.jt808.service.IJT808Server;

/**
 * 基于mina的808转发服务
 * 
 * @author DELL
 * 
 */
public class JT808TransferService implements IJT808TransferService {
	private int port = 8899;

	private String serverIp;

	private ConnectFuture cf;
	//808下行转发使能
	private boolean enable808DownTranasfer = false;

	private NioSocketConnector connector;
	
	private IoSession session;

	private Thread upThread;

	private Thread downThread;

	private static Logger logger = Logger.getLogger(JT808TransferService.class);

	private IJT808Server jt808Server;

	private boolean isStop = false;

	public JT808TransferService() {

	}

	
	public boolean isConnected()
	{
		return session != null && session.isConnected();
	}
	
	/* (non-Javadoc)
	 * @see com.ltmonitor.server.mina.IJT808TransferService#startTransfer()
	 */
	public void startTransfer()
	{
		isStop = false;
		JT808TransferQueue.enable808DownTranasfer = enable808DownTranasfer;
		//启动发送消息的线程
		if (downThread == null) {
			downThread = new Thread(new Runnable() {
				public void run() {
					downMessageThreadFunc();
				}
			});
			downThread.start();
		}
		
		if (upThread == null) {
			upThread = new Thread(new Runnable() {
				public void run() {
					upMessageThreadFunc();
				}
			});
			upThread.start();
		}
	}
	

	private void upMessageThreadFunc() {
		while (isStop == false) {
			
			try {
				Collection<MinaClient> mcList = JT808TransferQueue.getClientList();
				
				for(MinaClient mc :mcList)
				{
					byte[] msg = mc.upQueue.poll();
					while(msg != null)
					{
						mc.send(msg);
						msg = mc.downQueue.poll();
					}
				}
				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e1) {
			}
		}
	}


	private void downMessageThreadFunc() {
		while (isStop == false) {
			
			try {
				Collection<MinaClient> mcList = JT808TransferQueue.getClientList();
				
				for(MinaClient mc :mcList)
				{
					byte[] msg = mc.downQueue.poll();
					while(msg != null)
					{
						jt808Server.send(mc.getSimNo(), msg);
						msg = mc.downQueue.poll();
					}
				}
				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e1) {
			}
		}
	}

	public boolean sendData(byte[] messageBytes) {
		if (session != null && session.isConnected()) {
			WriteFuture wf = session.write(messageBytes);
			wf.awaitUninterruptibly(1000);
			if (wf.isWritten())
				return true;
			else {
				Throwable tr = wf.getException();
				if (tr != null) {
					logger.error(tr.getMessage(), tr);
				}

				return false;
			}
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see com.ltmonitor.server.mina.IJT808TransferService#stop()
	 */
	public void stop() {
		isStop = true; //关闭线程运作开关
		try {
			if (connector != null && session != null) {
				session.getCloseFuture().join();
				connector.dispose();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		if (downThread != null) {
			try {
				downThread.join();
			} catch (InterruptedException e) {
			}
			downThread = null;
		}
		if (upThread != null) {
			try {
				upThread.join();
			} catch (InterruptedException e) {
			}
			upThread = null;
		}
		Collection<MinaClient> mcList = JT808TransferQueue.getClientList();
		
		for(MinaClient mc :mcList)
		{
			mc.stop();
		}
		
	}

	public static void main(String[] args) {

	}


	public IJT808Server getJt808Server() {
		return jt808Server;
	}

	/* (non-Javadoc)
	 * @see com.ltmonitor.server.mina.IJT808TransferService#setJt808Server(com.ltmonitor.jt808.service.IJT808Server)
	 */
	public void setJt808Server(IJT808Server jt808Server) {
		this.jt808Server = jt808Server;
	}


	public boolean isEnable808DownTranasfer() {
		return enable808DownTranasfer;
	}


	public void setEnable808DownTranasfer(boolean enable808DownTranasfer) {
		this.enable808DownTranasfer = enable808DownTranasfer;
	}

}