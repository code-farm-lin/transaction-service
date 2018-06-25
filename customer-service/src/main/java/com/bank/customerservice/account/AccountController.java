package com.bank.customerservice.account;

import com.bank.customerservice.transaction.Transaction;
import com.bank.customerservice.transaction.TransactionClient;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@Log
public class AccountController {


    private final  AccountRepository accountRepository;


    private final TransactionClient transactionClient;

    @Autowired
    public  AccountController(AccountRepository accountRepository, TransactionClient transactionClient) {
        this.accountRepository = accountRepository;
        this.transactionClient = transactionClient;
    }

    @GetMapping(value = "/v1/accounts/query",headers = {"Accept=application/json","application/xml"}, produces = { "application/json", "application/xml" } )
    public @ResponseBody Flux<Account> retrieveAllAccounts(@RequestParam("customerid") String customerid) {
        Flux<Account> accountFlux = accountRepository.findByCustomerId(customerid);
        return accountFlux;
    }

    @PostMapping("/v1/accounts")
    public Mono<ServerResponse> createAccount(@RequestBody Account account) throws Exception {
        Mono<Account> newAccount = accountRepository.insert(account);
        if (account.getInitialCredit().compareTo(BigDecimal.ZERO) != 0) {
            newAccount.map(a -> postTransaction(a)).subscribe(t -> log.info(t.toString()));
        }
        return ServerResponse.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).contentType(MediaType.APPLICATION_JSON).body(newAccount, Account.class);
    }

    private Mono<Transaction> postTransaction(Account newAccount) {
       return transactionClient.postTransaction(Transaction.builder()
                .accountId(newAccount.getId())
                .amount(newAccount.getInitialCredit())
                .transactionDate(newAccount.getAccountOpenDate()).build());
    }
}

