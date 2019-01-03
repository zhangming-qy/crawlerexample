package com.crawler.example.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class MsgRequested implements Cloneable {
    private int id;
    private int com_info_id;
    private Timestamp created_time;
    private Timestamp modified_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCom_info_id() {
        return com_info_id;
    }

    public void setCom_info_id(int com_info_id) {
        this.com_info_id = com_info_id;
    }

    public Timestamp getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Timestamp created_time) {
        this.created_time = created_time;
    }

    public Timestamp getModified_time() {
        return modified_time;
    }

    public void setModified_time(Timestamp modified_time) {
        this.modified_time = modified_time;
    }

    @Override
    public String toString() {
        return "MsgRequested{" +
                "id=" + id +
                ", com_info_id='" + com_info_id +
                ", created_time='" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(created_time)  +
                ", modified_time='" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(modified_time) +
                "}'";
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
