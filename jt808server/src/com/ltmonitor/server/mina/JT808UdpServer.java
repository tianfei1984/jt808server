package com.ltmonitor.server.mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import com.ltmonitor.app.GlobalConfig;
import com.ltmonitor.app.GpsConnection;
import com.ltmonitor.jt808.service.IJT808Server;
/**
 * 基于udp的808服务器
 * @author DELL
 *
 */
public class JT808UdpServer implements IJT808Server {
	private static Logger logger = Logger.getLogger(JT808UdpServer.class);

	private int port;

	private NioDatagramAcceptor dataAcceptor;
	
	private Executor threadPool = Executors.newCachedThreadPool();

	private JT808ServerHandler jt808Handler;
	public JT808UdpServer()
	{

	}
	@Override
	public boolean start() {
		try {
			//从全局配置中，获取端口
			port = GlobalConfig.paramModel.getLocalPort();
			dataAcceptor = new NioDatagramAcceptor();
			DefaultIoFilterChainBuilder chain = dataAcceptor.getFilterChain();
			// chain.addLast("logger", new LoggingFilter());
			// chain.addLast("codec", new ProtocolCodecFilter(new
			// TextLineCodecFactory(Charset.forName("UTF-8"))));
			chain.addLast("threadPool", new ExecutorFilter(threadPool));
			dataAcceptor.setHandler(jt808Handler);
			//808协议解码器
			dataAcceptor.getFilterChain().addLast("codec",
					new ProtocolCodecFilter(new JT808MessageCodecFactory()));
			DatagramSessionConfig dcfg = dataAcceptor.getSessionConfig();
			dcfg.setReadBufferSize(4096);// 设置接收最大字节默认2048
			dcfg.setMaxReadBufferSize(65536);
			dcfg.setReceiveBufferSize(1024);// 设置输入缓冲区的大小
			dcfg.setSendBufferSize(1024);// 设置输出缓冲区的大小
			dcfg.setReuseAddress(true);// 设置每一个非主监听连接的端口可以重用

			dataAcceptor.bind(new InetSocketAddress(port));
			logger.info("UDP服务器启动成功!端口号:" + port);
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public void Stop() {
		if (null != dataAcceptor) {
			dataAcceptor.unbind();
			dataAcceptor.getFilterChain().clear(); // 清空Filter
													// chain，防止下次重新启动时出现重名错误
			dataAcceptor.dispose(); // 可以另写一个类存储IoAccept，通过spring来创建，这样调用dispose后也会重新创建一个新的。或者可以在init方法内部进行创建。
			dataAcceptor = null;
		}

	}
	/**
	 * 获得当前的连接列表
	 */
	public Collection<GpsConnection> getGpsConnections()
	{
		return getJt808Handler().getConnections();
	}
	
	@Override
	public boolean isOnline(String simNo)
	{
		if(simNo == null || simNo.length() == 0)
			return false;
		GpsConnection conn = this.getJt808Handler().getConnection(simNo);
		if(conn != null)
		{
			IoSession session = getSession(conn.getSessionId());
			return session != null && session.isConnected() ;
		}
		return false;
	}

	/**
	 * 向终端下发命令数据
	 */
	public boolean send(String simNo, byte[] msg)
	{
		GpsConnection conn = this.getJt808Handler().getConnection(simNo);
		if(conn != null)
			return send(conn.getSessionId(), msg);
		return false;
	}
	
	public  boolean send(long sessionId, byte[] msg) {
		try {
			IoSession session = getSession(sessionId);
			if (session != null && session.isConnected()) {
				WriteFuture wf = session.write(msg);
				wf.awaitUninterruptibly(1000);
				if(wf.isWritten())
					return true;
				else
				{
					Throwable tr = wf.getException();
					if(tr != null)
					{
						logger.error(tr.getMessage(), tr);
					}
						
					return false;
				}
					
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return false;
	}
	

	
	public IoSession getSession(long sid)
	{
		return dataAcceptor.getManagedSessions().get(sid);
	}

	public JT808ServerHandler getJt808Handler() {
		return jt808Handler;
	}

	public void setJt808Handler(JT808ServerHandler jt808Handler) {
		this.jt808Handler = jt808Handler;
	}

}
