package com.cognibank.ssoResourceServer.Controller;


import com.cognibank.ssoResourceServer.Model.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController("/")
public class MainController {


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


    //Receive data from UI and forward it to UserManagement team and receive email address and phone number
   @PostMapping(path = "loginUser", consumes = "application/json", produces = "application/json")
    public User loginUser (@RequestBody User user) {
        System.out.println(user.getUserName());
        final String uri = "http://localhost:8080/userManagement";
       RestTemplate restTemplate = new RestTemplate();
       User result =  restTemplate.postForObject(uri,user,User.class);
       System.out.println(("LoginUser sending to UM " ) + result);
       return result;
    }

    //Pass Email Address and Phone number to the UI team (--Encrypt)



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


    //Get OTP from UI team

    //Validate OTP and generate/send authID to UI


    //Store authID in REDIS




}
