package com.wolff.wrest1ctaskmanager.acivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.wolff.wrest1ctaskmanager.R;
import com.wolff.wrest1ctaskmanager.fragments.Fragment_TaskList;
import com.wolff.wrest1ctaskmanager.model.WBase;
import com.wolff.wrest1ctaskmanager.model.WTask;
import com.wolff.wrest1ctaskmanager.model.WUser;
import com.wolff.wrest1ctaskmanager.tasks.W1c_fetchr;

import java.util.ArrayList;

public class Activity_Main extends AppCompatActivity {
    private Menu optionsMenu;
    private ArrayList<WUser> mUsersList;
    private ArrayList<WBase> mBasesList;
    private ArrayList<WTask> mTaskList;
    private String mCurrentUserGuid;
      protected int getLayoutResId(){
        return R.layout.activity_singlefragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if((pref.getString("serverName","").equalsIgnoreCase(""))|(pref.getString("serverLogin","").equalsIgnoreCase(""))){
            openPreferences();
        }
     }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ON RESUME","RESUME");
;        updateTaskList();
    }

    private void openPreferences(){
        Intent intent = Activity_preference.newIntent(getApplicationContext());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_updatelist:
                updateTaskList();
                setOptionsMenuVisibility(false);
            break;
            case R.id.menu_preferences:
                openPreferences();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setOptionsMenuVisibility(boolean isVisible){
        if(optionsMenu!=null) {
            MenuItem item_edit = optionsMenu.findItem(R.id.menu_updatelist);
            item_edit.setVisible(isVisible);
        }
    }

private void updateTaskList() {
   new GetDataFromServer1c_Task().execute();
    Log.e("updateTaskList","UPDATE");

}
private void showFragments(){
    FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new Fragment_TaskList().newInstance(mUsersList,mBasesList,mTaskList,mCurrentUserGuid);
        fm.beginTransaction()
                .replace(R.id.container_list, fragment)
                .commit();
    Log.e("fhowFragments","ВЫВЕЛИ ФРАГМЕНТ");
    if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){

    }
}
//==================================================================================================
    public class GetDataFromServer1c_Task extends AsyncTask<Void,Void,Void>{

    @Override
    protected Void doInBackground(Void... params) {
        W1c_fetchr w1c_fetchr = new W1c_fetchr();
        mUsersList=w1c_fetchr.fetchWUsers(getApplicationContext());
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        for(WUser item:mUsersList){
            if(item.getDescription().equalsIgnoreCase(pref.getString("serverLogin",""))){
                mCurrentUserGuid=item.getRef_Key();
                Log.e("GET DATA ","CURRENT USER = "+mCurrentUserGuid);
            }
        }

        mBasesList= w1c_fetchr.fetchWBases(getApplicationContext());
        mTaskList= w1c_fetchr.fetchWTasks(getApplicationContext(),mCurrentUserGuid);

        return null;
    }
    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
        Log.e("GetWTasks_Task","ОБНОВИЛИ СПИСОК ЗАДАЧ");
        if(mUsersList==null){
            Snackbar.make(null,"NO CONNECTIONS WITH SERVER", Snackbar.LENGTH_LONG).setAction("Action",null).show();
            return;
        }else {
            showFragments();
            setOptionsMenuVisibility(true);
            Toast toast = Toast.makeText(getApplicationContext(),"Обновлен список задач",Toast.LENGTH_LONG);
            toast.show();
        }
    }

}

}
