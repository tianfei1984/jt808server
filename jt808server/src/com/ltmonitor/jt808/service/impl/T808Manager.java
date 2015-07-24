package com.ltmonitor.jt808.service.impl;

import java.util.Collection;
import java.util.Date;
import org.apache.log4j.Logger;
import com.ltmonitor.app.GlobalConfig;
import com.ltmonitor.app.GpsConnection;
import com.ltmonitor.app.ServiceLauncher;
import com.ltmonitor.entity.PlatformState;
import com.ltmonitor.entity.TerminalCommand;
import com.ltmonitor.jt808.protocol.T808Message;
import com.ltmonitor.jt808.service.IAckService;
import com.ltmonitor.jt808.service.ICommandHandler;
import com.ltmonitor.jt808.service.ICommandService;
import com.ltmonitor.jt808.service.IJT808Server;
import com.ltmonitor.jt808.service.IMediaService;
import com.ltmonitor.jt808.service.IMessageSender;
import com.ltmonitor.jt808.service.IT808Manager;
import com.ltmonitor.jt808.service.ITransferGpsService;
import com.ltmonitor.server.mina.IJT808TransferService;
import com.ltmonitor.server.mina.JT808TransferQueue;


/**
 * 提供外部调用808转发平台的接口
 * 启动服务StartServer方法
 * @author tianfei
 * 
 */
public class T808Manager implements IT808Manager {

	private static Logger logger = Logger.getLogger(T808Manager.class);
	/**
	 * gps服务器的监听端口
	 */
	private int listenPort;
	/**
	 * 是否启动转发
	 */
	private boolean enable808Tranasfer = false;
	/**
	 * 转发给第三方的808服务器的端口
	 */
	private int third808ServerPort = 0;
	/**
	 * 转发给第三方的808服务器ip地址
	 */
	private String third808ServerIp;
	
	private IJT808Server jt808Server;
	
	//private static ConcurrentLinkedQueue<T808Message> dataQueue = new ConcurrentLinkedQueue();

	/**
	 *  流水号计数器，每次下发自动增加1
	 */
	private static int serialNo = 0;
	/**
	 * 用户给终端下发指令的处理服务
	 */
	private ICommandService commandService;

	//private Thread processRealDataThread;
	/**
	 * 应答服务
	 */
	private IAckService ackService;
	/**
	 * 多媒体上传数据的处理服务
	 */
	private IMediaService mediaService;
	
	/**
	 *  数据转发服务,主要用于809转发
	 */
	private ITransferGpsService transferGpsService;
	/**
	 * 基于808协议的转发客户端
	 */
	private IJT808TransferService jt808TransferService;

	
	public static int getSerialNo() {
		return serialNo++;
	}

	public Collection<GpsConnection> getGpsConnections() {
		return getJt808Server().getGpsConnections();
	}

	// 向上级平台发送数据
	private  boolean send(String simNo, byte[] msg) {
		// 优先使用主链路发送数据
		boolean res = getJt808Server().send(simNo, msg);
		return res;
	}

	// 向发送数据
	public boolean Send(T808Message msg) {
		msg.getHeader().setMessageSerialNo((short) getSerialNo());
		// msg.setPacketDescr(strMsg);
		boolean res = send(msg.getSimNo(), msg.WriteToBytes());
		GlobalConfig.putMsg(msg);
		return res;
	}
	
	/**
	 * 启动JT808服务器
	 */
	public boolean StartServer() {
		// 启动服务器
		boolean res = getJt808Server().start();
		if (res) {
			JT808TransferQueue.transferEnabled = enable808Tranasfer;
			//启动第三方808转发服务
			if(this.enable808Tranasfer)
			{
				//开始转发
				JT808TransferQueue.port = (this.third808ServerPort);
				JT808TransferQueue.serverIp = (this.third808ServerIp);
			    this.jt808TransferService.startTransfer();
			}
			// 启动命令解析器，从数据库中读取命令进行解析
			//getCommandService().setBaseDao(ServiceLauncher.getBaseDao());
			//更新平台状态
			try {
				PlatformState ps = ServiceLauncher.getBaseService().getPlatformState();
				ps.setGpsServerDate(new Date());
				ps.setGpsServerState(PlatformState.STATE_START);
				ServiceLauncher.getBaseService().saveOrUpdate(ps);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			//启动终端下发命令服务
			getCommandService().Start();
		    this.commandService.setOnRecvCommand(new ICommandHandler(){
		    	/**
		    	 * 向终端下发命令
		    	 */
				@Override
				public boolean OnRecvCommand(T808Message tm, TerminalCommand tc) {
					if(getJt808Server().isOnline(tc.getSimNo()) == false)
					{
						tc.setStatus(TerminalCommand.STATUS_OFFLINE);
						return false;
					}else
					{
						boolean res = Send(tm);
						tc.setSN(tm.getHeader().getMessageSerialNo());
						tc.setStatus(res ? TerminalCommand.STATUS_PROCESSING : 
							TerminalCommand.STATUS_FAILED);
						return res;
					}				
				}
		    });
			//应答服务，调用Server，发送应答数据包
			ackService.setMessageSender(new IMessageSender(){
				@Override
				public void Send808Message(T808Message tm) {
					Send(tm);					
				}				
			});
			//媒体服务
			mediaService.setMessageSender(new IMessageSender(){
				@Override
				public void Send808Message(T808Message tm) {
					Send(tm);					
				}				
			});
			
			//if(transferGpsService.sta)
			//启动809数据转发服务
			transferGpsService.start();
			//processRealDataThread.start();
		}
		return res;
	}
	


	/**
	 * 停止服务
	 */
	public void StopServer() {
		try {
			PlatformState ps = ServiceLauncher.getBaseService().getPlatformState();
			ps.setGpsServerDate(new Date());
			ps.setGpsServerState(PlatformState.STATE_STOP);
			ServiceLauncher.getBaseService().saveOrUpdate(ps);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		try {
			getJt808Server().Stop();
			transferGpsService.stop();
			if(jt808TransferService != null)
			   this.jt808TransferService.stop();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	
	public void setJt808Server(IJT808Server jt808Server) {
		this.jt808Server = jt808Server;
	}

	public IJT808Server getJt808Server() {
		return jt808Server;
	}

	public void setCommandService(ICommandService commandService) {
		this.commandService = commandService;
	}

	public ICommandService getCommandService() {
		return commandService;
	}

	public void setAckService(IAckService ackService) {
		this.ackService = ackService;
	}

	public IAckService getAckService() {
		return ackService;
	}

	public int getListenPort() {
		return listenPort;
	}

	public void setListenPort(int listenPort) {
		this.listenPort = listenPort;
	}

	public ITransferGpsService getTransferGpsService() {
		return transferGpsService;
	}

	public void setTransferGpsService(ITransferGpsService transferGpsService) {
		this.transferGpsService = transferGpsService;
	}

	public IMediaService getMediaService() {
		return mediaService;
	}

	public void setMediaService(IMediaService mediaService) {
		this.mediaService = mediaService;
	}

	public boolean isEnable808Tranasfer() {
		return enable808Tranasfer;
	}

	public void setEnable808Tranasfer(boolean enable808Tranasfer) {
		this.enable808Tranasfer = enable808Tranasfer;
	}

	public int getThird808ServerPort() {
		return third808ServerPort;
	}

	public void setThird808ServerPort(int third808ServerPort) {
		this.third808ServerPort = third808ServerPort;
	}

	public String getThird808ServerIp() {
		return third808ServerIp;
	}

	public void setThird808ServerIp(String third808ServerIp) {
		this.third808ServerIp = third808ServerIp;
	}

	public IJT808TransferService getJt808TransferService() {
		return jt808TransferService;
	}

	public void setJt808TransferService(IJT808TransferService jt808TransferService) {
		this.jt808TransferService = jt808TransferService;
	}

}
