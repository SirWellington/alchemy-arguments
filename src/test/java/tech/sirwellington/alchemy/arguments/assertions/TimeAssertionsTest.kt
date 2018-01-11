/*
 * Copyright © 2018. Sir Wellington.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
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
import tech.sirwellington.alchemy.arguments.failedAssertion
import tech.sirwellington.alchemy.arguments.illegalArgument
import tech.sirwellington.alchemy.arguments.nullPointer
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.longs
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.negativeIntegers
import tech.sirwellington.alchemy.generator.TimeGenerators.Companion.anytime
import tech.sirwellington.alchemy.generator.TimeGenerators.Companion.futureInstants
import tech.sirwellington.alchemy.generator.TimeGenerators.Companion.pastInstants
import tech.sirwellington.alchemy.generator.one
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat
import tech.sirwellington.alchemy.test.junit.runners.Repeat
import java.time.Instant

/**

 * @author SirWellington
 */
@Repeat
@RunWith(AlchemyTestRunner::class)
class TimeAssertionsTest
{

    @Before
    fun setUp()
    {
    }

    @Test
    @Throws(InterruptedException::class)
    fun testInThePast()
    {
        val startTime = Instant.now()

        val instance = inThePast()
        assertThat(instance, notNullValue())

        // The past is indeed in the past
        val past = pastInstants().get()
        instance.check(past)

        //The recent past should be fine too
        val recentPast = Instant.now().minusMillis(1)
        instance.check(recentPast)

        //The futureInstants is not in the pastInstants
        val future = futureInstants().get()
        assertThrows { instance.check(future) }.failedAssertion()
        Thread.sleep(1)
        //The start time should now be in the past
        instance.check(startTime)

    }

    @Test
    fun testBefore()
    {
        val startTime = Instant.now()

        val instance = before(startTime)
        assertThat(instance, notNullValue())

        //The start time is not before itself
        assertThrows { instance.check(startTime) }.failedAssertion()
        //The past is before the present
        val past = one(pastInstants())
        instance.check(past)

        //The future is not before the present
        val future = one(futureInstants())
        assertThrows { instance.check(future) }.failedAssertion()
        //Edge case
        assertThrows { before(null!!) }.nullPointer()
    }

    @Test
    fun testInTheFuture()
    {
        val startTime = Instant.now()

        val instance = inTheFuture()
        assertThat(instance, notNullValue())

        //The start time is not in the future
        assertThrows { instance.check(startTime) }.failedAssertion()
        //The future is indeed in the future
        val future = one(futureInstants())
        instance.check(future)

        //The past is not in the future
        val past = one(pastInstants())
        assertThrows { instance.check(past) }.failedAssertion()
    }

    @Test
    fun testAfter()
    {
        val startTime = Instant.now()

        val instance = after(startTime)
        assertThat(instance, notNullValue())

        //The start time is not after itself
        assertThrows { instance.check(startTime) }.failedAssertion()
        //The future is indeed after the start time
        val future = one(futureInstants())
        instance.check(future)

        //The past is not after the start time
        val past = one(pastInstants())
        assertThrows { instance.check(past) }.failedAssertion()
        //Edge case
        assertThrows { after(null!!) }.nullPointer()
    }

    @Test
    fun testRightNow()
    {
        val assertion = rightNow()
        assertThat(assertion, notNullValue())

        assertion.check(Instant.now())

        val recentPast = Instant.now().minusMillis(50)
        assertThrows { assertion.check(recentPast) }.failedAssertion()

        val past = one(pastInstants())
        assertThrows { assertion.check(past) }.failedAssertion()

        val future = one(futureInstants())
        assertThrows { assertion.check(future) }
                .failedAssertion()
    }

    @Test
    fun testNowWithinDelta()
    {
        val marginOfErrorInMillis = 10L

        val assertion = nowWithinDelta(marginOfErrorInMillis)
        assertThat(assertion, notNullValue())

        assertion.check(Instant.now())

        val past = one(pastInstants())
        assertThrows { assertion.check(past) }.failedAssertion()

        val future = one(futureInstants())
        assertThrows { assertion.check(future) }
                .failedAssertion()
    }

    @DontRepeat
    @Test
    fun testNowWithinDeltaWithBadArguments()
    {
        val negative = one(negativeIntegers())
        assertThrows { nowWithinDelta(negative.toLong()) }
                .illegalArgument()
    }

    @Test
    @Throws(Exception::class)
    fun testEqualToInstantWithinDelta()
    {
        val delta = one(longs(1000, 100000))

        val instant = one(anytime())

        val assertion = equalToInstantWithinDelta(instant, delta)
        assertThat(assertion, notNullValue())

        var argument = instant.plusMillis(delta)
        assertion.check(argument)

        argument = instant.minusMillis(delta)
        assertion.check(argument)
    }

    @Test
    @Throws(Exception::class)
    fun testEqualToInstantWithinDeltaWhenDifferenceExceeded()
    {
        val delta = one(longs(1000, 1000000))
        val instant = one(anytime())

        val assertion = equalToInstantWithinDelta(instant, delta)
        assertThat(assertion, notNullValue())

        val argumentAfter = instant.plusMillis(delta + 10)
        assertThrows { assertion.check(argumentAfter) }.failedAssertion()

        val argumentBefore = instant.minusMillis(delta + 10)
        assertThrows { assertion.check(argumentBefore) }.failedAssertion()
    }

    @DontRepeat
    @Test
    @Throws(Exception::class)
    fun testEqualToInstantWithinDeltaWithBadArgs()
    {
        assertThrows { equalToInstantWithinDelta(null!!, 10) }.nullPointer()

        //Check with null argument
        val assertion = equalToInstantWithinDelta(Instant.now(), 10)
        assertThrows { assertion.check(null) }.failedAssertion()
    }

    @Test
    fun testEpochRightNow()
    {
        val assertion = epochRightNow()
        assertThat(assertion, notNullValue())

        assertion.check(Instant.now().toEpochMilli())

        val past = one(pastInstants())
        assertThrows { assertion.check(past.toEpochMilli()) }.failedAssertion()

        val future = one(futureInstants())
        assertThrows { assertion.check(future.toEpochMilli()) }.failedAssertion()
    }

    @Test
    fun testEpochNowWithinDelta()
    {
        val marginOfErrorInMillis = 10L

        val assertion = epochNowWithinDelta(marginOfErrorInMillis)
        assertThat(assertion, notNullValue())

        assertion.check(Instant.now().toEpochMilli())

        val past = one(pastInstants())
        assertThrows { assertion.check(past.toEpochMilli()) }.failedAssertion()

        val future = one(futureInstants())
        assertThrows { assertion.check(future.toEpochMilli()) }.failedAssertion()
    }

    @DontRepeat
    @Test
    fun testEpochNowWithinDeltaBadArguments()
    {
        val negative = one(negativeIntegers())
        assertThrows { epochNowWithinDelta(negative.toLong()) }.illegalArgument()
    }

}
