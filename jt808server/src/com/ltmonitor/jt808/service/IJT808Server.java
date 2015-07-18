package com.ltmonitor.jt808.service;

import java.util.Collection;

import org.apache.mina.core.session.IoSession;

import com.ltmonitor.app.GpsConnection;


public interface IJT808Server {

	public abstract void Stop();

	public abstract Collection<GpsConnection> getGpsConnections();

	public abstract boolean send(String simNo, byte[] msg);

	public abstract boolean send(long sessionId, byte[] msg);

	public abstract IoSession getSession(long sid);

	public abstract boolean start();

	public abstract boolean isOnline(String simNo);

}