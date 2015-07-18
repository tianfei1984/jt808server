package com.ltmonitor.app;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.ltmonitor.jt808.service.IT808Manager;

/**
 * 后台启动JT808服务
 * @author tianfei
 *
 */
public class JT808Server {
	
	static {
		String logConfig = System.getProperty("log.config","");
		if(!"".equals(logConfig)){
			PropertyConfigurator.configure(logConfig);
		}
	}
	
	private static Logger logger = Logger.getLogger(JT808Server.class);
	
	private final static int listenPort = 7611;
	
	static {
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("启动JT808服务...");
		//加载配置文件 
		ServiceLauncher.launch();
		//启动JT808服务
		IT808Manager t808Manager = (IT808Manager) ServiceLauncher.getBean("t808Manager");
		t808Manager = (IT808Manager) ServiceLauncher.getBean("t808Manager");
		t808Manager.setListenPort(listenPort);
		GlobalConfig.paramModel.setLocalPort(listenPort);
		t808Manager.StartServer();
	}
}
