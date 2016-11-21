This repository contains sample code on implementing a simple blog microservice
using [Spring 5][spring], [Reactor][reactor], and [Cassandra][cassandra].
As of writing, this uses Spring Boot 2.0.0-SNAPSHOT as Spring 5 and Spring Boot 2
have not been released yet.

The code is explained on [my blog post about Reactor][blog].

To run the project, execute:

    mvn spring-boot:run

You can also run the standard

    mvn package

and then execute the created jar file as per usual with a Spring Boot app.

[spring]: http://projects.spring.io/spring-framework/
[reactor]: https://projectreactor.io/
[cassandra]: https://cassandra.apache.org/
[blog]: http://musigma.org/java/2016/11/21/reactor.html
