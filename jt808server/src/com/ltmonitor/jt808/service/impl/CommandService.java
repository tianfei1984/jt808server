package com.ltmonitor.jt808.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import com.ltmonitor.app.StringHelper;
import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.entity.Enclosure;
import com.ltmonitor.entity.EnclosureNode;
import com.ltmonitor.entity.LineSegment;
import com.ltmonitor.entity.PointLatLng;
import com.ltmonitor.entity.StringUtil;
import com.ltmonitor.entity.TerminalCommand;
import com.ltmonitor.entity.VehicleData;
import com.ltmonitor.jt808.protocol.AnswerItem;
import com.ltmonitor.jt808.protocol.BitConverter;
import com.ltmonitor.jt808.protocol.CircleAreaItem;
import com.ltmonitor.jt808.protocol.EventSettingItem;
import com.ltmonitor.jt808.protocol.JT_8103;
import com.ltmonitor.jt808.protocol.JT_8104;
import com.ltmonitor.jt808.protocol.JT_8105;
import com.ltmonitor.jt808.protocol.JT_8201;
import com.ltmonitor.jt808.protocol.JT_8202;
import com.ltmonitor.jt808.protocol.JT_8203;
import com.ltmonitor.jt808.protocol.JT_8300;
import com.ltmonitor.jt808.protocol.JT_8301;
import com.ltmonitor.jt808.protocol.JT_8302;
import com.ltmonitor.jt808.protocol.JT_8303;
import com.ltmonitor.jt808.protocol.JT_8304;
import com.ltmonitor.jt808.protocol.JT_8400;
import com.ltmonitor.jt808.protocol.JT_8401;
import com.ltmonitor.jt808.protocol.JT_8500;
import com.ltmonitor.jt808.protocol.JT_8600;
import com.ltmonitor.jt808.protocol.JT_8601;
import com.ltmonitor.jt808.protocol.JT_8602;
import com.ltmonitor.jt808.protocol.JT_8603;
import com.ltmonitor.jt808.protocol.JT_8604;
import com.ltmonitor.jt808.protocol.JT_8605;
import com.ltmonitor.jt808.protocol.JT_8606;
import com.ltmonitor.jt808.protocol.JT_8607;
import com.ltmonitor.jt808.protocol.JT_8700;
import com.ltmonitor.jt808.protocol.JT_8701;
import com.ltmonitor.jt808.protocol.JT_8801;
import com.ltmonitor.jt808.protocol.JT_8802;
import com.ltmonitor.jt808.protocol.JT_8803;
import com.ltmonitor.jt808.protocol.JT_8804;
import com.ltmonitor.jt808.protocol.JT_8805;
import com.ltmonitor.jt808.protocol.JT_8900;
import com.ltmonitor.jt808.protocol.ParameterItem;
import com.ltmonitor.jt808.protocol.PhoneNoItem;
import com.ltmonitor.jt808.protocol.PointcastMessageItem;
import com.ltmonitor.jt808.protocol.PolygonNodeItem;
import com.ltmonitor.jt808.protocol.Recorder_DriverVehicleCode;
import com.ltmonitor.jt808.protocol.Recorder_FeatureFactor;
import com.ltmonitor.jt808.protocol.Recorder_RealTimeClock;
import com.ltmonitor.jt808.protocol.Recorder_VehicleLicenseInfo;
import com.ltmonitor.jt808.protocol.RectangleAreaItem;
import com.ltmonitor.jt808.protocol.RouteTurnPointItem;
import com.ltmonitor.jt808.protocol.T808Message;
import com.ltmonitor.jt808.protocol.T808MessageHeader;
import com.ltmonitor.jt808.service.ICommandHandler;
import com.ltmonitor.jt808.service.ICommandService;
import com.ltmonitor.jt808.tool.Tools;
import com.ltmonitor.service.JT808Constants;
import com.ltmonitor.service.MapFixService;
import com.ltmonitor.util.DateUtil;

//给终端下发命令的服务
public class CommandService implements ICommandService {
	private static Logger logger = Logger.getLogger(CommandService.class);
	/**
	 * 事件IDMap，当下发事件时，将事件存放在内存中，留待终端事件报告时，比对事件ID是否存在
	 */
	private static Map<Integer, String> eventIdMap = new HashMap<Integer, String>();

	/**
	 * 行驶记录仪版本 2003,2012两种,默认是2003
	 */
	private String vehicleRecorderVersion;
	private IBaseDao baseDao;

	public final IBaseDao getBaseDao() {
		return baseDao;
	}

	public final void setBaseDao(IBaseDao value) {
		baseDao = value;
	}

	private ICommandHandler commandHandler;

	public final ICommandHandler getOnRecvCommand() {
		return commandHandler;
	}

	public final void setOnRecvCommand(ICommandHandler value) {
		commandHandler = value;
	}

	private Thread parseThread;
	private boolean IsContinue = true;
	// 访问数据库时间间隔
	private int interval;

	public final int getInterval() {
		return interval;
	}

	public final void setInterval(int value) {
		interval = value;
	}
	/**
	 * 事件上报时，根据Id得到事件内容
	 * @param eventId
	 * @return
	 */
	public String getEventContent(int eventId)
	{
		return this.eventIdMap.get(eventId);
	}

	public CommandService() {
		setInterval(1000); // 默认1s
	}

	// 启动命令解析线程，自动解析命令，并发送给终端

	public final void Start() {
		IsContinue = true;
		logger.info("启动监听客户端命令线程");
		parseThread = new Thread(new Runnable() {
			public void run() {
				ParseCommandThreadFunc();
			}
		});
		parseThread.start();

	}

	public final void Stop() {
		IsContinue = false;
		try {
			parseThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void ParseCommandThreadFunc() {
		logger.info("开始监听客户端命令");
		while (IsContinue) {
			try {
				ParseCommand();
			} catch (RuntimeException ex) {
				logger.error(ex.getMessage());
				logger.error(ex.getStackTrace());
			}
			try {
				Thread.sleep(getInterval());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 解析数据库的命令，并调用通信服务器，发送给终端
	 */
	public final void ParseCommand() {
		String hsql = "from TerminalCommand where CreateDate > ? and Status = ? ";
		Date startTime = DateUtil.getDate(DateUtil.now(), Calendar.MINUTE, -5);
		java.util.List result = getBaseDao().query(hsql,
				new Object[] { startTime, TerminalCommand.STATUS_NEW });

		for (Object obj : result) {
			TerminalCommand tc = (TerminalCommand) obj;
			T808Message tm = null;
			try {
				tm = Parse(tc);
				if (tm != null) {
					boolean rs = commandHandler.OnRecvCommand(tm, tc);
					// tc.setStatus(rs ? "发送成功" : "发送失败");
				} else {
					tc.setStatus(TerminalCommand.STATUS_INVALID);
				}
				tc.setUpdateDate(new Date());
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
				tc.setStatus(TerminalCommand.STATUS_INVALID);
				tc.setRemark(ex.getMessage());
			}
			UpdateCommand(tc);
		}
	}

	/**
	 * 根据消息的流水号来更新状态
	 */
	public final TerminalCommand UpdateStatus(String GpsId, int SN,
			String status) {
		try {
			String hsql = "from TerminalCommand where simNo = ? and  SN = ? and CreateDate > ?";
			TerminalCommand tc = (TerminalCommand) getBaseDao()
					.find(hsql,
							new Object[] {
									GpsId,
									SN,
									DateUtil.getDate(DateUtil.now(),
											Calendar.HOUR, -1) });
			if (tc != null) {
				tc.setStatus(status);
				UpdateCommand(tc);
			}
			return tc;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return null;
	}

	/**
	 * 得到最近下发的某一个命令
	 * 
	 * @param cmdType
	 * @return
	 */
	public final TerminalCommand getLatestCommand(int cmdType,String simNo) {
		try {
			String hsql = "from TerminalCommand where cmdType = ? and simNo = ? and createDate > ? order by createDate desc";
			TerminalCommand tc = (TerminalCommand) getBaseDao()
					.find(hsql,
							new Object[] {
									cmdType, simNo,
									DateUtil.getDate(DateUtil.now(),
											Calendar.HOUR, -1) });
			return tc;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return null;
	}

	/**
	 * 更新命令的执行状态
	 */
	public final void UpdateCommand(TerminalCommand tc) {
		try {
			tc.setUpdateDate(new Date());
			getBaseDao().saveOrUpdate(tc);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

	}

	/**
	 * 根据流水号找到命令
	 * 
	 * @param sn
	 * @return
	 */
	@Override
	public TerminalCommand getCommandBySn(int sn) {
		String hsql = "from TerminalCommand where SN = ? and createDate > ? order by createDate desc";
		Date startDate = DateUtil.getDate(new Date(), Calendar.HOUR_OF_DAY, -1);
		TerminalCommand tc = (TerminalCommand) this.baseDao.find(hsql,
				new Object[] { sn, startDate });
		return tc;
	}

	// 解析区域设置命令
	private T808Message ParseAreaConfig(TerminalCommand tc) {
		T808Message ts = new T808Message();
		ts.setHeader(new T808MessageHeader());
		ts.getHeader().setSimId("0" + tc.getSimNo());
		ts.getHeader().setMessageSerialNo((short) T808Manager.getSerialNo());
		ts.getHeader().setIsPackage(false);
		ts.getHeader().setMessageType(tc.getCmdType()); // 命令字
		tc.setStatus(TerminalCommand.STATUS_PROCESSING);
		tc.setSN(ts.getHeader().getMessageSerialNo());

		java.util.ArrayList<Enclosure> enclosureList = new java.util.ArrayList<Enclosure>();
		ArrayList enclosureIds = new ArrayList();
		if (Tools.isNullOrEmpty(tc.getCmdData()) == false) {
			String[] strEnclosureIds = tc.getCmdData().split("[;]", -1);
			for (String enclosureId : strEnclosureIds) {
				if (StringHelper.isNullOrEmpty(enclosureId) == false) {
					int enId = Integer.parseInt(enclosureId);
					Enclosure ec = (Enclosure) getBaseDao().load(
							Enclosure.class, enId);
					enclosureList.add(ec);
					enclosureIds.add(enId);
				}
			}
		}
		if (tc.getCmdType() == JT808Constants.CMD_CIRCLE_CONFIG) {
			JT_8600 cmdData = new JT_8600();
			ts.setMessageContents(cmdData);
			cmdData.setAreasCount((byte) 1);
			byte settingType = Byte.parseByte(tc.getCmd());
			cmdData.setSettingType(settingType);
			cmdData.setCircleAreas(new java.util.ArrayList<CircleAreaItem>());

			for (Enclosure ec : enclosureList) {
				CircleAreaItem ci = new CircleAreaItem();
				ci.setCircleAreaId((int) ec.getEntityId());
				ci.setCircleAreaName(ec.getName());
				ci.setCircleAreaNameLength((byte) StringHelper.getByteLength(ec
						.getName())); // 名称不能超过byte的长度
				ci.setCircleAreaProperty(ec.CreateAreaAttr());
				ci.setStartTime(ec.getStartDate());
				ci.setEndTime(ec.getEndDate());
				ci.setRadius((int) ec.getRadius());
				ci.setMaxSpeed((short) ec.getMaxSpeed());
				ci.setOverSpeedLastTime((byte) ec.getDelay());

				java.util.ArrayList<EnclosureNode> nodes = ec.GetNodes();

				if (nodes.size() > 0) {
					EnclosureNode node = nodes.get(0);
					ci.setCenterLatitude((int) (node.getLat() * 1000000));
					ci.setCenterLongitude((int) (node.getLng() * 1000000));
				}
				cmdData.getCircleAreas().add(ci);
			}
		} else if (tc.getCmdType() == JT808Constants.CMD_RECT_CONFIG) {
			JT_8602 cmdData = new JT_8602();
			ts.setMessageContents(cmdData);
			cmdData.setAreasCount((byte) 1);
			byte settingType = Byte.parseByte(tc.getCmd());
			cmdData.setSettingType(settingType);
			cmdData.setRectangleAreas(new java.util.ArrayList<RectangleAreaItem>());

			for (Enclosure ec : enclosureList) {
				RectangleAreaItem ci = new RectangleAreaItem();
				ci.setRectangleAreaId((int) ec.getEntityId());
				ci.setRectangleAreaName(ec.getName());
				ci.setRectangleAreaNameLength((byte) StringHelper
						.getByteLength(ec.getName())); // 名称不能超过byte的长度
				ci.setRectangleAreaProperty(ec.CreateAreaAttr());
				ci.setStartTime(ec.getStartDate());
				ci.setEndTime(ec.getEndDate());
				ci.setMaxSpeed((short) ec.getMaxSpeed());
				ci.setOverSpeedLastTime((byte) ec.getDelay());

				java.util.ArrayList<EnclosureNode> nodes = ec.GetNodes();

				if (nodes.size() > 0) {
					EnclosureNode node = nodes.get(0);
					ci.setLeftTopLatitude((int) (node.getLat() * 1000000));
					ci.setLeftTopLongitude((int) (node.getLng() * 1000000));

					node = nodes.get(2);
					ci.setRightBottomLatitude((int) (node.getLat() * 1000000));
					ci.setRightBottomLongitude((int) (node.getLng() * 1000000));
				}
				cmdData.getRectangleAreas().add(ci);
			}
		} else if (tc.getCmdType() == JT808Constants.CMD_POLYGON_CONFIG) {
			JT_8604 ci = new JT_8604();
			ts.setMessageContents(ci);
			Enclosure ec = enclosureList.get(0);
			ci.setPolygonAreaId((int) ec.getEntityId());
			ci.setPolygonAreaName(ec.getName());
			ci.setPolygonAreaNameLength((byte) StringHelper.getByteLength(ec
					.getName())); // 名称不能超过byte的长度
			ci.setPolygonAreaProperty(ec.CreateAreaAttr());
			ci.setStartTime(ec.getStartDate());
			ci.setEndTime(ec.getEndDate());
			ci.setMaxSpeed((short) ec.getMaxSpeed());
			ci.setOverSpeedLastTime((byte) ec.getDelay());
			java.util.ArrayList<EnclosureNode> nodes = ec.GetNodes();
			ci.setAreaNodesCount((short) nodes.size());
			ci.setNodes(new java.util.ArrayList<PolygonNodeItem>());
			for (EnclosureNode node : nodes) {
				PolygonNodeItem item = new PolygonNodeItem();
				item.setLatitude((int) (node.getLat() * 1000000));
				item.setLongitude((int) (node.getLng() * 1000000));
				ci.getNodes().add(item);
			}
		} else if (tc.getCmdType() == JT808Constants.CMD_ROUTE_CONFIG) {
			JT_8606 ci = new JT_8606();
			ts.setMessageContents(ci);
			Enclosure ec = enclosureList.get(0);
			ci.setRouteId((int) ec.getEntityId());
			ci.setRouteName(ec.getName());
			ci.setRouteNameLength((byte) StringHelper.getByteLength(ec
					.getName())); // 名称不能超过byte的长度
			ci.setRouteProperty(ec.CreateAreaAttr());
			ci.setStartTime(ec.getStartDate());
			ci.setEndTime(ec.getEndDate());
			String hsql = "from LineSegment where EnclosureId = ? order by PointId";
			java.util.List nodes = (java.util.ArrayList) getBaseDao().query(
					hsql, new Object[] { ec.getEntityId() });
			ci.setRoutePointsCount((short) nodes.size());
			ci.setTurnPoints(new java.util.ArrayList<RouteTurnPointItem>());
			for (Object obj : nodes) {
				LineSegment node = (LineSegment) obj;
				// 下发终端前，坐标要纠偏，转换到wgs84坐标
				PointLatLng pt = MapFixService.gcjToWgs(node.getLatitude1(),
						node.getLongitude1());
				// pl.setLat(pt.getLat());
				// pl.setLng(pt.getLng());

				RouteTurnPointItem item = new RouteTurnPointItem();
				item.setRoutePointId((int) node.getEntityId());
				item.setRouteSegmentId((int) ec.getEntityId());
				item.setRouteSegmentProperty(node.CreateAreaAttr());
				item.setTurnPointLatitude((int) (pt.getLat() * 1000000));
				item.setTurnPointLongitude((int) (pt.getLng() * 1000000));
				item.setRouteSegmentWidth((byte) node.getLineWidth());
				item.setMaxDriveTimeLimited((byte) node.getMaxTimeLimit());
				item.setMinDriveTimeLimited((byte) node.getMinTimeLimit());
				item.setMaxSpeedLimited((byte) node.getMaxSpeed());
				item.setOverMaxSpeedLastTime((byte) node.getOverSpeedTime());
				ci.getTurnPoints().add(item);
			}
		} else if (tc.getCmdType() == JT808Constants.CMD_DELETE_ROUTE) {
			JT_8607 ci = new JT_8607();
			ci.setRoutesCount((byte) enclosureIds.size());
			ci.setRouteIDs(enclosureIds);
			ts.setMessageContents(ci);
		} else if (tc.getCmdType() == JT808Constants.CMD_DELETE_CIRCLE) {
			JT_8601 ci = new JT_8601();
			ci.setCircleAreasCount((byte) enclosureIds.size());
			ci.setCircleAreaIDs(enclosureIds);
			ts.setMessageContents(ci);
		} else if (tc.getCmdType() == JT808Constants.CMD_DELETE_RECT) {
			JT_8603 ci = new JT_8603();
			ci.setCircleAreasCount((byte) enclosureIds.size());
			ci.setCircleAreaIDs(enclosureIds);
			ts.setMessageContents(ci);
		} else if (tc.getCmdType() == JT808Constants.CMD_DELETE_POLYGON) {
			JT_8605 ci = new JT_8605();
			ci.setPolygonAreasCount((byte) enclosureIds.size());
			ci.setPolygonAreaIDs(enclosureIds);
			ts.setMessageContents(ci);
		}

		return ts;
	}

	/**
	 * 不对非法命令格式进行解析，在命令录入时确保格式正确
	 * 
	 * @param tc
	 * @return
	 * @throws Exception
	 */
	private final T808Message Parse(TerminalCommand tc) throws Exception {
		if (Tools.isNullOrEmpty(tc.getPlateNo()) == false
				&& Tools.isNullOrEmpty(tc.getSimNo())) {
			String hql = "from VehicleData where plateNo = ?";
			VehicleData vd = (VehicleData) this.baseDao.find(hql,
					tc.getPlateNo());
			if (vd != null) {
				tc.setSimNo(vd.getSimNo());
			} else {
				tc.setRemark("找不到该车辆");
				return null;
			}
		}
		// 单独对电子围栏的命令进行解析
		if (tc.getCmdType() == JT808Constants.CMD_POLYGON_CONFIG
				|| tc.getCmdType() == JT808Constants.CMD_RECT_CONFIG
				|| tc.getCmdType() == JT808Constants.CMD_CIRCLE_CONFIG
				|| tc.getCmdType() == JT808Constants.CMD_ROUTE_CONFIG
				|| tc.getCmdType() == JT808Constants.CMD_DELETE_ROUTE
				|| tc.getCmdType() == JT808Constants.CMD_DELETE_CIRCLE
				|| tc.getCmdType() == JT808Constants.CMD_DELETE_RECT
				|| tc.getCmdType() == JT808Constants.CMD_DELETE_POLYGON) {
			T808Message tmsg = ParseAreaConfig(tc);
			return tmsg;
		}
		T808Message ts = new T808Message();
		ts.setHeader(new T808MessageHeader());
		ts.getHeader().setSimId("0" + tc.getSimNo());
		// ts.getHeader().setMessageSerialNo((short)T808Manager.getSerialNo());
		ts.getHeader().setIsPackage(false);
		tc.setStatus(TerminalCommand.STATUS_PROCESSING);
		tc.setSN(ts.getHeader().getMessageSerialNo());
		ts.getHeader().setMessageType(tc.getCmdType()); // 命令字
		if (tc.getCmdType() == JT808Constants.CMD_REAL_MONITOR) {
			// 位置信息查询-点名
			JT_8201 cmdData = new JT_8201();
			ts.setMessageContents(cmdData);
		} else if (tc.getCmdType() == JT808Constants.CMD_MEDIA_SEARCH) {
			// 存储多媒体检索命令
			JT_8802 cmdData = new JT_8802();
			ts.setMessageContents(cmdData);
			String[] fields = tc.getCmdData().split("[;]", -1);
			cmdData.setMultimediaType(Byte.parseByte(fields[0]));
			cmdData.setChannelId(Byte.parseByte(fields[1]));
			cmdData.setEventCode(Byte.parseByte(fields[2]));
			Date startDate = DateUtil.toDateByFormat(fields[3], "yyyy-MM-dd HH:mm:ss");
			cmdData.setStartTime(startDate);
			Date endDate = DateUtil.toDateByFormat(fields[4], "yyyy-MM-dd HH:mm:ss");
			cmdData.setEndTime(endDate);
			//cmdData.setStartTime(new java.util.Date(java.util.Date
					//.parse(fields[3])));
			//cmdData.setEndTime(new java.util.Date(java.util.Date
					//.parse(fields[4])));
		} else if (tc.getCmdType() == JT808Constants.CMD_MEDIA_UPLOAD) {
			// 存储多媒体上传命令
			JT_8803 cmdData = new JT_8803();
			ts.setMessageContents(cmdData);
			String[] fields = tc.getCmdData().split("[;]", -1);
			cmdData.setMultimediaType(Byte.parseByte(fields[0]));
			cmdData.setChannelId(Byte.parseByte(fields[1]));
			cmdData.setEventCode(Byte.parseByte(fields[2]));
			Date startDate = DateUtil.toDateByFormat(fields[3], "yyyy-MM-dd HH:mm:ss");
			cmdData.setStartTime(startDate);
			Date endDate = DateUtil.toDateByFormat(fields[4], "yyyy-MM-dd HH:mm:ss");
			cmdData.setEndTime(endDate);
			cmdData.setDeleteFlag(Byte.parseByte(fields[5]));
		} else if (tc.getCmdType() == JT808Constants.CMD_MEDIA_UPLOAD_SINGLE) {
			// 单条存储多媒体上传命令
			JT_8805 cmdData = new JT_8805();
			ts.setMessageContents(cmdData);
			String[] fields = tc.getCmdData().split("[;]", -1);
			cmdData.setMultimediaId(Byte.parseByte(fields[0]));
			cmdData.setDeleteFlag(Byte.parseByte(fields[1]));
		} else if (tc.getCmdType() == JT808Constants.CMD_LOCATION_MONITOR) {
			// 位置跟踪
			// 时间间隔 WORD 位置跟踪有效期 DWORD
			JT_8202 cmdData = new JT_8202();
			ts.setMessageContents(cmdData);
			String[] fields = tc.getCmdData().split("[;]", -1);
			cmdData.setTimeInterval(Short.parseShort(fields[0]));
			cmdData.setTrackExpire(Short.parseShort(fields[1]));
		} else if (tc.getCmdType() == JT808Constants.CMD_SEND_TEXT) {
			// 文本信息下发
			// *
			// * 0 标志 BYTE 文本信息标志位含义见表27
			// 1 文本信息 STRING 最长为102字节，经GBK编码
			// 　　表27文本信息、标志位含义
			// 位 标志
			// 0 1：紧急
			// 1 保留
			// 2 1：终端显示器显示
			// 3 1：终端TTS播读
			// 4 1：广告屏显示
			// 5-7 保留
			//
			JT_8300 cmdData = new JT_8300();
			ts.setMessageContents(cmdData);
			String[] fields = tc.getCmdData().split("[;]", -1);

			cmdData.setFlag(Byte.parseByte(fields[0]));
			cmdData.setText(fields[1]);

		} else if (tc.getCmdType() == JT808Constants.CMD_CONFIG_PARAM) {
			// 设置终端参数
			JT_8103 cmdData = new JT_8103();
			ts.setMessageContents(cmdData);
			String[] fields = tc.getCmdData().split("[;]", -1);

			java.util.ArrayList<ParameterItem> paramItems = new java.util.ArrayList<ParameterItem>();
			for (String field : fields) {
				if (StringHelper.isNullOrEmpty(field) == false) {
					String[] items = field.split("[,]", -1);
					ParameterItem pi = new ParameterItem();
					pi.setParameterId(Integer.parseInt(items[0]));
					pi.setParameterValue(items[1]);
					paramItems.add(pi);
				}
			}
			cmdData.setParameters(paramItems);
			cmdData.setParametersCount((byte) paramItems.size());
		} else if (tc.getCmdType() == JT808Constants.CMD_QUERY_PARAM) {
			// 查询终端参数
			JT_8104 cmdData = new JT_8104();
			ts.setMessageContents(cmdData);
			String[] fields = tc.getCmdData().split("[;]", -1);
			// C# TO JAVA CONVERTER WARNING: Unsigned integer types have no
			// direct equivalent in Java:
			// ORIGINAL LINE: List<uint> parameters = new List<uint>();
			java.util.ArrayList<Integer> parameters = new java.util.ArrayList<Integer>();
			for (String field : fields) {
				if (StringHelper.isNullOrEmpty(field) == false) {
					String[] strP = field.split("[,]", -1);
					parameters.add(Integer.parseInt(strP[0]));
				}
			}
			cmdData.setParametersIDs(parameters);
			cmdData.setParametersCount((byte) parameters.size());
		} else if (tc.getCmdType() == JT808Constants.CMD_CONTROL_TERMINAL) {
			// 终端控制
			JT_8105 cmdData = new JT_8105();
			ts.setMessageContents(cmdData);
			// string[] fields = tc.getCmdData().split(';');
			cmdData.setCommandWord(Byte.parseByte(tc.getCmd())); // 命令字
			cmdData.setCommandParameters(tc.getCmdData());
		} else if (tc.getCmdType() == JT808Constants.CMD_TAKE_PHOTO) {
			// 摄像头立即上传命令
			JT_8801 cmdData = new JT_8801();
			ts.setMessageContents(cmdData);
			String[] fields = tc.getCmdData().split("[;]", -1);
			cmdData.setChannelId(Byte.parseByte(fields[0]));
			short cmdWord = (short) Integer.parseInt(fields[1]);// 录像命令是0xFFFF，超过short值
			cmdData.setPhotoCommand(cmdWord);
			cmdData.setPhotoTimeInterval(Short.parseShort(fields[2]));
			cmdData.setStoreFlag(Byte.parseByte(fields[3]));
			cmdData.setResolution(Byte.parseByte(fields[4]));
			cmdData.setQuality(Byte.parseByte(fields[5]));
			cmdData.setBrightness((byte) (Integer.parseInt(fields[6])));
			cmdData.setContrast((byte) (Integer.parseInt(fields[7])));

			cmdData.setSaturation((byte) (Integer.parseInt(fields[8])));
			cmdData.setChroma((byte) (Integer.parseInt(fields[9])));
			// *
			// * 通道ID BYTE >0
			// 拍摄命令 WORD 0表示停止拍摄；0xFFFF表示录像；其他表示拍照张数
			// 拍照间隔，录像时间 WORD 秒，0表示按最小间隔拍照或一直录像
			// 保持标志 BYTE 1：保存；0：实时上传
			// 分辨率 BYTE 0x01:320*210;
			// 0x02:640*480:
			// 0x03:800*600;
			// 0x04:1024*768;
			// 0x05:176*144;[Qcif];
			// 0x06:352*288;[Cif];
			// 0x07:704*288;[HALF D1];
			// 0x08:701*576;[D1];
			// 图像/视频质量 BYTE 1-10, 1代表质量损失最小，10表示压缩比最大
			// 亮度 BYTE 0-255
			// 对比度 BYTE 0-127
			// 饱和度 BYTE 0-127
			// 色度 BYTE 0-255
			//
		} else if (tc.getCmdType() == JT808Constants.CMD_EVENT_SET) {
			// 设置事件
			JT_8301 cmdData = new JT_8301();
			cmdData.setSettingType(Byte.parseByte(tc.getCmd()));
			ts.setMessageContents(cmdData);
			String[] fields = tc.getCmdData().split("[;]", -1);

			java.util.ArrayList<EventSettingItem> paramItems = new java.util.ArrayList<EventSettingItem>();
			for (String field : fields) {
				if (StringHelper.isNullOrEmpty(field)) {
					continue;
				}

				String[] items = field.split("[,]", -1);
				EventSettingItem pi = new EventSettingItem();
				pi.setEventId(Byte.parseByte(items[0]));
				pi.setEventContent(items[1]); // GBK编码
				
				eventIdMap.put((int)pi.getEventId(), pi.getEventContent());
				// pi.EventLength = byte.Parse(items[1]);
				paramItems.add(pi);
			}
			cmdData.setEvents(paramItems);
			cmdData.setEventsCount((byte) paramItems.size());
		} else if (tc.getCmdType() == JT808Constants.CMD_QUESTION) {
			// 提问下发
			JT_8302 cmdData = new JT_8302();
			cmdData.setFlag(Byte.parseByte(tc.getCmd())); // 标志
			ts.setMessageContents(cmdData);
			String[] fields = tc.getCmdData().split("[;]", -1);

			java.util.ArrayList<AnswerItem> paramItems = new java.util.ArrayList<AnswerItem>();
			for (String field : fields) {
				if (StringHelper.isNullOrEmpty(field)) {
					continue;
				}
				String[] items = field.split("[,]", -1);
				AnswerItem pi = new AnswerItem();
				pi.setAnswerId(Byte.parseByte(items[0]));
				pi.setAnswerLength((byte) StringHelper.getByteLength(items[1]));
				pi.setAnswerContent(items[1]); // GBK编码
				paramItems.add(pi);
			}
			cmdData.setCandidateAnswers(paramItems);
			cmdData.setQuestion(tc.getRemark()); // 问题
			// cmdData.QuestionLength = (byte)tc.Remark.Length; //GBK编码，需要占用两个字节
		} else if (tc.getCmdType() == JT808Constants.CMD_SET_MENU) {
			// 菜单设置
			JT_8303 cmdData = new JT_8303();
			cmdData.setSettingType(Byte.parseByte(tc.getCmd())); // 标志
			ts.setMessageContents(cmdData);
			java.util.ArrayList<PointcastMessageItem> paramItems = new java.util.ArrayList<PointcastMessageItem>();
			if (StringUtil.isNullOrEmpty(tc.getCmdData()) == false) {
				String[] fields = tc.getCmdData().split("[;]", -1);
				for (String field : fields) {
					if (StringHelper.isNullOrEmpty(field)) {
						continue;
					}
					String[] items = field.split("[,]", -1);
					PointcastMessageItem pi = new PointcastMessageItem();
					pi.setMessageType(Byte.parseByte(items[0]));
					pi.setMessage(items[1]); // GBK编码
					paramItems.add(pi);
				}
				cmdData.setMessages(paramItems);
			}
			cmdData.setInfoItemsCount((byte) paramItems.size()); // GBK编码，需要占用两个字节
		} else if (tc.getCmdType() == JT808Constants.CMD_INFORMATION) {
			// 信息服务
			JT_8304 cmdData = new JT_8304();
			ts.setMessageContents(cmdData);
			if (StringHelper.isNullOrEmpty(tc.getCmdData()))
				throw new Exception("命令内容不能为空");
			String[] fields = tc.getCmdData().split("[;]", -1);
			cmdData.setMessageType(Byte.parseByte(fields[0]));
			
			cmdData.setMessage(fields[1]);
		} else if (tc.getCmdType() == JT808Constants.CMD_DIAL_BACK) {
			// 电话回拨
			JT_8400 cmdData = new JT_8400();
			ts.setMessageContents(cmdData);
			String[] fields = tc.getCmdData().split("[;]", -1);
			cmdData.setFlag(Byte.parseByte(fields[0]));
			cmdData.setPhoneNo(fields[1]);
		} else if (tc.getCmdType() == JT808Constants.CMD_PHONE_BOOK) {
			// 电话本设置
			JT_8401 cmdData = new JT_8401();
			cmdData.setSettingType(Byte.parseByte(tc.getCmd())); // 标志
			ts.setMessageContents(cmdData);
			String[] fields = tc.getCmdData().split("[;]", -1);

			java.util.ArrayList<PhoneNoItem> paramItems = new java.util.ArrayList<PhoneNoItem>();
			for (String field : fields) {
				if (StringHelper.isNullOrEmpty(field)) {
					continue;
				}
				String[] items = field.split("[,]", -1);
				PhoneNoItem pi = new PhoneNoItem();
				pi.setDailFlag(Byte.parseByte(items[0]));
				// pi.PhoneNoLength = byte.Parse(items[1]);
				pi.setPhoneNo(items[1]);
				// pi.ContactLength = byte.Parse(items[3]);
				pi.setContact(items[2]); // GBK编码
				paramItems.add(pi);
			}
			cmdData.setContacts(paramItems);
			cmdData.setPhoneNosCount((byte) paramItems.size());
		} else if (tc.getCmdType() == JT808Constants.CMD_CONTROL_VEHICLE) {
			// 车辆控制
			JT_8500 cmdData = new JT_8500();
			ts.setMessageContents(cmdData);
			String[] fields = tc.getCmdData().split("[;]", -1);
			cmdData.setFlag(Byte.parseByte(fields[0]));
		} else if (tc.getCmdType() == JT808Constants.CMD_AUDIO_RECORDER) {
			// 录音开始命令
			JT_8804 cmdData = new JT_8804();
			ts.setMessageContents(cmdData);
			String[] fields = tc.getCmdData().split("[;]", -1);
			cmdData.setRecordCommad(Byte.parseByte(fields[0]));
			cmdData.setRecordTimePeriod(Short.parseShort(fields[1]));
			cmdData.setStoreFlag(Byte.parseByte(fields[2]));
			cmdData.setFrequency(Byte.parseByte(fields[3]));
		} else if (tc.getCmdType() == JT808Constants.CMD_VEHICLE_RECORDER) {
			// 行车记录仪数据采集命令
			JT_8700 cmdData = new JT_8700();
			ts.setMessageContents(cmdData);
			String[] fields = tc.getCmdData().split("[;]", -1);
			cmdData.setCommandWord(Byte.parseByte(fields[0])); // 命令字
			// cmdDat = uShort.parseShort(fields[1]);
			cmdData.setRepassPacketsCount((byte) 0); // 不需要重传包
			
			if(vehicleRecorderVersion!=null && vehicleRecorderVersion.equals("2012"))
			{
				
			}
		} else if (tc.getCmdType() == JT808Constants.CMD_VEHICLE_RECORDER_CONFIG) {
			// 行车记录参数下传命令
			JT_8701 cmdData = new JT_8701();
			ts.setMessageContents(cmdData);
			String[] fields = tc.getCmdData().split("[;]", -1);

			cmdData.setCommandWord((byte) Integer.parseInt(fields[0])); // 命令字
			// cmdDat = uShort.parseShort(fields[1]);
			Integer cmdWord = Integer.parseInt(fields[0]);
			if (cmdWord == 0x81) {
				// 设定的驾驶员代码及其对应的机动车驾驶证号码
				Recorder_DriverVehicleCode rd = new Recorder_DriverVehicleCode();
				cmdData.setData(rd);
				rd.setDriverCode(fields[1]);
				rd.setDriverLicenseNo(fields[2]);
			} else if (cmdWord == 0x82) {
				// 存储的车辆VIN号、车牌号码、分类
				Recorder_VehicleLicenseInfo rd = new Recorder_VehicleLicenseInfo();
				rd.setVinNo(fields[1]);
				rd.setVehicleLicenseNo(fields[2]);
				rd.setPlateType(fields[3]);
				cmdData.setData(rd);
			} else if (cmdWord == 0xC2) {
				// 记录仪的实时时钟 YY-MM-DD-hh-mm-ss
				Recorder_RealTimeClock rd = new Recorder_RealTimeClock();
				rd.setRealTimeClock(new java.util.Date(java.util.Date
						.parse(fields[1])));
				cmdData.setData(rd);
			} else if (cmdWord == 0xC3) {
				// 设定的车辆特征系数 (高中低字节)，对应车辆车速传感器系数设置
				Recorder_FeatureFactor rd = new Recorder_FeatureFactor();
				rd.setFeatureFactor(Integer.parseInt(fields[1]));
				cmdData.setData(rd);
			} else {
				throw new Exception("非法的命令字:" + cmdWord);
			}

		} else if (tc.getCmdType() == JT808Constants.CMD_TRANS) {
			// 透明传输
			JT_8900 cmdData = new JT_8900();
			ts.setMessageContents(cmdData);
			String[] fields = tc.getCmdData().split("[;]", -1);
			String format = fields[0];
			String messageCotent = fields[2];
			byte[] messageBytes = null;
			if (format == "hex") {
				// 16进制字符串
				messageBytes = Tools.HexString2Bytes(messageCotent);
			} else {
				messageBytes = BitConverter.getBytes(messageCotent);
				messageBytes = Base64.encodeBase64(messageBytes);
			}
			cmdData.setMessageType(Byte.parseByte(fields[1]));

			cmdData.setMessageContent(messageBytes);
		}else if (tc.getCmdType() == JT808Constants.CMD_CLEAR_ALARM) {

			String[] fields = tc.getCmdData().split("[;]", -1);
			int sn = Integer.parseInt(fields[0]);
			int msgType = Integer.parseInt(fields[1]);
			int ackResult = Integer.parseInt(fields[2]);
			/**
			JT_8001 cmdData = new JT_8001();
			ts.setMessageContents(cmdData);
			cmdData.setResponseMessageSerialNo((short)sn);
			cmdData.setResponseMessageId((short) msgType);
			cmdData.setResponseResult((byte) ackResult); // 应答成功
			*/
			JT_8203 cmdData = new JT_8203();
			ts.getHeader().setMessageType(0x8203);
			ts.setMessageContents(cmdData);
			cmdData.setResponseMessageSerialNo((short)sn);
			cmdData.setAlarmType(1);//紧急报警确认
		} else {
			tc.setStatus(TerminalCommand.STATUS_INVALID);
		}
		// BaseDao.saveOrUpdate(tc); //将流水号更新到命令表中
		return ts;
	}
}