package com.bank.transactionservice.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


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


    @PostMapping("v1/transactions")
    public Mono<ServerResponse> postTransaction(@RequestBody Transaction transaction) {
        Mono<Transaction> transactionMono = transactionRepository.save(transaction);
        return ServerResponse.created(ServletUriComponentsBuilder.fromCurrentRequest().build()
                .toUri()).contentType(MediaType.APPLICATION_JSON).body(transactionMono, Transaction.class);
    }
}


