# Helidon Quickstart MP Example

This example implements a simple Hello World REST service using MicroProfile.

## Build and run

With JDK8+
```bash
mvn package
java -jar target/iwater.jar
```

## Exercise the application

```
curl -X GET http://localhost:8080/greet
{"message":"Hello World!"}

curl -X GET http://localhost:8080/greet/Joe
{"message":"Hello Joe!"}

curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Hola"}' http://localhost:8080/greet/greeting

curl -X GET http://localhost:8080/greet/Jose
{"message":"Hola Jose!"}
```

## Try health and metrics

```
curl -s -X GET http://localhost:8080/health
{"outcome":"UP",...
. . .

# Prometheus Format
curl -s -X GET http://localhost:8080/metrics
# TYPE base:gc_g1_young_generation_count gauge
. . .

# JSON Format
curl -H 'Accept: application/json' -X GET http://localhost:8080/metrics
{"base":...
. . .

```

## Build the Docker Image

```
docker build -t iwater .
```

## Start the application with Docker

```
docker run --rm -p 8080:8080 iwater:latest
```

Exercise the application as described above

## Deploy the application to Kubernetes

```
kubectl cluster-info                         # Verify which cluster
kubectl get pods                             # Verify connectivity to cluster
kubectl create -f app.yaml               # Deploy application
kubectl get service iwater  # Verify deployed service
```

## Important

````
# Issue#1 
Problem: Eclipselink L1 Caching issue. Bug reported to helidon (https://github.com/oracle/helidon/issues/1174)
Solution: Temporary work around for L1 Caching issue is to use @ClearEntityManagerL1Cache interceptor which clears L1 cache before performing any operations
# Issue#2
Problem: JPA - NullPointerException when accessing SingularAttribute parameter before entityManager is accessed or transaction created
Solution: Initiate dummy db access before accessing metadata like 
repository.findOne(1);
sortBy = "+" + TestData_.name.getName();
(or) access any one method which first accesses database and then no issues
(or) we need to run simple db query on startup
# Issue#3
Problem: commiting transaction even though Exception is thrown due to HikariCP default autoCommit:true property.
Solutions: https://github.com/oracle/helidon/issues/1176


  
