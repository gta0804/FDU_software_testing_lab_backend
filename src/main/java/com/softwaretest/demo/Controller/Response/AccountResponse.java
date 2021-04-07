package com.softwaretest.demo.Controller.Response;

public class AccountResponse {
    private Long accountId;
    private String customerName;

    private Integer balance;
    private String type;


    public AccountResponse(Long accountId,String customerName,Integer balance,String type){
        this.accountId = accountId;
        this.customerName = customerName;
        this.balance = balance;
        this.type = type;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getType() {
        return type;
    }

    public Integer getBalance() {
        return balance;
    }

    public String getCustomerName() {
        return customerName;
    }
}
