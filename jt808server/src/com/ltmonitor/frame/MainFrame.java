package com.ltmonitor.frame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import com.ltmonitor.app.GlobalConfig;
import com.ltmonitor.app.GpsConnection;
import com.ltmonitor.app.ServiceLauncher;
import com.ltmonitor.entity.GPSRealData;
import com.ltmonitor.entity.StringUtil;
import com.ltmonitor.entity.VehicleData;
import com.ltmonitor.jt808.protocol.T808Message;
import com.ltmonitor.jt808.service.IGpsDataService;
import com.ltmonitor.jt808.service.IT808Manager;
import com.ltmonitor.jt808.tool.Tools;
import com.ltmonitor.service.IRealDataService;
import com.ltmonitor.service.JT808Constants;
import com.ltmonitor.util.DateUtil;

public class MainFrame extends JFrame {
	private JMenu jMenu3;
	private JScrollPane jScrollPane1;
	private JButton btnConnect;
	private JTextField textField_Port;
	private JLabel lblNewLabel;
	private JLabel statisticLabel;
	private JButton btnStopServer;
	private JScrollPane scrollPane;
	private JTable table;
	private JButton buttonClearLog;
	private JCheckBox checkBox_HideHeartLog;

	private JCheckBox checkBox_displayConnection;
	// 是否不显示心跳日志
	private boolean hideHeartBeatLog = false;
	private JScrollPane scrollPane_1;
	private JTable table_1;
	private JSplitPane splitPane_1;
	private JScrollPane scrollPane_2;
	public JTextArea dataArea;

	private long timerCount = 0;
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenuItem mntmNewMenuItem;
	private JMenu mnNewMenu_1;
	private JMenuItem mntmNewMenuItem_1;
	private JMenuItem mntmNewMenuItem_2;
	private JMenuItem mntmNewMenuItem_3;
	private JMenuItem mntmNewMenuItem_4;
	private JMenuItem mntmNewMenuItem_5;
	private JMenu mnNewMenu_2;
	private JMenu mnNewMenu_3;
	private JMenuItem mntmNewMenuItem_6;
	private JMenu mnNewMenu_4;
	private JButton btnNewButton;
	private JTextField textFilterBySimNo;
	private JLabel label;

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MainFrame.class);
	public MainFrame() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if (MainFrame.this.splitPane_1 != null)
					MainFrame.this.splitPane_1.setDividerLocation(0.6);
			}
		});
		setBackground(Color.LIGHT_GRAY);
		/**
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnNewMenu = new JMenu("\u7EC8\u7AEF\u8BBE\u7F6E(S)");
		menuBar.add(mnNewMenu);

		mntmNewMenuItem = new JMenuItem("New menu item");
		mnNewMenu.add(mntmNewMenuItem);

		mnNewMenu_1 = new JMenu("\u591A\u5A92\u4F53\u6307\u4EE4(M)");
		menuBar.add(mnNewMenu_1);

		mntmNewMenuItem_1 = new JMenuItem("\u62CD\u7167");
		mnNewMenu_1.add(mntmNewMenuItem_1);

		mntmNewMenuItem_2 = new JMenuItem("\u5F55\u97F3");
		mnNewMenu_1.add(mntmNewMenuItem_2);

		mntmNewMenuItem_3 = new JMenuItem(
				"\u5355\u6761\u591A\u5A92\u4F53\u68C0\u7D22");
		mnNewMenu_1.add(mntmNewMenuItem_3);

		mntmNewMenuItem_4 = new JMenuItem("\u591A\u5A92\u4F53\u67E5\u8BE2");
		mnNewMenu_1.add(mntmNewMenuItem_4);

		mntmNewMenuItem_5 = new JMenuItem("\u7535\u8BDD\u56DE\u62E8");
		mnNewMenu_1.add(mntmNewMenuItem_5);

		mnNewMenu_2 = new JMenu("\u63A7\u5236\u6307\u4EE4(C)");
		menuBar.add(mnNewMenu_2);

		mnNewMenu_3 = new JMenu("\u884C\u8F66\u8BB0\u5F55\u4EEA(T)");
		menuBar.add(mnNewMenu_3);

		mntmNewMenuItem_6 = new JMenuItem(
				"\u91C7\u96C6\u8BB0\u5F55\u4EEA\u65F6\u95F4");
		mnNewMenu_3.add(mntmNewMenuItem_6);

		mnNewMenu_4 = new JMenu("\u5173\u4E8E");
		menuBar.add(mnNewMenu_4);
		*/
		final Component c = this;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int option = JOptionPane.showConfirmDialog(c, "确定要退出吗？", " 提示",
						JOptionPane.OK_CANCEL_OPTION);
				if (JOptionPane.OK_OPTION == option) {

					IGpsDataService gpsService = (IGpsDataService) ServiceLauncher
							.getBean("gpsDataService");
					try {
						gpsService.resetGpsOnlineState();// 设置为离线
					} catch (Exception ex) {

					}
					t808Manager.StopServer();
				} else {

					MainFrame.this
							.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});

		initComponents();
		initParameter();
	}

	private void initParameter() {
		setTitle("\u7CBE\u81F4\u8F6F\u4EF6-JT/T 808 Gps\u670D\u52A1\u5668-www.ltmonitor.com");
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						"E:\\\u4EA4\u901A\u90E8\u68C0\u6D4B\\809Java\u7248\u672C\\809GNSS\\user_go.png"));
	}

	private void initComponents() {
		this.jMenu3 = new JMenu();
		this.jScrollPane1 = new JScrollPane();
		jScrollPane1
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		jScrollPane1
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setEnabled(false);
		jScrollPane1.setMinimumSize(new Dimension(1, 1));
		jScrollPane1.setMaximumSize(new Dimension(32767, 67));

		this.jMenu3.setText("jMenu3");

		setDefaultCloseOperation(3);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		getContentPane().add(jScrollPane1);

		scrollPane = new JScrollPane();
		getContentPane().add(scrollPane);

		table = new JTable();
		table.setFont(new Font("宋体", Font.PLAIN, 14));
		table.setModel(new DefaultTableModel(new Object[][] {
				{ null, null, null, null, null, null },
				{ null, null, null, null, null, null },
				{ null, null, null, null, null, null },
				{ null, null, null, null, null, null }, }, new String[] {
				"\u6D88\u606F\u7C7B\u578B", "Sim\u5361\u53F7",
				"\u8F66\u724C\u53F7", "\u6D88\u606F\u5185\u5BB9",
				"\u53D1\u9001\u65F6\u95F4",
				"\u539F\u59CB\u6570\u636E\u62A5\u6587" }));
		table.getColumnModel().getColumn(0).setPreferredWidth(180);
		table.getColumnModel().getColumn(0).setMaxWidth(380);
		table.getColumnModel().getColumn(1).setPreferredWidth(129);
		table.getColumnModel().getColumn(1).setMaxWidth(129);
		table.getColumnModel().getColumn(2).setPreferredWidth(95);
		table.getColumnModel().getColumn(2).setMaxWidth(450);
		table.getColumnModel().getColumn(3).setPreferredWidth(275);
		table.getColumnModel().getColumn(3).setMaxWidth(1050);
		table.getColumnModel().getColumn(4).setPreferredWidth(155);
		table.getColumnModel().getColumn(4).setMaxWidth(400);
		table.getColumnModel().getColumn(5).setPreferredWidth(187);
		scrollPane.setViewportView(table);

		pack();

		// 初始化系统
		JPanel panel_1 = new JPanel();
		jScrollPane1.setColumnHeaderView(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		lblNewLabel = new JLabel("\u7EC8\u7AEF\u76D1\u542C\u7AEF\u53E3\uFF1A");
		panel_1.add(lblNewLabel);

		textField_Port = new JTextField();
		panel_1.add(textField_Port);
		textField_Port.setColumns(5);

		// ParameterModel pm = GNSSImpl.parModel;
		this.textField_Port.setText("7611");

		btnConnect = new JButton("启动");
		panel_1.add(btnConnect);

		btnStopServer = new JButton("\u505C\u6B62");
		panel_1.add(btnStopServer);
		btnStopServer.setEnabled(false);

		checkBox_HideHeartLog = new JCheckBox("显示日志");
		panel_1.add(checkBox_HideHeartLog);

		checkBox_displayConnection = new JCheckBox("显示连接信息");
		panel_1.add(checkBox_displayConnection);

		label = new JLabel("\u65E5\u5FD7\u8FC7\u6EE4(\u5361\u53F7)\uFF1A");
		panel_1.add(label);

		textFilterBySimNo = new JTextField();
		textFilterBySimNo.getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						warn();
					}

					public void removeUpdate(DocumentEvent e) {
						warn();
					}

					public void insertUpdate(DocumentEvent e) {
						warn();
					}

					public void warn() {
						// JOptionPane.showMessageDialog(null,
						// "系统将只显示卡号为:"+textFilterBySimNo.getText()+"的数据包");
						GlobalConfig.filterSimNo = textFilterBySimNo.getText();
					}
				});

		textFilterBySimNo.setText("");
		textFilterBySimNo.setColumns(15);
		panel_1.add(textFilterBySimNo);

		btnNewButton = new JButton("\u751F\u6210\u6D4B\u8BD5\u8F66\u8F86");
		panel_1.add(btnNewButton);

		buttonClearLog = new JButton("清空日志");
		panel_1.add(buttonClearLog);

		statisticLabel = new JLabel("连接统计:当前连接数0");
		statisticLabel.setFont(new java.awt.Font("Dialog", 1, 13));
		statisticLabel.setForeground(Color.blue);
		panel_1.add(statisticLabel);

		splitPane_1 = new JSplitPane();
		jScrollPane1.setViewportView(splitPane_1);
		// dataArea = new JTextArea();

		scrollPane_1 = new JScrollPane();
		scrollPane_1
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		splitPane_1.setLeftComponent(scrollPane_1);

		table_1 = new JTable();
		table_1.setModel(new DefaultTableModel(new Object[][] {
				{ null, null, null, null, null, null, null, null, null, null,
						null },
				{ null, null, null, null, null, null, null, null, null, null,
						null },
				{ null, null, null, null, null, null, null, null, null, null,
						null },
				{ null, null, null, null, null, null, null, null, null, null,
						null },
				{ null, null, null, null, null, null, null, null, null, null,
						null },
				{ null, null, null, null, null, null, null, null, null, null,
						null },
				{ null, null, null, null, null, null, null, null, null, null,
						null },
				{ null, null, null, null, null, null, null, null, null, null,
						null },
				{ null, null, null, null, null, null, null, null, null, null,
						null }, }, new String[] { "\u5E8F\u53F7",
				"Sim\u5361\u53F7", "\u8F66\u724C\u53F7",
				"\u4E0A\u7EBF\u65F6\u95F4", "\u5728\u7EBF\u65F6\u95F4",
				"\u5B9A\u4F4D\u65F6\u95F4", "\u65AD\u7EBF\u6B21\u6570",
				"\u9519\u8BEF\u62A5\u6570", "总包数", "定位包数", "状态" }));
		table_1.getColumnModel().getColumn(0).setPreferredWidth(45);
		table_1.getColumnModel().getColumn(0).setMaxWidth(45);
		table_1.getColumnModel().getColumn(1).setPreferredWidth(100);
		table_1.getColumnModel().getColumn(1).setMinWidth(100);
		table_1.getColumnModel().getColumn(1).setMaxWidth(100);
		table_1.getColumnModel().getColumn(2).setPreferredWidth(95);
		table_1.getColumnModel().getColumn(2).setMinWidth(95);
		table_1.getColumnModel().getColumn(2).setMaxWidth(100);
		table_1.getColumnModel().getColumn(3).setPreferredWidth(120);
		table_1.getColumnModel().getColumn(3).setMinWidth(120);
		table_1.getColumnModel().getColumn(3).setMaxWidth(120);
		table_1.getColumnModel().getColumn(4).setPreferredWidth(120);
		table_1.getColumnModel().getColumn(4).setMinWidth(120);
		table_1.getColumnModel().getColumn(4).setMaxWidth(120);
		table_1.getColumnModel().getColumn(5).setPreferredWidth(120);
		table_1.getColumnModel().getColumn(5).setMinWidth(120);
		table_1.getColumnModel().getColumn(5).setMaxWidth(120);
		table_1.getColumnModel().getColumn(6).setMinWidth(75);
		table_1.getColumnModel().getColumn(6).setMaxWidth(100);
		table_1.getColumnModel().getColumn(7).setMinWidth(75);
		table_1.getColumnModel().getColumn(7).setMaxWidth(100);
		table_1.getColumnModel().getColumn(8).setMinWidth(75);
		table_1.getColumnModel().getColumn(8).setMaxWidth(100);
		table_1.getColumnModel().getColumn(9).setMinWidth(75);
		table_1.getColumnModel().getColumn(9).setMaxWidth(100);
		table_1.getColumnModel().getColumn(10).setMinWidth(75);
		table_1.getColumnModel().getColumn(10).setMaxWidth(100);
		scrollPane_1.setColumnHeaderView(table_1);
		scrollPane_1.setViewportView(table_1);// 解决显示表头问题：必须用这个

		splitPane_1.setDividerLocation(0.99);
		// scrollPane_2 = new JScrollPane();
		// splitPane_1.setRightComponent(scrollPane_2);

		// dataArea = new JTextArea();
		// scrollPane_2.setViewportView(dataArea);
		buttonClearLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// System.out.close();
				// System.setOut(new PrintStream(new GUIPrintStream(System.out,
				// MainFrame.this.dataArea)));
				//MainFrame.this.table.removeAll();
				//;

				DefaultTableModel tb = (DefaultTableModel) table.getModel();
				tb.setRowCount(0);
			}
		});
		this.btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createTestVehicle();
			}
		});
		checkBox_HideHeartLog.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				hideHeartBeatLog = checkBox_HideHeartLog.isSelected();
				GlobalConfig.displayMsg = hideHeartBeatLog;
			}
		});
		checkBox_displayConnection.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				boolean displayConnection = checkBox_displayConnection
						.isSelected();
				GlobalConfig.displayConnection = displayConnection;
			}
		});

		btnStopServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StopServer();
			}
		});
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StartServerActionPerfomed();
			}
		});

		t808Manager = (IT808Manager) ServiceLauncher.getBean("t808Manager");

		realDataService = (IRealDataService) ServiceLauncher
				.getBean("realDataService");

		this.textField_Port.setText("" + t808Manager.getListenPort());
	}

	private IRealDataService realDataService;
	private IT808Manager t808Manager;

	// 启动服务器
	private void StartServerActionPerfomed() {
		t808Manager = (IT808Manager) ServiceLauncher.getBean("t808Manager");
		int listenPort = Integer.parseInt(this.textField_Port.getText());
		t808Manager.setListenPort(listenPort);
		GlobalConfig.paramModel.setLocalPort(listenPort);
		boolean res = t808Manager.StartServer();

		if (res) {
			this.btnStopServer.setEnabled(true);
			// this.btnCloseMainLink.setEnabled(false);
			this.btnConnect.setEnabled(false);
			ActionListener taskPerformer = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {

					if (timerCount % 30 == 0)
						refreshConnections();
					else {

						T808Message tm = GlobalConfig.getMsgForDisplay();
						while (tm != null) {
							MainFrame.this.showMsg(tm);
							tm = GlobalConfig.getMsgForDisplay();
						}
					}

					timerCount++;
					if (timerCount > (Long.MAX_VALUE - 2))
						timerCount = 0;
				}
			};

			Timer timer = new Timer(100, taskPerformer);
			timer.setRepeats(true);
			timer.start();
		} else {
			JOptionPane.showMessageDialog(null, "808服务器启动失败，端口被占用");
		}
	}

	private void StopServer() {
		t808Manager.StopServer();

		this.btnStopServer.setEnabled(false);
		// this.btnCloseMainLink.setEnabled(false);
		this.btnConnect.setEnabled(true);

	}

	@SuppressWarnings("deprecation")
	private void refreshConnections() {
		Collection<GpsConnection> connList = t808Manager.getGpsConnections();
		DefaultTableModel tb = (DefaultTableModel) this.table_1.getModel();
		tb.setRowCount(0);
		int m = 1;
		GlobalConfig.connectNum = connList.size();
		int totalLocationPacketNum = 0;
		int totalPacketNum = 0;
		Date start = new Date();
		for (GpsConnection conn : connList) {
			String sendTime = "";
			GPSRealData rd = realDataService.get(conn.getSimNo());
			if (rd != null) {
				conn.setPlateNo(rd.getPlateNo());
				sendTime = DateUtil.toStringByFormat(rd.getSendTime(),
						"dd HH:mm:ss");
				realDataService.updateOnlineTime(rd, conn.getOnlineDate());
			}

			String state = null;
			//realDataService.UpdateConnectedState(conn.getSimNo(), conn.isConnected());
			if (conn.isConnected()) {
				state = (rd != null && rd.IsValid()) ? "已定位" : "未定位";
			} else
				state = "已断开";

			if (GlobalConfig.displayConnection) {
				if(GlobalConfig.filterSimNo!= null && GlobalConfig.filterSimNo.length() > 0 )
				{
					if(conn.getSimNo() == null || conn.getSimNo().indexOf(GlobalConfig.filterSimNo) < 0)
						continue;
				}
				
				tb.addRow(new Object[] {
						"" + m,
						conn.getSimNo(),
						conn.getPlateNo(),
						DateUtil.toStringByFormat(conn.getCreateDate(),
								"dd HH:mm:ss"),
						DateUtil.toStringByFormat(conn.getOnlineDate(),
								"dd HH:mm:ss"), sendTime,
						conn.getDisconnectTimes(), conn.getErrorPacketNum(),
						conn.getPackageNum(), conn.getPositionPackageNum(),
						state });
				table.setRowHeight(m, 35);
			}
			//totalLocationPacketNum += conn.getPositionPackageNum();
			//totalPacketNum += conn.getPackageNum();
			m++;
		}
		Date end = new Date();
		double seconds = DateUtil.getSeconds(start, end);
		if(seconds > 50)
		   logger.error("连接检索查询耗时:" + seconds + ",条数：" +  GlobalConfig.connectNum);
		//GlobalConfig.totalLocationPacketNum = totalLocationPacketNum;
		//GlobalConfig.totalPacketNum = totalPacketNum;

		String staticText = "当前连接数:" + GlobalConfig.connectNum
		// + ",接收总包数："+GlobalConfig.totalPacketNum
		// + ",接收定位包数："+GlobalConfig.totalLocationPacketNum
		;
		this.statisticLabel.setText(staticText);
	}

	private void createTestVehicle() {

		List result = new ArrayList();
		for (int m = 0; m < 10001; m++) {
			VehicleData vd = new VehicleData();
			String plateNo = "测B" + StringUtil.leftPad("" + m, 5, "0");
			vd.setPlateNo(plateNo);
			String simNo = "131" + StringUtil.leftPad("" + m, 8, "0");
			vd.setSimNo(simNo);
			vd.setPlateColor(2);
			if (m < 7000)
				vd.setDepId(117440527);
			else
				vd.setDepId(117440528);
			result.add(vd);
		}

		ServiceLauncher.getBaseDao().saveOrUpdateAll(result);

	}

	private void showMsg(T808Message tm) {
		/**
		 * if (this.hideHeartBeatLog == false) { // 不显示心跳和平台通用应答 if
		 * (tm.getMessageType() == 0x0002 || tm.getMessageType() == 0x0200 ||
		 * tm.getMessageType() == 0x8001 || tm.getMessageType() == 0x0102)
		 * return;
		 * 
		 * }
		 */
		Date now = new Date();
		String strMsgType = "0x" + Tools.ToHexString(tm.getMessageType(), 2);
		String msgDescr = "[" + strMsgType + "]"
				+ JT808Constants.GetDescr(strMsgType);

		DefaultTableModel tb = (DefaultTableModel) this.table.getModel();

		if (table.getRowCount() > 1000)
			tb.setRowCount(10);
		tb.insertRow(
				0,
				new Object[] { msgDescr, tm.getHeader().getSimId(),
						tm.getPlateNo(), tm.toString(), now.toLocaleString(),
						tm.getPacketDescr() });
		table.setRowHeight(0, 35);
	}

	public static void main(String[] args) {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager
					.getInstalledLookAndFeels())
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
		} catch (Exception ex) {

			Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null,
					ex);
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				MainFrame mf = new MainFrame();
				mf.setExtendedState(JFrame.MAXIMIZED_BOTH);// 设定窗体状态为屏幕最大化，即全屏尺寸。
				mf.setVisible(true);
				mf.splitPane_1.setDividerLocation(0.8);

			}
		});
	}
}
