 package com.ltmonitor.jt808.recording;
 
 import java.util.Date;
 
 public class ParamUpdateRecModel
 {
   private Date enentHappenTime;
   private int enentType;
 
   public Date getEnentHappenTime()
   {
     return this.enentHappenTime;
   }
 
   public void setEnentHappenTime(Date enentHappenTime) {
     this.enentHappenTime = enentHappenTime;
   }
 
   public int getEnentType() {
     return this.enentType;
   }
 
   public void setEnentType(int enentType) {
     this.enentType = enentType;
   }
 }

