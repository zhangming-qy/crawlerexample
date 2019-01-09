package com.crawler.example.map;

import com.crawler.example.entity.ComInfo;
import com.crawler.example.entity.MsgRequested;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ComInfoMapTest {

    @Autowired
    ComInfoMap comInfoMap;

    @Autowired
    MsgRequestedMap msgRequestedMap;

    @Autowired
    DbMap dbMap;

    @After
    public void tearDown() throws Exception {
        dbMap.deleteComInfo();
    }

    @Test
    public void insert() {
        ComInfo comInfo = new ComInfo();
        comInfo.setRegion("CHINA");
        comInfo.setCategory("TEST");
        comInfo.setName("TESTER");
        comInfo.setWeb_url("http:");
        int i = comInfoMap.insert(comInfo);
        Assert.assertEquals(1,i);

        ComInfo comInfo2 = dbMap.getAllComInfo().get(0);
        Assert.assertNotNull(comInfo2);
        //Assert.assertEquals("ID", 1, comInfo2.getId());
        Assert.assertEquals("REGION", "CHINA", comInfo2.getRegion());
        Assert.assertEquals("CATEGORY", "TEST", comInfo2.getCategory());
        Assert.assertEquals("NAME", "TESTER", comInfo2.getName());
        Assert.assertEquals("WEB URL", "http:", comInfo2.getWeb_url());
    }

    @Test
    public void getByUrl() {
        ComInfo comInfo = new ComInfo();
        comInfo.setRegion("CHINA");
        comInfo.setCategory("TEST");
        comInfo.setName("TESTER1");
        comInfo.setWeb_url("http:1");
        int i = comInfoMap.insert(comInfo);
        Assert.assertEquals(1,i);

        ComInfo comInfo2 = new ComInfo();
        comInfo2.setRegion("CHINA");
        comInfo2.setCategory("TEST");
        comInfo2.setName("TESTER2");
        comInfo2.setWeb_url("http:2");
        i = comInfoMap.insert(comInfo2);
        Assert.assertEquals(1,i);

        ComInfo comInfo3 = new ComInfo();
        comInfo3.setRegion("HK");
        comInfo3.setCategory("TEST2");
        comInfo3.setName("TESTER3");
        comInfo3.setWeb_url("http:1");
        i = comInfoMap.insert(comInfo3);
        Assert.assertEquals(1,i);

        List<ComInfo> comInfoList = comInfoMap.getByUrl("http:1");
        Assert.assertNotNull(comInfoList);
        Assert.assertEquals("SIZE", 2, comInfoList.size());
        Assert.assertEquals("REGION", "CHINA", comInfoList.get(0).getRegion());
        Assert.assertEquals("CATEGORY", "TEST", comInfoList.get(0).getCategory());
        Assert.assertEquals("NAME", "TESTER1", comInfoList.get(0).getName());
        Assert.assertEquals("WEB URL", "http:1", comInfoList.get(0).getWeb_url());

        Assert.assertEquals("REGION", "HK", comInfoList.get(1).getRegion());
        Assert.assertEquals("CATEGORY", "TEST2", comInfoList.get(1).getCategory());
        Assert.assertEquals("NAME", "TESTER3", comInfoList.get(1).getName());
        Assert.assertEquals("WEB URL", "http:1", comInfoList.get(1).getWeb_url());
    }

    @Test
    public void getByCategoryAndUrl() {
        ComInfo comInfo = new ComInfo();
        comInfo.setRegion("CHINA");
        comInfo.setCategory("TEST");
        comInfo.setName("TESTER1");
        comInfo.setWeb_url("http:categoryAndUrl");
        int i = comInfoMap.insert(comInfo);
        Assert.assertEquals(1,i);

        ComInfo comInfo2 = new ComInfo();
        comInfo2.setRegion("CHINA");
        comInfo2.setCategory("TEST");
        comInfo2.setName("TESTER2");
        comInfo2.setWeb_url("http:categoryAndUrl2");
        i = comInfoMap.insert(comInfo2);
        Assert.assertEquals(1,i);

        ComInfo comInfo3 = new ComInfo();
        comInfo3.setRegion("HK");
        comInfo3.setCategory("TEST2");
        comInfo3.setName("TESTER3");
        comInfo3.setWeb_url("http:categoryAndUrl");
        i = comInfoMap.insert(comInfo3);
        Assert.assertEquals(1,i);

        ComInfo comInfoByCategoryAndUrl = comInfoMap.getByCategoryAndUrl("TEST","http:categoryAndUrl");
        Assert.assertNotNull(comInfoByCategoryAndUrl);
        Assert.assertEquals("REGION", "CHINA", comInfoByCategoryAndUrl.getRegion());
        Assert.assertEquals("CATEGORY", "TEST", comInfoByCategoryAndUrl.getCategory());
        Assert.assertEquals("NAME", "TESTER1", comInfoByCategoryAndUrl.getName());
        Assert.assertEquals("WEB URL", "http:categoryAndUrl", comInfoByCategoryAndUrl.getWeb_url());
    }

    @Test
    public void getMsgUnRequestedListByCategory() {
        ComInfo comInfo = new ComInfo();
        comInfo.setRegion("CHINA");
        comInfo.setCategory("TEST");
        comInfo.setName("TESTER1");
        comInfo.setWeb_url("http:categoryAndUrl");
        Assert.assertNotNull(comInfoMap.insert(comInfo));

        ComInfo comInfo2 = new ComInfo();
        comInfo2.setRegion("CHINA");
        comInfo2.setCategory("TEST");
        comInfo2.setName("TESTER2");
        comInfo2.setWeb_url("http:categoryAndUrl2");
        Assert.assertNotNull(comInfoMap.insert(comInfo2));

        List<ComInfo> comInfoAll = dbMap.getAllComInfo();
        MsgRequested msgRequested = new MsgRequested();
        msgRequested.setCom_info_id(comInfoAll.get(0).getId());
        Assert.assertNotNull(msgRequestedMap.insert(msgRequested));

        List<ComInfo> comInfoList = comInfoMap.getMsgUnRequestedListByCategory("TEST");
        Assert.assertNotNull(comInfoList);
        Assert.assertEquals("SIZE", 1, comInfoList.size());
        Assert.assertEquals("REGION", "CHINA", comInfoList.get(0).getRegion());
        Assert.assertEquals("CATEGORY", "TEST", comInfoList.get(0).getCategory());
        Assert.assertEquals("NAME", "TESTER2", comInfoList.get(0).getName());
        Assert.assertEquals("WEB URL", "http:categoryAndUrl2", comInfoList.get(0).getWeb_url());
    }

    @Test
    public void update() {
        ComInfo comInfo = new ComInfo();
        comInfo.setRegion("CHINA");
        comInfo.setCategory("UPDATE");
        comInfo.setName("TESTER1");
        comInfo.setWeb_url("http:update");
        Assert.assertNotNull(comInfoMap.insert(comInfo));

        comInfo.setRegion("CHINA2");
        comInfo.setCategory("UPDATE2");
        comInfo.setName("TESTER2");
        comInfo.setDescription("description");
        comInfo.setWeb_url("http:update2");
        comInfo.setVisit_cnt(1);
        comInfoMap.update(comInfo);

        List<ComInfo> comInfoList = dbMap.getAllComInfo();
        Assert.assertNotNull(comInfoList);
        Assert.assertEquals("SIZE", 1, comInfoList.size());
        Assert.assertEquals("REGION", "CHINA2", comInfoList.get(0).getRegion());
        Assert.assertEquals("CATEGORY", "UPDATE2", comInfoList.get(0).getCategory());
        Assert.assertEquals("NAME", "TESTER2", comInfoList.get(0).getName());
        Assert.assertEquals("DESCRIPTION", "description",comInfoList.get(0).getDescription());
        Assert.assertEquals("WEB URL", "http:update2", comInfoList.get(0).getWeb_url());
        Assert.assertEquals("VISIT CNT", 1, comInfoList.get(0).getVisit_cnt());
    }

    @Test
    public void updateVisitCountById() {
        ComInfo comInfo = new ComInfo();
        comInfo.setRegion("CHINA");
        comInfo.setCategory("TEST");
        comInfo.setName("TESTER1");
        comInfo.setWeb_url("http:updaetVisitCountById");
        comInfo.setVisit_cnt(1);
        Assert.assertNotNull(comInfoMap.insert(comInfo));


        List<ComInfo> comInfoAll = dbMap.getAllComInfo();
        int id = comInfoAll.get(0).getId();
        comInfoMap.updateVisitCountById(id);

        comInfo = comInfoMap.getById(id);
        Assert.assertEquals("VISIT CNT", 2, comInfo.getVisit_cnt());

    }

    @Test
    public void updateVisitCountByCategoryAndUrl() {
        ComInfo comInfo = new ComInfo();
        comInfo.setRegion("CHINA");
        comInfo.setCategory("TEST");
        comInfo.setName("TESTER1");
        comInfo.setWeb_url("http:updateVisitCountByCategoryAndUrl");
        comInfo.setVisit_cnt(1);
        Assert.assertNotNull(comInfoMap.insert(comInfo));

        comInfoMap.updateVisitCountByCategoryAndUrl("TEST", "http:updateVisitCountByCategoryAndUrl");

        comInfo = comInfoMap.getByCategoryAndUrl("TEST", "http:updateVisitCountByCategoryAndUrl");
        Assert.assertEquals("VISIT CNT", 2, comInfo.getVisit_cnt());

    }
}