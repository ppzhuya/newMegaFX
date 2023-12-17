package com.ppzhu.newmegafx.entry;/*
 * @Author ppzhu
 * @Date 2023/12/17 23:02
 * @Discription
 */

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

public class ObjectList {
    private SimpleStringProperty key;
    private SimpleObjectProperty<Long> size;
    private SimpleObjectProperty<Date> lastModified;

    public ObjectList(String key, long size,Date lastModified) {
        this.key = new  SimpleStringProperty(key);
        this.size = new SimpleObjectProperty<>(size);
        this.lastModified = new SimpleObjectProperty<>(lastModified);
    }

    public String getKey() {
        return key.get();
    }

    public SimpleStringProperty keyProperty() {
        return key;
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public Long getSize() {
        return size.get();
    }

    public SimpleObjectProperty<Long> sizeProperty() {
        return size;
    }

    public void setSize(Long size) {
        this.size.set(size);
    }

    public Date getLastModified() {
        return lastModified.get();
    }

    public SimpleObjectProperty<Date> lastModifiedProperty() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified.set(lastModified);
    }
}
