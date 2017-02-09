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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat;
import tech.sirwellington.alchemy.test.junit.runners.GenerateList;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.CollectionAssertions.collectionOfSize;
import static tech.sirwellington.alchemy.arguments.assertions.CollectionAssertions.nonEmptySet;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.listOf;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.mapOf;
import static tech.sirwellington.alchemy.generator.NumberGenerators.negativeIntegers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.positiveIntegers;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphanumericString;
import static tech.sirwellington.alchemy.generator.StringGenerators.hexadecimalString;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@RunWith(AlchemyTestRunner.class)
@Repeat(50)
public class CollectionAssertionsTest
{

    @GenerateList(String.class)
    private List<String> strings;

    @Before
    public void setUp()
    {
    }

    @DontRepeat
    @Test
    public void testCannotInstantiateClass()
    {
        assertThrows(() -> CollectionAssertions.class.newInstance());

        assertThrows(() -> new CollectionAssertions())
            .isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testNonEmptyCollection()
    {

        AlchemyAssertion<Collection<String>> instance = CollectionAssertions.nonEmptyCollection();
        assertThat(instance, notNullValue());

        instance.check(strings);

        assertThrows(() -> instance.check(null))
            .isInstanceOf(FailedAssertionException.class);

        assertThrows(() -> instance.check(Collections.emptySet()))
            .isInstanceOf(FailedAssertionException.class);

    }

    @Test
    public void testNonEmptyList()
    {
        AlchemyAssertion<List<String>> instance = CollectionAssertions.nonEmptyList();
        assertThat(instance, notNullValue());

        instance.check(strings);

        assertThrows(() -> instance.check(null))
            .isInstanceOf(FailedAssertionException.class);

        assertThrows(() -> instance.check(Collections.emptyList()))
            .isInstanceOf(FailedAssertionException.class);
    }
    
      @Test
    public void testNonEmptySet()
    {
        AlchemyAssertion<Set<String>> instance = CollectionAssertions.nonEmptySet();
        assertThat(instance, notNullValue());
        
        Set<String> setOfStrings = new HashSet<>(strings);
        instance.check(setOfStrings);
        
        checkThat(setOfStrings).is(nonEmptySet());
        
        Set<String> emptySet = new HashSet<>();
        assertThrows(() -> instance.check(emptySet))
            .isInstanceOf(FailedAssertionException.class);
        
        assertThrows(() -> instance.check(null))
            .isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testNonEmptyMap()
    {
        AlchemyAssertion<Map<String, Integer>> instance = CollectionAssertions.nonEmptyMap();

        Map<String, Integer> map = mapOf(alphabeticString(),
                                         positiveIntegers(),
                                         40);

        instance.check(map);

        assertThrows(() -> instance.check(Collections.emptyMap()))
            .isInstanceOf(FailedAssertionException.class);

        assertThrows(() -> instance.check(null))
            .isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testNonEmptyArray()
    {
        AlchemyAssertion<String[]> instance = CollectionAssertions.nonEmptyArray();
        assertThat(instance, notNullValue());

        assertThrows(() -> instance.check(null))
            .isInstanceOf(FailedAssertionException.class);

        String[] stringArray = strings.toArray(new String[0]);

        instance.check(stringArray);

        String[] emptyStringArray = new String[]
        {
        };

        assertThrows(() -> instance.check(emptyStringArray))
            .isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testListContaining()
    {
        String string = strings.stream().findAny().orElseGet(alphabeticString());

        AlchemyAssertion<List<String>> instance = CollectionAssertions.listContaining(string);
        assertThat(instance, notNullValue());
        instance.check(strings);

        List<String> hex = listOf(hexadecimalString(100));

        assertThrows(() -> instance.check(hex))
            .isInstanceOf(FailedAssertionException.class);
    }
    
    @Test
    public void testListContainingWithBadArgs() throws Exception
    {
        assertThrows(() -> CollectionAssertions.listContaining(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testCollectionContaining()
    {
        String string = strings.stream().findAny().orElseGet(alphabeticString());

        AlchemyAssertion<Collection<String>> instance = CollectionAssertions.collectionContaining(string);
        assertThat(instance, notNullValue());
        instance.check(strings);

        List<String> hex = listOf(hexadecimalString(100));

        assertThrows(() -> instance.check(hex))
            .isInstanceOf(FailedAssertionException.class);
    }

    @DontRepeat
    @Test
    public void testCollectionContainingWithBadArgs() throws Exception
    {
        assertThrows(() -> CollectionAssertions.collectionContaining(null))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testCollectionContainingAll() throws Exception
    {
        List<String> args = listOf(alphabeticString());
        List<String> collection = listOf(alphabeticString());
        collection.addAll(args);
        
        String first = args.get(0);
        String[] others = args.subList(1, args.size()).toArray(new String[0]);
        
        AlchemyAssertion<Collection<String>> instance = CollectionAssertions.collectionContainingAll(first, others);
        assertThat(instance, notNullValue());
        instance.check(collection);
        
        List<String> otherCollection = listOf(alphabeticString());
        assertThrows(() -> instance.check(otherCollection)).isInstanceOf(FailedAssertionException.class);
    }
    
    @DontRepeat
    @Test
    public void testCollectionContainingAllWithBadArgs() throws Exception
    {
        assertThrows(() -> CollectionAssertions.collectionContainingAll(null)).isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testMapWithKey()
    {
        Map<Integer, String> map = mapOf(positiveIntegers(), hexadecimalString(100), 100);

        Integer key = map.keySet().stream().findAny().get();

        AlchemyAssertion<Map<Integer, String>> instance = CollectionAssertions.mapWithKey(key);
        assertThat(instance, notNullValue());

        instance.check(map);

        Map<Integer, String> badMap = mapOf(negativeIntegers(), hexadecimalString(100), 100);
        assertThrows(() -> instance.check(badMap));
    }

    @Test
    public void testMapWithKeyValue()
    {
        Map<Integer, String> map = mapOf(positiveIntegers(), alphabeticString(), 100);

        Map.Entry<Integer, String> anyEntry = map.entrySet().stream().findAny().get();

        AlchemyAssertion<Map<Integer, String>> instance;
        instance = CollectionAssertions.mapWithKeyValue(anyEntry.getKey(), anyEntry.getValue());
        assertThat(instance, notNullValue());

        //Should pass OK
        instance.check(map);

        Map<Integer, String> badMap = mapOf(negativeIntegers(), hexadecimalString(100), 100);
        assertThrows(() -> instance.check(badMap));
    }

    @Test
    public void testKeyInMap()
    {
        Map<String, String> map = mapOf(alphabeticString(), alphanumericString(), 25);

        AlchemyAssertion<String> assertion = CollectionAssertions.keyInMap(map);
        assertThat(assertion, notNullValue());

        String anyKey = map.keySet().stream().findAny().get();
        assertion.check(anyKey);

        String randomKey = one(hexadecimalString(42));
        assertThrows(() -> assertion.check(randomKey))
            .isInstanceOf(FailedAssertionException.class);

        //Edge cases
        assertThrows(() -> assertion.check(null))
            .isInstanceOf(FailedAssertionException.class);

        assertThrows(() -> CollectionAssertions.keyInMap(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testKeyInMapWithEmptyMap()
    {
        AlchemyAssertion<Object> assertion = CollectionAssertions.keyInMap(Collections.emptyMap());

        for (String string : strings)
        {
            assertThrows(() -> assertion.check(string))
                .isInstanceOf(FailedAssertionException.class);
        }
    }

    @Test
    public void testValueInMap()
    {

        Map<String, String> map = mapOf(alphanumericString(), alphabeticString(), 24);

        AlchemyAssertion<String> assertion = CollectionAssertions.valueInMap(map);
        assertThat(assertion, notNullValue());

        String anyValue = map.values().stream().findAny().get();
        assertion.check(anyValue);

        String randomValue = one(hexadecimalString(10));
        assertThrows(() -> assertion.check(randomValue))
            .isInstanceOf(FailedAssertionException.class);

        //Edge cases
        assertThrows(() -> assertion.check(null))
            .isInstanceOf(FailedAssertionException.class);

        assertThrows(() -> CollectionAssertions.valueInMap(null))
            .isInstanceOf(IllegalArgumentException.class);

        //Empty map should be ok
        CollectionAssertions.valueInMap(Collections.emptyMap());
    }

    @Test
    public void testValueInMapWithEmptyMap()
    {
        AlchemyAssertion<Object> assertion = CollectionAssertions.valueInMap(Collections.emptyMap());
        assertThat(assertion, notNullValue());

        for (String string : strings)
        {
            assertThrows(() -> assertion.check(string))
                .isInstanceOf(FailedAssertionException.class);
        }
    }

    @Test
    public void testElementInCollection()
    {
        AlchemyAssertion<String> assertion = CollectionAssertions.elementInCollection(strings);
        assertThat(assertion, notNullValue());

        String anyValue = strings.stream().findAny().get();
        assertion.check(anyValue);

        String randomValue = one(hexadecimalString(20));

        assertThrows(() -> assertion.check(randomValue))
            .isInstanceOf(FailedAssertionException.class);

        //Edge cases
        assertThrows(() -> assertion.check(null))
            .isInstanceOf(FailedAssertionException.class);

        assertThrows(() -> CollectionAssertions.elementInCollection(null))
            .isInstanceOf(IllegalArgumentException.class);

        //Empty Collections should be ok
        CollectionAssertions.elementInCollection(Collections.emptyList());

    }

    @Test
    public void testCollectionOfSize()
    {
        int size = strings.size();
        AlchemyAssertion<? super Collection<String>> instance = CollectionAssertions.collectionOfSize(size);
        instance.check(strings);

        strings.add(one(alphabeticString()));

        assertThrows(() -> instance.check(strings))
            .isInstanceOf(FailedAssertionException.class);

        checkThat(strings)
            .is(collectionOfSize(strings.size()));

    }

    @DontRepeat
    @Test
    public void testCollectionOfSizeWithBadArgs()
    {
        int badSize = one(negativeIntegers());

        assertThrows(() -> CollectionAssertions.collectionOfSize(badSize))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testEmptyCollection()
    {
        AlchemyAssertion<Collection<String>> instance = CollectionAssertions.emptyCollection();

        Collection<String> emptyCollection = new ArrayList<>();

        instance.check(emptyCollection);

        assertThrows(() -> instance.check(strings))
            .isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testEmptyList()
    {
        AlchemyAssertion<List<String>> instance = CollectionAssertions.emptyList();

        List<String> emptyList = new LinkedList<>();
        instance.check(emptyList);
        
        assertThrows(() -> instance.check(strings))
            .isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testEmptySet()
    {
        AlchemyAssertion<Set<String>> instance = CollectionAssertions.emptySet();
        
        Set<String> emptySet = new HashSet<>();
        instance.check(emptySet);
        
        Set<String> nonEmptySet = new HashSet<>(strings);
        assertThrows(() -> instance.check(nonEmptySet))
            .isInstanceOf(FailedAssertionException.class);
    }

}
