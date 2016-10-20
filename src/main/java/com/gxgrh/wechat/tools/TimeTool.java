package com.gxgrh.wechat.tools;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Administrator on 2016/9/28.
 */
@Component
public class TimeTool {


    /**
     * 获取当前时间毫秒数
     * @return
     */
    public static Long getCurrentTime(){

        return System.currentTimeMillis();

    }

    /**
     * 获取当前时间秒数
     * @return
     */
    public static Long getCurrentTimeInSeconds(){

        return System.currentTimeMillis()/1000L;

    }


    /**
     * 根据时间毫秒数获取Date对象
     * @param millisSeconds
     * @return
     */
    public static Date getCurrentTimeBySeconds(String millisSeconds){
        try{
            long millisSecond = Long.parseLong(millisSeconds);
            Date date = new Date(millisSecond);
            return date;
        }catch(NumberFormatException e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据时间毫秒数获取Date对象
     * @param millisSeconds
     * @return
     */
    public static Date getCurrentTimeBySeconds(Long millisSeconds){
        try{
            Date date = new Date(millisSeconds);
            return date;
        }catch(NumberFormatException e){
            e.printStackTrace();
            return null;
        }
    }

    public static long milliSecondsBetweenDates(Date date1,Date date2){
        long interval = date1.getTime()-date2.getTime();
        return interval;
    }



}
