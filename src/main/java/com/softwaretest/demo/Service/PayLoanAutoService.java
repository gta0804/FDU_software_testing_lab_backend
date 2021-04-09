package com.softwaretest.demo.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softwaretest.demo.Domain.Account;
import com.softwaretest.demo.Domain.Flow;
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

  public Set<Flow> payLoanAutomatically(){
    HashSet<Flow> flows = new HashSet<>();
    a:for (Loan loan : loanRepository.findAll()) {
      Long accountID = loan.getAccountId();
      Account account = accountRepository.findById(accountID).orElse(null);
      if (null == account)continue a;
      List<Installment> installments = loan.getInstallments();
      for (Installment installment : installments) {
        //分期过期且没还钱
        if (loanService.isExpired(installment) && !loanService.isPaid(installment)){
          double fine = loanService.getFine(loan);
          //没有交罚款
          if (fine > 0){
            if (account.getBalance() >= fine){
              flows.add(loanService.payFine(loan.getId(),fine,true));
            }else{
              continue a;
            }
          }
          double amountRemained = installment.getAmountRemained();
          if (account.getBalance() >= amountRemained){
            if(payBill(installment,account)){
              Flow flow = new Flow("贷款还款",account.getAccountId(),amountRemained,new Timestamp(System.currentTimeMillis()));
              flows.add(flow);
            }

          }
        }else{
          continue;
        }
      }
    }
    return flows;
  }

  @Transactional
  public boolean payBill(Installment installment,Account account){
    if (account.getBalance() >= installment.getAmountRemained()){
      account.setBalance(account.getBalance()-installment.getAmountRemained());
      installment.setAmountRemained(0.0);
      accountRepository.save(account);
      installmentRepository.save(installment);
      return true;
    }
    return false;
  }
}