/*
 * Copyright © 2026. Sir Wellington.
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

package tech.sirwellington.alchemy.arguments.assertions;

import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;

import java.util.*;

import static java.text.MessageFormat.format;
import static tech.sirwellington.alchemy.arguments.internal.Checks.checkNotNull;
import static tech.sirwellington.alchemy.arguments.internal.Checks.checkThat;

/**
 * Collection and Map assertion utilities.
 *
 * @author SirWellington
 */
public final class CollectionAssertions {

    private CollectionAssertions() {
        throw new AssertionError("Utility class");
    }

    /**
     * Asserts that the collection is not null and not empty.
     */
    public static <E, C extends Collection<E>> AlchemyAssertion<C> nonEmptyCollection() {
        return collection -> {
            checkNotNull(collection, "Collection cannot be null");

            if (collection.isEmpty()) {
                throw new FailedAssertionException("Collection is empty");
            }
        };
    }

    /**
     * Asserts that the List is not null and not empty.
     */
    public static <E> AlchemyAssertion<List<E>> nonEmptyList() {
        return list -> {
            checkNotNull(list, "List cannot be null");

            if (list.isEmpty()) {
                throw new FailedAssertionException("List is empty");
            }
        };
    }

    /**
     * Asserts that the Set is not null and not empty.
     */
    public static <E> AlchemyAssertion<Set<E>> nonEmptySet() {
        return set -> {
            checkNotNull(set, "Set cannot be null");

            if (set.isEmpty()) {
                throw new FailedAssertionException("Set is empty");
            }
        };
    }

    /**
     * Asserts that the Map is not null and not empty.
     */
    public static <K, V> AlchemyAssertion<Map<K, V>> nonEmptyMap() {
        return map -> {
            checkNotNull(map, "Map cannot be null");

            if (map.isEmpty()) {
                throw new FailedAssertionException("Map is empty");
            }
        };
    }

    /**
     * Asserts that the array is not null and not empty.
     */
    public static <E> AlchemyAssertion<E[]> nonEmptyArray() {
        return array -> {
            checkNotNull(array, "Array cannot be null");

            if (array.length == 0) {
                throw new FailedAssertionException("Array is empty");
            }
        };
    }

    // ——— Empty Collection Assertions ———

    /**
     * Asserts that the collection is not null and is empty.
     */
    public static <E, C extends Collection<E>> AlchemyAssertion<C> emptyCollection() {
        return collection -> {
            checkNotNull(collection, "Collection cannot be null");

            if (!collection.isEmpty()) {
                throw new FailedAssertionException(
                    format("Expected an empty collection, but it has size [{0}]", collection.size())
                );
            }
        };
    }

    public static <E> AlchemyAssertion<List<E>> emptyList() {
        return emptyCollection();
    }

    public static <E> AlchemyAssertion<Set<E>> emptySet() {
        return emptyCollection();
    }

    public static <K, V> AlchemyAssertion<Map<K, V>> emptyMap() {
        return map -> {
            checkNotNull(map, "Map cannot be null");

            if (!map.isEmpty()) {
                throw new FailedAssertionException(
                    format("Expected an empty map, but instead [{0}]", map)
                );
            }
        };
    }

    // ——— Containment Assertions ———

    /**
     * Asserts that the list contains the specified element.
     */
    public static <E> AlchemyAssertion<List<E>> listContaining(E element) {
        checkNotNull(element, "Cannot check for null element");

        return list -> {
            checkNotNull(list, "List cannot be null");

            if (!list.contains(element)) {
                throw new FailedAssertionException(element + " not found in List");
            }
        };
    }

    /**
     * Asserts that the collection contains the specified element.
     */
    public static <E, C extends Collection<E>> AlchemyAssertion<C> collectionContaining(E element) {
        checkNotNull(element, "Cannot check for null element");

        return collection -> {
            checkNotNull(collection, "Collection cannot be null");

            if (!collection.contains(element)) {
                throw new FailedAssertionException(element + " not found in Collection");
            }
        };
    }

    /**
     * Asserts that the collection contains all the specified elements.
     */
    @SafeVarargs
    public static <E, C extends Collection<E>> AlchemyAssertion<C> collectionContainingAll(E first, E... rest) {
        checkNotNull(first, "First argument cannot be null");

        if (rest.length == 0) {
            return collectionContaining(first);
        }

        return collection -> {
            checkNotNull(collection, "Collection cannot be null");

            collectionContaining(first).check(collection);

            var missing = Arrays.stream(rest)
                                .filter(e -> !collection.contains(e))
                                .toList();

            if (!missing.isEmpty()) {
                throw new FailedAssertionException(
                    "Element(s) not found in Collection: " + missing
                );
            }
        };
    }

    /**
     * Asserts that the collection contains at least one of the specified elements.
     */
    @SafeVarargs
    public static <E, C extends Collection<E>> AlchemyAssertion<C> collectionContainingAtLeastOneOf(
        E first,
        E... others
    ) {
        checkNotNull(first, "First argument cannot be null");

        if (others.length == 0) {
            return collectionContaining(first);
        }

        return collection -> {
            checkNotNull(collection, "Collection cannot be null");

            boolean found = collection.contains(first) ||
                Arrays.stream(others).anyMatch(collection::contains);

            if (!found) {
                throw new FailedAssertionException(
                    format(
                        "Collection does not contain any of [{0}, {1}]",
                        first,
                        Arrays.toString(others)
                    )
                );
            }
        };
    }

    // ——— Map Assertions ———

    public static <K, V> AlchemyAssertion<Map<K, V>> mapWithKey(K key) {
        checkNotNull(key, "Key cannot be null");

        return map -> {
            checkNotNull(map, "Map cannot be null");

            if (!map.containsKey(key)) {
                throw new FailedAssertionException(
                    format("Expected key [{0}] in Map", key)
                );
            }
        };
    }

    public static <K, V> AlchemyAssertion<Map<K, V>> mapWithKeyValue(K key, V value) {
        checkNotNull(key, "Key cannot be null");

        return map -> {
            CollectionAssertions.<K, V>mapWithKey(key).check(map);

            V actualValue = map.get(key);
            if (!Objects.equals(value, actualValue)) {
                throw new FailedAssertionException(
                    format("Value in Map [{0}] does not match expected value [{1}] ", actualValue, value)
                );
            }
        };
    }

    public static <K, V> AlchemyAssertion<K> keyInMap(Map<K, V> map) {
        checkNotNull(map, "Map cannot be null");

        return key -> {
            checkNotNull(key, "Key cannot be null");

            if (!map.containsKey(key)) {
                throw new FailedAssertionException(
                    format("Expected key [{0}] to be in map", key)
                );
            }
        };
    }

    public static <K, V> AlchemyAssertion<V> valueInMap(Map<K, V> map) {
        checkNotNull(map, "Map cannot be null");

        return value -> {
            checkNotNull(value, "Value cannot be null");

            if (!map.containsValue(value)) {
                throw new FailedAssertionException(
                    format("Expected value [{0}] to be in map", value)
                );
            }
        };
    }

    public static <E> AlchemyAssertion<E> elementInCollection(Collection<E> collection) {
        checkNotNull(collection, "Collection cannot be null");

        return element -> {
            checkNotNull(element, "Element cannot be null");

            if (!collection.contains(element)) {
                throw new FailedAssertionException(
                    format("Expected element [{0}] to be in collection", element)
                );
            }
        };
    }

    // ——— Size Assertions ———

    public static <E, C extends Collection<E>> AlchemyAssertion<C> collectionOfSize(int size) {
        checkThat(size >= 0, "Size must be ≥ 0");

        return collection -> {
            CollectionAssertions.<E, C>nonEmptyCollection().check(collection);

            int actualSize = collection.size();
            if (actualSize != size) {
                throw new FailedAssertionException(
                    format(
                        "Expected collection with size [{0}] but is instead [{1}]",
                        size,
                        actualSize
                    )
                );
            }
        };
    }
}
