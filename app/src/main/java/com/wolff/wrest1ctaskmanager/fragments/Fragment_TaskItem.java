package com.wolff.wrest1ctaskmanager.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.wolff.wrest1ctaskmanager.R;
import com.wolff.wrest1ctaskmanager.model.Const;
import com.wolff.wrest1ctaskmanager.model.WBase;
import com.wolff.wrest1ctaskmanager.model.WTask;
import com.wolff.wrest1ctaskmanager.model.WUser;
import com.wolff.wrest1ctaskmanager.tasks.PostDataToServer;
import com.wolff.wrest1ctaskmanager.utils.Utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import static com.wolff.wrest1ctaskmanager.model.Const.CATALOG_TASKS;
import static com.wolff.wrest1ctaskmanager.model.Const.DATE_FORMAT_STR;


public class Fragment_TaskItem extends Fragment {
    private static final String ARG_WTASK = "WTask";
    private static final String ARG_CURRENT_USER_GUID = "CurrentUserGuid";
    private static final String ARG_USER_LIST = "WUserList";
    private static final String ARG_BASES_LIST = "WBasesList";

    private WTask mCurrentTask;
    private String mCurrentUserGuid;
    private ArrayList<WUser> mUserList;
    private ArrayList<WBase> mBasesList;
    private boolean isNewItem;
    private boolean isDataChanged;

    private TextView tvCode;
    private EditText edDescription;
    private EditText edContent;
    private Switch switchIsInWork;
    private Switch switchIsClosed;
    private TextView tvDateCreation;
    private TextView tvDateClosed;
    private TextView tvDateInWork;
    private EditText edNote;
    private Spinner spBase;
    private Spinner spProgrammer;
    private TextView tvAuthor;

    private Menu optionsMenu;

    public static Fragment_TaskItem newInstance(Context context, WTask item, String currentUserGuid, ArrayList<WUser> userList,ArrayList<WBase>basesList) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_WTASK,item);
        args.putString(ARG_CURRENT_USER_GUID,currentUserGuid);
        args.putSerializable(ARG_USER_LIST,userList);
        args.putSerializable(ARG_BASES_LIST,basesList);

        Fragment_TaskItem fragment = new Fragment_TaskItem();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mCurrentTask = (WTask)getArguments().getSerializable(ARG_WTASK);
        mCurrentUserGuid = getArguments().getString(ARG_CURRENT_USER_GUID);
        mUserList = (ArrayList<WUser>)getArguments().getSerializable(ARG_USER_LIST);
        mBasesList = (ArrayList<WBase>)getArguments().getSerializable(ARG_BASES_LIST);
        if(mCurrentTask==null){
            isNewItem=true;
            isDataChanged=true;
            mCurrentTask = new WTask();
            mCurrentTask.setAuthor_Key(mCurrentUserGuid);
            mCurrentTask.setDateCreation(new Date());
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
        tvDateCreation = (TextView) v.findViewById(R.id.tvDateCreation);
        tvDateClosed = (TextView) v.findViewById(R.id.tvDateClosed);
        tvDateInWork = (TextView) v.findViewById(R.id.tvDateInWork);
        edNote = (EditText) v.findViewById(R.id.edNote);
        spBase = (Spinner) v.findViewById(R.id.spBase);
        spProgrammer = (Spinner) v.findViewById(R.id.spProgrammer);
        tvAuthor = (TextView) v.findViewById(R.id.tvAuthor);

        tvCode.setText(mCurrentTask.getCode());
        edDescription.setText(mCurrentTask.getDescription());
        edContent.setText(mCurrentTask.getContent());
        switchIsInWork.setChecked(mCurrentTask.isInWork());
        switchIsClosed.setChecked(mCurrentTask.isClosed());
        Utils utils = new Utils();
        tvDateCreation.setText(utils.dateToString(mCurrentTask.getDateCreation(), Const.DATE_FORMAT_VID));
        tvDateClosed.setText(utils.dateToString(mCurrentTask.getDateClosed(), Const.DATE_FORMAT_VID));
        tvDateInWork.setText(utils.dateToString(mCurrentTask.getDateInWork(), Const.DATE_FORMAT_VID));
        edNote.setText(mCurrentTask.getNote());

        ArrayAdapter<WUser> adapterUser = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,mUserList);
        spProgrammer.setAdapter(adapterUser);
        spProgrammer.setSelection(adapterUser.getPosition(utils.getUserByGuid(mUserList,mCurrentTask.getProg_Key())));

        ArrayAdapter<WBase> adapterBase = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,mBasesList);
        spBase.setAdapter(adapterBase);
        spBase.setSelection(adapterBase.getPosition(utils.getBaseByGuid(mBasesList,mCurrentTask.getBase_Key())));
        tvAuthor.setText(utils.getUserByGuid(mUserList,mCurrentTask.getAuthor_Key()).getDescription());

        edDescription.addTextChangedListener(textChangedListener);
        edContent.addTextChangedListener(textChangedListener);
        switchIsClosed.setOnCheckedChangeListener(switchChangedListener);
        switchIsInWork.setOnCheckedChangeListener(switchChangedListener);
        edNote.addTextChangedListener(textChangedListener);
        spProgrammer.setOnItemSelectedListener(itemSelectedListener);
        spBase.setOnItemSelectedListener(itemSelectedListener);
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
        Utils utils = new Utils();
        String currDate = utils.dateToString(new Date(),DATE_FORMAT_STR);
        InputStream is_header = getResources().openRawResource(R.raw.post_query_task_header);
        InputStream is_footer = getResources().openRawResource(R.raw.post_query_task_footer);

        String ss_header = String.format(utils.getStringFromInputStream(is_header),currDate);
        String ss_footer = utils.getStringFromInputStream(is_footer);
        String ss_body = utils.format_task_body_to_save(mCurrentTask);
        PostDataToServer.PostDataToServer_Task pdt = new PostDataToServer.PostDataToServer_Task(getContext());
        pdt.execute("POST",ss_header+ss_body+ss_footer,CATALOG_TASKS,null);
        getActivity().finish();
    }

    private void updateTask_Server(){
        Utils utils = new Utils();
        String ss2 = utils.format_task_patch_update(getContext(),mCurrentTask);
        PostDataToServer.PostDataToServer_Task pdt = new PostDataToServer.PostDataToServer_Task(getContext());
        pdt.execute("PATCH",ss2,CATALOG_TASKS,mCurrentTask.getRef_Key());
        getActivity().finish();
    }
    private void deleteTask_Server(){
        Utils utils = new Utils();
        String ss2 = utils.format_task_patch_delete(getContext(),mCurrentTask);
        PostDataToServer.PostDataToServer_Task pdt = new PostDataToServer.PostDataToServer_Task(getContext());
        pdt.execute("PATCH",ss2,CATALOG_TASKS,mCurrentTask.getRef_Key());
        getActivity().finish();

    }
    private void updateCurremtTaskFields(){
       //обновляет поля текущего элемента для апдейта или сейва на сервере
        Utils utils = new Utils();
        mCurrentTask.setDescription(edDescription.getText().toString());
        mCurrentTask.setContent(edContent.getText().toString());
        WUser prog = (WUser)spProgrammer.getSelectedItem();
        mCurrentTask.setProg_Key(prog.getRef_Key());
        WBase bas = (WBase)spBase.getSelectedItem();
        mCurrentTask.setBase_Key(bas.getRef_Key());
        mCurrentTask.setNote(edNote.getText().toString());
        mCurrentTask.setDateInWork(utils.dateFromString(tvDateInWork.getText().toString(),Const.DATE_FORMAT_VID));
        mCurrentTask.setInWork(switchIsInWork.isChecked());
        mCurrentTask.setClosed(switchIsClosed.isChecked());
        mCurrentTask.setDateClosed(utils.dateFromString(tvDateClosed.getText().toString(),Const.DATE_FORMAT_VID));

    }

    private void setOptionsMenuVisibility(){
        if(optionsMenu!=null){
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
            Utils utils = new Utils();
            switch (buttonView.getId()){
                case R.id.switchIsClosed:
                    if(switchIsClosed.isChecked()){
                        tvDateClosed.setText(utils.dateToString(new Date(),Const.DATE_FORMAT_VID));
                    }else {
                        tvDateClosed.setText("");
                    }
                    break;
                case R.id.switchIsInWork:
                    if(switchIsInWork.isChecked()){
                        tvDateInWork.setText(utils.dateToString(new Date(),Const.DATE_FORMAT_VID));
                    }else {
                        tvDateInWork.setText("");
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            isDataChanged=true;
            setOptionsMenuVisibility();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
