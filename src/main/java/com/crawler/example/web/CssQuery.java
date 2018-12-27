package com.crawler.example.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CssQuery {

    private static List<String> msgElems;
    public static List<String> getMsgElems() {
        return msgElems;
    }

    @Value("#{'${web.cssquery.msgcode}'.split(',')}")
    public void setMsgElems(List<String> msgElems) {
        CssQuery.msgElems = msgElems;
    }

    private static List<String> regElems;

    public static List<String> getRegElems() {
        return regElems;
    }

    @Value("#{'${web.cssquery.reg}'.split(',')}")
    public void setRegElems(List<String> regElems) {
        CssQuery.regElems = regElems;
    }
}
