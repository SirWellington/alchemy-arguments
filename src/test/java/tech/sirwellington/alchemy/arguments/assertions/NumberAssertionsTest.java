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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.longs;
import static tech.sirwellington.alchemy.generator.NumberGenerators.negativeIntegers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.positiveIntegers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.positiveLongs;
import static tech.sirwellington.alchemy.generator.NumberGenerators.smallPositiveIntegers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.smallPositiveLongs;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@RunWith(AlchemyTestRunner.class)
@Repeat
public class NumberAssertionsTest
{

    @Before
    public void setUp()
    {
    }

    @Test
    public void testCannotInstantiateClass()
    {
        assertThrows(() -> NumberAssertions.class.newInstance());

        assertThrows(() -> new NumberAssertions())
                .isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testLongLessThan()
    {
        long upperBound = one(longs(-10000L, 100000));
        AlchemyAssertion<Long> instance = NumberAssertions.lessThan(upperBound);

        Tests.checkForNullCase(instance);

        AlchemyGenerator<Long> badNumbers = () -> upperBound + one(longs(0, 10000L));
        AlchemyGenerator<Long> goodNumbers = () -> upperBound - one(smallPositiveLongs());
        Tests.runTests(instance, badNumbers, goodNumbers);

        badNumbers = () -> upperBound;
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @DontRepeat
    @Test
    public void testLongLessThanEdgeCases()

    {
        assertThrows(() -> NumberAssertions.lessThan(Long.MIN_VALUE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testLongGreaterThan()
    {
        long lowerBound = one(longs(-100000L, 100000L));
        AlchemyAssertion<Long> instance = NumberAssertions.greaterThan(lowerBound);
        Tests.checkForNullCase(instance);
        AlchemyGenerator<Long> badNumbers = () -> lowerBound - one(longs(0, 1000L));
        AlchemyGenerator<Long> goodNumbers = () -> lowerBound + one(smallPositiveLongs());
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    public void testLongGreaterThanEdgeCases()
    {
        assertThrows(() -> NumberAssertions.greaterThan(Long.MAX_VALUE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testGreaterThanOrEqualToLong() throws Exception
    {
        long inclusiveLowerBound = one(longs(-10000L, 1000L));
        AlchemyAssertion<Long> instance = NumberAssertions.greaterThanOrEqualTo(inclusiveLowerBound);

        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        AlchemyGenerator<Long> goodArguments = longs(inclusiveLowerBound, Long.MAX_VALUE);
        AlchemyGenerator<Long> badArguments = longs(Long.MIN_VALUE, inclusiveLowerBound);
        Tests.runTests(instance, badArguments, goodArguments);
    }

    @Test
    public void testIntGreaterThan()
    {
        int lowerBound = one(integers(-1000, 1000));
        AlchemyAssertion<Integer> instance = NumberAssertions.greaterThan(lowerBound);

        Tests.checkForNullCase(instance);

        AlchemyGenerator<Integer> badNumbers = integers(lowerBound - one(smallPositiveIntegers()), lowerBound);
        AlchemyGenerator<Integer> goodNumbers = integers(lowerBound + 1, lowerBound + one(integers(2, 1000)));
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @DontRepeat
    @Test
    public void testIntGreaterThanEdgeCases()
    {
        assertThrows(() -> NumberAssertions.greaterThan(Integer.MAX_VALUE))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void testNumberBetweenInts() throws Exception
    {
        int min = one(integers(Integer.MIN_VALUE, Integer.MAX_VALUE - 10));
        int max = one(integers(min, Integer.MAX_VALUE));
        AlchemyAssertion<Integer> instance = NumberAssertions.numberBetween(min, max);

        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        AlchemyGenerator<Integer> goodNumbers = integers(min, max);
        instance.check(goodNumbers.get());

        int numberBelowMinimum = min - one(positiveIntegers());
        if (numberBelowMinimum < min)
        {
            assertThrows(() -> instance.check(numberBelowMinimum)).isInstanceOf(FailedAssertionException.class);
        }
        int numberAboveMaximum = max + one(positiveIntegers());
        if (numberAboveMaximum > max)
        {
            assertThrows(() -> instance.check(numberAboveMaximum)).isInstanceOf(FailedAssertionException.class);
        }
    }

    @Test
    public void testNumberBetweenIntsEdgeCases() throws Exception
    {
        int min = one(integers(Integer.MIN_VALUE, Integer.MAX_VALUE - 10));
        int max = one(integers(min, Integer.MAX_VALUE));

        assertThrows(() -> NumberAssertions.numberBetween(max, min))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testPositiveLong()
    {
        AlchemyAssertion<Long> instance = NumberAssertions.positiveLong();
        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        AlchemyGenerator<Long> goodNumbers = positiveLongs();
        AlchemyGenerator<Long> badNumbers = longs(Long.MIN_VALUE, 0L);

        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    public void testIntLessThan()
    {
        int upperBound = one(integers(-1000, 1000));
        AlchemyAssertion<Integer> instance = NumberAssertions.lessThan(upperBound);

        Tests.checkForNullCase(instance);

        AlchemyGenerator<Integer> badNumbers = () -> upperBound + one(integers(0, 100));
        AlchemyGenerator<Integer> goodNumbers = () -> upperBound - one(smallPositiveIntegers());
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    public void testIntLessThanEdgeCases()
    {
        assertThrows(() -> NumberAssertions.lessThan(Integer.MIN_VALUE))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void testIntLessThanOrEqualTo()
    {
        int upperBound = one(integers(-1000, 1000));
        AlchemyAssertion<Integer> instance = NumberAssertions.lessThanOrEqualTo(upperBound);

        Tests.checkForNullCase(instance);

        AlchemyGenerator<Integer> badNumbers = () -> upperBound + one(smallPositiveIntegers());
        AlchemyGenerator<Integer> goodNumbers = () -> upperBound - one(integers(0, 1000));
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    public void testGreaterThanInt()
    {
        int exclusiveLowerBound = one(integers(-1000, 1000));
        AlchemyAssertion<Integer> instance = NumberAssertions.greaterThan(exclusiveLowerBound);

        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        int amountToAdd = one(smallPositiveIntegers());
        assertThrows(() -> instance.check(exclusiveLowerBound)).isInstanceOf(FailedAssertionException.class);
        instance.check(exclusiveLowerBound + 1);
        instance.check(exclusiveLowerBound + amountToAdd);

        int amountToSubtract = amountToAdd;
        int badValue = exclusiveLowerBound - amountToSubtract;
        assertThrows(() -> instance.check(badValue)).isInstanceOf(FailedAssertionException.class);
    }

    @DontRepeat
    @Test
    public void testGreaterThanIntEdgeCases()
    {
        assertThrows(() -> NumberAssertions.greaterThan(Integer.MAX_VALUE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public void testGreaterThanOrEqualToInt() throws Exception
    {
        int inclusiveLowerBound = one(integers(-1000, 1000));
        AlchemyAssertion<Integer> instance = NumberAssertions.greaterThanOrEqualTo(inclusiveLowerBound);

        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        int amountToAdd = one(integers(40, 100));
        instance.check(inclusiveLowerBound);
        instance.check(inclusiveLowerBound + amountToAdd);

        int amountToSubtract = one(integers(50, 100));
        int badValue = inclusiveLowerBound - amountToSubtract;
        assertThrows(() -> instance.check(badValue)).isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testNumberBetweenLongs() throws Exception
    {
        long min = one(longs(Long.MIN_VALUE, Long.MAX_VALUE - 10L));
        long max = one(longs(min, Long.MAX_VALUE));
        AlchemyAssertion<Long> instance = NumberAssertions.numberBetween(min, max);

        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        long goodLong = one(longs(min, max));
        instance.check(goodLong);

        long numberBelowMin = min - one(positiveLongs());
        if (numberBelowMin < min)
        {
            assertThrows(() -> instance.check(numberBelowMin)).isInstanceOf(FailedAssertionException.class);
        }

        long numberAboveMax = max + one(positiveIntegers());
        if (numberAboveMax > max)
        {
            assertThrows(() -> instance.check(numberAboveMax)).isInstanceOf(FailedAssertionException.class);
        }
    }

    @Test
    public void testNumberBetweenLongsEdgeCases() throws Exception
    {
        long min = one(longs(Long.MIN_VALUE, Long.MAX_VALUE - 10L));
        long max = one(longs(min, Long.MAX_VALUE));
        assertThrows(() -> NumberAssertions.numberBetween(max, min)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testPositiveInteger()
    {
        AlchemyAssertion<Integer> instance = NumberAssertions.positiveInteger();

        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        int goodNumber = one(positiveIntegers());
        instance.check(goodNumber);

        int badNumber = one(negativeIntegers());
        assertThrows(() -> instance.check(badNumber)).isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testLongLessThanOrEqualTo()
    {
        long lowerBound = one(longs(-10000L, 100000L));
        AlchemyAssertion<Long> instance = NumberAssertions.lessThanOrEqualTo(lowerBound);
        Tests.checkForNullCase(instance);

        AlchemyGenerator<Long> badNumbers = () -> lowerBound + one(smallPositiveLongs());
        AlchemyGenerator<Long> goodNumbers = () -> lowerBound - one(longs(0, 1000L));

        Tests.runTests(instance, badNumbers, goodNumbers);
        goodNumbers = () -> lowerBound;
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

}
