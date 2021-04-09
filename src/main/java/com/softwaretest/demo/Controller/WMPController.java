package com.softwaretest.demo.Controller;

import com.softwaretest.demo.Service.WMPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class WMPController {
    @Autowired
    private WMPService wmpService;


}
