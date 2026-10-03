package com.devsu.hackerearth.backend.account.model.dto;

import java.util.Date;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankStatementDto {
    
	private Date date;
	private String client;
	private String accountNumber;
	private String accountType;
	private double initialAmount;
    private boolean isActive;
	private String transactionType;
	private double amount;
	private double balance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof BankStatementDto)) return false;
        BankStatementDto that = (BankStatementDto) o;
        if (Double.compare(that.initialAmount, initialAmount) != 0) return false;
        if (isActive != that.isActive) return false;
        if (Double.compare(that.amount, amount) != 0) return false;
        if (Double.compare(that.balance, balance) != 0) return false;
        if (!Objects.equals(client, that.client)) return false;
        if (!Objects.equals(accountNumber, that.accountNumber)) return false;
        if (!Objects.equals(accountType, that.accountType)) return false;
        if (!Objects.equals(transactionType, that.transactionType)) return false;
        if (date != null && that.date != null) {
            return Math.abs(date.getTime() - that.date.getTime()) < 5000;
        }
        return date == null && that.date == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, accountNumber, accountType, initialAmount, isActive, transactionType, amount, balance);
    }
}
