package com.bank.customerservice.customer;

import com.bank.customerservice.account.Account;
import com.bank.customerservice.account.AccountRepository;
import com.bank.customerservice.account.AccountType;
import com.bank.customerservice.transaction.Transaction;
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
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureDataMongo
public class CustomerControllerTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;


    private TransactionClient transactionClient;

    private CustomerController customerController;

    private WebTestClient webTestClient;


    Account savedAccount;

    @Before
    public void init() {
        transactionClient = mock(TransactionClient.class);

        customerController = new CustomerController(customerRepository, accountRepository, transactionClient);
         webTestClient = WebTestClient.bindToController(customerController).build();
        Mono<Customer> cs =  customerRepository.save(Customer.builder().firstName("test").lastName("test").build());

        Account account = Account.builder()
                .accountType(AccountType.CURRENT_ACCOUNT.name())
                .customerId(cs.block().getId())
                .accountOpenDate(new Date())
                .initialCredit(BigDecimal.valueOf(20))
                .build();
        savedAccount = accountRepository.save(account).block();

    }

    @Test
    public void should_return_customer_details () {

        when(transactionClient.getTransactions(anyString())).thenReturn(
                Arrays.<Transaction>asList(Transaction.builder().direction("IN").accountId("23fadf").amount(BigDecimal.valueOf(10)).transactionDate(new Date()).build()));
       List<CustomerAccountDetails> details = webTestClient.get()
                .uri("/v1/customers/query?customerid="+savedAccount.getCustomerId())
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(CustomerAccountDetails.class)
                .hasSize(1)
                .returnResult()
                .getResponseBody();

        assertEquals(1, details.size());
        assertEquals(1, details.get(0).getAccounts().size());
        assertEquals(BigDecimal.valueOf(10), details.get(0).getAccounts().get(0).getBalance());
    }
}