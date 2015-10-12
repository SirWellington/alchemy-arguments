/*
 * Copyright 2015 Sir Wellington.
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
import static sir.wellington.alchemy.arguments.Assertions.greaterThan;
import static sir.wellington.alchemy.arguments.Assertions.greaterThanOrEqualTo;
import static sir.wellington.alchemy.arguments.Assertions.nonEmptyCollection;
import static sir.wellington.alchemy.arguments.Assertions.nonEmptyList;
import static sir.wellington.alchemy.arguments.Assertions.nonEmptyMap;
import static sir.wellington.alchemy.arguments.Assertions.nonEmptyString;
import static sir.wellington.alchemy.arguments.Assertions.notNull;
import static sir.wellington.alchemy.arguments.Assertions.numberBetween;
import static sir.wellington.alchemy.arguments.Assertions.positiveInteger;
import static sir.wellington.alchemy.arguments.Assertions.positiveLong;
import static sir.wellington.alchemy.arguments.Assertions.stringWithLength;
import static sir.wellington.alchemy.arguments.Assertions.stringWithLengthBetween;
import static sir.wellington.alchemy.arguments.Assertions.stringWithLengthGreaterThanOrEqualTo;
import static sir.wellington.alchemy.arguments.Assertions.stringWithLengthLessThanOrEqualTo;
import static sir.wellington.alchemy.arguments.Assertions.stringWithNoWhitespace;
import sir.wellington.alchemy.test.DataGenerator;
import static sir.wellington.alchemy.test.DataGenerator.alphabeticString;
import static sir.wellington.alchemy.test.DataGenerator.hexadecimalString;
import static sir.wellington.alchemy.test.DataGenerator.integers;
import static sir.wellington.alchemy.test.DataGenerator.listOf;
import static sir.wellington.alchemy.test.DataGenerator.longs;
import static sir.wellington.alchemy.test.DataGenerator.mapOf;
import static sir.wellington.alchemy.test.DataGenerator.negativeIntegers;
import static sir.wellington.alchemy.test.DataGenerator.oneOf;
import static sir.wellington.alchemy.test.DataGenerator.positiveIntegers;
import static sir.wellington.alchemy.test.DataGenerator.positiveLongs;
import static sir.wellington.alchemy.test.DataGenerator.smallPositiveIntegers;
import static sir.wellington.alchemy.test.DataGenerator.smallPositiveLongs;
import static sir.wellington.alchemy.test.DataGenerator.strings;
import static sir.wellington.alchemy.test.DataGenerator.stringsFromFixedList;
import static sir.wellington.alchemy.test.DataGenerator.uuids;
import static sir.wellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

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
        iterations = oneOf(integers(1000, 10_000));
    }

    private void checkForNullCase(Assertion assertion)
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
    public void testNotNull() throws Exception
    {
        System.out.println("testNullCheck");

        Assertion<Object> instance = notNull();
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

        Assertion<String> instance = nonEmptyString();
        assertThat(instance, notNullValue());

        doInLoop(() ->
        {
            String arg = oneOf(alphabeticString());
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

        assertThrows(() -> stringWithLength(oneOf(negativeIntegers())))
                .isInstanceOf(IllegalArgumentException.class);

        int expectedLength = oneOf(integers(5, 25));
        Assertion<String> instance = stringWithLength(expectedLength);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {

            String arg = oneOf(alphabeticString(expectedLength));
            instance.check(arg);

            String tooShort = oneOf(alphabeticString(expectedLength - 1));
            assertThrows(() -> instance.check(tooShort)).
                    isInstanceOf(FailedAssertionException.class);

            String tooLong = oneOf(strings(expectedLength + 1));
            assertThrows(() -> instance.check(tooLong))
                    .isInstanceOf(FailedAssertionException.class);

        });

    }

    @Test
    public void testGreaterThanInt()
    {
        System.out.println("testGreaterThanInt");

        int exclusiveLowerBound = oneOf(integers(-1000, 1000));
        Assertion<Integer> instance = greaterThan(exclusiveLowerBound);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            int amountToAdd = oneOf(smallPositiveIntegers());

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

        int inclusiveLowerBound = oneOf(integers(-1000, 1000));
        Assertion<Integer> instance = greaterThanOrEqualTo(inclusiveLowerBound);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            int amountToAdd = oneOf(integers(40, 100));

            instance.check(inclusiveLowerBound);
            instance.check(inclusiveLowerBound + amountToAdd);

            int amountToSubtract = oneOf(integers(50, 100));
            int badValue = inclusiveLowerBound - amountToSubtract;
            assertThrows(() -> instance.check(badValue))
                    .isInstanceOf(FailedAssertionException.class);
        });

    }

    @Test
    public void testGreaterThanOrEqualToLong() throws Exception
    {
        System.out.println("testGreaterThanOrEqualToLong");

        long inclusiveLowerBound = oneOf(longs(-10000L, 1000L));
        Assertion<Long> instance = greaterThanOrEqualTo(inclusiveLowerBound);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            long amountToAdd = oneOf(longs(40, 100));

            instance.check(inclusiveLowerBound);
            instance.check(inclusiveLowerBound + amountToAdd);

            long amountToSubtract = oneOf(longs(50, 100));
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
            int negativeNumber = oneOf(negativeIntegers());

            assertThrows(() -> stringWithLengthGreaterThanOrEqualTo(negativeNumber))
                    .isInstanceOf(IllegalArgumentException.class);
        });
    }

    @Test
    public void testStringWithLengthGreaterThanOrEqualTo() throws Exception
    {
        System.out.println("testStringWithLengthGreaterThanOrEqualTo");

        int expectedSize = oneOf(integers(10, 100));

        Assertion<String> instance = stringWithLengthGreaterThanOrEqualTo(expectedSize);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            String goodString = oneOf(strings(expectedSize));
            instance.check(goodString);

            int amountToAdd = oneOf(integers(1, 5));
            String anotherGoodString = oneOf(strings(expectedSize + amountToAdd));
            instance.check(anotherGoodString);

            int amountToSubtract = oneOf(integers(1, 5));
            String badString = oneOf(strings(expectedSize - amountToSubtract));
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
            int negativeNumber = oneOf(negativeIntegers());

            assertThrows(() -> stringWithLengthLessThanOrEqualTo(negativeNumber))
                    .isInstanceOf(IllegalArgumentException.class);
        });
    }

    @Test
    public void testStringWithLengthLessThanOrEqualTo() throws Exception
    {
        System.out.println("testStringWithLengthLessThanOrEqualTo");

        int expectedSize = oneOf(integers(5, 100));
        Assertion<String> instance = stringWithLengthLessThanOrEqualTo(expectedSize);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            String goodString = oneOf(strings(expectedSize));
            instance.check(goodString);

            goodString = oneOf(strings(expectedSize - 1));
            instance.check(goodString);

            int amountToAdd = oneOf(integers(5, 10));
            final String badString = oneOf(strings(expectedSize + amountToAdd));
            assertThrows(() -> instance.check(badString))
                    .isInstanceOf(FailedAssertionException.class);
        });

    }

    @Test
    public void testStringWithNoWhitespace() throws Exception
    {
        System.out.println("testStringWithNoWhitespace");

        Assertion<String> instance = stringWithNoWhitespace();
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            String goodString = oneOf(alphabeticString());
            instance.check(goodString);

            String badString = goodString + oneOf(stringsFromFixedList(" ", "\n", "\t")) + goodString;
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
            int goodMin = oneOf(integers(-1000, 10000));
            int goodMax = goodMin + oneOf(integers(1000, 10_000));

            int badMin = oneOf(negativeIntegers());
            int badMax = goodMin - oneOf(positiveIntegers());

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

        int minimumLength = oneOf(integers(10, 100));
        int maximumLength = oneOf(integers(minimumLength, 1_000));

        assertThrows(() -> stringWithLengthBetween(maximumLength, minimumLength))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> stringWithLengthBetween(-minimumLength, maximumLength))
                .isInstanceOf(IllegalArgumentException.class);

        Assertion<String> instance = stringWithLengthBetween(minimumLength, maximumLength);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        DataGenerator<String> tooShort = () ->
        {
            //Sad cases
            int stringTooShortLength = minimumLength - oneOf(integers(1, 9));
            String stringTooShort = oneOf(strings(stringTooShortLength));
            return stringTooShort;
        };

        DataGenerator<String> tooLong = () ->
        {
            int stringTooLongLength = maximumLength + oneOf(smallPositiveIntegers());
            String stringTooLong = oneOf(strings(stringTooLongLength));
            return stringTooLong;
        };

        DataGenerator<String> goodStrings = () ->
        {
            int length = oneOf(integers(minimumLength, maximumLength));
            return oneOf(strings(length));
        };

        runTests(instance, tooLong, goodStrings);

        goodStrings = strings(minimumLength);
        runTests(instance, tooShort, goodStrings);

    }

    @Test
    public void testNumberBetweenInts() throws Exception
    {
        System.out.println("testNumberBetweenInts");

        int min = oneOf(integers(Integer.MIN_VALUE, Integer.MAX_VALUE - 10));
        int max = oneOf(integers(min, Integer.MAX_VALUE));

        assertThrows(() -> numberBetween(max, min))
                .isInstanceOf(IllegalArgumentException.class);

        Assertion<Integer> instance = numberBetween(min, max);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            int goodNumber = oneOf(integers(min, max));
            instance.check(goodNumber);

            int numberBelowMinimum = min - oneOf(positiveIntegers());

            if (numberBelowMinimum < min)
            {
                assertThrows(() -> instance.check(numberBelowMinimum))
                        .isInstanceOf(FailedAssertionException.class);
            }

            int numberAboveMaximum = max + oneOf(positiveIntegers());
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

        long min = oneOf(longs(Long.MIN_VALUE, Long.MAX_VALUE - 10L));
        long max = oneOf(longs(min, Long.MAX_VALUE));

        assertThrows(() -> numberBetween(max, min))
                .isInstanceOf(IllegalArgumentException.class);

        Assertion<Long> instance = numberBetween(min, max);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            long goodLong = oneOf(longs(min, max));
            instance.check(goodLong);

            long numberBelowMin = min - oneOf(positiveLongs());

            if (numberBelowMin < min)
            {
                assertThrows(() -> instance.check(numberBelowMin))
                        .isInstanceOf(FailedAssertionException.class);
            }

            long numberAboveMax = max + oneOf(positiveIntegers());
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

        Assertion<Integer> instance = positiveInteger();
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            int goodNumber = oneOf(positiveIntegers());
            instance.check(goodNumber);

            int badNumber = oneOf(negativeIntegers());
            assertThrows(() -> instance.check(badNumber))
                    .isInstanceOf(FailedAssertionException.class);
        });

    }

    @Test
    public void testPositiveLong()
    {
        System.out.println("testPositiveLong");

        Assertion<Long> instance = positiveLong();
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            long goodNumber = oneOf(positiveLongs());
            instance.check(goodNumber);

            long badNumber = oneOf(negativeIntegers());
            assertThrows(() -> instance.check(badNumber))
                    .isInstanceOf(FailedAssertionException.class);
        });
    }

    @Test
    public void testHasNoWhitespace()
    {
        System.out.println("testHasNoWhitespace");

        Assertion<String> instance = stringWithNoWhitespace();
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            String alphabetic = oneOf(alphabeticString());
            instance.check(alphabetic);

            String hex = oneOf(hexadecimalString(10));
            instance.check(hex);

            assertThrows(() -> instance.check(oneOf(alphabeticString()) + " "))
                    .isInstanceOf(FailedAssertionException.class);

            assertThrows(() -> instance.check("some white space here"))
                    .isInstanceOf(FailedAssertionException.class);

            assertThrows(() -> instance.check(" " + oneOf(uuids)))
                    .isInstanceOf(FailedAssertionException.class);

        });

    }

    @Test
    public void testNonEmptyCollection()
    {
        System.out.println("testNonEmptyCollection");

        Assertion<Collection<String>> instance = nonEmptyCollection();
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

        Assertion<List<String>> instance = nonEmptyList();
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

        Assertion<Map<String, Integer>> instance = nonEmptyMap();

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

        int lowerBound = oneOf(integers(-1000, 1000));
        Assertion<Integer> instance = Assertions.greaterThan(lowerBound);
        checkForNullCase(instance);

        DataGenerator<Integer> badNumbers = integers(lowerBound - oneOf(smallPositiveIntegers()), lowerBound);
        DataGenerator<Integer> goodNumbers = integers(lowerBound + 1, lowerBound + oneOf(smallPositiveIntegers()));

        runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    public void testIntLessThan()
    {
        System.out.println("testIntLessThan");

        assertThrows(() -> Assertions.lessThan(Integer.MIN_VALUE))
                .isInstanceOf(IllegalArgumentException.class);

        int upperBound = oneOf(integers(-1000, 1000));

        Assertion<Integer> instance = Assertions.lessThan(upperBound);
        checkForNullCase(instance);

        DataGenerator<Integer> badNumbers = () -> upperBound + oneOf(integers(0, 100));
        DataGenerator<Integer> goodNumbers = () -> upperBound - oneOf(smallPositiveIntegers());
        runTests(instance, badNumbers, goodNumbers);

    }

    @Test
    public void testIntLessThanOrEqualTo()
    {
        System.out.println("testIntLessThanOrEqualTo");

        int upperBound = oneOf(integers(-1000, 1000));
        Assertion<Integer> instance = Assertions.lessThanOrEqualTo(upperBound);
        checkForNullCase(instance);

        DataGenerator<Integer> badNumbers = () -> upperBound + oneOf(smallPositiveIntegers());
        DataGenerator<Integer> goodNumbers = () -> upperBound - oneOf(integers(0, 1000));
        runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    public void testLongGreaterThan()
    {
        System.out.println("testLongGreaterThan");

        assertThrows(() -> Assertions.greaterThan(Long.MAX_VALUE))
                .isInstanceOf(IllegalArgumentException.class);

        long lowerBound = oneOf(longs(-100_000L, 100_000L));
        Assertion<Long> instance = Assertions.greaterThan(lowerBound);
        checkForNullCase(instance);

        DataGenerator<Long> badNumbers = () -> lowerBound - oneOf(longs(0, 1000L));
        DataGenerator<Long> goodNumbers = () -> lowerBound + oneOf(smallPositiveLongs());
        runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    public void testLongLessThan()
    {
        System.out.println("testLongLessThan");

        assertThrows(() -> Assertions.lessThan(Long.MIN_VALUE))
                .isInstanceOf(IllegalArgumentException.class);

        long upperBound = oneOf(longs(-10_000L, 100_000));
        Assertion<Long> instance = Assertions.lessThan(upperBound);
        checkForNullCase(instance);

        DataGenerator<Long> badNumbers = () -> upperBound + oneOf(longs(0, 10_000L));
        DataGenerator<Long> goodNumbers = () -> upperBound - oneOf(smallPositiveLongs());
        runTests(instance, badNumbers, goodNumbers);
        badNumbers = () -> upperBound;
        runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    public void testLongLessThanOrEqualTo()
    {
        System.out.println("testLongLessThanOrEqualTo");

        long lowerBound = oneOf(longs(-10_000L, 100_000L));
        Assertion<Long> instance = Assertions.lessThanOrEqualTo(lowerBound);
        checkForNullCase(instance);

        DataGenerator<Long> badNumbers = () -> lowerBound + oneOf(smallPositiveLongs());
        DataGenerator<Long> goodNumbers = () -> lowerBound - oneOf(longs(0, 1000L));
        runTests(instance, badNumbers, goodNumbers);
        goodNumbers = () -> lowerBound;
        runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    public void testEmptyString()
    {
        System.out.println("testEmptyString");

        Assertion<String> instance = Assertions.emptyString();

        DataGenerator<String> badArguments = alphabeticString();
        DataGenerator<String> goodArguments = stringsFromFixedList(null, "");
        runTests(instance, badArguments, goodArguments);
    }

    @Test
    public void testStringWithLengthGreaterThan()
    {
        System.out.println("testStringWithLengthGreaterThan");

        int minAccepted = oneOf(smallPositiveIntegers());
        Assertion<String> instance = Assertions.stringWithLengthGreaterThan(minAccepted);
        DataGenerator<String> badArguments = () ->
        {
            int length = abs(minAccepted - oneOf(integers(0, 10)));
            return oneOf(alphabeticString(length));
        };
        DataGenerator<String> goodArguments = () ->
        {
            int length = minAccepted + oneOf(smallPositiveIntegers());
            return oneOf(alphabeticString(length));
        };

        runTests(instance, badArguments, goodArguments);
        badArguments = strings(minAccepted);
        runTests(instance, badArguments, goodArguments);
    }

    @Test
    public void testStringWithLengthLessThan()
    {
        System.out.println("testStringWithLengthLessThan");

        int upperBound = oneOf(smallPositiveIntegers());
        Assertion<String> instance = Assertions.stringWithLengthLessThan(upperBound);
        DataGenerator<String> badArguments = () ->
        {
            int length = upperBound + oneOf(integers(0, 100));
            return oneOf(strings(length));
        };

        DataGenerator<String> goodArugments = () ->
        {
            int length = oneOf(integers(1, upperBound - 1));
            return oneOf(strings(length));
        };

        runTests(instance, badArguments, goodArugments);
    }

    @Test
    public void testStringThatMatches()
    {
        System.out.println("testStringWithLengthLessThan");

        String letter = oneOf(alphabeticString()).substring(0, 1);
        Pattern pattern = Pattern.compile(".*" + letter + ".*");
        Assertion<String> instance = Assertions.stringThatMatches(pattern);
        DataGenerator<String> badArguments = () -> alphabeticString().get().replaceAll(letter, "");
        DataGenerator<String> goodArguments = () -> alphabeticString().get() + letter;
        runTests(instance, badArguments, goodArguments);
    }

    private <T> void runTests(Assertion<T> assertion,
                              DataGenerator<T> badArguments,
                              DataGenerator<T> goodArguments)
    {
        doInLoop(() ->
        {
            assertThat(assertion, notNullValue());

            T badArgument = oneOf(badArguments);
            assertThrows(() -> assertion.check(badArgument))
                    .isInstanceOf(FailedAssertionException.class);

            T goodArgument = oneOf(goodArguments);
            assertion.check(goodArgument);
        });
    }

    @Test
    public void testNonEmptyArray()
    {
        System.out.println("testNonEmptyArray");

        Assertion<String[]> instance = Assertions.nonEmptyArray();
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
