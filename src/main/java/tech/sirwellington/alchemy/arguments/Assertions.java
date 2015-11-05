/*
 * Copyright 2015 SirWellington Tech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tech.sirwellington.alchemy.arguments;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.annotations.arguments.NonNull;
import tech.sirwellington.alchemy.annotations.arguments.Nullable;
import tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern;

import static java.lang.String.format;
import static tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern.Role.CONCRETE_BEHAVIOR;

/**
 * Common {@linkplain AlchemyAssertion Alchemy Assertions}.
 *
 * @author SirWellington
 */
@StrategyPattern(role = CONCRETE_BEHAVIOR)
@NonInstantiable
public final class Assertions
{

    private final static Logger LOG = LoggerFactory.getLogger(Assertions.class);

    Assertions() throws IllegalAccessException
    {
        throw new IllegalAccessException("cannot instantiate class");
    }

    /**
     * Asserts that the argument is not null.
     *
     * @param <A>
     *
     * @return
     */
    public static <A> AlchemyAssertion<A> notNull()
    {
        return (reference) ->
        {
            if (reference == null)
            {
                throw new FailedAssertionException("Argument is null");
            }
        };
    }

    /**
     * Asserts that the argument is the same instance as {@code other}.
     *
     * @param <A>
     * @param other
     *
     * @return
     */
    public static <A> AlchemyAssertion<A> sameInstance(@Nullable Object other)
    {
        return (argument) ->
        {
            if (argument == null && other == null)
            {
                return;
            }

            if (argument != other)
            {
                throw new FailedAssertionException("Expected " + argument + " to be the same instance as " + other);
            }
        };
    }

    /**
     * Asserts that an argument is an {@code instanceOf} the specified class. This Assertion
     * respects the inheritance hierarchy, so
     *
     * <pre>
     *
     * Integer instanceOf Object
     * Integer instanceOf Number
     * Integer instanceOf Integer
     * </pre>
     *
     * will pass, but
     *
     * <pre>
     *
     * Integer instanceOf Double
     * Integer instanceOf String
     * </pre>
     *
     * will fail.
     *
     * @param <A>
     * @param classOfExpectedType
     * @return
     */
    public static <A> AlchemyAssertion<A> instanceOf(Class<?> classOfExpectedType)
    {
        Checks.checkNotNull(classOfExpectedType, "class cannot be null");

        return (argument) ->
        {
            notNull().check(argument);

            if (!classOfExpectedType.isInstance(argument))
            {
                throw new FailedAssertionException("Expected Object of type: " + classOfExpectedType);
            }
        };
    }

    /**
     * Asserts that the argument is
     * {@linkplain Object#equals(java.lang.Object) equal to} {@code other}.
     *
     * @param <A>
     * @param other
     *
     * @return
     */
    public static <A> AlchemyAssertion<A> equalTo(@Nullable A other)
    {
        return (argument) ->
        {
            if (argument == null && other == null)
            {
                return;
            }

            if (argument == null)
            {
                throw new FailedAssertionException("null is not equal to " + other);
            }

            if (!argument.equals(other))
            {
                throw new FailedAssertionException("Expected " + argument + " to be equal to " + other);
            }
        };
    }

    /**
     * Runs the inverse on another {@link AlchemyAssertion}. This allows you to create expressions
     * such as:
     *
     * <pre>
     * {@code
     * checkThat(filename)
     *      .is(not( stringWithWhitespace() ))
     *      .is(not( equalTo("info.txt") ));
     * }
     * </pre>
     *
     * @param <A>
     * @param assertion
     *
     * @return
     */
    public static <A> AlchemyAssertion<A> not(@NonNull AlchemyAssertion<A> assertion)
    {
        Checks.checkNotNull(assertion, "missing assertion");

        return (argument) ->
        {
            try
            {
                assertion.check(argument);
            }
            catch (FailedAssertionException ex)
            {
                return;
            }

            throw new FailedAssertionException("Expected assertion to fail, but it passed: " + assertion);

        };
    }

    //==========================Number Assertions====================================
    /**
     * Asserts that an integer is positive, or {@code > 0}
     *
     * @return
     */
    public static AlchemyAssertion<Integer> positiveInteger()
    {
        return (number) ->
        {
            notNull().check(number);

            if (number <= 0)
            {
                throw new FailedAssertionException("Expected positive integer: " + number);
            }
        };
    }

    /**
     * Asserts that a long is positive, or {@code > 0}
     *
     * @return
     */
    public static AlchemyAssertion<Long> positiveLong()
    {
        return (number) ->
        {
            notNull().check(number);

            if (number <= 0)
            {
                throw new FailedAssertionException("Expected positive long: " + number);
            }
        };
    }

    /**
     * Asserts that an integer argument is in the specified (inclusive) range.
     *
     * @param min The lower bound for the range, inclusive
     * @param max The upper bound for the range, inclusive
     *
     * @return
     *
     * @throws IllegalArgumentException If {@code min >= max}. {@code min} should always be less
     *                                  than {@code max}.
     */
    public static AlchemyAssertion<Integer> numberBetween(int min, int max) throws IllegalArgumentException
    {
        Checks.checkThat(min < max, "Minimum must be less than Max.");

        return (Integer number) ->
        {
            notNull().check(number);

            if (number < min || number > max)
            {
                String message = format("Expected a number between %d and %d but got %d instead", min, max, number);
                throw new FailedAssertionException(message);
            }
        };
    }

    /**
     * Asserts that a long argument is in the specified (inclusive) range.
     *
     * @param min The lower bound for the range, inclusive
     * @param max The upper bound for the range, inclusive
     *
     * @return
     *
     * @throws IllegalArgumentException If {@code min >= max}. {@code min} should always be less
     */
    public static AlchemyAssertion<Long> numberBetween(long min, long max) throws IllegalArgumentException
    {
        Checks.checkThat(min < max, "Minimum must be less than Max.");

        return (number) ->
        {
            notNull().check(number);

            if (number < min || number > max)
            {
                String message = format("Expected a number between %d and %d but got %d instead", min, max, number);
                throw new FailedAssertionException(message);
            }
        };
    }

    /**
     * Asserts than an integer is {@code >} the supplied value.
     *
     * @param exclusiveLowerBound The argument must be {@code > exclusiveLowerBound}.
     *
     * @return
     */
    public static AlchemyAssertion<Integer> greaterThan(int exclusiveLowerBound)
    {
        Checks.checkThat(exclusiveLowerBound != Integer.MAX_VALUE, "Integers cannot exceed " + Integer.MAX_VALUE);

        return (integer) ->
        {
            notNull().check(integer);
            if (integer <= exclusiveLowerBound)
            {
                throw new FailedAssertionException("Number must be > " + exclusiveLowerBound);
            }
        };
    }

    /**
     * Asserts than a long is {@code > exclusiveLowerBound}.
     *
     * @param exclusiveLowerBound The argument must be {@code >} this value.
     *
     * @return
     */
    public static AlchemyAssertion<Long> greaterThan(long exclusiveLowerBound)
    {
        Checks.checkThat(exclusiveLowerBound != Long.MAX_VALUE, "Longs cannot exceed " + Long.MAX_VALUE);

        return (number) ->
        {
            notNull().check(number);
            if (number <= exclusiveLowerBound)
            {
                throw new FailedAssertionException("Number must be > " + exclusiveLowerBound);
            }
        };
    }

    /**
     * Asserts that an integer is {@code >=} the supplied value.
     *
     * @param inclusiveLowerBound The argument integer must be {@code >= inclusiveLowerBound}
     *
     * @return
     */
    public static AlchemyAssertion<Integer> greaterThanOrEqualTo(int inclusiveLowerBound)
    {
        return (number) ->
        {
            notNull().check(number);

            if (number < inclusiveLowerBound)
            {
                throw new FailedAssertionException("Number must be greater than or equal to " + inclusiveLowerBound);
            }
        };
    }

    /**
     * Asserts that a long is {@code >= inclusiveLowerBound}.
     *
     * @param inclusiveLowerBound The argument integer must be {@code >= inclusiveUpperBound}
     *
     * @return
     */
    public static AlchemyAssertion<Long> greaterThanOrEqualTo(long inclusiveLowerBound)
    {
        return (number) ->
        {
            notNull().check(number);

            if (number < inclusiveLowerBound)
            {
                throw new FailedAssertionException("Number must be greater than or equal to " + inclusiveLowerBound);
            }
        };
    }

    /**
     * Asserts than an integer is {@code <} the supplied value.
     *
     * @param exclusiveUpperBound The argument must be {@code < exclusiveUpperBound}.
     *
     * @return
     */
    public static AlchemyAssertion<Integer> lessThan(int exclusiveUpperBound)
    {
        Checks.checkThat(exclusiveUpperBound != Integer.MIN_VALUE, "Ints cannot be less than " + Integer.MIN_VALUE);

        return (number) ->
        {
            notNull().check(number);

            if (number >= exclusiveUpperBound)
            {
                throw new FailedAssertionException("Number must be < " + exclusiveUpperBound);
            }
        };
    }

    /**
     * Asserts than a long is {@code <} the supplied value.
     *
     * @param exclusiveUpperBound The argument must be {@code < exclusiveUpperBound}.
     *
     * @return
     */
    public static AlchemyAssertion<Long> lessThan(long exclusiveUpperBound)
    {
        Checks.checkThat(exclusiveUpperBound != Long.MIN_VALUE, "Longs cannot be less than " + Long.MIN_VALUE);

        return (number) ->
        {
            notNull().check(number);

            if (number >= exclusiveUpperBound)
            {
                throw new FailedAssertionException("Number must be < " + exclusiveUpperBound);
            }
        };
    }

    /**
     * Asserts that an integer is {@code <=} the supplied value.
     *
     * @param inclusiveUpperBound The argument must be {@code <= inclusiveUpperBound}.
     *
     * @return
     */
    public static AlchemyAssertion<Integer> lessThanOrEqualTo(int inclusiveUpperBound)
    {
        return (number) ->
        {
            notNull().check(number);

            if (number > inclusiveUpperBound)
            {
                throw new FailedAssertionException("Number must be less than or equal to " + inclusiveUpperBound);
            }
        };
    }

    /**
     * Asserts that a long is {@code <=} the supplied value.
     *
     * @param inclusiveUpperBound The argument must be {@code <= inclusiveUpperBound}.
     *
     * @return
     */
    public static AlchemyAssertion<Long> lessThanOrEqualTo(long inclusiveUpperBound)
    {
        return (number) ->
        {
            notNull().check(number);

            if (number > inclusiveUpperBound)
            {
                throw new FailedAssertionException("Number must be less than or equal to " + inclusiveUpperBound);
            }
        };
    }

    //==========================String Assertions====================================
    /**
     * Asserts that a given string is not empty (neither null nor completely empty).
     *
     * @return
     */
    public static AlchemyAssertion<String> nonEmptyString()
    {
        return (string) ->
        {
            if (Checks.isNullOrEmpty(string))
            {
                throw new FailedAssertionException("String argument is empty");
            }
        };
    }

    /**
     * Asserts that a given string is empty (that it has no value).
     *
     * @return
     */
    public static AlchemyAssertion<String> emptyString()
    {
        return (string) ->
        {
            if (!Checks.isNullOrEmpty(string))
            {
                throw new FailedAssertionException("Expected empty string but got: " + string);
            }
        };
    }

    /**
     * Asserts that the argument string has a length of exactly {@code expectedLength}
     *
     * @param expectedLength The expected length of the string.
     *
     * @return
     */
    public static AlchemyAssertion<String> stringWithLength(int expectedLength)
    {
        Checks.checkThat(expectedLength >= 0, "expectedLength must be >= 0");

        return (string) ->
        {
            notNull().check(string);

            if (string.length() != expectedLength)
            {
                throw new FailedAssertionException("Expecting a String with length " + expectedLength);
            }
        };
    }

    /**
     * Asserts that the argument string has a length {@code > minimumLength}
     *
     * @param minimumLength The exclusive lower bound for the size of the argument string.
     *
     * @return
     */
    public static AlchemyAssertion<String> stringWithLengthGreaterThan(int minimumLength)
    {
        Checks.checkThat(minimumLength > 0, "minimumLength must be > 0");
        Checks.checkThat(minimumLength < Integer.MAX_VALUE, "not possible to have a String larger than Integer.MAX_VALUE");

        return (string) ->
        {
            nonEmptyString().check(string);

            if (string.length() <= minimumLength)
            {
                throw new FailedAssertionException("Expected a String with length > " + minimumLength);
            }
        };
    }

    /**
     * Asserts that the argument string has a length {@code >= minimumLength}.
     *
     * @param minimumLength The length of the argument string must {@code >= minimumLength}
     *
     * @return
     */
    public static AlchemyAssertion<String> stringWithLengthGreaterThanOrEqualTo(int minimumLength)
    {
        Checks.checkThat(minimumLength >= 0);
        return (string) ->
        {
            notNull().check(string);

            if (string.length() < minimumLength)
            {
                throw new FailedAssertionException("Expecting a String with length >= " + minimumLength);
            }
        };
    }

    /**
     * Asserts that the length of the argument string is less than the specified upper bound.
     *
     * @param upperBound The length of the string must be {@code <upperBound}
     *
     * @return
     */
    public static AlchemyAssertion<String> stringWithLengthLessThan(int upperBound)
    {
        Checks.checkThat(upperBound > 0, "upperBound must be > 0");

        return (string) ->
        {
            nonEmptyString().check(string);

            if (string.length() >= upperBound)
            {
                throw new FailedAssertionException("Expecting a String with length < " + upperBound);
            }
        };
    }

    /**
     * Asserts that the length of the argument string is at most maximumLength.
     *
     * @param maximumLength The length of the argument must be {@code <= maximumLength}
     *
     * @return
     */
    public static AlchemyAssertion<String> stringWithLengthLessThanOrEqualTo(int maximumLength)
    {
        Checks.checkThat(maximumLength >= 0);
        return (string) ->
        {
            notNull().check(string);

            if (string == null || string.length() > maximumLength)
            {
                throw new FailedAssertionException("Argument exceeds the maximum string length of: " + maximumLength);
            }
        };
    }

    /**
     * Asserts that the argument string's length is between the specified lengths, inclusively.
     *
     * @param minimumLength String length must be greater than or equal to this.
     * @param maximumLength String length must be less than or equal to this.
     *
     * @return
     */
    public static AlchemyAssertion<String> stringWithLengthBetween(int minimumLength, int maximumLength)
    {
        Checks.checkThat(minimumLength >= 0, "Minimum length must be at least 0");
        Checks.checkThat(minimumLength < maximumLength, "Minimum length must be > maximum length.");

        return (string) ->
        {
            notNull().check(string);

            if (string.length() < minimumLength ||
                string.length() > maximumLength)
            {
                String message = String.format("Argument size is not between acceptable range of [%d -> %d]",
                                               minimumLength,
                                               maximumLength);
                throw new FailedAssertionException(message);
            }
        };
    }

    /**
     * Asserts that the argument String does not have any whitespace characters whatsoever.
     *
     * @return
     */
    public static AlchemyAssertion<String> stringWithNoWhitespace()
    {
        return (string) ->
        {
            notNull().check(string);

            for (char character : string.toCharArray())
            {
                if (Character.isWhitespace(character))
                {
                    throw new FailedAssertionException("Argument should not have whitespace.");
                }
            }
        };

    }

    /**
     * Asserts that the argument String matches the specified pattern.
     *
     * @param pattern The pattern to match against.
     *
     * @return
     */
    public static AlchemyAssertion<String> stringThatMatches(Pattern pattern)
    {
        Checks.checkNotNull(pattern, "missing pattern");

        return (string) ->
        {
            if (!pattern.matcher(string).matches())
            {
                throw new FailedAssertionException("Expected String to match pattern: " + pattern);
            }
        };
    }

    /**
     * Assert that the argument String starts with a particular prefix.
     *
     * @param prefix The prefix to check that argument against.
     *
     * @return
     */
    public static AlchemyAssertion<String> stringThatStartsWith(String prefix)
    {
        Checks.checkThat(!Checks.isNullOrEmpty(prefix), "missing prefix");

        return (string) ->
        {
            nonEmptyString().check(string);

            if (!string.startsWith(prefix))
            {
                String message = String.format("Expected \"%s\" to start with \"%s\"", string, prefix);
                throw new FailedAssertionException(message);
            }
        };

    }

    //==========================Collection Assertions====================================
    /**
     * Asserts that the collection is not null and not empty.
     *
     * @param <T>
     *
     * @return
     */
    public static <T> AlchemyAssertion<Collection<T>> nonEmptyCollection()
    {
        return (collection) ->
        {
            notNull().check(collection);

            if (collection.isEmpty())
            {
                throw new FailedAssertionException("Collection is empty");
            }
        };
    }

    /**
     * Asserts that the List is not null and not empty
     *
     * @param <T>
     *
     * @return
     */
    public static <T> AlchemyAssertion<List<T>> nonEmptyList()
    {
        return (list) ->
        {
            notNull().check(list);

            if (list.isEmpty())
            {
                throw new FailedAssertionException("List is empty");
            }
        };

    }

    /**
     * Asserts that the Map is not null and not empty
     *
     * @param <K>
     * @param <V>
     *
     * @return
     */
    public static <K, V> AlchemyAssertion<Map<K, V>> nonEmptyMap()
    {
        return (map) ->
        {
            notNull().check(map);

            if (map.isEmpty())
            {
                throw new FailedAssertionException("Map is empty");
            }

        };
    }

    public static <T> AlchemyAssertion<T[]> nonEmptyArray()
    {
        return (array) ->
        {
            notNull().check(array);

            if (array.length == 0)
            {
                throw new FailedAssertionException("Array is empty");
            }
        };
    }

}
