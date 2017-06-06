
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
import tech.sirwellington.alchemy.test.junit.runners.*;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.arguments.Arguments.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.*;

/**
 * @author SirWellington
 */
@RunWith(AlchemyTestRunner.class)
@Repeat(10_000)
public class NumberAssertionsTest
{

    @Before
    public void setUp()
    {
    }

    @DontRepeat
    @Test(expected = IllegalAccessException.class)
    public void testCannotInstantiateClass1() throws IllegalAccessException, InstantiationException
    {
        NumberAssertions.class.newInstance();

        new NumberAssertions();
    }


    @DontRepeat
    @Test(expected = IllegalAccessException.class)
    public void testCannotInstantiateClass2() throws IllegalAccessException, InstantiationException
    {
        NumberAssertions.class.newInstance();

        new NumberAssertions();
    }


    //==============================
    //INTEGER TESTS
    //==============================

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


    }

    @Test(expected = FailedAssertionException.class)
    public void testNumberBetweenIntsWithBadArgs1() throws Exception
    {
        int min = one(integers(Integer.MIN_VALUE, Integer.MAX_VALUE - 10));
        int max = one(integers(min, Integer.MAX_VALUE));
        AlchemyAssertion<Integer> instance = NumberAssertions.numberBetween(min, max);

        int numberBelowMinimum = min - one(positiveIntegers());
        if (numberBelowMinimum < min)
        {
            instance.check(numberBelowMinimum);
        }
    }


    @Test(expected = FailedAssertionException.class)
    public void testNumberBetweenIntsWithBadArgs2() throws Exception
    {
        int min = one(integers(Integer.MIN_VALUE, Integer.MAX_VALUE - 10));
        int max = one(integers(min, Integer.MAX_VALUE));
        AlchemyAssertion<Integer> instance = NumberAssertions.numberBetween(min, max);

        int numberAboveMaximum = max + one(positiveIntegers());
        if (numberAboveMaximum > max)
        {
            instance.check(numberAboveMaximum);
        }
    }


    @Test(expected = IllegalArgumentException.class)
    public void testNumberBetweenIntsEdgeCases() throws Exception
    {
        int min = one(integers(Integer.MIN_VALUE, Integer.MAX_VALUE - 10));
        int max = one(integers(min, Integer.MAX_VALUE));

        NumberAssertions.numberBetween(max, min);
    }

    @Test
    public void testIntLessThan()
    {
        final int upperBound = one(integers(-1000, 1000));

        AlchemyAssertion<Integer> instance = NumberAssertions.lessThan(upperBound);

        Tests.checkForNullCase(instance);

        AlchemyGenerator<Integer> badNumbers = new AlchemyGenerator<Integer>()
        {
            @Override
            public Integer get()
            {
                return upperBound + one(integers(0, 100));
            }
        };

        AlchemyGenerator<Integer> goodNumbers = new AlchemyGenerator<Integer>()
        {
            @Override
            public Integer get()
            {
                return upperBound - one(smallPositiveIntegers());
            }
        };

        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntLessThanEdgeCases()
    {
        NumberAssertions.lessThan(Integer.MIN_VALUE);
    }

    @Test
    public void testIntLessThanOrEqualTo()
    {
        final int upperBound = one(integers(-1000, 1000));
        AlchemyAssertion<Integer> instance = NumberAssertions.lessThanOrEqualTo(upperBound);

        Tests.checkForNullCase(instance);

        AlchemyGenerator<Integer> badNumbers = new AlchemyGenerator<Integer>()
        {
            @Override
            public Integer get()
            {
                return upperBound + one(smallPositiveIntegers());
            }
        };

        AlchemyGenerator<Integer> goodNumbers = new AlchemyGenerator<Integer>()
        {
            @Override
            public Integer get()
            {
                return upperBound - one(integers(0, 1000));
            }
        };

        Tests.runTests(instance, badNumbers, goodNumbers);
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
    @Test(expected = IllegalArgumentException.class)
    public void testIntGreaterThanEdgeCases()
    {
        NumberAssertions.greaterThan(Integer.MAX_VALUE);
    }

    @Test
    public void testIntGreaterThanOrEqualTo() throws Exception
    {
        int inclusiveLowerBound = one(integers(-1000, 1000));
        AlchemyAssertion<Integer> instance = NumberAssertions.greaterThanOrEqualTo(inclusiveLowerBound);

        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        int amountToAdd = one(integers(40, 100));
        instance.check(inclusiveLowerBound);
        instance.check(inclusiveLowerBound + amountToAdd);
    }

    @Test(expected = FailedAssertionException.class)
    public void testIntGreaterThanOrEqualToWithBadArgs() throws Exception
    {
        int inclusiveLowerBound = one(integers(-1000, 1000));
        AlchemyAssertion<Integer> instance = NumberAssertions.greaterThanOrEqualTo(inclusiveLowerBound);

        int amountToSubtract = one(integers(50, 100));
        int badValue = inclusiveLowerBound - amountToSubtract;
        instance.check(badValue);
    }

    @Test
    public void testPositiveInteger()
    {
        AlchemyAssertion<Integer> instance = NumberAssertions.positiveInteger();

        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        int goodNumber = one(positiveIntegers());
        instance.check(goodNumber);

    }

    @Test(expected = FailedAssertionException.class)
    public void testPositiveIntegerWithBadArgs() throws Exception
    {
        AlchemyAssertion<Integer> instance = NumberAssertions.positiveInteger();

        int badNumber = one(negativeIntegers());
        instance.check(badNumber);
    }

    @Test
    public void testNegativeInteger()
    {
        AlchemyAssertion<Integer> instance = NumberAssertions.negativeInteger();
        assertThat(instance, notNullValue());

        int negative = one(negativeIntegers());
        instance.check(negative);
    }

    @Test(expected = FailedAssertionException.class)
    public void testNegativeIntegerWithBadArgs1() throws Exception
    {
        AlchemyAssertion<Integer> instance = NumberAssertions.negativeInteger();
        int positive = one(positiveIntegers());
        instance.check(positive);
    }

    @Test(expected = FailedAssertionException.class)
    public void testNegativeIntegerWithBadArgs2() throws Exception
    {
        AlchemyAssertion<Integer> instance = NumberAssertions.negativeInteger();
        instance.check(null);
    }

    //==============================
    //LONG TESTS
    //==============================
    @Test
    public void testLongGreaterThan()
    {
        final long lowerBound = one(longs(-100000L, 100000L));
        AlchemyAssertion<Long> instance = NumberAssertions.greaterThan(lowerBound);
        Tests.checkForNullCase(instance);

        AlchemyGenerator<Long> badNumbers = new AlchemyGenerator<Long>()
        {
            @Override
            public Long get()
            {
                return lowerBound - one(longs(0, 1000L));
            }
        };

        AlchemyGenerator<Long> goodNumbers = new AlchemyGenerator<Long>()
        {
            @Override
            public Long get()
            {
                return lowerBound + one(smallPositiveLongs());
            }
        };

        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLongGreaterThanEdgeCases()
    {
        NumberAssertions.greaterThan(Long.MAX_VALUE);
    }

    @Test
    public void testLongGreaterThanOrEqualTo() throws Exception
    {
        long inclusiveLowerBound = one(longs(-10_000L, 10_000L));
        AlchemyAssertion<Long> instance = NumberAssertions.greaterThanOrEqualTo(inclusiveLowerBound);

        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        AlchemyGenerator<Long> goodArguments = longs(inclusiveLowerBound, Long.MAX_VALUE);
        AlchemyGenerator<Long> badArguments = longs(Long.MIN_VALUE, inclusiveLowerBound);
        Tests.runTests(instance, badArguments, goodArguments);
    }

    @Test
    public void testLongLessThan()
    {
        final long upperBound = one(longs(-10000L, 100000));
        AlchemyAssertion<Long> instance = NumberAssertions.lessThan(upperBound);

        Tests.checkForNullCase(instance);

        AlchemyGenerator<Long> badNumbers = new AlchemyGenerator<Long>()
        {
            @Override
            public Long get()
            {
                return upperBound + one(longs(0, 10000L));
            }
        };

        AlchemyGenerator<Long> goodNumbers = new AlchemyGenerator<Long>()
        {
            @Override
            public Long get()
            {
                return upperBound - one(smallPositiveLongs());
            }
        };

        Tests.runTests(instance, badNumbers, goodNumbers);

        badNumbers = new AlchemyGenerator<Long>()
        {
            @Override
            public Long get()
            {
                return upperBound;
            }
        };

        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @DontRepeat
    @Test(expected = IllegalArgumentException.class)
    public void testLongLessThanEdgeCases()

    {
        NumberAssertions.lessThan(Long.MIN_VALUE);
    }

    @Test
    public void testLongLessThanOrEqualTo()
    {
        final long lowerBound = one(longs(-10000L, 100000L));
        AlchemyAssertion<Long> instance = NumberAssertions.lessThanOrEqualTo(lowerBound);
        Tests.checkForNullCase(instance);

        AlchemyGenerator<Long> badNumbers = new AlchemyGenerator<Long>()
        {
            @Override
            public Long get()
            {
                return lowerBound + one(smallPositiveLongs());
            }
        };

        AlchemyGenerator<Long> goodNumbers = new AlchemyGenerator<Long>()
        {
            @Override
            public Long get()
            {
                return lowerBound - one(longs(0, 1000L));
            }
        };

        Tests.runTests(instance, badNumbers, goodNumbers);

        goodNumbers = new AlchemyGenerator<Long>()
        {
            @Override
            public Long get()
            {
                return lowerBound;
            }
        };

        Tests.runTests(instance, badNumbers, goodNumbers);
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
    }

    @Test(expected = FailedAssertionException.class)
    public void testNumberBetweenLongsWithBadArgs1() throws Exception
    {
        long min = one(longs(Long.MIN_VALUE, Long.MAX_VALUE - 10L));
        long max = one(longs(min, Long.MAX_VALUE));
        AlchemyAssertion<Long> instance = NumberAssertions.numberBetween(min, max);

        long numberBelowMin = min - one(positiveLongs());
        if (numberBelowMin < min)
        {
            instance.check(numberBelowMin);
        }
    }

    @Test(expected = FailedAssertionException.class)
    public void testNumberBetweenLongsWithBadArgs2() throws Exception
    {
        long min = one(longs(Long.MIN_VALUE, Long.MAX_VALUE - 10L));
        long max = one(longs(min, Long.MAX_VALUE));
        AlchemyAssertion<Long> instance = NumberAssertions.numberBetween(min, max);

        long numberAboveMax = max + one(positiveIntegers());
        if (numberAboveMax > max)
        {
            instance.check(numberAboveMax);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNumberBetweenLongsEdgeCases() throws Exception
    {
        long min = one(longs(Long.MIN_VALUE, Long.MAX_VALUE - 10L));
        long max = one(longs(min, Long.MAX_VALUE));
        NumberAssertions.numberBetween(max, min);
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
    public void testNegativeLong()
    {
        AlchemyAssertion<Long> instance = NumberAssertions.negativeLong();
        checkThat(instance, notNullValue());

        long negative = one(longs(Long.MIN_VALUE, 0));
        instance.check(negative);
    }

    @Test(expected = FailedAssertionException.class)
    public void testNegativeLongWithBadArgs1() throws Exception
    {
        AlchemyAssertion<Long> instance = NumberAssertions.negativeLong();
        long positive = one(positiveLongs());

        instance.check(positive);
    }
 @Test(expected = FailedAssertionException.class)
    public void testNegativeLongWithBadArgs2() throws Exception
    {
        AlchemyAssertion<Long> instance = NumberAssertions.negativeLong();

        instance.check(null);
    }

    //==============================
    //DOUBLE TESTS
    //==============================
    @Test
    public void testDoubleLessThan()
    {
        final double upperBound = one(doubles(-10000, 100000));
        AlchemyAssertion<Double> instance = NumberAssertions.lessThan(upperBound);

        Tests.checkForNullCase(instance);

        AlchemyGenerator<Double> badNumbers = new AlchemyGenerator<Double>()
        {
            @Override
            public Double get()
            {
                return  upperBound + one(doubles(0, 10000L));
            }
        };

        AlchemyGenerator<Double> goodNumbers = new AlchemyGenerator<Double>()
        {
            @Override
            public Double get()
            {
                return upperBound - one(doubles(1.0, 100.0));
            }
        };

        Tests.runTests(instance, badNumbers, goodNumbers);

        badNumbers = new AlchemyGenerator<Double>()
        {
            @Override
            public Double get()
            {
                return upperBound;
            }
        };

        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @DontRepeat
    @Test(expected = IllegalArgumentException.class)
    public void testDoubleLessThanEdgeCases()
    {
        NumberAssertions.lessThan(-Double.MAX_VALUE);
    }

    @Test
    public void testDoubleLessThanWithDelta()
    {
        double upperBound = one(doubles(-10000, 100000));
        double delta = one(doubles(1.0, 10));
        AlchemyAssertion<Double> instance = NumberAssertions.lessThan(upperBound, delta);

        Tests.checkForNullCase(instance);

        AlchemyGenerator<Double> badNumbers = doubles(upperBound + delta + 1.0, Double.MAX_VALUE);
        AlchemyGenerator<Double> goodNumbers = doubles(-Double.MAX_VALUE, upperBound - delta);
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    public void testDoubleLessThanOrEqualTo()
    {
        double upperBound = one(doubles(0.0, Double.MAX_VALUE / 2));
        AlchemyAssertion<Double> instance = NumberAssertions.lessThanOrEqualTo(upperBound);
        Tests.checkForNullCase(instance);

        AlchemyGenerator<Double> badNumbers = doubles(upperBound + 0.1, Double.MAX_VALUE);
        AlchemyGenerator<Double> goodNumber = doubles(-Double.MAX_VALUE, upperBound);
        Tests.runTests(instance, badNumbers, goodNumber);
    }

    @Test
    public void testDoubleLessThanOrEqualToWithDelta()
    {
        double upperBound = one(doubles(0.0, Double.MAX_VALUE / 2));
        double delta = one(doubles(1.0, 100.0));
        AlchemyAssertion<Double> instance = NumberAssertions.lessThanOrEqualTo(upperBound, delta);
        Tests.checkForNullCase(instance);

        AlchemyGenerator<Double> badNumbers = doubles(upperBound + delta + 0.1, Double.MAX_VALUE);
        AlchemyGenerator<Double> goodNumber = doubles(-Double.MAX_VALUE, upperBound);
        Tests.runTests(instance, badNumbers, goodNumber);
    }

    @Test
    public void testDoubleGreaterThan()
    {
        double lowerBound = one(doubles(-100000, 100000));
        AlchemyAssertion<Double> instance = NumberAssertions.greaterThan(lowerBound);
        Tests.checkForNullCase(instance);

        AlchemyGenerator<Double> badNumbers = doubles(-Double.MAX_VALUE, lowerBound);
        AlchemyGenerator<Double> goodNumbers = doubles(lowerBound + 0.1, Double.MAX_VALUE);
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoubleGreaterThanEdgeCases()
    {
        NumberAssertions.greaterThan(Double.MAX_VALUE);
    }

    @Test
    public void testDoubleGreaterThanWithDelta()
    {
        double lowerBound = one(doubles(-100000, 100000));
        double delta = one(doubles(1.0, 100.0));
        AlchemyAssertion<Double> instance = NumberAssertions.greaterThan(lowerBound, delta);
        Tests.checkForNullCase(instance);

        AlchemyGenerator<Double> badNumbers = doubles(-Double.MAX_VALUE, lowerBound - delta);
        AlchemyGenerator<Double> goodNumbers = doubles(lowerBound, Double.MAX_VALUE);
        Tests.runTests(instance, badNumbers, goodNumbers);
    }


    @Test
    public void testDoubleGreaterThanOrEqualTo() throws Exception
    {
        double inclusiveLowerBound = one(doubles(-10_000, 10_000));
        AlchemyAssertion<Double> instance = NumberAssertions.greaterThanOrEqualTo(inclusiveLowerBound);

        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        AlchemyGenerator<Double> goodArguments = doubles(inclusiveLowerBound, Double.MAX_VALUE);
        AlchemyGenerator<Double> badArguments = doubles(-Double.MAX_VALUE, inclusiveLowerBound);
        Tests.runTests(instance, badArguments, goodArguments);
    }

}
