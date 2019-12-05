# Helidon Quickstart MP Example

This example implements a simple Hello World REST service using MicroProfile.

## Build and run

With JDK8+
With PostgreSQL 9.3

```bash
mvn clean install
java -jar testProject.jar
```

## Exercise the application

```
create data:

http://localhost:8080/rest/testData/create?name=test111
http://localhost:8080/rest/testData/create?name=test222
http://localhost:8080/rest/testData/create?name=test333

view created data by id:
http://localhost:8080/rest/testData/getById/1
{"createdBy":"admin","createdTime":"2019-12-05T15:04:15.702Z[UTC]","description":"","id":1,"lastModifiedTime":"2019-12-05T15:04:15.702Z[UTC]","name":"test111"}


test (description field is saved even though BadRequestException is thrown)
http://localhost:8080/rest/testData/test?id=1
{"createdBy":"admin","createdTime":"2019-12-05T15:04:15.702Z[UTC]","description":"data should not save as the exception is thrown, it seems to be bug!!!","id":1,"lastModifiedTime":"2019-12-05T15:04:15.702Z[UTC]","name":"test1"}

```
