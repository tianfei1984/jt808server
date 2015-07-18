package com.ltmonitor.app;

public enum VehiclePlateColor {
	 /// <summary>
    /// 蓝色
    /// </summary>
    Blue(1),
    /// <summary>
    /// 黄色
    /// </summary>
    Yellow(2),
    /// <summary>
    /// 黑色
    /// </summary>
    Black(3),
    /// <summary>
    /// 白色
    /// </summary>
    White(4),
    /// <summary>
    /// 其他
    /// </summary>
    Other(5);
    
    private int nCode ;
 
       // 构造函数，枚举类型只能为私有
       private VehiclePlateColor( int _nCode) {
           this . nCode = _nCode;
       }
 
       @Override
       public String toString() {
           return String.valueOf ( this . nCode );
       }
}
