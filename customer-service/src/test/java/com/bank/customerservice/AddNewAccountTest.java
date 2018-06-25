package com.bank.customerservice;


import com.bank.customerservice.account.Account;
import com.bank.customerservice.account.AccountController;
import com.bank.customerservice.account.AccountRepository;
import com.bank.customerservice.account.AccountType;
import com.bank.customerservice.customer.Customer;
import com.bank.customerservice.customer.CustomerRepository;
import com.bank.customerservice.transaction.Transaction;
import com.bank.customerservice.transaction.TransactionClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureDataMongo
public class AddNewAccountTest {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AccountController accountController;

    WebTestClient webTestClient;

    Optional<Customer> customer;

    @Autowired
    TransactionClient transactionClient;

    @Before
    public void init() throws Exception {

        accountController = new AccountController(accountRepository, transactionClient);

        webTestClient = WebTestClient.bindToController(accountController).build();

        customer = customerRepository
                .save(Customer.builder().firstName("test").lastName("test").build()).blockOptional();
    }

    @Test
    public void when_create_account_is_called_and_account_is_created_then_iscreated_is_returned() {
        if (!customer.isPresent()) {
            fail();
        }

        Account account = Account.builder()
                .accountType(AccountType.CURRENT_ACCOUNT.name())
                .customerId(customer.get().getId())
                .accountOpenDate(new Date())
                .initialCredit(BigDecimal.valueOf(20))
                .build();

        webTestClient.post().uri("/v1/accounts").body(Mono.just(account), Account.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8);


    }


}
