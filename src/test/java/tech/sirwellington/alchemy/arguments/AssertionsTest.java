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

import static java.lang.Math.abs;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import org.mockito.runners.MockitoJUnitRunner;
import static tech.sirwellington.alchemy.arguments.Assertions.greaterThan;
import static tech.sirwellington.alchemy.arguments.Assertions.greaterThanOrEqualTo;
import static tech.sirwellington.alchemy.arguments.Assertions.nonEmptyCollection;
import static tech.sirwellington.alchemy.arguments.Assertions.nonEmptyList;
import static tech.sirwellington.alchemy.arguments.Assertions.nonEmptyMap;
import static tech.sirwellington.alchemy.arguments.Assertions.nonEmptyString;
import static tech.sirwellington.alchemy.arguments.Assertions.notNull;
import static tech.sirwellington.alchemy.arguments.Assertions.numberBetween;
import static tech.sirwellington.alchemy.arguments.Assertions.positiveInteger;
import static tech.sirwellington.alchemy.arguments.Assertions.positiveLong;
import static tech.sirwellington.alchemy.arguments.Assertions.stringWithLength;
import static tech.sirwellington.alchemy.arguments.Assertions.stringWithLengthBetween;
import static tech.sirwellington.alchemy.arguments.Assertions.stringWithLengthGreaterThanOrEqualTo;
import static tech.sirwellington.alchemy.arguments.Assertions.stringWithLengthLessThanOrEqualTo;
import static tech.sirwellington.alchemy.arguments.Assertions.stringWithNoWhitespace;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.listOf;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.mapOf;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.longs;
import static tech.sirwellington.alchemy.generator.NumberGenerators.negativeIntegers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.positiveIntegers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.positiveLongs;
import static tech.sirwellington.alchemy.generator.NumberGenerators.smallPositiveIntegers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.smallPositiveLongs;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString;
import static tech.sirwellington.alchemy.generator.StringGenerators.hexadecimalString;
import static tech.sirwellington.alchemy.generator.StringGenerators.strings;
import static tech.sirwellington.alchemy.generator.StringGenerators.stringsFromFixedList;
import static tech.sirwellington.alchemy.generator.StringGenerators.uuids;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class AssertionsTest
{

    private int iterations;

    @Before
    public void setUp()
    {
        iterations = one(integers(1000, 10_000));
    }

    private void checkForNullCase(AlchemyAssertion assertion)
    {
        assertThrows(() -> assertion.check(null))
                .isInstanceOf(FailedAssertionException.class)
                .containsInMessage("null");
    }

    private void doInLoop(Runnable operation)
    {
        for (int i = 0; i < iterations; ++i)
        {
            operation.run();
        }
    }
    
    
    @Test
    public void testCannotInstantiateClass()
    {
        System.out.println("testCannotInstantiateClass");
        assertThrows(() -> Assertions.class.newInstance());
        
        assertThrows(() -> new Assertions())
                .isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testNotNull() throws Exception
    {
        System.out.println("testNullCheck");

        AlchemyAssertion<Object> instance = notNull();
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        Object mock = mock(Object.class);
        instance.check(mock);
        verifyZeroInteractions(mock);

    }

    @Test
    public void testNonEmptyString() throws Exception
    {
        System.out.println("testNonEmptyString");

        AlchemyAssertion<String> instance = nonEmptyString();
        assertThat(instance, notNullValue());

        doInLoop(() ->
        {
            String arg = one(alphabeticString());
            instance.check(arg);

            assertThrows(() -> instance.check(""))
                    .isInstanceOf(FailedAssertionException.class);

            assertThrows(() -> instance.check(null))
                    .isInstanceOf(FailedAssertionException.class);
        });
    }

    @Test
    public void testStringWithLength() throws Exception
    {
        System.out.println("testStringWithLength");

        assertThrows(() -> stringWithLength(one(negativeIntegers())))
                .isInstanceOf(IllegalArgumentException.class);

        int expectedLength = one(integers(5, 25));
        AlchemyAssertion<String> instance = stringWithLength(expectedLength);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {

            String arg = one(alphabeticString(expectedLength));
            instance.check(arg);

            String tooShort = one(alphabeticString(expectedLength - 1));
            assertThrows(() -> instance.check(tooShort)).
                    isInstanceOf(FailedAssertionException.class);

            String tooLong = one(strings(expectedLength + 1));
            assertThrows(() -> instance.check(tooLong))
                    .isInstanceOf(FailedAssertionException.class);

        });

    }

    @Test
    public void testGreaterThanInt()
    {
        System.out.println("testGreaterThanInt");

        int exclusiveLowerBound = one(integers(-1000, 1000));
        AlchemyAssertion<Integer> instance = greaterThan(exclusiveLowerBound);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            int amountToAdd = one(smallPositiveIntegers());

            assertThrows(() -> instance.check(exclusiveLowerBound))
                    .isInstanceOf(FailedAssertionException.class);

            instance.check(exclusiveLowerBound + 1);
            instance.check(exclusiveLowerBound + amountToAdd);

            int amountToSubtract = amountToAdd;
            int badValue = exclusiveLowerBound - amountToSubtract;
            assertThrows(() -> instance.check(badValue))
                    .isInstanceOf(FailedAssertionException.class);
        });
    }

    @Test
    public void testGreaterThanOrEqualToInt() throws Exception
    {
        System.out.println("testGreaterThanOrEqualToInt");

        int inclusiveLowerBound = one(integers(-1000, 1000));
        AlchemyAssertion<Integer> instance = greaterThanOrEqualTo(inclusiveLowerBound);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            int amountToAdd = one(integers(40, 100));

            instance.check(inclusiveLowerBound);
            instance.check(inclusiveLowerBound + amountToAdd);

            int amountToSubtract = one(integers(50, 100));
            int badValue = inclusiveLowerBound - amountToSubtract;
            assertThrows(() -> instance.check(badValue))
                    .isInstanceOf(FailedAssertionException.class);
        });

    }

    @Test
    public void testGreaterThanOrEqualToLong() throws Exception
    {
        System.out.println("testGreaterThanOrEqualToLong");

        long inclusiveLowerBound = one(longs(-10000L, 1000L));
        AlchemyAssertion<Long> instance = greaterThanOrEqualTo(inclusiveLowerBound);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            long amountToAdd = one(longs(40, 100));

            instance.check(inclusiveLowerBound);
            instance.check(inclusiveLowerBound + amountToAdd);

            long amountToSubtract = one(longs(50, 100));
            long badValue = inclusiveLowerBound - amountToSubtract;
            assertThrows(() -> instance.check(badValue))
                    .isInstanceOf(FailedAssertionException.class);
        });

    }

    @Test
    public void testStringWithLengthGreaterThanOrEqualToWithBadArgs() throws Exception
    {
        System.out.println("testStringWithLengthGreaterThanOrEqualToWithBadArgs");

        doInLoop(() ->
        {
            int negativeNumber = one(negativeIntegers());

            assertThrows(() -> stringWithLengthGreaterThanOrEqualTo(negativeNumber))
                    .isInstanceOf(IllegalArgumentException.class);
        });
    }

    @Test
    public void testStringWithLengthGreaterThanOrEqualTo() throws Exception
    {
        System.out.println("testStringWithLengthGreaterThanOrEqualTo");

        int expectedSize = one(integers(10, 100));

        AlchemyAssertion<String> instance = stringWithLengthGreaterThanOrEqualTo(expectedSize);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            String goodString = one(strings(expectedSize));
            instance.check(goodString);

            int amountToAdd = one(integers(1, 5));
            String anotherGoodString = one(strings(expectedSize + amountToAdd));
            instance.check(anotherGoodString);

            int amountToSubtract = one(integers(1, 5));
            String badString = one(strings(expectedSize - amountToSubtract));
            assertThrows(() -> instance.check(badString))
                    .isInstanceOf(FailedAssertionException.class);
        });

    }

    @Test
    public void testStringWithLengthLessThanOrEqualToWithBadArgs() throws Exception
    {
        System.out.println("testStringWithLengthLessThanOrEqualToWithBadArgs");

        doInLoop(() ->
        {
            int negativeNumber = one(negativeIntegers());

            assertThrows(() -> stringWithLengthLessThanOrEqualTo(negativeNumber))
                    .isInstanceOf(IllegalArgumentException.class);
        });
    }

    @Test
    public void testStringWithLengthLessThanOrEqualTo() throws Exception
    {
        System.out.println("testStringWithLengthLessThanOrEqualTo");

        int expectedSize = one(integers(5, 100));
        AlchemyAssertion<String> instance = stringWithLengthLessThanOrEqualTo(expectedSize);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            String goodString = one(strings(expectedSize));
            instance.check(goodString);

            goodString = one(strings(expectedSize - 1));
            instance.check(goodString);

            int amountToAdd = one(integers(5, 10));
            final String badString = one(strings(expectedSize + amountToAdd));
            assertThrows(() -> instance.check(badString))
                    .isInstanceOf(FailedAssertionException.class);
        });

    }

    @Test
    public void testStringWithNoWhitespace() throws Exception
    {
        System.out.println("testStringWithNoWhitespace");

        AlchemyAssertion<String> instance = stringWithNoWhitespace();
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            String goodString = one(alphabeticString());
            instance.check(goodString);

            String badString = goodString + one(stringsFromFixedList(" ", "\n", "\t")) + goodString;
            assertThrows(() -> instance.check(badString))
                    .isInstanceOf(FailedAssertionException.class);
        });

    }

    @Test
    public void testStringWithLengthBetweenWithBadArgs() throws Exception
    {
        System.out.println("testStringWithLengthBetweenWithBadArgs");

        doInLoop(() ->
        {
            int goodMin = one(integers(-1000, 10000));
            int goodMax = goodMin + one(integers(1000, 10_000));

            int badMin = one(negativeIntegers());
            int badMax = goodMin - one(positiveIntegers());

            assertThrows(() -> stringWithLengthBetween(badMin, goodMax))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThrows(() -> stringWithLengthBetween(goodMin, badMax))
                    .isInstanceOf(IllegalArgumentException.class);
        });

    }

    @Test
    public void testStringWithLengthBetween() throws Exception
    {
        System.out.println("testStringWithLengthBetween");

        int minimumLength = one(integers(10, 100));
        int maximumLength = one(integers(minimumLength, 1_000));

        assertThrows(() -> stringWithLengthBetween(maximumLength, minimumLength))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> stringWithLengthBetween(-minimumLength, maximumLength))
                .isInstanceOf(IllegalArgumentException.class);

        AlchemyAssertion<String> instance = stringWithLengthBetween(minimumLength, maximumLength);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        AlchemyGenerator<String> tooShort = () ->
        {
            //Sad cases
            int stringTooShortLength = minimumLength - one(integers(1, 9));
            String stringTooShort = one(strings(stringTooShortLength));
            return stringTooShort;
        };

        AlchemyGenerator<String> tooLong = () ->
        {
            int stringTooLongLength = maximumLength + one(smallPositiveIntegers());
            String stringTooLong = one(strings(stringTooLongLength));
            return stringTooLong;
        };

        AlchemyGenerator<String> goodStrings = () ->
        {
            int length = one(integers(minimumLength, maximumLength));
            return one(strings(length));
        };

        runTests(instance, tooLong, goodStrings);

        goodStrings = strings(minimumLength);
        runTests(instance, tooShort, goodStrings);

    }

    @Test
    public void testNumberBetweenInts() throws Exception
    {
        System.out.println("testNumberBetweenInts");

        int min = one(integers(Integer.MIN_VALUE, Integer.MAX_VALUE - 10));
        int max = one(integers(min, Integer.MAX_VALUE));

        assertThrows(() -> numberBetween(max, min))
                .isInstanceOf(IllegalArgumentException.class);

        AlchemyAssertion<Integer> instance = numberBetween(min, max);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            int goodNumber = one(integers(min, max));
            instance.check(goodNumber);

            int numberBelowMinimum = min - one(positiveIntegers());

            if (numberBelowMinimum < min)
            {
                assertThrows(() -> instance.check(numberBelowMinimum))
                        .isInstanceOf(FailedAssertionException.class);
            }

            int numberAboveMaximum = max + one(positiveIntegers());
            if (numberAboveMaximum > max)
            {
                assertThrows(() -> instance.check(numberAboveMaximum))
                        .isInstanceOf(FailedAssertionException.class);
            }

        });

    }

    @Test
    public void testNumberBetweenLongs() throws Exception
    {
        System.out.println("testNumberBetweenLongs");

        long min = one(longs(Long.MIN_VALUE, Long.MAX_VALUE - 10L));
        long max = one(longs(min, Long.MAX_VALUE));

        assertThrows(() -> numberBetween(max, min))
                .isInstanceOf(IllegalArgumentException.class);

        AlchemyAssertion<Long> instance = numberBetween(min, max);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            long goodLong = one(longs(min, max));
            instance.check(goodLong);

            long numberBelowMin = min - one(positiveLongs());

            if (numberBelowMin < min)
            {
                assertThrows(() -> instance.check(numberBelowMin))
                        .isInstanceOf(FailedAssertionException.class);
            }

            long numberAboveMax = max + one(positiveIntegers());
            if (numberAboveMax > max)
            {
                assertThrows(() -> instance.check(numberAboveMax))
                        .isInstanceOf(FailedAssertionException.class);
            }
        });

    }

    @Test
    public void testPositiveInteger()
    {

        System.out.println("testPositiveInteger");

        AlchemyAssertion<Integer> instance = positiveInteger();
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            int goodNumber = one(positiveIntegers());
            instance.check(goodNumber);

            int badNumber = one(negativeIntegers());
            assertThrows(() -> instance.check(badNumber))
                    .isInstanceOf(FailedAssertionException.class);
        });

    }

    @Test
    public void testPositiveLong()
    {
        System.out.println("testPositiveLong");

        AlchemyAssertion<Long> instance = positiveLong();
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            long goodNumber = one(positiveLongs());
            instance.check(goodNumber);

            long badNumber = one(negativeIntegers());
            assertThrows(() -> instance.check(badNumber))
                    .isInstanceOf(FailedAssertionException.class);
        });
    }

    @Test
    public void testHasNoWhitespace()
    {
        System.out.println("testHasNoWhitespace");

        AlchemyAssertion<String> instance = stringWithNoWhitespace();
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            String alphabetic = one(alphabeticString());
            instance.check(alphabetic);

            String hex = one(hexadecimalString(10));
            instance.check(hex);

            assertThrows(() -> instance.check(one(alphabeticString()) + " "))
                    .isInstanceOf(FailedAssertionException.class);

            assertThrows(() -> instance.check("some white space here"))
                    .isInstanceOf(FailedAssertionException.class);

            assertThrows(() -> instance.check(" " + one(uuids)))
                    .isInstanceOf(FailedAssertionException.class);

        });

    }

    @Test
    public void testNonEmptyCollection()
    {
        System.out.println("testNonEmptyCollection");

        AlchemyAssertion<Collection<String>> instance = nonEmptyCollection();
        assertThat(instance, notNullValue());

        doInLoop(() ->
        {
            List<String> strings = listOf(alphabeticString());
            instance.check(strings);

            assertThrows(() -> instance.check(null))
                    .isInstanceOf(FailedAssertionException.class);

            assertThrows(() -> instance.check(Collections.emptySet()))
                    .isInstanceOf(FailedAssertionException.class);
        });

    }

    @Test
    public void testNonEmptyList()
    {
        System.out.println("testNonEmptyList");

        AlchemyAssertion<List<String>> instance = nonEmptyList();
        assertThat(instance, notNullValue());

        doInLoop(() ->
        {
            List<String> strings = listOf(alphabeticString());
            instance.check(strings);

            assertThrows(() -> instance.check(null))
                    .isInstanceOf(FailedAssertionException.class);

            assertThrows(() -> instance.check(Collections.emptyList()))
                    .isInstanceOf(FailedAssertionException.class);
        });
    }

    @Test
    public void testNonEmptyMap()
    {
        System.out.println("testNonEmptyMap");

        AlchemyAssertion<Map<String, Integer>> instance = nonEmptyMap();

        doInLoop(() ->
        {
            Map<String, Integer> map = mapOf(alphabeticString(),
                                             positiveIntegers(),
                                             40);

            instance.check(map);

            assertThrows(() -> instance.check(Collections.emptyMap()))
                    .isInstanceOf(FailedAssertionException.class);

            assertThrows(() -> instance.check(null))
                    .isInstanceOf(FailedAssertionException.class);

        });

    }

    @Test
    public void testIntGreaterThan()
    {
        System.out.println("testIntGreaterThan");

        assertThrows(() -> Assertions.greaterThan(Integer.MAX_VALUE))
                .isInstanceOf(IllegalArgumentException.class);

        int lowerBound = one(integers(-1000, 1000));
        AlchemyAssertion<Integer> instance = Assertions.greaterThan(lowerBound);
        checkForNullCase(instance);

        AlchemyGenerator<Integer> badNumbers = integers(lowerBound - one(smallPositiveIntegers()), lowerBound);
        AlchemyGenerator<Integer> goodNumbers = integers(lowerBound + 1, lowerBound + one(smallPositiveIntegers()));

        runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    public void testIntLessThan()
    {
        System.out.println("testIntLessThan");

        assertThrows(() -> Assertions.lessThan(Integer.MIN_VALUE))
                .isInstanceOf(IllegalArgumentException.class);

        int upperBound = one(integers(-1000, 1000));

        AlchemyAssertion<Integer> instance = Assertions.lessThan(upperBound);
        checkForNullCase(instance);

        AlchemyGenerator<Integer> badNumbers = () -> upperBound + one(integers(0, 100));
        AlchemyGenerator<Integer> goodNumbers = () -> upperBound - one(smallPositiveIntegers());
        runTests(instance, badNumbers, goodNumbers);

    }

    @Test
    public void testIntLessThanOrEqualTo()
    {
        System.out.println("testIntLessThanOrEqualTo");

        int upperBound = one(integers(-1000, 1000));
        AlchemyAssertion<Integer> instance = Assertions.lessThanOrEqualTo(upperBound);
        checkForNullCase(instance);

        AlchemyGenerator<Integer> badNumbers = () -> upperBound + one(smallPositiveIntegers());
        AlchemyGenerator<Integer> goodNumbers = () -> upperBound - one(integers(0, 1000));
        runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    public void testLongGreaterThan()
    {
        System.out.println("testLongGreaterThan");

        assertThrows(() -> Assertions.greaterThan(Long.MAX_VALUE))
                .isInstanceOf(IllegalArgumentException.class);

        long lowerBound = one(longs(-100_000L, 100_000L));
        AlchemyAssertion<Long> instance = Assertions.greaterThan(lowerBound);
        checkForNullCase(instance);

        AlchemyGenerator<Long> badNumbers = () -> lowerBound - one(longs(0, 1000L));
        AlchemyGenerator<Long> goodNumbers = () -> lowerBound + one(smallPositiveLongs());
        runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    public void testLongLessThan()
    {
        System.out.println("testLongLessThan");

        assertThrows(() -> Assertions.lessThan(Long.MIN_VALUE))
                .isInstanceOf(IllegalArgumentException.class);

        long upperBound = one(longs(-10_000L, 100_000));
        AlchemyAssertion<Long> instance = Assertions.lessThan(upperBound);
        checkForNullCase(instance);

        AlchemyGenerator<Long> badNumbers = () -> upperBound + one(longs(0, 10_000L));
        AlchemyGenerator<Long> goodNumbers = () -> upperBound - one(smallPositiveLongs());
        runTests(instance, badNumbers, goodNumbers);
        badNumbers = () -> upperBound;
        runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    public void testLongLessThanOrEqualTo()
    {
        System.out.println("testLongLessThanOrEqualTo");

        long lowerBound = one(longs(-10_000L, 100_000L));
        AlchemyAssertion<Long> instance = Assertions.lessThanOrEqualTo(lowerBound);
        checkForNullCase(instance);

        AlchemyGenerator<Long> badNumbers = () -> lowerBound + one(smallPositiveLongs());
        AlchemyGenerator<Long> goodNumbers = () -> lowerBound - one(longs(0, 1000L));
        runTests(instance, badNumbers, goodNumbers);
        goodNumbers = () -> lowerBound;
        runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    public void testEmptyString()
    {
        System.out.println("testEmptyString");

        AlchemyAssertion<String> instance = Assertions.emptyString();

        AlchemyGenerator<String> badArguments = alphabeticString();
        AlchemyGenerator<String> goodArguments = stringsFromFixedList(null, "");
        runTests(instance, badArguments, goodArguments);
    }

    @Test
    public void testStringWithLengthGreaterThan()
    {
        System.out.println("testStringWithLengthGreaterThan");

        int minAccepted = one(smallPositiveIntegers());
        AlchemyAssertion<String> instance = Assertions.stringWithLengthGreaterThan(minAccepted);
        AlchemyGenerator<String> badArguments = () ->
        {
            int length = abs(minAccepted - one(integers(0, 10)));
            return one(alphabeticString(length));
        };
        AlchemyGenerator<String> goodArguments = () ->
        {
            int length = minAccepted + one(smallPositiveIntegers());
            return one(alphabeticString(length));
        };

        runTests(instance, badArguments, goodArguments);
        badArguments = strings(minAccepted);
        runTests(instance, badArguments, goodArguments);
    }

    @Test
    public void testStringWithLengthLessThan()
    {
        System.out.println("testStringWithLengthLessThan");

        int upperBound = one(smallPositiveIntegers());
        AlchemyAssertion<String> instance = Assertions.stringWithLengthLessThan(upperBound);
        AlchemyGenerator<String> badArguments = () ->
        {
            int length = upperBound + one(integers(0, 100));
            return one(strings(length));
        };

        AlchemyGenerator<String> goodArugments = () ->
        {
            int length = one(integers(1, upperBound - 1));
            return one(strings(length));
        };

        runTests(instance, badArguments, goodArugments);
    }

    @Test
    public void testStringThatMatches()
    {
        System.out.println("testStringWithLengthLessThan");

        String letter = one(alphabeticString()).substring(0, 1);
        Pattern pattern = Pattern.compile(".*" + letter + ".*");
        AlchemyAssertion<String> instance = Assertions.stringThatMatches(pattern);
        AlchemyGenerator<String> badArguments = () -> alphabeticString().get().replaceAll(letter, "");
        AlchemyGenerator<String> goodArguments = () -> alphabeticString().get() + letter;
        runTests(instance, badArguments, goodArguments);
    }

    private <T> void runTests(AlchemyAssertion<T> assertion,
                              AlchemyGenerator<T> badArguments,
                              AlchemyGenerator<T> goodArguments)
    {
        doInLoop(() ->
        {
            assertThat(assertion, notNullValue());

            T badArgument = one(badArguments);
            assertThrows(() -> assertion.check(badArgument))
                    .isInstanceOf(FailedAssertionException.class);

            T goodArgument = one(goodArguments);
            assertion.check(goodArgument);
        });
    }

    @Test
    public void testNonEmptyArray()
    {
        System.out.println("testNonEmptyArray");

        AlchemyAssertion<String[]> instance = Assertions.nonEmptyArray();
        assertThat(instance, notNullValue());

        assertThrows(() -> instance.check(null))
                .isInstanceOf(FailedAssertionException.class);

        doInLoop(() ->
        {
            String[] stringArray = listOf(alphabeticString()).toArray(new String[]
            {
            });

            instance.check(stringArray);

            String[] emptyStringArray = new String[]
            {
            };

            assertThrows(() -> instance.check(emptyStringArray))
                    .isInstanceOf(FailedAssertionException.class);
        });
    }

}
