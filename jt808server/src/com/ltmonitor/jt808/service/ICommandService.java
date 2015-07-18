package com.ltmonitor.jt808.service;

import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.entity.TerminalCommand;
import com.ltmonitor.jt808.protocol.T808Message;


public interface ICommandService {

	public abstract IBaseDao getBaseDao();

	public abstract void setBaseDao(IBaseDao value);

	public abstract ICommandHandler getOnRecvCommand();

	public abstract void setOnRecvCommand(ICommandHandler value);

	public abstract int getInterval();

	public abstract void setInterval(int value);

	// 启动命令解析线程，自动解析命令，并发送给终端
	public abstract void Start();

	public abstract void Stop();

	public abstract void ParseCommand();

	// 根据消息的流水号来更新状态
	public abstract TerminalCommand UpdateStatus(String GpsId, int SN,
			String status);

	public abstract void UpdateCommand(TerminalCommand tc);

	public abstract TerminalCommand getCommandBySn(int sn);

	/**
	 * 最近下发的某个命令
	 * @param cmdType
	 * @return
	 */
	TerminalCommand getLatestCommand(int cmdType, String simNo);
	/**
	 * 事件上报时，根据Id得到事件内容
	 * @param eventId
	 * @return
	 */
	String getEventContent(int eventId);

	// 不对非法命令格式进行解析，在命令录入时确保格式正确
	//public abstract T808Message Parse(TerminalCommand tc);

}