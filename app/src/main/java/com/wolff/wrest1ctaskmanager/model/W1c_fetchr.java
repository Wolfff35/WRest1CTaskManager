package com.wolff.wrest1ctaskmanager.model;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

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
    String TAG = "W1c_fetchr";

    private HttpURLConnection getConnection(String catalog){
        try {

            //URL url = new URL(Const.BASE_URL+catalog+"/"+Const.RESULT_FORMAT);
            String url_s = Uri.parse(Const.BASE_URL+catalog+"/").buildUpon().appendQueryParameter("$format","json").build().toString();

            URL url = new URL(url_s);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            String authString = Const.LOGIN+ ":" + Const.PASSWORD;
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

    public byte[] getUrlBytes(String catalog) throws IOException {
        //URL url = new URL(urlSpec);
       // HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        HttpURLConnection connection = getConnection(catalog);
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
    public String getUrlString(String catalog) throws IOException {
        return new String(getUrlBytes(catalog));
    }
//--------------------------------------------------------------------------------------------------
    public List<WUser> fetchWUsers(){
        List<WUser> wUsers = new ArrayList<>();
        try {
            String jsonStringUsers = getUrlString(Const.CATALOG_USERS);
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
public List<WContragent> fetchWContragents(){
    List<WContragent> wContragents = new ArrayList<>();
    try {
        String jsonStringUsers = getUrlString(Const.CATALOG_KONTRAG);
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
    public List<WTask> fetchWTasks(){
        List<WTask> wTasks = new ArrayList<>();
        try {
            String jsonStringUsers = getUrlString(Const.CATALOG_TASKS);
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
