 package com.ltmonitor.jt808.tool.log4j;
 
 import java.io.IOException;
 import java.io.OutputStream;
 import org.apache.log4j.ConsoleAppender;
 
 public class SelfAppender extends ConsoleAppender
 {
   public void activateOptions()
   {
     super.activateOptions();
     setWriter(createWriter(new SystemOutStream()));
   }
 
   private static class SystemOutStream extends OutputStream
   {
     public void close()
     {
     }
 
     public void flush()
     {
       System.out.flush();
     }
 
     public void write(byte[] b)
       throws IOException
     {
       System.out.write(b);
     }
 
     public void write(byte[] b, int off, int len)
       throws IOException
     {
       System.out.write(b, off, len);
     }
 
     public void write(int b)
       throws IOException
     {
       System.out.write(b);
     }
   }
 }

