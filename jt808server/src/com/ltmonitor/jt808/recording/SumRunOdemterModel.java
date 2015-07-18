 package com.ltmonitor.jt808.recording;
 
 import java.util.Date;
 
 public class SumRunOdemterModel
 {
   private Date realTime;
   private Date firstInstaTime;
   private long startOdemter;
   private long sumOdemeter;
 
   public Date getRealTime()
   {
     return this.realTime;
   }
 
   public void setRealTime(Date realTime)
   {
     this.realTime = realTime;
   }
 
   public Date getFirstInstaTime()
   {
     return this.firstInstaTime;
   }
 
   public void setFirstInstaTime(Date firstInstaTime)
   {
     this.firstInstaTime = firstInstaTime;
   }
 
   public long getStartOdemter()
   {
     return this.startOdemter;
   }
 
   public void setStartOdemter(long startOdemter)
   {
     this.startOdemter = startOdemter;
   }
 
   public long getSumOdemeter()
   {
     return this.sumOdemeter;
   }
 
   public void setSumOdemeter(long sumOdemeter)
   {
     this.sumOdemeter = sumOdemeter;
   }
 }

