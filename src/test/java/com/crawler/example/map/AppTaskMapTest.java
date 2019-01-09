package com.crawler.example.map;

import com.crawler.example.entity.AppTask;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTaskMapTest {

    @Autowired
    AppTaskMap appTaskMap;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAppTasksByStatus() {
        AppTask appTask = new AppTask();
        appTask.setRoot_url("http:bystatus");
        appTask.setStatus("TEST-STATUS");
        Assert.assertNotNull(appTaskMap.insert(appTask));

        List<AppTask> appTaskList = appTaskMap.getAppTasksByStatus("TEST-STATUS");
        Assert.assertNotNull(appTaskList);
        Assert.assertEquals("Size",1, appTaskList.size());
        Assert.assertEquals("Root_URL","http:bystatus", appTaskList.get(0).getRoot_url());
        Assert.assertEquals("STATUS","TEST-STATUS", appTaskList.get(0).getStatus());
    }

    @Test
    public void getAppTasksInAndOrderByStatus() {
        //TODO:H2 doesn't support Order by field function, this test case skip.
    }

    @Test
    public void getAppTasksByRootUrlAndJClass() {
        AppTask appTask = new AppTask();
        appTask.setRoot_url("http:getAppTasksByRootUrlAndJClass");
        appTask.setJclass("Jclass1");
        Assert.assertNotNull(appTaskMap.insert(appTask));

        AppTask appTask2 = new AppTask();
        appTask2.setRoot_url("http:getAppTasksByRootUrlAndJClass");
        appTask2.setJclass("Jclass2");
        Assert.assertNotNull(appTaskMap.insert(appTask2));

        AppTask appTask3 = new AppTask();
        appTask3.setRoot_url("http:xxxxx");
        appTask3.setJclass("Jclass1");
        Assert.assertNotNull(appTaskMap.insert(appTask3));

        List<AppTask> appTaskList = appTaskMap.getAppTasksByRootUrlAndJClass("http:getAppTasksByRootUrlAndJClass", "Jclass1");
        Assert.assertNotNull(appTaskList);
        Assert.assertEquals("Size",1, appTaskList.size());
        Assert.assertEquals("Root_URL","http:getAppTasksByRootUrlAndJClass", appTaskList.get(0).getRoot_url());
        Assert.assertEquals("JCLASS","Jclass1", appTaskList.get(0).getJclass());

    }

    @Test
    public void insert() {
        AppTask appTask = new AppTask();
        appTask.setRoot_url("http");
        Assert.assertNotNull(appTaskMap.insert(appTask));
    }

    @Test
    public void update() {
        AppTask appTask = new AppTask();
        appTask.setRoot_url("http:update");
        Assert.assertNotNull(appTaskMap.insert(appTask));

        appTask.setCurr_url("http:updateCurr");
        appTask.setLast_url("http:updateLast");
        appTask.setStatus("UPDATE");
        appTaskMap.update(appTask);

        List<AppTask> appTaskList = appTaskMap.getAppTasksByStatus("UPDATE");
        Assert.assertNotNull(appTaskList);
        Assert.assertEquals("Size",1, appTaskList.size());
        Assert.assertEquals("Root_URL","http:update", appTaskList.get(0).getRoot_url());
        Assert.assertEquals("CURR_URL","http:updateCurr", appTaskList.get(0).getCurr_url());
        Assert.assertEquals("LAST_URL","http:updateLast", appTaskList.get(0).getLast_url());
        Assert.assertEquals("STATUS","UPDATE", appTaskList.get(0).getStatus());
    }

    @Test
    public void getAll() {
        AppTask appTask = new AppTask();
        appTask.setRoot_url("http:all");
        Assert.assertNotNull(appTaskMap.insert(appTask));

        List<AppTask> appTaskList = appTaskMap.getAll();
        Assert.assertTrue(appTaskList.size()==1);
    }
}