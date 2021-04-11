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
import org.springframework.test.annotation.Rollback;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
/**
 * @program: demo
 * @description:
 * @author: Shen Zhengyu
 * @create: 2021-04-11 11:10
 **/
@SpringBootTest(classes = PayLoanAutoService.class)
public class PayLoanAutoServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private InstallmentRepository installmentRepository;

    @Autowired
    private FlowRepository flowRepository;
    @Autowired
    private PayLoanAutoService payLoanAutoService;
    private Timestamp castStringToTimeStamp(String timeStr){
        return Timestamp.valueOf(timeStr);
    }

    /**
     *
     * Method: payLoanAutomatically()
     *
     */
    @Test
    @Rollback(false)
    public void testPayLoanAutomatically() throws Exception {
        List<Account> accounts = accountRepository.findByIdNumber("20008888");
        if (accounts.isEmpty()){
            Account accountToAdd = new Account();
            accountToAdd.setIdNumber("20008888");
            accountToAdd.setType("储蓄");
            accountToAdd.setBalance(500000.00);
            accountToAdd.setCustomerName("沈征宇");
            accountRepository.save(accountToAdd);
            accounts = accountRepository.findByIdNumber("20008888");
            Account account = accounts.get(0);
            Loan loan = new Loan();
            loan.setAccountId(account.getAccountId());
            loan.setAmount(12000.00);
            loan.setInterestRate(0.05);
            loan.setStartDate(castStringToTimeStamp("2021-01-23 00:00:00"));
            Installment installment0 = new Installment(4200.00,4200.00,castStringToTimeStamp("2021-02-23 00:00:00"));

            Installment installment1 = new Installment(4200.00,4200.00,castStringToTimeStamp("2021-03-23 00:00:00"));
            Installment installment2 = new Installment(4200.00,4200.00,castStringToTimeStamp("2021-04-23 00:00:00"));
            Installment installment3 = new Installment(4200.00,4200.00,castStringToTimeStamp("2021-05-23 00:00:00"));
            List<Installment> installments = new LinkedList<>();
            installments.add(installment0);
            installments.add(installment1); installments.add(installment2); installments.add(installment3);
            installmentRepository.saveAll(installments);
            loan.setInstallments(installments);
            loanRepository.save(loan);

            Flow flow = new Flow("贷款发放",account.getAccountId(),12000.00,castStringToTimeStamp("2021-01-23 00:00:00"));
            flowRepository.save(flow);
        }
        accounts = accountRepository.findByIdNumber("20008888");
        Account account = accounts.get(0);
        for (Flow flow : payLoanAutoService.payLoanAutomatically()) {
            System.out.println(flow);
        }

    }

    /**
     *
     * Method: payBill(Installment installment, Account account)
     *
     */
    @Test
    public void testPayBill() throws Exception {
//TODO: Test goes here...
    }


}