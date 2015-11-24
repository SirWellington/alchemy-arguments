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

 
package tech.sirwellington.alchemy.arguments.assertions;


import java.util.regex.Pattern;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.annotations.arguments.NonEmpty;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.Checks;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;

import static java.lang.String.format;
import static tech.sirwellington.alchemy.arguments.Checks.Internal.isNullOrEmpty;

/**
 *
 * @author SirWellington
 */
@NonInstantiable
public final class StringAssertions
{
    StringAssertions() throws IllegalAccessException
    {
        throw new IllegalAccessException("cannot instantiate");
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
        Checks.Internal.checkNotNull(pattern, "missing pattern");
        return (String string) ->
        {
            if (!pattern.matcher(string).matches())
            {
                throw new FailedAssertionException("Expected String to match pattern: " + pattern);
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
        return (String string) ->
        {
            if (!Checks.Internal.isNullOrEmpty(string))
            {
                throw new FailedAssertionException("Expected empty string but got: " + string);
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
        Checks.Internal.checkThat(minimumLength >= 0);
        return (String string) ->
        {
            Assertions.notNull().check(string);
            if (string.length() < minimumLength)
            {
                throw new FailedAssertionException("Expecting a String with length >= " + minimumLength);
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
        return (String string) ->
        {
            Assertions.notNull().check(string);
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
     * Asserts that the argument string has a length of exactly {@code expectedLength}
     *
     * @param expectedLength The expected length of the string.
     *
     * @return
     */
    public static AlchemyAssertion<String> stringWithLength(int expectedLength)
    {
        Checks.Internal.checkThat(expectedLength >= 0, "expectedLength must be >= 0");
        return (String string) ->
        {
            Assertions.notNull().check(string);
            if (string.length() != expectedLength)
            {
                throw new FailedAssertionException("Expecting a String with length " + expectedLength);
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
        Checks.Internal.checkThat(upperBound > 0, "upperBound must be > 0");
        return (String string) ->
        {
            nonEmptyString().check(string);
            if (string.length() >= upperBound)
            {
                throw new FailedAssertionException("Expecting a String with length < " + upperBound);
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
    public static AlchemyAssertion<String> stringBeginningWith(String prefix)
    {
        Checks.Internal.checkThat(!isNullOrEmpty(prefix), "missing prefix");

        return (String string) ->
        {
            nonEmptyString().check(string);
            if (!string.startsWith(prefix))
            {
                String message = String.format("Expected \"%s\" to start with \"%s\"", string, prefix);
                throw new FailedAssertionException(message);
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
        Checks.Internal.checkThat(maximumLength >= 0);
        return (String string) ->
        {
            Assertions.notNull().check(string);
            if (string == null || string.length() > maximumLength)
            {
                throw new FailedAssertionException("Argument exceeds the maximum string length of: " + maximumLength);
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
        Checks.Internal.checkThat(minimumLength > 0, "minimumLength must be > 0");
        Checks.Internal.checkThat(minimumLength < Integer.MAX_VALUE, "not possible to have a String larger than Integer.MAX_VALUE");
        return (String string) ->
        {
            nonEmptyString().check(string);
            if (string.length() <= minimumLength)
            {
                throw new FailedAssertionException("Expected a String with length > " + minimumLength);
            }
        };
    }

    /**
     * Asserts that a given string is not empty (neither null nor completely empty).
     *
     * @return
     */
    public static AlchemyAssertion<String> nonEmptyString()
    {
        return (String string) ->
        {
            if (Checks.Internal.isNullOrEmpty(string))
            {
                throw new FailedAssertionException("String argument is empty");
            }
        };
    }

    /**
     * Asserts that the argument string's length is between the specified lengths, inclusively.
     *
     * @param minimumLength Minimum String Length, inclusive.
     * @param maximumLength Maximum String Length, inclusive.
     *
     * @return
     */
    public static AlchemyAssertion<String> stringWithLengthBetween(int minimumLength, int maximumLength)
    {
        Checks.Internal.checkThat(minimumLength >= 0, "Minimum length must be at least 0");
        Checks.Internal.checkThat(minimumLength < maximumLength, "Minimum length must be < maximum length.");

        return string ->
        {
            Assertions.notNull().check(string);
            if (string.length() < minimumLength || string.length() > maximumLength)
            {
                String message = String.format("Argument size is not between acceptable range of [%d -> %d]", minimumLength, maximumLength);
                throw new FailedAssertionException(message);
            }
        };
    }
    
    /**
     * Checks that a string contains another.
     * 
     * @param substring
     * @throws IllegalArgumentException If {@code substring} is null or empty.
     */
    public static AlchemyAssertion<String> stringContaining(@NonEmpty String substring) throws IllegalArgumentException
    {
        Checks.Internal.checkNotNullOrEmpty(substring, "substring cannot be empty");
        
        return string ->
        {
            nonEmptyString().check(string);
            
            if (!string.contains(substring))
            {
                throw new FailedAssertionException(format("Expected %s to contain %s", string, substring));
            }
        };
    }
    
    /**
     * Checks that a String has All Upper-Cased characters (also known as ALL-CAPS).
     * 
     * @return 
     */
    public static AlchemyAssertion<String> allUpperCaseString()
    {
        return string ->
        {
            nonEmptyString().check(string);

            for (char character : string.toCharArray())
            {
                if (!Character.isUpperCase(character))
                {
                    throw new FailedAssertionException(format("Expected %s to be all upper-case, but %s isn't",
                                                              string,
                                                              character));
                }
            }
        };
    }
    
    /**
     * Checks that a String has All Lower-Cased characters.
     * 
     * @return 
     */
    public static AlchemyAssertion<String> allLowerCaseString()
    {
       return string -> 
       {
           nonEmptyString().check(string);

           for (char character : string.toCharArray())
           {
               if (!Character.isLowerCase(character))
               {
                   throw new FailedAssertionException(format("Expected %s to be all lower-case, but %s isn't",
                                                             string,
                                                             character));
               }
           }
         
       };
    }

}
