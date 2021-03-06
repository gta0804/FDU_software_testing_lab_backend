package com.softwaretest.demo.Domain;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// 理财产品实体类
@Entity
public class WMP {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 所属的账户id
    private Long accountId;

    private Integer type; // 1:定期理财产品 2：基金 3：股票

    private Double amount;// 初期买入金额

    @OneToMany
    private List<Flow> benifits = new LinkedList<>();

    private String startDate;

    public WMP(Long accountId, Integer type, Double amount, ArrayList<Flow> benifits) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.benifits = benifits;
    }

    public WMP() {

    }

    public void setBenifits(List<Flow> benifits) {
        this.benifits = benifits;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<Flow> getBenifits() {
        return benifits;
    }

    public void setBenifits(ArrayList<Flow> benifits) {
        this.benifits = benifits;
    }
}
