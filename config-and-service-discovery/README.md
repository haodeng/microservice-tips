# Config
Spring Cloud Config provides server-side and client-side support for externalized configuration in a distributed system. 


## Config Server
With the Config Server, you have a central place to manage external properties for applications across all environments.

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
    </dependency>
        
    @EnableConfigServer
    

We use "File System Backend" check detail:

https://cloud.spring.io/spring-cloud-config/reference/html/#_version_control_backend_filesystem_use

There is also a “native” profile in the Config Server that does not use Git but loads the config files from the local classpath or file system (any static URL you want to point to with spring.cloud.config.server.native.searchLocations). To use the native profile, launch the Config Server with spring.profiles.active=native.

Start config-server and check service-a:

    curl http://localhost:8080/service-a/default
    
    {
      "name": "service-a",
      "profiles": [
        "default"
      ],
      "label": null,
      "version": null,
      "state": null,
      "propertySources": [
        {
          "name": "class path resource [config-repo/service-a.yml]",
          "source": {
            "property1": "property1 customized for service-a from config server",
            "property2": "property2 customized for service-a from config server"
          }
        },
        {
          "name": "class path resource [config-repo/application.yml]",
          "source": {
            "property1": "Generic property1 from config server",
            "property2": "Generic property2 from config server"
          }
        }
      ]
    }

check service-b:

    {
      "name": "service-b",
      "profiles": [
        "default"
      ],
      "label": null,
      "version": null,
      "state": null,
      "propertySources": [
        {
          "name": "class path resource [config-repo/application.yml]",
          "source": {
            "property1": "Generic property1 from config server",
            "property2": "Generic property2 from config server"
          }
        }
      ]
    }
    
## Config Client 
In application.yml of service-a and service-b

    spring:
      application:
        name: service-a
      config:
        import: optional:configserver:http://localhost:8080

Start service-a on random port, check: 

curl -s http://localhost:60269/service-a/property1

    property1 customized for service-a
The value should be taken from config server config-repo/service-a.yml

Start service-b on random port, check: 

curl -s http://localhost:62149//service-b/property1

    Generic property1 from config
The value should be taken from config server config-repo/application.yml

## vault as secret config server
Vault secure, store and tightly control access to tokens, passwords, certificates, encryption keys for protecting secrets and other sensitive data using a UI, CLI, or HTTP API.

    docker run -d --name vault --cap-add=IPC_LOCK -e 'VAULT_DEV_ROOT_TOKEN_ID=hao' -p 8200:8200 vault

Login: http://localhost:8200/, TOKEN, hao

Enable secret version 1, which allows static key/val, add path service-b, key/val: secret1/val1

Add vault config to config-server application.yml:

    spring:
      application:
        name: config-server
      profiles:
        active: native, vault #Enable for Vault backend
      cloud:
        config:
          server:
            native:
              searchLocations: classpath:/config-repo
            vault:
              host: 127.0.0.1
              authentication: TOKEN
              token: hao # should pass via jvm option later

Restart config-server, service-b

Check: curl -s http://localhost:59293/service-b/secret1
should return: val1


## Discovery Server
### Via Eureka
In service-a and service-b, enable eureka-client

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    
    add to application.yml:
    eureka:
      instance:
        instanceId: ${spring.cloud.client.hostname}:${spring.application.name}:${spring.application.instance_id:${random.value}}}

In discovery-server

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
    
    @EnableEurekaServer
    
* Run discovery-service
* Allow paralle run of service-a and service-b
* run service-a and service-b multiple instances
* check localhost:8761

### Via Consul

    docker run -d --name consul -p 8500:8500 consul
    
    docker logs -f consul

Check Consul UI: http://localhost:8500/

In service-c, add:

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-consul-discovery</artifactId>
    </dependency>

Consul already has config server feature, to activate config:

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-consul-config</artifactId>
    </dependency>
    
    add key-value config/service-c/data:
    property1: consul pop1
    property2: consul pop2
           
Restart service-c, check consul config works:  

    curl -s http://localhost:52559/service-c/property1
should return "consul pop1"

