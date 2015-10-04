/*
 * Copyright 2015   SirWellington.
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
package sir.wellington.alchemy.arguments;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import static java.lang.String.format;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sir.wellington.alchemy.annotations.patterns.StrategyPattern;
import static sir.wellington.alchemy.annotations.patterns.StrategyPattern.Role.CONCRETE_BEHAVIOR;

/**
 * A class containing a library of common argument assertions.
 *
 * @author SirWellington
 */
@StrategyPattern(role = CONCRETE_BEHAVIOR)
public final class Assertions
{

    private final static Logger LOG = LoggerFactory.getLogger(Assertions.class);

    private Assertions()
    {
    }

    /**
     * Asserts that the argument is not null.
     *
     * @param <A>
     *
     * @return
     */
    public static <A> Assertion<A> notNull()
    {
        return (reference) ->
        {
            if (reference == null)
            {
                throw new FailedAssertionException("Argument is null");
            }
        };
    }

    //==========================Number Assertions====================================
    /**
     * Asserts that an integer is positive, or {@code > 0}
     *
     * @return
     */
    public static Assertion<Integer> positiveInteger()
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
    public static Assertion<Long> positiveLong()
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
     */
    public static Assertion<Integer> numberBetween(int min, int max)
    {
        Preconditions.checkArgument(min < max, "Minimum must be less than Max.");

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
     */
    public static Assertion<Long> numberBetween(long min, long max)
    {
        Preconditions.checkArgument(min < max, "Minimum must be less than Max.");

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
    public static Assertion<Integer> greaterThan(int exclusiveLowerBound)
    {
        Preconditions.checkArgument(exclusiveLowerBound != Integer.MAX_VALUE, "Integers cannot exceed " + Integer.MAX_VALUE);

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
     * Asserts than a long is {@code >} the supplied value.
     *
     * @param exclusiveLowerBound The argument must be {@code > exclusiveLowerBound}.
     *
     * @return
     */
    public static Assertion<Long> greaterThan(long exclusiveLowerBound)
    {
        Preconditions.checkArgument(exclusiveLowerBound != Long.MAX_VALUE, "Longs cannot exceed " + Long.MAX_VALUE);

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
    public static Assertion<Integer> greaterThanOrEqualTo(int inclusiveLowerBound)
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
     * Asserts that a long is {@code >=} the supplied value.
     *
     * @param inclusiveLowerBound The argument integer must be {@code >= inclusiveUpperBound}
     *
     * @return
     */
    public static Assertion<Long> greaterThanOrEqualTo(long inclusiveLowerBound)
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
    public static Assertion<Integer> lessThan(int exclusiveUpperBound)
    {
        Preconditions.checkArgument(exclusiveUpperBound != Integer.MIN_VALUE, "Ints cannot be less than " + Integer.MIN_VALUE);

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
    public static Assertion<Long> lessThan(long exclusiveUpperBound)
    {
        Preconditions.checkArgument(exclusiveUpperBound != Long.MIN_VALUE, "Longs cannot be less than " + Long.MIN_VALUE);

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
    public static Assertion<Integer> lessThanOrEqualTo(int inclusiveUpperBound)
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
    public static Assertion<Long> lessThanOrEqualTo(long inclusiveUpperBound)
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
    public static Assertion<String> nonEmptyString()
    {
        return (string) ->
        {
            if (Strings.isNullOrEmpty(string))
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
    public static Assertion<String> emptyString()
    {
        return (string) ->
        {
            if (!Strings.isNullOrEmpty(string))
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
    public static Assertion<String> stringWithLength(int expectedLength)
    {
        Preconditions.checkArgument(expectedLength >= 0, "expectedLength must be >= 0");

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
    public static Assertion<String> stringWithLengthGreaterThan(int minimumLength)
    {
        Preconditions.checkArgument(minimumLength > 0, "minimumLength must be > 0");

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
    public static Assertion<String> stringWithLengthGreaterThanOrEqualTo(int minimumLength)
    {
        Preconditions.checkArgument(minimumLength >= 0);
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
    public static Assertion<String> stringWithLengthLessThan(int upperBound)
    {
        Preconditions.checkArgument(upperBound > 0, "upperBound must be > 0");

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
    public static Assertion<String> stringWithLengthLessThanOrEqualTo(int maximumLength)
    {
        Preconditions.checkArgument(maximumLength >= 0);
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
    public static Assertion<String> stringWithLengthBetween(int minimumLength, int maximumLength)
    {
        Preconditions.checkArgument(minimumLength >= 0, "Minimum length must be at least 0");
        Preconditions.checkArgument(minimumLength < maximumLength, "Minimum length must be > maximum length.");

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
    public static Assertion<String> stringWithNoWhitespace()
    {
        return (string) ->
        {
            notNull().check(string);

            if (CharMatcher.WHITESPACE.matchesAnyOf(string))
            {
                throw new FailedAssertionException("Argument should not have whitespace.");
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
    public static Assertion<String> stringThatMatches(Pattern pattern)
    {
        Preconditions.checkNotNull(pattern, "missing pattern");

        return (string) ->
        {
            if (!pattern.matcher(string).matches())
            {
                throw new FailedAssertionException("Expected String to match pattern: " + pattern);
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
    public static <T> Assertion<Collection<T>> nonEmptyCollection()
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
    public static <T> Assertion<List<T>> nonEmptyList()
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
    public static <K, V> Assertion<Map<K, V>> nonEmptyMap()
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
    
    public static <T> Assertion<T[]> nonEmptyArray()
    {
        return (array) ->
        {
            notNull().check(array);
            
            if(array.length == 0)
            {
                throw new FailedAssertionException("Array is empty");
            }
        };
    }

}
