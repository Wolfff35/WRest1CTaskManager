package com.wolff.wrest1ctaskmanager.model;

import java.io.Serializable;

/**
 * Created by wolff on 26.04.2017.
 */

public class WContragent extends WCatalog implements Serializable {
    private static final long serialVersionUID = 2163051468057804396L;

    @Override
    public String toString() {
        return getDescription();
    }

}
