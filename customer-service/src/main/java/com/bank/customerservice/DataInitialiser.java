package com.bank.customerservice;

import com.bank.customerservice.account.Account;
import com.bank.customerservice.account.AccountRepository;
import com.bank.customerservice.account.AccountType;
import com.bank.customerservice.customer.Customer;
import com.bank.customerservice.customer.CustomerRepository;
import com.bank.customerservice.transaction.Transaction;
import com.bank.customerservice.transaction.TransactionClient;
import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Flux.just;

@Log
@Component
//@Profile("dev")
public class DataInitialiser implements ApplicationRunner {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TransactionClient transactionClient;


    public DataInitialiser() {

    }
    public void run(ApplicationArguments args) throws Exception {


            //create 3 customer

            Flux<Customer> customers = just(Customer.builder().firstName("Liwen").lastName("Lin").build(),
                    Customer.builder().firstName("Joe").lastName("Doe").build(),
                    Customer.builder().firstName("John").lastName("Solomon").build())
                    .flatMap(customer -> customerRepository.save(customer));

            //for each customer, create 2 account each (6 accounds in total)
            Flux<Account> accountPlux =  customers
                    .map(this::createAccounts)
                    .flatMap(accountRepository::saveAll);

            // for each accounts create 10 transactioins
			accountPlux.map(this::createTransactions)
					.flatMap(Flux::merge)
					.map(transactionClient::postTransaction)
					.subscribe(t -> log.info(t.toString()));
            accountPlux.subscribe(t -> log.info(t.toString()));
            customers.subscribe(t -> log.info(t.toString()));

    }

    public Account createAccount(Customer cs) {
        return Account.builder()
                .customerId(cs.getId())
                .accountType(AccountType.CURRENT_ACCOUNT.name())
                .initialCredit(BigDecimal.valueOf(new Random().nextInt(1000)))
                .accountOpenDate(Date.from(ZonedDateTime.now().minus(RandomUtils.nextLong(7, 365), ChronoUnit.DAYS).toInstant()))
                .build();
    }

    public List<Account> createAccounts ( final Customer cs){

        return IntStream.range(1, 3).
                mapToObj(id -> createAccount(cs)
                ).collect(toList());

    }


    public Flux<Transaction> createTransactions (final Account account){
        return Flux.just(Arrays.stream(Direction.values())
                .map(dir -> creatRandomTransaction(account, dir)
                ).flatMap(List::stream).collect(toList()).toArray(new Transaction[0]));

    }

    List<Transaction> creatRandomTransaction (Account account, Direction dir){
        return IntStream.range(1, 6).
                mapToObj(id ->
                        Transaction.builder().accountId(account.getId())
                                .transactionDate(Date.from(ZonedDateTime.now().minus(RandomUtils.nextLong(7, 365), ChronoUnit.DAYS).toInstant()))
                                .amount(BigDecimal.valueOf(new Random().nextInt(1000)*dir.getDirection()))
                                .direction(dir.name())
                                .build()).collect(toList());
    }


    enum Direction{
        IN(1), OUT(-1);
        int direction;
        private Direction(int direction) {
            this.direction = direction;
        }
        public int getDirection() {
            return direction;
        }
    }
    }
