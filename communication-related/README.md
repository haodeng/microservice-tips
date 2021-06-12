# Communication Related
start config-and-service-discovery/discovery-server first

## Synchronous RestTemplate
Check Eureka server to get post service name.

In client-resttemplate-service, use http://post-service/posts/ to call post service
try:

    curl http://locallhost:8080/client/posts

## Reactive WebClient

## OpenFeign

## Load Balancer

## Circuit Breaker (resilience4j)