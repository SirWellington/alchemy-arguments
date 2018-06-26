/*
 * Copyright © 2018. Sir Wellington.
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
package tech.sirwellington.alchemy.arguments

import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.arguments.Arguments.checkThat
import tech.sirwellington.alchemy.arguments.assertions.nonEmptyString
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat
import tech.sirwellington.alchemy.test.junit.runners.GenerateList
import tech.sirwellington.alchemy.test.junit.runners.GenerateString
import tech.sirwellington.alchemy.test.junit.runners.GenerateString.Type.ALPHABETIC
import tech.sirwellington.alchemy.test.junit.runners.Repeat

/**

 * @author SirWellington
 */
@RunWith(AlchemyTestRunner::class)
@Repeat
class ArgumentsTest
{

    @GenerateString(ALPHABETIC)
    private lateinit var argument: String

    @GenerateList(String::class)
    private lateinit var strings: List<String>

    @DontRepeat
    @Test
    fun testConstructorThrows()
    {
        assertThrows { Arguments::class.java.newInstance() }
    }

    @Test
    fun testCheckThat()
    {
        val instance = checkThat<String>(argument)
        assertThat(instance, notNullValue())
    }

    @Test
    fun testCheckThatWithMultipleArguments()
    {
        val stringArray = strings.toTypedArray()

        var instance = checkThat(argument, *stringArray)
        assertThat(instance, notNullValue())
        instance.are(nonEmptyString())

        instance = checkThat(argument, *arrayOfNulls(0))
        assertThat(instance, notNullValue())
        instance.are(nonEmptyString())
    }

    @Test
    fun testCheckThatWithMultipleArgumentsWithFailure()
    {
        val instance = checkThat(argument, *arrayOfNulls(1))

        assertThat(instance, notNullValue())

        assertThrows { instance.are(nonEmptyString()) }.failedAssertion()
    }

}
