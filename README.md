Alchemy Arguments
==============================================

[<img src="https://raw.githubusercontent.com/SirWellington/alchemy/develop/Graphics/Logo/Alchemy-Logo-v3-name.png" width="200">](https://github.com/SirWellington/alchemy)

## "Check Yoself!"

[![Build Status](https://travis-ci.org/SirWellington/alchemy-arguments.svg)](https://travis-ci.org/SirWellington/alchemy-arguments)


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

## Custom Exceptions

Sometimes an `IllegalArgumentException` is not the Exception you want to thrown from your argument checks.

For example,
```java
{
	String nameField; //Part of the Request Object
	//Throws IllegalArgumentException
	Preconditions.checkArgument(!Strings.isNullOrEmpty(nameField));
}
```
In stock Jersey, this would cause a 500, and make it seem like your Service failed.

This library allows you to throw your own custom exceptions when making assertions.

```java
checkThat(password)
	.throwing(BadPasswordException.class)
	.is(nonEmptyString())
	.is(alphanumericString())
	.is(stringWithLengthBetween(10, 20));

```

In the example above, if the password fails the checks, a `BadPasswordException` will be thrown. It is much cleaner writing:

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

This also allows you to decide what message to include in the exception, and whether to include or mask the causing exception.

## Custom Assertions

You can create your own library of custom assertions and reuse them.

Thanks to the new Java 8 lambdas, it is much easier to create inline assertions in your code.

```java
Assertion<Car> sedan = car ->
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
Assertion<Vehicle> truck = v ->
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


# Download

To use, simply add the following maven dependency.

## Release
```xml
<dependency>
	<groupId>tech.sirwellington.alchemy</groupId>
	<artifactId>alchemy-arguments</artifactId>
	<version>1.2</version>
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
	<version>1.3-SNAPSHOT</version>
</dependency>
```

# [Javadocs](http://www.javadoc.io/doc/tech.sirwellington.alchemy/alchemy-arguments/)

# Requirements

+ Java 8
+ Maven

# Building
This project builds with maven. Just run a `mvn clean install` to compile and install to your local maven repository


# Release Notes

## 1.3
New Assertions
+ `mapWithKey()
+ `mapWithKeyAndValue()`


## 1.2
Multiple Assertions
```java

checkThat(firstName, middleName, lastName, password, description)
.are(nonEmptyString());
```

New Assertions
+ `instanceOf()`
+ `stringThatStartsWith()`

## 1.1
+ Initial Public Release


## 1.0.0
+ Initial Release

# Planned Features

## Assertions on multiple arguments simultaneously
For example

```java

checkThat(firstName, middleName, lastName)
.are(nonEmptyString());

```

# License

This Software is licensed under the Apache 2.0 License

http://www.apache.org/licenses/LICENSE-2.0
