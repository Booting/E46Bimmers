package com.e46bimmerscommunity.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Referensi {
	// Default dari link utama tempat service
	public static String url="http://e46bimmers.id/e46bimmerscommunity";
	//public static String url="http://192.168.222.53/e46bimmerscommunity";
	public static String PREF_NAME = "bimmers";
	
	public static long getRemainingDays(long lastUpdate) {
        Date dateLastUpdate     = new Date(lastUpdate);
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        dateformat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        getSimpleDateFormatHours().setTimeZone(TimeZone.getTimeZone("GMT+8"));
        long currentMillis = java.lang.System.currentTimeMillis();
        Date dtLastUpdate  = null;
        try {
        	dtLastUpdate = dateformat.parse(dateformat.format(dateLastUpdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long eventMillis   = dtLastUpdate.getTime();
        long diffMillis    = currentMillis - eventMillis;
        long remainingDays = diffMillis / 86400000;

        return remainingDays;
    }
	
	public static SimpleDateFormat getSimpleDateFormatHours() {
        SimpleDateFormat dateformatHours = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        return dateformatHours;
    }
	
	public static long getCurrentMillis() {
        long currentMillis = java.lang.System.currentTimeMillis();
        return currentMillis;
    }
	
	public static String currencyFormater(Double value) {
        DecimalFormat myFormatter = new DecimalFormat("###,###,###");
        return myFormatter.format(value);
    }
}  