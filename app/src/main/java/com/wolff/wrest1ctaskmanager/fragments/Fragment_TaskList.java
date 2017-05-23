package com.wolff.wrest1ctaskmanager.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wolff.wrest1ctaskmanager.R;
import com.wolff.wrest1ctaskmanager.acivities.Activity_TaskItem;
import com.wolff.wrest1ctaskmanager.model.WBase;
import com.wolff.wrest1ctaskmanager.model.WTask;
import com.wolff.wrest1ctaskmanager.model.WUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wolff on 26.04.2017.
 */

public class Fragment_TaskList extends Fragment {
    private Menu optionsMenu;
    private RecyclerView mTaskListRecyclerView;
    private ArrayList<WUser> mUsersList;
    private ArrayList<WBase> mBasesList;
    private ArrayList<WTask> mTaskList;// = new ArrayList<>();
    private String mCurrentUserGuid;

    private static final String ARG_CURRENT_USER_GUID = "CurrentUserGuid";
    private static final String ARG_USER_LIST = "WUserList";
    private static final String ARG_BASES_LIST = "WBasesList";
    private static final String ARG_TASK_LIST = "WTaskList";

    public static Fragment_TaskList newInstance(ArrayList<WUser>userList,ArrayList<WBase>basesList,ArrayList<WTask>taskList,String currentUserGuid){
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_LIST,taskList);
        args.putString(ARG_CURRENT_USER_GUID,currentUserGuid);
        args.putSerializable(ARG_USER_LIST,userList);
        args.putSerializable(ARG_BASES_LIST,basesList);

        Fragment_TaskList fragment = new Fragment_TaskList();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentUserGuid = getArguments().getString(ARG_CURRENT_USER_GUID);
        mUsersList = (ArrayList<WUser>)getArguments().getSerializable(ARG_USER_LIST);
        mBasesList = (ArrayList<WBase>)getArguments().getSerializable(ARG_BASES_LIST);
        mTaskList = (ArrayList<WTask>)getArguments().getSerializable(ARG_TASK_LIST);

        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tasklist,container,false);
        mTaskListRecyclerView = (RecyclerView)v.findViewById(R.id.fragment_tasklist_recyclerview);
        mTaskListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        return v;
    }
    private void setupAdapter(){
        if(isAdded()){
            mTaskListRecyclerView.setAdapter(new TaskListAdapter(mTaskList));
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.optionsMenu = menu;
        inflater.inflate(R.menu.fragment_tasklist_menu, menu);
        super.onCreateOptionsMenu(optionsMenu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
           case R.id.menu_addnewtask:
                addNewTask();
                break;
            default:
            break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void addNewTask(){
        Intent intent = Activity_TaskItem.newIntent(getContext(),null,null,mUsersList,mBasesList,mCurrentUserGuid);
        startActivity(intent);
    }
//==================================================================================================
private class TaskListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView mTitleTextView;
    private ImageView mTitleImageView;
    private WTask mCurrentTask;

    public TaskListHolder(View itemView) {
        super(itemView);
        mTitleTextView = (TextView) itemView.findViewById(R.id.tvTaskDescribe);
        mTitleImageView = (ImageView) itemView.findViewById(R.id.ivTaskDescribe);
        itemView.setOnClickListener(this);
    }
    public void bindTaskListItem(WTask item){
        mCurrentTask = item;

        if(mCurrentTask.isClosed()){
            mTitleTextView.setTypeface(null, Typeface.ITALIC);
        }
        if(mCurrentTask.isInWork()){
            mTitleTextView.setTypeface(null, Typeface.BOLD);
        }
        if(mCurrentTask.isDeletionMark()) {
            mTitleImageView.setImageResource(android.R.drawable.ic_delete);
         }else {
            mTitleImageView.setImageResource(android.R.drawable.checkbox_on_background);
        }
        mTitleTextView.setText(mCurrentTask.getDescription());
        mTitleTextView.setText(item.toString());
    }


 //-------------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v) {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = Activity_TaskItem.newIntent(v.getContext(), mCurrentTask, mTaskList, mUsersList, mBasesList, mCurrentUserGuid);
            startActivity(intent);
        }else {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            Fragment fragment = new Fragment_TaskItem().newInstance(v.getContext(),mCurrentTask,mCurrentUserGuid,mUsersList,mBasesList);
            fm.beginTransaction()
                    .replace(R.id.container_detail, fragment)
                    .commit();
            Log.e("on Click","ВЫВЕЛИ ФРАГМЕНТ DETAIL");

        }

    }
}

//--------------------------------------------------------------------------------------------------
    private class TaskListAdapter extends RecyclerView.Adapter<TaskListHolder>{
    public TaskListAdapter(ArrayList<WTask> taskListItems){
        mTaskList=taskListItems;
    }

    @Override
    public TaskListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.task_list_adapter_item,parent,false);
        return new TaskListHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskListHolder holder, int position) {
        WTask taskItem = mTaskList.get(position);
        holder.bindTaskListItem(taskItem);
    }

    @Override
    public int getItemCount() {
        if(mTaskList!=null) {
            return mTaskList.size();
        }else {
            return 0;
        }
    }
}

}
