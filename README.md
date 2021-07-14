# A bit everything for SpringBoot Microservice
For springboot and spring cloud version compatibility check: https://spring.io/projects/spring-cloud

## Basic tips
* Collect and monitor metrics
* API doc
* Centralize logs
* Build and publish images
* Include git commit id
* Test

## Config and service discovery
* Config Server
* Vault protect sensitive config
* Config Client
* Discovery Server
* Alternatively use Consul as config server and discover server

## Communication
* inner communication
* by rest template
* by reactive webclient
* by feign
* timeout
* client side load balancer
* Circuit Breaker (resilience4j)

## Api Gateway
* Setup api gateway (the single entry of services)
* server side load balancer
* RequestRateLimiter by Redis
* gateway metric
* Circuit Breaker for Api gateway

## Distributed tracing
* Zipkin server
* Sleuth and Zipkin client

## Spring cloud stream
checkout: https://github.com/haodeng/spring-cloud-streams-demo


# Some of micro service patterns
* Externalized configuration
* Service discovery and registration
* Circuit Breaker
* Database per service
* API gateway
* CQRS
* Event sourcing
* Log aggregation
* Distributed tracing
* Audit logging
* Application metrics
* Health check API
