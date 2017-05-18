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

import com.wolff.wrest1ctaskmanager.R;
import com.wolff.wrest1ctaskmanager.fragments.Fragment_TaskItem;
import com.wolff.wrest1ctaskmanager.model.WBase;
import com.wolff.wrest1ctaskmanager.model.WTask;
import com.wolff.wrest1ctaskmanager.model.WUser;

import java.util.ArrayList;

public class Activity_TaskItem extends AppCompatActivity {
    private ArrayList<WTask> mTaskList;
    private ArrayList<WUser> mUserList;
    private ArrayList<WBase> mBasesList;
    private String mCurrentUserGuid;
    public static final String EXTRA_CURRENT_TASK = "currentTask";
    public static final String EXTRA_TASK_LIST = "TaskList";
    public static final String EXTRA_USER_LIST = "UserList";
    public static final String EXTRA_BASES_LIST = "BasesList";
    public static final String EXTRA_CURRENT_USER_GUID = "CurrentUserGuid";

    public static Intent newIntent(Context context, WTask currentTask, ArrayList<WTask> taskList, ArrayList<WUser>userList, ArrayList<WBase>basesList, String currentUserGuid){
        Intent intent = new Intent(context,Activity_TaskItem.class);
        intent.putExtra(EXTRA_CURRENT_TASK,currentTask);
        intent.putExtra(EXTRA_TASK_LIST, taskList);
        intent.putExtra(EXTRA_USER_LIST, userList);
        intent.putExtra(EXTRA_BASES_LIST, basesList);
        intent.putExtra(EXTRA_CURRENT_USER_GUID, currentUserGuid);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_item);
        WTask currentTask = (WTask) getIntent().getSerializableExtra(EXTRA_CURRENT_TASK);
        mTaskList = (ArrayList<WTask>)getIntent().getSerializableExtra(EXTRA_TASK_LIST);
        mUserList = (ArrayList<WUser>)getIntent().getSerializableExtra(EXTRA_USER_LIST);
        mBasesList = (ArrayList<WBase>)getIntent().getSerializableExtra(EXTRA_BASES_LIST);
        mCurrentUserGuid = getIntent().getStringExtra(EXTRA_CURRENT_USER_GUID);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager_container);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                WTask item;
                if(mTaskList==null){
                    item = null;
                }else {
                    item = mTaskList.get(position);
                }
                return Fragment_TaskItem.newInstance(getApplicationContext(),item,mCurrentUserGuid,mUserList,mBasesList);
            }

            @Override
            public int getCount() {
                if(mTaskList==null){
                    return 1;
                }else {
                    return mTaskList.size();
                }
            }
        });
        if(mTaskList==null){
            viewPager.setCurrentItem(0);
        }else {
            viewPager.setCurrentItem(mTaskList.indexOf(currentTask));
        }
     }
}
