package com.wolff.wrest1ctaskmanager.model;

/**
 * Created by wolff on 26.04.2017.
 */

public class WCatalog {
    private String mRef_Key;
    private boolean mDeletionMark;
    private String mCode;
    private String mDescription;

    public String getRef_Key() {
        return mRef_Key;
    }

    public void setRef_Key(String ref_Key) {
        mRef_Key = ref_Key;
    }

    public boolean isDeletionMark() {
        return mDeletionMark;
    }

    public void setDeletionMark(boolean deletionMark) {
        mDeletionMark = deletionMark;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
    @Override
    public String toString() {
        return getDescription();
    }

}
