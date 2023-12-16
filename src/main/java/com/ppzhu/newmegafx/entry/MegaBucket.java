package com.ppzhu.newmegafx.entry;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

public class MegaBucket {
    private  SimpleStringProperty name;
    private SimpleObjectProperty<Date> creationTime;

    public MegaBucket(String name,Date creationTime) {
        this.name = new SimpleStringProperty(name);
        this.creationTime = new SimpleObjectProperty<>(creationTime);
    }

    public String getName() {
        return name.get();
    }

    @Override
    public String toString() {
        return "MegaBucket{" +
                "name=" + name +
                ", creationTime=" + creationTime +
                '}';
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }


    public Date getCreationTime() {
        return creationTime.get();
    }

    public SimpleObjectProperty<Date> creationTimeProperty() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime.set(creationTime);
    }
}
