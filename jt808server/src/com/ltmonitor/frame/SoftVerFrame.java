 package com.ltmonitor.frame;
 
 import java.awt.EventQueue;
 import java.awt.Frame;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.WindowAdapter;
 import java.awt.event.WindowEvent;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import javax.swing.GroupLayout;
 import javax.swing.ImageIcon;
 import javax.swing.JButton;
 import javax.swing.JDialog;
 import javax.swing.JFrame;
 import javax.swing.JLabel;
 import javax.swing.UIManager;
 import javax.swing.UnsupportedLookAndFeelException;
 
 public class SoftVerFrame extends JDialog
 {
   private JButton OKButton;
   private JLabel jLabel1;
   private JLabel jLabel2;
   private JLabel jLabel3;
 
   public SoftVerFrame(Frame parent, boolean modal)
   {
     super(parent, modal);
     initComponents();
     initParameter();
   }
 
   private void initParameter() {
     setTitle("版本信息");
     setIconImage(new ImageIcon(getClass().getResource("/tm/base/frame/20111108112654.png")).getImage());
   }
 
   private void initComponents()
   {
     this.jLabel1 = new JLabel();
     this.jLabel2 = new JLabel();
     this.jLabel3 = new JLabel();
     this.OKButton = new JButton();
 
     setDefaultCloseOperation(2);
 
     this.jLabel1.setIcon(new ImageIcon(getClass().getResource("/tm/base/frame/20111108112654.png")));
 
     this.jLabel2.setText("版本：1.0.2");
 
     this.jLabel3.setText("版权：精致软件 ltmonitor.com");
 
     this.OKButton.setText("确定");
     this.OKButton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent evt) {
         SoftVerFrame.this.OKButtonActionPerformed(evt);
       }
     });
     GroupLayout layout = new GroupLayout(getContentPane());
     getContentPane().setLayout(layout);
     layout.setHorizontalGroup(
       layout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addGroup(layout.createSequentialGroup()
       .addContainerGap()
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
       .addComponent(this.OKButton)
       .addGroup(layout.createSequentialGroup()
       .addComponent(this.jLabel1)
       .addGap(18, 18, 18)
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addComponent(this.jLabel2)
       .addComponent(this.jLabel3))))
       .addContainerGap(23, 32767)));
 
     layout.setVerticalGroup(
       layout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addGroup(layout.createSequentialGroup()
       .addContainerGap()
       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addComponent(this.jLabel1)
       .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
       .addComponent(this.jLabel3)
       .addGap(30, 30, 30)
       .addComponent(this.jLabel2)
       .addGap(37, 37, 37)
       .addComponent(this.OKButton)))
       .addContainerGap(-1, 32767)));
 
     pack();
   }
 
   private void OKButtonActionPerformed(ActionEvent evt)
   {
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
       Logger.getLogger(SoftVerFrame.class.getName()).log(Level.SEVERE, null, ex);
     } catch (InstantiationException ex) {
       Logger.getLogger(SoftVerFrame.class.getName()).log(Level.SEVERE, null, ex);
     } catch (IllegalAccessException ex) {
       Logger.getLogger(SoftVerFrame.class.getName()).log(Level.SEVERE, null, ex);
     } catch (UnsupportedLookAndFeelException ex) {
       Logger.getLogger(SoftVerFrame.class.getName()).log(Level.SEVERE, null, ex);
     }
 
     EventQueue.invokeLater(new Runnable() {
       public void run() {
         SoftVerFrame dialog = new SoftVerFrame(new JFrame(), true);
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

