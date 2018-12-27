package com.crawler.example.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class AppTasks implements Cloneable {

    private int id;
    private String url;
    private String group_name;
    private int order_num;
    private String jClass;
    private String status;
    private Timestamp created_time;
    private Timestamp modified_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public String getJClass() {
        return jClass;
    }

    public void setJClass(String jclass) {
        this.jClass = jclass;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        return "AppTasks{" +
                "id=" + id +
                ", url='" + url +
                ", group_name='" + group_name +
                ", order_num='" + order_num +
                ", status='" + status +
                ", created_time='" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(created_time)  +
                ", modified_time='" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(modified_time) +
                "}'";
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
