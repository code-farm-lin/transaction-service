# transaction-service
Provide rest service to receive requests to add transactions and query transactions. The service is intended to be a very simple and should be able to handle high throughput and scalable. 

## Instruction to run the application
- Checkout the customer-service application from github
- Run maven command: mvn spring-boot:run

the account service api will be available under http://localhost:8082/
This service provide endponts to enable transaction to be posted to the server and also enable the query of account transactions.
Similar to account serivce, API documentation of transction service can be reviewd at the following URL (http://localhost:8082/swagger-ui.html#/) once you have the transaction service up and running.
