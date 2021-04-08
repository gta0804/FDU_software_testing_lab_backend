package com.softwaretest.demo.Controller.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class FinePaymentRequest {
    private Long loanId;
    private Double amount;

    @Autowired
    public FinePaymentRequest(){

    }

    public Long getLoanId() {
        return loanId;
    }

    public Double getAmount() {
        return amount;
    }
}
