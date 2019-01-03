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

import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.arguments.failedAssertion
import tech.sirwellington.alchemy.generator.NumberGenerators
import tech.sirwellington.alchemy.generator.one
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.*
import tech.sirwellington.alchemy.test.junit.runners.GenerateInteger.Type.RANGE
import java.net.URL


/**

 * @author SirWellington
 */
@Repeat(1000)
@RunWith(AlchemyTestRunner::class)
class NetworkAssertionsTest
{
    @GenerateURL
    private lateinit var url: URL

    @GenerateString
    private lateinit var badUrl: String

    @GenerateInteger(value = RANGE, min = 1, max = MAX_PORT)
    private val port: Int = 0

    @Before
    fun setUp()
    {
    }

    @Test
    fun testValidURL()
    {
        val assertion = validURL()
        assertThat(assertion, notNullValue())

        assertion.check(url.toString())

        assertThrows { assertion.check(badUrl) }.failedAssertion()
    }

    @Test
    fun testValidPort()
    {
        val assertion = validPort()
        assertThat(assertion, notNullValue())

        assertion.check(port)

        val negative = one(NumberGenerators.negativeIntegers())
        assertThrows { assertion.check(negative) }.failedAssertion()

        val tooHigh = one(NumberGenerators.integers(MAX_PORT + 1, Integer.MAX_VALUE))
        assertThrows { assertion.check(tooHigh) }.failedAssertion()
    }

    companion object
    {
        private const val MAX_PORT = 65535
    }

}