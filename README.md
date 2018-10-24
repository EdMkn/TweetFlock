# README #

This is an exercise implementation for an Assessment.
This provide a API endpoint that accepts a country code (limited to UK and US for simplicity) and returns the latest twitter trends for that location, along with the top 10 tweets for that twitter trend.

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
docker run --name=api -v ~/api_csv:/data -p 8080:8080 com.volmar/tweetflock
```

`-v` attribute is defining that will be created a folder called `api_csv` at your home folder, this where application will create the csv files, feel free to change, however `/data` can't be changed, application is pointing directly to this path.

If you are not familiar with docker, just copy, paste and trust me :)

You should get application available at:

```
http://localhost:8080/api/tweetflock/<Country Code>
```
e.g.
```
curl -X GET -H 'Content-Type: application/json' -i 'http://127.0.0.1:8080/api/tweetflock/us'
```

The only supported contry codes are `US` and `UK`, any other else will return `HTTP 400`.

### Solution Decisions ###

* The instructions is not clear about .csv files, when should be created, where, name pattern, possible performance issues. Was decided create it with `<search tag>_<timestamp>.csv` at request.

* To avoid performance issues with multiple requests writing new files was added a cache by default configured for 60 minutes, this will retrieve updated values every 60 minutes, therefore, write new csv files with updated timestamp.

* Was not defined a policy to manage csv files, this will indefinately create new files, in a real scenario should be managed by a cron or a job in the application itself.

* Was not defined a contract for json response, only for csv file, I decided to return all fields in rest api, in a real scenario this should be discussed with business to check actual requirements in order to save resources.

* Was prioritised return the results to the client even if faced any issue with csv file, this is another business logic that should be discussed.

### What is missing and improvements ###

* Add swagger for rest documentation

* More tests exploring thread concurrency, its working as a POC but the csv file writing should be more explored based on business logic.

* Explore more test cases, few were added covering the main scenarios.
