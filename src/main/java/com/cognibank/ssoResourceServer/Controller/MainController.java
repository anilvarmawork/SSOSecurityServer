package com.cognibank.ssoResourceServer.Controller;


import com.cognibank.ssoResourceServer.Model.User;
import com.cognibank.ssoResourceServer.Model.emailAndPhone;
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
    public emailAndPhone sendDataToUserManagement(@RequestBody User user) {
        System.out.print("sendDataToUserManagement " + user.getUserName());
        emailAndPhone mailAndPhone = new emailAndPhone();
        mailAndPhone.setEmail("anilvarma@gmail.com");
        mailAndPhone.setPhone("1408937230498");
        return mailAndPhone;
    }

    //Amit
    @PostMapping (path = "notification" , consumes = "application/json", produces = "application/json")
    public void sendDataToNotification(@RequestBody String emailOrPhone) {
        System.out.print("sendDataToNotification " + emailOrPhone);
    }

   @PostMapping(path = "loginUser", consumes = "application/json", produces = "application/json")
    public emailAndPhone loginUser (@RequestBody User user) {
        System.out.println(user.getUserName());

        final String uri = "http://localhost:8080/userManagement";

       RestTemplate restTemplate = new RestTemplate();


       emailAndPhone result =  restTemplate.postForObject(uri,user,emailAndPhone.class);

//       System.out.println(restTemplate.getForObject(uri,String.class));
//       String result = restTemplate.getForObject(uri,String.class);

       return result;
    }

    @PostMapping(path = "receivingEmailOrPhoneFromUI", consumes = "application/json", produces = "application/json")
    public void receivingEmailOrPhoneFromUI(@RequestBody String emailOrPhone){
        final String uri = "http://localhost:8080/notification";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(uri,emailOrPhone,String.class);
        System.out.println(result);
    }



}
