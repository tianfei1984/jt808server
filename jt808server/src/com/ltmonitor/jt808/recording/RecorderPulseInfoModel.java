 package com.ltmonitor.jt808.recording;
 
 import java.util.Date;
 
 public class RecorderPulseInfoModel
 {
   private Date recCurTime;
   private String pulseInfo;
 
   public Date getRecCurTime()
   {
     return this.recCurTime;
   }
 
   public void setRecCurTime(Date recCurTime)
   {
     this.recCurTime = recCurTime;
   }
 
   public String getPulseInfo()
   {
     return this.pulseInfo;
   }
 
   public void setPulseInfo(String pulseInfo)
   {
     this.pulseInfo = pulseInfo;
   }
 }

