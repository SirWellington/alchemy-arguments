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

import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions
import tech.sirwellington.alchemy.arguments.AssertionBuilderImpl.checkThat
import tech.sirwellington.alchemy.arguments.assertions.*
import tech.sirwellington.alchemy.generator.CollectionGenerators
import tech.sirwellington.alchemy.generator.StringGenerators.Companion.alphabeticString
import tech.sirwellington.alchemy.generator.one
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.GenerateString
import tech.sirwellington.alchemy.test.junit.runners.GenerateString.Type.ALPHABETIC
import tech.sirwellington.alchemy.test.junit.runners.Repeat
import java.io.IOException
import java.sql.SQLException
import java.util.Arrays.asList

/**

 * @author SirWellington
 */
@Repeat(100)
@RunWith(AlchemyTestRunner::class)
class AssertionBuilderImplTest
{

    @Mock
    private lateinit var assertion: AlchemyAssertion<Any>

    @Mock
    private lateinit var exceptionMapper: ExceptionMapper<SQLException>

    @GenerateString(ALPHABETIC)
    private lateinit var argument: String
    private lateinit var arguments: List<String>

    @GenerateString
    private lateinit var errorMessage: String

    private lateinit var instance: AssertionBuilderImpl<Any, FailedAssertionException>

    private lateinit var assertException: FailedAssertionException

    @Before
    fun setUp()
    {
        arguments = asList(argument)

        instance = AssertionBuilderImpl.checkThat(arguments.toList())

        assertException = FailedAssertionException(errorMessage)
    }

    @Test
    fun testCheckThat()
    {
        val mockArgument = mock(Any::class.java)
        instance = checkThat(arguments)
        assertThat(instance, notNullValue())
        verifyZeroInteractions(mockArgument)

        instance = checkThat(null)
        assertThat(instance, notNullValue())
    }

    @Test
    fun testThrowingWhenExceptionIsNotWrapped()
    {
        whenever(exceptionMapper.apply(assertException))
                .thenReturn(SQLException(errorMessage))

        doThrow(assertException)
                .whenever(assertion)
                .check(argument)

        assertThrows { instance.throwing(exceptionMapper).isA(assertion) }
                .isInstanceOf(SQLException::class.java)

        verify(exceptionMapper).apply(assertException)
        verify(assertion).check(argument)
    }

    @Test
    fun testThrowingWhenExceptionIsWrapped()
    {
        whenever(exceptionMapper.apply(assertException))
                .thenReturn(SQLException(errorMessage, assertException))

        doThrow(assertException)
                .whenever(assertion)
                .check(argument)

        assertThrows { instance.throwing(exceptionMapper).isA(assertion) }
                .isInstanceOf(SQLException::class.java)
                .hasCauseInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testThrowingExceptionClass()
    {
        whenever(exceptionMapper.apply(assertException))
                .thenReturn(SQLException())

        doThrow(assertException)
                .whenever(assertion)
                .check(argument)

        assertThrows { instance.throwing(SQLException::class.java).isA(assertion) }
                .isInstanceOf(SQLException::class.java)
                .hasCauseInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testIsWhenAssertionFails()
    {
        doThrow(assertException)
                .whenever(assertion)
                .check(argument)

        assertThrows { instance.isA(assertion) }
                .failedAssertion()
                .hasMessage(assertException.message)
    }

    @Test
    @Throws(Exception::class)
    fun testIsWhenAssertionPasses()
    {
        doNothing()
                .whenever(assertion)
                .check(argument)

        instance.isA(assertion)
        verify(assertion).check(argument)
    }

    @Test
    fun testIsWhenAssertionThrowsUnexpectedException()
    {
        doThrow(RuntimeException())
                .whenever(assertion)
                .check(argument)

        assertThrows { instance.isA(assertion) }
                .failedAssertion()
                .hasCauseInstanceOf(RuntimeException::class.java)

    }

    @Test
    fun testUsingMessage()
    {
        val embeddedExceptionMessage = one(alphabeticString())
        val overrideMessage = one(alphabeticString())

        doThrow(FailedAssertionException(embeddedExceptionMessage))
                .whenever(assertion)
                .check(argument)

        assertThrows { instance.isA(assertion) }
                .failedAssertion()
                .hasMessage(embeddedExceptionMessage)

        assertThrows { instance.usingMessage(overrideMessage).isA(assertion) }
                .failedAssertion()
                .hasMessage(overrideMessage)

    }

    @Test
    fun testChecksWithMultipleArguments()
    {
        val arguments = CollectionGenerators.listOf(alphabeticString()).toMutableList()
        //No Exceptions expected
        AssertionBuilderImpl.checkThat(arguments)
                .are(nonEmptyString())

        arguments.add("")
        //Test 'is'
        assertThrows { AssertionBuilderImpl.checkThat(arguments).isA(nonEmptyString()) }
                .failedAssertion()

        //Test 'are' as well
        assertThrows { AssertionBuilderImpl.checkThat(arguments).are(nonEmptyString()) }
                .failedAssertion()

    }

    @Test
    fun testOverrideMessagePreservedWithCustomException()
    {
        doThrow(FailedAssertionException::class)
                .whenever(assertion)
                .check(argument)

        val overrideMessage = one(alphabeticString())

        val newInstance = instance.usingMessage(overrideMessage)
                .throwing(IOException::class.java)

        assertThrows { newInstance.isA(assertion) }
                .isInstanceOf(IOException::class.java)
                .hasMessage(overrideMessage)
    }

    @Test
    fun testOverrideMessagePreservedWithCustomExceptionReversed()
    {
        doThrow(FailedAssertionException::class)
                .whenever(assertion)
                .check(argument)

        val overrideMessage = one(alphabeticString())

        val newInstance = instance.throwing(IOException::class.java)
                .usingMessage(overrideMessage)

        assertThrows { newInstance.isA(assertion) }
                .isInstanceOf(IOException::class.java)
                .hasMessage(overrideMessage)
    }
}
