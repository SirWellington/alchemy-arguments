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

package tech.sirwellington.alchemy.arguments.assertions

import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.checkThat
import tech.sirwellington.alchemy.arguments.failedAssertion
import tech.sirwellington.alchemy.arguments.illegalArgument
import tech.sirwellington.alchemy.arguments.whichever
import tech.sirwellington.alchemy.generator.CollectionGenerators.Companion.listOf
import tech.sirwellington.alchemy.generator.CollectionGenerators.Companion.mapOf
import tech.sirwellington.alchemy.generator.NumberGenerators
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.negativeIntegers
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.positiveIntegers
import tech.sirwellington.alchemy.generator.StringGenerators.Companion.alphabeticString
import tech.sirwellington.alchemy.generator.StringGenerators.Companion.alphanumericString
import tech.sirwellington.alchemy.generator.StringGenerators.Companion.hexadecimalString
import tech.sirwellington.alchemy.generator.one
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat
import tech.sirwellington.alchemy.test.junit.runners.GenerateList
import tech.sirwellington.alchemy.test.junit.runners.Repeat
import java.util.LinkedList

/**

 * @author SirWellington
 */
@RunWith(AlchemyTestRunner::class)
@Repeat(50)
class CollectionAssertionsTest
{

    @GenerateList(String::class)
    private lateinit var strings: MutableList<String>

    @Before
    fun setUp()
    {
    }


    @Test
    fun testNonEmptyCollection()
    {

        val instance = CollectionAssertions.nonEmptyCollection<String>()
        assertThat(instance, notNullValue())

        instance.check(strings as Collection<String>)

        assertThrows { instance.check(null) }.failedAssertion()
        assertThrows { instance.check(emptySet<String>()) }.failedAssertion()

    }

    @Test
    fun testNonEmptyList()
    {
        val instance = CollectionAssertions.nonEmptyList<String>()
        assertThat(instance, notNullValue())

        instance.check(strings)

        assertThrows { instance.check(null) }.failedAssertion()
        assertThrows { instance.check(emptyList<String>()) }.failedAssertion()
    }

    @Test
    fun testNonEmptySet()
    {
        val instance = CollectionAssertions.nonEmptySet<String>()
        assertThat(instance, notNullValue())

        val setOfStrings = strings.toSet()
        instance.check(setOfStrings)

        checkThat(setOfStrings).isA(CollectionAssertions.nonEmptySet())

        val emptySet = HashSet<String>()
        assertThrows { instance.check(emptySet) }.failedAssertion()
        assertThrows { instance.check(null) }
                .failedAssertion()
    }

    @Test
    fun testNonEmptyMap()
    {
        val instance = CollectionAssertions.nonEmptyMap<String, Int>()

        val map = mapOf(alphabeticString(),
                        NumberGenerators.positiveIntegers(),
                        40)

        instance.check(map)

        assertThrows { instance.check(emptyMap()) }.failedAssertion()
        assertThrows { instance.check(null) }.failedAssertion()
    }

    @Test
    fun testNonEmptyArray()
    {
        val instance = CollectionAssertions.nonEmptyArray<String>()
        assertThat(instance, notNullValue())

        assertThrows { instance.check(null) }.failedAssertion()

        val stringArray = strings.toTypedArray()

        instance.check(stringArray)

        val emptyStringArray = arrayOf<String>()

        assertThrows { instance.check(emptyStringArray) }.failedAssertion()
    }

    @Test
    fun testListContaining()
    {
        val string = strings.whichever()

        val instance = CollectionAssertions.listContaining(string)
        assertThat(instance, notNullValue())
        instance.check(strings)

        val hex = listOf(hexadecimalString(100))

        assertThrows { instance.check(hex) }.failedAssertion()
    }

    @Test
    @Throws(Exception::class)
    fun testListContainingWithBadArgs()
    {
        assertThrows { CollectionAssertions.listContaining(null) }
                .illegalArgument()
    }

    @Test
    fun testCollectionContaining()
    {
        val string = strings.stream().findAny().orElse(alphabeticString().get())

        val instance = CollectionAssertions.collectionContaining(string)
        assertThat(instance, notNullValue())
        instance.check(strings)

        val hex = listOf(hexadecimalString(100))

        assertThrows { instance.check(hex) }.failedAssertion()
    }

    @DontRepeat
    @Test
    @Throws(Exception::class)
    fun testCollectionContainingWithBadArgs()
    {
        assertThrows { CollectionAssertions.collectionContaining(null) }
                .illegalArgument()
    }

    @Test
    @Throws(Exception::class)
    fun testCollectionContainingAll()
    {
        val args = listOf(alphabeticString())
        val collection = listOf(alphabeticString()).toMutableList()
        collection.addAll(args)

        val first = args.first()
        val others = args.subList(1, args.size).toTypedArray()

        val instance = CollectionAssertions.collectionContainingAll(first, *others)
        assertThat(instance, notNullValue())
        instance.check(collection)

        val otherCollection = listOf(alphabeticString())
        assertThrows { instance.check(otherCollection) }.failedAssertion()
    }

    @DontRepeat
    @Test
    @Throws(Exception::class)
    fun testCollectionContainingAllWithBadArgs()
    {
        assertThrows { CollectionAssertions.collectionContainingAll(null) }
                .illegalArgument()
    }

    @Test
    @Throws(Exception::class)
    fun testCollectionContainingAtLeastOnceOf()
    {
        val arguments = listOf(alphanumericString())
        val collection = listOf(alphanumericString()) + arguments

        val first = collection.first()
        val others = arguments.subList(1, arguments.size).toTypedArray()

        val instance = CollectionAssertions.collectionContainingAtLeastOneOf(first, *others)
        instance.check(collection)

        val otherCollection = listOf(alphabeticString()).toMutableList()
        assertThrows { instance.check(otherCollection) }.failedAssertion()

        //With at least one, it should pass.
        otherCollection.add(first)
        instance.check(otherCollection)
    }

    @DontRepeat
    @Test
    @Throws(Exception::class)
    fun testCollectionContainingAtLeastOnceOfWithBadArgs()
    {
        assertThrows { CollectionAssertions.collectionContainingAtLeastOneOf(null) }
                .illegalArgument()
    }

    @Test
    fun testMapWithKey()
    {
        val map = mapOf(positiveIntegers(), hexadecimalString(100), 100)

        val key = map.keys.whichever()

        val instance = CollectionAssertions.mapWithKey<Int, String>(key)
        assertThat(instance, notNullValue())

        instance.check(map)

        val badMap = mapOf(negativeIntegers(), hexadecimalString(100), 100)
        assertThrows { instance.check(badMap) }
    }

    @Test
    fun testMapWithKeyValue()
    {
        val map = mapOf(positiveIntegers(), alphabeticString(), 100)

        val anyEntry = map.entries.whichever()

        val instance: AlchemyAssertion<Map<Int, String>>
        instance = CollectionAssertions.mapWithKeyValue(anyEntry.key, anyEntry.value)
        assertThat(instance, notNullValue())

        //Should pass OK
        instance.check(map)

        val badMap = mapOf(negativeIntegers(), hexadecimalString(100), 100)
        assertThrows { instance.check(badMap) }
    }

    @Test
    fun testKeyInMap()
    {
        val map = mapOf(alphabeticString(), alphanumericString(), 25)

        val assertion = CollectionAssertions.keyInMap(map)
        assertThat(assertion, notNullValue())

        val anyKey = map.keys.stream().findAny().get()
        assertion.check(anyKey)

        val randomKey = one(hexadecimalString(42))
        assertThrows { assertion.check(randomKey) }.failedAssertion()

        //Edge cases
        assertThrows { assertion.check(null) }.failedAssertion()

        assertThrows { CollectionAssertions.keyInMap<Any, Any>(null!!) }
    }

    @Test
    fun testKeyInMapWithEmptyMap()
    {
        val assertion = CollectionAssertions.keyInMap(emptyMap<Any, Any>())

        for (string in strings)
        {
            assertThrows { assertion.check(string) }.failedAssertion()
        }
    }

    @Test
    fun testValueInMap()
    {

        val map = mapOf(alphanumericString(), alphabeticString(), 24)

        val assertion = CollectionAssertions.valueInMap(map)
        assertThat(assertion, notNullValue())

        val anyValue = map.values.whichever()
        assertion.check(anyValue)

        val randomValue = one(hexadecimalString(10))
        assertThrows { assertion.check(randomValue) }.failedAssertion()
        //Edge cases
        assertThrows { assertion.check(null) }.failedAssertion()
        assertThrows { CollectionAssertions.valueInMap<Any, Any>(null!!) }

        //Empty map should be ok
        CollectionAssertions.valueInMap(emptyMap<Any, Any>())
    }

    @Test
    fun testValueInMapWithEmptyMap()
    {
        val assertion = CollectionAssertions.valueInMap(emptyMap<Any, Any>())
        assertThat(assertion, notNullValue())

        for (string in strings)
        {
            assertThrows { assertion.check(string) }.failedAssertion()
        }
    }

    @Test
    fun testElementInCollection()
    {
        val assertion = CollectionAssertions.elementInCollection(strings)
        assertThat(assertion, notNullValue())

        val anyValue = strings.whichever()
        assertion.check(anyValue)

        val randomValue = one(hexadecimalString(20))

        assertThrows { assertion.check(randomValue) }.failedAssertion()
        //Edge cases
        assertThrows { assertion.check(null) }.failedAssertion()
        assertThrows { CollectionAssertions.elementInCollection<Any>(null!!) }

        //Empty Collections should be ok
        CollectionAssertions.elementInCollection(emptyList<Any>())

    }

    @Test
    fun testCollectionOfSize()
    {
        val size = strings.size
        val instance = CollectionAssertions.collectionOfSize<Collection<String>>(size)
        instance.check(strings)

        strings.add(one(alphabeticString()))

        assertThrows { instance.check(strings) }.failedAssertion()
        checkThat(strings)
                .isA(CollectionAssertions.collectionOfSize(strings.size))

    }

    @DontRepeat
    @Test
    fun testCollectionOfSizeWithBadArgs()
    {
        val badSize = one(negativeIntegers())

        assertThrows { CollectionAssertions.collectionOfSize<Collection<Any>>(badSize) }
                .illegalArgument()
    }

    @Test
    fun testEmptyCollection()
    {
        val instance = CollectionAssertions.emptyCollection<String>()

        val emptyCollection = listOf<String>()
        instance.check(emptyCollection)

        assertThrows { instance.check(strings) }.failedAssertion()
    }

    @Test
    fun testEmptyList()
    {
        val instance = CollectionAssertions.emptyList<String>()

        val emptyList = LinkedList<String>()
        instance.check(emptyList)

        assertThrows { instance.check(strings) }.failedAssertion()
    }

    @Test
    fun testEmptySet()
    {
        val instance = CollectionAssertions.emptySet<String>()

        val emptySet = HashSet<String>()
        instance.check(emptySet)

        val nonEmptySet = HashSet(strings)
        assertThrows { instance.check(nonEmptySet) }.failedAssertion()
    }

}
