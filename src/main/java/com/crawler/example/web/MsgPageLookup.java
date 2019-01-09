package com.crawler.example.web;

import com.crawler.example.entity.ComInfo;
import com.crawler.example.entity.MsgRequested;
import com.crawler.example.entity.MsgSites;
import com.crawler.example.map.MsgRequestedMap;
import com.crawler.example.map.MsgSitesMap;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
@Scope("prototype")
public class MsgPageLookup implements Runnable {

    private final Logger log = LoggerFactory.getLogger(MsgPageLookup.class);

    private final static ConcurrentLinkedDeque<ComInfo> comInfoDeque = new ConcurrentLinkedDeque<>();

    @Autowired
    private Util util;

    @Autowired
    private MsgSitesMap msgSitesMap;

    @Autowired
    private MsgRequestedMap msgRequestedMap;

    public static void setComInfoDeque(Collection<? extends ComInfo> c){
        comInfoDeque.addAll(c);
    }

    @Override
    public void run() {
        try {
            ComInfo comInfo = comInfoDeque.poll();
            if(comInfo ==null) return;

            log.info("Proceeding {}", comInfo.getWeb_url());
            MsgSites msgSites = getMsgSites(comInfo.getWeb_url(), true);
            if (msgSites != null) {
                msgSites.setSite_name(comInfo.getName());
                MsgSites msgSitesCheck = msgSitesMap.selectByDomain(msgSites.getDomain_name());
                if (msgSitesCheck == null) {
                    msgSitesMap.insert(msgSites);
                    log.info("Inserted message sites {} - {}", msgSites.getSite_name(), msgSites.getReg_url());
                } else {
                    log.info("Duplicated website" + msgSites.getSite_name() + " " + msgSites.getReg_url());
                }
            }

            //save requested url com info if
            MsgRequested msgRequested = new MsgRequested();
            msgRequested.setCom_info_id(comInfo.getId());
            msgRequestedMap.insert(msgRequested);
            log.info("Audit requested page {}", comInfo.getWeb_url());
        }
        catch (Exception e){
            log.error(e.getMessage());
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
