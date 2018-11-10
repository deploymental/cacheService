package com.sample.distributedservice;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(properties = {
		"ehpath=ehcache.xml"})
public class ApplicationTests {

	private final String URI = "/api/cache?value=5001";

	@Test
	public void testPutAndGetCache() throws Exception {
		String[] args =   new String[]{"--server.port=3003","--ehpath=ehcache.xml"};

		WebApplicationContext appContext = (WebApplicationContext) SpringApplication.run(Application.class, args);
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(appContext).build();

		mockMvc.perform(MockMvcRequestBuilders.put(URI));
		MvcResult getResult = mockMvc.perform(MockMvcRequestBuilders.get(URI)).andReturn();

		String content = getResult.getResponse().getContentAsString();
		Assert.assertEquals("5001",content);
	}

	@Test
	public void testDistributedPutAndGetCache() throws Exception {
		String[] args =  new String[]{"--server.port=3001","--ehpath=ehcache.xml"};
		String[] args1 =  new String[]{"--server.port=3002","--ehpath=ehcache1.xml"};

		WebApplicationContext appContext = (WebApplicationContext)SpringApplication.run(Application.class, args);
		WebApplicationContext appContext1 = (WebApplicationContext)SpringApplication.run(Application.class, args1);
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(appContext).build();
		MockMvc mockMvc1 = MockMvcBuilders.webAppContextSetup(appContext1).build();

		mockMvc.perform(MockMvcRequestBuilders.put(URI));
		MvcResult getResult = mockMvc1.perform(MockMvcRequestBuilders.get(URI)).andReturn();

		String content = getResult.getResponse().getContentAsString();
		Assert.assertEquals("5001",content);
	}

	@Test
	public void testCacheTTL() throws Exception {
		String[] args =   new String[]{"--server.port=3004","--ehpath=ehcache.xml"};

		WebApplicationContext appContext = (WebApplicationContext) SpringApplication.run(Application.class, args);
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(appContext).build();

		mockMvc.perform(MockMvcRequestBuilders.put(URI));
		Thread.sleep(5000);
		MvcResult getResult = mockMvc.perform(MockMvcRequestBuilders.get(URI)).andReturn();

		String content = getResult.getResponse().getContentAsString();
		Assert.assertEquals("Not found",content);
	}
}