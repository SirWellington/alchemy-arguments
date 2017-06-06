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
import org.mockito.Mock
import org.mockito.Spy
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner

import org.mockito.Matchers.any
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

/**

 * @author SirWellington
 */
@RunWith(AlchemyTestRunner::class)
class AssertionBuilderTest
{

    @Mock
    private val assertion: AlchemyAssertion<*>? = null

    @Spy
    private val instance: FakeInstance<*>? = null

    @Before
    @Throws(Throwable::class)
    fun setUp()
    {
        `when`<AssertionBuilder>(instance!!.are(ArgumentMatchers.any()))
                .thenCallRealMethod()
    }

    @Test
    @Throws(Throwable::class)
    fun testAreCallsIs()
    {
        instance!!.are(assertion)
        verify<FakeInstance>(instance).`is`(assertion)
    }

    private class FakeInstance<A> : AssertionBuilder<A, Throwable>
    {

        override fun usingMessage(message: String): AssertionBuilder<A, Throwable>
        {
            return this
        }

        override fun <Ex : Throwable> throwing(exceptionMapper: ExceptionMapper<Ex>): AssertionBuilder<A, Ex>
        {
            return this as AssertionBuilder<A, Ex>
        }

        @Throws(Throwable::class)
        override fun `is`(assertion: AlchemyAssertion<A>): AssertionBuilder<A, Throwable>
        {
            return this
        }

    }

}
