package com.cognibank.ssoResourceServer.Controller;


import com.cognibank.ssoResourceServer.Model.User;
import com.cognibank.ssoResourceServer.Repository.UserRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
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
        mailAndPhone.setPhone("1234");
        return mailAndPhone;
    }

    //Amit
    @PostMapping (path = "notification" , consumes = "application/json", produces = "application/json")
    public void sendDataToNotification(@RequestBody String emailOrPhone) {
        System.out.print("sendDataToNotification " + emailOrPhone);
    }


    //Receive data from UI and forward it to UserManagement team and receive email address and phone number and forward email/phone to UI
   @PostMapping(path = "loginUser", consumes = "application/json", produces = "application/json")
    public Map<String, String> loginUser (@RequestBody User user) {
        System.out.println(user.getUserName());
        final String uri = "http://localhost:8080/userManagement";
       RestTemplate restTemplate = new RestTemplate();
       User userObjFromUserManagement =  restTemplate.postForObject(uri,user,User.class);
       System.out.println(("LoginUser sending to UM " ) + userObjFromUserManagement);

       // store in redis so can compare once receive from UI team to generate otp
       storeDataToRedis(userObjFromUserManagement);

       //format email/phone before sending to the UI
       String emailID = userObjFromUserManagement.getEmail();
       String phone = userObjFromUserManagement.getPhone();


       //Map only the required data that is to be sent to the user
       Map<String,String> dataToUI = new HashMap<String,String>();
       dataToUI.put("userID", userObjFromUserManagement.getUserId());
       dataToUI.put("email", emailID);
       dataToUI.put("phone", phone);

       return dataToUI;
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
    public void storeRedisDataPost(@RequestBody User user){
        System.out.println("not stream " + user.getEmail());

        if(userRedisRepository.getUser(user.getUserId())!=null){
            System.out.println("In update");
            userRedisRepository.updateUser(user);
        }else{
            System.out.println("In Add");
            userRedisRepository.addUser(user);
        }

        System.out.println(user.toString());

    }


    public void storeDataToRedis(User user){
        System.out.println("not stream " + user.getEmail());

        if(userRedisRepository.getUser(user.getUserId())!=null){
            System.out.println("In update");
            userRedisRepository.updateUser(user);
        }else{
            System.out.println("In Add");
            userRedisRepository.addUser(user);
        }

        System.out.println(user.toString());

    }


    //Get OTP from UI team

    //Validate OTP and generate/send authID to UI


    //Store authID in REDIS




}
