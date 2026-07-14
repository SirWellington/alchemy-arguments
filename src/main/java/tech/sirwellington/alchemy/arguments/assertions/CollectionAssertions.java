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

import java.util.*;

import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.internal.Checks;

import static tech.sirwellington.alchemy.arguments.internal.Checks.*;

/**
 * Assertions to {@link Collection Collection types} and {@link Map Maps}.
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
    public static <E> AlchemyAssertion<Collection<E>> nonEmptyCollection() {
        return collection -> {
            if (Checks.isNullOrEmpty(collection)) {
                failAssertion("Collection is empty");
            }
        };
    }

    /**
     * Asserts that the List is not null and not empty.
     */
    public static <E> AlchemyAssertion<List<E>> nonEmptyList() {
        return list -> {
            if (Checks.isNullOrEmpty(list)) {
                failAssertion("List is empty");
            }
        };
    }

    /**
     * Asserts that the Set is not null and not empty.
     */
    public static <E> AlchemyAssertion<Set<E>> nonEmptySet() {
        return set -> {
            if (Checks.isNullOrEmpty(set)) {
                failAssertion("Set is empty");
            }
        };
    }

    /**
     * Asserts that the Map is not null and not empty.
     */
    public static <K, V> AlchemyAssertion<Map<K, V>> nonEmptyMap() {
        return map -> {
            if (Checks.isNullOrEmpty(map)) {
                failAssertion("Map is empty");
            }
        };
    }

    /**
     * Asserts that the array is not null and not empty.
     */
    public static <E> AlchemyAssertion<E[]> nonEmptyArray() {
        return array -> {
            if (array == null || array.length == 0) {
                failAssertion("Array is empty");
            }
        };
    }

    // ——— Empty Collection Assertions ———

    /**
     * Asserts that the collection is not null and is empty.
     */
    public static <E, C extends Collection<E>> AlchemyAssertion<C> emptyCollection() {
        return collection -> {
            if (collection == null) {
                failAssertion("Collection is null");
            }

            if (!collection.isEmpty()) {
                failAssertion(
                    "Expected an empty collection, but it has size [{0}]",
                    collection.size()
                );
            }
        };
    }

    /**
     * Asserts that the {@link List} is empty.
     */
    public static <E> AlchemyAssertion<List<E>> emptyList() {
        return emptyCollection();
    }

    /**
     * Asserts that the {@link Set} is empty.
     */
    public static <E> AlchemyAssertion<Set<E>> emptySet() {
        return emptyCollection();
    }

    /**
     * Asserts that the {@link Map} is empty.
     */
    public static <K, V> AlchemyAssertion<Map<K, V>> emptyMap() {
        return map -> {
            if (map == null) {
                failAssertion("Map is null, not empty.");
            }
            if (!map.isEmpty()) {
                failAssertion(
                    "Expected an empty map, but instead a map with {0} elements",
                    map.size()
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
            if (isNullOrEmpty(list)) {
                failAssertion("List is null and does not contain {0}", element);
            }

            if (!list.contains(element)) {
                failAssertion("Element [{0}] not found in List: [{1}]", element, list);
            }
        };
    }

    /**
     * Asserts that the collection contains the specified element.
     */
    public static <E, C extends Collection<E>> AlchemyAssertion<C> collectionContaining(E element) {
        checkNotNull(element, "Cannot check for null element");

        return collection -> {
            if (isNullOrEmpty(collection)) {
                failAssertion("Collection is null or empty and does not contain {0}", element);
            }

            if (!collection.contains(element))
                failAssertion(element + " not found in Collection");
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
            if (isNullOrEmpty(collection)) {
                failAssertion("Collection is empty or null");
            }

            collectionContaining(first).check(collection);

            var missing = Arrays.stream(rest)
                                .filter(e -> !collection.contains(e))
                                .toList();

            if (!missing.isEmpty()) {
                failAssertion(
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

            var found = collection.contains(first) || Arrays.stream(others).anyMatch(collection::contains);

            if (!found) {
                failAssertion(
                    "Collection does not contain any of [{0}, {1}]",
                    first,
                    Arrays.toString(others)
                );
            }
        };
    }

    // ——— Map Assertions ———

    public static <K, V> AlchemyAssertion<Map<K, V>> mapWithKey(K key) {
        checkNotNull(key, "Key cannot be null");

        return map -> {
            if (map == null) {
                failAssertion("Map was null");
            }

            if (!map.containsKey(key)) {
                failAssertion("Expected key [{0}] in Map", key);
            }
        };
    }

    public static <K, V> AlchemyAssertion<Map<K, V>> mapWithKeyValue(K key, V value) {
        checkNotNull(key, "Key cannot be null");

        return map -> {
            CollectionAssertions.<K, V>mapWithKey(key).check(map);

            V actualValue = map.get(key);
            if (!Objects.equals(value, actualValue)) {
                failAssertion(
                    "Value in Map [{0}] does not match expected value [{1}] ",
                    actualValue,
                    value
                );
            }
        };
    }

    public static <K, V> AlchemyAssertion<K> keyInMap(Map<K, V> map) {
        checkNotNull(map, "Map cannot be null");

        return key -> {
            if (!map.containsKey(key)) {
                failAssertion("Expected key [{0}] to be in map", key);
            }
        };
    }

    public static <K, V> AlchemyAssertion<V> valueInMap(Map<K, V> map) {
        checkNotNull(map, "Map cannot be null");

        return value -> {
            if (!map.containsValue(value)) {
                failAssertion("Expected value [{0}] to be in map", value);
            }
        };
    }

    public static <E> AlchemyAssertion<E> elementInCollection(Collection<E> collection) {
        checkNotNull(collection, "Collection cannot be null");

        return element -> {
            if (!collection.contains(element)) {
                failAssertion("Expected element [{0}] to be in collection", element);
            }
        };
    }

    // ——— Size Assertions ———

    public static <E, C extends Collection<E>> AlchemyAssertion<C> collectionOfSize(int size) {
        checkThat(size >= 0, "Size must be ≥ 0");

        return collection -> {
            CollectionAssertions.<E>nonEmptyCollection().check(collection);

            int actualSize = collection.size();
            if (actualSize != size) {
                failAssertion(
                    "Expected collection with size [{0}] but is instead [{1}]",
                    size,
                    actualSize
                );
            }
        };
    }
}
