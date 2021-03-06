package com.crawler.example.web;

import com.crawler.example.entity.MsgSites;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LvseSitesTest {

    @Autowired
    private LvseSites lvseSites;

    @Test
    public void getMsgSites() {
        String url = "https://passport.111.com.cn/sso/register.action";
        MsgSites msgSites = lvseSites.getMsgSites(url, true);
        Assert.assertEquals("https://passport.111.com.cn/sso/login.action", msgSites.getReg_url());
    }
}