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
    }

    @Test
    public void getAppTasksInAndOrderByStatus() {
    }

    @Test
    public void getAppTasksByRootUrlAndJClass() {
    }

    @Test
    public void getAll() {
    }

    @Test
    public void insert() {
        AppTask appTask = new AppTask();
        appTask.setRoot_url("http");
        Assert.assertNotNull(appTaskMap.insert(appTask));
    }

    @Test
    public void update() {
    }
}