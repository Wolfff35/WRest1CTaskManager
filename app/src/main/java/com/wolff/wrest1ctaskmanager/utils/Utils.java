package com.wolff.wrest1ctaskmanager.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wolff on 27.04.2017.
 */

public class Utils {
    public Date dateFromString(String strDate, String strFormat){
        //2017-02-02T15:30:00
        DateFormat format = new SimpleDateFormat(strFormat, Locale.ENGLISH);
        try {
            Date date = format.parse(strDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String dateToString(Date date,String strFormat){
        DateFormat format = new SimpleDateFormat(strFormat, Locale.ENGLISH);
        String strDate = format.format(date);
        return strDate;
    }

}
