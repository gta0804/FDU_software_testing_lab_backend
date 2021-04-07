package com.softwaretest.demo.Service;

import com.softwaretest.demo.Controller.Response.CheckResponse;
import com.softwaretest.demo.Domain.Account;
import com.softwaretest.demo.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class LoanService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    public LoanService(){

    }

    public List<CheckResponse> getLoans(String idNumber){
        List<Account> accounts = accountRepository.findByIdNumber(idNumber);
        if(accounts == null){
            return null;
        }

        List<CheckResponse> checkResponses = new LinkedList<>();

        return checkResponses;
    }
}
