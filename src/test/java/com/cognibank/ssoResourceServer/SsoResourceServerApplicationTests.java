package com.cognibank.ssoResourceServer;

import com.cognibank.ssoResourceServer.Controller.MainController;
import com.cognibank.ssoResourceServer.Model.User;
import com.cognibank.ssoResourceServer.Repository.UserRedisRepository;
import org.junit.Assert;
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

	@Autowired
	private MainController mainController;

	@Autowired
	private UserRedisRepository userRedisRepository;

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
				"  \"userName\" : \"anil\",\n" +
				"  \"password\" : \"12345\"\n" +
				"}")).andExpect(status().isOk()).andDo(print ()).andExpect(content ().string(containsString ("aniXXX@gmail.com")));
	}

//	@Test
//	public void testReceivingEmailOrPhoneFromUI() throws Exception {
//		this.mockMvc.perform(post("/receivingEmailOrPhoneFromUI").contentType("application/json").content("{\n" +
//				"  \"email\" : \"anilvarma123@gmail.com\"\n" +
//				"}")).andDo(print ()).andExpect(status().isOk());
//	}
//
//	@Test
//	public void testingTheRedisServer() throws Exception {
//		this.mockMvc.perform(post("/pingRedis").contentType("application/json").content("{\n" +
//				"  \"userId\" : \"Pooja\",\n" +
//				"  \"phone\" : \"12345678\"\n" +
//				"}")).andDo(print ()).andExpect(status().isOk());
//	}


	@Test
	public void validateUserWithOTPWhenUserIsNotPresent() throws Exception {

		this.mockMvc.perform(post("/validateUserWithOTP").contentType("application/json").content("{\n" +
				"  \"userId\" : \"ABC\",\n" +
				"  \"otpCode\" : \"1234\"\n" +
				"}")).andDo(print ()).andExpect(status().isOk()).andExpect(content ().string(containsString ("User not found")));
	}

	@Test
	public void validateUserWithOTPWhenUserIsPresent() throws Exception {

		String userID = "ABCD";
		User newUser = new User();
		newUser.setUserId(userID);
		newUser.setOtpCode("1234");
		mainController.storeDataToRedis(newUser);
		this.mockMvc.perform(post("/validateUserWithOTP").contentType("application/json").content("{\n" +
				"  \"userId\" : \"" + userID + "\",\n" +
				"  \"otpCode\" : \"1234\"\n" +
				"}")).andDo(print ()).andExpect(status().isOk()).andExpect(content ().string(containsString ("User found!!! Hurray!!")));
	}

	@Test
	public void testOtpGeneration() throws Exception{

		Assert.assertNotNull(mainController.generateOTP());
		Assert.assertEquals(6,mainController.generateOTP().length());
	}

}
