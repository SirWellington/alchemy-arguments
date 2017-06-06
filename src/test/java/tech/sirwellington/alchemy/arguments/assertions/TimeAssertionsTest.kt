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
import tech.sirwellington.alchemy.arguments.FailedAssertionException
import tech.sirwellington.alchemy.generator.TimeGenerators
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

    @DontRepeat
    @Test
    fun testCannotInstantiate()
    {
        assertThrows { TimeAssertions() }
                .isInstanceOf(IllegalAccessException::class.java)

        assertThrows { TimeAssertions::class.java.newInstance() }
                .isInstanceOf(IllegalAccessException::class.java)
    }

    @Test
    @Throws(InterruptedException::class)
    fun testInThePast()
    {
        val startTime = Instant.now()

        val instance = TimeAssertions.inThePast()
        assertThat(instance, notNullValue())

        // The past is indeed in the past
        val past = TimeGenerators.pastInstants().get()
        instance.check(past)

        //The recent past should be fine too
        val recentPast = Instant.now().minusMillis(1)
        instance.check(recentPast)

        //The futureInstants is not in the pastInstants
        val future = TimeGenerators.futureInstants().get()
        assertThrows { instance.check(future) }
                .isInstanceOf(FailedAssertionException::class.java)

        Thread.sleep(1)
        //The start time should now be in the past
        instance.check(startTime)

    }

    @Test
    fun testBefore()
    {
        val startTime = Instant.now()

        val instance = TimeAssertions.before(startTime)
        assertThat(instance, notNullValue())

        //The start time is not before itself
        assertThrows { instance.check(startTime) }
                .isInstanceOf(FailedAssertionException::class.java)

        //The past is before the present
        val past = one(TimeGenerators.pastInstants())
        instance.check(past)

        //The future is not before the present
        val future = one(TimeGenerators.futureInstants())
        assertThrows { instance.check(future) }
                .isInstanceOf(FailedAssertionException::class.java)

        //Edge case
        assertThrows { TimeAssertions.before(null!!) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun testInTheFuture()
    {
        val startTime = Instant.now()

        val instance = TimeAssertions.inTheFuture()
        assertThat(instance, notNullValue())

        //The start time is not in the future
        assertThrows { instance.check(startTime) }
                .isInstanceOf(FailedAssertionException::class.java)

        //The future is indeed in the future
        val future = one(TimeGenerators.futureInstants())
        instance.check(future)

        //The past is not in the future
        val past = one(TimeGenerators.pastInstants())
        assertThrows { instance.check(past) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testAfter()
    {
        val startTime = Instant.now()

        val instance = TimeAssertions.after(startTime)
        assertThat(instance, notNullValue())

        //The start time is not after itself
        assertThrows { instance.check(startTime) }
                .isInstanceOf(FailedAssertionException::class.java)

        //The future is indeed after the start time
        val future = one(TimeGenerators.futureInstants())
        instance.check(future)

        //The past is not after the start time
        val past = one(TimeGenerators.pastInstants())
        assertThrows { instance.check(past) }
                .isInstanceOf(FailedAssertionException::class.java)

        //Edge case
        assertThrows { TimeAssertions.after(null!!) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun testRightNow()
    {
        val assertion = TimeAssertions.rightNow()
        assertThat(assertion, notNullValue())

        assertion.check(Instant.now())

        val recentPast = Instant.now().minusMillis(50)
        assertThrows { assertion.check(recentPast) }
                .isInstanceOf(FailedAssertionException::class.java)


        val past = one(pastInstants())
        assertThrows { assertion.check(past) }
                .isInstanceOf(FailedAssertionException::class.java)

        val future = one(futureInstants())
        assertThrows { assertion.check(future) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testNowWithinDelta()
    {
        val marginOfErrorInMillis = 10L

        val assertion = TimeAssertions.nowWithinDelta(marginOfErrorInMillis)
        assertThat(assertion, notNullValue())

        assertion.check(Instant.now())

        val past = one(pastInstants())
        assertThrows { assertion.check(past) }
                .isInstanceOf(FailedAssertionException::class.java)

        val future = one(futureInstants())
        assertThrows { assertion.check(future) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @DontRepeat
    @Test
    fun testNowWithinDeltaWithBadArguments()
    {
        val negative = one(negativeIntegers())
        assertThrows { TimeAssertions.nowWithinDelta(negative.toLong()) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testEqualToInstantWithinDelta()
    {
        val delta = one(longs(1000, 100000))

        val instant = one(anytime())

        val assertion = TimeAssertions.equalToInstantWithinDelta(instant, delta)
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

        val assertion = TimeAssertions.equalToInstantWithinDelta(instant, delta)
        assertThat(assertion, notNullValue())

        val argumentAfter = instant.plusMillis(delta + 10)
        assertThrows { assertion.check(argumentAfter) }.isInstanceOf(FailedAssertionException::class.java)

        val argumentBefore = instant.minusMillis(delta + 10)
        assertThrows { assertion.check(argumentBefore) }.isInstanceOf(FailedAssertionException::class.java)
    }

    @DontRepeat
    @Test
    @Throws(Exception::class)
    fun testEqualToInstantWithinDeltaWithBadArgs()
    {
        assertThrows { TimeAssertions.equalToInstantWithinDelta(null!!, 10) }
                .isInstanceOf(IllegalArgumentException::class.java)

        //Check with null argument
        val assertion = TimeAssertions.equalToInstantWithinDelta(Instant.now(), 10)
        assertThrows { assertion.check(null) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testEpochRightNow()
    {
        val assertion = TimeAssertions.epochRightNow()
        assertThat(assertion, notNullValue())

        assertion.check(Instant.now().toEpochMilli())

        val past = one(pastInstants())
        assertThrows { assertion.check(past.toEpochMilli()) }
                .isInstanceOf(FailedAssertionException::class.java)

        val future = one(futureInstants())
        assertThrows { assertion.check(future.toEpochMilli()) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testEpochNowWithinDelta()
    {
        val marginOfErrorInMillis = 10L

        val assertion = TimeAssertions.epochNowWithinDelta(marginOfErrorInMillis)
        assertThat(assertion, notNullValue())

        assertion.check(Instant.now().toEpochMilli())

        val past = one(pastInstants())
        assertThrows { assertion.check(past.toEpochMilli()) }
                .isInstanceOf(FailedAssertionException::class.java)

        val future = one(futureInstants())
        assertThrows { assertion.check(future.toEpochMilli()) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @DontRepeat
    @Test
    fun testEpochNowWithinDeltaBadArguments()
    {
        val negative = one(negativeIntegers())
        assertThrows { TimeAssertions.epochNowWithinDelta(negative.toLong()) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

}
