package com.softwaretest.demo.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import com.softwaretest.demo.Controller.Response.AccountDetailsResponse;
import com.softwaretest.demo.Controller.Response.AccountResponse;
import com.softwaretest.demo.Controller.Response.InstallmentResponse;
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
        if (isExpired(installment) && !isPaid(installment)){
          double fine = getFine(loan);
          //没有交罚款
          if (fine > 0){
            if (account.getBalance() >= fine){
              flows.add(payFine(loan.getId(),fine,true));
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


  public Flow payFine(Long loanId, Double amount, boolean overload) {
    Loan loan = loanRepository.findById(loanId).orElse(null);
    if (loan == null) {
      return null;
    }
    Account account = accountRepository.findById(loan.getAccountId()).orElse(null);
    if (account == null) {
      return null;
    }
    if (account.getBalance() < amount) {
      return null;
    }
    List<Installment> installments = loan.getInstallments();
    for (Installment installment : installments) {
      if (isExpired(installment) && !installment.getFineHasPaid()) {
        installment.setFineHasPaid(true);
      }
    }
    double remainBalance = Double.parseDouble(String.format("%.2f", account.getBalance() - amount));
    account.setBalance(remainBalance);
    installmentRepository.saveAll(installments);
    loan.setInstallments(installments);
    loanRepository.save(loan);
    accountRepository.save(account);

    Flow flow = new Flow("罚金缴纳", account.getAccountId(), amount, new Timestamp(System.currentTimeMillis()));
    flowRepository.save(flow);
    return flow;
  }


    /*
    @Description : 获取罚金
     */

  public double getFine(Loan loan){
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

  public boolean isPaid(Installment installment){
    return installment.getAmountRemained() <0.01;
  }


  public boolean isExpired(Installment installment){
    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
    Timestamp expiredTime = installment.getDeadline();
    return expiredTime.before(currentTime)&&!isPaid(installment);
  }

}