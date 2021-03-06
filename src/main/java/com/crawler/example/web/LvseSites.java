package com.crawler.example.web;

import com.crawler.example.app.AppTasksStatus;
import com.crawler.example.app.AppTaskMan;
import com.crawler.example.app.ITaskRunner;
import com.crawler.example.entity.AppTask;
import com.crawler.example.entity.MsgSites;
import com.crawler.example.map.MsgSitesMap;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LvseSites implements ITaskRunner {

    private final Logger log = LoggerFactory.getLogger(LvseSites.class);

    @Autowired
    private Util util;

    @Autowired
    private MsgSitesMap msgSitesMap;

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
    public void run() {
        AppTask appTask = getAppTask();
        String url = appTask.getCurr_url() == null ? appTask.getRoot_url() : appTask.getCurr_url();
        appTaskMan.updateAppTasksStatus(AppTasksStatus.RUNNING);
        crawlLvseSites(url);
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

        Elements h2List = document.select("div.info h2:has(a)");

        for (Element h2: h2List) {
            Element brand = h2.selectFirst("a:not(.visit)");
            Element link = h2.selectFirst("a.visit[href]");
            String aHref = link.absUrl("href");

            log.info("Brand: " + brand.text());
            log.info("Brand Link: " + aHref);

            MsgSites msgSites = getMsgSites(aHref,true);

            if(msgSites != null){
                msgSites.setSite_name(brand.text());
                log.info("Get website " + msgSites.getSite_name() + " " + msgSites.getReg_url());
                MsgSites msgSitesCheck = msgSitesMap.selectByDomain(msgSites.getDomain_name());
                if(msgSitesCheck == null){
                    msgSitesMap.insert(msgSites);
                }
                else {
                    log.info("Duplicated website" + msgSites.getSite_name() + " " + msgSites.getReg_url());
                }
            }

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
