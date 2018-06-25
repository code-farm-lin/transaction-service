package com.bank.customerservice.customer;

import com.bank.customerservice.account.Account;
import com.bank.customerservice.account.AccountRepository;
import com.bank.customerservice.transaction.Transaction;
import com.bank.customerservice.transaction.TransactionClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CustomerController {


    private CustomerRepository customerRepository;

    private AccountRepository accountRepository;

    private TransactionClient transactionClient;

    @Autowired
    public CustomerController(CustomerRepository customerRepository, AccountRepository accountRepository, TransactionClient transactionClient) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.transactionClient = transactionClient;
    }

    @GetMapping("/v1/customers/{id}")
    public Mono<Customer> retrieveCustomer(@PathVariable String id) {
        return customerRepository.findById(id);
    }

    @GetMapping("/v1/customers/")
    public Flux<Customer> retrieveAllCustomer() {
        return customerRepository.findAll();
    }

    @GetMapping("/v1/customers/query")
    public Mono<CustomerAccountDetails> retrieveCustomerDetails(@RequestParam("customerid") String customerId) {
        Customer customer = customerRepository.findById(customerId).block();
        List<Account> accounts =   accountRepository.findByCustomerId(customerId).collectList().block();
        Map<String, List<Transaction>> transactions =   fetchTransactionInfo(accounts);
        Map<String, BigDecimal> balance =   fetchAccountBalance(transactions);

        accounts.stream().forEach(account ->{
                account.setTransactions(transactions.get(account.getId()));
                account.setBalance(balance.get(account.getId()));
                });
        CustomerAccountDetails customerDetails =
                CustomerAccountDetails.builder().accounts(accounts)
                        .customer(customer)
                        .build();

        return Mono.just(customerDetails);
    }

     Map<String, List<Transaction>> fetchTransactionInfo(List<Account> accounts) {
        Map<String, List<Transaction>> map = new HashMap();
        accounts.stream().forEach(
                account -> {
                    List<Transaction> transactions = transactionClient.getTransactions(account.getId());
                    map.put(account.getId(), transactions) ;
                });
        return map;
   }

     Map<String, BigDecimal> fetchAccountBalance(Map<String, List<Transaction>>  transactions) {
        Map<String, BigDecimal> map = new HashMap();
        transactions.entrySet().stream().forEach(e ->
                map.put(e.getKey(), e.getValue().stream().map(t -> t.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add)));
        return map;

    }
}

