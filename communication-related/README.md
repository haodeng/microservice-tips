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


## Circuit Breaker (resilience4j)