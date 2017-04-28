package com.wolff.wrest1ctaskmanager.acivities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wolff.wrest1ctaskmanager.R;
import com.wolff.wrest1ctaskmanager.fragments.Fragment_TaskItem;
import com.wolff.wrest1ctaskmanager.model.WTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Activity_TaskItem extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<WTask> mWTaskList;
    private WTask mCurrentTask;
    public static final String EXTRA_CURRENT_TASK = "currentTask";
    public static final String EXTRA_TASK_LIST = "TaskList";

    public static Intent newIntent(Context context, WTask currentTask,ArrayList<WTask> taskList){
        Intent intent = new Intent(context,Activity_TaskItem.class);
        intent.putExtra(EXTRA_CURRENT_TASK,currentTask);
        intent.putExtra(EXTRA_TASK_LIST, taskList);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_item);
        mCurrentTask = (WTask) getIntent().getSerializableExtra(EXTRA_CURRENT_TASK);
        mWTaskList = (List<WTask>)getIntent().getSerializableExtra(EXTRA_TASK_LIST);
        mViewPager = (ViewPager)findViewById(R.id.viewPager_container);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                WTask item = mWTaskList.get(position);
                return Fragment_TaskItem.newInstance(item,getApplicationContext());
            }

            @Override
            public int getCount() {
                return mWTaskList.size();
            }
        });
    mViewPager.setCurrentItem(mWTaskList.indexOf(mCurrentTask));
        /*for (int i = 0; i < mWTaskList.size(); i++) {
            if (mWTaskList.get(i).getRef_Key().equals(mCurrentTask.getRef_Key())) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }*/

    }
}
