 package com.ltmonitor.frame;
 
 import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.ltmonitor.jt808.model.Parameter;

 
 public class ParameterFrame extends JDialog
 {
   private JButton cancleButton;
   private JTextField idleTime;
   private JButton jButton1;
   private JComboBox jComboBox1;
   private JLabel jLabel1;
   private JLabel jLabel10;
   private JLabel jLabel11;
   private JLabel jLabel12;
   private JLabel jLabel13;
   private JLabel jLabel14;
   private JLabel jLabel15;
   private JLabel jLabel2;
   private JLabel jLabel3;
   private JLabel jLabel4;
   private JLabel jLabel5;
   private JLabel jLabel6;
   private JLabel jLabel7;
   private JLabel jLabel8;
   private JLabel jLabel9;
   private JTextField licenseNoText;
   private JTextField localIP;
   private JTextField localPort;
   private JTextField miyaoIA1;
   private JTextField miyaoIC1;
   private JTextField miyaoM1;
   private JTextField platID;
   private JTextField platIP;
   private JTextField platPassword;
   private JTextField platPort;
   private JTextField platUsername;
   private JTextField protovolVer;
   private JTextField sendCycle;
   private JTextField usernameText;
 
   public ParameterFrame(Frame parent, boolean modal)
   {
     super(parent, modal);
     initComponents();
     initParameter();
     initFrame();
   }
 
   private void initFrame() {
     setTitle("平台参数设置");
     setIconImage(new ImageIcon(getClass().getResource("/tm/base/frame/20111108112654.png")).getImage());
   }
 
   private void initParameter() {
     Parameter pm = new Parameter();
     this.platIP.setText(pm.getPlatformIP());
     this.platPassword.setText(pm.getPlatformPass());
     this.platUsername.setText(String.valueOf(pm.getPlatformUserName()));
     this.platPort.setText(String.valueOf(pm.getPlatformPort()));
     this.idleTime.setText(String.valueOf(pm.getIdleTime()));
     this.localPort.setText(String.valueOf(pm.getLocalPort()));
     this.platID.setText(String.valueOf(pm.getPlatformCenterId()));
     this.miyaoM1.setText(String.valueOf(pm.getMiyaoM()));
     this.miyaoIA1.setText(String.valueOf(pm.getMiyaoA()));
     this.miyaoIC1.setText(String.valueOf(pm.getMiyaoC()));
     this.sendCycle.setText("20");
     this.localIP.setText(pm.getLocalIp());
     this.licenseNoText.setText(pm.getLicenseNo());
     this.usernameText.setText(pm.getUsername());
   }
 
   private void initComponents()
   {
     this.jComboBox1 = new JComboBox();
     this.jLabel1 = new JLabel();
     this.platPort = new JTextField();
     this.jLabel2 = new JLabel();
     this.platIP = new JTextField();
     this.jLabel3 = new JLabel();
     this.platID = new JTextField();
     this.jLabel4 = new JLabel();
     this.platPassword = new JTextField();
     this.jLabel5 = new JLabel();
     this.platUsername = new JTextField();
     this.jLabel6 = new JLabel();
     this.localPort = new JTextField();
     this.jLabel8 = new JLabel();
     this.jLabel9 = new JLabel();
     this.idleTime = new JTextField();
     this.sendCycle = new JTextField();
     this.jButton1 = new JButton();
     jButton1.addActionListener(new ActionListener() {
     	public void actionPerformed(ActionEvent arg0) {
     		saveButtonActionPerformed(arg0); //保存参数设置
     	}
     });
     this.cancleButton = new JButton();
     this.jLabel7 = new JLabel();
     this.jLabel10 = new JLabel();
     this.jLabel11 = new JLabel();
     this.miyaoM1 = new JTextField();
     this.miyaoIA1 = new JTextField();
     this.miyaoIC1 = new JTextField();
     this.jLabel12 = new JLabel();
     this.protovolVer = new JTextField();
     this.jLabel13 = new JLabel();
     this.localIP = new JTextField();
     this.jLabel14 = new JLabel();
     this.licenseNoText = new JTextField();
     this.jLabel15 = new JLabel();
     this.usernameText = new JTextField();
 
     this.jComboBox1.setModel(new DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
 
     setDefaultCloseOperation(2);
 
     this.jLabel1.setText("平台端口：");
 
     this.jLabel2.setText("平台 I D ：");
 
     this.jLabel3.setText("平台 I P ：");
 
     this.jLabel4.setText("用户密码：");
 
     this.jLabel5.setText("用户名称：");
 
     this.jLabel6.setText("本地端口：");
 
     this.jLabel8.setText("服务空闲时间：");
 
     this.jLabel9.setText("信息发送周期：");
 
     this.jButton1.setText("保存");
 
     this.cancleButton.setText("取消");
     this.cancleButton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent evt) {
         ParameterFrame.this.cancleButtonActionPerformed(evt);
       }
     });
     this.jLabel7.setText("密钥 M 1 ：");
 
     this.jLabel10.setText("密钥 IA1 ：");
 
     this.jLabel11.setText("密钥 IC1 ：");
 
     this.jLabel12.setText("协议版本：");
 
     this.jLabel13.setText("本地IP:");
 
     this.jLabel14.setText("许可证号：");
 
     this.jLabel15.setText("用户账号：");
 
     GroupLayout layout = new GroupLayout(getContentPane());
     getContentPane().setLayout(layout);
     layout.setHorizontalGroup(
       layout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addGroup(layout.createSequentialGroup()
       .addContainerGap()
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addGroup(layout.createSequentialGroup()
       .addComponent(this.jLabel9)
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addComponent(this.sendCycle, -2, 172, -2))
       .addComponent(this.jLabel6)))
       .addGroup(layout.createSequentialGroup()
       .addContainerGap()
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
       .addGroup(layout.createSequentialGroup()
       .addComponent(this.jLabel13)
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767)
       .addComponent(this.localIP, -2, 172, -2))
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
       .addGroup(layout.createSequentialGroup()
       .addComponent(this.jLabel1)
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767)
       .addComponent(this.platPort, -2, 172, -2))
       .addGroup(layout.createSequentialGroup()
       .addComponent(this.jLabel4)
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767)
       .addComponent(this.platPassword, -2, 172, -2))
       .addGroup(layout.createSequentialGroup()
       .addComponent(this.jLabel5)
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767)
       .addComponent(this.platUsername, -2, 172, -2))
       .addGroup(layout.createSequentialGroup()
       .addComponent(this.jLabel8)
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addComponent(this.idleTime, -2, 172, -2)))
       .addComponent(this.localPort, GroupLayout.Alignment.TRAILING, -2, 172, -2)))
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addGroup(layout.createSequentialGroup()
       .addGap(2, 2, 2)
       .addComponent(this.jLabel11)
       .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
       .addComponent(this.miyaoIC1, -2, 164, -2))
       .addGroup(layout.createSequentialGroup()
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addComponent(this.jLabel2, -2, 66, -2)
       .addComponent(this.jLabel3)
       .addGroup(layout.createSequentialGroup()
       .addGap(2, 2, 2)
       .addComponent(this.jLabel12)))
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addGroup(layout.createSequentialGroup()
       .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addComponent(this.platIP, -2, 166, -2)
       .addComponent(this.platID, -2, 166, -2)))
       .addGroup(layout.createSequentialGroup()
       .addGap(12, 12, 12)
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
       .addComponent(this.usernameText)
       .addComponent(this.cancleButton)
       .addComponent(this.protovolVer, -1, 164, 32767)
       .addComponent(this.licenseNoText))))))
       .addGap(1, 1, 1))
       .addGroup(layout.createSequentialGroup()
       .addGap(3, 3, 3)
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
       .addComponent(this.jLabel7)
       .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
       .addComponent(this.miyaoM1, -2, 164, -2))
       .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
       .addComponent(this.jLabel10)
       .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
       .addComponent(this.miyaoIA1, -2, 164, -2))))
       .addGroup(layout.createSequentialGroup()
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addComponent(this.jLabel15)
       .addComponent(this.jLabel14)))))
       .addGroup(layout.createSequentialGroup()
       .addGap(194, 194, 194)
       .addComponent(this.jButton1)));
 
     layout.setVerticalGroup(
       layout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addGroup(layout.createSequentialGroup()
       .addContainerGap()
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
       .addComponent(this.jLabel1)
       .addComponent(this.platPort, -2, -1, -2)
       .addComponent(this.jLabel3)
       .addComponent(this.platIP, -2, -1, -2))
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addComponent(this.platID, -2, -1, -2)
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
       .addComponent(this.jLabel4)
       .addComponent(this.platPassword, -2, -1, -2)
       .addComponent(this.jLabel2)))
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
       .addComponent(this.jLabel5)
       .addComponent(this.platUsername, -2, -1, -2))
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
       .addComponent(this.miyaoM1, -2, -1, -2)
       .addComponent(this.jLabel7)))
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
       .addComponent(this.jLabel8)
       .addComponent(this.idleTime, -2, -1, -2)
       .addComponent(this.jLabel10)
       .addComponent(this.miyaoIA1, -2, -1, -2))
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
       .addComponent(this.sendCycle, -2, -1, -2)
       .addComponent(this.jLabel9)
       .addComponent(this.jLabel11)
       .addComponent(this.miyaoIC1, -2, -1, -2))
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
       .addComponent(this.jLabel6)
       .addComponent(this.localPort, -2, -1, -2)
       .addComponent(this.jLabel12)
       .addComponent(this.protovolVer, -2, -1, -2))
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
       .addComponent(this.jLabel13)
       .addComponent(this.localIP, -2, -1, -2)
       .addComponent(this.jLabel14)
       .addComponent(this.licenseNoText, -2, -1, -2))
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
       .addComponent(this.jLabel15)
       .addComponent(this.usernameText, -2, -1, -2))
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767)
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
       .addComponent(this.jButton1)
       .addComponent(this.cancleButton))
       .addContainerGap()));
 
     pack();
   }
 
   private void cancleButtonActionPerformed(ActionEvent evt)
   {
     dispose();
   }
   
   private void saveButtonActionPerformed(ActionEvent evt)
   {
	   Parameter pm = new Parameter();
	   pm.setPlatformIP(this.platIP.getText());
	   pm.setPlatformPort(Integer.parseInt(this.platPort.getText()));
	   pm.setLocalIp(this.localIP.getText());
	   pm.setLocalPort(Integer.parseInt(localPort.getText()));
	   JOptionPane.showMessageDialog(null, "保存成功");
	   dispose();
   }
 
   public static void main(String[] args)
   {
     try
     {
       for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
         if ("Nimbus".equals(info.getName())) {
           UIManager.setLookAndFeel(info.getClassName());
           break;
         }
     }
     catch (ClassNotFoundException ex) {
       Logger.getLogger(ParameterFrame.class.getName()).log(Level.SEVERE, null, ex);
     } catch (InstantiationException ex) {
       Logger.getLogger(ParameterFrame.class.getName()).log(Level.SEVERE, null, ex);
     } catch (IllegalAccessException ex) {
       Logger.getLogger(ParameterFrame.class.getName()).log(Level.SEVERE, null, ex);
     } catch (UnsupportedLookAndFeelException ex) {
       Logger.getLogger(ParameterFrame.class.getName()).log(Level.SEVERE, null, ex);
     }
 
     EventQueue.invokeLater(new Runnable() {
       public void run() {
         ParameterFrame dialog = new ParameterFrame(new JFrame(), true);
         dialog.addWindowListener(new WindowAdapter()
         {
           public void windowClosing(WindowEvent e) {
             System.exit(0);
           }
         });
         dialog.setVisible(true);
       }
     });
   }
 }

