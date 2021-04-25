package com.softwaretest.demo.Service;

import com.softwaretest.demo.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class WMPServiceTest {
    @Autowired
    private WMPService wmpService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private InstallmentRepository installmentRepository;

    @Autowired
    private FlowRepository flowRepository;

    @Autowired
    private WMPRepository wmpRepository;
    

    @BeforeEach
    void setUp() {
        flowRepository.deleteAll();
        wmpRepository.deleteAll();
        loanRepository.deleteAll();
        installmentRepository.deleteAll();
        accountRepository.deleteAll();

        LoanServiceTest loanServiceTest = new LoanServiceTest();
        loanServiceTest.insertAccountA();
        loanServiceTest.insertAccountB();
        loanServiceTest.insertAccountC();
    }

    @Test
    public void buyWMP() {
    }

    @Test
    public void updateWMPs() {
    }

    @Test
    public void allWMPs() {
    }

    @Test
    public void benifitsDetail() {
    }
}