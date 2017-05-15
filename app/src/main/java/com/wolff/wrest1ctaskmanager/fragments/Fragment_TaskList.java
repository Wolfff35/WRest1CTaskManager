package com.wolff.wrest1ctaskmanager.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolff.wrest1ctaskmanager.R;
import com.wolff.wrest1ctaskmanager.acivities.Activity_TaskItem;
import com.wolff.wrest1ctaskmanager.acivities.Activity_preference;
import com.wolff.wrest1ctaskmanager.model.Const;
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
    private Menu optionsMenu;
    private RecyclerView mTaskListRecyclerView;
    private List<WUser> mUsersList;
    private List<WContragent> mContragentsList;
    private List<WTask> mTaskList = new ArrayList<>();
    private String mCurrentUserGuid;
    public static Fragment_TaskList newInstance(){
        return new Fragment_TaskList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        updateTaskList();
        Log.e("","");
    }

    private void updateTaskList() {
        new GetWUsers_Task().execute();
        new GetWContragents_Task().execute();
        new GetWTasks_Task().execute();
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
            case R.id.menu_updatelist:
                updateTaskList();
                setOptionsMenuVisibility(false);
            break;
            case R.id.menu_addnewtask:
                addNewTask();
                break;
            case R.id.menu_preferences:
                openPreferences();
                break;
            default:


            break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setOptionsMenuVisibility(boolean isVisible){
        if(optionsMenu!=null) {
            MenuItem item_edit = optionsMenu.findItem(R.id.menu_updatelist);
            item_edit.setVisible(isVisible);
        }
    }
    private void addNewTask(){
        Log.e("ADD NEW TASK"," NULL");
        Intent intent = Activity_TaskItem.newIntent(getContext(),null,null,(ArrayList<WUser>) mUsersList,(ArrayList<WContragent>) mContragentsList);
        startActivity(intent);

    }
    private void openPreferences(){
        Log.e("OPEN PREF"," NULL");
        Intent intent = Activity_preference.newIntent(getContext());
        startActivity(intent);

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
      //
        //String label = formatListItem(mCurrentTask);
        if(mCurrentTask.isClosed()){
            mTitleTextView.setTypeface(null, Typeface.ITALIC);
        }
        if(mCurrentTask.isInWork()){
            mTitleTextView.setTypeface(null, Typeface.BOLD);
        }
        if(mCurrentTask.isDeletionMark()) {
         }
        //mTitleTextView.setText(Html.fromHtml(label));
        mTitleTextView.setText(mCurrentTask.getDescription());
        //mTitleTextView.setText("Автор: "+mCurrentTask.getAuthor().getName()+", Дата создания:"+convert.dateToString(mCurrentTask.getDateCreate(),DATE_FORMAT_VID));


        //
        mTitleTextView.setText(item.toString());
    }


 //-------------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v) {
        Log.e("ON CLICK"," "+mCurrentTask.getDescription());
        Intent intent = Activity_TaskItem.newIntent(v.getContext(),mCurrentTask,(ArrayList<WTask>) mTaskList,(ArrayList<WUser>) mUsersList,(ArrayList<WContragent>)mContragentsList);
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
            return w1c_fetchr.fetchWUsers(getActivity().getApplicationContext());
        }

        @Override
        protected void onPostExecute(List<WUser> wUsers) {
            super.onPostExecute(wUsers);
            mUsersList = wUsers;
            for(WUser item:mUsersList){
                if(item.getDescription().equalsIgnoreCase(Const.LOGIN)){
                    mCurrentUserGuid=item.getRef_Key();
                }
            }
        }
    }
//==================================================================================================
public class GetWContragents_Task extends AsyncTask<Void,Void,List<WContragent>> {
    String TAG = "GetWContragents_Task";

    @Override
    protected List<WContragent> doInBackground(Void... params) {
        W1c_fetchr w1c_fetchr = new W1c_fetchr();
        return w1c_fetchr.fetchWContragents(getActivity().getApplicationContext());
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
            return w1c_fetchr.fetchWTasks(getActivity().getApplicationContext(),mCurrentUserGuid);
        }

        @Override
        protected void onPostExecute(List<WTask> wTasks) {
            super.onPostExecute(wTasks);
            mTaskList = wTasks;
            setupAdapter();
            setOptionsMenuVisibility(true);
        }
    }
}
