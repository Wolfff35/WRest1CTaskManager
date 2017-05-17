package com.wolff.wrest1ctaskmanager.acivities;

import android.support.v4.app.Fragment;

import com.wolff.wrest1ctaskmanager.fragments.Fragment_TaskList;

public class Activity_Main_TaskList extends Activity_SingleFragment {

    @Override
    protected Fragment createFragment() {
        return new Fragment_TaskList().newInstance();
    }

 }
