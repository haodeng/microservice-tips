# Basic
## Collect and monitor metrics

## API doc

## Centralize logs

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

## Test