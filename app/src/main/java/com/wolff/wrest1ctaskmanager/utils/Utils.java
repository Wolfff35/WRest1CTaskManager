package com.wolff.wrest1ctaskmanager.utils;

import android.content.Context;
import android.util.Log;

import com.wolff.wrest1ctaskmanager.model.Const;
import com.wolff.wrest1ctaskmanager.model.W1c_fetchr;
import com.wolff.wrest1ctaskmanager.model.WTask;
import com.wolff.wrest1ctaskmanager.model.WUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.wolff.wrest1ctaskmanager.model.Const.DATE_FORMAT_STR;

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
        if(date==null){
            return "";
        }
        DateFormat format = new SimpleDateFormat(strFormat, Locale.ENGLISH);
        String strDate = format.format(date);
        return strDate;
    }
//
public String getStringFromInputStream(InputStream is){
    BufferedReader r = new BufferedReader(new InputStreamReader(is));
    StringBuilder total = new StringBuilder();
    String line;
    try {
        while ((line = r.readLine()) != null) {
            total.append(line).append('\n');
        }
        return total.toString();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}
    public String format_task_body_to_save(WTask task){
        StringBuffer sb = new StringBuffer();
        addField(sb,"Description",task.getDescription());
        addField(sb,"Автор_Key",task.getAuthor_Key());
        addField(sb,"Исполнитель_Key",task.getProg_Key());
        addField(sb,"Содержание",task.getContent());
        addField(sb,"Примечание",task.getNote());
        addField(sb,"фЗавершена",String.valueOf(task.isClosed()));
        addField(sb,"ДатаСоздания",dateToString(task.getDateCreation(),DATE_FORMAT_STR));
        addField(sb,"ДатаЗавершения",dateToString(task.getDateClosed(),DATE_FORMAT_STR));
        addField(sb,"База_Key",task.getBase_Key());
        addField(sb,"фПринятаВРаботу",String.valueOf(task.isInWork()));
        addField(sb,"ДатаПринятияВРаботу",dateToString(task.getDateInWork(),DATE_FORMAT_STR));
        addField(sb,"DeletionMark","false");
        Log.e("formattosave",""+sb.toString());
        return sb.toString();
    }

    private void addField(StringBuffer sb,String fieldName,String field){
        Log.e("addField",""+fieldName+": "+field);
        if(field!=null&&field!="") {
            sb.append("<d:" + fieldName + ">" + field + "</d:" + fieldName + ">");
        }
    }

    public String format_task_patch_update(WTask task){
         JSONObject object = new JSONObject();
        W1c_fetchr fetchr = new W1c_fetchr();
        try {
            object.put("odata.metadata",""+ fetchr.getBaseUrl()+"$metadata#Catalog_Tasks/@Element");
            object.put("Description",task.getDescription());
            object.put("Содержание",task.getContent());
            object.put("Примечание",task.getNote());
            object.put("фЗавершена",task.isClosed());
            if((task.isClosed())&&(task.getDateClosed()!=null)) {
                object.put("ДатаЗавершения", dateToString(task.getDateClosed(), DATE_FORMAT_STR));
            }
            object.put("фПринятаВРаботу",task.isInWork());
            if((task.isInWork())&&(task.getDateInWork()!=null)) {
                object.put("ДатаПринятияВРаботу", dateToString(task.getDateInWork(), DATE_FORMAT_STR));
            }
            return object.toString();
        } catch (JSONException e) {
            Log.e("ERROR"," = "+e.getLocalizedMessage());
            return null;
        }
    }
    public String format_task_patch_delete(WTask task){
        JSONObject object = new JSONObject();
        W1c_fetchr fetchr = new W1c_fetchr();
        try {
            object.put("odata.metadata",""+ fetchr.getBaseUrl()+"$metadata#Catalog_Tasks/@Element");
            object.put("DeletionMark","true");
            return object.toString();
        } catch (JSONException e) {
            Log.e("ERROR"," = "+e.getLocalizedMessage());
            return null;
        }
    }

}
