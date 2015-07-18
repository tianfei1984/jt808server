 package com.ltmonitor.jt808.recording;
 
 import java.util.Date;
 
 public class DriverSpeedRecordModel
 {
   private Date startTime;
   private int minuteNO;
   private int secondNO;
   private int speed;
 
   public Date getStartTime()
   {
     return this.startTime;
   }
 
   public void setStartTime(Date startTime) {
     this.startTime = startTime;
   }
 
   public int getMinuteNO() {
     return this.minuteNO;
   }
 
   public void setMinuteNO(int minuteNO) {
     this.minuteNO = minuteNO;
   }
 
   public int getSecondNO() {
     return this.secondNO;
   }
 
   public void setSecondNO(int secondNO) {
     this.secondNO = secondNO;
   }
 
   public int getSpeed() {
     return this.speed;
   }
 
   public void setSpeed(int speed) {
     this.speed = speed;
   }
 }

