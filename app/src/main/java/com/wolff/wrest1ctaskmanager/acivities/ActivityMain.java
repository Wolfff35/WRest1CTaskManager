package com.wolff.wrest1ctaskmanager.acivities;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.wolff.wrest1ctaskmanager.fragments.Fragment_TaskList;
import com.wolff.wrest1ctaskmanager.R;

public class ActivityMain extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new Fragment_TaskList();
    }

 }
