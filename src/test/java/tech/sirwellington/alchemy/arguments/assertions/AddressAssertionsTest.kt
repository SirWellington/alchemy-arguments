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
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.Repeat

/**

 * @author SirWellington
 */
@Repeat(100)
@RunWith(AlchemyTestRunner::class)
class AddressAssertionsTest
{

    private lateinit var zip: String

    private lateinit var badZip: String


    @Before
    @Throws(Exception::class)
    fun setUp()
    {

        setupData()
    }


    @Throws(Exception::class)
    private fun setupData()
    {
        zip = zipToString(one(NumberGenerators.integers(0, 100000)))
        badZip = zipToString(one(NumberGenerators.integers(100000, Integer.MAX_VALUE)))
    }

    private fun zipToString(zip: Int): String
    {
        if (zip < 99999)
        {
            return String.format("%05d", zip)
        }
        else
        {
            return zip.toString()
        }
    }

    @Test
    fun testValidZipCode()
    {
        val assertion = validZipCode()
        assertThat(assertion, notNullValue())

        assertion.check(zip)
    }

    @Test
    fun testInvalidZipCode()
    {
        val assertion = validZipCode()

        assertThrows { assertion.check(badZip) }.failedAssertion()
    }

    @Test
    fun testValidZipCodeString()
    {
        val assertion = validZipCodeString()
        assertThat(assertion, notNullValue())

        assertion.check(zip)
    }

    @Test
    fun testValidZipCodeStringWithInvalid()
    {
        val assertion = validZipCodeString()

        assertThrows { assertion.check(badZip) }.failedAssertion()
    }

}