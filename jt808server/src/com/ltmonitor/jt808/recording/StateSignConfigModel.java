 package com.ltmonitor.jt808.recording;
 
 import java.util.Date;
 
 public class StateSignConfigModel
 {
   private Date recRealTime;
   private String configWord;
   private String configNO;
   private String serialNO;
 
   public Date getRecRealTime()
   {
     return this.recRealTime;
   }
 
   public void setRecRealTime(Date recRealTime) {
     this.recRealTime = recRealTime;
   }
 
   public String getConfigWord() {
     return this.configWord;
   }
 
   public void setConfigWord(String configWord) {
     this.configWord = configWord;
   }
 
   public String getConfigNO() {
     return this.configNO;
   }
 
   public void setConfigNO(String configNO) {
     this.configNO = configNO;
   }
 
   public String getSerialNO() {
     return this.serialNO;
   }
 
   public void setSerialNO(String serialNO) {
     this.serialNO = serialNO;
   }
 }

