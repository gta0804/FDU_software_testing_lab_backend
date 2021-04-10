package com.softwaretest.demo.Controller;

import com.softwaretest.demo.Controller.Request.BuyWMPRequest;
import com.softwaretest.demo.Service.WMPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WMPController {
    @Autowired
    private WMPService wmpService;

    @PostMapping("/wmp/buy")
    public ResponseEntity<HashMap<String,Object>> buyWMP(@RequestParam BuyWMPRequest request){
        HashMap<String,Object> map = new HashMap<>();
        map.put("success", wmpService.buyWMP(request));
        return ResponseEntity.ok(map);
    }
}
