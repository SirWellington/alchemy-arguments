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

import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions
import tech.sirwellington.alchemy.arguments.AssertionBuilderImpl.checkThat
import tech.sirwellington.alchemy.arguments.assertions.alphabeticString
import tech.sirwellington.alchemy.generator.StringGenerators
import tech.sirwellington.alchemy.generator.StringGenerators.Companion.alphanumericString
import tech.sirwellington.alchemy.generator.StringGenerators.Companion.alphabeticString
import tech.sirwellington.alchemy.generator.one
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
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
    private val assertion: AlchemyAssertion<*>

    @Mock
    private val exceptionMapper: ExceptionMapper<SQLException>

    private var argument: String
    private var arguments: MutableList<String>

    private var instance: AssertionBuilderImpl<String, FailedAssertionException>

    private var assertException: FailedAssertionException

    @Before
    fun setUp()
    {
        argument = one(alphabeticString())
        arguments = asList(argument!!)

        instance = AssertionBuilderImpl.checkThat(arguments)

        assertException = FailedAssertionException(one(StringGenerators.alphabeticString()))
    }

    @Test
    fun testCheckThat()
    {
        val mockArgument = mock(Any::class.java)
        instance = checkThat(arguments)
        assertThat<AssertionBuilderImpl<String, FailedAssertionException>>(instance, notNullValue())
        verifyZeroInteractions(mockArgument)

        instance = checkThat<String>(null)
        assertThat<AssertionBuilderImpl<String, FailedAssertionException>>(instance, notNullValue())
    }

    @Test
    fun testThrowingWhenExceptionIsNotWrapped()
    {
        whenever(exceptionMapper!!.apply(assertException))
                .thenReturn(SQLException(one(alphabeticString())))

        doThrow(assertException)
                .whenever<AlchemyAssertion>(assertion)
                .check(argument)

        assertThrows { instance!!.throwing(exceptionMapper).`is`(assertion) }
                .isInstanceOf(SQLException::class.java)

        verify(exceptionMapper).apply(assertException)
        verify<AlchemyAssertion>(assertion).check(argument)
    }

    @Test
    fun testThrowingWhenExceptionIsWrapped()
    {
        whenever(exceptionMapper!!.apply(assertException))
                .thenReturn(SQLException(one(alphabeticString()), assertException))

        doThrow(assertException)
                .whenever<AlchemyAssertion>(assertion)
                .check(argument)

        assertThrows { instance!!.throwing(exceptionMapper).`is`(assertion) }
                .isInstanceOf(SQLException::class.java)
                .hasCauseInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testThrowingExceptionClass()
    {
        whenever(exceptionMapper!!.apply(assertException))
                .thenReturn(SQLException())

        doThrow(assertException)
                .whenever<AlchemyAssertion>(assertion)
                .check(argument)

        assertThrows { instance!!.throwing(SQLException::class.java).`is`(assertion) }
                .isInstanceOf(SQLException::class.java)
                .hasCauseInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testIsWhenAssertionFails()
    {
        doThrow(assertException)
                .whenever<AlchemyAssertion>(assertion)
                .check(argument)

        assertThrows { instance!!.`is`(assertion) }
                .failedAssertion()
                .hasMessage(assertException!!.message)
    }

    @Test
    @Throws(Exception::class)
    fun testIsWhenAssertionPasses()
    {
        doNothing()
                .whenever<AlchemyAssertion>(assertion)
                .check(argument)

        instance!!.`is`(assertion)
        verify<AlchemyAssertion>(assertion).check(argument)
    }

    @Test
    fun testIsWhenAssertionThrowsUnexpectedException()
    {
        doThrow(RuntimeException())
                .whenever<AlchemyAssertion>(assertion)
                .check(argument)

        assertThrows { instance!!.`is`(assertion) }
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

        assertThrows { instance!!.`is`(assertion) }
                .failedAssertion()
                .hasMessage(embeddedExceptionMessage)

        assertThrows { instance!!.usingMessage(overrideMessage).`is`(assertion) }
                .failedAssertion()
                .hasMessage(overrideMessage)

    }

    @Test
    fun testChecksWithMultipleArguments()
    {
        arguments = listOf<String>(alphabeticString())
        //No Exceptions expected
        AssertionBuilderImpl.checkThat(arguments)
                .are(StringAssertions.nonEmptyString())

        arguments!!.add("")
        //Test 'is'
        assertThrows { AssertionBuilderImpl.checkThat(arguments).`is`(StringAssertions.nonEmptyString()) }
                .failedAssertion()

        //Test 'are' as well
        assertThrows { AssertionBuilderImpl.checkThat(arguments).are(StringAssertions.nonEmptyString()) }
                .failedAssertion()

    }

    @Test
    fun testOverrideMessagePreservedWithCustomException()
    {
        doThrow(FailedAssertionException::class.java)
                .whenever<AlchemyAssertion>(assertion)
                .check(argument)

        val overrideMessage = one(alphabeticString())

        val newInstance = instance!!.usingMessage(overrideMessage)
                .throwing(IOException::class.java)

        assertThrows { newInstance.`is`(assertion) }
                .isInstanceOf(IOException::class.java)
                .hasMessage(overrideMessage)
    }

    @Test
    fun testOverrideMessagePreservedWithCustomExceptionReversed()
    {
        doThrow(FailedAssertionException::class.java)
                .whenever<AlchemyAssertion>(assertion)
                .check(argument)

        val overrideMessage = one(alphabeticString())

        val newInstance = instance!!.throwing(IOException::class.java)
                .usingMessage(overrideMessage)

        assertThrows { newInstance.`is`(assertion) }
                .isInstanceOf(IOException::class.java)
                .hasMessage(overrideMessage)
    }
}
