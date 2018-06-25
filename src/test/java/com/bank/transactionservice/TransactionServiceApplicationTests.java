package com.bank.transactionservice;

import com.bank.transactionservice.transaction.Transaction;
import com.bank.transactionservice.transaction.TransactionController;
import com.bank.transactionservice.transaction.TransactionRepository;
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


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureDataMongo
public class TransactionServiceApplicationTests {

	private WebTestClient webTestClient;

	@Autowired
	private TransactionRepository transactionRepository;

	private TransactionController transactionController;

	@Before
	public void init() throws Exception{

		transactionController = new TransactionController(transactionRepository);

		webTestClient = WebTestClient.bindToController(transactionController).build();

	}


	@Test
	public void should_add_new_transaction() {
		Transaction transaction = Transaction.builder().transactionDate(new Date())
				.accountId("12345").amount(BigDecimal.TEN).direction("IN").build();
		webTestClient.post().uri("/v1/transactions")
				.body(Mono.just(transaction), Transaction.class)
				.exchange()
				.expectStatus()
				.isCreated()
				.expectHeader()
				.contentType(MediaType.APPLICATION_JSON_UTF8);

	}


	@Test
	public void should_show_invalid_request_when_transaction_is_not_valid() {
		Transaction transaction = Transaction.builder().transactionDate(new Date())
				.accountId("12345").direction("IN").build();
		webTestClient.post().uri("/v1/transactions")
				.body(Mono.just(transaction), Transaction.class)
				.exchange()
				.expectStatus()
				.isBadRequest()
				.expectHeader()
				.contentType(MediaType.APPLICATION_JSON_UTF8);

	}


}
