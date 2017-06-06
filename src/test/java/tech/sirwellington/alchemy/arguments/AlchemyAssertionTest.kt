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

import com.nhaarman.mockito_kotlin.spy
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.never
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.Spy
import tech.sirwellington.alchemy.arguments.assertions.*
import tech.sirwellington.alchemy.generator.AlchemyGenerator
import tech.sirwellington.alchemy.generator.CollectionGenerators
import tech.sirwellington.alchemy.generator.StringGenerators
import tech.sirwellington.alchemy.generator.one
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.GenerateString
import java.util.Arrays.asList

/**

 * @author SirWellington
 */
@RunWith(AlchemyTestRunner::class)
class AlchemyAssertionTest
{

    @Spy
    private lateinit var first: FakeAssertion<Any>

    private lateinit var otherAssertions: Array<FakeAssertion<Any>>

    @GenerateString
    private lateinit var argument: String

    @Before
    fun setUp()
    {
        val generator = AlchemyGenerator { spy<FakeAssertion<Any>>() }
        val assertions = CollectionGenerators.listOf(generator)

        otherAssertions = assertions.toTypedArray()
    }

    @Test
    fun testOther()
    {
        var assertion: AlchemyAssertion<Any> = first

        for (element in otherAssertions)
        {
            assertion = assertion.and(element)
        }

        assertion.check(argument)
        verify<FakeAssertion<Any>>(first).check(argument)
        asList(*otherAssertions).forEach { a -> verify(a).check(argument) }
    }

    @Test
    fun testOtherWithBadArgs()
    {
        assertThrows { first.and(null!!) }
    }

    @Test
    fun testCombineWithMultiple()
    {
        val multipleAssertions = combine(first, *otherAssertions)

        multipleAssertions.check(argument)

        verify<FakeAssertion<Any>>(first).check(argument)

        asList(*otherAssertions)
                .forEach { a -> verify(a).check(argument) }
    }

    @Test
    fun testCombineWithMultipleWithBadArgs()
    {
        assertThrows { combine(null!!, this.first) }

    }

    @Test
    fun testCombineWithSingle()
    {
        val multipleAssertions = combine(first)
        multipleAssertions.check(argument)

        verify<FakeAssertion<Any>>(first).check(argument)
        asList(*otherAssertions)
                .forEach { a -> verify(a, never()).check(argument) }
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
