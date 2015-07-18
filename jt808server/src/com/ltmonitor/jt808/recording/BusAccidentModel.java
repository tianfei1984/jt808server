 package com.ltmonitor.jt808.recording;
 
 import java.util.Date;
 
 public class BusAccidentModel
 {
   private Date stopTime;
   private int driverNO;
   private double Stop_speed;
   private int stopState;
   private double lon;
   private double lat;
   private int elevation;
 
   public Date getStopTime()
   {
     return this.stopTime;
   }
   public void setStopTime(Date stopTime) {
     this.stopTime = stopTime;
   }
   public int getDriverNO() {
     return this.driverNO;
   }
   public void setDriverNO(int driverNO) {
     this.driverNO = driverNO;
   }
   public double getStop_speed() {
     return this.Stop_speed;
   }
   public void setStop_speed(double stopSpeed) {
     this.Stop_speed = stopSpeed;
   }
   public int getStopState() {
     return this.stopState;
   }
   public void setStopState(int stopState) {
     this.stopState = stopState;
   }
   public double getLon() {
     return this.lon;
   }
   public void setLon(double lon) {
     this.lon = lon;
   }
   public double getLat() {
     return this.lat;
   }
   public void setLat(double lat) {
     this.lat = lat;
   }
   public int getElevation() {
     return this.elevation;
   }
   public void setElevation(int elevation) {
     this.elevation = elevation;
   }
 }

