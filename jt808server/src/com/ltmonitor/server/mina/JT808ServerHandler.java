package com.ltmonitor.server.mina;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.transport.socket.SocketSessionConfig;
import com.ltmonitor.app.GpsConnection;
import com.ltmonitor.entity.StringUtil;
import com.ltmonitor.jt808.protocol.T808Message;
import com.ltmonitor.jt808.service.IMessageProcessService;
import com.ltmonitor.service.IRealDataService;

/**
 * JT808O业务处理器
 * @author tianfei
 *
 */
public class JT808ServerHandler extends IoHandlerAdapter {
	private Logger logger = Logger.getLogger(JT808ServerHandler.class);

	private IMessageProcessService messageProcessService;
	//终端连接集合
	private static ConcurrentMap<String, GpsConnection> connctionMap = new ConcurrentHashMap<String, GpsConnection>();
	
	private IRealDataService realDataService;

	public Collection<GpsConnection> getConnections() {
		return connctionMap.values();
	}

	public void exceptionCaught(IoSession session, Throwable e)
			throws Exception {
		//this.logger.error(getSimNo(session) + "通讯时发生异常：" + e.getMessage(), e);
		this.logger.error(getSimNo(session) + "通讯时发生异常：" + e.getMessage());
	}

	private String getSimNo(IoSession session) {
		return "" + session.getAttribute("simNo");
	}

	public GpsConnection getConnection(String simNo) {
		if (simNo.length() > 11)
			simNo = simNo.substring(1);
		GpsConnection conn = connctionMap.get(simNo);
		return conn;
	}

	private GpsConnection getConnection(long sessionId, T808Message msg) {
		if (msg == null || msg.getSimNo() == null) {
			logger.error("错误的空消息:");
			return null;
		}
		GpsConnection conn = connctionMap.get(msg.getSimNo());
		if (conn == null) {
			conn = new GpsConnection(msg.getSimNo(), sessionId);
			connctionMap.put(msg.getSimNo(), conn);
		} else if (conn.getSessionId() != sessionId) {
			// 新的连接
			//logger.error(msg.getSimNo() + "接入新的连接");
			conn.setSessionId(sessionId);
		}
		conn.setOnlineDate(new Date());
		conn.setSessionId(sessionId);

		return conn;
	}

	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// tm.platform.server.LocalServer.session = session;
		T808Message tm = (T808Message) message;

		// T808Manager.putMsg(tm);
		session.setAttribute("simNo", tm.getSimNo());
		GpsConnection conn = getConnection(session.getId(), tm);
		if (conn != null) {
			conn.setConnected(true);
			//消息处理
			messageProcessService.processMsg(tm);
			conn.setPlateNo(tm.getPlateNo()); // 设置连接的车牌号
			if(conn.getPackageNum() == Integer.MAX_VALUE)
			{
				conn.setPackageNum(0);
				conn.setErrorPacketNum(0);
				conn.setErrorPacketNum(0);
			}

			conn.setPackageNum(conn.getPackageNum() + 1);

			if (tm.getErrorMessage() != null) {
				// 收到错误解析的包
				conn.setErrorPacketNum(conn.getErrorPacketNum() + 1);
			}
			if (tm.getHeader() != null
					&& tm.getHeader().getMessageType() == 0x0200) {
				conn.setPositionPackageNum(conn.getPositionPackageNum() + 1);
			}
		}

	}

	public void messageSent(IoSession session, Object message) throws Exception {
		this.logger.info("SimNo:" + session.getAttribute("simNo") + "下发命令发送成功!");
	}

	public void sessionClosed(IoSession session) throws Exception {

		String simNo = "" + session.getAttribute("simNo");
		if (StringUtil.isNullOrEmpty(simNo) == false) {
			GpsConnection conn = connctionMap.get(simNo);
			if (conn != null) {
				// connctionMap.remove(simNo);
				conn.setConnected(false);
				conn.setDisconnectTimes(conn.getDisconnectTimes() + 1);
			}
		}
		session.close(true);
		this.logger.info("与本地服务器断开连接, SimNo:" + simNo);
	}

	public void sessionCreated(IoSession session) throws Exception {
		// 当网络连接被创建时此方法被调用（这个肯定在sessionOpened(IoSession
		// session)方法之前被调用），这里可以对Socket设置一些网络参数
		IoSessionConfig cfg1 = session.getConfig();
		if (cfg1 instanceof SocketSessionConfig) {
			SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();
			// ((SocketSessionConfig) cfg).setReceiveBufferSize(4096);
			cfg.setReceiveBufferSize(2 * 1024 * 1024);
			cfg.setReadBufferSize(2 * 1024 * 1024);
			cfg.setKeepAlive(true);
			// if (session.== TransportType.SOCKET) {
			// ((SocketSessionConfig) cfg).setKeepAlive(true);
			((SocketSessionConfig) cfg).setSoLinger(0);
			((SocketSessionConfig) cfg).setTcpNoDelay(true);
			((SocketSessionConfig) cfg).setWriteTimeout(1000);
		}

	}

	public void sessionIdle(IoSession session, IdleStatus idle)
			throws Exception {
		String simNo = getSimNo(session);
		this.logger.info(simNo + "空闲时间过长，系统将关闭连接");
		session.close(true);
	}

	public void sessionOpened(IoSession session) throws Exception {
	}

	public void setMessageProcessService(
			IMessageProcessService messageProcessService) {
		this.messageProcessService = messageProcessService;
	}

	public IMessageProcessService getMessageProcessService() {
		return messageProcessService;
	}

	public IRealDataService getRealDataService() {
		return realDataService;
	}

	public void setRealDataService(IRealDataService realDataService) {
		this.realDataService = realDataService;
	}
}
