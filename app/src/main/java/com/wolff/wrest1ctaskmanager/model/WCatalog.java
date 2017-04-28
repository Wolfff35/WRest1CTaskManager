package com.wolff.wrest1ctaskmanager.model;

import java.io.Serializable;

/**
 * Created by wolff on 26.04.2017.
 */

public class WCatalog implements Serializable{
    private static final long serialVersionUID = 2163051469151804397L;
    private String mRef_Key;
    private boolean mDeletionMark;
    private String mCode;
    private String mDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WCatalog)) return false;

        WCatalog wCatalog = (WCatalog) o;

        return getRef_Key() != null ? getRef_Key().equals(wCatalog.getRef_Key()) : wCatalog.getRef_Key() == null;

    }

    @Override
    public int hashCode() {
        return getRef_Key() != null ? getRef_Key().hashCode() : 0;
    }

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
