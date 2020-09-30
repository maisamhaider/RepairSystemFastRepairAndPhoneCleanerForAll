package com.cleaner.booster.phone.repairer.app.models;

import java.io.File;
import java.util.ArrayList;

public class CommonModel {

    String name;
    String path;
    long size;

    public CommonModel() {
    }

    public CommonModel(String name, String path, long size) {
        this.name = name;
        this.path = path;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
