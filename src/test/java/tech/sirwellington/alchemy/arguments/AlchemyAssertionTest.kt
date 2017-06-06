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
package tech.sirwellington.alchemy.arguments

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Spy
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner

import java.util.Arrays.asList
import org.mockito.Mockito.never
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import tech.sirwellington.alchemy.generator.AlchemyGenerator.one
import tech.sirwellington.alchemy.generator.CollectionGenerators.listOf
import tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows

/**

 * @author SirWellington
 */
@RunWith(AlchemyTestRunner::class)
class AlchemyAssertionTest
{

    @Spy
    private val first: FakeAssertion<Any>? = null

    private var otherAssertions: Array<FakeAssertion<*>>? = null

    private var argument: Any? = null

    @Before
    fun setUp()
    {
        val assertions = listOf<FakeAssertion<Any>>({ spy(FakeAssertion<*>::class.java) })

        otherAssertions = assertions.toTypedArray<FakeAssertion<*>>()
        argument = one(alphabeticString())
    }

    @Test
    fun testOther()
    {
        var assertion: AlchemyAssertion<Any> = first

        for (element in otherAssertions!!)
        {
            assertion = assertion.and(element)
        }

        assertion.check(argument)
        verify<FakeAssertion<Any>>(first).check(argument)
        asList<FakeAssertion>(*otherAssertions!!).forEach { a -> verify<FakeAssertion>(a).check(argument) }
    }

    @Test
    fun testOtherWithBadArgs()
    {
        assertThrows { first!!.and(null) }
    }

    @Test
    fun testCombineWithMultiple()
    {
        val multipleAssertions = AlchemyAssertion.combine<Any>(first, *otherAssertions)

        multipleAssertions.check(argument)

        verify<FakeAssertion<Any>>(first).check(argument)

        asList<FakeAssertion>(*otherAssertions!!)
                .forEach { a -> verify<FakeAssertion>(a).check(argument) }
    }

    @Test
    fun testCombineWithMultipleWithBadArgs()
    {
        assertThrows { AlchemyAssertion.combine(null, *null as Array<AlchemyAssertion<*>>?) }
        assertThrows { AlchemyAssertion.combine<Any>(first, *null as Array<AlchemyAssertion<*>>?) }

    }

    @Test
    fun testCombineWithSingle()
    {
        val multipleAssertions = AlchemyAssertion.combine(first)
        multipleAssertions.check(argument)

        verify<FakeAssertion<Any>>(first).check(argument)
        asList<FakeAssertion>(*otherAssertions!!)
                .forEach { a -> verify<FakeAssertion>(a, never()).check(argument) }
    }

    internal class FakeAssertion<T> : AlchemyAssertion<T>
    {

        @Throws(FailedAssertionException::class)
        override fun check(argument: T)
        {
            //ok
        }

    }

}
