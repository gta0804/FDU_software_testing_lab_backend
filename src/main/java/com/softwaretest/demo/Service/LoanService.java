package com.softwaretest.demo.Service;

import com.softwaretest.demo.Controller.Response.AccountDetailsResponse;
import com.softwaretest.demo.Controller.Response.AccountResponse;
import com.softwaretest.demo.Controller.Response.InstallmentResponse;
import com.softwaretest.demo.Domain.Account;
import com.softwaretest.demo.Domain.Flow;
import com.softwaretest.demo.Domain.Installment;
import com.softwaretest.demo.Domain.Loan;
import com.softwaretest.demo.Repository.AccountRepository;
import com.softwaretest.demo.Repository.FlowRepository;
import com.softwaretest.demo.Repository.InstallmentRepository;
import com.softwaretest.demo.Repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@Service
public class LoanService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private InstallmentRepository installmentRepository;

    @Autowired
    private FlowRepository flowRepository;

    @Autowired
    public LoanService(){

    }



    public List<AccountResponse> getAccounts(String idNumber){
        List<Account> accounts = accountRepository.findByIdNumber(idNumber);
        if(accounts == null){
            return null;
        }
        List<AccountResponse> accountDetailsResponses = new LinkedList<>();
        for(Account account: accounts){
            accountDetailsResponses.add(new AccountResponse(account.getAccountId(),account.getCustomerName(),account.getBalance(),account.getType()));
        }
        return accountDetailsResponses;
    }

    /*
    @Description :获取账号有关的贷款情况
     */


    public List<AccountDetailsResponse> getLoans(Long accountId){
        List<Loan> loans = loanRepository.findByAccountId(accountId);
        if(loans == null){
            return null;
        }

        List<AccountDetailsResponse> accountDetailsResponses = new LinkedList<>();
        for(Loan loan:loans){
            double fine = getFine(loan);
            List<InstallmentResponse> installmentResponses = new LinkedList<>();
            boolean isAllPaid = true;
            for(Installment installment:loan.getInstallments()){
                if(!isPaid(installment)){
                    isAllPaid = false;
                }
                if(!isAllPaid){
                    InstallmentResponse installmentResponse = new InstallmentResponse(installment.getAmount(),installment.getAmountRemained(),installment.getDeadline());
                    installmentResponses.add(installmentResponse);
                }
            }
            AccountDetailsResponse accountDetailsResponse = new AccountDetailsResponse(loan.getId(),loan.getAmount(),installmentResponses,loan.getInterestRate(),loan.getStartDate(),fine);
            accountDetailsResponses.add(accountDetailsResponse);
        }
        return accountDetailsResponses;
    }

    public boolean payFine(Long loanId,Double amount){
        Loan loan = loanRepository.findById(loanId).orElse(null);
        if(loan == null){
            return false;
        }
        Account account = accountRepository.findById(loan.getAccountId()).orElse(null);
        if(account == null){
            return false;
        }
        if(account.getBalance()<amount){
            return false;
        }
        List<Installment> installments = loan.getInstallments();
        for(Installment installment:installments){
            if(isExpired(installment)&&!installment.getFineHasPaid()){
                installment.setFineHasPaid(true);
            }
        }
        double remainBalance = Double.parseDouble(String.format("%.2f",account.getBalance()-amount));
        account.setBalance(remainBalance);
        installmentRepository.saveAll(installments);
        loan.setInstallments(installments);
        loanRepository.save(loan);
        accountRepository.save(account);

        Flow flow = new Flow("贷款罚金缴纳",account.getAccountId(),amount,new Timestamp(System.currentTimeMillis()));
        flowRepository.save(flow);
        return true;
    }

    /*
    @Description : 获取罚金
     */

    private double getFine(Loan loan){
        double result = 0;
        List<Installment> installments = loan.getInstallments();
        for(Installment installment: installments){
            if(isExpired(installment)&&!installment.getFineHasPaid()){
                result +=Double.parseDouble(String.format("%.2f",installment.getAmountRemained()*0.05)) ;
            }
        }
        return result;
    }

    /*
    @Description : 判断该贷款的某个分期是否过期
     */

    private boolean isPaid(Installment installment){
        return installment.getAmountRemained() <0.01;
    }


    private boolean isExpired(Installment installment){
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Timestamp expiredTime = installment.getDeadline();
        return expiredTime.before(currentTime)&&!isPaid(installment);
    }
}
