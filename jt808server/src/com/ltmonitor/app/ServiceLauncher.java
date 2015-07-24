package com.ltmonitor.app;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.entity.VehicleData;
import com.ltmonitor.jt808.service.IGpsDataService;
import com.ltmonitor.service.IBaseService;

public class ServiceLauncher {

	private static Logger logger = Logger.getLogger(ServiceLauncher.class);
	protected static ApplicationContext context = null;

	private static IGpsDataService GpsDataService;

	private static IBaseDao BaseDao;
	
	private static IBaseService baseService;

	public static void launch() {

		// PropertyConfigurator.configure("log4j.properties");
		// context = new ClassPathXmlApplicationContext(
		// "classpath:applicationContextService.xml");
		context = new ClassPathXmlApplicationContext(new String[] {
				"applicationContextService-resources.xml",
				"applicationContextService-dao.xml",
				"applicationContext-quartz.xml",
				"applicationContextService.xml",
				"applicationContext-ibatis.xml",
				"applicationContext-jt808.xml",
				"applicationContext-rmi-service.xml" });
		
		
		if (context == null) {
			int x = 0;
		}

		GpsDataService = (IGpsDataService) getBean("gpsDataService");
		baseService = (IBaseService) getBean("baseService");
		setBaseDao((IBaseDao) getBean("baseDao"));
		logger.info("成功加载数据库和服务");
		// URL log4jRes = ServiceLauncher.class.getResource("log4j.properties");
	}

	public static Object getBean(String beanID) {
		return context.getBean(beanID);
	}
	

	public static void main(String[] args) {
		try {
			ServiceLauncher launcher = new ServiceLauncher();

			launcher.launch();
			IBaseDao dao = (IBaseDao) launcher.getBean("entityManager");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			System.out.println(ex.getStackTrace());
		}
	}

	public static IGpsDataService getGpsDataService() {
		return GpsDataService;
	}

	public static void setGpsDataService(IGpsDataService gpsDataService) {
		GpsDataService = gpsDataService;
	}

	public void CreateTestVehicle() {
		String plateNo = "测A";
		String sn = "139";
		for (int m = 0; m < 100; m++) {
			String str = "" + m;
			while (str.length() < 5) {
				str = "0" + str;
			}
			VehicleData vd = new VehicleData();
			vd.setPlateNo(str);

		}
	}

	public static IBaseDao getBaseDao() {
		return BaseDao;
	}

	public static void setBaseDao(IBaseDao baseDao) {
		BaseDao = baseDao;
	}

	public static IBaseService getBaseService() {
		return baseService;
	}

	public static void setBaseService(IBaseService baseService) {
		ServiceLauncher.baseService = baseService;
	}

}
