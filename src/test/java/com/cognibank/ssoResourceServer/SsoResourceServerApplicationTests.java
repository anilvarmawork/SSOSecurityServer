package com.cognibank.ssoResourceServer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SsoResourceServerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() {
	}

	@Test
	public void helloWorldTesting() throws Exception {
		this.mockMvc.perform(get("/helloWorld")).andExpect(status().isOk ())
				.andExpect(content ().string(containsString ("hello")));
	}

	@Test
	public void userNameAndPasswordInfo() throws Exception {
		this.mockMvc.perform(post("/loginUser").contentType("application/json").content("{\n" +
				"  \"userName\" : \"Anil\",\n" +
				"  \"password\" : \"Varma\"\n" +
				"}")).andDo(print ()).andExpect(content ().string(containsString ("anilvarma@gmail.com")));
	}

	@Test
	public void testReceivingEmailOrPhoneFromUI() throws Exception {
		this.mockMvc.perform(post("/receivingEmailOrPhoneFromUI").contentType("application/json").content("{\n" +
				"  \"email\" : \"anilvarma123@gmail.com\"\n" +
				"}")).andDo(print ()).andExpect(status().isOk());
	}

	@Test
	public void testingTheRedisServer() throws Exception {
		this.mockMvc.perform(post("/pingRedis").contentType("application/json").content("{\n" +
				"  \"userId\" : \"Anil\",\n" +
				"  \"email\" : \"Varma\"\n" +
				"}")).andDo(print ()).andExpect(status().isOk());
	}


}
