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

import java.util.Objects
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.FailedAssertionException
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat
import tech.sirwellington.alchemy.test.junit.runners.Repeat

import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat
import org.mockito.Matchers.any
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.mock
import org.mockito.Mockito.verifyZeroInteractions
import tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull
import tech.sirwellington.alchemy.generator.AlchemyGenerator.one
import tech.sirwellington.alchemy.generator.NumberGenerators.positiveDoubles
import tech.sirwellington.alchemy.generator.NumberGenerators.positiveIntegers
import tech.sirwellington.alchemy.generator.NumberGenerators.positiveLongs
import tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString
import tech.sirwellington.alchemy.generator.StringGenerators.strings
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows

/**

 * @author SirWellington
 */
@RunWith(AlchemyTestRunner::class)
@Repeat
class AssertionsTest
{

    @Before
    fun setUp()
    {
    }

    @DontRepeat
    @Test
    fun testCannotInstantiateClass()
    {
        assertThrows { Assertions::class.java.newInstance() }

        assertThrows { Assertions() }
                .isInstanceOf(IllegalAccessException::class.java)
    }

    @DontRepeat
    @Test
    @Throws(Exception::class)
    fun testNotNull()
    {

        val instance = Companion.notNull()
        assertThat<AlchemyAssertion<Any>>(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val mock = mock(Any::class.java)
        instance.check(mock)
        verifyZeroInteractions(mock)
    }

    @Test
    fun testNullObject()
    {
        val instance = Assertions.nullObject<Any>()
        assertThat(instance, notNullValue())

        instance.check(null)

        val string = one(strings())
        assertThrows { instance.check(string) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testSameInstanceAs()
    {
        val instanceOne = Assertions.sameInstanceAs<Any>(null)

        //null is the same instance as null
        assertThat(instanceOne, notNullValue())
        instanceOne.check(null)

        //null is not the same instance as any other non-null object
        assertThrows { instanceOne.check("") }
                .isInstanceOf(FailedAssertionException::class.java)

        val someObject = Any()
        val instanceTwo = Assertions.sameInstanceAs<Any>(someObject)
        instanceTwo.check(someObject)

        val differentObject = Any()
        assertThrows { instanceTwo.check(differentObject) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testInstanceOf()
    {

        val instance = Assertions.instanceOf<Any>(Number::class.java)

        instance.check(one(positiveIntegers()))
        instance.check(one(positiveLongs()))
        instance.check(one(positiveDoubles()))

        assertThrows { instance.check(one(alphabeticString())) }
    }

    @Test
    fun testInstanceOfEdgeCases()
    {
        assertThrows { Assertions.instanceOf<Any>(null!!) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun testNot()
    {
        val assertion = mock(AlchemyAssertion<*>::class.java)
        doThrow(FailedAssertionException())
                .`when`<AlchemyAssertion<Any>>(assertion)
                .check(ArgumentMatchers.any())

        val instance = Assertions.not<Any>(assertion)

        instance.check("")

        doNothing()
                .`when`<AlchemyAssertion<Any>>(assertion)
                .check(ArgumentMatchers.any())

        assertThrows { instance.check("") }
                .isInstanceOf(FailedAssertionException::class.java)

    }

    @Test
    fun testNotEdgeCases()
    {
        assertThrows { Assertions.not<Any>(null!!) }
                .isInstanceOf(IllegalArgumentException::class.java)
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

        val instance = Assertions.equalTo(second)

        //Check against self should be ok;
        instance.check(second)
        instance.check("" + second)

        assertThrows { instance.check(first) }
                .isInstanceOf(FailedAssertionException::class.java)

    }

}
