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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static sir.wellington.commons.arguments.Assertions.stringWithNoWhitespace;
import static sir.wellington.commons.arguments.Assertions.nonEmptyString;
import static sir.wellington.commons.arguments.Assertions.intIsAtLeast;
import static sir.wellington.commons.arguments.Assertions.nonEmptyCollection;
import static sir.wellington.commons.arguments.Assertions.nonEmptyList;
import static sir.wellington.commons.arguments.Assertions.nonEmptyMap;
import static sir.wellington.commons.arguments.Assertions.notNull;
import static sir.wellington.commons.arguments.Assertions.numberBetween;
import static sir.wellington.commons.arguments.Assertions.positiveInteger;
import static sir.wellington.commons.arguments.Assertions.stringIsAtLeastOfLength;
import static sir.wellington.commons.arguments.Assertions.stringIsAtMostOfLength;
import static sir.wellington.commons.arguments.Assertions.stringIsOExactfLength;
import static sir.wellington.commons.arguments.Assertions.stringLengthBetween;
import static sir.wellington.commons.test.DataGenerator.alphabeticString;
import static sir.wellington.commons.test.DataGenerator.hexadecimalString;
import static sir.wellington.commons.test.DataGenerator.integers;
import static sir.wellington.commons.test.DataGenerator.listOf;
import static sir.wellington.commons.test.DataGenerator.longs;
import static sir.wellington.commons.test.DataGenerator.mapOf;
import static sir.wellington.commons.test.DataGenerator.negativeIntegers;
import static sir.wellington.commons.test.DataGenerator.oneOf;
import static sir.wellington.commons.test.DataGenerator.positiveIntegers;
import static sir.wellington.commons.test.DataGenerator.positiveLongs;
import static sir.wellington.commons.test.DataGenerator.strings;
import static sir.wellington.commons.test.DataGenerator.stringsFromFixedList;
import static sir.wellington.commons.test.DataGenerator.uuids;
import static sir.wellington.commons.test.junit.ThrowableAssertion.assertThrows;

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
        iterations = oneOf(integers(100, 1000));
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
    public void testStringIsOExactfLength() throws Exception
    {
        System.out.println("stringIsOfLength");

        int expectedLength = oneOf(integers(5, 25));
        Assertion<String> instance = stringIsOExactfLength(expectedLength);
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
    public void testIntIsAtLeast() throws Exception
    {
        System.out.println("intIsAtLeast");

        int atLeastThisMuch = oneOf(positiveIntegers());
        Assertion<Integer> instance = intIsAtLeast(atLeastThisMuch);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            int amountToAdd = oneOf(positiveIntegers());
            instance.check(atLeastThisMuch + amountToAdd);

            int amountToSubtract = oneOf(positiveIntegers());
            assertThrows(() -> instance.check(atLeastThisMuch - amountToSubtract))
                    .isInstanceOf(FailedAssertionException.class);
        });

    }

    @Test
    public void testStringIsAtLeastOfLengthWithBadArgs() throws Exception
    {
        System.out.println("testStringIsAtLeastOfLengthWithBadArgs");

        doInLoop(() ->
        {
            int negativeNumber = oneOf(negativeIntegers());

            assertThrows(() -> stringIsAtLeastOfLength(negativeNumber))
                    .isInstanceOf(IllegalArgumentException.class);
        });
    }

    @Test
    public void testStringIsAtLeastOfLength() throws Exception
    {
        System.out.println("stringIsAtLeastOfLength");

        int expectedSize = oneOf(integers(10, 100));

        Assertion<String> instance = stringIsAtLeastOfLength(expectedSize);
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
    public void testStringIsAtMostOfLengthWithBadArgs() throws Exception
    {
        System.out.println("testStringIsAtMostOfLengthWithBadArgs");

        doInLoop(() ->
        {
            int negativeNumber = oneOf(negativeIntegers());

            assertThrows(() -> stringIsAtMostOfLength(negativeNumber))
                    .isInstanceOf(IllegalArgumentException.class);
        });
    }

    @Test
    public void testStringIsAtMostOfLength() throws Exception
    {
        System.out.println("stringIsAtMostOfLength");

        int expectedSize = oneOf(integers(5, 100));
        Assertion<String> instance = stringIsAtMostOfLength(expectedSize);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            final String goodString = oneOf(strings(expectedSize));
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
    public void testStringLengthBetweenWithBadArgs() throws Exception
    {
        System.out.println("testStringLengthBetweenWithBadArgs");
        int goodMin = oneOf(positiveIntegers());
        int goodMax = goodMin + oneOf(positiveIntegers());

        int badMin = oneOf(negativeIntegers());
        int badMax = goodMin - oneOf(positiveIntegers());

        assertThrows(() -> stringLengthBetween(badMin, goodMax))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> stringLengthBetween(goodMin, badMax))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testStringLengthBetween() throws Exception
    {
        System.out.println("stringLengthBetween");

        int minimumLength = oneOf(integers(10, 1000));
        int maximumLength = minimumLength + oneOf(positiveIntegers());

        Assertion<String> instance = stringLengthBetween(minimumLength, maximumLength);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            //Happy Cases
            String stringAtMinimum = oneOf(strings(minimumLength));
            instance.check(stringAtMinimum);

            String stringAtMaximum = oneOf(strings(maximumLength));
            instance.check(stringAtMaximum);

            //Sad cases
            int stringTooShortLength = minimumLength - oneOf(integers(1, 9));
            String stringTooShort = oneOf(strings(stringTooShortLength));
            assertThrows(() -> instance.check(stringTooShort))
                    .isInstanceOf(FailedAssertionException.class);

            int stringTooLongLength = maximumLength + oneOf(positiveIntegers());
            String stringTooLong = oneOf(strings(stringTooLongLength));
            assertThrows(() -> instance.check(stringTooLong))
                    .isInstanceOf(FailedAssertionException.class);
        });

    }

    @Test
    public void testNumberBetween_int_int() throws Exception
    {
        System.out.println("testNumberBetween_int_int");

        int min = oneOf(integers(-100, 100));
        int max = min + oneOf(positiveIntegers());
        Assertion<Integer> instance = numberBetween(min, max);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            int goodNumber = oneOf(integers(min, max));
            instance.check(goodNumber);

            int numberBelowMinimum = min - oneOf(positiveIntegers());
            assertThrows(() -> instance.check(numberBelowMinimum))
                    .isInstanceOf(FailedAssertionException.class);

            int numberAboveMaximum = max + oneOf(positiveIntegers());
            assertThrows(() -> instance.check(numberAboveMaximum))
                    .isInstanceOf(FailedAssertionException.class);
        });

    }

    @Test
    public void testNumberBetween_long_long() throws Exception
    {
        System.out.println("testNumberBetween_long_long");

        long min = oneOf(longs(-10000, 10000));
        long max = min + oneOf(positiveLongs());
        Assertion<Long> instance = numberBetween(min, max);
        assertThat(instance, notNullValue());
        checkForNullCase(instance);

        doInLoop(() ->
        {
            long goodLong = oneOf(longs(min, max));
            instance.check(goodLong);

            long numberBelowMin = min - oneOf(positiveLongs());
            assertThrows(() -> instance.check(numberBelowMin))
                    .isInstanceOf(FailedAssertionException.class);

            long numberAboveMax = max + oneOf(positiveIntegers());
            assertThrows(() -> instance.check(numberAboveMax))
                    .isInstanceOf(FailedAssertionException.class);
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

            int badNumber = oneOf(integers(-10000, -100));
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

        String arg = oneOf(alphabeticString());
        instance.check(arg);
        arg = oneOf(hexadecimalString(10));
        instance.check(arg);
        arg = oneOf(strings(10));
        instance.check(arg);

        assertThrows(() -> instance.check(oneOf(alphabeticString()) + " "))
                .isInstanceOf(FailedAssertionException.class);

        assertThrows(() -> instance.check("some white space here"))
                .isInstanceOf(FailedAssertionException.class);

        assertThrows(() -> instance.check(" " + oneOf(uuids)))
                .isInstanceOf(FailedAssertionException.class);

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

}
