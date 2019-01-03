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

package tech.sirwellington.alchemy.arguments

import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.generator.*
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat
import tech.sirwellington.alchemy.test.junit.runners.GenerateString
import tech.sirwellington.alchemy.test.junit.runners.GenerateString.Type.ALPHABETIC
import tech.sirwellington.alchemy.test.junit.runners.Repeat
import java.util.Collections

/**

 * @author SirWellington
 */
@RunWith(AlchemyTestRunner::class)
@Repeat
class ChecksTest
{

    private lateinit var strings: AlchemyGenerator<String>

    private lateinit var string: String
    private lateinit var `object`: Any
    private lateinit var varArgs: Array<String>

    @GenerateString(ALPHABETIC)
    private lateinit var message: String

    @Before
    fun setUp()
    {
        strings = StringGenerators.alphanumericStrings()
        string = one(strings)
        `object` = one(strings)

        val listOfStrings = CollectionGenerators.listOf(strings)
        varArgs = listOfStrings.toTypedArray()
    }


    @DontRepeat
    @Test
    fun testCheckNotNull()
    {
        checkNotNull("")
        checkNotNull(this)

        assertThrows { checkNotNull(null) }
                .illegalArgument()
    }

    @Test
    fun testCheckNotNullWithMessage()
    {
        checkNotNull("", message)
        checkNotNull(this, message)

        assertThrows { checkNotNull(null, message) }
                .illegalArgument()
                .hasMessage(message)
    }

    @Test
    fun testCheckThat()
    {
        checkThat(true)

        assertThrows { checkThat(false) }
                .illegalArgument()
    }

    @Test
    fun testCheckThatWithMessage()
    {
        checkThat(true, message)

        assertThrows { checkThat(false, message) }
                .illegalArgument()
                .hasMessage(message)
    }

    @Test
    fun testCheckState()
    {
        checkState(true, message)

        assertThrows { checkState(false, message) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage(message)
    }

    @Test
    fun testIsNullOrEmptyString()
    {
        assertThat(isNullOrEmpty(null as String?), equalTo(true))
        assertThat(isNullOrEmpty(""), equalTo(true))
        assertThat(isNullOrEmpty(" "), equalTo(false))

        assertThat(isNullOrEmpty(string), equalTo(false))
    }

    @Test
    fun testIsNullOrEmptyCollection()
    {
        assertThat(isNullOrEmpty(Collections.EMPTY_LIST), equalTo(true))
        assertThat(isNullOrEmpty(Collections.EMPTY_SET), equalTo(true))
        assertThat(isNullOrEmpty(null as Collection<*>?), equalTo(true))

        val numbers = CollectionGenerators.listOf(NumberGenerators.positiveIntegers())
        val strings = CollectionGenerators.listOf(StringGenerators.strings(10))

        assertThat(isNullOrEmpty(numbers), equalTo(false))
        assertThat(isNullOrEmpty(strings), equalTo(false))
    }

    @Test
    fun testCheckNotNullOrEmpty()
    {
        val string = one(strings)
        checkNotNullOrEmpty(string)

        val emptyString = ""
        assertThrows { checkNotNullOrEmpty(emptyString) }.illegalArgument()

        val nullString: String? = null

        assertThrows { checkNotNullOrEmpty(nullString) }
                .illegalArgument()
    }

    @Test
    fun testCheckNotNullOrEmptyWithMessage()
    {
        val string = one(strings)
        checkNotNullOrEmpty(string, message)

        assertThrows { checkNotNullOrEmpty("", message) }
                .illegalArgument()
                .hasMessage(message)

        assertThrows { checkNotNullOrEmpty(null, message) }
                .illegalArgument()
                .hasMessage(message)

    }

    @Test
    fun testIsNull()
    {
        assertTrue(isNull(null))
        assertFalse(isNull(string))
        assertFalse(isNull(`object`))
    }

    @Test
    fun testNotNull()
    {
        assertTrue(notNull(`object`))
        assertTrue(notNull(string))
        assertFalse(notNull(null))
    }

    @Test
    fun testAnyAreNull()
    {
        assertTrue(anyAreNull())
        assertTrue(anyAreNull(null as Any?))
        assertTrue(anyAreNull(string, null))
        assertTrue(anyAreNull(one(strings), one(strings), null))

        assertFalse(anyAreNull(varArgs as Any?))
        assertFalse(anyAreNull(varArgs[0], varArgs[1], varArgs[2]))

    }

    @Test
    fun testAllAreNull()
    {
        assertTrue(allAreNull())
        assertTrue(allAreNull(null as Any?))
        assertTrue(allAreNull(null, null))
        assertTrue(allAreNull(null, null, null))

        assertFalse(allAreNull(varArgs as Any?))
        assertFalse(allAreNull(null, string))
        assertFalse(allAreNull(null, string, string))
        assertFalse(allAreNull(null, string, string, `object`))
    }

    @Test
    fun testIsNullOrEmpty()
    {
        assertTrue(isNullOrEmpty(""))
        assertTrue(isNullOrEmpty(null as String?))

        assertFalse(isNullOrEmpty(string))
    }

    @Test
    fun testNotNullOrEmpty()
    {
        assertTrue(notNullOrEmpty(string))

        assertFalse(notNullOrEmpty(null as String?))
        assertFalse(notNullOrEmpty(""))
    }

    @Test
    fun testAnyAreNullOrEmpty()
    {
        assertTrue(anyAreNullOrEmpty(string, null))
        assertTrue(anyAreNullOrEmpty(null, string, string))
        assertTrue(anyAreNullOrEmpty(null, string, one(strings)))
        assertTrue(anyAreNullOrEmpty(null, null, null))

        assertFalse(anyAreNullOrEmpty(*varArgs))
        assertFalse(anyAreNullOrEmpty(string, string, string))
    }

    @Test
    fun testAllAreNullOrEmpty()
    {
        assertTrue(allAreNullOrEmpty())
        assertTrue(allAreNullOrEmpty(""))
        assertTrue(allAreNullOrEmpty("", ""))
        assertTrue(allAreNullOrEmpty("", "", null))
        assertTrue(allAreNullOrEmpty(null as String?))
        assertTrue(allAreNullOrEmpty(null, null, null))
        assertTrue(allAreNullOrEmpty(null, null, null, ""))

        assertFalse(allAreNullOrEmpty(string, null))
        assertFalse(allAreNullOrEmpty(string, ""))
        assertFalse(allAreNullOrEmpty(string, string, string, string, ""))
        assertFalse(allAreNullOrEmpty(string, string, string, `object`.toString(), ""))
    }

}
