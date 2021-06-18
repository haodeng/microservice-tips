# Api Gateway

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

## RequestRateLimiter by Redis
The RequestRateLimiter GatewayFilter factory uses a RateLimiter implementation to determine if the current request is allowed to proceed. 
If it is not, a status of HTTP 429 - Too Many Requests (by default) is returned.

Enable dependency:

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
    </dependency>

Start redis:

    docker run -d --name redis -p 6379:6379 redis:latest
    
Add config under spring.cloud.gateway.routes.filter:

    filters:
      - RewritePath=/api(?<path>/?.*), $\{path}
      - name: RequestRateLimiter
      args:
        redis-rate-limiter.replenishRate: 10
        redis-rate-limiter.burstCapacity: 20
 
 Add redis config under spring:
 
    redis:
        host: 10.2.32.186
 Note: Since we are using Docker to run redis, it will be running in a Docker network that won't understand localhost/127.0.0.1, as you might expect. 
 Since our app is running on localhost, and for the Docker container, localhost means its own network, we have to specify our system IP in place of it.                       

Create a keyresovler for all reuqests

    @Bean
    KeyResolver keyResolver() {
        return exchange -> Mono.just("1);
    }
    
Try one request (verbose):

     curl -v http://localhost:9000/api/api-a/ping?fakevar=1
     
     *   Trying ::1...
     * TCP_NODELAY set
     * Connected to localhost (::1) port 9000 (#0)
     > GET /api/api-a/ping?user=1 HTTP/1.1
     > Host: localhost:9000
     > User-Agent: curl/7.64.1
     > Accept: */*
     > 
     < HTTP/1.1 200 OK
     < X-RateLimit-Remaining: 19
     < X-RateLimit-Requested-Tokens: 1
     < X-RateLimit-Burst-Capacity: 20
     < X-RateLimit-Replenish-Rate: 10
     < Content-Type: text/plain;charset=UTF-8
     < Date: Fri, 18 Jun 2021 20:39:59 GMT
     < content-length: 5
     < 
     * Connection #0 to host localhost left intact
     api a* Closing connection 0

X-RateLimit-* indicates token info

Now let's try to bomb to api:

    curl -v http://localhost:9000/api/api-a/ping?fakevar=[1-100]
    
    At some point, we will get:
    
    * Found bundle for host localhost: 0x7fb7f3c1baa0 [can pipeline]
    * Could pipeline, but not asked to!
    * Re-using existing connection! (#0) with host localhost
    * Connected to localhost (::1) port 9000 (#0)
    > GET /api/api-a/ping?user=50 HTTP/1.1
    > Host: localhost:9000
    > User-Agent: curl/7.64.1
    > Accept: */*
    > 
    < HTTP/1.1 429 Too Many Requests
    < X-RateLimit-Remaining: 0
    < X-RateLimit-Requested-Tokens: 1
    < X-RateLimit-Burst-Capacity: 20
    < X-RateLimit-Replenish-Rate: 10
    < content-length: 0
    < 
    * Connection #0 to host localhost left intact
    * Closing connection 0

"429 Too Many Requests" is expected

## Metrics
By default, the gateway metrics filter runs as long as the property spring.cloud.gateway.metrics.enabled is not set to false

These metrics are then available to be scraped from /actuator/metrics/gateway.requests and can be easily integrated with Prometheus to create a Grafana dashboard.

Try:

    http://localhost:9000/actuator/metrics/spring.cloud.gateway.requests
    
    {
      "name": "spring.cloud.gateway.requests",
      "description": null,
      "baseUnit": "seconds",
      "measurements": [
        {
          "statistic": "COUNT",
          "value": 105.0
        },
        {
          "statistic": "TOTAL_TIME",
          "value": 0.9591748099999999
        },
        {
          "statistic": "MAX",
          "value": 0.010831643
        }
      ],
      "availableTags": [
        {
          "tag": "routeUri",
          "values": [
            "lb://api-a"
          ]
        },
        {
          "tag": "routeId",
          "values": [
            "api-a"
          ]
        },
        {
          "tag": "httpMethod",
          "values": [
            "GET"
          ]
        },
        {
          "tag": "outcome",
          "values": [
            "CLIENT_ERROR",
            "SUCCESSFUL"
          ]
        },
        {
          "tag": "status",
          "values": [
            "TOO_MANY_REQUESTS",
            "OK"
          ]
        },
        {
          "tag": "httpStatusCode",
          "values": [
            "200",
            "429"
          ]
        }
      ]
    }


## Circuit Breaker for Api gateway
Since spring gateway is based on reactive, we should use reactive circuitbreaker

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-circuitbreaker-reactor-resilience4j</artifactId>
        </dependency>

Config under spring.cloud.gateway.routes.filter:

    - name: CircuitBreaker
      args:
        name: myCircuitBreaker
        fallbackUri: forward:/fallback/default

Add a FallbackController for fallback path

Create Bean for Resilience4J, remember to use reactive version

    @Bean
        Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        ...

Try:

    curl http://localhost:9000/api/api-b/ping-slow
should return: default fallback, the fallback controller should be callled