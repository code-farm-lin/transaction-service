package com.bank.customerservice.transaction;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 *This should be replaced by messaging service instead of rest call as it is behind the firewall for internal
 * service communication. Due to time constrain, I am not able to implement the messaging service. My iniytial thought
 * is to use Kafka for messaging. However, this depends on requirements.
*/
@Service
@Log
public class TransactionClient {


    @Value("${bank.transaction.serivce.uri}")
    private String transationSeriveURI;


    public Mono<Transaction> postTransaction(Transaction transaction) {

        WebClient webclient = WebClient.builder().baseUrl(transationSeriveURI)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        return  webclient.post()
                .uri("/transactions")
                .body(Mono.just(transaction), Transaction.class)
                .exchange()
                .map(this::processResponse)
                .block();



    }
    private Mono<Transaction> processResponse(ClientResponse clientResponse) {
        if(clientResponse.statusCode().isError()) {
            //TODO: handle error
            log.warning("Error post transction");
            throw new RuntimeException("unable to post transaction");
        } else {
            return clientResponse.bodyToMono(Transaction.class);
        }

    }

    @GetMapping("/transactions/query")
    public List<Transaction> getTransactions(@RequestParam("accountid") String accountid) {
        WebClient webClient = WebClient.builder()
                .baseUrl(transationSeriveURI)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

        Flux<Transaction> tranasctions =  webClient.get().uri("transactions/query?accountid="+accountid).retrieve().bodyToFlux(Transaction.class);


        return tranasctions.collectList().block();
    }

}