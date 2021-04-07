package com.softwaretest.demo.Controller;

import com.softwaretest.demo.Controller.RemoteResponse.CheckIdentityResponse;
import com.softwaretest.demo.Controller.RemoteResponse.LoginSuccessResponse;
import com.softwaretest.demo.Controller.Request.CheckIdentityRequest;
import com.softwaretest.demo.Controller.Request.LoginRequest;
import com.softwaretest.demo.Controller.Request.LogoutRequest;
import com.softwaretest.demo.Controller.Response.AccountDetailsResponse;
import com.softwaretest.demo.Controller.Response.AccountResponse;
import com.softwaretest.demo.Service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoanController {
    @Autowired
    private LoanService loanService;


    @Autowired
    private RestTemplate restTemplate;


    /*
    @Description : 在查看账号之前需要先验证身份
     */
    @GetMapping("/account/check")
    public ResponseEntity<HashMap<String,Object>>  checkIdentity(@RequestParam String idNumber) {
        HashMap<String,Object> responseMap = new HashMap<>();

        //向服务器发送请求的封装map
        List<AccountResponse> responses = loanService.getAccounts(idNumber);
        if(responses == null){
            responseMap.put("success",false);
        }
        else{
            responseMap.put("success",true);
            responseMap.put("accounts",responses);
        }
        return ResponseEntity.ok(responseMap);
    }

    @GetMapping("/account/loan/details")
    public ResponseEntity<HashMap<String,Object>> getLoanDetails(@RequestParam Long accountId){
        HashMap<String,Object> map = new HashMap<>();
        List<AccountDetailsResponse> responses = loanService.getLoans(accountId);
        map.put("loans",responses);
        map.put("success",true);
        return ResponseEntity.ok(map);
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

