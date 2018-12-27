package com.crawler.example.entity;

public class MsgSites implements Cloneable {

    private int id;
    private String site_name;
    private String domain_name;
    private String reg_url;
    private String post_url;

    public int getId() {
        return id;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    public String getReg_url() {
        return reg_url;
    }

    public void setReg_url(String reg_url) {
        this.reg_url = reg_url;
    }

    public String getPost_url() {
        return post_url;
    }

    public void setPost_url(String post_url) {
        this.post_url = post_url;
    }

    @Override
    public String toString(){
        return "MsgSites{" +
                "id=" + id +
                ", domain_name='" + domain_name +
                ", reg_url='" + reg_url +
                ", post_url='" + post_url +
                "}'";
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
