/*
 * Copyright 2017 SirWellington Tech.
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

import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.isEmptyOrNullString
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.notNullValue
import org.hamcrest.Matchers.nullValue
import org.hamcrest.Matchers.sameInstance
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.annotations.access.Internal
import tech.sirwellington.alchemy.generator.StringGenerators
import tech.sirwellington.alchemy.generator.one
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.GenerateString
import tech.sirwellington.alchemy.test.junit.runners.GenerateString.Type.ALPHABETIC
import tech.sirwellington.alchemy.test.junit.runners.Repeat

/**

 * @author SirWellington
 */
@Repeat(25)
@RunWith(AlchemyTestRunner::class)
class DynamicExceptionSupplierTest
{

    private lateinit var exceptionClass: Class<FakeExceptionWithMessage>

    @GenerateString(ALPHABETIC)
    private lateinit var overrideMessage: String

    private lateinit var instance: DynamicExceptionSupplier<FakeExceptionWithMessage>

    private lateinit var assertionException: FailedAssertionException

    @Before
    fun setUp()
    {
        assertionException = FailedAssertionException(one(StringGenerators.alphabeticStrings()))
        exceptionClass = FakeExceptionWithMessage::class.java

        instance = DynamicExceptionSupplier(exceptionClass, overrideMessage)
    }

    @Test
    fun testApplyWithNoMessageOrCause()
    {
        var instance = DynamicExceptionSupplier(FakeException::class.java, null)

        var result = instance.apply(assertionException)
        assertThat(result, notNullValue())
        assertThat(result.cause, nullValue())
        assertThat(result.message, isEmptyOrNullString())

        instance = DynamicExceptionSupplier(FakeException::class.java, overrideMessage)
        result = instance.apply(assertionException)
        assertThat(result, notNullValue())
        assertThat(result.message, isEmptyOrNullString())
        assertThat(result.cause, nullValue())
    }

    @Test
    fun testApplyWithMessage()
    {
        var result = instance.apply(assertionException)
        assertThat(result, notNullValue())
        assertThat(result.cause, nullValue())
        assertThat(result.message, equalTo(overrideMessage))

        result = instance.apply(null)
        assertThat(result, notNullValue())
        assertThat(result.cause, nullValue())
        assertThat(result.message, equalTo(overrideMessage))

        instance = DynamicExceptionSupplier(FakeExceptionWithMessage::class.java, null)
        result = instance.apply(assertionException)
        assertThat(result, notNullValue())
        assertThat(result.cause, nullValue())
        assertThat(result.message, equalTo(assertionException.message))

    }

    @Test
    fun testApplyWithCause()
    {
        var instance = DynamicExceptionSupplier(FakeExceptionWithThrowable::class.java, overrideMessage)

        var result = instance.apply(assertionException)
        assertThat(result, notNullValue())
        assertThat(result.cause, notNullValue())
        assertThat(result.cause, instanceOf(FailedAssertionException::class.java))

        result = instance.apply(null)
        assertThat(result, notNullValue())
        assertThat(result.cause, nullValue())
        assertThat(result.message, isEmptyOrNullString())

        instance = DynamicExceptionSupplier(FakeExceptionWithThrowable::class.java, null)

        result = instance.apply(assertionException)
        assertThat(result, notNullValue())
        assertThat<Throwable>(result.cause, equalTo(assertionException))

    }

    @Test
    fun testApplyWithMessageAndCause()
    {
        var instance = DynamicExceptionSupplier(FakeExceptionWithBoth::class.java, overrideMessage)

        var result = instance.apply(assertionException)
        assertThat(result, notNullValue())
        assertThat(result.message, equalTo(overrideMessage))
        assertThat(result.cause, notNullValue())
        assertThat(result.cause, instanceOf(FailedAssertionException::class.java))

        instance = DynamicExceptionSupplier(FakeExceptionWithBoth::class.java, null)

        result = instance.apply(assertionException)
        assertThat(result, notNullValue())
        assertThat(result.cause, notNullValue())
        assertThat(result.cause, instanceOf(FailedAssertionException::class.java))

    }

    @Test
    @Throws(Exception::class)
    fun testApplyWithCauseButNoMessageConstructor()
    {
        val expectedMessage = assertionException.message

        val instance = DynamicExceptionSupplier(FakeExceptionWithMessage::class.java, "")

        val result = instance.apply(assertionException)

        assertThat(result, notNullValue())
        assertThat(result.message, equalTo(expectedMessage))
        assertThat(result.cause, nullValue())
    }

    @Test
    @Throws(Exception::class)
    fun testWhenInvocationFails()
    {
        val instance = DynamicExceptionSupplier(FakeExceptionThatThrowsOnConstruct::class.java, overrideMessage)

        val result = instance.apply(assertionException)
        assertThat(result, nullValue())
    }

    @Test
    fun testGetExceptionClass()
    {
        val result = instance.exceptionClass
        assertThat(result, sameInstance(exceptionClass))
    }

    @Test
    fun testToString()
    {
        assertThat(instance.toString(), notNullValue())
        assertThat(instance.toString(), not(isEmptyOrNullString()))

    }

    //Private Fake Exception types used for Testing.
    @Internal
    private class FakeException : Exception()

    @Internal
    private class FakeExceptionWithMessage : Exception
    {

        constructor()

        constructor(message: String) : super(message)

    }

    @Internal
    private class FakeExceptionWithThrowable : Exception
    {

        constructor()

        constructor(cause: Throwable) : super(cause)

    }

    @Internal
    private class FakeExceptionWithBoth : Exception
    {

        constructor()

        constructor(message: String) : super(message)

        constructor(message: String, cause: Throwable) : super(message, cause)

        constructor(cause: Throwable) : super(cause)

    }

    @Internal
    private class FakeExceptionThatThrowsOnConstruct : Exception()
    {
        init
        {
            throw RuntimeException("This is a test")
        }

    }
}
