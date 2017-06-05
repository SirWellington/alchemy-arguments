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

package tech.sirwellington.alchemy.arguments.assertions;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.annotations.arguments.*;
import tech.sirwellington.alchemy.annotations.arguments.Optional;
import tech.sirwellington.alchemy.arguments.*;

import static java.lang.String.format;
import static tech.sirwellington.alchemy.arguments.Checks.Internal.checkNotNull;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;

/**
 * @author SirWellington
 */
@NonInstantiable
public final class CollectionAssertions
{

    private final static Logger LOG = LoggerFactory.getLogger(CollectionAssertions.class);

    CollectionAssertions() throws IllegalAccessException
    {
        throw new IllegalAccessException("cannot instantiate");
    }

    /**
     * Asserts that the collection is not null and not empty.
     *
     * @param <E>
     * @return
     */
    public static <E> AlchemyAssertion<Collection<E>> nonEmptyCollection()
    {
        return new AlchemyAssertion<Collection<E>>()
        {
            @Override
            public void check(Collection<E> collection) throws FailedAssertionException
            {
                notNull().check(collection);

                if (collection.isEmpty())
                {
                    throw new FailedAssertionException("Collection is empty");
                }
            }
        };
    }

    /**
     * Asserts that the List is not null and not empty
     *
     * @param <E>
     * @return
     */
    public static <E> AlchemyAssertion<List<E>> nonEmptyList()
    {

        return new AlchemyAssertion<List<E>>()
        {
            @Override
            public void check(List<E> list) throws FailedAssertionException
            {
                notNull().check(list);

                if (list.isEmpty())
                {
                    throw new FailedAssertionException("List is empty");
                }
            }
        };
    }

    /**
     * Asserts that the Set is not null and not empty.
     *
     * @param <E>
     * @return
     */
    public static <E> AlchemyAssertion<Set<E>> nonEmptySet()
    {
        return new AlchemyAssertion<Set<E>>()
        {
            @Override
            public void check(Set<E> set) throws FailedAssertionException
            {
                notNull().check(set);

                if (set.isEmpty())
                {
                    throw new FailedAssertionException("Set is empty");
                }
            }
        };
    }

    /**
     * Asserts that the Map is not null and not empty
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> AlchemyAssertion<Map<K, V>> nonEmptyMap()
    {
        return new AlchemyAssertion<Map<K, V>>()
        {
            @Override
            public void check(Map<K, V> map) throws FailedAssertionException
            {
                notNull().check(map);

                if (map.isEmpty())
                {
                    throw new FailedAssertionException("Map is empty");
                }
            }
        };
    }

    public static <E> AlchemyAssertion<E[]> nonEmptyArray()
    {
        return new AlchemyAssertion<E[]>()
        {
            @Override
            public void check(E[] array) throws FailedAssertionException
            {
                notNull().check(array);

                if (array.length == 0)
                {
                    throw new FailedAssertionException("Array is empty");
                }
            }
        };
    }

    public static <E> AlchemyAssertion<Collection<E>> emptyCollection()
    {
        return new AlchemyAssertion<Collection<E>>()
        {
            @Override
            public void check(Collection<E> collection) throws FailedAssertionException
            {
                notNull().check(collection);

                if (!collection.isEmpty())
                {
                    throw new FailedAssertionException(format("Expected an empty collection, but it has size [%s]",
                                                              collection.size()));
                }
            }
        };
    }


    public static <E> AlchemyAssertion<List<E>> emptyList()
    {
        return new AlchemyAssertion<List<E>>()
        {
            @Override
            public void check(List<E> list) throws FailedAssertionException
            {
                CollectionAssertions.<E>emptyCollection().check(list);
            }
        };
    }

    public static <E> AlchemyAssertion<Set<E>> emptySet()
    {
        return new AlchemyAssertion<Set<E>>()
        {
            @Override
            public void check(Set<E> set) throws FailedAssertionException
            {
                CollectionAssertions.<E>emptyCollection().check(set);

            }
        };
    }

    public static <E> AlchemyAssertion<List<E>> listContaining(@Required final E element) throws IllegalArgumentException
    {
        checkNotNull(element, "cannot check for null");

        return new AlchemyAssertion<List<E>>()
        {
            @Override
            public void check(List<E> list) throws FailedAssertionException
            {
                notNull().check(list);
                if (!list.contains(element))
                {
                    throw new FailedAssertionException(element + " not found in List");
                }
            }
        };
    }

    public static <E, C extends Collection<E>> AlchemyAssertion<C> collectionContaining(@Required final E element) throws IllegalArgumentException
    {
        checkNotNull(element, "cannot check for null");

        return new AlchemyAssertion<C>()
        {
            @Override
            public void check(C collection) throws FailedAssertionException
            {
                notNull().check(collection);

                if (!collection.contains(element))
                {
                    throw new FailedAssertionException(element + " not found in Collection");
                }
            }
        };
    }

    /**
     * Checks that the {@link Collection} contains ALL of the specified values.
     *
     * @param <E>
     * @param <C>
     * @param first
     * @param andOther
     * @return
     * @throws IllegalArgumentException
     */
    public static <E, C extends Collection<E>> AlchemyAssertion<C> collectionContainingAll(@Required final E first, @Optional final E... andOther) throws IllegalArgumentException
    {
        checkNotNull(first, "first argument cannot be null");

        if (andOther == null || andOther.length == 0)
        {
            return collectionContaining(first);
        }

        return new AlchemyAssertion<C>()
        {
            @Override
            public void check(C collection) throws FailedAssertionException
            {
                notNull().check(collection);

                collectionContaining(first).check(collection);

                List<E> arguments = Arrays.asList(andOther);

                for (E argument : arguments)
                {
                    if (!collection.contains(argument))
                    {
                        throw new FailedAssertionException("Element not found in Collection: " + argument);
                    }
                }
            }
        };
    }

    /**
     * Checks whether a collection contains at least one of the specified parameters.
     *
     * @param <E>
     * @param <C>
     * @param first
     * @param orOthers
     * @return
     * @throws IllegalArgumentException
     */
    public static <E, C extends Collection<E>> AlchemyAssertion<C> collectionContainingAtLeastOnceOf(@Required final E first, @Optional final E... orOthers) throws IllegalArgumentException
    {
        checkNotNull(first, "first argument cannot be null");

        if (orOthers == null || orOthers.length == 0)
        {
            return collectionContaining(first);
        }

        return new AlchemyAssertion<C>()
        {
            @Override
            public void check(C collection) throws FailedAssertionException
            {
                if (collection.contains(first))
                {
                    return;
                }

                for (E argument : orOthers)
                {
                    if (collection.contains(argument))
                    {
                        return;
                    }
                }

                throw new FailedAssertionException("Collection does not contain any of : " + first + ", " + Arrays.toString(orOthers));

            }
        };
    }

    public static <K, V> AlchemyAssertion<Map<K, V>> mapWithKey(@Required final K key) throws IllegalArgumentException
    {
        checkNotNull(key, "key cannot be null");

        return new AlchemyAssertion<Map<K, V>>()
        {
            @Override
            public void check(Map<K, V> map) throws FailedAssertionException
            {
                notNull().check(map);

                if (!map.containsKey(key))
                {
                    throw new FailedAssertionException(format("Expected Key %s in Map", key));
                }
            }
        };
    }

    public static <K, V> AlchemyAssertion<Map<K, V>> mapWithKeyValue(@Required final K key, final V value) throws IllegalArgumentException
    {
        checkNotNull(key, "key cannot be null");

        return new AlchemyAssertion<Map<K, V>>()
        {
            @Override
            public void check(Map<K, V> map) throws FailedAssertionException
            {
                CollectionAssertions.<K, V>mapWithKey(key).check(map);

                V valueInMap = map.get(key);

                if (!Objects.equals(value, valueInMap))
                {
                    throw new FailedAssertionException(format("Value in Map [%s] does not match expcted value %s", valueInMap, value));
                }
            }
        };
    }

    public static <K, V> AlchemyAssertion<K> keyInMap(@Required final Map<K, V> map) throws IllegalArgumentException
    {
        checkNotNull(map, "map cannot be null");

        final Consumer<K> failAssertion = new Consumer<>();

        return new AlchemyAssertion<K>()
        {
            @Override
            public void check(K key) throws FailedAssertionException
            {
                notNull().check(key);

                if (!map.containsKey(key))
                {
                    failAssertion.accept(key);
                }
            }
        };
    }

    public static <K, V> AlchemyAssertion<V> valueInMap(@Required final Map<K, V> map) throws IllegalArgumentException
    {
        checkNotNull(map, "map cannot be null");

        final Consumer<V> failAssertion = new Consumer<>();

        return new AlchemyAssertion<V>()
        {
            @Override
            public void check(V value) throws FailedAssertionException
            {
                notNull().check(value);

                if (!map.containsValue(value))
                {
                    failAssertion.accept(value);
                }
            }
        };
    }

    public static <E> AlchemyAssertion<E> elementInCollection(@NonEmpty final Collection<E> collection) throws IllegalArgumentException
    {
        checkNotNull(collection, "collection cannot be null");

        return new AlchemyAssertion<E>()
        {
            @Override
            public void check(E element) throws FailedAssertionException
            {
                notNull().check(element);

                if (!collection.contains(element))
                {
                    throw new FailedAssertionException(format("Expected element [%s] to be in collection", element));
                }
            }
        };
    }

    public static <C extends Collection> AlchemyAssertion<C> collectionOfSize(@Positive final int size) throws IllegalArgumentException
    {
        Checks.Internal.checkThat(size >= 0, "size must be >= 0");

        return new AlchemyAssertion<C>()
        {
            @Override
            public void check(C collection) throws FailedAssertionException
            {
                CollectionAssertions.nonEmptyCollection().check(collection);

                int actualSize = collection.size();

                if (actualSize != size)
                {
                    throw new FailedAssertionException(format("Expected collection with size [%s] but is instead [%s]",
                                                              size,
                                                              actualSize));
                }
            }
        };
    }


    private static class Consumer<K>
    {
        void accept(K key)
        {
            throw new FailedAssertionException(format("Expected key [%s] to be in map", key));
        }
    }
}
