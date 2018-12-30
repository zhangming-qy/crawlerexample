package com.crawler.example.web;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

@Component
public class Util {

    private final Logger log = LoggerFactory.getLogger(Util.class);

    @Value("${chrome.path}")
    private String chrome; // "C:\\Users\\Mark_Zhang\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe";

    public Document getContent(String url){
        try{
            //invalid url
            if(!checkUrl(url))
                return null;

            Document doc = Jsoup.connect(url).get();

            return doc;
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        catch (IllegalArgumentException ex){
            ex.printStackTrace();
        }
        catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }

        return null;
    }

    public Document getContentByChrome(String url){

        //invalid url
        if(!checkUrl(url))
            return null;

        ArrayList<String> command = new ArrayList<String>();
        //hiden chrome
        command.add("--headless");
        Launcher launcher = new Launcher();

        try{
            SessionFactory factory = launcher.launch(chrome,command);
            Session session = factory.create();
            session.navigate(url);
            session.waitDocumentReady();
            String content = (String)session.getContent();

            Document doc = Jsoup.parse(content,url);

            return doc;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }

    public static boolean checkUrl(String url){

        if (url==null) return false;

        return url.matches("^((https|http|ftp|rtsp|mms)?://)"
                + "+(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}"
                + "|"
                + "([0-9a-z_!~*'()-]+\\.)*"
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\."
                + "[a-z]{2,6})"
                + "(:[0-9]{1,4})?"
                + "((/?)|"
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$");
    }

    public static String getHost(String url){
        try{
            URL objUrl = new URL(url);
            return objUrl.getHost();
        }
        catch (MalformedURLException ex){
            ex.printStackTrace();
        }

        return null;
    }
}
