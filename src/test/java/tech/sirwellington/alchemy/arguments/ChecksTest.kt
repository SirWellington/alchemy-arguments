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

import java.util.Collections
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.generator.AlchemyGenerator
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat
import tech.sirwellington.alchemy.test.junit.runners.Repeat

import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import tech.sirwellington.alchemy.generator.AlchemyGenerator.one
import tech.sirwellington.alchemy.generator.CollectionGenerators.listOf
import tech.sirwellington.alchemy.generator.NumberGenerators.positiveIntegers
import tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString
import tech.sirwellington.alchemy.generator.StringGenerators.alphanumericString
import tech.sirwellington.alchemy.generator.StringGenerators.strings
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows

/**

 * @author SirWellington
 */
@RunWith(AlchemyTestRunner::class)
@Repeat
class ChecksTest
{

    private var strings: AlchemyGenerator<String>? = null

    private var string: String? = null
    private var `object`: Any? = null
    private var varArgs: Array<String>? = null

    @Before
    fun setUp()
    {
        strings = alphanumericString()
        string = one(strings)
        `object` = one(strings)

        val listOfStrings = listOf<String>(strings)
        varArgs = listOfStrings.toTypedArray()
    }

    @DontRepeat
    @Test
    fun testCannotInstantiate()
    {
        assertThrows { Checks::class.java.newInstance() }
                .isInstanceOf(IllegalAccessException::class.java)

        assertThrows { Checks.Internal::class.java.newInstance() }
                .isInstanceOf(IllegalAccessException::class.java)
    }

    @DontRepeat
    @Test
    fun testCheckNotNull()
    {
        Checks.Internal.checkNotNull("")
        Checks.Internal.checkNotNull(this)

        assertThrows { Checks.Internal.checkNotNull(null) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun testCheckNotNullWithMessage()
    {
        val message = one(alphabeticString())
        Checks.Internal.checkNotNull("", message)
        Checks.Internal.checkNotNull(this, message)

        assertThrows { Checks.Internal.checkNotNull(null, message) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(message)
    }

    @Test
    fun testCheckThat()
    {
        Checks.Internal.checkThat(true)

        assertThrows { Checks.Internal.checkThat(false) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun testCheckThatWithMessage()
    {
        val message = one(alphabeticString())

        Checks.Internal.checkThat(true, message)

        assertThrows { Checks.Internal.checkThat(false, message) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(message)
    }

    @Test
    fun testCheckState()
    {
        val message = one(alphabeticString())
        Checks.Internal.checkState(true, message)

        assertThrows { Checks.Internal.checkState(false, message) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage(message)
    }

    @Test
    fun testIsNullOrEmptyString()
    {
        assertThat(Checks.Internal.isNullOrEmpty(null as String?), `is`(true))
        assertThat(Checks.Internal.isNullOrEmpty(""), `is`(true))
        assertThat(Checks.Internal.isNullOrEmpty(" "), `is`(false))

        val string = one(alphabeticString())
        assertThat(Checks.Internal.isNullOrEmpty(string), `is`(false))
    }

    @Test
    fun testIsNullOrEmptyCollection()
    {
        assertThat(Checks.Internal.isNullOrEmpty(Collections.EMPTY_LIST), `is`(true))
        assertThat(Checks.Internal.isNullOrEmpty(Collections.EMPTY_SET), `is`(true))
        assertThat(Checks.Internal.isNullOrEmpty(null as Collection<*>?), `is`(true))

        val numbers = listOf<Int>(positiveIntegers())
        val strings = listOf<String>(strings(10))

        assertThat(Checks.Internal.isNullOrEmpty(numbers), `is`(false))
        assertThat(Checks.Internal.isNullOrEmpty(strings), `is`(false))
    }

    @Test
    fun testCheckNotNullOrEmpty()
    {
        val string = one(strings())
        Checks.Internal.checkNotNullOrEmpty(string)

        val emptyString = ""
        assertThrows { Checks.Internal.checkNotNullOrEmpty(emptyString) }
                .isInstanceOf(IllegalArgumentException::class.java)

        val nullString: String? = null
        assertThrows { Checks.Internal.checkNotNullOrEmpty(nullString) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun testCheckNotNullOrEmptyWithMessage()
    {
        val string = one(strings())
        val message = one(alphabeticString())
        Checks.Internal.checkNotNullOrEmpty(string, message)

        assertThrows { Checks.Internal.checkNotNullOrEmpty("", message) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(message)

        assertThrows { Checks.Internal.checkNotNullOrEmpty(null, message) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(message)

    }

    @Test
    fun testIsNull()
    {
        assertTrue(Checks.isNull(null))
        assertFalse(Checks.isNull(string))
        assertFalse(Checks.isNull(`object`))
    }

    @Test
    fun testNotNull()
    {
        assertTrue(Checks.notNull(`object`))
        assertTrue(Checks.notNull(string))
        assertFalse(Checks.notNull(null))
    }

    @Test
    fun testAnyAreNull()
    {
        assertTrue(Checks.anyAreNull())
        assertTrue(Checks.anyAreNull(null as Any?))
        assertTrue(Checks.anyAreNull(string, null))
        assertTrue(Checks.anyAreNull(one(strings), one(strings), null))

        assertFalse(Checks.anyAreNull(varArgs as Any?))
        assertFalse(Checks.anyAreNull(varArgs!![0], varArgs!![1], varArgs!![2]))

    }

    @Test
    fun testAllAreNull()
    {
        assertTrue(Checks.allAreNull())
        assertTrue(Checks.allAreNull(null as Any?))
        assertTrue(Checks.allAreNull(null, null))
        assertTrue(Checks.allAreNull(null, null, null))

        assertFalse(Checks.allAreNull(varArgs as Any?))
        assertFalse(Checks.allAreNull(null, string))
        assertFalse(Checks.allAreNull(null, string, string))
        assertFalse(Checks.allAreNull(null, string, string, `object`))
    }

    @Test
    fun testIsNullOrEmpty()
    {
        assertTrue(Checks.isNullOrEmpty(""))
        assertTrue(Checks.isNullOrEmpty(null))

        assertFalse(Checks.isNullOrEmpty(string))
    }

    @Test
    fun testNotNullOrEmpty()
    {
        assertTrue(Checks.notNullOrEmpty(string))

        assertFalse(Checks.notNullOrEmpty(null))
        assertFalse(Checks.notNullOrEmpty(""))
    }

    @Test
    fun testAnyAreNullOrEmpty()
    {
        assertTrue(Checks.anyAreNullOrEmpty(string, null))
        assertTrue(Checks.anyAreNullOrEmpty(null, string, string))
        assertTrue(Checks.anyAreNullOrEmpty(null, string, one(strings)))
        assertTrue(Checks.anyAreNullOrEmpty(null, null, null))

        assertFalse(Checks.anyAreNullOrEmpty(*varArgs))
        assertFalse(Checks.anyAreNullOrEmpty(string, string, string))
    }

    @Test
    fun testAllAreNullOrEmpty()
    {
        assertTrue(Checks.allAreNullOrEmpty())
        assertTrue(Checks.allAreNullOrEmpty(""))
        assertTrue(Checks.allAreNullOrEmpty("", ""))
        assertTrue(Checks.allAreNullOrEmpty("", "", null))
        assertTrue(Checks.allAreNullOrEmpty(null as String?))
        assertTrue(Checks.allAreNullOrEmpty(null, null, null))
        assertTrue(Checks.allAreNullOrEmpty(null, null, null, ""))

        assertFalse(Checks.allAreNullOrEmpty(string, null))
        assertFalse(Checks.allAreNullOrEmpty(string, ""))
        assertFalse(Checks.allAreNullOrEmpty(string, string, string, string, ""))
        assertFalse(Checks.allAreNullOrEmpty(string, string, string, `object`!!.toString(), ""))
    }

}
