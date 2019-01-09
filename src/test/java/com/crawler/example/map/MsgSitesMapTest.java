package com.crawler.example.map;

import com.crawler.example.entity.MsgSites;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MsgSitesMapTest {

    @Autowired
    MsgSitesMap msgSitesMap;

    @Test
    public void insert() {
        MsgSites msgSites = new MsgSites();
        msgSites.setDomain_name("http");
        Assert.assertNotNull(msgSitesMap.insert(msgSites));
    }

    @Test
    public void selectByDomain() {
        MsgSites msgSites = new MsgSites();
        msgSites.setDomain_name("http:domain1");
        Assert.assertNotNull(msgSitesMap.insert(msgSites));

        MsgSites msgSites2 = new MsgSites();
        msgSites2.setDomain_name("http:domain2");
        Assert.assertNotNull(msgSitesMap.insert(msgSites2));

        MsgSites msgSitesByDomain = msgSitesMap.selectByDomain("http:domain2");
        Assert.assertNotNull(msgSitesByDomain);
        Assert.assertEquals("DOMAIN NAME","http:domain2", msgSitesByDomain.getDomain_name());
    }
}