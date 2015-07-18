 package com.ltmonitor.jt808.recording;
 
 import java.util.Date;
 
 public class SpeedStateLogModel
 {
   private String speedState;
   private Date speedStateST;
   private Date speedStateET;
   private double recordSpeed;
   private double referenceSpeed;
 
   public String getSpeedState()
   {
     return this.speedState;
   }
 
   public void setSpeedState(String speedState) {
     this.speedState = speedState;
   }
 
   public Date getSpeedStateST() {
     return this.speedStateST;
   }
 
   public void setSpeedStateST(Date speedStateST) {
     this.speedStateST = speedStateST;
   }
 
   public Date getSpeedStateET() {
     return this.speedStateET;
   }
 
   public void setSpeedStateET(Date speedStateET) {
     this.speedStateET = speedStateET;
   }
 
   public double getRecordSpeed() {
     return this.recordSpeed;
   }
 
   public void setRecordSpeed(double recordSpeed) {
     this.recordSpeed = recordSpeed;
   }
 
   public double getReferenceSpeed() {
     return this.referenceSpeed;
   }
 
   public void setReferenceSpeed(double referenceSpeed) {
     this.referenceSpeed = referenceSpeed;
   }
 }

