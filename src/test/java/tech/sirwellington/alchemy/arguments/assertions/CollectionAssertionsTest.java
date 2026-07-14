package tech.sirwellington.alchemy.arguments.assertions;

import java.util.*;

import org.junit.jupiter.api.RepeatedTest;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.Arguments;
import tech.sirwellington.alchemy.test.AlchemyTest;
import tech.sirwellington.alchemy.test.generation.GenerateList;
import tech.sirwellington.alchemy.test.generation.GenerateMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static tech.sirwellington.alchemy.arguments.TestHelpers.assertThrowsFailedAssertion;
import static tech.sirwellington.alchemy.arguments.TestHelpers.randomElementFrom;
import static tech.sirwellington.alchemy.arguments.assertions.CollectionAssertions.*;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;

@AlchemyTest
class CollectionAssertionsTest {
    private static final int TEST_ITERATIONS = 50;
    
    private Set<String> emptySet = Collections.emptySet();
    private List<String> emptyList = Collections.emptyList();

    @GenerateList(String.class)
    private List<String> strings;

    @GenerateMap(keyType = String.class, valueType = String.class)
    private Map<String, String> map;

    @RepeatedTest(TEST_ITERATIONS)
    public void testNonEmptyCollection() {
        var instance = CollectionAssertions.<String>nonEmptyCollection();
        assertThat(instance, notNullValue());

        instance.check(strings);
        assertThrowsFailedAssertion(() -> instance.check(null));
        assertThrowsFailedAssertion(() -> instance.check(Collections.emptySet()));
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testNonEmptyList() {
        AlchemyAssertion<List<String>> instance = nonEmptyList();
        assertThat(instance, notNullValue());

        instance.check(strings);
        assertThrowsFailedAssertion(() -> instance.check(null));
        assertThrowsFailedAssertion(() -> instance.check(emptyList));
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testNonEmptySet() {
        AlchemyAssertion<Set<String>> instance = nonEmptySet();
        assertThat(instance, notNullValue());

        var setOfStrings = Set.copyOf(strings);
        instance.check(setOfStrings);

        assertThrowsFailedAssertion(() -> instance.check(new HashSet<>()));
        assertThrowsFailedAssertion(() -> instance.check(null));
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testNonEmptyMap() {
        AlchemyAssertion<Map<String,Integer>> instance = nonEmptyMap();
        assertThat(instance, notNullValue());

        var map = new HashMap<String, Integer>();
        for (int i = 0; i < 5; i++) {
            map.put("key" + i, i);
        }
        instance.check(map);

        assertThrowsFailedAssertion(() -> instance.check(Collections.emptyMap()));
        assertThrowsFailedAssertion(() -> instance.check(null));
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testNonEmptyArray() {
        AlchemyAssertion<String[]> instance = nonEmptyArray();
        assertThat(instance, notNullValue());

        var array = strings.toArray(new String[0]);
        instance.check(array);

        assertThrowsFailedAssertion(() -> instance.check(null));
        assertThrowsFailedAssertion(() -> instance.check(new String[0]));
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testListContaining() {
        // Given
        var element = randomElementFrom(strings);
        var instance = listContaining(element);
        // Then
        Tests.checkForNullCase(instance);

        // Then
        instance.check(strings);
        // Then
        assertThrowsFailedAssertion(
            () -> instance.check(Collections.singletonList("xyz"))
        );
        assertThrowsFailedAssertion(
            () -> instance.check(Collections.emptyList())
        );
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testListContainingWithBadArgs() {
        assertThrows(() -> listContaining(null));
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testCollectionContaining() {
        var element = randomElementFrom(strings);
        var instance = collectionContaining(element);
        assertThat(instance, notNullValue());

        instance.check(strings);
        assertThrowsFailedAssertion(() -> instance.check(Collections.singletonList("xyz")));
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testCollectionContainingWithBadArgs() {
        assertThrows(() -> collectionContaining(null)).isIllegalArgumentException();
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testCollectionContainingAll() {
        var args = strings.subList(0, 2);
        var collection = new ArrayList<>(strings);
        collection.addAll(args);

        var first = args.getFirst();
        var others = args.subList(1, args.size()).toArray(new String[0]);

        var instance = collectionContainingAll(first, others);
        assertThat(instance, notNullValue());

        instance.check(collection);
        assertThrowsFailedAssertion(() -> instance.check(Collections.singletonList("xyz")));
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testCollectionContainingAllWithBadArgs() {
        assertThrows(() -> collectionContainingAll((String[]) null))
            .isIllegalArgumentException();
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testCollectionContainingAtLeastOneOf() {
        var args = strings.subList(0, 2);
        var collection = new ArrayList<>(strings);
        collection.addAll(args);

        var first = args.getFirst();
        var others = args.subList(1, args.size()).toArray(new String[0]);

        var instance = collectionContainingAtLeastOneOf(first, others);
        assertThat(instance, notNullValue());

        instance.check(collection);
        assertThrowsFailedAssertion(() -> instance.check(Collections.singletonList("xyz")));

        // Re-add first to make it pass
        collection.add(first);
        instance.check(collection);
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testCollectionContainingAtLeastOneOfWithBadArgs() {
        assertThrows(() -> collectionContainingAtLeastOneOf((String[]) null))
            .isIllegalArgumentException();
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testMapWithKey() {
        // Given
        var map = new HashMap<String, String>();
        for (int i = 0; i < 5; i++) {
            map.put("key" + i, "val" + i);
        }
        var key = map.keySet().iterator().next();

        var instance = CollectionAssertions.<String, String>mapWithKey(key);
        assertThat(instance, notNullValue());

        instance.check(map);
        assertThrowsFailedAssertion(() -> instance.check(Collections.singletonMap("bad", "val")));
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testMapWithKeyValue() {
        var map = new HashMap<String, Integer>();
        
        for (int i = 0; i < 5; i++) {
            map.put(String.valueOf(i), i);
        }

        Map.Entry<String, Integer> entry = map.entrySet().iterator().next();
        var instance = mapWithKeyValue(entry.getKey(), entry.getValue());
        assertThat(instance, notNullValue());

        instance.check(map);
        assertThrowsFailedAssertion(() -> instance.check(Collections.singletonMap("bad", -1)));
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testKeyInMap() {
        var map = new HashMap<String, String>();
        for (int i = 0; i < 5; i++) {
            map.put("key" + i, "val" + i);
        }
        var assertion = keyInMap(map);

        var anyKey = map.keySet().iterator().next();
        assertion.check(anyKey);

        assertThrowsFailedAssertion(() -> assertion.check("nonexistent"));
        assertThrowsFailedAssertion(() -> assertion.check(null));

        assertThrows(() -> keyInMap((Map<?, ?>) null))
            .isIllegalArgumentException();
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testKeyInMapWithEmptyMap() {
        var assertion = keyInMap(Collections.emptyMap());

        for (String s : strings) {
            assertThrowsFailedAssertion(() -> assertion.check(s));
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testValueInMap() {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            map.put("key" + i, "val" + i);
        }
        var assertion = valueInMap(map);

        String anyValue = map.values().iterator().next();
        assertion.check(anyValue);

        assertThrowsFailedAssertion(() -> assertion.check("nonexistent"));
        assertThrowsFailedAssertion(() -> assertion.check(null));

        assertThrows(() -> valueInMap((Map<?, ?>) null))
            .isIllegalArgumentException();
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testValueInMapWithEmptyMap() {
        var assertion = valueInMap(Collections.emptyMap());

        for (String s : strings) {
            assertThrowsFailedAssertion(() -> assertion.check(s));
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testElementInCollection() {
        var assertion = elementInCollection(strings);

        var anyValue = strings.get(Math.abs(new Random().nextInt()) % strings.size());
        assertion.check(anyValue);

        assertThrowsFailedAssertion(() -> assertion.check("nonexistent"));
        assertThrowsFailedAssertion(() -> assertion.check(null));

        assertThrows(() -> elementInCollection((List<?>) null))
            .isIllegalArgumentException();
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testCollectionOfSize() {
        // Given
        int size = strings.size();
        var instance = CollectionAssertions.<String, Collection<String>>collectionOfSize(size);

        // Then
        assertDoesNotThrow(() -> instance.check(strings));

        // When
        strings.add("extra");

        // Then
        assertThrowsFailedAssertion(() -> instance.check(strings));

        // Then
        var newSize = strings.size();
        assertDoesNotThrow(
            () -> Arguments.checkThat(strings).isA(collectionOfSize(newSize))
        );
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testCollectionOfSizeWithBadArgs() {
        int badSize = -1;
        assertThrows(() -> collectionOfSize(badSize))
            .isIllegalArgumentException();
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testEmptyCollection() {
        // Given
        var instance = CollectionAssertions.<String,Collection<String>>emptyCollection();
        // Then
        assertThat(instance, notNullValue());

        // Then
        assertDoesNotThrow(
            () -> instance.check(Collections.emptySet())
        );
        assertThrowsFailedAssertion(() -> instance.check(strings));
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testEmptyList() {
        // When
        var instance = emptyList();
        // Then
        assertThat(instance, notNullValue());

        // Then
        assertDoesNotThrow(
            () -> instance.check(Collections.emptyList())
        );
        assertThrowsFailedAssertion(
            () -> Arguments.checkThat(strings).is(emptyList())
        );
    }

    @RepeatedTest(TEST_ITERATIONS)
    public void testEmptySet() {
        // When
        var instance = emptySet();
        // Then
        assertThat(instance, notNullValue());

        assertDoesNotThrow(
            () -> Arguments.checkThat(emptySet).isA(emptySet())
        );
        var nonEmpty = Set.copyOf(strings);
        assertThrowsFailedAssertion(
            () -> Arguments.checkThat(nonEmpty).isA(emptySet())
        );
    }
}
