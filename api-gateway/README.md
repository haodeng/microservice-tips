#Api Gateway
An API Gateway is a server that is the single entry point into the system. It is similar to the Facade pattern from object‑oriented design. The API Gateway encapsulates the internal system architecture and provides an API that is tailored to each client. It might have other responsibilities such as authentication, monitoring, load balancing, caching, request shaping and management, and static response handling.
Details: https://microservices.io/patterns/apigateway.html


Netflix zuul is deprecated, spring cloud gateway is recommended to use.

    <dependency>
	    <groupId>org.springframework.cloud</groupId>
	    <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>

start config-and-service-discovery/discovery-server first


Config:

    spring:
      application:
        name: gateway-service
      cloud:
        gateway:
          discovery:
            locator:
              enabled: true
              lowerCaseServiceId: true
          routes:
            - id: api-a
              uri: lb://api-a
              predicates:
                - Path=/api/api-a/**
              filters:
                - RewritePath=/api(?<path>/?.*), $\{path}
            - id: api-b
              uri: lb://api-b
              predicates:
                - Path=/api/api-b/**
              filters:
                - StripPrefix=1

* StripPrefix=1

When a request is made through the gateway to /api/api-b/ping, 
the request made to api-b looks like api-b/ping.

Validate

    curl http://localhost:9000/api/api-a/ping
    curl http://localhost:9000/api/api-b/ping

## Server side load balancer
Api gateway already provide load balancer feature. 

If the URL has a scheme of lb (such as lb://myservice), it uses the Spring Cloud LoadBalancerClient to resolve the name (myservice in this case) to an actual host and port and replaces the URI in the same attribute. 

Demo

Start 2 api-a instances

    curl http://localhost:9000/api/api-a/ping?fakevar=[1-10]

Check if requests are load balanced.