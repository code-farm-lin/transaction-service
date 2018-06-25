package com.bank.customerservice;

import com.bank.customerservice.account.Account;
import com.bank.customerservice.account.AccountController;
import com.bank.customerservice.account.AccountRepository;
import com.bank.customerservice.account.AccountType;
import com.bank.customerservice.transaction.TransactionClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.Date;

import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureDataMongo
public class AccountControllerWebFluxTest {

    @Autowired
    AccountRepository accountRepository;

     TransactionClient transactionClient ;


    AccountController accountController;

    WebTestClient webTestClient;

    @Before
    public void init() throws Exception{
        transactionClient = mock(TransactionClient.class);

        accountController = new AccountController(accountRepository, transactionClient);

        webTestClient = WebTestClient.bindToController(accountController).build();
        accountRepository.deleteAll().subscribe();
        Thread.sleep(1000);

    }

    private void createAccount( ) {
        Account account = Account.builder()
                .accountType(AccountType.CURRENT_ACCOUNT.name())
                .customerId("12345")
                .accountOpenDate(new Date())
                .initialCredit(BigDecimal.valueOf(20))
                .build();
       accountRepository.save(account).subscribe();
    }
    @Test
    public void find_all_accounts_test() throws InterruptedException {
        createAccount();

        Thread.sleep(4000);
        webTestClient.get().uri("/v1/accounts/").header(HttpHeaders.CONTENT_TYPE, "application/json")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Account.class).hasSize(1);


    }

    @Test
    public void find_all_accounts_without_any_account_stored_test() throws InterruptedException {

        Thread.sleep(1000);
        webTestClient.get().uri("/v1/accounts/").header(HttpHeaders.CONTENT_TYPE, "application/json")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Account.class)
                .hasSize(0);
    }
}
