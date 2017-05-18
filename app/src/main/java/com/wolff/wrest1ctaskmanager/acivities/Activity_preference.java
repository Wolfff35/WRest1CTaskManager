package com.wolff.wrest1ctaskmanager.acivities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wolff.wrest1ctaskmanager.R;
import com.wolff.wrest1ctaskmanager.fragments.Fragment_TaskList;
import com.wolff.wrest1ctaskmanager.fragments.Fragment_preference;

public class Activity_preference extends AppCompatActivity {


    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,Activity_preference.class);
        return intent;


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

     /*   FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_list);

        if (fragment == null) {
            fragment = new Fragment_preference().newInstance();
            fm.beginTransaction()
                    .add(R.id.fragment_list, fragment)
                    .commit();
        }*/
     showFragments();
    }
    protected int getLayoutResId(){
        return R.layout.activity_singlefragment;
    }

    private void showFragments(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new Fragment_preference().newInstance();
        fm.beginTransaction()
                .replace(R.id.container_list, fragment)
                .commit();
        Log.e("fhowFragments","ВЫВЕЛИ ФРАГМЕНТ");
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){

        }
    }

}
