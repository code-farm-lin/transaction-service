package com.bank.customerservice.customer;

import com.bank.customerservice.account.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CustomerAccountDetails {
    private Customer customer;
    private List<Account> accounts;
}
