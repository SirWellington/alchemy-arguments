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
import tech.sirwellington.alchemy.arguments.failedAssertion
import tech.sirwellington.alchemy.generator.DateGenerators
import tech.sirwellington.alchemy.generator.one
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat
import tech.sirwellington.alchemy.test.junit.runners.Repeat
import java.util.*

/**

 * @author SirWellington
 */
@RunWith(AlchemyTestRunner::class)
@Repeat
class DateAssertionsTest
{

    @Before
    fun setUp()
    {
    }

    @DontRepeat
    @Test
    fun testCannotInstantiate()
    {
        assertThrows { DateAssertions() }
                .isInstanceOf(IllegalAccessException::class.java)

        assertThrows { DateAssertions::class.java.newInstance() }
                .isInstanceOf(IllegalAccessException::class.java)
    }

    @Test
    @Throws(InterruptedException::class)
    fun testInThePast()
    {
        val startTime = Date()

        val instance = DateAssertions.inThePast()
        assertThat(instance, notNullValue())

        val past = one(DateGenerators.pastDates())
        instance.check(past)

        val future = one(DateGenerators.futureDates())
        assertThrows { instance.check(future) }
                .failedAssertion()

        Thread.sleep(1)
        //Start time should presentDate be past as well
        instance.check(startTime)
    }

    @Test
    fun testAfter()
    {
        val referenceDate = Date()

        val instance = DateAssertions.after(referenceDate)
        assertThat(instance, notNullValue())

        //The reference date is not after itself
        assertThrows { instance.check(referenceDate) }
                .failedAssertion()

        //The past is not after the reference date
        val past = one(DateGenerators.pastDates())
        assertThrows { instance.check(past) }
                .failedAssertion()

        //The future should be after the reference date
        val future = one(DateGenerators.futureDates())
        instance.check(future)

    }

    @Test
    fun testBefore()
    {
        val referenceDate = Date()

        val instance = DateAssertions.before(referenceDate)
        assertThat(instance, notNullValue())

        //The reference date is not before itself
        assertThrows { instance.check(referenceDate) }
                .failedAssertion()

        //The future is not before the reference date
        val future = one(DateGenerators.after(referenceDate))
        assertThrows { instance.check(future) }
                .failedAssertion()

        //The past is before the reference date
        val past = one(DateGenerators.before(referenceDate))
        instance.check(past)

    }

    @Test
    fun testInTheFuture()
    {
        val startTime = Date()

        val instance = DateAssertions.inTheFuture()
        assertThat(instance, notNullValue())

        //Future is ahead of the presentDate
        val future = one(DateGenerators.futureDates())
        instance.check(future)

        //Past is behind the presentDate
        val past = one(DateGenerators.pastDates())
        assertThrows { instance.check(past) }
                .isInstanceOf(IllegalArgumentException::class.java)

        //The start time is not in the future
        assertThrows { instance.check(startTime) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

}
