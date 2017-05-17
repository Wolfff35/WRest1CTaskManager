package com.wolff.wrest1ctaskmanager.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.wolff.wrest1ctaskmanager.R;
import com.wolff.wrest1ctaskmanager.acivities.Activity_TaskItem;
import com.wolff.wrest1ctaskmanager.acivities.Activity_preference;
import com.wolff.wrest1ctaskmanager.model.Const;
import com.wolff.wrest1ctaskmanager.tasks.W1c_fetchr;
import com.wolff.wrest1ctaskmanager.model.WContragent;
import com.wolff.wrest1ctaskmanager.model.WTask;
import com.wolff.wrest1ctaskmanager.model.WUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        if((pref.getString("serverName","").equalsIgnoreCase(""))|(pref.getString("serverLogin","").equalsIgnoreCase(""))){
            openPreferences();
        }

        new GetWUsers_Task().execute();
        new GetWContragents_Task().execute();

        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mUsersList!=null) {
            updateTaskList();
        }
    }

    private void updateTaskList() {
        Log.e("UPDATE TASK LIST","UPDATE TASK LIST");
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
        Intent intent = Activity_TaskItem.newIntent(getContext(),null,null,(ArrayList<WUser>) mUsersList,(ArrayList<WContragent>) mContragentsList,mCurrentUserGuid);
        startActivity(intent);
    }
    private void openPreferences(){
        Intent intent = Activity_preference.newIntent(getContext());
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
        Intent intent = Activity_TaskItem.newIntent(v.getContext(),mCurrentTask,(ArrayList<WTask>) mTaskList,(ArrayList<WUser>) mUsersList,(ArrayList<WContragent>)mContragentsList,mCurrentUserGuid);
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
        @Override
        protected List<WUser> doInBackground(Void... params) {
            W1c_fetchr w1c_fetchr = new W1c_fetchr();
            return w1c_fetchr.fetchWUsers(getActivity().getApplicationContext());
        }

        @Override
        protected void onPostExecute(List<WUser> wUsers) {
            super.onPostExecute(wUsers);
            mUsersList = wUsers;
            //Toast toast = Toast.makeText(getContext(),"Обновлен список пользователей",Toast.LENGTH_LONG);
            //toast.show();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
            for(WUser item:mUsersList){
                if(item.getDescription().equalsIgnoreCase(pref.getString("serverLogin",""))){
                    mCurrentUserGuid=item.getRef_Key();
                    Log.e("GetWUsers_Task","mCurrentUserGuid = "+mCurrentUserGuid);
                }
            }
            updateTaskList();
        }
    }
//==================================================================================================
public class GetWContragents_Task extends AsyncTask<Void,Void,List<WContragent>> {
    @Override
    protected List<WContragent> doInBackground(Void... params) {
        W1c_fetchr w1c_fetchr = new W1c_fetchr();
        return w1c_fetchr.fetchWContragents(getActivity().getApplicationContext());
    }

    @Override
    protected void onPostExecute(List<WContragent> wContragents) {
        super.onPostExecute(wContragents);
        mContragentsList = wContragents;
        //Toast toast = Toast.makeText(getContext(),"Обновлен список контрагентов",Toast.LENGTH_LONG);
        //toast.show();
    }
}
    //==================================================================================================
    public class GetWTasks_Task extends AsyncTask<Void,Void,List<WTask>> {
        @Override
        protected List<WTask> doInBackground(Void... params) {
            W1c_fetchr w1c_fetchr = new W1c_fetchr();
            return w1c_fetchr.fetchWTasks(getActivity().getApplicationContext(),mCurrentUserGuid);
        }

        @Override
        protected void onPostExecute(List<WTask> wTasks) {
            super.onPostExecute(wTasks);
            mTaskList = wTasks;
            Toast toast = Toast.makeText(getContext(),"Обновлен список задач",Toast.LENGTH_LONG);
            toast.show();
            setupAdapter();
            setOptionsMenuVisibility(true);
        }
    }
}
