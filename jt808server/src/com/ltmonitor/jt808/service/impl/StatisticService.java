package com.ltmonitor.jt808.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.dao.impl.DaoIbatisImpl;
import com.ltmonitor.entity.AlarmRecord;
import com.ltmonitor.entity.Department;
import com.ltmonitor.entity.FuelChangeRecord;
import com.ltmonitor.entity.FuelConsumption;
import com.ltmonitor.entity.FuelRecord;
import com.ltmonitor.entity.GpsMileage;
import com.ltmonitor.entity.OnlineStatic;
import com.ltmonitor.entity.VehicleData;
import com.ltmonitor.entity.VehicleOnlineRate;
import com.ltmonitor.jt808.service.IStatisticService;
import com.ltmonitor.util.DateUtil;

/**
 * 定时统计服务，在quartz.xml文件中定时配置
 * 
 * @author DELL
 * 
 */
public class StatisticService implements IStatisticService // :
// QuartzJobObject
{

	private DaoIbatisImpl queryDao;

	private IBaseDao baseDao;

	public final IBaseDao getBaseDao() {
		return baseDao;
	}

	public final void setBaseDao(IBaseDao value) {
		baseDao = value;
	}

	private static Logger logger = Logger.getLogger(StatisticService.class);

	private double privateMaxOil;

	public final double getMaxOil() {
		return privateMaxOil;
	}

	public final void setMaxOil(double value) {
		privateMaxOil = value;
	}

	// *
	// protected override void ExecuteInternal(JobExecutionContext context)
	// {
	// string msg = string.Format("{0}: 回家吃饭时间, 姓名: {1}, 下次吃饭时间 {2}",
	// DateTime.Now, UserName,
	// context.NextFireTimeUtc.Value.ToLocalTime());
	//
	// Console.WriteLine(msg);
	// }
	//

	private GpsMileage GetLastMileageData(String PlateNo) {
		String hsql = "from GpsMileage where plateNo = ?";
		GpsMileage gm = (GpsMileage) getBaseDao().find(hsql,
				new Object[] { PlateNo });

		if (gm == null) {
			gm = new GpsMileage();
			gm.setPlateNo(PlateNo);
		}
		return gm;
	}

	/**
	 * 每天创建一个Gps记录表
	 */
	public final void CreateTableForGpsInfo() {
		try {
			for (int m = 1; m < 4; m++) {
				Date nextDay = DateUtil.getDate(new Date(),
						Calendar.DAY_OF_YEAR, m);
				String tableName = "gpsInfo"
						+ DateUtil.toStringByFormat(nextDay, "yyyyMMdd");
				queryDao.createGpsInfoTableIfNotExist(tableName);
			}

			Date nextMonth = DateUtil.getDate(new Date(), Calendar.MONTH, 1);
			// 每月创建一个报警记录表
			String tableName = "gpsInfo"
					+ DateUtil.toStringByFormat(nextMonth, "yyyyMM");
			queryDao.createNewAlarmTableIfNotExist(tableName);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	// 里程每小时统计一次
	public final void StaticMileageByHour() {
		java.util.List result = null;
		try {
			Map parameters = new HashMap();
			result = queryDao.queryForList("selectVehicleMileage", parameters);
		} catch (Exception ex) {
			logger.error(ex.getMessage());

			return;
		}

		List gmList = this.baseDao.loadAll(GpsMileage.class);
		Map<String, GpsMileage> gpsMileageMap = new HashMap<String, GpsMileage>();
		for (Object obj : gmList) {
			GpsMileage gm = (GpsMileage) obj;
			gpsMileageMap.put(gm.getPlateNo(), gm);
		}

		logger.warn("**********************static by hour");
		java.util.ArrayList al = new java.util.ArrayList();
		for (Object obj : result) {
			java.util.HashMap ht = (java.util.HashMap) obj;
			String plateNo = (String) ht.get("plateNo");
			double mileage = 0;	//当前车辆里程
			double gas = 0;	//当前车辆油量
			if (ht.get("mileage") != null)
				mileage = Double.parseDouble("" + ht.get("mileage"));
			if (ht.get("gas") != null)
				gas = Double.parseDouble("" + ht.get("gas"));
			try {
				GpsMileage gm = gpsMileageMap.get(plateNo);
				if (gm == null) {
					gm = new GpsMileage();
					gm.setPlateNo(plateNo);
				}
				FuelConsumption fc = new FuelConsumption();
				fc.setPlateNo(plateNo);
				fc.setMileage1(gm.getMileageLastHour());
				fc.setMileage2(mileage);
				fc.setGas1(gm.getGasLastHour());
				fc.setGas2(gas);
				fc.setMileage(fc.getMileage2() - fc.getMileage1());
				fc.setGas(fc.getGas2() - fc.getGas1());
				fc.setStaticDate(new java.util.Date());
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				fc.setHour(cal.get(Calendar.HOUR_OF_DAY));
				fc.setIntervalType(FuelConsumption.STATIC_BY_HOUR); // 按小时统计
				al.add(fc);
				gm.setMileageLastHour(mileage);
				gm.setGasLastHour(gas);
				al.add(gm);
				if (al.size() > 10) {
					getBaseDao().saveOrUpdateAll(al);
					al.clear();
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}

		}

		if (al.size() > 0) {
			getBaseDao().saveOrUpdateAll(al);
		}

	}

	// 每天统计一次
	public final void StaticMileageByDay() {
		java.util.List result = null;
		try {
			Map parameters = new HashMap();
			result = queryDao.queryForList("selectVehicleMileage", parameters);
		} catch (Exception ex) {
			logger.error(ex.getMessage());

			return;
		}

		List gmList = this.baseDao.loadAll(GpsMileage.class);
		Map<String, GpsMileage> gpsMileageMap = new HashMap<String, GpsMileage>();
		for (Object obj : gmList) {
			GpsMileage gm = (GpsMileage) obj;
			gpsMileageMap.put(gm.getPlateNo(), gm);
		}

		logger.warn("**********************static by hour");
		java.util.ArrayList al = new java.util.ArrayList();
		for (Object obj : result) {
			java.util.HashMap ht = (java.util.HashMap) obj;
			String plateNo = (String) ht.get("plateNo");
			double mileage = 0;
			double gas = 0;

			if (ht.get("mileage") != null)
				mileage = Double.parseDouble("" + ht.get("mileage"));
			if (ht.get("gas") != null)
				gas = Double.parseDouble("" + ht.get("gas"));
			try {
				GpsMileage gm = gpsMileageMap.get(plateNo);
				if (gm == null) {
					if (gm == null) {
						gm = new GpsMileage();
						gm.setPlateNo(plateNo);
					}
				}
				FuelConsumption fc = new FuelConsumption();
				fc.setMileage1(gm.getMileageLastDay());
				fc.setMileage2(mileage);
				fc.setGas1(gm.getGasLastDay());
				fc.setGas2(gas);
				fc.setMileage(fc.getMileage2() - fc.getMileage1());
				fc.setGas(fc.getGas2() - fc.getGas1());
				fc.setStaticDate(new java.util.Date());
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				fc.setHour(cal.get(Calendar.HOUR_OF_DAY));
				fc.setIntervalType(FuelConsumption.STATIC_BY_DAY); // 按天统计
				al.add(fc);
				gm.setMileageLastDay(mileage);
				gm.setGasLastDay(gas);
				al.add(gm);
				if (al.size() > 10) {
					getBaseDao().saveOrUpdateAll(al);
					al.clear();
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}

		}

		if (al.size() > 0) {
			getBaseDao().saveOrUpdateAll(al);
		}

	}

	// 五分钟记录一次油量记录，每五分钟比较一次油量变化
	public final void StaticMileageByMinute() {
		java.util.List result = null;
		try {
			Map parameters = new HashMap();
			result = queryDao.queryForList("selectVehicleMileage", parameters);
		} catch (Exception ex) {
			logger.error(ex.getMessage());

			return;
		}

		List gmList = this.baseDao.loadAll(GpsMileage.class);
		Map<String, GpsMileage> gpsMileageMap = new HashMap<String, GpsMileage>();
		for (Object obj : gmList) {
			GpsMileage gm = (GpsMileage) obj;
			gpsMileageMap.put(gm.getPlateNo(), gm);
		}

		logger.warn("**********************static by hour");
		java.util.ArrayList al = new java.util.ArrayList();
		for (Object obj : result) {
			java.util.HashMap ht = (java.util.HashMap) obj;
			String plateNo = (String) ht.get("plateNo");
			double mileage = 0;
			double gas = 0;

			if (ht.get("mileage") != null)
				mileage = Double.parseDouble("" + ht.get("mileage"));
			if (ht.get("gas") != null)
				gas = Double.parseDouble("" + ht.get("gas"));
			try {
				GpsMileage gm = gpsMileageMap.get(plateNo);
				if (gm == null) {
					if (gm == null) {
						gm = new GpsMileage();
						gm.setPlateNo(plateNo);
					}
				}
				double changeOil = gas - gm.getGasLastComp();
				// 如果油量出现剧烈变化的时候，就记录到油量变化表当中
				if (changeOil >= getMaxOil()) {
					FuelChangeRecord fcr = new FuelChangeRecord();

					fcr.setPlateNo(plateNo);
					// fcr.setLatitude(latitude);
					// fcr.setLongitude(rd.getLongitude());
					fcr.setMileage(mileage);
					// fcr.setHappenTime(rd.getSendTime());
					fcr.setFuel(changeOil);
					// fcr.setLocation(rd.getLocation());
					al.add(fcr);
				}
				// 如果油量发生了变化，就更新到LastComp字段里.
				if (gm.getGasLastComp() != gas
						|| gm.getMileageLastComp() != mileage) {
					gm.setLastCompTime(new java.util.Date());
					gm.setGasLastComp(gas);
					gm.setMileageLastComp(mileage);
					// BaseDao.saveOrUpdate(gm);
					al.add(gm);
				}

				FuelRecord vi = new FuelRecord();
				vi.setPlateNo(plateNo);
				vi.setMileage(mileage);
				vi.setGas(gas);
				// vi.setLatitude(rd.getLatitude());
				// vi.setLongitude(rd.getLongitude());
				// vi.setSendTime(rd.getSendTime());
				// vi.setRecordVelocity(rd.getRecordVelocity());
				// vi.setVelocity(rd.getVelocity());
				// vi.setLocation(rd.getLocation());
				// vi.setDirection(rd.getDirection());
				// vi.setStatus(rd.getStatus());
				// vi.setAlarmState(rd.getAlarmState());
				if (al.size() > 10) {
					getBaseDao().saveOrUpdateAll(al);
					al.clear();
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}

		}

		if (al.size() > 0) {
			getBaseDao().saveOrUpdateAll(al);
		}

	}

	/**
	 * 统计每个车辆每天的上线时间，下线时间和上线率
	 */
	public void staticVehicleOnlineRate() {
		try {
			Date staticDate = DateUtil.getDate(new Date()); // 统计前一天的上线率
			Date startDate = staticDate;
			Date endDate = new Date();
			logger.warn("开始统计车辆上线率,统计时间段:" + startDate + "," + endDate);

			// 对于没有上线记录，也就是当天没上线的车辆，也要生成记录
			List<VehicleData> vehicles = baseDao.query(
					"from VehicleData where deleted = ?", false);
			java.util.HashMap<String, VehicleOnlineRate> onlineMap = new java.util.HashMap<String, VehicleOnlineRate>();
			List<VehicleOnlineRate> ls = new ArrayList();
			for (VehicleData vd : vehicles) {
				String hql = "from VehicleOnlineRate where plateNo = ? and staticDate = ?";
				VehicleOnlineRate r = (VehicleOnlineRate) this.baseDao.find(
						hql, new Object[] { vd.getPlateNo(), staticDate });

				if (r == null) {
					r = new VehicleOnlineRate();
					r.setPlateNo(vd.getPlateNo());
				}
				r.setOnlineTime(0);
				r.setTotalTime(24 * 60);
				onlineMap.put(vd.getPlateNo(), r);
				ls.add(r);
			}

			java.util.HashMap parameters = new java.util.HashMap();

			String hsql = "from AlarmRecord where ((startTime >= ? and startTime <= ?)"
					+ " or  (endTime >= ? and endTime <= ?) "
					+ " or  ( startTime < ? and endTime > ?)) and childType = ? ";

			java.util.List result = getBaseDao().query(
					hsql,
					new Object[] { startDate, endDate, startDate, endDate,
							startDate, endDate, AlarmRecord.TYPE_ONLINE });
			for (Object obj : result) {
				AlarmRecord ar = (AlarmRecord) obj;
				double interval = getInterval(ar, startDate, endDate);
				VehicleOnlineRate r = onlineMap.get(ar.getPlateNo());
				if (r == null) {
					r = new VehicleOnlineRate();
					r.setPlateNo(ar.getPlateNo());
				}
				r.setOnlineTime(interval + r.getOnlineTime());
			}

			for (VehicleOnlineRate r : ls) {
				r.setOfflineTime(r.getTotalTime() - r.getOnlineTime());
				double onlineRate = r.getOnlineTime() * 100 / r.getTotalTime();
				r.setOnlineRate(onlineRate);
				r.setStaticDate(startDate);
			}

			baseDao.saveOrUpdateAll(ls);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	private double getInterval(AlarmRecord ar, Date start, Date end) {
		if (ar.getStartTime().compareTo(start) >= 0)
			start = ar.getStartTime();
		if (ar.getEndTime().compareTo(end) <= 0)
			end = ar.getEndTime();

		double interval = com.ltmonitor.util.DateUtil.getSeconds(start, end);
		return interval / 60; // 折算成分钟
	}

	// 统计部门的上线率,在quartz.xml文件中定时配置，每小时统计一次
	public final void StaticOnlineRate() {
		logger.warn("统计上线率 by hour");
		java.util.ArrayList<OnlineStatic> result = new java.util.ArrayList<OnlineStatic>();
		java.util.HashMap parameters = new java.util.HashMap();

		java.util.HashMap onlineMap = new java.util.HashMap();
		String hsql = "from Department where Deleted = ?";
		try {
			java.util.List departments = getBaseDao().query(hsql,
					new Object[] { false });

			// Dictionary<int, Department> departmentMap = new Dictionary<int,
			// Department>();
			for (Object obj : departments) {
				Department dep = (Department) obj;
				OnlineStatic rc = new OnlineStatic();
				rc.setDepId(dep.getEntityId());
				rc.setDepName(dep.getName());
				rc.setStatisticDate(new java.util.Date());
				rc.setParentDepId(dep.getParentId());
				onlineMap.put(rc.getDepId(), rc);
				// departmentMap[rc.DepId] = dep;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);

		}

		// 查询基层车管部门的上线率
		try {
			java.util.List il = queryDao.queryForList("selectOnlineRate",
					parameters);
			for (Object obj : il) {
				java.util.HashMap ht = (java.util.HashMap) obj;
				int DepId = (Integer) ht.get("DepId");
				if (onlineMap.containsKey(DepId)) {
					OnlineStatic rc = (OnlineStatic) onlineMap.get(DepId);
					while (rc != null) {
						rc.setOnlineNum(rc.getOnlineNum()
								+ Integer.parseInt("" + ht.get("OnlineNum")));
						rc.setVehicleNum(rc.getVehicleNum()
								+ Integer.parseInt("" + ht.get("VehicleNum")));
						if (rc.getVehicleNum() > 0) {
							rc.setOnlineRate((100.00 * rc.getOnlineNum())
									/ rc.getVehicleNum());
						}

						// 对上级部门进行累计运算
						rc = (OnlineStatic) onlineMap.get(rc.getParentDepId());
					}
				}
			}
			java.util.ArrayList staticList = new java.util.ArrayList();
			staticList.addAll(onlineMap.values());
			getBaseDao().saveOrUpdateAll(staticList);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);

		}
	}

	public DaoIbatisImpl getQueryDao() {
		return queryDao;
	}

	public void setQueryDao(DaoIbatisImpl queryDao) {
		this.queryDao = queryDao;
	}
}
// : QuartzJobObject