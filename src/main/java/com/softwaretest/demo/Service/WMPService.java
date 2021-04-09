package com.softwaretest.demo.Service;

import com.softwaretest.demo.Repository.WMPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WMPService {
    @Autowired
    public WMPService(WMPRepository wmpRepository) {
    }


}
