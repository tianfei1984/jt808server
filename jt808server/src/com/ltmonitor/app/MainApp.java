package com.ltmonitor.app;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.ltmonitor.frame.MainFrame;
import com.ltmonitor.jt808.service.IT808Manager;


public class MainApp {
	IT808Manager t808Manager;
	private static Logger logger = Logger.getLogger(MainApp.class);
	public static void main(String[] args) {

		try {

			ServiceLauncher.launch();

			/**
			 * 换肤: Windows, WindowsClassic,Metal,
			 * 
			 */
			UIManager.LookAndFeelInfo[] arrayOfLookAndFeelInfo;
			int j = (arrayOfLookAndFeelInfo = UIManager
					.getInstalledLookAndFeels()).length;
			for (int i = 0; i < j; i++) {
				UIManager.LookAndFeelInfo info = arrayOfLookAndFeelInfo[i];
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}

			MainFrame mainFrame = new MainFrame();
			
			// 输出log到主界面的文本控件中显示
			//System.setOut(new PrintStream(new GUIPrintStream(System.out,
					//mainFrame.dataArea)));
			mainFrame.setLocationRelativeTo(null);

			mainFrame.setVisible(true);
			mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		} catch (Exception ex) {
			//Logger.getLogger(ParameterFrame.class.getName()).log(Level.SEVERE,
					//null, ex);
			logger.error(ex.getMessage(), ex);
		}


	}
}
