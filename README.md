Silver Bar Market
=================

Simple implementation of a **Silver Bar Live Order Board**.

Given this functionality is supposed to be used by the UI team easily, I have implemented it as a microservice using **Spring Boot**, exposing REST API operations that can be directly used by the UI team.

The microservice also starts **Swagger** for API documentation and provides the Swagger UI tool allowing the API clients to visualise and interact with the API resources. 

Live Preview on AWS
-------------------

The application is deployed to an AWS Elastic Beanstalk environment.

Link to Order Board REST API reference:  

> [http://silver-bar-market-api-preview.fbxtm5bete.eu-west-2.elasticbeanstalk.com/](http://silver-bar-market-api-preview.fbxtm5bete.eu-west-2.elasticbeanstalk.com/swagger-ui.html#/order-board-controller)

Application Components
----------------------

Main application components overview:  

![Application Components](https://s3.eu-west-2.amazonaws.com/public-lukas/silver-bar-market-components.png)

Build
-----

This is a standard Maven project and can be build (incl. running the unit and acceptance tests):

``` 
mvn clean verify
```

Run Locally
-----------

The Spring Boot application starting an embedded container and exposing the REST API can be started locally using:

```
mvn spring-boot:run
```

By default the application is running on port 8080:

```
http://localhost:8080
```

When the application is running, the Swagger UI can be used to view and explore the Order Board REST API operations:

```
http://localhost:8080/swagger-ui.html#/order-board-controller
```

If you need to configure a different port or context path, feel free to change them in `application.yml` config file.
 
Features
--------

**Register a new order**

> As a silver buyer/seller,  
> I need to register a buy/sell order using the user id, order quantity (kg) and price (per kg).

**Cancel an order**

> As a silver buyer/seller,  
> I need to cancel a registered order.

**Order Summary**

> As a trader,  
> I need to get a summary information of live orders where same price items should be merged together,  
> So that I get a transparent overview of the silver bar demand.

Test Coverage
-------------

The project is covered by a set of **Unit Tests** (testing the individual components) and one main **Acceptance Test**
implemented as a Spring Integration Test on REST API level covering the acceptance criteria corresponding
to the above application features.

Implementation Notes
--------------------

* No real persistence implemented. Just an in-memory implementation provided.
* No real authentication implemented. The `DefaultOrderBoardService` uses a dummy userId.
