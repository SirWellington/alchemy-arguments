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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;

import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;

import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;

/**
 *
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
     * @param <T>
     *
     * @return
     */
    public static <T> AlchemyAssertion<Collection<T>> nonEmptyCollection()
    {
        return (collection) ->
        {
            notNull().check(collection);

            if (collection.isEmpty())
            {
                throw new FailedAssertionException("Collection is empty");
            }
        };
    }

    /**
     * Asserts that the List is not null and not empty
     *
     * @param <T>
     *
     * @return
     */
    public static <T> AlchemyAssertion<List<T>> nonEmptyList()
    {
        return (list) ->
        {
            notNull().check(list);

            if (list.isEmpty())
            {
                throw new FailedAssertionException("List is empty");
            }
        };

    }

    /**
     * Asserts that the Map is not null and not empty
     *
     * @param <K>
     * @param <V>
     *
     * @return
     */
    public static <K, V> AlchemyAssertion<Map<K, V>> nonEmptyMap()
    {
        return (map) ->
        {
            notNull().check(map);

            if (map.isEmpty())
            {
                throw new FailedAssertionException("Map is empty");
            }

        };
    }

    public static <T> AlchemyAssertion<T[]> nonEmptyArray()
    {
        return (array) ->
        {
            notNull().check(array);

            if (array.length == 0)
            {
                throw new FailedAssertionException("Array is empty");
            }
        };
    }

}
