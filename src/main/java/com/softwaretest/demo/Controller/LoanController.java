package com.softwaretest.demo.Controller;

import com.softwaretest.demo.Controller.RemoteResponse.CheckIdentityResponse;
import com.softwaretest.demo.Controller.RemoteResponse.LoginSuccessResponse;
import com.softwaretest.demo.Controller.Request.CheckIdentityRequest;
import com.softwaretest.demo.Controller.Request.LoginRequest;
import com.softwaretest.demo.Controller.Request.LogoutRequest;
import com.softwaretest.demo.Service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoanController {
    @Autowired
    private LoanService loanService;


    @Autowired
    private RestTemplate restTemplate;


    /*
    @Description : 前端发来身份验证的请求
     */
    @RequestMapping("/check")
    public ResponseEntity<HashMap<String,Object>>  checkIdentity(@RequestBody CheckIdentityRequest request){
        HashMap<String,Object> responseMap = new HashMap<>();

        //向服务器发送请求的封装map
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("IDNumber",request.getIDNumber());
        requestMap.put("login-token",request.getToken());
        CheckIdentityResponse response = restTemplate.getForObject("http://10.176.122.174:8080/account/check",CheckIdentityResponse.class,requestMap);
        responseMap.put("code",response.getCode());
        responseMap.put("success",response.isFlag());
        responseMap.put("token",request.getToken());
        return ResponseEntity.ok(responseMap);
    }

    /*
    @Description: 前端发送请求进行登录，后端调用服务器登录接口
    @Param
       code 表示请求服务器的响应状态码
     */
    @RequestMapping("/login")
    public ResponseEntity<HashMap<String,Object>> login(@RequestBody LoginRequest request){
        HashMap<String,Object> responseMap = new HashMap<>();

        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("username",request.getUsername());
        requestMap.put("password",request.getPassword());
        ResponseEntity<LoginSuccessResponse> loginResponse = restTemplate.postForEntity("http://10.176.122.174:8080/sys/login/restful",requestMap,LoginSuccessResponse.class);
        LoginSuccessResponse responseBody = loginResponse.getBody();
        responseMap.put("code",loginResponse.getStatusCodeValue());
        if(responseBody!=null){
            responseMap.put("expireTime",responseBody.getExpireTime());
            responseMap.put("token",responseBody.getToken());
        }
        return ResponseEntity.ok(responseMap);
    }

    @RequestMapping("/logout")
    public ResponseEntity<HashMap<String,Object>> logout(@RequestBody LogoutRequest request){
        HashMap<String,Object> responseMap = new HashMap<>();
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("login-token",request.getToken());
        ResponseEntity<String> logoutResponse = restTemplate.getForEntity("http://10.176.122.174:8080:sys/logout",String.class,requestMap);
        responseMap.put("code",logoutResponse.getStatusCodeValue());
        return ResponseEntity.ok(responseMap);
    }



    /*
    @Description : 测试用Demo
     */
    @RequestMapping("/hello")
    public String hello(){
        return "redirect:hello.html";
    }

}

