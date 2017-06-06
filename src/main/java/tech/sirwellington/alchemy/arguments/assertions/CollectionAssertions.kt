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

@file:JvmName("CollectionAssertions")

package tech.sirwellington.alchemy.arguments.assertions

import tech.sirwellington.alchemy.annotations.access.NonInstantiable
import tech.sirwellington.alchemy.annotations.arguments.NonEmpty
import tech.sirwellington.alchemy.annotations.arguments.Optional
import tech.sirwellington.alchemy.annotations.arguments.Positive
import tech.sirwellington.alchemy.annotations.arguments.Required
import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.Checks.Internal
import tech.sirwellington.alchemy.arguments.Checks.Internal.checkNotNull
import tech.sirwellington.alchemy.arguments.FailedAssertionException
import java.util.*

/**

 * @author SirWellington
 */
@NonInstantiable
class CollectionAssertions
@Throws(IllegalAccessException::class)
internal constructor()
{

    init
    {
        throw IllegalAccessException("cannot instantiate")
    }
}

/**
 * Asserts that the collection is not null and not empty.

 * @param <E>
 *
 *
 * @return
</E> */

fun <E> nonEmptyCollection(): AlchemyAssertion<Collection<E>>
{
    return AlchemyAssertion { collection ->
        notNull<Any>().check(collection)

        if (collection.isEmpty())
        {
            throw FailedAssertionException("Collection is empty")
        }
    }
}

/**
 * Asserts that the List is not null and not empty

 * @param <E>
 *
 *
 * @return
</E> */

fun <E> nonEmptyList(): AlchemyAssertion<List<E>>
{
    return AlchemyAssertion { list ->
        notNull<Any>().check(list)

        if (list.isEmpty())
        {
            throw FailedAssertionException("List is empty")
        }
    }

}

/**
 * Asserts that the Set is not null and not empty.
 * @param <E>
 *
 *
 * @return
</E> */

fun <E> nonEmptySet(): AlchemyAssertion<Set<E>>
{
    return AlchemyAssertion { set ->
        notNull<Any>().check(set)

        if (set.isEmpty())
        {
            throw FailedAssertionException("Set is empty")
        }
    }
}

/**
 * Asserts that the Map is not null and not empty

 * @param <K>
 *
 * @param <V>
 *
 *
 * @return
</V></K> */

fun <K, V> nonEmptyMap(): AlchemyAssertion<Map<K, V>>
{
    return AlchemyAssertion { map ->
        notNull<Any>().check(map)

        if (map.isEmpty())
        {
            throw FailedAssertionException("Map is empty")
        }

    }
}


fun <E> nonEmptyArray(): AlchemyAssertion<Array<E>>
{
    return AlchemyAssertion { array ->

        notNull<Any>().check(array)

        if (array.isEmpty())
        {
            throw FailedAssertionException("Array is empty")
        }
    }
}


fun <E> emptyCollection(): AlchemyAssertion<Collection<E>>
{
    return AlchemyAssertion { collection ->
        notNull<Any>().check(collection)

        if (!collection.isEmpty())
        {
            throw FailedAssertionException("Expected an empty collection, but it has size [${collection.size}]")
        }
    }
}


fun <E> emptyList(): AlchemyAssertion<List<E>>
{
    return AlchemyAssertion { list -> emptyCollection<E>().check(list) }
}


fun <E> emptySet(): AlchemyAssertion<Set<E>>
{
    return AlchemyAssertion { set -> emptyCollection<E>().check(set) }
}

@Throws(IllegalArgumentException::class)

fun <E> listContaining(@Required element: E): AlchemyAssertion<List<E>>
{
    checkNotNull(element, "cannot check for null")

    return AlchemyAssertion { list ->
        notNull<Any>().check(list)
        if (!list.contains(element))
        {
            throw FailedAssertionException(element.toString() + " not found in List")
        }
    }
}

@Throws(IllegalArgumentException::class)

fun <E, C : Collection<E>> collectionContaining(@Required element: E): AlchemyAssertion<C>
{
    checkNotNull(element, "cannot check for null")

    return AlchemyAssertion { collection ->
        notNull<Any>().check(collection)

        if (!collection.contains(element))
        {
            throw FailedAssertionException(element.toString() + " not found in Collection")
        }
    }
}

/**
 * Checks that the [Collection] contains ALL of the specified values.

 * @param <E>
 *
 * @param <C>
 *
 * @param first
 *
 * @param andOther
 *
 * @return
 *
 * @throws IllegalArgumentException
</C></E> */
@Throws(IllegalArgumentException::class)

fun <E, C : Collection<E>> collectionContainingAll(@Required first: E, @Optional vararg andOther: E): AlchemyAssertion<C>
{
    checkNotNull(first, "first argument cannot be null")

    if (andOther == null || andOther.size == 0)
    {
        return collectionContaining(first)
    }

    return AlchemyAssertion { collection ->
        notNull<Any>().check(collection)

        collectionContaining(first).check(collection)

        val arguments = Arrays.asList(*andOther)

        for (argument in arguments)
        {
            if (!collection.contains(argument))
            {
                throw FailedAssertionException("Element not found in Collection: " + argument)
            }
        }
    }
}

/**
 * Checks whether a collection contains at least one of the specified parameters.

 * @param <E>
 *
 * @param <C>
 *
 * @param first
 *
 * @param orOthers
 *
 * @return
 *
 * @throws IllegalArgumentException
</C></E> */
@Throws(IllegalArgumentException::class)

fun <E, C : Collection<E>> collectionContainingAtLeastOnceOf(@Required first: E, @Optional vararg orOthers: E): AlchemyAssertion<C>
{
    checkNotNull(first, "first argument cannot be null")

    if (orOthers.isEmpty())
    {
        return collectionContaining(first)
    }

    return AlchemyAssertion block@ { collection ->

        if (collection.contains(first))
        {
            return@block
        }

        for (argument in orOthers)
        {
            if (collection.contains(argument))
            {
                return@block
            }
        }

        if (orOthers.any { collection.contains(it) })
        {
            throw FailedAssertionException("Collection does not contain any of : $first , ${Arrays.toString(orOthers)}")
        }
    }
}

@Throws(IllegalArgumentException::class)

fun <K, V> mapWithKey(@Required key: K): AlchemyAssertion<Map<K, V>>
{
    checkNotNull(key, "key cannot be null")

    return AlchemyAssertion { map ->

        notNull<Any>().check(map)

        if (!map.containsKey(key))
        {
            throw FailedAssertionException("Expected Key [$key] in Map")
        }
    }
}

@Throws(IllegalArgumentException::class)

fun <K, V> mapWithKeyValue(@Required key: K, value: V): AlchemyAssertion<Map<K, V>>
{
    checkNotNull(key, "key cannot be null")

    return AlchemyAssertion { map ->

        mapWithKey<K, V>(key).check(map)

        val valueInMap = map[key]

        if (value != valueInMap)
        {
            throw FailedAssertionException("Value in Map [$valueInMap] does not match expected value $value")
        }

    }
}

@Throws(IllegalArgumentException::class)

fun <K, V> keyInMap(@Required map: Map<K, V>): AlchemyAssertion<K>
{
    checkNotNull(map, "map cannot be null")

    return AlchemyAssertion { key ->

        notNull<Any>().check(key)

        if (!map.containsKey(key))
        {
            throw FailedAssertionException("Expected key [$key] to be in map")
        }
    }
}

@Throws(IllegalArgumentException::class)

fun <K, V> valueInMap(@Required map: Map<K, V>): AlchemyAssertion<V>
{
    checkNotNull(map, "map cannot be null")

    return AlchemyAssertion { value ->

        notNull<Any>().check(value)

        if (!map.containsValue(value))
        {
            throw FailedAssertionException("Expected value [$value] to be in map")
        }
    }
}

@Throws(IllegalArgumentException::class)

fun <E> elementInCollection(@NonEmpty collection: Collection<E>): AlchemyAssertion<E>
{
    checkNotNull(collection, "collection cannot be null")

    return AlchemyAssertion { element ->
        notNull<Any>().check(element)

        if (!collection.contains(element))
        {
            throw FailedAssertionException("Expected element [$element] to be in collection")
        }
    }
}

@Throws(IllegalArgumentException::class)

fun <C : Collection<Any>> collectionOfSize(@Positive size: Int): AlchemyAssertion<C>
{
    Internal.checkThat(size >= 0, "size must be >= 0")

    return AlchemyAssertion { collection ->

        nonEmptyCollection<Any>().check(collection)

        val actualSize = collection.size

        if (actualSize != size)
        {
            throw FailedAssertionException("Expected collection with size [$size] but is instead [$actualSize]")
        }
    }
}