 package com.ltmonitor.jt808.tool.log4j;
 
 import java.io.OutputStream;
 import java.io.PrintStream;
 import javax.swing.JTextArea;
 import javax.swing.SwingUtilities;
 
 public class GUIPrintStream extends PrintStream
 {
   private JTextArea component;
   private StringBuffer sb = new StringBuffer();
 
   private int lineCount = 1000;
 
   public GUIPrintStream(OutputStream out, JTextArea component)
   {
     super(out);
     this.component = component;
   }
 
   public void write(byte[] buf, int off, int len)
   {
     final String message = new String(buf, off, len);
     if (this.component.getLineCount() > this.lineCount)
     {
       this.sb.setLength(0);
     }
     SwingUtilities.invokeLater(new Runnable()
     {
       public void run()
       {
         GUIPrintStream.this.sb.append(message);
         GUIPrintStream.this.component.setText(GUIPrintStream.this.sb.toString());
       }
     });
   }
 }

