# Communication Related
start config-and-service-discovery/discovery-server first

## Inner service communication
### Synchronous RestTemplate
Check Eureka server to get post service name.

In client-resttemplate-service, use http://post-service/posts/ to call post service
try:

    curl http://locallhost:8080/client/posts

### Reactive WebClient

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

## Circuit Breaker (resilience4j)