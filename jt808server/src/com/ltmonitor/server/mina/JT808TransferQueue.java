package com.ltmonitor.server.mina;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class JT808TransferQueue {

	public static boolean transferEnabled = false;
	
	public static boolean enable808DownTranasfer = false;

	public static boolean connected = false;
	
	public static String serverIp;
	
	public static int port;

	public static ConcurrentHashMap<String,MinaClient> clientPool = new ConcurrentHashMap();

	public static void forward(String simNo,byte[] messageBytes) {
		if (transferEnabled)
		{
			MinaClient mc = null;
			if(clientPool.containsKey(simNo) == false)
			{
				mc = new MinaClient(simNo, serverIp, port );
				clientPool.put(simNo, mc);
			}else
				mc = clientPool.get(simNo);
			
			mc.addUpQueue(messageBytes);
		}
	}
	
	
	public static void down(String simNo,byte[] messageBytes) {
		if (transferEnabled && enable808DownTranasfer)
		{
			MinaClient mc = getClient(simNo);
			if(mc != null)
			{
				mc.addDownQueue(messageBytes);
			}
		}
	}
	
	public static MinaClient getClient(String simNo)
	{
		return clientPool.get(simNo);
	}
	
	public static Collection<MinaClient> getClientList()
	{
	    Collection<MinaClient> ls = clientPool.values();
	    
	    return ls;
	}
}
