# Spring Boot Ignite Demo App #

<pre><b>Latest Version: 0.0.5</b></pre>

This is a Spring Boot application used to demonstrate some of the features of the Apache Ignite platform.

It's bloated and ugly, but it works. I encourage you to mess around with it yourself!

The main interface for the application is the REST API.

## REST API ##

Swagger documentation available at http://{server}:{port}/swagger-ui.html#/ (e.x.http://localhost:8080/swagger-ui.html#/)

### Current Endpoints ###

#### Create Credit Card ####

**POST** /ignite/creditcard

Payload:
```json
{
  "number": "3337723895589542",
  "type": "visa",
  "expireMonth": 1,
  "expireYear": 2020,
  "cvv2": 539,
  "firstName": "Malcolm",
  "lastName": "Koch",
  "billingAddress": {
    "line1": "1 Banana Tree Lane",
    "line2": null,
    "city": "Island",
    "state": "KY",
    "postalCode": "12345",
    "countryCode": "US",
    "phone": "+41446681800",
    "status": "ACTIVE"
  },
  "externalCustomerId": "5469854215",
  "fundingState": "AUTHORIZED",
  "validUntil": "2015-10-28T04:52:14.080Z",
  "payerId": "8454785236"
}
```

#### Find Credit Card (By Number) ####

**GET** /ignite/creditcard?number=3337723895589542

#### Find Credit Card (Expiring Before or After Month and Year) ####

**GET** /ignite/creditcard?preposition=before&month=11&year=2017

#### List All Credit Cards ####

**GET** /ignite/creditcard/list

#### Calculate Average of a Large Set of Numbers ####

**POST** /ignite/creditcard/compute/average

Payload:
```json
[
  [1, 2, 3, 4, 5],
  [6, 7, 8, 9, 10],
  [11, 12, 13, 14, 15],
  [16, 17, 18, 19, 20],
  [21, 22, 23, 24, 25],
  [26, 27, 28, 29, 30],
  [31, 32, 33, 34, 35],
  [36, 37, 38, 39, 40],
  [41, 42, 43, 44, 45],
  [46, 47, 48, 49, 50],
  [51, 52, 53, 54, 55],
  [56, 57, 58, 59, 60],
  [61, 62, 63, 64, 65],
  [66, 67, 68, 69, 70],
  [71, 72, 73, 74, 75],
  [76, 77, 78, 79, 80],
  [81, 82, 83, 84, 85],
  [86, 87, 88, 89, 90],
  [91, 92, 93, 94, 95],
  [96, 97, 98, 99, 100]
]
```

## How do I get set up? ##

You'll need to modify the location of the Ignite configuration file depending on your environment.

The configuration file is currently specified in the class ```com.sageburner.api.creditcard.dao.jdbc.CacheJdbcStore```

**Example:**
```Ignite ignite = Ignition.start("/usr/app/ignite/example-ignite.xml");```

### Run locally ###

```terminal
$ ./gradew clean bootRun
```

### Create and/or Publish Docker Image ###

```terminal
$ ./gradlew clean buildDocker -P publish=[true|false]
```

## License

The module is licensed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0) license