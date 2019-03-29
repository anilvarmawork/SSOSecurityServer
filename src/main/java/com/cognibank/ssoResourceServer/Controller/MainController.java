package com.cognibank.ssoResourceServer.Controller;


import com.cognibank.ssoResourceServer.Model.User;
import com.cognibank.ssoResourceServer.Repository.UserRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@RestController("/")
public class MainController {

    @Autowired
    private UserRedisRepository userRedisRepository;


    @GetMapping("helloWorld")
    public String HelloWorld() {
        System.out.print("sadsdisand ");
        return "hello";
    }

    //Can
    @PostMapping (path = "userManagement" , consumes = "application/json", produces = "application/json")
    public User sendDataToUserManagement(@RequestBody User user) {
        System.out.print("sendDataToUserManagement " + user.getUserName());
        User mailAndPhone = new User();
        mailAndPhone.setEmail("anilvarma@gmail.com");
        mailAndPhone.setPhone("1408937230498");
        return mailAndPhone;
    }

    //Amit
    @PostMapping (path = "notification" , consumes = "application/json", produces = "application/json")
    public void sendDataToNotification(@RequestBody String emailOrPhone) {
        System.out.print("sendDataToNotification " + emailOrPhone);
    }


    //Receive data from UI and forward it to UserManagement team and receive email address and phone number and forward email/phone to UI
   @PostMapping(path = "loginUser", consumes = "application/json", produces = "application/json")
    public User loginUser (@RequestBody User user) {
        System.out.println(user.getUserName());
        final String uri = "http://localhost:8080/userManagement";
       RestTemplate restTemplate = new RestTemplate();
       User userObjFromUserManagement =  restTemplate.postForObject(uri,user,User.class);
       System.out.println(("LoginUser sending to UM " ) + userObjFromUserManagement);

       // store in redis so can compare once receive from UI team to generate otp
       //create constructor to just return email and phone
       return userObjFromUserManagement;
    }


    //Receive Phone or Email from UI and send it to Notifications team  (---Pending OTP generation, Rabbit MQ)
    @PostMapping(path = "receivingEmailOrPhoneFromUI", consumes = "application/json", produces = "application/json")
    public void receivingEmailOrPhoneFromUI(@RequestBody String emailOrPhone){
        final String uri = "http://localhost:8080/notification";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(emailOrPhone, headers);
        restTemplate.exchange(uri, HttpMethod.POST, request, new ParameterizedTypeReference<String>() { });
    }

    //store test for redis

    @PostMapping(path = "pingRedis", consumes = "application/json", produces = "application/json")
    public void storeRedisData(@RequestBody User user){
        System.out.println("not stream " + user.getEmail());
        userRedisRepository.addUser(user);
      User user2 = userRedisRepository.getUser(user.getUserId());
      user2.setPhone("aadasdasd");
      userRedisRepository.addUser(user2);
      System.out.println(userRedisRepository.getUser(user2.getUserId()).toString());

    }



    //Get OTP from UI team

    //Validate OTP and generate/send authID to UI


    //Store authID in REDIS




}
