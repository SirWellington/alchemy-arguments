/*
 * Copyright © 2019. Sir Wellington.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.sirwellington.alchemy.arguments.assertions

import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.verifyZeroInteractions
import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.FailedAssertionException
import tech.sirwellington.alchemy.arguments.failedAssertion
import tech.sirwellington.alchemy.generator.StringGenerators.Companion.strings
import tech.sirwellington.alchemy.generator.one
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.*

/**

 * @author SirWellington
 */
@RunWith(AlchemyTestRunner::class)
@Repeat
class AssertionsTest
{

    @GenerateString
    private lateinit var string: String

    @GenerateInteger
    private var positiveInt: Int = 0

    @GenerateLong
    private var positiveLong: Long = 0

    @GenerateDouble
    private var positiveDouble: Double = 0.0

    @Before
    fun setUp()
    {
    }

    @DontRepeat
    @Test
    @Throws(Exception::class)
    fun testNotNull()
    {

        val instance = notNull<Any>()

        assertThat(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val mock = mock<Any>()
        instance.check(mock)
        verifyZeroInteractions(mock)
    }

    @DontRepeat
    @Test
    fun testNonNullReference()
    {
        val instance = nonNullReference<Any>()
        assertThat(instance, notNullValue())

        Tests.checkForNullCase(instance)

        val mock = mock<Any>()
        instance.check(mock)
        verifyZeroInteractions(mock)

        assertThrows { instance.check(null) }.failedAssertion()
    }

    @DontRepeat
    @Test
    fun testNullObject()
    {
        val instance = nullObject<Any>()
        assertThat(instance, notNullValue())

        instance.check(null)

        assertThrows { instance.check(string) }.failedAssertion()
    }

    @Test
    fun testSameInstanceAs()
    {
        val instanceOne = sameInstanceAs<Any?>(null)

        //null is the same instance as null
        assertThat(instanceOne, notNullValue())
        instanceOne.check(null)

        //null is not the same instance as any other non-null object
        assertThrows { instanceOne.check("") }.failedAssertion()

        val someObject = Any()
        val instanceTwo = sameInstanceAs(someObject)
        instanceTwo.check(someObject)

        val differentObject = Any()
        assertThrows { instanceTwo.check(differentObject) }.failedAssertion()
    }

    @Test
    fun testInstanceOf()
    {

        val instance = instanceOf<Any>(Number::class.java)

        instance.check(positiveInt)
        instance.check(positiveLong)
        instance.check(positiveDouble)

        assertThrows { instance.check(string) }.failedAssertion()
    }

    @Test
    fun testInstanceOfEdgeCases()
    {
        val assertion = instanceOf<Any>(Number::class.java)
        assertThrows { assertion.check(null) }.failedAssertion()
    }

    @Test
    fun testNot()
    {
        val assertion = mock<AlchemyAssertion<Any>>()

        doThrow(FailedAssertionException())
                .whenever(assertion)
                .check(ArgumentMatchers.any())

        val instance = not(assertion)

        instance.check("")

        doNothing()
                .whenever(assertion)
                .check(ArgumentMatchers.any())

        assertThrows { instance.check("") }.failedAssertion()
    }

    @Test
    fun testNotEdgeCases()
    {
        assertThrows { not<Any>(null!!) }
    }

    @Test
    fun testEqualTo()
    {
        val first = one(strings())
        var second = ""

        do
        {
            second = one(strings())
        } while (first == second)

        val instance = equalTo(second)

        //Check against self should be ok;
        instance.check(second)
        instance.check("" + second)

        assertThrows { instance.check(first) }.failedAssertion()

    }

}
