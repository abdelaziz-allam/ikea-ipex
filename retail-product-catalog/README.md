# IKEA Retail Product Catalog Back End Service

**IKEA IPEX Product Catalog Service**  is a backend service that is used to handle search products / add new product / fetch product details


## PRODUCT API
     - POST /products - Add a new product.
     - GET /products - Retrieve all products.
     - GET /search - Search for products by name.
     - GET /products/:id - Get details for a specific product.



# Technology Stack
***IKEA Product Catalog Service*** has been build with below java APIs
* Java 21
* Spring boot starters
* H2 As Production Database And As Testing Data base
* Aspjectj along with logback for logging
* jUnit - Mockito - Spring Testing as testing frameworks
* Jacco For Code Coverage
* Swagger For Rest API Documentation
* IntelliJ

# Project Structure



#### Code Level
* **src/main/java** holding all the classes used for handling the required business
* **src/main/resources** resource files that will be used by the main source code
* **src/test/java** holding all Integration and unit test classes
* **src/test/resources** resource files that will be used by testing classes


# How to run retail-product-catalog as Docker

* execute the blow command

```sh
    docker run -p 8090:8090  abdelazizallam/ikea-ipex-catalog-backend
```


# How to run product-catalog-service Standalone


#### Build Project
 
- Make sure that you are on the retail-product-catalog dictory then start executing below commands
```sh
$ mvn clean install
$ java -jar target/retail-product-catalog-0.0.1-SNAPSHOT.jar
```

# How to test champion-service APIS
- After successfully start
- Open browser click [swagger](http://localhost:8090/swagger-ui/)
- How swagger should look like  ![docs.png](images%2Fdocs.png)

### Below Samples response for the API which is rapped inside API Response as all endpoints have one API Response with different results


#Product-Controller

-  http://localhost:8090/api/v1/products?offset=0&pageSize=10 GET Request 
-  http://localhost:8090/api/v1/products/search?term=prod Get Request 
- http://localhost:8090/api/v1/products/6fe620b7-7649-45a6-ac6e-409f42c802ca Get Request

----------------------------------------------------------

## Enhancement Needed to be done
- Cover more test cases to cover all scenarios
- Handle Exceptions Using advisors 
- Build Docker Image for the backend