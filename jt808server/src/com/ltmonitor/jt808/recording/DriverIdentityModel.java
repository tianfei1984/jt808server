 package com.ltmonitor.jt808.recording;
 
 import java.util.Date;
 
 public class DriverIdentityModel
 {
   private Date eventHappenTime;
   private String motorDrivLiceNum;
   private String enentType;
 
   public Date getEventHappenTime()
   {
     return this.eventHappenTime;
   }
 
   public void setEventHappenTime(Date eventHappenTime) {
     this.eventHappenTime = eventHappenTime;
   }
 
   public String getMotorDrivLiceNum() {
     return this.motorDrivLiceNum;
   }
 
   public void setMotorDrivLiceNum(String motorDrivLiceNum) {
     this.motorDrivLiceNum = motorDrivLiceNum;
   }
 
   public String getEnentType() {
     return this.enentType;
   }
 
   public void setEnentType(String enentType) {
     this.enentType = enentType;
   }
 }

