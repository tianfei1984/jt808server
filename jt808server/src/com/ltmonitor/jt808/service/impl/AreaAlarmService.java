package com.ltmonitor.jt808.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.dao.impl.DaoIbatisImpl;
import com.ltmonitor.entity.AlarmRecord;
import com.ltmonitor.entity.Enclosure;
import com.ltmonitor.entity.EnclosureBinding;
import com.ltmonitor.entity.GPSRealData;
import com.ltmonitor.entity.LineSegment;
import com.ltmonitor.entity.PointLatLng;
import com.ltmonitor.entity.StringUtil;
import com.ltmonitor.jt808.service.AlarmItem;
import com.ltmonitor.jt808.service.IAreaAlarmService;
import com.ltmonitor.jt808.service.INewAlarmService;
import com.ltmonitor.service.IRealDataService;
import com.ltmonitor.service.MapFixService;
import com.ltmonitor.util.Constants;
import com.ltmonitor.util.DateUtil;

/**
 * 区域报警和路线偏移报警分析服务
 * 
 * @author DELL
 * 
 */
public class AreaAlarmService implements IAreaAlarmService {

	protected Logger logger = Logger.getLogger(getClass());

	public static String TURN_ON = "1"; // 报警开
	public static String TURN_OFF = "0"; // 报警关闭
	private IBaseDao baseDao;

	private INewAlarmService newAlarmService;

	private boolean areaAlarmEnabled;

	private IRealDataService realDataService;

	private DaoIbatisImpl queryDao;

	/**
	 * 分析进出区域的线程
	 */
	private Thread analyzeThread;

	private boolean continueAnalyze = true;

	// 保存车辆当前所在地班线
	private Map offsetRouteWarn = new HashMap();
	private Map onRouteWarn = new HashMap();
	private Map routePointMap = new HashMap();
	private Map<String, AlarmItem> alarmMap = new HashMap<String, AlarmItem>();

	Map<String,GPSRealData> realDataMap = new HashMap<String,GPSRealData>();
	public ConcurrentMap<String, Enclosure> enclosureMap = new ConcurrentHashMap<String, Enclosure>();
	public ConcurrentMap<String, LineSegment> overSpeedMap = new ConcurrentHashMap<String, LineSegment>();

	public void start() {
		if (areaAlarmEnabled) {
			analyzeThread = new Thread(new Runnable() {
				public void run() {
					analyzeThreadFunc();
				}
			});
			analyzeThread.start();
		}
	}

	private void analyzeThreadFunc() {
		while (continueAnalyze) {

			analyze();
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e1) {
			}
		}
	}

	public void stopService() {
		continueAnalyze = false;
		if(analyzeThread == null)
			return;
		try {
			analyzeThread.join(50000);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	private void analyze() {
		Map params = new HashMap();
		Date d = new Date();
		d = DateUtil.getDate(d, Calendar.MINUTE, -5);
		params.put("onlineDate", d);// 只查上线车辆的绑定区域
		String queryId = "selectAllBindAreas";// 查询平台所有绑定的区域，sql语句在Ibatis配置文件vehicleInfo.xml对应的querID中
		List bindings = queryDao.queryForList(queryId, params);
		Map<Integer, Enclosure> areaMap = new HashMap<Integer, Enclosure>();
		for (Object obj : bindings) {
			Map eb = (Map) obj;
			int areaId = Integer.parseInt("" + eb.get("enclosureId"));
			int bindId = Integer.parseInt("" + eb.get("bindId"));
			String simNo = "" + eb.get("simNo");

			GPSRealData rd = this.realDataService.get(simNo);
			if (rd != null) {
				GPSRealData oldRd = realDataMap.get(simNo);
				if(oldRd!=null)
				{
					if(oldRd.getLatitude() == rd.getLatitude() && oldRd.getLongitude() == rd.getLongitude())
					{
						continue;//如果坐标没变化，没必要分析区域
					}
				}else
				{
					oldRd = new GPSRealData();
				}

				oldRd.setLatitude(rd.getLatitude());
				oldRd.setLongitude(rd.getLongitude());
				realDataMap.put(simNo, oldRd);
				
				Enclosure ec = areaMap.get(areaId);
				if (ec == null) {
					ec = (Enclosure) this.baseDao.load(Enclosure.class, areaId);
					areaMap.put(areaId, ec);
				}
				
				this.AnalyzeAreaAlarm(rd, ec);
			}
		}
	}

	// 当前时间是否在班线的检测运行时间端内
	private Boolean IsInTimeSpan(Enclosure br) {
		/**
		 * String str = new Date().toString("HH:mm"); Date now = new Date();
		 * Date start = Date.Parse(new Date().ToString("yyyy-MM-dd ") +
		 * br.StartTime); Date end = Date.Parse(new
		 * Date().ToString("yyyy-MM-dd ") + br.EndTime);
		 * 
		 * return now.CompareTo(start) >= 0 && now.CompareTo(end) <= 0 ;
		 */
		return true;
	}

	/**
	 * 分析分段限速超速报警
	 * 
	 * @param rd
	 * @param seg
	 */
	private void AnalyzeOverSpeed(GPSRealData rd, LineSegment seg, Enclosure ec) {
		String key = rd.getPlateNo();
		LineSegment alarmSeg = overSpeedMap.get(key);

		if (seg == null && alarmSeg != null) {
			// 如果偏离了路段或者离开了路段，报警消除
			this.overSpeedMap.remove(key);
			CreateWarnRecord(AlarmRecord.ALARM_FROM_PLATFORM,
					AlarmRecord.TYPE_OVER_SPEED_ON_ROUTE, TURN_OFF, rd,
					alarmSeg.getEntityId(), null);
		}

		// 查看是否打开分段限速开关
		if (seg == null || seg.getLimitSpeed() == false)
			return;

		// 获取当前在超速报警的路段
		if (alarmSeg != null && alarmSeg.getEntityId() != seg.getEntityId()) {
			// 报警消除
			this.overSpeedMap.remove(key);
			CreateWarnRecord(AlarmRecord.ALARM_FROM_PLATFORM,
					AlarmRecord.TYPE_OVER_SPEED_ON_ROUTE, TURN_OFF, rd,
					alarmSeg.getEntityId(), null);
			alarmSeg = null;
		}
		// 超速
		if (rd.getVelocity() > seg.getMaxSpeed()) {
			insertAlarm(AlarmRecord.ALARM_FROM_PLATFORM,
					AlarmRecord.TYPE_OVER_SPEED_ON_ROUTE, rd, ec.getName()
							+ ",拐点:" + seg.getName());
			if (alarmSeg == null) {
				// 第一次报警
				CreateWarnRecord(AlarmRecord.ALARM_FROM_PLATFORM,
						AlarmRecord.TYPE_OVER_SPEED_ON_ROUTE, TURN_ON, rd,
						seg.getEntityId(),
						ec.getName() + ",路段:" + seg.getName());
				this.overSpeedMap.put(key, seg);
			}

		} else {
			if (alarmSeg != null) {
				// 报警消除
				this.overSpeedMap.remove(key);
				CreateWarnRecord(AlarmRecord.ALARM_FROM_PLATFORM,
						AlarmRecord.TYPE_OVER_SPEED_ON_ROUTE, TURN_OFF, rd,
						seg.getEntityId(), null);
			}
		}
	}

	private void AnalyzeOffsetRoute(GPSRealData rd, Enclosure ec, PointLatLng mp) {
		Date start = new Date();
		String alarmType = AlarmRecord.TYPE_OFFSET_ROUTE;
		String alarmSource = AlarmRecord.ALARM_FROM_PLATFORM;
		int maxAllowedOffsetTime = ec.getOffsetDelay(); // 最长偏移时间
		if (IsInTimeSpan(ec)) {
			// 判断是否在班线中
			LineSegment seg = IsInLineSegment(mp, ec);
			boolean isOnRoute = seg != null;
			logger.info(rd.getPlateNo() + ",班线" + ec.getName() + ","
					+ (seg == null ? "偏移" : "在路线上") + rd.getSendTime());
			String alarmKey = rd.getPlateNo() + "_" + ec.getEntityId();
			AlarmItem offsetAlarm = (AlarmItem) offsetRouteWarn.get(alarmKey);
			AlarmItem onRouteAlarm = (AlarmItem) onRouteWarn.get(alarmKey);
			if (isOnRoute == false) {
				if (offsetAlarm == null) {
					this.AnalyzeOverSpeed(rd, null, ec);
					offsetAlarm = new AlarmItem(rd, alarmType, alarmSource);
					// 第一次偏移
					offsetRouteWarn.put(alarmKey, offsetAlarm);
					offsetAlarm.setStatus("");
				}
				Date offsetTime = offsetAlarm.getAlarmTime();
				double ts = DateUtil.getSeconds(offsetTime, rd.getSendTime());
				// 超过最大允许偏移时间
				if (ts > maxAllowedOffsetTime
						&& offsetAlarm.getStatus().equals("")) {
					offsetAlarm.setStatus(AlarmRecord.STATUS_NEW); // 设置为报警状态
					this.insertAlarm(alarmSource, alarmType, rd, ec.getName());
					// 偏离报警开始
					Date originTime = rd.getSendTime();
					rd.setSendTime(offsetTime);
					// 创建偏移记录
					AlarmRecord sr = CreateRecord(
							AlarmRecord.ALARM_FROM_PLATFORM,
							AlarmRecord.TYPE_OFFSET_ROUTE, TURN_ON, rd,
							ec.getEntityId());
					if (sr != null) {
						sr.setStation(ec.getEntityId());
						sr.setLocation(ec.getName());
						baseDao.saveOrUpdate(sr);
					}
					// 关闭进入线路报警记录
					sr = CreateRecord(AlarmRecord.ALARM_FROM_PLATFORM,
							AlarmRecord.TYPE_ON_ROUTE, TURN_OFF, rd,
							ec.getEntityId());
					if (sr != null) {
						sr.setStation(ec.getEntityId());
						sr.setLocation(ec.getName());
						baseDao.saveOrUpdate(sr);
					}
					rd.setSendTime(originTime);
					onRouteWarn.remove(alarmKey);
				}

			} else {
				AnalyzeOverSpeed(rd, seg, ec);// 分段限速报警
				if (offsetAlarm != null) {
					offsetAlarm.setStatus(AlarmRecord.STATUS_OLD);// 报警关闭
					logger.info(rd.getPlateNo() + "重回路线," + rd.getSendTime());
					// 重新回到路线中，报警关闭
					CreateWarnRecord(AlarmRecord.ALARM_FROM_PLATFORM,
							AlarmRecord.TYPE_OFFSET_ROUTE, TURN_OFF, rd,
							ec.getEntityId(), null);
					offsetRouteWarn.remove(alarmKey);
				}
				if (onRouteAlarm == null) {
					onRouteAlarm = new AlarmItem(rd, AlarmRecord.TYPE_ON_ROUTE,
							alarmSource);
					// 第一次进入路线
					onRouteWarn.put(alarmKey, onRouteAlarm);
					// 同时创建进入线路的记录
					AlarmRecord sr = CreateRecord(
							AlarmRecord.ALARM_FROM_PLATFORM,
							AlarmRecord.TYPE_ON_ROUTE, TURN_ON, rd,
							ec.getEntityId());
					if (sr != null) {
						sr.setStation(ec.getEntityId());
						sr.setLocation(ec.getName());
						baseDao.saveOrUpdate(sr);
					}
				}
			}
		}

		double ts2 = DateUtil.getSeconds(start, new Date());
		if (ts2 > 0.2)
			logger.info(rd.getPlateNo() + "," + ec.getName() + "，线路偏移耗时" + ts2);

	}

	/**
	 * 创建报警记录
	 */
	private void CreateWarnRecord(String OperateType, String childType,
			String warnState, GPSRealData rd, int enclosureId, String location) {
		AlarmRecord sr = CreateRecord(OperateType, childType, warnState, rd,
				enclosureId);
		if (sr != null) {
			if (location != null)
				sr.setLocation(location);
			baseDao.saveOrUpdate(sr);
		}
	}

	private LineSegment IsInLineSegment(PointLatLng mp, Enclosure ec) {
		logger.info("开始检测线路:" + ec.getName());
		List segments = GetLineSegments(ec.getEntityId());
		for (Object obj : segments) {
			LineSegment seg = (LineSegment) obj;
			/**
			 * 旧的缓冲区算法 //List<PointLatLng> bps =
			 * GetBufferPoints(seg.getEntityId());
			 * 
			 * //Boolean result = MapFixService.IsInPolygon(mp, bps);
			 */
			PointLatLng p1 = new PointLatLng(seg.getLongitude1(),
					seg.getLatitude1());
			PointLatLng p2 = new PointLatLng(seg.getLongitude2(),
					seg.getLatitude2());
			boolean result = MapFixService.IsPointOnLine(p1, p2, mp,
					seg.getLineWidth());
			if (result)
				return seg;
		}

		return null;
	}

	private List<LineSegment> GetLineSegments(int enclosureId) {
		String hsql = "from LineSegment where enclosureId = ?";
		List ls = baseDao.query(hsql, enclosureId);
		return ls;
	}

	private Enclosure getOldEnclosure(String plateNo, int enclosureId) {
		Enclosure oldEc = null;
		String key = plateNo + "_" + enclosureId;
		if (enclosureMap.containsKey(key)) {
			oldEc = enclosureMap.get(key);
		}
		return oldEc;
	}

	private void CrossBorder(Enclosure ec, GPSRealData rd, boolean isInEnclosure) {
		Enclosure oldEc = getOldEnclosure(rd.getPlateNo(), ec.getEntityId());
		if (isInEnclosure && oldEc == null) {
			insertAlarm(AlarmRecord.ALARM_FROM_PLATFORM,
					AlarmRecord.TYPE_IN_AREA, rd, ec.getName());

			// 说明第一次进入围栏
			CreateAlarmRecord(AlarmRecord.ALARM_FROM_PLATFORM,
					AlarmRecord.TYPE_IN_AREA, TURN_ON, rd, ec);

			// CreateAlarmRecord(AlarmRecord.ALARM_FROM_PLATFORM,
			// AlarmRecord.TYPE_CROSS_BORDER, TURN_OFF, rd, ec);

			String key = rd.getPlateNo() + "_" + ec.getEntityId();
			enclosureMap.put(key, ec); // 保存在内存中，表明当前车辆在这个围栏中
		}

		if (isInEnclosure == false && oldEc != null) {

			insertAlarm(AlarmRecord.ALARM_FROM_PLATFORM,
					AlarmRecord.TYPE_CROSS_BORDER, rd, ec.getName());

			// 第一次离开围栏
			// CreateAlarmRecord(AlarmRecord.ALARM_FROM_PLATFORM,
			// AlarmRecord.TYPE_CROSS_BORDER, TURN_ON, rd, ec);

			CreateAlarmRecord(AlarmRecord.ALARM_FROM_PLATFORM,
					AlarmRecord.TYPE_IN_AREA, TURN_OFF, rd, ec);
			String key = rd.getPlateNo() + "_" + ec.getEntityId();
			enclosureMap.remove(key);// 表明当前车辆不在这个围栏中
		}

	}

	// 创建围栏报警记录
	private void CreateAlarmRecord(String OperateType, String childType,
			String warnState, GPSRealData rd, Enclosure ec) {
		AlarmRecord sr = CreateRecord(OperateType, childType, warnState, rd,
				ec.getEntityId());
		if (sr != null && ec != null) {
			sr.setStation(ec.getEntityId());
			sr.setLocation(ec.getName());
		}
		if (sr != null)
			baseDao.saveOrUpdate(sr);
	}

	private AlarmRecord CreateRecord(String alarmSource, String alarmType,
			String alarmState, GPSRealData rd, int stationId) {
		String hsql = "from AlarmRecord rec where rec.plateNo = ? and rec.status = ? and rec.type = ? and rec.childType = ? and station = ?";
		// 查看是否有未消除的报警记录
		AlarmRecord sr = (AlarmRecord) baseDao.find(hsql,
				new Object[] { rd.getPlateNo(), AlarmRecord.STATUS_NEW,
						alarmSource, alarmType, stationId });

		if (sr == null) {
			if (TURN_OFF.equals(alarmState))
				return null;

			sr = new AlarmRecord();
			sr.setVehicleId(rd.getVehicleId());
			sr.setPlateNo(rd.getPlateNo());
			sr.setStartTime(rd.getSendTime());
			sr.setStatus(AlarmRecord.STATUS_NEW);
			sr.setEndTime(new Date());
			sr.setLatitude(rd.getLatitude());
			sr.setLongitude(rd.getLongitude());
			// sr.setLocation( MapFix.GetLocation(sr.getLongitude(),
			// sr.getLatitude()));
			sr.setVelocity(rd.getVelocity());
		} else {
			sr.setEndTime(new Date());
			double minutes = DateUtil.getSeconds(sr.getStartTime(),
					sr.getEndTime()) / 60;
			sr.setTimeSpan(minutes);
			if (alarmState.equals(TURN_OFF)) {
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

	/**
	 * 报警插入到数据库中，等待推送到前台弹出报警
	 * 
	 * @param alarmType
	 * @param alarmSource
	 * @param rd
	 */
	private void insertAlarm(String alarmSource, String alarmType,
			GPSRealData rd, String areaName) {
		rd.setLocation("区域:" + areaName);
		this.newAlarmService.insertAlarm(alarmSource, alarmType, rd);
	}

	private List<Enclosure> getBindAreas(int vehicleId) {

		List<Enclosure> result = new ArrayList<Enclosure>();
		String hql = "from EnclosureBinding where vehicleId = ? and bindType = ?";
		List bindings = this.baseDao.query(hql, new Object[] { vehicleId,
				EnclosureBinding.BINDING_PLATFORM });
		for (Object obj : bindings) {
			EnclosureBinding eb = (EnclosureBinding) obj;
			Enclosure ec = (Enclosure) this.baseDao.load(Enclosure.class,
					eb.getEnclosureId());
			if (ec.getDeleted() == false)
				result.add(ec);
		}
		return result;

	}

	// 判断车辆当前位置是否在围栏中
	private void AnalyzeAreaAlarm(GPSRealData rd, Enclosure ec) {

		double lat = rd.getLatitude();
		double lng = rd.getLongitude();

		PointLatLng mp = null;
		// 查询该车辆绑定的区域
		PointLatLng pointForGoogle = null;
		PointLatLng pointForBaidu = null;

		if (StringUtil.isNullOrEmpty(ec.getPoints()))
			return;

		if (Constants.MAP_BAIDU.equals(ec.getMapType())) {
			if (pointForBaidu == null) {
				pointForBaidu = MapFixService
						.fix(lat, lng, Constants.MAP_BAIDU);
			}
			mp = pointForBaidu;
		} else {

			if (pointForGoogle == null) {
				pointForGoogle = MapFixService.fix(lat, lng,
						Constants.MAP_GOOGLE);
			}
			mp = pointForGoogle;
		}

		if (Enclosure.ROUTE.equals(ec.getEnclosureType())) {
			// 路线偏移报警
			AnalyzeOffsetRoute(rd, ec, mp);
		} else {
			if (ec.getKeyPoint() == 1) {
				if (ec.getByTime())
					monitorKeyPointArrvie(rd, ec, mp);// 监控在规定的时间段内到达
				else
					monitorKeyPointLeave(rd, ec, mp); // 监控在规定的时间段内离开
			} else {
				boolean inEnclosure = IsInEnclourse(ec, mp);
				CrossBorder(ec, rd, inEnclosure);
			}
		}
	}

	// 监控车辆是否在规定的时间段内到达
	private void monitorKeyPointArrvie(GPSRealData rd, Enclosure ec,
			PointLatLng mp) {
		Date now = new Date();
		String alarmType = AlarmRecord.TYPE_ARRIVE_NOT_ON_TIME;
		String alarmSource = AlarmRecord.ALARM_FROM_PLATFORM;
		String key = rd.getPlateNo() + "_" + ec.getEntityId() + "_" + alarmType;
		AlarmItem item = alarmMap.get(key);

		if (now.compareTo(ec.getStartDate()) >= 0
				&& now.compareTo(ec.getEndDate()) <= 0) {
			if (item == null) {
				boolean inEnclosure = IsInEnclourse(ec, mp);
				item = new AlarmItem(rd, alarmType, alarmSource);
				alarmMap.put(key, item);
			}
		} else if (now.compareTo(ec.getEndDate()) > 0) {
			if (item == null) {
				boolean inEnclosure = IsInEnclourse(ec, mp);
				if (inEnclosure == false) {
					this.insertAlarm(alarmSource, alarmType, rd, ec.getName());
					// 第一次报警
					CreateAlarmRecord(AlarmRecord.ALARM_FROM_PLATFORM,
							alarmType, TURN_ON, rd, ec);
					
					item = new AlarmItem(rd, alarmType, alarmSource);
					item.setStatus(AlarmRecord.STATUS_OLD);
					alarmMap.put(key, item);
				}
			}
			/**
			 * boolean inEnclosure = IsInEnclourse(ec, mp); if (inEnclosure) {
			 * // 关闭报警 CreateAlarmRecord(AlarmRecord.ALARM_FROM_PLATFORM,
			 * alarmType, TURN_OFF, rd, ec); item = new AlarmItem(key, new
			 * Date()); item.setOpen(false); alarmMap.put(key, item); }
			 */
		}

	}

	/**
	 * 监控车辆是否在规定的时间段内离开
	 * 
	 * @param rd
	 * @param ec
	 * @param mp
	 */
	private void monitorKeyPointLeave(GPSRealData rd, Enclosure ec,
			PointLatLng mp) {
		Date now = new Date();
		String alarmSource = AlarmRecord.ALARM_FROM_PLATFORM;
		String alarmType = AlarmRecord.TYPE_LEAVE_NOT_ON_TIME;
		String key = rd.getPlateNo() + "_" + ec.getEntityId() + "_" + alarmType;
		AlarmItem item = alarmMap.get(key);
		if (now.compareTo(ec.getStartDate()) < 0
				&& now.compareTo(ec.getEndDate()) > 0) {
			if (item == null) {
				// 判断是否在规定的时间离开关键点
				boolean inEnclosure = IsInEnclourse(ec, mp);
				if (inEnclosure) {
					this.insertAlarm(alarmSource, alarmType, rd, ec.getName());
					// 第一次报警
					CreateAlarmRecord(AlarmRecord.ALARM_FROM_PLATFORM,
							alarmType, TURN_ON, rd, ec);

					item = new AlarmItem(rd, alarmType, alarmSource);
					alarmMap.put(key, item);
				}
			} else if (item.getStatus().equals(AlarmRecord.STATUS_NEW)) {
				// 报警已经打开,则需要关闭报警
				boolean inEnclosure = IsInEnclourse(ec, mp);
				if (inEnclosure == false) {
					// 关闭报警
					CreateAlarmRecord(AlarmRecord.ALARM_FROM_PLATFORM,
							alarmType, TURN_OFF, rd, ec);
					item = new AlarmItem(rd, alarmType, alarmSource);
					// item.setOpen(false);
					alarmMap.put(key, item);
				}
			}
		}

	}

	private boolean IsInEnclourse(Enclosure ec, PointLatLng mp) {
		List<PointLatLng> points = GetPoints(ec.getPoints());

		if (Enclosure.POLYGON.equals(ec.getEnclosureType())
				&& points.size() > 2) {
			if (MapFixService.IsInPolygon(mp, points)) {
				Date end = new Date();
				// TimeSpan ts = end - start;
				// logger.info("围栏查询耗时:" + ts.TotalSeconds);
				return true;
			}
		} else if (Enclosure.CIRCLE.equals(ec.getEnclosureType())
				&& points.size() > 0) {
			PointLatLng pl = points.get(0);
			double radius = ec.getRadius();
			if (MapFixService.IsInCircle(mp, pl, radius))
				return true;
		} else if (Enclosure.RECT.equals(ec.getEnclosureType())
				&& points.size() > 1) {
			PointLatLng p1 = points.get(0);
			PointLatLng p2 = points.get(2);

			if (MapFixService.IsInRect(mp.lng, mp.lat, p1.lng, p1.lat, p2.lng,
					p2.lat))
				return true;
		}
		return false;
	}

	/**
	 * 字符串坐标点，变成列表形式， 字符串格式要求：112.24，31.23；112.24，31.23
	 * 
	 * @param strPoints
	 * @return
	 */
	private List<PointLatLng> GetPoints(String strPoints) {
		List<PointLatLng> results = new ArrayList<PointLatLng>();

		String[] strPts = strPoints.split(";");
		for (String strPt : strPts) {
			if (StringUtil.isNullOrEmpty(strPt) == false) {
				String[] strPoint = strPt.split(",");
				if (strPoint.length == 2) {
					PointLatLng pl = new PointLatLng(
							Double.parseDouble(strPoint[0]),
							Double.parseDouble(strPoint[1]));
					results.add(pl);
				}
			}
		}
		return results;
	}

	public IBaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(IBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public boolean isAreaAlarmEnabled() {
		return areaAlarmEnabled;
	}

	public void setAreaAlarmEnabled(boolean areaAlarmEnabled) {
		this.areaAlarmEnabled = areaAlarmEnabled;
	}

	public INewAlarmService getNewAlarmService() {
		return newAlarmService;
	}

	public void setNewAlarmService(INewAlarmService newAlarmService) {
		this.newAlarmService = newAlarmService;
	}

	public IRealDataService getRealDataService() {
		return realDataService;
	}

	public void setRealDataService(IRealDataService realDataService) {
		this.realDataService = realDataService;
	}

	public DaoIbatisImpl getQueryDao() {
		return queryDao;
	}

	public void setQueryDao(DaoIbatisImpl queryDao) {
		this.queryDao = queryDao;
	}

}
