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
import tech.sirwellington.alchemy.arguments.Arguments.checkThat
import tech.sirwellington.alchemy.arguments.FailedAssertionException
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat
import tech.sirwellington.alchemy.test.junit.runners.GenerateList
import tech.sirwellington.alchemy.test.junit.runners.Repeat
import java.util.*

/**

 * @author SirWellington
 */
@RunWith(AlchemyTestRunner::class)
@Repeat(50)
class CollectionAssertionsTest
{

    @GenerateList(String::class)
    private val strings: MutableList<String>? = null

    @Before
    fun setUp()
    {
    }

    @DontRepeat
    @Test
    fun testCannotInstantiateClass()
    {
        assertThrows { CollectionAssertions::class.java.newInstance() }

        assertThrows { CollectionAssertions() }
                .isInstanceOf(IllegalAccessException::class.java)
    }

    @Test
    fun testNonEmptyCollection()
    {

        val instance = CollectionAssertions.nonEmptyCollection<String>()
        assertThat(instance, notNullValue())

        instance.check(strings)

        assertThrows { instance.check(null) }
                .isInstanceOf(FailedAssertionException::class.java)

        assertThrows { instance.check(emptySet<Any>()) }
                .isInstanceOf(FailedAssertionException::class.java)

    }

    @Test
    fun testNonEmptyList()
    {
        val instance = CollectionAssertions.nonEmptyList<String>()
        assertThat(instance, notNullValue())

        instance.check(strings)

        assertThrows { instance.check(null) }
                .isInstanceOf(FailedAssertionException::class.java)

        assertThrows { instance.check(emptyList<Any>()) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testNonEmptySet()
    {
        val instance = CollectionAssertions.nonEmptySet<String>()
        assertThat(instance, notNullValue())

        val setOfStrings = HashSet(strings!!)
        instance.check(setOfStrings)

        checkThat<Set<String>>(setOfStrings).`is`(Companion.nonEmptySet())

        val emptySet = HashSet<String>()
        assertThrows { instance.check(emptySet) }
                .isInstanceOf(FailedAssertionException::class.java)

        assertThrows { instance.check(null) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testNonEmptyMap()
    {
        val instance = CollectionAssertions.nonEmptyMap<String, Int>()

        val map = mapOf<String, Int>(alphabeticString(),
                                     positiveIntegers(),
                                     40)

        instance.check(map)

        assertThrows { instance.check(emptyMap<Any, Any>()) }
                .isInstanceOf(FailedAssertionException::class.java)

        assertThrows { instance.check(null) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testNonEmptyArray()
    {
        val instance = CollectionAssertions.nonEmptyArray<String>()
        assertThat(instance, notNullValue())

        assertThrows { instance.check(null) }
                .isInstanceOf(FailedAssertionException::class.java)

        val stringArray = strings!!.toTypedArray()

        instance.check(stringArray)

        val emptyStringArray = arrayOf<String>()

        assertThrows { instance.check(emptyStringArray) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testListContaining()
    {
        val string = strings!!.stream().findAny().orElseGet(alphabeticString())

        val instance = CollectionAssertions.listContaining(string)
        assertThat(instance, notNullValue())
        instance.check(strings)

        val hex = listOf<String>(hexadecimalString(100))

        assertThrows { instance.check(hex) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testListContainingWithBadArgs()
    {
        assertThrows { CollectionAssertions.listContaining<Any>(null) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun testCollectionContaining()
    {
        val string = strings!!.stream().findAny().orElseGet(alphabeticString())

        val instance = CollectionAssertions.collectionContaining<Any, Collection<String>>(string)
        assertThat(instance, notNullValue())
        instance.check(strings)

        val hex = listOf<String>(hexadecimalString(100))

        assertThrows { instance.check(hex) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @DontRepeat
    @Test
    @Throws(Exception::class)
    fun testCollectionContainingWithBadArgs()
    {
        assertThrows { CollectionAssertions.collectionContaining<Any, Collection<*>>(null) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testCollectionContainingAll()
    {
        val args = listOf<String>(alphabeticString())
        val collection = listOf<String>(alphabeticString())
        collection.addAll(args)

        val first = args[0]
        val others = args.subList(1, args.size).toTypedArray()

        val instance = CollectionAssertions.collectionContainingAll<Any, Collection<String>>(first, *others)
        assertThat(instance, notNullValue())
        instance.check(collection)

        val otherCollection = listOf<String>(alphabeticString())
        assertThrows { instance.check(otherCollection) }.isInstanceOf(FailedAssertionException::class.java)
    }

    @DontRepeat
    @Test
    @Throws(Exception::class)
    fun testCollectionContainingAllWithBadArgs()
    {
        assertThrows { CollectionAssertions.collectionContainingAll<Any, Collection<*>>(null) }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testCollectionContainingAtLeastOnceOf()
    {
        val args = listOf<String>(alphanumericString())
        val collection = listOf<String>(alphanumericString())
        collection.addAll(args)

        val first = collection[0]
        val others = args.subList(1, args.size).toTypedArray()

        val instance = CollectionAssertions.collectionContainingAtLeastOnceOf<Any, Collection<String>>(first, *others)
        instance.check(collection)

        val otherCollection = listOf<String>(alphabeticString())
        assertThrows { instance.check(otherCollection) }

        //With at least one, it should pass.
        otherCollection.add(first)
        instance.check(otherCollection)
    }

    @DontRepeat
    @Test
    @Throws(Exception::class)
    fun testCollectionContainingAtLeastOnceOfWithBadArgs()
    {
        assertThrows { CollectionAssertions.collectionContainingAtLeastOnceOf<Any, Collection<*>>(null) }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun testMapWithKey()
    {
        val map = mapOf<Int, String>(positiveIntegers(), hexadecimalString(100), 100)

        val key = map.keys.stream().findAny().get()

        val instance = CollectionAssertions.mapWithKey<Int, String>(key)
        assertThat(instance, notNullValue())

        instance.check(map)

        val badMap = mapOf<Int, String>(negativeIntegers(), hexadecimalString(100), 100)
        assertThrows { instance.check(badMap) }
    }

    @Test
    fun testMapWithKeyValue()
    {
        val map = mapOf<Int, String>(positiveIntegers(), alphabeticString(), 100)

        val anyEntry = map.entries.stream().findAny().get()

        val instance: AlchemyAssertion<Map<Int, String>>
        instance = CollectionAssertions.mapWithKeyValue(anyEntry.key, anyEntry.value)
        assertThat(instance, notNullValue())

        //Should pass OK
        instance.check(map)

        val badMap = mapOf<Int, String>(negativeIntegers(), hexadecimalString(100), 100)
        assertThrows { instance.check(badMap) }
    }

    @Test
    fun testKeyInMap()
    {
        val map = mapOf<String, String>(alphabeticString(), alphanumericString(), 25)

        val assertion = CollectionAssertions.keyInMap(map)
        assertThat(assertion, notNullValue())

        val anyKey = map.keys.stream().findAny().get()
        assertion.check(anyKey)

        val randomKey = one(hexadecimalString(42))
        assertThrows { assertion.check(randomKey) }
                .isInstanceOf(FailedAssertionException::class.java)

        //Edge cases
        assertThrows { assertion.check(null) }
                .isInstanceOf(FailedAssertionException::class.java)

        assertThrows { CollectionAssertions.keyInMap<Any, Any>(null!!) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun testKeyInMapWithEmptyMap()
    {
        val assertion = CollectionAssertions.keyInMap(emptyMap<Any, Any>())

        for (string in strings!!)
        {
            assertThrows { assertion.check(string) }
                    .isInstanceOf(FailedAssertionException::class.java)
        }
    }

    @Test
    fun testValueInMap()
    {

        val map = mapOf<String, String>(alphanumericString(), alphabeticString(), 24)

        val assertion = CollectionAssertions.valueInMap(map)
        assertThat(assertion, notNullValue())

        val anyValue = map.values.stream().findAny().get()
        assertion.check(anyValue)

        val randomValue = one(hexadecimalString(10))
        assertThrows { assertion.check(randomValue) }
                .isInstanceOf(FailedAssertionException::class.java)

        //Edge cases
        assertThrows { assertion.check(null) }
                .isInstanceOf(FailedAssertionException::class.java)

        assertThrows { CollectionAssertions.valueInMap<Any, Any>(null!!) }
                .isInstanceOf(IllegalArgumentException::class.java)

        //Empty map should be ok
        CollectionAssertions.valueInMap(emptyMap<Any, Any>())
    }

    @Test
    fun testValueInMapWithEmptyMap()
    {
        val assertion = CollectionAssertions.valueInMap(emptyMap<Any, Any>())
        assertThat(assertion, notNullValue())

        for (string in strings!!)
        {
            assertThrows { assertion.check(string) }
                    .isInstanceOf(FailedAssertionException::class.java)
        }
    }

    @Test
    fun testElementInCollection()
    {
        val assertion = CollectionAssertions.elementInCollection(strings!!)
        assertThat(assertion, notNullValue())

        val anyValue = strings.stream().findAny().get()
        assertion.check(anyValue)

        val randomValue = one(hexadecimalString(20))

        assertThrows { assertion.check(randomValue) }
                .isInstanceOf(FailedAssertionException::class.java)

        //Edge cases
        assertThrows { assertion.check(null) }
                .isInstanceOf(FailedAssertionException::class.java)

        assertThrows { CollectionAssertions.elementInCollection<Any>(null!!) }
                .isInstanceOf(IllegalArgumentException::class.java)

        //Empty Collections should be ok
        CollectionAssertions.elementInCollection(emptyList<Any>())

    }

    @Test
    fun testCollectionOfSize()
    {
        val size = strings!!.size
        val instance = CollectionAssertions.collectionOfSize<in Collection<String>>(size)
        instance.check(strings)

        strings.add(one(alphabeticString()))

        assertThrows { instance.check(strings) }
                .isInstanceOf(FailedAssertionException::class.java)

        checkThat<List<String>>(strings)
                .`is`(Companion.collectionOfSize(strings.size))

    }

    @DontRepeat
    @Test
    fun testCollectionOfSizeWithBadArgs()
    {
        val badSize = one(negativeIntegers())

        assertThrows { CollectionAssertions.collectionOfSize<Collection<*>>(badSize) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun testEmptyCollection()
    {
        val instance = CollectionAssertions.emptyCollection<String>()

        val emptyCollection = ArrayList<String>()

        instance.check(emptyCollection)

        assertThrows { instance.check(strings) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testEmptyList()
    {
        val instance = CollectionAssertions.emptyList<String>()

        val emptyList = LinkedList<String>()
        instance.check(emptyList)

        assertThrows { instance.check(strings) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testEmptySet()
    {
        val instance = CollectionAssertions.emptySet<String>()

        val emptySet = HashSet<String>()
        instance.check(emptySet)

        val nonEmptySet = HashSet(strings!!)
        assertThrows { instance.check(nonEmptySet) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

}
