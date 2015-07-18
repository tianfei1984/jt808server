 package com.ltmonitor.jt808.recording;
 
 import java.util.Date;
 
 public class OverTimeDriverModel
 {
   private String driverNO;
   private Date driverST;
   private Date driverET;
   private double lon;
   private double lat;
   private int elevation;
 
   public String getDriverNO()
   {
     return this.driverNO;
   }
 
   public void setDriverNO(String driverNO) {
     this.driverNO = driverNO;
   }
 
   public Date getDriverST() {
     return this.driverST;
   }
 
   public void setDriverST(Date driverST) {
     this.driverST = driverST;
   }
 
   public Date getDriverET() {
     return this.driverET;
   }
 
   public void setDriverET(Date driverET) {
     this.driverET = driverET;
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

