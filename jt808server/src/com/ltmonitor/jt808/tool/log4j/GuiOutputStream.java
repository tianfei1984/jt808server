 package com.ltmonitor.jt808.tool.log4j;
 
 import java.io.OutputStream;
 import javax.swing.JTextArea;
 
 public class GuiOutputStream extends OutputStream
 {
   private JTextArea _text;
   private byte _c;
   private boolean _b = false;
 
   public GuiOutputStream(JTextArea text)
   {
     this._text = text;
   }
 
   public void write(int i) {
     try {
       byte c = (byte)i;
       if (this._b) {
         this._b = false;
         byte[] bs = new byte[2];
         bs[0] = this._c;
         bs[1] = c;
         this._text.append(new String(bs));
       }
       else if (c > 0) {
         this._b = false;
         this._text.append(String.valueOf((char)c));
       }
       else {
         this._b = true;
         this._c = c;
       }
     }
     catch (Exception ex) {
       ex.printStackTrace();
     }
   }
 }

