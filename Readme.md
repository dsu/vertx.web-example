# vertx - simple API endpoint

Based on vertx.web, create a REST API Endpoint `/api/search?q=KEYWORD` that delegates to the amazon.com website
to get  a product data. 

# Technologies
* GIT
* https://vertx.io/ especially https://vertx.io/docs/#web


# Configuration

DeploymentOptions needs to have a service key : amazon|mock
example:

```javascript
{
	"http.port": 8082,
	"service": "mock"
}
```
# Building a fat jar
```
mvn clean
mvn package
```
As a result you will have vertx-amazon-0.0.1-SNAPSHOT-fat.jar in a target directory

# Running a fat jar
```
java -jar target/vertx-amazon-0.0.1-SNAPSHOT-fat.jar -conf conf/dev-conf.json
```
