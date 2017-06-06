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
package tech.sirwellington.alchemy.arguments.assertions

import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.Arguments.checkThat
import tech.sirwellington.alchemy.arguments.FailedAssertionException
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat
import tech.sirwellington.alchemy.test.junit.runners.Repeat

/**

 * @author SirWellington
 */
@RunWith(AlchemyTestRunner::class)
@Repeat(10000)
class NumberAssertionsTest
{

    @Before
    fun setUp()
    {
    }

    @DontRepeat
    @Test
    fun testCannotInstantiateClass()
    {
        assertThrows { NumberAssertions::class.java.newInstance() }

        assertThrows { NumberAssertions() }
                .isInstanceOf(IllegalAccessException::class.java)
    }
    //==============================
    //INTEGER TESTS
    //==============================

    @Test
    @Throws(Exception::class)
    fun testNumberBetweenInts()
    {
        val min = one(integers(Integer.MIN_VALUE, Integer.MAX_VALUE - 10))
        val max = one(integers(min, Integer.MAX_VALUE))
        val instance = NumberAssertions.numberBetween(min, max)

        assertThat(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val goodNumbers = integers(min, max)
        instance.check(goodNumbers.get())

        val numberBelowMinimum = min - one(positiveIntegers())
        if (numberBelowMinimum < min)
        {
            assertThrows { instance.check(numberBelowMinimum) }.failedAssertion()
        }
        val numberAboveMaximum = max + one(positiveIntegers())
        if (numberAboveMaximum > max)
        {
            assertThrows { instance.check(numberAboveMaximum) }.failedAssertion()
        }
    }

    @Test
    @Throws(Exception::class)
    fun testNumberBetweenIntsEdgeCases()
    {
        val min = one(integers(Integer.MIN_VALUE, Integer.MAX_VALUE - 10))
        val max = one(integers(min, Integer.MAX_VALUE))

        assertThrows { NumberAssertions.numberBetween(max, min) }
                .illegalArgument()
    }

    @Test
    fun testIntLessThan()
    {
        val upperBound = one(integers(-1000, 1000))
        val instance = NumberAssertions.lessThan(upperBound)

        Tests.checkForNullCase(instance)

        val badNumbers = { upperBound + one(integers(0, 100)) }
        val goodNumbers = { upperBound - one(smallPositiveIntegers()) }
        Tests.runTests<Int>(instance, badNumbers, goodNumbers)
    }

    @Test
    fun testIntLessThanEdgeCases()
    {
        assertThrows { NumberAssertions.lessThan(Integer.MIN_VALUE) }
                .illegalArgument()
    }

    @Test
    fun testIntLessThanOrEqualTo()
    {
        val upperBound = one(integers(-1000, 1000))
        val instance = NumberAssertions.lessThanOrEqualTo(upperBound)

        Tests.checkForNullCase(instance)

        val badNumbers = { upperBound + one(smallPositiveIntegers()) }
        val goodNumbers = { upperBound - one(integers(0, 1000)) }
        Tests.runTests<Int>(instance, badNumbers, goodNumbers)
    }

    @Test
    fun testIntGreaterThan()
    {
        val lowerBound = one(integers(-1000, 1000))
        val instance = NumberAssertions.greaterThan(lowerBound)

        Tests.checkForNullCase(instance)

        val badNumbers = integers(lowerBound - one(smallPositiveIntegers()), lowerBound)
        val goodNumbers = integers(lowerBound + 1, lowerBound + one(integers(2, 1000)))
        Tests.runTests<Int>(instance, badNumbers, goodNumbers)
    }

    @DontRepeat
    @Test
    fun testIntGreaterThanEdgeCases()
    {
        assertThrows { NumberAssertions.greaterThan(Integer.MAX_VALUE) }
                .illegalArgument()
    }

    @Throws(Exception::class)
    fun testIntGreaterThanOrEqualTo()
    {
        val inclusiveLowerBound = one(integers(-1000, 1000))
        val instance = NumberAssertions.greaterThanOrEqualTo(inclusiveLowerBound)

        assertThat<AlchemyAssertion<Int>>(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val amountToAdd = one(integers(40, 100))
        instance.check(inclusiveLowerBound)
        instance.check(inclusiveLowerBound + amountToAdd)

        val amountToSubtract = one(integers(50, 100))
        val badValue = inclusiveLowerBound - amountToSubtract
        assertThrows { instance.check(badValue) }.failedAssertion()
    }

    @Test
    fun testPositiveInteger()
    {
        val instance = NumberAssertions.positiveInteger()

        assertThat(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val goodNumber = one(positiveIntegers())
        instance.check(goodNumber)

        val badNumber = one(negativeIntegers())
        assertThrows { instance.check(badNumber) }.failedAssertion()
    }

    @Test
    fun testNegativeInteger()
    {
        val instance = NumberAssertions.negativeInteger()
        assertThat(instance, notNullValue())

        val negative = one(negativeIntegers())
        instance.check(negative)

        val positive = one(positiveIntegers())
        assertThrows { instance.check(positive) }.failedAssertion()
        assertThrows { instance.check(null) }
                .failedAssertion()
    }

    //==============================
    //LONG TESTS
    //==============================
    @Test
    fun testLongGreaterThan()
    {
        val lowerBound = one(longs(-100000L, 100000L))
        val instance = NumberAssertions.greaterThan(lowerBound)
        Tests.checkForNullCase(instance)
        val badNumbers = { lowerBound - one(longs(0, 1000L)) }
        val goodNumbers = { lowerBound + one(smallPositiveLongs()) }
        Tests.runTests<Long>(instance, badNumbers, goodNumbers)
    }

    @Test
    fun testLongGreaterThanEdgeCases()
    {
        assertThrows { NumberAssertions.greaterThan(java.lang.Long.MAX_VALUE) }
                .illegalArgument()
    }

    @Test
    @Throws(Exception::class)
    fun testLongGreaterThanOrEqualTo()
    {
        val inclusiveLowerBound = one(longs(-10_000L, 10_000L))
        val instance = NumberAssertions.greaterThanOrEqualTo(inclusiveLowerBound)

        assertThat<AlchemyAssertion<Long>>(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val goodArguments = longs(inclusiveLowerBound, java.lang.Long.MAX_VALUE)
        val badArguments = longs(java.lang.Long.MIN_VALUE, inclusiveLowerBound)
        Tests.runTests<Long>(instance, badArguments, goodArguments)
    }

    @Test
    fun testLongLessThan()
    {
        val upperBound = one(longs(-10000L, 100000))
        val instance = NumberAssertions.lessThan(upperBound)

        Tests.checkForNullCase(instance)

        var badNumbers = { upperBound + one(longs(0, 10000L)) }
        val goodNumbers = { upperBound - one(smallPositiveLongs()) }
        Tests.runTests<Long>(instance, badNumbers, goodNumbers)

        badNumbers = { upperBound }
        Tests.runTests<Long>(instance, badNumbers, goodNumbers)
    }

    @DontRepeat
    @Test
    fun testLongLessThanEdgeCases()
    {
        assertThrows { NumberAssertions.lessThan(java.lang.Long.MIN_VALUE) }
                .illegalArgument()
    }

    @Test
    fun testLongLessThanOrEqualTo()
    {
        val lowerBound = one(longs(-10000L, 100000L))
        val instance = NumberAssertions.lessThanOrEqualTo(lowerBound)
        Tests.checkForNullCase(instance)

        val badNumbers = { lowerBound + one(smallPositiveLongs()) }
        var goodNumbers = { lowerBound - one(longs(0, 1000L)) }

        Tests.runTests<Long>(instance, badNumbers, goodNumbers)
        goodNumbers = { lowerBound }
        Tests.runTests<Long>(instance, badNumbers, goodNumbers)
    }

    @Test
    @Throws(Exception::class)
    fun testNumberBetweenLongs()
    {
        val min = one(longs(java.lang.Long.MIN_VALUE, java.lang.Long.MAX_VALUE - 10L))
        val max = one(longs(min, java.lang.Long.MAX_VALUE))
        val instance = NumberAssertions.numberBetween(min, max)

        assertThat<AlchemyAssertion<Long>>(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val goodLong = one(longs(min, max))
        instance.check(goodLong)

        val numberBelowMin = min - one(positiveLongs())
        if (numberBelowMin < min)
        {
            assertThrows { instance.check(numberBelowMin) }.failedAssertion()
        }

        val numberAboveMax = max + one(positiveIntegers())
        if (numberAboveMax > max)
        {
            assertThrows { instance.check(numberAboveMax) }.failedAssertion()
        }
    }

    @Test
    @Throws(Exception::class)
    fun testNumberBetweenLongsEdgeCases()
    {
        val min = one(longs(java.lang.Long.MIN_VALUE, java.lang.Long.MAX_VALUE - 10L))
        val max = one(longs(min, java.lang.Long.MAX_VALUE))
        assertThrows { NumberAssertions.numberBetween(max, min) }.illegalArgument()
    }

    @Test
    fun testPositiveLong()
    {
        val instance = NumberAssertions.positiveLong()
        assertThat(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val goodNumbers = positiveLongs()
        val badNumbers = longs(java.lang.Long.MIN_VALUE, 0L)

        Tests.runTests(instance, badNumbers, goodNumbers)
    }

    @Test
    fun testNegativeLong()
    {
        val instance = NumberAssertions.negativeLong()
        checkThat(instance, notNullValue())

        val negative = one(longs(java.lang.Long.MIN_VALUE, 0))
        instance.check(negative)

        val positive = one(positiveLongs())
        assertThrows { instance.check(positive) }.failedAssertion()
        assertThrows { instance.check(null) }
                .failedAssertion()
    }

    //==============================
    //DOUBLE TESTS
    //==============================
    @Test
    fun testDoubleLessThan()
    {
        val upperBound = one(doubles(-10000.0, 100000.0))
        val instance = NumberAssertions.lessThan(upperBound)

        Tests.checkForNullCase(instance)

        var badNumbers = { upperBound + one(doubles(0.0, 10000.0)) }
        val goodNumbers = { upperBound - one(doubles(1.0, 100.0)) }
        Tests.runTests<Double>(instance, badNumbers, goodNumbers)

        badNumbers = { upperBound }
        Tests.runTests<Double>(instance, badNumbers, goodNumbers)
    }

    @DontRepeat
    @Test
    fun testDoubleLessThanEdgeCases()
    {
        assertThrows { NumberAssertions.lessThan(-java.lang.Double.MAX_VALUE) }
                .illegalArgument()
    }

    @Test
    fun testDoubleLessThanWithDelta()
    {
        val upperBound = one(doubles(-10000.0, 100000.0))
        val delta = one(doubles(1.0, 10.0))
        val instance = NumberAssertions.lessThan(upperBound, delta)

        Tests.checkForNullCase(instance)

        val badNumbers = doubles(upperBound + delta + 1.0, java.lang.Double.MAX_VALUE)
        val goodNumbers = doubles(-java.lang.Double.MAX_VALUE, upperBound - delta)
        Tests.runTests(instance, badNumbers, goodNumbers)
    }

    @Test
    fun testDoubleLessThanOrEqualTo()
    {
        val upperBound = one(doubles(0.0, java.lang.Double.MAX_VALUE / 2))
        val instance = NumberAssertions.lessThanOrEqualTo(upperBound)
        Tests.checkForNullCase(instance)

        val badNumbers = doubles(upperBound + 0.1, java.lang.Double.MAX_VALUE)
        val goodNumber = doubles(-java.lang.Double.MAX_VALUE, upperBound)
        Tests.runTests<Double>(instance, badNumbers, goodNumber)
    }

    @Test
    fun testDoubleLessThanOrEqualToWithDelta()
    {
        val upperBound = one(doubles(0.0, java.lang.Double.MAX_VALUE / 2))
        val delta = one(doubles(1.0, 100.0))
        val instance = NumberAssertions.lessThanOrEqualTo(upperBound, delta)
        Tests.checkForNullCase(instance)

        val badNumbers = doubles(upperBound + delta + 0.1, java.lang.Double.MAX_VALUE)
        val goodNumber = doubles(-java.lang.Double.MAX_VALUE, upperBound)
        Tests.runTests(instance, badNumbers, goodNumber)
    }

    @Test
    fun testDoubleGreaterThan()
    {
        val lowerBound = one(doubles(-100000.0, 100000.0))
        val instance = NumberAssertions.greaterThan(lowerBound)
        Tests.checkForNullCase(instance)

        val badNumbers = doubles(-java.lang.Double.MAX_VALUE, lowerBound)
        val goodNumbers = doubles(lowerBound + 0.1, java.lang.Double.MAX_VALUE)
        Tests.runTests<Double>(instance, badNumbers, goodNumbers)
    }

    @Test
    fun testDoubleGreaterThanEdgeCases()
    {
        assertThrows { NumberAssertions.greaterThan(java.lang.Double.MAX_VALUE) }
                .illegalArgument()
    }

    @Test
    fun testDoubleGreaterThanWithDelta()
    {
        val lowerBound = one(doubles(-100000.0, 100000.0))
        val delta = one(doubles(1.0, 100.0))
        val instance = NumberAssertions.greaterThan(lowerBound, delta)
        Tests.checkForNullCase(instance)

        val badNumbers = doubles(-java.lang.Double.MAX_VALUE, lowerBound - delta)
        val goodNumbers = doubles(lowerBound, java.lang.Double.MAX_VALUE)
        Tests.runTests(instance, badNumbers, goodNumbers)
    }


    @Test
    @Throws(Exception::class)
    fun testDoubleGreaterThanOrEqualTo()
    {
        val inclusiveLowerBound = one(doubles(-10000.0, 10000.0))
        val instance = NumberAssertions.greaterThanOrEqualTo(inclusiveLowerBound)

        assertThat<AlchemyAssertion<Double>>(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val goodArguments = doubles(inclusiveLowerBound, java.lang.Double.MAX_VALUE)
        val badArguments = doubles(-java.lang.Double.MAX_VALUE, inclusiveLowerBound)
        Tests.runTests<Double>(instance, badArguments, goodArguments)
    }

}
