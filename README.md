Docker pas encore operationnelle
Lancer juste le main en débug
curl -X GET -H 'Content-Type: application/json' -i 'http://127.0.0.1:8080/api/tweetflock/us'
# README #

This is an example exercising the use of Spring Boot and Spring Social Twitter project.
It provides an API endpoint that accepts a country code (limited to UK and US for simplicity) and returns the latest twitter trends for that location, along with the top 10 tweets for that twitter trend.

### Requirements ###

This application was built and tested using:

* Java 1.8.0_181
* Maven 3.5.3
* Docker 18.06.1-ce

### TL;DR ###
Configuration available at:

```
/src/main/resources/application.properties
```

To build and start application

```
./tweetflock.sh start
```

The application will be available at URL:

```
http://localhost:8080/api/tweetflock/<Country Code>
```

e.g.
```
curl -X GET -H 'Content-Type: application/json' -i 'http://127.0.0.1:8080/api/tweetflock/us'
```

To stop application

```
./tweetflock.sh stop
```

### Configuration ###

There is available a configuration file where you can setup debug level, cache settings and twitter account secrets.

```
/src/main/resources/application.properties
```

All settings are optional despite the following twitter secrets settings:

```
spring.social.twitter.appId={APPID}
spring.social.twitter.appSecret={APP_SECRET}
twitter.access.token={TOKEN}
twitter.access.token.secret={TOKEN_SECRET}
```

For optimisation purposes, was added a cache for the api requests, by default is configured to retain cache for 60 minutes
More details about how was adopted this solution in next sessions.

```
spring.cache.caffeine.spec=expireAfterWrite=60m
```

### How to build? ###

If you want to build and execute manually instead use provided script you should follow these steps

As a maven project, should be executed:

```
#!shell
mvn clean package
```
It will run tests, generate your snapshot version jar and create a new docker image, you can check created image as following

```
#!shell
$ docker images
REPOSITORY                        TAG                 IMAGE ID            CREATED             SIZE
com.volmar/tweetflock        latest              b59a9ec0130c        18 minutes ago      121MB
```

### Running ###

The script runs the container with the application as a daemon, if you want run attached to terminal, check debug logs you can run manually

```
docker run --name=api -p 8080:8080 com.volmar/tweetflock
```

You should get application available at:

```
http://localhost:8080/api/tweetflock/<Country Code>
```
e.g.
```
curl -X GET -H 'Content-Type: application/json' -i 'http://127.0.0.1:8080/api/tweetflock/us'
```

The only supported contry codes are `US` and `UK`, any other else will return `HTTP 400`.
