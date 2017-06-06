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

import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.arguments.assertions.StringAssertions
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat
import tech.sirwellington.alchemy.test.junit.runners.Repeat

/**

 * @author SirWellington
 */
@RunWith(AlchemyTestRunner::class)
@Repeat
class ArgumentsTest
{

    private var argument: String? = null

    @Before
    fun setUp()
    {
        argument = one(alphabeticString())
    }

    @DontRepeat
    @Test
    fun testConstructorThrows()
    {
        assertThrows { Arguments::class.java.newInstance() }
    }

    @Test
    fun testCheckThat()
    {
        val instance = Arguments.checkThat<String>(argument)
        assertThat<AssertionBuilder<Any, FailedAssertionException>>(instance, notNullValue())
    }

    @Test
    fun testCheckThatWithMultipleArguments()
    {
        val strings = listOf<String>(alphabeticString(), 30)
        val stringArray = strings.toTypedArray()

        var instance = Arguments.checkThat<String>(argument, *stringArray)
        assertThat(instance, notNullValue())
        instance.are(StringAssertions.nonEmptyString())

        instance = Arguments.checkThat<String>(argument, *arrayOfNulls<String>(0))
        assertThat(instance, notNullValue())
        instance.are(StringAssertions.nonEmptyString())
    }

    @Test
    fun testCheckThatWithMultipleArgumentsWithFailure()
    {
        val instance = Arguments.checkThat<String>(argument, *arrayOfNulls<String>(1))
        assertThat(instance, notNullValue())
        assertThrows { instance.are(StringAssertions.nonEmptyString()) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

}
