# Simple Bank Account and transaction management Application

##Initial functional requirement 

 
## Assumptions
As it is a bank transaction and account maintainance application. The following assumptions
has been made to justify the design decision.


Followings are my assumptions of what is important to teh system.  
1) The first assumption is that it is critical to be able handle high volume of transactions at the same time. the 
system should be able to handle high throughput
2) it is important that transaction records will not be lost without a trace. As 
a result, it requires a very reliable system and fault tolerance.
3) In addition, the system should be highly scalable as with the trend of online payments,
there will be increase volume of transactions. 
4) fast time to Market 


The following are in lower priority than the above. 
1) low latency, for example the transaction must be executed within a nano or milisecond.
2) development cost  
3) the data can be eventually consistent. 

## Initial System Design
 
 The initial system design is to build a green field application using MicroService. The 
 advantage of this choice is that the system can is highly scalable and able to handle high through put. 
 
 On the downside is that the system can be very complex which include the complete CI/CD and monitoring system. In addition, 
 the system cost is high.
 
 For this exercise, I impelement 3 micro-services using spring boot. The main technologies I used are:
 1) Spring boots
 2) in memory Mongo DB
 3) Spring WebFlux
 4) Swagger for API documentation
 
 Due to time constraint, I haven't complete a few important non-functional feature such as security using OAuth2, 
 exception handling, logging, load balance etc. 