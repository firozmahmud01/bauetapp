package com.firoz.mahmud.bauet;

import java.util.ArrayList;

public class StudentItem {
    public StudentItem(){

    }
    private String id,name,batch,department;
    private String face;

    public StudentItem(String id, String name, String batch, String department, String face) {
        this.id = id;
        this.name = name;
        this.batch = batch;
        this.department = department;
        this.face = face;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }
}
