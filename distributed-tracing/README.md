# Distributed tracing

Two services: tracing-service-one calls tracing-service-two


Start zipkin-server:

    curl -sSL https://zipkin.io/quickstart.sh | bash -s
    java -jar zipkin.jar
    
    in browser open: http://127.0.0.1:9411/ for zipkin UI


start config-and-service-discovery/discovery-server first

In both services:

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-sleuth</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zipkin</artifactId>
            <version>2.2.8.RELEASE</version>
        </dependency>

start both services.

Demo

    curl http://localhost:<random_port_of_service_one>/hi
    Hi, from tracing-service-two
    

Checkout zipkin server UI for the trace

