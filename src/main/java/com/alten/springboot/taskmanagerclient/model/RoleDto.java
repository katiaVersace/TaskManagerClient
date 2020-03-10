package com.alten.springboot.taskmanagerclient.model;

import java.io.Serializable;

public class RoleDto implements Serializable {

    private int id;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
     * @Override public String toString() { return "RoleDto [id=" + id + ", name=" +
     * name + "]"; }
     */
}
