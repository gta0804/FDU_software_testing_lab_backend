package com.softwaretest.demo.Service;

import com.softwaretest.demo.DemoApplication;
import com.softwaretest.demo.Domain.Account;
import com.softwaretest.demo.Domain.Flow;
import com.softwaretest.demo.Domain.Installment;
import com.softwaretest.demo.Domain.Loan;
import com.softwaretest.demo.Repository.AccountRepository;
import com.softwaretest.demo.Repository.FlowRepository;
import com.softwaretest.demo.Repository.InstallmentRepository;
import com.softwaretest.demo.Repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest(classes = DemoApplication.class)
public class LoanServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private InstallmentRepository installmentRepository;

    @Autowired
    private FlowRepository flowRepository;

    @Test
    void insertAccount(){
        Account account = new Account();
        account.setIdNumber("123456");
        account.setType("储蓄");
        account.setBalance(100000.00);
        account.setCustomerName("郭泰安");
        accountRepository.save(account);
    }

    @Test
    void insertLoan(){
        List<Account> accounts = accountRepository.findByIdNumber("123456");
        if(accounts == null){
            return;
        }
        Account account = accounts.get(0);
        Loan loan = new Loan();
        loan.setAccountId(account.getAccountId());
        loan.setAmount(12000.00);
        loan.setInterestRate(0.05);
        loan.setStartDate(castStringToTimeStamp("2021-02-28 00:00:00"));

        Installment installment1 = new Installment(4200.00,4200.00,castStringToTimeStamp("2021-03-28 00:00:00"));
        Installment installment2 = new Installment(4200.00,4200.00,castStringToTimeStamp("2021-03-28 00:00:00"));
        Installment installment3 = new Installment(4200.00,4200.00,castStringToTimeStamp("2021-04-28 00:00:00"));
        List<Installment> installments = new LinkedList<>();
        installments.add(installment1); installments.add(installment2); installments.add(installment3);
        installmentRepository.saveAll(installments);
        loan.setInstallments(installments);
        loanRepository.save(loan);

        Flow flow = new Flow("贷款发放",account.getAccountId(),12000.00,castStringToTimeStamp("2021-02-28 00:00:00"));
        flowRepository.save(flow);
    }

    private Timestamp castStringToTimeStamp(String timeStr){
        return Timestamp.valueOf(timeStr);
    }

}
