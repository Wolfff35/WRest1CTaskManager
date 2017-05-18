package com.wolff.wrest1ctaskmanager.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;

import static com.wolff.wrest1ctaskmanager.model.Const.CONNECT_TIMEOUT;
import static com.wolff.wrest1ctaskmanager.model.Const.READ_TIMEOUT;

/**
 * Created by wolff on 10.05.2017.
 */

public class PostDataToServer {

    public static int postDataToServer1c(Context context, String type, String data, String catalog, String guidTask) {
        int responseCode = 0;

        W1c_fetchr fetchr = new W1c_fetchr();
        HttpURLConnection conn;
        if(type.equalsIgnoreCase("POST")) {
            conn = fetchr.getConnection(context,type,catalog,null);

        }else {
            conn = fetchr.getConnection(context,type,catalog+"(guid'"+guidTask+"')",null);
        }
        conn.setReadTimeout(READ_TIMEOUT);
        conn.setConnectTimeout(CONNECT_TIMEOUT);
        try {
            conn.setRequestMethod(type);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes().length));

            OutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(data.getBytes());
            os.flush();
            conn.connect();

            responseCode = conn.getResponseCode();
            if ((responseCode >= 200) && (responseCode <= 203)) {
                //is = conn.getInputStream();
                Log.e("SUCCESS", "code== " + responseCode);
            } else {
                //is = conn.getErrorStream();
                Log.e("ERROR", "code== " + responseCode+"; "+conn.getErrorStream().toString());
            }
        } catch (ProtocolException e) {
           // e.printStackTrace();
            Log.e("!!ProtocolException",""+e.getLocalizedMessage());
        } catch (IOException e) {
           // e.printStackTrace();
            Log.e("!!IOException",""+e.getLocalizedMessage());
        } finally {
            conn.disconnect();

        }
        return responseCode;

    }


    public static class PostDataToServer_Task extends AsyncTask<String, Void, Integer> {
        private Context mContext;
        public PostDataToServer_Task(Context context){
            mContext=context;
        }
        @Override
        protected Integer doInBackground(String... params) {

            int res = postDataToServer1c(mContext,params[0], params[1],params[2],params[3]);
            return res;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            Toast toast;
            if ((s == 200) | (s == 201)) {
                    toast = Toast.makeText(mContext, "Запись успешно сохранена", Toast.LENGTH_LONG);
                Log.e("PostDataToServer_Task", "Запись успешно сохранена");
            } else {
                    toast = Toast.makeText(mContext, "Не удалось сохранить запись", Toast.LENGTH_LONG);
                Log.e("PostDataToServer_Task", "Не удалось сохранить запись");
            }
            toast.show();

        }

    }
}
