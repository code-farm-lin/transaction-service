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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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

@SpringBootApplication
@Log
public class AccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}

	@Autowired
	AccountRepository accountRepository;
	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	TransactionClient transactionClient;


	InitializingBean SeedInitialData() {
		return () -> {
			Flux<Customer> customers = just(Customer.builder().firstName("Liwen").lastName("Lin").build(),
					Customer.builder().firstName("Joe").lastName("Doe").build(),
					Customer.builder().firstName("John").lastName("Solomon").build())
					.flatMap(customer -> customerRepository.save(customer));

			Flux<Account> accountPlux =  customers
					.map(this::createAccounts)
					.flatMap(accountRepository::saveAll);


//			accountPlux.map(this::createTransactions)
//					.flatMap(Flux::merge)
//					.map(transactionClient::postTransaction)
//					.subscribe(t -> log.info(t.toString()));
			accountPlux.subscribe(t -> log.info(t.toString()));
			customers.subscribe(t -> log.info(t.toString()));
			};
	}

	Flux<Transaction> createTransactions ( final Account account){
		return Flux.just(Arrays.stream(Direction.values())
				.map(dir -> creatRandomTransaction(account, dir)
				).flatMap(List::stream).collect(toList()).toArray(new Transaction[0]));

	}

	List<Transaction> creatRandomTransaction (Account account, Direction dir){
		return IntStream.range(1, 5).
				mapToObj(id ->
						Transaction.builder().accountId(account.getId())
								.transactionDate(Date.from(ZonedDateTime.now().minus(RandomUtils.nextLong(7, 365), ChronoUnit.DAYS).toInstant()))
								.amount(BigDecimal.valueOf(new Random().nextInt(1000)))
								.direction(dir.name())
								.build()).collect(toList());
	}

	Account createAccount(Customer cs) {
		return Account.builder()
				.customerId(cs.getId())
				.accountType(AccountType.CURRENT_ACCOUNT.name())
				.initialCredit(BigDecimal.valueOf(new Random().nextInt(1000)))
				.accountOpenDate(Date.from(ZonedDateTime.now().minus(RandomUtils.nextLong(7, 365), ChronoUnit.DAYS).toInstant()))
				.build();
	}

	List<Account> createAccounts ( final Customer cs){

		return IntStream.range(1, 3).
				mapToObj(id -> createAccount(cs)
				).collect(toList());

	}
	enum Direction{
		IN, OUT;
	}
}
