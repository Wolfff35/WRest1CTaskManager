package com.wolff.wrest1ctaskmanager.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.wolff.wrest1ctaskmanager.model.Const;
import com.wolff.wrest1ctaskmanager.model.WContragent;
import com.wolff.wrest1ctaskmanager.model.WTask;
import com.wolff.wrest1ctaskmanager.model.WUser;
import com.wolff.wrest1ctaskmanager.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wolff on 26.04.2017.
 */

public class W1c_fetchr {
    public String getBaseUrl(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return "http://"+pref.getString("serverName","")+"/"+pref.getString("baseName","")+"/odata/standard.odata/";
    }

    public HttpURLConnection getConnection(Context context,String type,String catalog,String guidCurrentUser){
        try {
            String url_s;
            if(type.equalsIgnoreCase("GET")) {
                url_s = getBaseUrl(context) + catalog + "/?$format=json"+getFiltersForQuery(context,catalog,guidCurrentUser);
            }else if(type.equalsIgnoreCase("PATCH")) {
                url_s = Uri.parse(getBaseUrl(context) + catalog + "/").buildUpon().appendQueryParameter("$format", "json").build().toString();
            }else if(type.equalsIgnoreCase("POST")){
                url_s = Uri.parse(getBaseUrl(context) + catalog).buildUpon().build().toString();
            }else {
                url_s=null;
            }
            //Log.e("GET CONNECTION",""+url_s);
            URL url = new URL(url_s);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            String authString = getAuthorization1C(context);
            String authStringEnc = new String(Base64.encode(authString.getBytes(),0));
            connection.setRequestProperty("Authorization", "Basic " + authStringEnc);
            return connection;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String getAuthorization1C(Context context){
        //Const.LOGIN+ ":" + Const.PASSWORD
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString("serverLogin","")+":"+pref.getString("serverPassword","");
    }

    public byte[] getUrlBytes(Context context,String catalog,String guidCurrentUser) throws IOException {
        HttpURLConnection connection = getConnection(context,"GET",catalog,guidCurrentUser);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with ");
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    public String getUrlString(Context context,String catalog,String guidCurrentUser) throws IOException {
        return new String(getUrlBytes(context,catalog,guidCurrentUser));
    }
//--------------------------------------------------------------------------------------------------
private String getFiltersForQuery(Context context,String catalog, String guidCurrentUser){
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    boolean iAmAuthor = sp.getBoolean("iAmAuthor",false)&&guidCurrentUser!=null;
    boolean iAmProgrammer = sp.getBoolean("iAmProgrammer",false)&&guidCurrentUser!=null;
    boolean notDeleted = sp.getBoolean("notDeleted",false);
    boolean notFinished = sp.getBoolean("notFinished",false);
    boolean isFirst=false;
    StringBuffer sb = new StringBuffer();

     if(catalog.equalsIgnoreCase(Const.CATALOG_TASKS)){
        Log.e("GET FILTER","iAmAuthor = "+iAmAuthor);
        Log.e("GET FILTER","iAmProgrammer = "+iAmProgrammer);
        Log.e("GET FILTER","notDeleted = "+notDeleted);
        Log.e("GET FILTER","notFinished = "+notFinished);

        if(iAmAuthor|iAmProgrammer|notDeleted|notFinished){
            sb.append("&$filter=");
        }else{
            return "";
        }
        if (iAmAuthor){
            isFirst=true;
            sb.append("Автор_Key%20eq%20guid'"+guidCurrentUser+"'");
        }
        if (iAmProgrammer){
            if(isFirst){
                sb.append("%20and%20");
            }
            isFirst=true;
            sb.append("Исполнитель_Key%20eq%20guid'"+guidCurrentUser+"'");
        }

if (notDeleted){
            if(isFirst){
                sb.append("%20and%20");
            }
            isFirst=true;
            sb.append("DeletionMark%20eq%20false");
        }
        if (notFinished){
            if(isFirst){
                sb.append("%20and%20");
            }
            sb.append("фЗавершена%20eq%20false");
        }
        Log.e("GET FILTER",""+sb.toString());
        return  sb.toString();
    }
    return "";
}


    //--------------------------------------------------------------------------------------------------
    public List<WUser> fetchWUsers(Context context){
        List<WUser> wUsers = new ArrayList<>();
        try {
            String jsonStringUsers = getUrlString(context,Const.CATALOG_USERS,null);
            JSONObject jsonbody = new JSONObject(jsonStringUsers);
            parseWUsers(wUsers,jsonbody);
            return wUsers;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseWUsers(List<WUser> wUsers, JSONObject jsonBody) {

        try {
            JSONArray usersJsonArray = jsonBody.getJSONArray("value");
            for (int i = 0; i < usersJsonArray.length(); i++) {
                JSONObject userJsonObject = usersJsonArray.getJSONObject(i);
                WUser item = new WUser();
                item.setRef_Key(userJsonObject.getString("Ref_Key"));
                item.setDescription(userJsonObject.getString("Description"));
                item.setDeletionMark(userJsonObject.getBoolean("DeletionMark"));
                item.setCode(userJsonObject.getString("Code"));
                item.setGod(userJsonObject.getBoolean("фБог"));
                item.setProg(userJsonObject.getBoolean("фПрограммист"));
                item.setUniqId(userJsonObject.getString("УникальныйИД"));
                item.setUser(userJsonObject.getBoolean("фПользователь"));
                wUsers.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

      }
//--------------------------------------------------------------------------------------------------
public List<WContragent> fetchWContragents(Context context){
    List<WContragent> wContragents = new ArrayList<>();
    try {
        String jsonStringUsers = getUrlString(context,Const.CATALOG_KONTRAG,null);
        JSONObject jsonbody = new JSONObject(jsonStringUsers);
        parseWContragents(wContragents,jsonbody);
        return wContragents;
    } catch (IOException e) {
        e.printStackTrace();
    } catch (JSONException e) {
        e.printStackTrace();
    }
    return null;
}
    private void parseWContragents(List<WContragent> wContragents, JSONObject jsonBody) {
        try {
            JSONArray usersJsonArray = jsonBody.getJSONArray("value");
            for (int i = 0; i < usersJsonArray.length(); i++) {
                JSONObject userJsonObject = usersJsonArray.getJSONObject(i);
                WContragent item = new WContragent();
                item.setRef_Key(userJsonObject.getString("Ref_Key"));
                item.setDescription(userJsonObject.getString("Description"));
                item.setDeletionMark(userJsonObject.getBoolean("DeletionMark"));
                item.setCode(userJsonObject.getString("Code"));
                wContragents.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------------------------------
    public List<WTask> fetchWTasks(Context context, String guidCurrentUser){
        List<WTask> wTasks = new ArrayList<>();
        try {
            String jsonStringUsers = getUrlString(context,Const.CATALOG_TASKS,guidCurrentUser);
            JSONObject jsonbody = new JSONObject(jsonStringUsers);
            parseWTasks(wTasks,jsonbody);
            return wTasks;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void parseWTasks(List<WTask> wTasks, JSONObject jsonBody) {
         try {
            JSONArray usersJsonArray = jsonBody.getJSONArray("value");
            for (int i = 0; i < usersJsonArray.length(); i++) {
                JSONObject userJsonObject = usersJsonArray.getJSONObject(i);
                WTask item = new WTask();
                item.setRef_Key(userJsonObject.getString("Ref_Key"));
                item.setDescription(userJsonObject.getString("Description"));
                item.setDeletionMark(userJsonObject.getBoolean("DeletionMark"));
                item.setCode(userJsonObject.getString("Code"));
                item.setAuthor_Key(userJsonObject.getString("Автор_Key"));
                item.setProg_Key(userJsonObject.getString("Исполнитель_Key"));
                item.setContent(userJsonObject.getString("Содержание"));
                item.setNote(userJsonObject.getString("Примечание"));
                item.setClosed(userJsonObject.getBoolean("фЗавершена"));
                item.setDateCreation(new Utils().dateFromString(userJsonObject.getString("ДатаСоздания"),Const.DATE_FORMAT_STR));
                item.setDateClosed(new Utils().dateFromString(userJsonObject.getString("ДатаЗавершения"),Const.DATE_FORMAT_STR));
                item.setDateInWork(new Utils().dateFromString(userJsonObject.getString("ДатаПринятияВРаботу"),Const.DATE_FORMAT_STR));
                item.setInWork(userJsonObject.getBoolean("фПринятаВРаботу"));
                item.setBase_Key(userJsonObject.getString("База_Key"));

                wTasks.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
