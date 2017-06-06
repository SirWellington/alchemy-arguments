/*
 * Copyright 2016 Aroma Tech.
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
import tech.sirwellington.alchemy.generator.PeopleGenerators
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat
import tech.sirwellington.alchemy.test.junit.runners.Repeat


/**

 * @author SirWellington
 */
@Repeat(100)
@RunWith(AlchemyTestRunner::class)
class PeopleAssertionsTest
{

    private var email: String
    private var badEmail: String

    @Before
    @Throws(Exception::class)
    fun setUp()
    {

        setupData()
        setupMocks()
    }

    @Throws(Exception::class)
    private fun setupData()
    {
        email = one(PeopleGenerators.emails())
        badEmail = one(alphabeticString())
    }

    @Throws(Exception::class)
    private fun setupMocks()
    {

    }

    @DontRepeat
    @Test
    fun testConstructor()
    {
        assertThrows { PeopleAssertions() }
                .isInstanceOf(IllegalAccessException::class.java)
    }

    @Test
    fun testValidEmailAddress()
    {
        val instance = PeopleAssertions.validEmailAddress()
        assertThat(instance, notNullValue())

        instance.check(email)

        assertThrows { instance.check(badEmail) }
                .failedAssertion()
    }

    @DontRepeat
    @Test
    fun testValidEmailAddressWithEmptyArgs()
    {
        val instance = PeopleAssertions.validEmailAddress()

        assertThrows { instance.check(null) }
                .failedAssertion()

        assertThrows { instance.check("") }
                .failedAssertion()
    }

}