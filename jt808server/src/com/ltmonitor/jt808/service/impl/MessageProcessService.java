package com.ltmonitor.jt808.service.impl;

import org.apache.log4j.Logger;
import com.ltmonitor.app.GlobalConfig;
import com.ltmonitor.jt808.protocol.T808Message;
import com.ltmonitor.jt808.service.IAckService;
import com.ltmonitor.jt808.service.IGpsDataService;
import com.ltmonitor.jt808.service.IMessageProcessService;

/**
 * 终端上行数据处理服务
 * @author tianfei
 *
 */
public class MessageProcessService implements IMessageProcessService {

	private static Logger logger = Logger.getLogger(MessageProcessService.class);
	private IGpsDataService gpsDataService;

	private IAckService ackService;
	/* (non-Javadoc)
	 * @see tm.app.service.impl.IMessageProcessService#processMsg(tm.protocol.jt808.T808Message)
	 */
	public void processMsg(T808Message msgFromTerminal) {
		GlobalConfig.putMsg(msgFromTerminal);
		
		int msgType = msgFromTerminal.getMessageType();
		if (msgType == 0)
			return;
//		String simId = msgFromTerminal.getSimNo();
		try {
			// 消息入库
			gpsDataService.ProcessMessage(msgFromTerminal);
		} catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
		}
		//应答服务
		getAckService().beginAck(msgFromTerminal);
	}
	
	public void setAckService(IAckService ackService) {
		this.ackService = ackService;
	}
	public IAckService getAckService() {
		return ackService;
	}

	public void setGpsDataService(IGpsDataService gpsDataService) {
		this.gpsDataService = gpsDataService;
	}

	public IGpsDataService getGpsDataService() {
		return gpsDataService;
	}


}
