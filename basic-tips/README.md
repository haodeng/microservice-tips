# Basic
## Collect and monitor metrics
Micrometer automatically exposes /actuator/metrics data into something your monitoring system can understand. 
All you need to do is include that vendor-specific micrometer dependency in your application.

    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-registry-prometheus</artifactId>
    </dependency>

Bind-mount your prometheus.yml from the host by running:

    docker run \
        -p 9090:9090 \
        -v /Users/dev/IdeaProjects/microservice-tips/basic-tips/prometheus.yml:/etc/prometheus/prometheus.yml \
        prom/prometheus 

Example prometheus.yml file:

    global:
      scrape_interval: 10s
    
    scrape_configs:
      - job_name: 'spring_micrometer'
        metrics_path: '/actuator/prometheus'
        scrape_interval: 5s
        static_configs:
          - targets: ['192.168.1.12:8080'] 
Note: Since we are using Docker to run Prometheus, it will be running in a Docker network that won't understand localhost/127.0.0.1, as you might expect. 
Since our app is running on localhost, and for the Docker container, localhost means its own network, we have to specify our system IP in place of it.

So instead of using localhost:8080, 192.168.1.12:8080 is used where 192.168.2.8 is my PC IP at the moment.

## API doc

        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-webmvc-core</artifactId>
            <version>1.5.9</version>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-ui</artifactId>
            <version>1.5.9</version>
        </dependency>
            
    http://localhost:8080/v3/api-docs
    http://localhost:8080/swagger-ui.htm"l"

## Centralize logs
ELK stack:

Checkout: https://github.com/haodeng/microservice-centralize-log-elk-demo

## Build and publish images

    <plugin>
        <groupId>com.google.cloud.tools</groupId>
        <artifactId>jib-maven-plugin</artifactId>
        <version>3.0.0</version>
            <configuration>
                <to>haodeng/microservice-basic-tip</to>
            </configuration>
    </plugin>
    
    docker login
    mvn compile jib:build
    
## Include git commit id

    <plugin>
        <groupId>pl.project13.maven</groupId>
        <artifactId>git-commit-id-plugin</artifactId>
            <configuration>
                <failOnNoGitDirectory>false</failOnNoGitDirectory>
            </configuration>
    </plugin>
    
    mvn clean spring-boot:run

Checkout info:

    curl http://localhost:8080/actuator/info
    
    {
      "git": {
        "commit": {
          "time": "2021-06-06T22:44:47Z",
          "id": "07b7c9c"
        },
        "branch": "main"
      },
      "build": {
        "version": "0.0.1-SNAPSHOT",
        "artifact": "basic-tips",
        "name": "basic-tips",
        "group": "demo.hao",
        "time": "2021-06-07T21:12:37.899Z"
      }
    }

## Include project info to health check
Add these to application.properties

    info.project.version: @project.version@
    info.java.version: @java.version@
    info.spring.framework.version: @spring-framework.version@
    info.spring.data.version: @spring-data-bom.version@

For yaml add:

    info:
      project.version: '@project.version@'
      java.version: '@java.version@'
      spring:
        framework.version: '@spring-framework.version@'
        data.version: '@spring-data-bom.version@'

Test

    curl http://localhost:8080/actuator/info   
                   
    {
      "project": {
        "version": "0.0.1-SNAPSHOT"
      },
      "java": {
        "version": "11.0.11"
      },
      "spring": {
        "framework": {
          "version": "5.3.7"
        },
        "data": {
          "version": "2021.0.1"
        }
      },
      "git": {
        "branch": "main",
        "commit": {
          "id": "6cffa6e",
          "time": "2021-08-08T20:22:58Z"
        }
      },
      "build": {
        "artifact": "basic-tips",
        "name": "basic-tips",
        "time": "2021-08-14T17:41:40.528Z",
        "version": "0.0.1-SNAPSHOT",
        "group": "demo.hao"
      }
    }
     
## Thread dump and head dump

    # in json format
    http://localhost:8080/actuator/threaddump
    
    # this will download a hprof file
    http://localhost:8080/actuator/heapdump
    

## Trace http requests
Add:

        @Bean
    	HttpTraceRepository traceRepository() {
    	    // restart server will throw away all traces in memory
    		return new InMemoryHttpTraceRepository();
    	}

    http://localhost:8080/actuator/httptrace

The trace is on single service instance only, to use microservice distributed trace is recommended.
    

    