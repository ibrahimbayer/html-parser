# Html Parser

A HTMl parser for page analysis, collects information about input webpage.


## Requirement

Java 8 to execute application.

## Configuration

System configuration is important for better results.
For more configuration details please check https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html

Below parameters are managed by system admins.
org.ibayer.html.parser.threadcount=(numeric thread count to target link validation urls) default : 20
org.ibayer.html.parser.connection.request-timeout=(milliseconds for request timeout) default : 5000
org.ibayer.html.parser.connection.connect-timeout=(milliseconds for connection timeout) default : 1000
org.ibayer.html.parser.connection.read-timeout=(milliseconds for read timeout) default : 3000

## Running Application

mvn install "please refer to Maven documentation for setting up mvn on your computer" http://maven.apache.org/install.html

mvn spring-boot:run Starts the application

## Accessing UI

UI is accessible for two different channels first is a simple UI to interact rest service. http://localhost:8080/index.html

The other UI is to directly call Rest services through Swagger Editor. http://localhost:8080/swagger-ui.html

### Available Api Methods

Get Web Page -> http://localhost:8080/webpages/{url} !!! {url} parameter has to be encoded by base64.

You can use https://www.base64encode.org/ to encode your web links.

##Example requests

request : `curl http://localhost:8080/webpages/`