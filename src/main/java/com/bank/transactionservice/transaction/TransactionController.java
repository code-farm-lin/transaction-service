package com.bank.transactionservice.transaction;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
public class TransactionController {


    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * This will get all transactions belong to account accoundid.
     * @param accountid
     * @return
     */
    @GetMapping("v1/transactions/query")
    public Flux<Transaction> getAccountTransctions(@RequestParam("accountid") String accountid) {
        return transactionRepository.findByAccountId(accountid);
    }


    @GetMapping("v1/transactions/")
    public Flux<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }


    @PostMapping("/v1/transactions")
    Publisher<Transaction> saveTransaction(@RequestBody Mono<Transaction> transaction) {
        Mono<Transaction> transactionMono = transaction.flatMap(transactionRepository::save);
        transactionMono.subscribe();
        return transactionMono;

    }
}


