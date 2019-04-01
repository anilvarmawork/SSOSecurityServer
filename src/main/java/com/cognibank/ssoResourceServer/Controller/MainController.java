package com.cognibank.ssoResourceServer.Controller;


import com.cognibank.ssoResourceServer.Model.User;
import com.cognibank.ssoResourceServer.Repository.UserRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
        mailAndPhone.setUserId("123456");
        mailAndPhone.setEmail("anilvarma@gmail.com");
        mailAndPhone.setPhone("+11234567890");
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

        //Get Email and phone from User management team
        System.out.println(user.getUserName());
       final String uri = "http://localhost:8080/userManagement";
      // final String uri = "http://10.61.142.247:8090/checkUserNamePassword";
       RestTemplate restTemplate = new RestTemplate();
       User userObjFromUserManagement =  restTemplate.postForObject(uri,user,User.class);
       System.out.println(("LoginUser sending to UM " ) + userObjFromUserManagement);

       // store in redis so that we can compare once we receive from UI team to generate otp
       storeDataToRedis(userObjFromUserManagement);

       //format email/phone before sending to the UI
       String emailID = userObjFromUserManagement.getEmail();
       String emailIDFormatted = emailID.replace(emailID.substring(3,emailID.indexOf('@')), "XXX");
      // System.out.println("emailIDFormatted " + emailIDFormatted);

       String phone = userObjFromUserManagement.getPhone();
       String phoneFormatted = phone.replace(phone.substring(4,9), "XXXXX");
      // System.out.println("phoneFormatted " + phoneFormatted);

       //Map only the required data that is to be sent to the user
       Map<String,String> dataToUI = new HashMap<String,String>();
       dataToUI.put("userID", userObjFromUserManagement.getUserId());
       dataToUI.put("email", emailIDFormatted);
       dataToUI.put("phone", phoneFormatted);

       System.out.println("Data sent to user----> " + dataToUI.toString());

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


    //Recieved OTP from User and returning authID if authenticated
    @PostMapping(path = "validateUserWithOTP", consumes = "application/json", produces = "application/json")
    public String validateUser(@RequestBody User user, HttpServletResponse response){

        String message = "User not found";
        User validateThisUser = userRedisRepository.getUser(user.getUserId());
        if(validateThisUser!=null) {
            if ((user.getOtpCode()).equalsIgnoreCase(validateThisUser.getOtpCode())) {
                String authCode = authCodeGenerator();
                response.addHeader("Authorization", authCode);
                validateThisUser.setAuthID(authCode);
                userRedisRepository.updateUser(validateThisUser);
                System.out.println("validateThisUser.toString() ----------------------------> " + validateThisUser.toString());
                message = "User found!!! Hurray!!";
            }
        }
        return message;

    }

    public String authCodeGenerator() {
        String credentials = UUID.randomUUID().toString();
        return credentials;
    }


    public void storeDataToRedis(User user){

        System.out.println("storeDataToRedis" + user.toString());
        if(userRedisRepository.getUser(user.getUserId())!=null){
            System.out.println("In update");
            userRedisRepository.updateUser(user);
        }else{
            System.out.println("In Add");
            userRedisRepository.addUser(user);
        }
    }


    public String generateOTP() {
        int otpNumber = 100000 + new Random().nextInt(900000);
        String otp = Integer.toString(otpNumber);
        System.out.println(otp);
        return otp;
    }



}
