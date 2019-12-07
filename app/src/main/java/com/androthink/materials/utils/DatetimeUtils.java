package com.androthink.materials.utils;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatetimeUtils {

    public static String formatDateTime(String dateString,String datetimePattern,String resultDatetimePattern) {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat(datetimePattern, Locale.ENGLISH);
        Date convertedDate = null;
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (convertedDate != null) {
            dateFormat.applyPattern(resultDatetimePattern);
            return dateFormat.format(convertedDate);
        }else
            return dateString;
    }

    @NonNull
    public static String formatDateTime(Date date,String datetimePattern) {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat(datetimePattern, Locale.ENGLISH);
        return dateFormat.format(date);
    }

    @NonNull
    public static String getCurrentDateTime(String datetimePattern) {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat(datetimePattern, Locale.ENGLISH);
        return dateFormat.format(new Date());
    }

    @NonNull
    public static String getCurrentDateTime() {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        return dateFormat.format(new Date());
    }

    @NonNull
    public static String getCurrentTimeStamp(String timeStampPattern) {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat(timeStampPattern, Locale.ENGLISH);
        return dateFormat.format(new Date());
    }

    @NonNull
    public static String getCurrentTimeStamp() {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm_aa", Locale.ENGLISH);
        return dateFormat.format(new Date());
    }

    public static boolean isToday(String dateTime,String datetimePattern) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat(datetimePattern, Locale.ENGLISH);
        try {
            date = format.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date current = new Date();
        return !(date != null && current.compareTo(date) > 0);
    }

    public static boolean isToday(Date date) {
        return !(date != null && new Date().compareTo(date) > 0);
    }

    public static boolean isSameDate(String firstDate,String secondDate , String datetimePattern) {
        Date first = null;
        Date second = null;
        SimpleDateFormat format = new SimpleDateFormat(datetimePattern, Locale.ENGLISH);
        try {
            first = format.parse(firstDate);
            second = format.parse(secondDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ((first != null && second != null)&& second.compareTo(first) > 0);
    }

    public static boolean isSameDate( Date first,Date second) {
        return ((first != null && second != null)&& second.compareTo(first) > 0);
    }

    @NonNull
    public static Date addHours(Date date,int amount){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, amount);
        return calendar.getTime();
    }

    @NonNull
    public static Date addMinutes(Date date,int amount){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, amount);
        return calendar.getTime();
    }
}