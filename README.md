Alchemy Arguments
==============================================

[<img src="https://raw.githubusercontent.com/SirWellington/alchemy/develop/Graphics/Logo/Alchemy-Logo-v3-name.png" width="200">](https://github.com/SirWellington/alchemy)

## "Check Yoself!"

[![Build Status](https://travis-ci.org/SirWellington/alchemy-arguments.svg)](https://travis-ci.org/SirWellington/alchemy-arguments)

- [Purpose](#purpose)
- [API](#api)
  - [Multiple Arguments](#multiple-arguments)
  - [Error Message](#error-message)
  - [Custom Exceptions](#custom-exceptions)
  - [Custom Assertions](#custom-assertions)
- [Download](#download)
  - [Release](#release)
  - [Snapshot](#snapshot)
- [[Javadocs](http://www.javadoc.io/doc/tech.sirwellington.alchemy/alchemy-arguments/)](#javadocshttpwwwjavadociodoctechsirwellingtonalchemyalchemy-arguments)
- [Requirements](#requirements)
- [Building](#building)
- [Release Notes](#release-notes)
  - [1.3](#13)
  - [1.2](#12)
  - [1.1](#11)
  - [1.0.0](#100)
- [Planned Features](#planned-features)
  - [Assertions on multiple arguments simultaneously](#assertions-on-multiple-arguments-simultaneously)
- [License](#license)

# Purpose
Part of the [Alchemy Collection](https://github.com/SirWellington/alchemy), **Alchemy Arguments** allows developers to perform fluid argument checking and validation.


# API

The API of this library aims to use fluid-style language to argument checking and validation. It's like Hamcrest's Matchers,
but with the additional ability to apply multiple conditions on a single argument.


Instead of

``` java
if (zipCode < 0)
{
	throw new IllegalArgumentException("Zip Code must be positive");
}

if (zipCode > 99999)
{
	throw new IllegalArgumentException("Zip Code cannot exceed 99999");
}

```
You can now just do:

``` java
checkThat(zipCode)
	.is(positiveInteger())
	.is(lessThanOrEqualTo(99999));
```

## Multiple Arguments

You can also check multiple Arguments at the same time.

```java
checkThat(firstName, middleName, lastName)
	.are(nonEmptyString())
	.are(stringWithLengthAtLeast(1));
```

## Error Message

Each Assertion includes a specific error message in the Exception, but sometimes you want to include a
message more suited to the context.

```java
checkThat(responseCode)
	.usingMessage("Server Response not OK")
	.is(equalTo(200));
```

## Custom Exceptions

Sometimes an `IllegalArgumentException` is not the Exception you want to throw.

For example,
```java
@GET
public Response getCoffee(String nameField)
{
	String nameField;

	checkThat(nameField)
		.usingMessage("missing name")
		.is(nonEmptyString());
}
```
In stock Jersey, this would cause a 500, and make it seem like your Service failed.

You can throw a more specific Exceptions when making assertions.

```java
checkThat(password)
	.throwing(BadPasswordException.class)
	.is(nonEmptyString())
	.is(alphanumericString())
	.is(stringWithLengthBetween(10, 20));

```
In the example above, if the password fails the checks, a `BadPasswordException` will be thrown.

>Compare that to:
``` java
if (Strings.isNullOrEmpty(password) &&
	password.length() < MIN_LENGTH &&
	password.length() > MAX_LENGTH)
{
	throw new BadPasswordException("missing password");
}
```


Alternatively, you can also supply custom Exception throwing behavior.
Let's try with an age check:

```java
checkThat(age)
	.throwing(ex -> new BadPasswordException(ex))
	.is(positiveInteger())
	.is(greaterThanOrEqualTo(18))
	.is(lessThan(150));
```

This also allows you to decide what message to include in the exception, and whether to include or mask the underlying assertion error.

## Custom Assertions

You can create your own library of custom assertions and reuse them. In fact, **we encourage it**. It is common to perform the same argument checks in multiple parts of the Codebase.

Thanks to the new Java 8 lambdas, it is much easier to create inline assertions in your code.

```java
AlchemyAssertion<Car> sedan = car ->
{
	if (!(car instanceof Sedan))
	{
		throw new FailedAssertionException("Expecting a Sedan");
	}
};

checkThat(car)
	.is(sedan);

```

```java
AlchemyAssertion<Vehicle> truck = v ->
{
	if (!(v instanceof Truck))
	{
		throw new FailedAssertionException("Expecting a Truck but got " + v);
	}
};

//This masks the causing FailedAssertionException message.
checkThat(vehicle)
	.throwing(ex -> new UnauthorizedException("Trucks only"))
	.is(truck);

```

Building on Existing assertions can make for powerful checks.

```java

public static AlchemyAssertion<Person> validPerson()
{
	return person ->
	{
		checkThat(person).is(notNull);

		checkThat(person.firstName, person.lastName)
			.usingMessage("person is missing names")
			.are(nonEmptyString());

		checkThat(person.birthday)
			.is(beforeNow());
	};
}

// Reuse the argument checks
public String findUsername(Person person)
{
	checkThat(person)
		.is(validPerson());

	//Proceed safely
}

```

# Download

To use, simply add the following maven dependency.

## Release
```xml
<dependency>
	<groupId>tech.sirwellington.alchemy</groupId>
	<artifactId>alchemy-arguments</artifactId>
	<version>1.3</version>
</dependency>
```

## Snapshot

>First add the Snapshot Repository
```xml
<repository>
	<id>ossrh</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
</repository>
```

```xml
<dependency>
	<groupId>tech.sirwellington.alchemy</groupId>
	<artifactId>alchemy-arguments</artifactId>
	<version>1.4-SNAPSHOT</version>
</dependency>
```

# [Javadocs](http://www.javadoc.io/doc/tech.sirwellington.alchemy/alchemy-arguments/)

# Requirements

+ Java 8
+ Maven

# Building
This project builds with maven. Just run a `mvn clean install` to compile and install to your local maven repository


# Release Notes

## 1.4
New String Assertions
+ `allUpperCaseString()`
+ `allLowerCaseString()`
+ `stringEndingWith()`


## 1.3
New Assertions
+ `mapWithKey()`
+ `mapWithKeyAndValue()`

New Assertions
Dates and Times:
+ `inThePast()`
+ `inTheFuture()`
+ `before()`
+ `after()`
Other
+ `stringBeginningWith()`
+ `stringContaining()`
+ `stringThatMatches()`
+ `listContaining()`

## 1.2
Multiple Assertions
```java

checkThat(firstName, middleName, lastName, password, description)
.are(nonEmptyString());
```

New Assertions
+ `instanceOf()`


## 1.1
+ Initial Public Release

# License

This Software is licensed under the Apache 2.0 License

http://www.apache.org/licenses/LICENSE-2.0
