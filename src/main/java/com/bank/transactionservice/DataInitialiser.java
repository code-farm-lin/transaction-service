package com.bank.transactionservice;

import com.bank.transactionservice.transaction.Transaction;
import com.bank.transactionservice.transaction.TransactionRepository;
import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomUtils;
import org.reactivestreams.Publisher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log
@Component
@Profile("dev")
public class DataInitialiser implements ApplicationRunner {

    private TransactionRepository transactionRepository;

    public DataInitialiser(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Flux.just("id-1", "id-2", "id-3")
                .map(this::createTransaction)
                .flatMap(transactionRepository::saveAll)
                .subscribe(m -> log.info(m.toString()));
            return;
    }
    private List<Transaction> createTransaction(String accountId) {
       return IntStream.range(1,  50)
                .mapToObj(num ->
                        Transaction.builder().accountId(accountId)
                                .transactionDate(Date.from(ZonedDateTime.now().minus(RandomUtils.nextLong(7, 365), ChronoUnit.DAYS).toInstant()))
                                .amount(BigDecimal.valueOf(new Random().nextInt(1000)))
                                .direction("IN")
                                .build()).collect(Collectors.toList() );
    }


}
