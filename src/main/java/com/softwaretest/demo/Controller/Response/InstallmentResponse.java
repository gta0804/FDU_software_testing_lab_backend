package com.softwaretest.demo.Controller.Response;

import java.sql.Timestamp;

public class InstallmentResponse {
    private Double amount;
    private Double amountRemained;

    private Timestamp deadline;

    public InstallmentResponse(Double amount, Double amountPaid, Timestamp deadline){
        this.amount = amount;
        this.amountRemained = amountPaid;
        this.deadline = deadline;
    }

    public Double getAmount() {
        return amount;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public Double getAmountRemained() {
        return amountRemained;
    }
}
