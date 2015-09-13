/*
 * Copyright 2015 Wellington.
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
package sir.wellington.commons.arguments;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import static java.lang.String.format;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class containing a library of common argument assertions.
 *
 * @author SirWellington
 */
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
     * Asserts that an integer argument is in the specified (inclusive) range.
     *
     * @param min The argument must be {@code >= min && <= max}
     * @param max
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
                String message = format("argument %d must be between %d and %d", number, min, max);
                throw new FailedAssertionException(message);
            }
        };
    }

    /**
     * Asserts that a long argument is in the specified (inclusive) range.
     *
     * @param min
     * @param max
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
                String message = format("argument %d must be between %d and %d", number, min, max);
                throw new FailedAssertionException(message);
            }
        };
    }

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
                throw new FailedAssertionException("string argument is empty");
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
    public static Assertion<String> stringIsOExactfLength(int expectedLength)
    {
        Preconditions.checkArgument(expectedLength >= 0);
        return (string) ->
        {
            notNull().check(string);
            
            if (string.length() != expectedLength)
            {
                throw new FailedAssertionException("Argument is not of length " + expectedLength);
            }
        };
    }

    /**
     * Asserts that the argument string has a length of at least {@code minimumLength}.
     *
     * @param minimumLength The length of the argument string must {@code >= minimumLength}
     *
     * @return
     */
    public static Assertion<String> stringIsAtLeastOfLength(int minimumLength)
    {
        Preconditions.checkArgument(minimumLength >= 0);
        return (string) ->
        {
            notNull().check(string);
            
            if (string == null || string.length() < minimumLength)
            {
                throw new FailedAssertionException("Argument does not have the mininum string length of: " + minimumLength);
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
    public static Assertion<String> stringIsAtMostOfLength(int maximumLength)
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
    public static Assertion<String> stringLengthBetween(int minimumLength, int maximumLength)
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
     * Asserts that an integer is at least as great as the supplied value.
     *
     * @param atLeastThisMuch The argument integer must be {@code >= atLeastThisMuch}
     *
     * @return
     */
    public static Assertion<Integer> intIsAtLeast(int atLeastThisMuch)
    {
        return (string) ->
        {
            notNull().check(string);
            
            if (string < atLeastThisMuch)
            {
                throw new FailedAssertionException("Argument must be at least " + atLeastThisMuch);
            }
        };
    }
    
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
}
