package com.wolff.wrest1ctaskmanager.fragments;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolff.wrest1ctaskmanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_preference extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences_general);
    }

    public static Fragment_preference newInstance(){
        return new Fragment_preference();
    }



}
