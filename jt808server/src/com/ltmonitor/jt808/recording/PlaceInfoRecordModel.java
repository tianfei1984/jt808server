 package com.ltmonitor.jt808.recording;
 
 import java.util.Date;
 
 public class PlaceInfoRecordModel
 {
   private Date date;
   private int hourNO;
   private int minuteNO;
   private double lat;
   private double lon;
   private int elevation;
 
   public Date getDate()
   {
     return this.date;
   }
 
   public void setDate(Date date) {
     this.date = date;
   }
 
   public int getHourNO() {
     return this.hourNO;
   }
 
   public void setHourNO(int hourNO) {
     this.hourNO = hourNO;
   }
 
   public int getMinuteNO() {
     return this.minuteNO;
   }
 
   public void setMinuteNO(int minuteNO) {
     this.minuteNO = minuteNO;
   }
 
   public double getLat() {
     return this.lat;
   }
 
   public void setLat(double lat) {
     this.lat = lat;
   }
 
   public double getLon() {
     return this.lon;
   }
 
   public void setLon(double lon) {
     this.lon = lon;
   }
 
   public int getElevation() {
     return this.elevation;
   }
 
   public void setElevation(int elevation) {
     this.elevation = elevation;
   }
 }

