package com.rrvow.rrchain.main.model;

import com.rrvow.rrchain.common.utils.StringUtil;

import java.io.Serializable;

/**
 * @author lpc
 */
public class CrystalBean implements Serializable {

    /**
     * name : 18801775936
     * id : 3
     * value : 1
     */

    private String name;
    private String id;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
