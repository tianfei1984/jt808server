package com.ltmonitor.jt808.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.dao.impl.DaoIbatisImpl;
import com.ltmonitor.entity.GPSRealData;
import com.ltmonitor.entity.GpsInfo;
import com.ltmonitor.entity.StringUtil;
import com.ltmonitor.entity.VehicleData;
import com.ltmonitor.jt808.service.IAlarmService;
import com.ltmonitor.service.ILocationService;
import com.ltmonitor.service.IRealDataService;
import com.ltmonitor.service.IVehicleService;
import com.ltmonitor.jt808.service.IOnlineRecordService;
import com.ltmonitor.util.DateUtil;

/**
 * 实时数据服务 1.提供一个大的缓存，保存实时数据在内存中; 2.启动线程，定时更新实时数据到数据库中，便于报表查询;
 * 3.提供RMI服务，web应用可以远程获取内存中的实时数据;
 * 
 * @author DELL
 * 
 */
public class RealDataService implements IRealDataService {

	private static Logger logger = Logger.getLogger(RealDataService.class);
	private IBaseDao baseDao;

	private IAlarmService alarmService;
	private IOnlineRecordService onlineRecordService;

	public ConcurrentMap<String, GPSRealData> realDataMap = new ConcurrentHashMap<String, GPSRealData>();
	/**
	 * 实时更新时间的数据
	 */
	public ConcurrentMap<String, Date> onlineMap = new ConcurrentHashMap<String, Date>();
	/**
	 *  实时数据更新时间 
	 */
	private HashMap<String, Date> updateMap = new HashMap<String, Date>();

	// 车辆数据缓存
	private ConcurrentMap<String, VehicleData> vehicleMap = new ConcurrentHashMap<String, VehicleData>();
	private IVehicleService vehicleService;

	private DaoIbatisImpl queryDao;
	/**
	 * 实时数据数据处理线程
	 */
	private Thread processRealDataThread;

	/**
	 * 位置查询线程
	 */
	private Thread locationThread;

	private ILocationService locationService;

	private double maxOfflineTime = 60 * 1; // 1分钟, 1分钟不更新数据包，视为离线

	// private Map<String, Date> onlineMap = new HashMap<String, Date>();

	private boolean startUpdate = true;
	/**
	 * 服务器各个连接的状态
	 */
	private ConcurrentMap<String, Boolean> connectedStateMap = new ConcurrentHashMap<String, Boolean>();
	
	/**
	 * 线程池，默认是50个
	 */
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

	// ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

	public RealDataService() {

	}

	public void start() {
		processRealDataThread = new Thread(new Runnable() {
			public void run() {
				ProcessRealDataThreadFunc();
			}
		});
		processRealDataThread.start();
		//逆地址解析
		locationThread = new Thread(new Runnable() {
			public void run() {
				locationThreadFunc();
			}
		});
		locationThread.start();

		String hql = "from GPSRealData where online = ? and simNo is not null";
		List ls = this.baseDao.query(hql, true);
		for (Object obj : ls) {
			GPSRealData rd = (GPSRealData) obj;
			// rd.setSendTime(new Date());
			//rd.setOnlineDate(new Date());
			this.realDataMap.put(rd.getSimNo(), rd);
			this.updateMap.put(rd.getSimNo(), rd.getSendTime());
			this.onlineMap.put(rd.getSimNo(), new Date());
		}
	}

	@Override
	public void stopService() {
		startUpdate = false;
		try {
			processRealDataThread.join(50000);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		try {
			locationThread.join(50000);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 批量更新实时库
	 * 
	 * @param realDataList
	 */
	public void batchUpdate(List realDataList) {
		this.queryDao.batchUpdate("updateRealData", realDataList);
	}

	/**
	 * 逆地址解析
	 */
	private void locationThreadFunc() {
		while (startUpdate) {
			try {
//				Date start = new Date();
				ArrayList<String> keys = null;
				// logger.error("获取Online");
				// synchronized (onlineMap) {
				keys = new ArrayList(onlineMap.keySet());
				// }
				// logger.error("获取Online结束");
				int size = 0;
				for (String simNo : keys) {
					// logger.error("获取Rd");
					GPSRealData rd = get(simNo);
					// 根据有效的坐标，解析出地址，更新实时数据
					// logger.error("获取location");
					Date lastupDate = updateMap.get(simNo);
					if(lastupDate != null && lastupDate.compareTo(rd.getSendTime())>=0){
						continue;
					}
					if (rd.getLatitude() > 0 && rd.getLongitude() > 0) {
						String location = locationService.getLocation(rd.getLatitude(), rd.getLongitude());
						if (StringUtil.isNullOrEmpty(location) == false) {
							rd.setLocation(location);
						}
						size++;
					}
					// logger.error("获取location结束");
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			// logger.error("位置线程正在运行");
			try {
				Thread.sleep(23 * 1000L);
			} catch (InterruptedException e1) {
				logger.error(e1.getMessage(), e1);
			}
		}
	}

	private void ProcessRealDataThreadFunc() {
		//
		while (startUpdate) {
			try {
				List<GPSRealData> result = new ArrayList<GPSRealData>();
				// Set<String> keys = onlineMap.keySet();
				ArrayList<String> keys = null;
				// synchronized (onlineMap) {
				keys = new ArrayList(onlineMap.keySet());
				// }
				for (String simNo : keys) {
					GPSRealData rd = get(simNo);
					boolean changed = isOnlineStateChanged(rd);
					Date lastUpdateDate = updateMap.get(simNo);
					if (changed == false && lastUpdateDate != null) {
						if (lastUpdateDate.compareTo(rd.getSendTime()) >= 0) {
							// 实时数据没有变化的不再更新
							continue;
						}
					}
					if (rd.getID() == 0) {
						this.saveRealData(rd);
					} else {
						rd.setUpdateDate(new Date());
						result.add(rd);
					}
					updateMap.put(simNo, rd.getSendTime());
				}
				/**
				 * 批量更新实时数据
				 */
				if (result.size() > 0) {
					Date start = new Date();
					batchUpdate(result);
					Date end = new Date();
					double seconds = DateUtil.getSeconds(start, end);
					logger.error("实时数据更新耗时:" + seconds + ",条数：" + result.size());

				}
				// logger.error("实时更新线程正在运行");
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e1) {
			}
		}
	}

	/**
	 * 更新车辆实时GPS信息
	 */
	public void update(GPSRealData rd) {
		rd.setOnlineDate(new Date());
		// rd.setOnline(true);
		onlineMap.put(rd.getSimNo(), rd.getSendTime());
		realDataMap.put(rd.getSimNo(), rd);
	}

	// 更新在线时间
	public void updateOnlineTime(GPSRealData r, Date onlineDate) {
		if (r != null && r.getOnlineDate().compareTo(onlineDate) < 0) {
			onlineMap.put(r.getSimNo(), onlineDate);
			r.setOnlineDate(onlineDate);
			// this.checkOnline(rd);
		}
	}
	

	public void UpdateConnectedState(String simNo, Boolean isConnected)
	{
		this.connectedStateMap.put(simNo, isConnected);
	}
	
	public Boolean isConnected(String simNo)
	{
		if(connectedStateMap.containsKey(simNo))
		{
			return connectedStateMap.get(simNo);
		}
		return false;
	}
	
	// 检测终端的上线状态,返回是是否变化
	private Boolean isOnlineStateChanged(GPSRealData rd) {
		// 离线以最新数据包的时间间隔为准，不是以实时数据的间隔为准
		boolean online = false;

		Date onlineTime = rd.getOnlineDate();
		/**
		if(this.isConnected(rd.getSimNo()) == false)
		{
			maxTime = 100;
		}*/
		if (onlineTime != null) {
			double ts = DateUtil.getSeconds(onlineTime, new Date());
			online = (ts < maxOfflineTime);
		}
		boolean changed = rd.getOnline() != online;
		if (changed) {
			//终端上线或超时退出
			rd.setOnline(online);
			rd.setSendTime(onlineTime);
			onlineRecordService.checkOnline(rd);
			
			/**
			 * logger.error(rd.getPlateNo() + (online ? "上线" : "下线") + "," +
			 * DateUtil.toStringByFormat(onlineTime, "yyyy-MM-dd HH:mm:ss"));
			 * rd.setSendTime(onlineTime);
			 */
		}
		return changed;
	}

	public List<GPSRealData> getRealDataList(List<String> simNoList) {
		List<GPSRealData> result = new ArrayList<GPSRealData>();
		for (String simNo : simNoList) {
			GPSRealData rd = get(simNo);
			if (rd != null)
				result.add(rd);
		}
		return result;
	}

	/**
	 * 获取当前在线的实时数据列表
	 * 
	 * @return
	 */
	public List<GPSRealData> getOnlineRealDataList() {
		List<GPSRealData> result = new ArrayList<GPSRealData>();
		Set<String> keys = onlineMap.keySet();
		for (String simNo : keys) {
			GPSRealData rd = get(simNo);
			result.add(rd);
		}
		return result;
	}

	public void saveRealData(GPSRealData rd) {
		try {
			getBaseDao().saveOrUpdate(rd);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public GPSRealData get(String simNo) {
		GPSRealData rd = null;
		if (realDataMap.containsKey(simNo)) {
			rd = realDataMap.get(simNo);
		} else {
			String hsql = "from GPSRealData gps where gps.simNo= ? ";
			rd = (GPSRealData) getBaseDao().find(hsql, simNo);
			if (rd == null) {
				// logger.error("没找到实时数据:" + simNo);

				VehicleData vd = this.getVehicleData(simNo);

				if (vd == null) {
					return null; // 该终端没有在后台录入，无法入库。
				}

				if (vd != null) {
					rd = new GPSRealData();
					rd.setSimNo(simNo);
					rd.setVehicleId(vd.getEntityId());
					rd.setPlateNo(vd.getPlateNo());
					rd.setDepId(vd.getDepId());
					try {
						// saveRealData(rd);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
			rd.setOnline(false);
			rd.setValid(false);
			if (StringUtil.isNullOrEmpty(rd.getLocation())
					&& rd.getLatitude() > 0 && rd.getLongitude() > 0) {

				// this.saveRealData(rd);
			}
			realDataMap.put(simNo, rd);
		}

		if (rd.getOnlineDate() == null)
			rd.setOnlineDate(rd.getSendTime());
		return rd;
	}

	private void setLocation(Object obj) {
		String simNo = "" + obj;
		GPSRealData rd = this.get(simNo);
		String location = locationService.getLocation(rd.getLatitude(),
				rd.getLongitude());
		if (StringUtil.isNullOrEmpty(location) == false) {
			rd.setLocation(location);
		}
	}

	// 得到缓存中的车辆数据
	public VehicleData getVehicleData(String simNo) {
		if (vehicleMap.containsKey(simNo) == false) {
			String hql = "from VehicleData v where v.simNo =  ? and deleted = ?";

			VehicleData v = (VehicleData) getBaseDao().find(hql,
					new Object[] { simNo, false });
			if (v != null)
				vehicleMap.put(simNo, v);
			return v;
		} else {
			VehicleData v = vehicleMap.get(simNo);
			if (v.getDeleted())
				return null;
			return v;
		}
	}

	/**
	 * 更新缓存中的车辆数据
	 * 
	 * @param simNo
	 * @param v
	 */
	public void updateVehicleData(String simNo, VehicleData v) {
		vehicleMap.put(v.getSimNo(), v);
		if (simNo.equals(v.getSimNo()) == false) {
			vehicleMap.remove(simNo);
			this.realDataMap.remove(simNo);
		}
		if (v.getDeleted()) {
			vehicleMap.remove(v.getSimNo());
			this.realDataMap.remove(v.getSimNo());
		}

		GPSRealData rd = this.get(v.getSimNo());
		if (rd != null) {
			rd.setPlateNo(v.getPlateNo());
			rd.setVehicleId(v.getEntityId());

		}
	}

	public IBaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(IBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public IVehicleService getVehicleService() {
		return vehicleService;
	}

	public void setVehicleService(IVehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}

	public DaoIbatisImpl getQueryDao() {
		return queryDao;
	}

	public void setQueryDao(DaoIbatisImpl queryDao) {
		this.queryDao = queryDao;
	}

	public IAlarmService getAlarmService() {
		return alarmService;
	}

	public void setAlarmService(IAlarmService alarmService) {
		this.alarmService = alarmService;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public double getMaxOfflineTime() {
		return maxOfflineTime;
	}

	public void setMaxOfflineTime(double maxOfflineTime) {
		this.maxOfflineTime = maxOfflineTime;
	}

	public IOnlineRecordService getOnlineRecordService() {
		return onlineRecordService;
	}

	public void setOnlineRecordService(IOnlineRecordService onlineRecordService) {
		this.onlineRecordService = onlineRecordService;
	}

	@Override
	public void saveHisGpsInfo(GpsInfo gi) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateOnlineStatus(String simNo,boolean onlineStatue) {
		GPSRealData rd = get(simNo);
		if(rd != null){
			rd.setOnline(onlineStatue);
			rd.setOnlineDate(new Date());
			rd.setSendTime(new Date());
			onlineRecordService.checkOnline(rd);
		}
	}

}
