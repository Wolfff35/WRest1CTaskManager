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
import android.widget.ArrayAdapter;

import com.wolff.wrest1ctaskmanager.R;
import com.wolff.wrest1ctaskmanager.fragments.Fragment_TaskItem;
import com.wolff.wrest1ctaskmanager.model.WContragent;
import com.wolff.wrest1ctaskmanager.model.WTask;
import com.wolff.wrest1ctaskmanager.model.WUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Activity_TaskItem extends AppCompatActivity {
    private List<WTask> mWTaskList;
    private List<WUser> mUserList;
    private List<WContragent> mContragentList;
    public static final String EXTRA_CURRENT_TASK = "currentTask";
    public static final String EXTRA_TASK_LIST = "TaskList";
    public static final String EXTRA_USER_LIST = "UserList";
    public static final String EXTRA_CONTRAGENT_LIST = "ContragentList";

    public static Intent newIntent(Context context, WTask currentTask, ArrayList<WTask> taskList, ArrayList<WUser>userList,ArrayList<WContragent>contragentList){
        Intent intent = new Intent(context,Activity_TaskItem.class);
        intent.putExtra(EXTRA_CURRENT_TASK,currentTask);
        intent.putExtra(EXTRA_TASK_LIST, taskList);
        intent.putExtra(EXTRA_USER_LIST, userList);
        intent.putExtra(EXTRA_CONTRAGENT_LIST, contragentList);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_item);
        WTask currentTask = (WTask) getIntent().getSerializableExtra(EXTRA_CURRENT_TASK);
        mWTaskList = (List<WTask>)getIntent().getSerializableExtra(EXTRA_TASK_LIST);
        mUserList = (List<WUser>)getIntent().getSerializableExtra(EXTRA_USER_LIST);
        mContragentList = (List<WContragent>)getIntent().getSerializableExtra(EXTRA_CONTRAGENT_LIST);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager_container);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                WTask item;
                if(mWTaskList==null){
                    item = null;
                }else {
                    item = mWTaskList.get(position);
                }
                return Fragment_TaskItem.newInstance(item,getApplicationContext());
            }

            @Override
            public int getCount() {
                if(mWTaskList==null){
                    return 1;
                }else {
                    return mWTaskList.size();
                }
            }
        });
        if(mWTaskList==null){
            viewPager.setCurrentItem(0);
        }else {
            viewPager.setCurrentItem(mWTaskList.indexOf(currentTask));
        }
     }
}
