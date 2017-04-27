package com.wolff.wrest1ctaskmanager.model;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wolff on 26.04.2017.
 */

public class WTask extends WCatalog implements Serializable {
    private static final long serialVersionUID = 2163051469151804396L;

    private String mAuthor_Key; //Автор_Key
    private String mProg_Key;   //Исполнитель_Key
    private String mContent;    // Содержание
    private String mNote;       // Примечание
    private boolean mIsClosed;  // фЗавершена
    private Date mDateClosed;   // ДатаЗавершения
    private Date mDateCreation; // ДатаСоздания
    private String mBase_Key;   // База_Key
    private boolean mIsInWork;  // фПринятаВРаботу
    private Date mDateInWork;   // ДатаПринятияВРаботу

    public String getAuthor_Key() {
        return mAuthor_Key;
    }

    public void setAuthor_Key(String author_Key) {
        mAuthor_Key = author_Key;
    }

    public String getProg_Key() {
        return mProg_Key;
    }

    public void setProg_Key(String prog_Key) {
        mProg_Key = prog_Key;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }

    public boolean isClosed() {
        return mIsClosed;
    }

    public void setClosed(boolean closed) {
        mIsClosed = closed;
    }

    public Date getDateClosed() {
        return mDateClosed;
    }

    public void setDateClosed(Date dateClosed) {
        mDateClosed = dateClosed;
    }

    public Date getDateCreation() {
        return mDateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        mDateCreation = dateCreation;
    }

    public String getBase_Key() {
        return mBase_Key;
    }

    public void setBase_Key(String base_Key) {
        mBase_Key = base_Key;
    }

    public boolean isInWork() {
        return mIsInWork;
    }

    public void setInWork(boolean inWork) {
        mIsInWork = inWork;
    }

    public Date getDateInWork() {
        return mDateInWork;
    }

    public void setDateInWork(Date dateInWork) {
        mDateInWork = dateInWork;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
