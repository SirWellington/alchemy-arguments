/*
 * Copyright 2016 RedRoma, Inc..
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

import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.arguments.FailedAssertionException
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

    private var zip: String? = null

    private var badZip: String? = null


    @Before
    @Throws(Exception::class)
    fun setUp()
    {

        setupData()
    }


    @Throws(Exception::class)
    private fun setupData()
    {
        zip = zipToString(one(integers(0, 100000)))
        badZip = zipToString(one(integers(100000, Integer.MAX_VALUE)))
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
        val assertion = AddressAssertions.validZipCode()
        assertThat(assertion, notNullValue())

        assertion.check(zip)
    }

    @Test
    fun testInvalidZipCode()
    {
        val assertion = AddressAssertions.validZipCode()

        assertThrows { assertion.check(badZip) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testValidZipCodeString()
    {
        val assertion = AddressAssertions.validZipCodeString()
        assertThat(assertion, notNullValue())

        assertion.check(zip)
    }

    @Test
    fun testValidZipCodeStringWithInvalid()
    {
        val assertion = AddressAssertions.validZipCodeString()

        assertThrows { assertion.check(badZip) }
    }

}