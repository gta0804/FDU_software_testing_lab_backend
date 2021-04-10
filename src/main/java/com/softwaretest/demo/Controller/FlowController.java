package com.softwaretest.demo.Controller;

import com.softwaretest.demo.Controller.Response.FlowResponse;
import com.softwaretest.demo.Service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

@Controller
public class FlowController {

    @Autowired
    private FlowService flowService;

    public FlowController(){

    }

    @CrossOrigin("*")
    @GetMapping("/account/flow")
    public ResponseEntity<HashMap<String,Object>> getFlows(@RequestParam Long accountId,@RequestParam String option,@RequestParam String order){
        HashMap<String,Object> map = new HashMap<>();
        List<FlowResponse>  responses = null;
        if(option == null&& accountId == null){
            responses = flowService.findAll();
        }
        else if("amount".equals(option)){
            if(accountId!=null){
                responses = flowService.findByAccountIdOrderByAmount(accountId,order);
            }
            else{
                responses = flowService.findAllOrderByAmount(order);
            }
        }
        else if("date".equals(option)){
            if(accountId!=null){
                responses = flowService.findByAccountIdOrderByDate(accountId,order);
            }
            else{
                responses = flowService.findAllOrderByDate(order);
            }
        }
        map.put("flows",responses);
        map.put("success",true);
        return ResponseEntity.ok(map);
    }
}
