package com.wolff.wrest1ctaskmanager.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.wolff.wrest1ctaskmanager.R;
import com.wolff.wrest1ctaskmanager.model.WTask;
import com.wolff.wrest1ctaskmanager.tasks.PostDataToServer;
import com.wolff.wrest1ctaskmanager.utils.Utils;

import java.io.InputStream;
import java.util.Date;

import static com.wolff.wrest1ctaskmanager.model.Const.CATALOG_TASKS;
import static com.wolff.wrest1ctaskmanager.model.Const.DATE_FORMAT_STR;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_TaskItem extends Fragment {
    private static final String ARG_WTASK = "WTask";
    private WTask mCurrentTask;
    private boolean isNewItem;
    private boolean isDataChanged;

    private TextView tvCode;
    private EditText edDescription;
    private EditText edContent;
    private Switch switchIsInWork;
    private Switch switchIsClosed;
    private Menu optionsMenu;

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
            isDataChanged=true;
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

        edDescription.addTextChangedListener(textChangedListener);
        edContent.addTextChangedListener(textChangedListener);
        switchIsClosed.setOnCheckedChangeListener(switchChangedListener);
        switchIsInWork.setOnCheckedChangeListener(switchChangedListener);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.optionsMenu = menu;
        inflater.inflate(R.menu.fragment_taskitem_menu, menu);
        setOptionsMenuVisibility();
        super.onCreateOptionsMenu(optionsMenu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_save:
                updateCurremtTaskFields();
                if(isNewItem){
                    saveTask_Server();
                }else {
                    updateTask_Server();
                }
                break;
            case R.id.menu_delete:
                deleteTask_Server();
                break;
            default:

                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //==============================================================================================
    private void saveTask_Server(){
        Log.e("SAVE ON SERVER","-");
        Utils utils = new Utils();
        String currDate = utils.dateToString(new Date(),DATE_FORMAT_STR);
        InputStream is_header = getResources().openRawResource(R.raw.post_query_task_header);
        InputStream is_footer = getResources().openRawResource(R.raw.post_query_task_footer);

        String ss_header = String.format(utils.getStringFromInputStream(is_header),currDate);
        String ss_footer = utils.getStringFromInputStream(is_footer);
        String ss_body = utils.format_task_body_to_save(mCurrentTask);
        PostDataToServer.PostDataToServer_Task pdt = new PostDataToServer.PostDataToServer_Task();
        pdt.execute("POST",ss_header+ss_body+ss_footer,CATALOG_TASKS,null);
        Log.e("saveTask_Server","SAVE");
        getActivity().finish();
    }

    private void updateTask_Server(){
        Log.e("UPDATE ON SERVER","-");
        Utils utils = new Utils();
        String ss2 = utils.format_task_patch_update(mCurrentTask);
        PostDataToServer.PostDataToServer_Task pdt = new PostDataToServer.PostDataToServer_Task();
        pdt.execute("PATCH",ss2,CATALOG_TASKS,mCurrentTask.getRef_Key());
        Log.e("updateTask_Server","UPDATE");
        getActivity().finish();
    }
    private void deleteTask_Server(){
        Log.e("DELETE ON SERVER","-");
        Utils utils = new Utils();
        String ss2 = utils.format_task_patch_delete(mCurrentTask);
        PostDataToServer.PostDataToServer_Task pdt = new PostDataToServer.PostDataToServer_Task();
        pdt.execute("PATCH",ss2,CATALOG_TASKS,mCurrentTask.getRef_Key());
        Log.e("deleteTask_Server","DELETE");
        getActivity().finish();

    }
    private void updateCurremtTaskFields(){
       //обновляет поля текущего элемента для апдейта или сейва на сервере
        /*
        Description
        Автор_Key
        Исполнитель_Key
        Содержание
        Примечание
        фЗавершена
        ДатаСоздания
        ДатаЗавершения
        База_Key
        фПринятаВРаботу
        ДатаПринятияВРаботу
        DeletionMark

        */
        mCurrentTask.setDescription(edDescription.getText().toString());
        mCurrentTask.setAuthor_Key("4b1b55a1-bc6d-11e6-80c2-f2bd425ab9dd");
        mCurrentTask.setContent(edContent.getText().toString());
        mCurrentTask.setProg_Key("4b1b55a1-bc6d-11e6-80c2-f2bd425ab9dd");
        //mCurrentTask.setNote();
        //mCurrentTask.setDateInWork();
        //mCurrentTask.setInWork();
        //mCurrentTask.setClosed();
        //mCurrentTask.setDateCreation();
        //mCurrentTask.setContent();
        //mCurrentTask.setDateClosed();

    }

    private void setOptionsMenuVisibility(){
        //Log.e("SET VISIBILITY","!");
        if(optionsMenu!=null){
            //Log.e("SET VISIBILITY"," NOT NULL");
            MenuItem item_save = optionsMenu.findItem(R.id.menu_save);
            item_save.setVisible(isDataChanged);
            MenuItem item_delete = optionsMenu.findItem(R.id.menu_delete);
            item_delete.setVisible(!isNewItem);
        }
    }
    //=========================== LISTENERS ========================================================
    private TextWatcher textChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            isDataChanged=true;
            setOptionsMenuVisibility();
        }
    };
    private CompoundButton.OnCheckedChangeListener switchChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            isDataChanged=true;
            setOptionsMenuVisibility();
        }
    };
}
