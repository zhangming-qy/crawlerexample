package com.crawler.example.web;

import com.crawler.example.app.AppTaskMan;
import com.crawler.example.app.AppTaskStatus;
import com.crawler.example.app.ITaskRunner;
import com.crawler.example.app.TaskRunner;
import com.crawler.example.entity.AppTask;
import com.crawler.example.entity.ComInfo;
import com.crawler.example.entity.MsgRequested;
import com.crawler.example.entity.MsgSites;
import com.crawler.example.map.ComInfoMap;
import com.crawler.example.map.MsgRequestedMap;
import com.crawler.example.map.MsgSitesMap;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
public class MsgPageLookup implements ITaskRunner {

    private final Logger log = LoggerFactory.getLogger(MsgPageLookup.class);

    private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(TaskRunner.CORE_POOL_SIZE,
            TaskRunner.CORE_POOL_SIZE*2, 10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(500));

    @Autowired
    private Util util;

    @Autowired
    private MsgSitesMap msgSitesMap;

    @Autowired
    private ComInfoMap comInfoMap;

    @Autowired
    private MsgRequestedMap msgRequestedMap;

    @Autowired
    private AppTaskMan appTaskMan;

    public AppTask getAppTask() {
        return appTaskMan.getAppTask();
    }

    @Override
    public void setTask(AppTask appTask) {
        this.appTaskMan.setTask(appTask);
    }

    @Override
    public boolean isSupportConcurrent() {
        return true;
    }

    @Override
    public AppTask call(){

        AppTask appTask = getAppTask();
        List<ComInfo> comInfoList = comInfoMap.getMsgUnRequestedListByCategory(appTask.getGroup_name());

        //Status PENDING to RUNNING
        if(appTask.getStatus().equals(AppTaskStatus.PENDING.name()))
            appTaskMan.updateAppTasksStatus(AppTaskStatus.RUNNING);
        else if(comInfoList.size() == 0 && appTask.getStatus().equals(AppTaskStatus.RUNNING.name())){
            appTaskMan.updateAppTasksStatus(AppTaskStatus.WAITING);
        }

        for(ComInfo comInfo : comInfoList){
            MsgSites msgSites = getMsgSites(comInfo.getWeb_url(),true);
            if(msgSites != null){
                msgSites.setSite_name(comInfo.getName());
                MsgSites msgSitesCheck = msgSitesMap.selectByDomain(msgSites.getDomain_name());
                if(msgSitesCheck == null){
                    msgSitesMap.insert(msgSites);
                }
                else {
                    log.info("Duplicated website" + msgSites.getSite_name() + " " + msgSites.getReg_url());
                }
            }

            //save requested url com info if
            MsgRequested msgRequested = new MsgRequested();
            msgRequested.setCom_info_id(comInfo.getId());
            msgRequestedMap.insert(msgRequested);
        }

        return appTask;
    }

    public MsgSites getMsgSites(String url, boolean isLoop){

        MsgSites msgSites = new MsgSites();
        Document doc = util.getContent(url);

        if(doc ==null) {
            log.warn("Can't get content from " + url);
            return null;
        }

        List<String> cssMsgs = CssQuery.getMsgElems(); /*new String[]{"input[type=button]:contains(验证码)",
                "input[type=button]input[value*=验证码]",
                "a:contains(验证码)",
                "button:contains(验证码)"
        };*/

        List<String> cssRegs = CssQuery.getRegElems(); //new String[] {"a[href]:contains(注册)"};

        String host = Util.getHost(url);
        msgSites.setDomain_name(host);

        for (String cssQuery : cssMsgs){
            Element elem = doc.selectFirst(cssQuery);
            //this is a send message page
            if(elem != null){
                msgSites.setReg_url(url);
                return msgSites;
            }
        }


        for(String cssQuery : cssRegs){
            //check register link
            Element elem = doc.selectFirst(cssQuery);

            //contains register link
            if(elem != null && isLoop){
                String link =  elem.absUrl("href");
                //System.out.println("Reg Page:" + link);
                return getMsgSites(link,false);
            }
        }

        return null;
    }
}
