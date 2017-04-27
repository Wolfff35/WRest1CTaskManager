package com.wolff.wrest1ctaskmanager.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolff.wrest1ctaskmanager.R;
import com.wolff.wrest1ctaskmanager.model.WTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_TaskItem extends Fragment {
    private static final String ARG_WTASK = "WTask";
    public static Fragment_TaskItem newInstance(WTask item, Context context) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_WTASK,item);

        Fragment_TaskItem fragment = new Fragment_TaskItem();
        fragment.setArguments(args);
        return fragment;
    }

    //public Fragment_TaskItem() {
        // Required empty public constructor
    //}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_taskitem, container, false);
    }

}
