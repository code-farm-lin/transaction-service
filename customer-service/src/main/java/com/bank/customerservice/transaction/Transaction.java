package com.bank.customerservice.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Transaction {

    private String id;

    private String accountId;

    private Date transactionDate;

    private BigDecimal amount;

    private String direction;
}
