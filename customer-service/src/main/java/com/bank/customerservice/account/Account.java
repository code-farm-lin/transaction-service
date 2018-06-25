package com.bank.customerservice.account;

import com.bank.customerservice.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Document
public class Account {

    @Id
    private String id;


    @NotNull
    private String customerId;

    @NotNull
    private String accountType;

    @NotNull
    private BigDecimal initialCredit;

    private Date accountOpenDate;

    @Transient
    private BigDecimal balance;

    @Transient
    private List<Transaction> transactions;
}
