package com.ltmonitor.jt808.service;

import com.ltmonitor.entity.TerminalCommand;
import com.ltmonitor.jt808.protocol.T808Message;

/**
 * JT808ÃüÁî´¦ÀíÆ÷
 * @author tianfei
 *
 */
public interface ICommandHandler {
	boolean OnRecvCommand(T808Message tm, TerminalCommand tc);

}