package com.androthink.materials.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;

import com.androthink.materials.callback.SelectDatePikerCallback;

import java.util.Calendar;

public class SelectDatePickerDialog implements DatePickerDialog.OnDateSetListener, DatePickerDialog.OnCancelListener{

    private final Calendar calendar;
    private final DatePickerDialog pickerDialog;
    private SelectDatePikerCallback callback;

    public static SelectDatePickerDialog getInstance(Context context,SelectDatePikerCallback callback) {
        return new SelectDatePickerDialog(context,callback);
    }

    public static SelectDatePickerDialog getInstance(Context context,int themeId,SelectDatePikerCallback callback) {
        return new SelectDatePickerDialog(context,themeId,callback);
    }

    private SelectDatePickerDialog(Context context,SelectDatePikerCallback callback) {
        this.calendar = Calendar.getInstance();
        this.callback = callback;

        this.pickerDialog = new DatePickerDialog(context,this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        this.pickerDialog.setCancelable(false);
        this.pickerDialog.setOnCancelListener(this);
    }

    private SelectDatePickerDialog(Context context,int themeId,SelectDatePikerCallback callback) {
        this.calendar = Calendar.getInstance();
        this.callback = callback;

        this.pickerDialog = new DatePickerDialog(context, themeId, this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        this.pickerDialog.setCancelable(false);
        this.pickerDialog.setOnCancelListener(this);
    }

    public SelectDatePickerDialog setTitle(String title){
        this.pickerDialog.setTitle(title);
        return this;
    }

    public SelectDatePickerDialog setMessage(String message){
        this.pickerDialog.setMessage(message);
        return this;
    }

    public void show(){
        this.pickerDialog.show();
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        callback.onDateSelected(calendar.getTime());
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        callback.onCancel();
    }

    public static class Themes{
        public static final int HOLO = AlertDialog.THEME_HOLO_LIGHT;
        public static final int HOLO_DARK = AlertDialog.THEME_HOLO_DARK;
        public static final int TRADITIONAL = AlertDialog.THEME_TRADITIONAL;
        public static final int DEFAULT_DARK = AlertDialog.THEME_DEVICE_DEFAULT_DARK;
    }
}