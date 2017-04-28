package com.wolff.wrest1ctaskmanager.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.wolff.wrest1ctaskmanager.R;
import com.wolff.wrest1ctaskmanager.model.WTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_TaskItem extends Fragment {
    private static final String ARG_WTASK = "WTask";
    private WTask mCurrentTask;
    private boolean isNewItem;

    private TextView tvCode;
    private EditText edDescription;
    private EditText edContent;
    private Switch switchIsInWork;
    private Switch switchIsClosed;

    public static Fragment_TaskItem newInstance(WTask item, Context context) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_WTASK,item);

        Fragment_TaskItem fragment = new Fragment_TaskItem();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mCurrentTask = (WTask)getArguments().getSerializable(ARG_WTASK);
        if(mCurrentTask==null){
            isNewItem=true;
            mCurrentTask = new WTask();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_taskitem, container, false);
        tvCode = (TextView) v.findViewById(R.id.tvCode);
        edDescription = (EditText) v.findViewById(R.id.edDescription);
        edContent = (EditText) v.findViewById(R.id.edContent);
        switchIsInWork = (Switch) v.findViewById(R.id.switchIsInWork);
        switchIsClosed = (Switch) v.findViewById(R.id.switchIsClosed);
        tvCode.setText(mCurrentTask.getCode());
        edDescription.setText(mCurrentTask.getDescription());
        edContent.setText(mCurrentTask.getContent());
        switchIsInWork.setChecked(mCurrentTask.isInWork());
        switchIsClosed.setChecked(mCurrentTask.isClosed());


        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
