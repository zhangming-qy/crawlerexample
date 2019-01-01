package com.crawler.example.init;


import com.crawler.example.app.AppTaskStatus;
import com.crawler.example.entity.AppTask;
import com.crawler.example.map.AppTaskMap;
import com.crawler.example.web.LvseSites;
import com.crawler.example.web.Util;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
public class Lvse {

    private final Logger log = LoggerFactory.getLogger(Lvse.class);

    @Autowired
    private AppTaskMap appTaskMap;

    @Autowired
    private Util util;

    @Value("${web.lvse.url}")
    private String web_url;

    @Value("${web.lvse.category}")
    private String web_catetory;

    @Value("${web.lvse.next}")
    private String web_next;

    @Value("${web.lvse.last}")
    private String web_last;

    @RequestMapping("/init/lvse")
    public String initTask(){
        StringBuilder stringBuilder = new StringBuilder();

        log.info("Requesting URL: " + web_url);
        Document document = util.getContent(web_url);

        if(document == null){
            log.warn("Can't get content from " + web_url);
            return "";
        }

        Elements elems = document.select(web_catetory);

        for(Element elem : elems){
            String link = elem.absUrl("href");

            List<AppTask> appTasks = appTaskMap.getAppTasksByRootUrl(link);

            stringBuilder.append(link);
            stringBuilder.append("<br>");

            if(appTasks.size() > 0) continue;

            AppTask appTask = new AppTask();
            appTask.setRoot_url(link);
            appTask.setGroup_name("lvse");
            appTask.setOrder_num(elems.indexOf(elem));
            appTask.setJclass(LvseSites.class.getName());
            appTask.setStatus(AppTaskStatus.PENDING.name());
            appTask.setCreated_time(new Timestamp(System.currentTimeMillis()));
            appTask.setModified_time(new Timestamp(System.currentTimeMillis()));
            appTaskMap.insert(appTask);
        }

        return stringBuilder.toString();
    }

    @RequestMapping("/init/lvse/update")
    public String updateTask(){
        List<AppTask> appTasks = appTaskMap.getAll();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<table border='1px' cellspacing='0' cellpadding='0' cellspacing='4'  border-collapse='collapse'>");
        stringBuilder.append("<th>first page</th><th>last page</th>");
        for(AppTask appTask : appTasks){
            String root_url = appTask.getRoot_url();
            stringBuilder.append("<tr><td>");
            stringBuilder.append(root_url);
            stringBuilder.append("</td><td>");

            if(appTask.getLast_url() == null || appTask.getLast_url().isEmpty()) {
                Document docLink = util.getContent(root_url);
                Element elemLast = docLink.selectFirst(web_last);
                appTask.setLast_url(elemLast.absUrl("href"));
                appTaskMap.update(appTask);
            }

            stringBuilder.append(appTask.getLast_url());
            stringBuilder.append("</td></tr>");
        }
        stringBuilder.append("</table>");
        return stringBuilder.toString();
    }
}
