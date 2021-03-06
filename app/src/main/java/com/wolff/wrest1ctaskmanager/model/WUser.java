package com.wolff.wrest1ctaskmanager.model;

import java.io.Serializable;

/**
 * Created by wolff on 26.04.2017.
 */

public class WUser extends WCatalog implements Serializable {
    private static final long serialVersionUID = 2167771469151804396L;
    private boolean mIsUser;
    private boolean mIsProg;
    private boolean mIsGod;
    private String mUniqId;

    public boolean isUser() {
        return mIsUser;
    }

    public void setUser(boolean user) {
        mIsUser = user;
    }

    public boolean isProg() {
        return mIsProg;
    }

    public void setProg(boolean prog) {
        mIsProg = prog;
    }

    public boolean isGod() {
        return mIsGod;
    }

    public void setGod(boolean god) {
        mIsGod = god;
    }

    public String getUniqId() {
        return mUniqId;
    }

    public void setUniqId(String uniqId) {
        mUniqId = uniqId;
    }

    @Override
    public String toString() {
        return getDescription();
    }

}
