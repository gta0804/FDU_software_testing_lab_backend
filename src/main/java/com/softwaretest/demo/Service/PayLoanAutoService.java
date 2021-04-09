package com.softwaretest.demo.Service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softwaretest.demo.Domain.Installment;
import com.softwaretest.demo.Domain.Loan;
import com.softwaretest.demo.Repository.AccountRepository;
import com.softwaretest.demo.Repository.FlowRepository;
import com.softwaretest.demo.Repository.InstallmentRepository;
import com.softwaretest.demo.Repository.LoanRepository;

@Service
public class PayLoanAutoService {
  @Autowired
  LoanService loanService;
  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private LoanRepository loanRepository;

  @Autowired
  private InstallmentRepository installmentRepository;

  @Autowired
  private FlowRepository flowRepository;
  @Autowired
  public PayLoanAutoService() {
  }

  public void payLoanAutomatically(){
    for (Loan loan : loanRepository.findAll()) {
      Long accountID = loan.getAccountId();
      List<Installment> installments = loan.getInstallments();
      for (Installment installment : installments) {
        //分期过期且没还钱
        if (loanService.isExpired(installment) && !loanService.isPaid(installment)){
          //已经交了罚款
          if (installment.getFineHasPaid()){

          }else{

          }
        }else{
          continue;
        }
      }
    }
  }
}
