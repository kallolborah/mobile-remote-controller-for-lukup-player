/*
 * Copyright (c) Lukup Media Pvt Limited, India.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Lukup Media Pvt Limited ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms
 * of the licence agreement you entered into with Lukup Media Pvt Limited.
 *
 */
package com.player.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import com.player.Layout;

/**
 * This class provides the functionality to set and get the something.
 * @author Yuvaraja
 *
 */
public class CommonUtil {
	private static final String TAG = "CommonUtil";

	
	/**
	 * Convert millisec to timer
	 * @param milliseconds
	 * @return
	 */
	public static String milliSecondsToTimer(long milliseconds) {
	    String finalTimerString = "";
	    String secondsString = "";
	    String minString = "";
	    // Convert total duration into time
	    int hours = (int) (milliseconds / (1000 * 60 * 60));
	    int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
	    int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
	    // Add hours if there
	   /* if (hours > 0) {
	        finalTimerString = hours + ":";
	    }else{
	    	
	    }*/
	    
	    // Prepending 0 to seconds if it is one digit
	    
	    if (hours < 10) {
	    	finalTimerString = "0" + hours;
	    } else {
	    	finalTimerString = "" + hours;
	    }
	    
	    if (minutes < 10) {
	    	minString = "0" + minutes;
	    } else {
	    	minString = "" + minutes;
	    }
	    
	    if (seconds < 10) {
	        secondsString = "0" + seconds;
	    } else {
	        secondsString = "" + seconds;
	    }

	    finalTimerString = finalTimerString+ ":" + minString + ":" + secondsString;

	    // return timer string
	    return finalTimerString;
	}
	
	public static boolean currentTimeEquals(String currentTime,
			String programStartTime) {
		return false;
	}

	public static boolean currentDateIsLess(String currentDate,
			String programEndDate) {
		return false;
	}

	public static String getInHourFormat(int duration) {
		String stringDuration = "";
		if(duration > 0) {
			int a = duration/3600;
			int b,c,d;
			if(a > 0) {
				b = duration%a;
				c = b/60;
				d = b%c;
			} else {
				c = duration/60;
				if(c > 0) {
					d = duration%c;
				} else {
					d = duration;
				}
			}

			if(a<10) {
				stringDuration += "0"+a;
			} else {
				stringDuration += ""+a;
			}

			if(c<10) {
				stringDuration += ":0"+c;
			} else {
				stringDuration += ":"+c;
			}

			if(d<10) {
				stringDuration += ":0"+d;
			} else {
				stringDuration += ":"+d;
			}


		} else {
			stringDuration = "00:00:00";
		}

		return stringDuration;
	}

	/**
	 * Get Current System Time
	 * @return -current time
	 */
	public static  String getCurrentTime() {
		String time=null;
		Calendar c = Calendar.getInstance(); 
		int hours = c.get(Calendar.HOUR_OF_DAY);
		int minutes = c.get(Calendar.MINUTE);
		int seconds = c.get(Calendar.SECOND);
		String  sthr=hours+"";
		String stmin=minutes+"";
		String stsec=seconds+"";
		if(sthr.length()==1){
			sthr="0"+sthr;
		}
		if(stmin.length()==1){
			stmin="0"+stmin;
		}
		if(stsec.length()==1){
			stsec="0"+stsec;
		}
		time=sthr+":"+stmin+":"+stsec;
		return time;
	}

	public static Vector<String> getTimeSlots()
	{
		String[] time = {"12:00 AM", "12:30 AM","1:00 AM", "1:30 AM","2:00 AM", "2:30 AM","3:00 AM", "3:30 AM","4:00 AM", "4:30 AM","5:00 AM", "5:30 AM","6:00 AM", "6:30 AM","7:00 AM", "7:30 AM","8:00 AM", "8:30 AM","9:00 AM", "9:30 AM","10:00 AM", "10:30 AM","11:00 AM", "11:30 AM","12:00 PM", "12:30 PM","1:00 PM", "1:30 PM","2:00 PM", "2:30 PM","3:00 PM", "3:30 PM","4:00 PM", "4:30 PM","5:00 PM", "5:30 PM","6:00 PM", "6:30 PM","7:00 PM", "7:30 PM","8:00 PM", "8:30 PM","9:00 PM", "9:30 PM","10:00 PM", "10:30 PM","11:00 PM", "11:30 PM","12:00 AM"};
		Vector<String> times = new Vector<String>();
		times.addAll(Arrays.asList(time));
		return times;
	}
	public static  String getCurrentHour() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));
		int min = cal.get(Calendar.MINUTE);
		if( min < 30)
			min = 0;
		if(min >= 30)
			min = 30;
		int hr = cal.get(Calendar.HOUR_OF_DAY);
		String per = "AM";
		if(hr > 12){
			hr = hr - 12;
			per = "PM";
		}
		String strhr =  hr+ "";
		String strmn = min + "";

		if (strmn.length() == 1) {
			strmn = "0" + strmn;
		}

		String currTime = strhr + ":" + strmn + " "+per;
		return currTime;
	}

	/**
	 * get current date of the deivice
	 * @return date as string
	 */
	public static  String getCurrentDate() {
		String date=null;
		Calendar c = Calendar.getInstance(); 
		String mYear = c.get(Calendar.YEAR)+""; 
		String mMonth = (c.get(Calendar.MONTH) +1)+""; 
		String mDay = c.get(Calendar.DAY_OF_MONTH)+""; 
		if(mYear.length()==1){
			mYear="0"+mYear;
		}
		if(mMonth.length()==1){
			mMonth="0"+mMonth;
		}
		if(mDay.length()==1){
			mDay="0"+mDay;
		}
		date=mYear+"-"+mMonth+"-"+mDay;
		return date;
	}

	public static String getCurrentDatAndTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
		//dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));
		return dateFormat.format(calendar.getTime());
	}

	public static int MoneyConverter(String val){
		int cost = 0;
		if(!val.equalsIgnoreCase("")){
			cost = (int) Double.parseDouble(val);
		}
		return cost;
	}
	
	public static boolean isNetworkConnected(Context a) {
		ConnectivityManager cm = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
	    if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
	            && cm.getActiveNetworkInfo().isConnected()) {
	        return true;
	    } else {
	        return false;
	    }
	}
	
	
	
}