# Communication Related
start config-and-service-discovery/discovery-server first

## Inner service communication
### Synchronous RestTemplate
Check Eureka server to get post service name.

In client-resttemplate-service, use http://post-service/posts/ to call post service
try:

    curl http://locallhost:8080/client/posts

### Reactive WebClient

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    
    use reactive WebClient

### OpenFeign
enable feign client

    @EnableFeignClients

Add Feign proxy to post-service

    @FeignClient(name = "post-service", path = "/posts")
    public interface PostProxy {
    
        @GetMapping
        Collection<Post> getAllPosts();
    }

start post-service and client-feign-service, call:

    curl http://localhost:8080/client-feign/posts
    
## Load Balancer
### Client side load balancer
Ribbon has been deprecated. We should use recommended client-side-load-balancing approach from spring.io


Enable load balancer to resttemplate and reactive webclient builder

     @LoadBalanced

Start 2 instances of post-service, try get posts 10 times:

    curl http://localhost:8080/client/posts?fakevar=[1-10]

Each post-service should be hit 5 times

### Server side load balancer
checkout api gateway


## Circuit Breaker (resilience4j)
Spring Cloud Hystrix project is deprecate. resilience4j is recommended.

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
    </dependency>

Setup circuit breaker

    @Bean
    Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofMillis(500))
                .build();

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .failureRateThreshold(50F)
                .slowCallRateThreshold(50F)
                .build();

        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build());
    }

Enable circuit breaker in controller

    @GetMapping("/posts/slow")
    public Collection<Post> getAllPostsSlow() {
        Resilience4JCircuitBreaker circuitBreaker = circuitBreakerFactory.create("random-circuit");
        return circuitBreaker.run(() -> restTemplate.getForObject("http://post-service/posts/slow", Collection.class));
    }

Test

    curl http://localhost:8080/client/posts/slow?fakevar=[1-20]

First, it should get TimeLimiter 'random-circuit' recorded a timeout exception.
After a while, CircuitBreaker 'random-circuit' is OPEN and does not permit further calls