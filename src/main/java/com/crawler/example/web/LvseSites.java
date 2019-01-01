package com.crawler.example.web;

import com.crawler.example.app.AppTaskStatus;
import com.crawler.example.app.AppTaskMan;
import com.crawler.example.app.ITaskRunner;
import com.crawler.example.entity.AppTask;
import com.crawler.example.entity.ComInfo;
import com.crawler.example.entity.MsgSites;
import com.crawler.example.map.ComInfoMap;
import com.crawler.example.map.MsgSitesMap;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class LvseSites implements ITaskRunner {

    private final Logger log = LoggerFactory.getLogger(LvseSites.class);

    @Autowired
    private Util util;

    @Autowired
    private ComInfoMap comInfoMap;

   /*
    @Value("${web.url}")
    private String root_url;
    */

   @Autowired
    private AppTaskMan appTaskMan;

    public LvseSites(){}

    public AppTask getAppTask() {
        return appTaskMan.getAppTask();
    }

    public AppTaskMan getAppTaskMan() {
        return appTaskMan;
    }

    public void setAppTaskMan(AppTaskMan appTaskMan) {
        this.appTaskMan = appTaskMan;
    }

    @Override
    //@Scheduled(fixedDelay=86400000)
    public String call() {
        AppTask appTask = getAppTask();
        String url = appTask.getCurr_url() == null ? appTask.getRoot_url() : appTask.getCurr_url();
        appTaskMan.updateAppTasksStatus(AppTaskStatus.RUNNING);
        crawlLvseSites(url);

        if(appTask.getCurr_url().equals(appTask.getLast_url())){
            appTaskMan.updateAppTasksStatus(AppTaskStatus.DONE);
        }

        return appTaskMan.getAppStatusStr();
    }

    @Override
    public void setTask(AppTask appTask) {
        this.appTaskMan.setTask(appTask);
    }

    public void crawlLvseSites(String url){
        log.info("Requesting URL: " + url);
        Document document = util.getContent(url);

        if(document == null){
            log.warn("Can't get content from " + url);
            return;
        }

        log.info(url);
        System.out.println("Host:" + Util.getHost(url));
        System.out.println("Location:" + url);

        ComInfo comInfo = new ComInfo();
        comInfo.setFrom_url(url);
        Element elemRegion = document.selectFirst("div.area#area1 a.on");
        if(elemRegion != null)
            comInfo.setRegion(elemRegion.text());

        Element elemCategory = document.selectFirst("div.area#area2 a.on");
        if(elemCategory != null)
            comInfo.setCategory(elemCategory.text());

        Elements slistingList = document.select("div#slisting"); //div.info h2:has(a)

        for (Element slisting: slistingList) {
            Element brand = slisting.selectFirst("div.info a:not(.visit)");
            Element link = slisting.selectFirst("div.info a.visit[href]");
            Element desc = slisting.selectFirst("div.info p:not(.l)");
            String aHref = link.absUrl("href");

            comInfo.setName(brand.text());
            comInfo.setDescription(desc.ownText());
            comInfo.setWeb_url(aHref);
            comInfo.setVisit_cnt(0);

            log.info("Brand: " + brand.text());
            log.info("Brand Link: " + aHref);

            ComInfo comInfoCheck = comInfoMap.selectByUrl(aHref);
            if(comInfoCheck == null)
                comInfoMap.insert(comInfo);

        }

        //next page
        Element nextLink = document.selectFirst("a[href]:contains(下一页)");
        if(nextLink!=null){
            String nextPage = nextLink.absUrl("href");
            appTaskMan.updateAppTasksCurrUrl(nextPage);
            crawlLvseSites(nextPage);
        }
        else{
            log.info("This is the last page: " + url);
        }
    }
}
