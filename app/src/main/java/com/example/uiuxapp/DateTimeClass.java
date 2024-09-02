package com.example.uiuxapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeClass {

    public String getMillis(String date, String format){
        SimpleDateFormat getFormat = new SimpleDateFormat(format);
        try {
            Date getDate = getFormat.parse(date);
            long getMillis = getDate.getTime();
            return String.valueOf(getMillis);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getCurrentMillis(){
        Date date = new Date();
        long getMillis = date.getTime();
        return String.valueOf(getMillis);
    }

    public String millisToDate(String millis , String format){
        // use hh:mm a for 24 hour format
        long milliSec = Long.parseLong(millis);
        DateFormat simple = new SimpleDateFormat(format);
        Date date =  new Date();
        date.setTime(milliSec);
        return String.valueOf(simple.format(date));
    }

    public  void DatePicker(Context context , EditText editText, TextView textView){
        // Get Current Date

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            if (editText !=null){
                                editText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                            else if(textView !=null){
                                textView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }

    public void TimePicker(Context context, EditText editText, TextView textView){

        // Get Current Time
        final Calendar c;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            if (editText !=null){
                                editText.setText(hourOfDay + ":" + (minute + 1));
                            }
                            else if(textView !=null){
                                textView.setText(hourOfDay + ":" + (minute + 1));
                            }
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }


    }
    public String getCurrectMillis(){
        Date date = new Date();
        long getMillis = date.getTime();
        return String.valueOf(getMillis);
    }
}
