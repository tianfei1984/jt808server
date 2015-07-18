package com.ltmonitor.jt808.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import org.apache.log4j.Logger;
import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.dao.impl.DaoIbatisImpl;
import com.ltmonitor.entity.AlarmConfig;
import com.ltmonitor.entity.AlarmRecord;
import com.ltmonitor.entity.Enclosure;
import com.ltmonitor.entity.GPSRealData;
import com.ltmonitor.entity.StringUtil;
import com.ltmonitor.jt808.service.IAlarmService;
import com.ltmonitor.jt808.service.IAreaAlarmService;
import com.ltmonitor.jt808.service.INewAlarmService;
import com.ltmonitor.jt808.service.ITransferGpsService;
import com.ltmonitor.service.ILocationService;
import com.ltmonitor.util.DateUtil;

/**
 * 报警分析服务
 * 
 * @author DELL
 * 
 */
public class AlarmNewService implements IAlarmService {

	private static Logger logger = Logger.getLogger(AlarmNewService.class);
	private ConcurrentLinkedQueue<AlarmRecord> dataQueue = new ConcurrentLinkedQueue<AlarmRecord>();

	public ConcurrentMap<String, GPSRealData> oldRealDataMap = new ConcurrentHashMap<String, GPSRealData>();
	private IBaseDao baseDao;

	private DaoIbatisImpl queryDao;

	private Thread processRealDataThread;

	// 内存中的保留的已经发生的报警
	private ConcurrentHashMap<String, AlarmRecord> alarmMap = new ConcurrentHashMap<String, AlarmRecord>();
	private Boolean startAnalyze = true;
	// 围栏报警服务
	private IAreaAlarmService areaAlarmService;

	private ITransferGpsService transferGpsService;

	private INewAlarmService newAlarmService;

	private ILocationService locationService;

	// private boolean parkingAlarmEnabled;
	//报警 配置集合
	private Map<String, AlarmConfig> alarmConfigMap = new HashMap<String, AlarmConfig>();

	public AlarmNewService() {

	}
	//启动报警处理服务
	public void start() {
		this.getAlarmConfig();
		processRealDataThread = new Thread(new Runnable() {
			public void run() {
				processRealDataThreadFunc();
			}
		});
		//报警处理线程
		processRealDataThread.start();
		try {
			String hql = "from AlarmRecord where status = ?";
			List ls = this.baseDao.query(hql, AlarmRecord.STATUS_NEW);
			for (Object obj : ls) {
				AlarmRecord r = (AlarmRecord) obj;
				String key = r.getPlateNo() + "_" + r.getChildType() + "_"
						+ r.getType();
				this.alarmMap.put(key, r);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
		}
		this.getAlarmConfig();
	}

	@Override
	public void stopService() {
		startAnalyze = false;
		try {
			processRealDataThread.join(50000);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		// processRealDataThread.stop();
	}
	
	//加载报警配置
	private void getAlarmConfig() {
		try {
			List ls = this.baseDao.query("from AlarmConfig");
			for (Object obj : ls) {
				AlarmConfig a = (AlarmConfig) obj;
				alarmConfigMap.put(a.getAlarmType() + "_" + a.getAlarmSource(),
						a);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			logger.error(ex.getStackTrace());
		}
	}

	public boolean isAlarmEnabled(String alarmType, String alarmSource) {
		String key = alarmType + "_" + alarmSource;
		if (alarmConfigMap.containsKey(key)) {
			AlarmConfig a = alarmConfigMap.get(key);
			return a.isEnabled();
		}
		return false;
	}
	/**
	 * 报警分析的入口函数，实时数据获取后，调用此函数，开始报警分析
	 */
	@Override
	public void processRealData(GPSRealData rd) {
		if (dataQueue.size() > 500) {
			logger.error("报警记录队列堵塞，堵塞队列:" + dataQueue.size());
			// dataQueue.clear();
		}

		this.analyzeData(rd);
	}

	private void processRealDataThreadFunc() {
		//ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
		int count = 0;
		while (startAnalyze) {
			try {
				AlarmRecord tm = dataQueue.poll();
				final List<AlarmRecord> msgList = new ArrayList<AlarmRecord>();
				while (tm != null) {
					msgList.add(tm);
					if (msgList.size() > 30)
						break;
					tm = dataQueue.poll();
				}
				if (msgList.size() > 0) {
					saveAlarmRecord(msgList);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			if (count % 300 == 0) {
				getAlarmConfig();// 重新更新下报警配置
			}
			count++;
			if(count > Short.MAX_VALUE)
				count = 0;
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e1) {
			}
		}
	}

	/**
	 * 保存报警统计记录
	 * @param msgList
	 */
	private void saveAlarmRecord(List<AlarmRecord> msgList) {

		for (AlarmRecord r : msgList) {
			try {
				// analyzeData(msg);
				if (r.getStatus().equals(AlarmRecord.STATUS_NEW)) {
					this.baseDao.save(r);
				} else {
					this.baseDao.saveOrUpdate(r);
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}

	}

	private void analyzeData(GPSRealData rd) {

		try {
			String newStatus = rd.getStatus();
			String newAlarmState = rd.getAlarmState();
			createChangeRecord(AlarmRecord.STATE_FROM_TERM, newStatus, rd);
			createChangeRecord(AlarmRecord.ALARM_FROM_TERM, newAlarmState, rd);

			// 停车报警
			if (isAlarmEnabled(AlarmRecord.TYPE_PARKING,AlarmRecord.ALARM_FROM_PLATFORM)) {
				// 判断是否停车
				String alarmState = rd.getVelocity() < 1 ? AlarmRecord.TURN_ON
						: AlarmRecord.TURN_OFF;
				analyzeAlarm(rd, AlarmRecord.TYPE_PARKING,AlarmRecord.ALARM_FROM_PLATFORM, alarmState);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 报警处理
	 * @param rd
	 * @param alarmType
	 * @param alarmSource
	 * @param alarmState
	 */
	private void analyzeAlarm(GPSRealData rd, String alarmType, String alarmSource, String alarmState) {
		String alarmKey = rd.getPlateNo() + "_" + alarmType + "_" + alarmSource;
		if (alarmState.equals(AlarmRecord.TURN_ON)) {
			// 发生报警
			if (alarmMap.containsKey(alarmKey) == false) {
				AlarmRecord item = new AlarmRecord(rd, alarmType, alarmSource);

				this.dataQueue.add(item);// 加入队列进行入库
				alarmMap.put(alarmKey, item);// 报警标志驻留在内存中，留待下次判断是否已经报警
			} else {
				AlarmRecord item = alarmMap.get(alarmKey);
				if (item.getStatus().equals(AlarmRecord.STATUS_OLD)) {
					AlarmRecord itemNew = new AlarmRecord(rd, alarmType,
							alarmSource);
					this.dataQueue.add(itemNew);// 加入队列进行入库
					alarmMap.put(alarmKey, item);// 报警标志驻留在内存中，留待下次判断是否已经报警
				}
			}
		} else if (alarmState.equals(AlarmRecord.TURN_OFF)) {
			// 报警结束,关闭报警
			if (alarmMap.containsKey(alarmKey)) {
				AlarmRecord item = alarmMap.get(alarmKey); // 如果有产生的报警，则需要消除报警
				// item.setOpen(false);
				alarmMap.remove(alarmKey);
				item.setStatus(AlarmRecord.STATUS_OLD);
				item.setEndTime(rd.getSendTime());
				item.setLatitude1(rd.getLatitude());
				item.setLongitude1(rd.getLongitude());
				double minutes = 0.1 * DateUtil.getSeconds(item.getStartTime(),item.getEndTime()) / 6;
				item.setTimeSpan(minutes);// 计算出报警时长
				this.dataQueue.add(item);
				// this.CreateWarnRecord(AlarmRecord.ALARM_FROM_TERM, alarmType,
				// alarmState, rd);
			}
		}
	}

	/**
	 * 创建状态位变化的记录，记录变化的起止时间和间隔，及经纬度坐标
	 * 
	 * @param alarmSource
	 * @param oldStatus
	 * @param newStatus
	 * @param rd
	 */
	private void createChangeRecord(String alarmSource, String newStatus,
			GPSRealData rd) {

		char[] newChars = (char[]) newStatus.toCharArray();
		for (int m = 0; m < newChars.length; m++) {
			String alarmState = "" + newChars[m];
			int alarmId = 31 - m; // 倒序，转换为部标的报警序号
			String alarmType = "" + alarmId;
			// 转发报警信息
			if (alarmSource.equals(AlarmRecord.ALARM_FROM_TERM)
					&& alarmState.equals(AlarmRecord.TURN_ON)
					&& this.isAlarmEnabled(alarmType, alarmSource)) {

				// 转发809报警
				if (this.transferGpsService.isTransferTo809Enabled()) {
					transferAlarm(alarmSource, alarmType, rd);
				}

				// if (alarmId == 0) {
				// 插入新的紧急报警
				newAlarmService.insertAlarm(alarmSource, alarmType, rd);
				// }
			}
			if (alarmSource.equals(AlarmRecord.ALARM_FROM_TERM)) {
				if (alarmId == 20) {
					//进出区域报警
					alarmType = rd.getEnclosureAlarm() == 0 ? AlarmRecord.TYPE_IN_AREA
							: AlarmRecord.TYPE_CROSS_BORDER;
				} else if (alarmId == 21) {
					//进出路线报警
					alarmType = rd.getEnclosureAlarm() == 0 ? AlarmRecord.TYPE_ON_ROUTE
							: AlarmRecord.TYPE_OFFSET_ROUTE;
				}
			}
			if (this.isAlarmEnabled(alarmType, alarmSource)) {
				this.analyzeAlarm(rd, alarmType, alarmSource, alarmState);
			}
		}
	}

	/**
	 * 创建报警记录 OperateType表示类型，状态位变化(State)还是报警标志位变化(Warn)， childType
	 * 表示标志位的字节32位序号, 如Acc标志位在32个状态位的第一个位置,超速报警在报警位的第二个位置. warnState “1”代表报警 ，
	 * 0代表报警消除
	 */
	private void CreateWarnRecord(String OperateType, String childType,
			String warnState, GPSRealData rd) {
		AlarmRecord sr = CreateRecord(OperateType, childType, warnState, rd);
		if (sr != null)
			getBaseDao().saveOrUpdate(sr);
	}

	/**
	 * 将报警转发给上级平台
	 */
	private void transferAlarm(String alarmSource, String alarmType,
			GPSRealData rd) {
		if (transferGpsService.isTransferTo809Enabled() == false)
			return;
		AlarmRecord ar = new AlarmRecord();
		ar.setPlateNo(rd.getPlateNo());
		ar.setStartTime(rd.getSendTime());
		ar.setChildType(alarmType);
		ar.setType(alarmSource);
		ar.setVehicleId(rd.getVehicleId());
		this.transferGpsService.transfer(ar, rd);
	}

	public AlarmRecord CreateRecord(String alarmSource, String alarmType,
			String alarmState, GPSRealData rd) {
		String hsql = "from AlarmRecord rec where startTime > ? and  rec.plateNo = ? and rec.status = ? and rec.type = ? and rec.childType = ?";
		// 查看是否有未消除的报警记录
		Date startDate = DateUtil.getDate(new Date(), Calendar.DAY_OF_YEAR, -5);
		AlarmRecord sr = (AlarmRecord) getBaseDao().find(
				hsql,
				new Object[] { startDate, rd.getPlateNo(),
						AlarmRecord.STATUS_NEW, alarmSource, alarmType });

		if (sr == null) {
			if (AlarmRecord.TURN_OFF.equals(alarmState))
				return null;

			sr = new AlarmRecord();
			// 停车报警
			if (alarmType.equals("19")) {
				/**
				 * Enclosure ec = IsInEnclosure(rd); if (ec != null) {
				 * sr.Station = ec.Name; sr.Location = ec.Name; }
				 */
			}

			if (rd.getEnclosureId() > 0) {
				String hql = "from Enclosure where enclosureId = ?";
				Enclosure ec = (Enclosure) baseDao.find(hql,
						rd.getEnclosureId());
				if (ec != null) {
					sr.setLocation(ec.getName());
				}

			}
			sr.setVehicleId(rd.getVehicleId());
			sr.setType(alarmSource);
			sr.setPlateNo(rd.getPlateNo());
			sr.setStartTime(rd.getSendTime());
			sr.setStatus(AlarmRecord.STATUS_NEW);
			sr.setEndTime(new Date());
			sr.setLatitude(rd.getLatitude());
			sr.setLongitude(rd.getLongitude());
			String location = rd.getLocation();
			if (StringUtil.isNullOrEmpty(location))
				location = locationService.getLocation(sr.getLatitude(),
						sr.getLongitude());
			sr.setLocation(location);
			sr.setVelocity(rd.getVelocity());
			sr.setChildType(alarmType);
			sr.setResponseSn(rd.getResponseSn());

		} else {
			sr.setEndTime(new Date());
			double minutes = DateUtil.getSeconds(sr.getStartTime(),
					rd.getSendTime()) / 60;
			sr.setTimeSpan(minutes);// 计算出报警时长
			if (alarmState.equals(AlarmRecord.TURN_OFF)) {
				sr.setStatus(AlarmRecord.STATUS_OLD);
				sr.setEndTime(rd.getSendTime());

				sr.setLatitude1(rd.getLatitude());
				sr.setLongitude1(rd.getLongitude());
			} else
				return null;

		}

		sr.setType(alarmSource);
		sr.setChildType(alarmType);
		return sr;
	}

	private GPSRealData getRalData(String plateNo) {
		GPSRealData rd = oldRealDataMap.get(plateNo);
		if (rd == null) {
			rd = new GPSRealData();
			rd.setOnline(false);
		}
		return rd;
	}


	public IBaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(IBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public ITransferGpsService getTransferGpsService() {
		return transferGpsService;
	}

	public void setTransferGpsService(ITransferGpsService transferGpsService) {
		this.transferGpsService = transferGpsService;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public DaoIbatisImpl getQueryDao() {
		return queryDao;
	}

	public void setQueryDao(DaoIbatisImpl queryDao) {
		this.queryDao = queryDao;
	}

	public IAreaAlarmService getAreaAlarmService() {
		return areaAlarmService;
	}

	public void setAreaAlarmService(IAreaAlarmService areaAlarmService) {
		this.areaAlarmService = areaAlarmService;
	}

	public INewAlarmService getNewAlarmService() {
		return newAlarmService;
	}

	public void setNewAlarmService(INewAlarmService newAlarmService) {
		this.newAlarmService = newAlarmService;
	}

}
