package com.crawler.example.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ComInfo implements Cloneable {

    private int id;
    private String region;
    private String category;
    private String name;
    private String description;
    private String web_url;
    private int visit_cnt;
    private String from_url;
    private Timestamp created_time;
    private Timestamp modified_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public int getVisit_cnt() {
        return visit_cnt;
    }

    public void setVisit_cnt(int visit_cnt) {
        this.visit_cnt = visit_cnt;
    }

    public String getFrom_url() {
        return from_url;
    }

    public void setFrom_url(String from_url) {
        this.from_url = from_url;
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
        return "ComInfo{" +
                "id=" + id +
                ", region='" + region +
                ", category='" + category +
                ", name='" + name +
                ", description='" + description +
                ", web_url='" + web_url +
                ", visit_cnt='" + visit_cnt +
                ", from_url='" + from_url +
                ", created_time='" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(created_time)  +
                ", modified_time='" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(modified_time) +
                "}'";
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
