package com.wolff.wrest1ctaskmanager.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolff.wrest1ctaskmanager.R;
import com.wolff.wrest1ctaskmanager.acivities.Activity_TaskItem;
import com.wolff.wrest1ctaskmanager.model.W1c_fetchr;
import com.wolff.wrest1ctaskmanager.model.WContragent;
import com.wolff.wrest1ctaskmanager.model.WTask;
import com.wolff.wrest1ctaskmanager.model.WUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wolff on 26.04.2017.
 */

public class Fragment_TaskList extends Fragment {
    private RecyclerView mTaskListRecyclerView;
    private List<WUser> mWUsersList;
    private List<WContragent> mContragentsList;
    private List<WTask> mTaskList = new ArrayList<>();

    public static Fragment_TaskList newInstance(){
        return new Fragment_TaskList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new GetWUsers_Task().execute();
        new GetWContragents_Task().execute();
        new GetWTasks_Task().execute();
        Log.e("","");
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
//==================================================================================================
private class TaskListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView mTitleTextView;
    private WTask mCurrentTask;

    public TaskListHolder(View itemView) {
        super(itemView);
        mTitleTextView = (TextView) itemView.findViewById(R.id.tvTaskDescribe);
        itemView.setOnClickListener(this);
    }
    public void bindTaskListItem(WTask item){
        mCurrentTask = item;
        mTitleTextView.setText(item.toString());
    }

    @Override
    public void onClick(View v) {
        Log.e("ON CLICK"," "+mCurrentTask.getDescription());
        Intent intent = Activity_TaskItem.newIntent(v.getContext(),mCurrentTask,(ArrayList<WTask>) mTaskList);
        startActivity(intent);
    }
}
//--------------------------------------------------------------------------------------------------
    private class TaskListAdapter extends RecyclerView.Adapter<TaskListHolder>{
    public TaskListAdapter(List<WTask> taskListItems){
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
        return mTaskList.size();
    }
}
//==================================================================================================

    public class GetWUsers_Task extends AsyncTask<Void,Void,List<WUser>> {
        String TAG = "GetWUsers_Task";

        @Override
        protected List<WUser> doInBackground(Void... params) {
            W1c_fetchr w1c_fetchr = new W1c_fetchr();
            return w1c_fetchr.fetchWUsers();
        }

        @Override
        protected void onPostExecute(List<WUser> wUsers) {
            super.onPostExecute(wUsers);
            mWUsersList = wUsers;
        }
    }
//==================================================================================================
public class GetWContragents_Task extends AsyncTask<Void,Void,List<WContragent>> {
    String TAG = "GetWContragents_Task";

    @Override
    protected List<WContragent> doInBackground(Void... params) {
        W1c_fetchr w1c_fetchr = new W1c_fetchr();
        return w1c_fetchr.fetchWContragents();
    }

    @Override
    protected void onPostExecute(List<WContragent> wContragents) {
        super.onPostExecute(wContragents);
        mContragentsList = wContragents;
    }
}
    //==================================================================================================
    public class GetWTasks_Task extends AsyncTask<Void,Void,List<WTask>> {
        String TAG = "GetWTasks_Task";

        @Override
        protected List<WTask> doInBackground(Void... params) {
            W1c_fetchr w1c_fetchr = new W1c_fetchr();
            return w1c_fetchr.fetchWTasks();
        }

        @Override
        protected void onPostExecute(List<WTask> wTasks) {
            super.onPostExecute(wTasks);
            mTaskList = wTasks;
            setupAdapter();
        }
    }
}
