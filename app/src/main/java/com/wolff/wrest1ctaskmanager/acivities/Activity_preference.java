package com.wolff.wrest1ctaskmanager.acivities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.wolff.wrest1ctaskmanager.fragments.Fragment_preference;

public class Activity_preference extends Activity_SingleFragment {

    @Override
    protected Fragment createFragment() {
        return new Fragment_preference().newInstance();
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,Activity_preference.class);
        return intent;
    }

}
