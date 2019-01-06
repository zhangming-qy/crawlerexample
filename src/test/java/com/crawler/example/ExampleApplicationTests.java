package com.crawler.example;

import com.crawler.example.entity.AppTask;
import com.crawler.example.web.MsgPageLookup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = "app.scheduling.enable=false")
@SpringBootTest
public class ExampleApplicationTests {

	@Test
	public void contextLoads() {

	}

}
