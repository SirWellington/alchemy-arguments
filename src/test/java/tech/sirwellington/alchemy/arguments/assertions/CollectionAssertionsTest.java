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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;
import tech.sirwellington.alchemy.test.junit.runners.*;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static tech.sirwellington.alchemy.arguments.Arguments.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.listOf;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.mapOf;
import static tech.sirwellington.alchemy.generator.NumberGenerators.*;
import static tech.sirwellington.alchemy.generator.StringGenerators.*;

/**
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
    @Test(expected = IllegalAccessException.class)
    public void testCannotInstantiateClass1() throws IllegalAccessException, InstantiationException
    {
        new CollectionAssertions();
    }

    @DontRepeat
    @Test(expected = IllegalAccessException.class)
    public void testCannotInstantiateClass2() throws IllegalAccessException, InstantiationException
    {
        CollectionAssertions.class.newInstance();
    }

    @Test
    public void testNonEmptyCollection()
    {

        AlchemyAssertion<Collection<String>> instance = CollectionAssertions.nonEmptyCollection();
        assertThat(instance, notNullValue());

        instance.check(strings);
    }

    @Test(expected = FailedAssertionException.class)
    @DontRepeat
    public void testNonEmptyCollectionWithBadArgs1() throws Exception
    {
        AlchemyAssertion<Collection<String>> instance = CollectionAssertions.nonEmptyCollection();
        instance.check(null);

    }

    @Test(expected = FailedAssertionException.class)
    @DontRepeat
    public void testNonEmptyCollectionWithBadArgs2() throws Exception
    {
        AlchemyAssertion<Collection<String>> instance = CollectionAssertions.nonEmptyCollection();
        instance.check(Collections.<String>emptySet());
    }

    @Test
    public void testNonEmptyList()
    {
        AlchemyAssertion<List<String>> instance = CollectionAssertions.nonEmptyList();
        assertThat(instance, notNullValue());

        instance.check(strings);
    }

    @Test(expected = FailedAssertionException.class)
    public void testNonEmptyListWithBadArgs1() throws Exception
    {
        AlchemyAssertion<List<String>> instance = CollectionAssertions.nonEmptyList();

        instance.check(null);

    }

    @Test(expected = FailedAssertionException.class)
    public void testNonEmptyListWithBadArgs2() throws Exception
    {
        AlchemyAssertion<List<String>> instance = CollectionAssertions.nonEmptyList();

        instance.check(Collections.<String>emptyList());

    }

    @Test
    public void testNonEmptySet()
    {
        AlchemyAssertion<Set<String>> instance = CollectionAssertions.nonEmptySet();
        assertThat(instance, notNullValue());

        Set<String> setOfStrings = new HashSet<>(strings);
        instance.check(setOfStrings);

        checkThat(setOfStrings).is(CollectionAssertions.<String>nonEmptySet());

    }

    @DontRepeat
    @Test(expected = FailedAssertionException.class)
    public void testNonEmptySetWithBadArgs1() throws Exception
    {
        AlchemyAssertion<Set<String>> instance = CollectionAssertions.nonEmptySet();

        Set<String> emptySet = new HashSet<>();
        instance.check(emptySet);
    }

    @DontRepeat
    @Test(expected = FailedAssertionException.class)
    public void testNonEmptySetWithBadArgs2() throws Exception
    {
        AlchemyAssertion<Set<String>> instance = CollectionAssertions.nonEmptySet();
        instance.check(null);
    }

    @Test
    public void testNonEmptyMap()
    {
        AlchemyAssertion<Map<String, Integer>> instance = CollectionAssertions.nonEmptyMap();

        Map<String, Integer> map = mapOf(alphabeticString(),
                                         positiveIntegers(),
                                         40);
    }

    @Test(expected = FailedAssertionException.class)
    public void testNonEmptyMapWithBadArgs1() throws Exception
    {
        AlchemyAssertion<Map<String, Integer>> instance = CollectionAssertions.nonEmptyMap();

        instance.check(Collections.<String, Integer>emptyMap());

    }

    @Test(expected = FailedAssertionException.class)
    public void testNonEmptyMapWithBadArgs2() throws Exception
    {
        AlchemyAssertion<Map<String, Integer>> instance = CollectionAssertions.nonEmptyMap();

        instance.check(null);
    }

    @Test
    public void testNonEmptyArray()
    {
        AlchemyAssertion<String[]> instance = CollectionAssertions.nonEmptyArray();
        assertThat(instance, notNullValue());

        String[] stringArray = strings.toArray(new String[0]);
        instance.check(stringArray);
    }

    @Test(expected = FailedAssertionException.class)
    public void testNonEmptyArrayWithBadArgs1() throws Exception
    {
        AlchemyAssertion<String[]> instance = CollectionAssertions.nonEmptyArray();
        instance.check(null);

    }

    @Test(expected = FailedAssertionException.class)
    public void testNonEmptyArrayWithBadArgs2() throws Exception
    {
        AlchemyAssertion<String[]> instance = CollectionAssertions.nonEmptyArray();
        String[] emptyStringArray = new String[]{};

        instance.check(emptyStringArray);
    }

    @Test
    public void testListContaining()
    {
        int index = one(integers(0, strings.size() - 1));

        String string = strings.get(index);

        AlchemyAssertion<List<String>> instance = CollectionAssertions.listContaining(string);
        assertThat(instance, notNullValue());
        instance.check(strings);
    }

    @Test(expected = FailedAssertionException.class)
    public void testListContainingWithBadArgs1() throws Exception
    {
        String string = anyOf(strings);
        AlchemyAssertion<List<String>> instance = CollectionAssertions.listContaining(string);

        List<String> hex = listOf(hexadecimalString(100));

        instance.check(hex);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testListContainingWithBadArgs2() throws Exception
    {
        CollectionAssertions.listContaining(null);
    }

    @Test
    public void testCollectionContaining()
    {
        String string = anyOf(strings);

        AlchemyAssertion<Collection<String>> instance = CollectionAssertions.collectionContaining(string);
        assertThat(instance, notNullValue());
        instance.check(strings);
    }

    @Test(expected = FailedAssertionException.class)
    public void testCollectionContainingWithBadArgs1()
    {
        String string = anyOf(strings);

        AlchemyAssertion<Collection<String>> instance = CollectionAssertions.collectionContaining(string);

        List<String> hex = listOf(hexadecimalString(100));
        instance.check(hex);
    }

    @DontRepeat
    @Test(expected = IllegalArgumentException.class)
    public void testCollectionContainingWithBadArgs2() throws Exception
    {
        CollectionAssertions.collectionContaining(null);
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
    }

    @Test(expected = FailedAssertionException.class)
    public void testCollectionContainingAllWithBadArgs1() throws Exception
    {
        List<String> args = listOf(alphabeticString());
        List<String> collection = listOf(alphabeticString());
        collection.addAll(args);

        String first = args.get(0);
        String[] others = args.subList(1, args.size()).toArray(new String[0]);

        AlchemyAssertion<Collection<String>> instance = CollectionAssertions.collectionContainingAll(first, others);

        List<String> otherCollection = listOf(alphabeticString());
        instance.check(otherCollection);
    }

    @DontRepeat
    @Test(expected = IllegalArgumentException.class)
    public void testCollectionContainingAllWithBadArgs2() throws Exception
    {
        CollectionAssertions.collectionContainingAll(null);
    }

    @Test
    public void testCollectionContainingAtLeastOnceOf() throws Exception
    {
        List<String> args = listOf(alphanumericString());
        List<String> collection = listOf(alphanumericString());
        collection.addAll(args);

        String first = collection.get(0);
        String[] others = args.subList(1, args.size()).toArray(new String[0]);

        AlchemyAssertion<Collection<String>> instance = CollectionAssertions.collectionContainingAtLeastOnceOf(first, others);
        instance.check(collection);

        List<String> otherCollection = listOf(alphabeticString());
        instance.check(otherCollection);
        ;

        //With at least one, it should pass.
        otherCollection.add(first);
        instance.check(otherCollection);
    }

    @DontRepeat
    @Test(expected = IllegalArgumentException.class)
    public void testCollectionContainingAtLeastOnceOfWithBadArgs() throws Exception
    {
        CollectionAssertions.collectionContainingAtLeastOnceOf(null);
    }

    @Test
    public void testMapWithKey()
    {
        Map<Integer, String> map = mapOf(positiveIntegers(), hexadecimalString(100), 100);

        Integer key = anyOf(map.keySet());

        AlchemyAssertion<Map<Integer, String>> instance = CollectionAssertions.mapWithKey(key);
        assertThat(instance, notNullValue());

        instance.check(map);

        Map<Integer, String> badMap = mapOf(negativeIntegers(), hexadecimalString(100), 100);
        instance.check(badMap);
        ;
    }

    @Test
    public void testMapWithKeyValue()
    {
        Map<Integer, String> map = mapOf(positiveIntegers(), alphabeticString(), 100);

        Map.Entry<Integer, String> anyEntry = anyOf(map.entrySet());

        AlchemyAssertion<Map<Integer, String>> instance;
        instance = CollectionAssertions.mapWithKeyValue(anyEntry.getKey(), anyEntry.getValue());
        assertThat(instance, notNullValue());

        //Should pass OK
        instance.check(map);

        Map<Integer, String> badMap = mapOf(negativeIntegers(), hexadecimalString(100), 100);
        instance.check(badMap);
    }

    @Test
    public void testKeyInMap()
    {
        Map<String, String> map = mapOf(alphabeticString(), alphanumericString(), 25);

        AlchemyAssertion<String> assertion = CollectionAssertions.keyInMap(map);
        assertThat(assertion, notNullValue());

        String anyKey = anyOf(map.keySet());
        assertion.check(anyKey);
    }

    @Test(expected = FailedAssertionException.class)
    public void testKeyInMapWithBadArgs1() throws Exception
    {
        Map<String, String> map = mapOf(alphabeticString(), alphanumericString(), 25);

        AlchemyAssertion<String> assertion = CollectionAssertions.keyInMap(map);

        String randomKey = one(hexadecimalString(42));
        assertion.check(randomKey);
    }

    @Test(expected = FailedAssertionException.class)
    public void testKeyInMapWithBadArgs2() throws Exception
    {
        Map<String, String> map = mapOf(alphabeticString(), alphanumericString(), 25);

        AlchemyAssertion<String> assertion = CollectionAssertions.keyInMap(map);

        assertion.check(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testKeyInMapWithBadArgs3() throws Exception
    {
        CollectionAssertions.keyInMap(null);
    }

    @Test
    public void testKeyInMapWithEmptyMap()
    {
        AlchemyAssertion<Object> assertion = CollectionAssertions.keyInMap(Collections.emptyMap());

        for (String string : strings)
        {
            try
            {
                assertion.check(string);
                fail("expected exception here");
            }
            catch (FailedAssertionException ex)
            {

            }
            catch (Exception ex)
            {
                fail("Unexpected exception: " + ex);
            }
        }
    }

    @Test
    public void testValueInMap()
    {
        Map<String, String> map = mapOf(alphanumericString(), alphabeticString(), 24);

        AlchemyAssertion<String> assertion = CollectionAssertions.valueInMap(map);
        assertThat(assertion, notNullValue());

        String anyValue = anyOf(map.values());
        assertion.check(anyValue);

        //Empty map should be ok
        CollectionAssertions.valueInMap(Collections.emptyMap());
    }

    @Test(expected = FailedAssertionException.class)
    public void testValueInMapWithBadArgs1() throws Exception
    {
        Map<String, String> map = mapOf(alphanumericString(), alphabeticString(), 24);

        AlchemyAssertion<String> assertion = CollectionAssertions.valueInMap(map);

        String randomValue = one(hexadecimalString(10));
        assertion.check(randomValue);
    }

    @Test(expected = FailedAssertionException.class)
    public void testValueInMapWithBadArgs2() throws Exception
    {
        Map<String, String> map = mapOf(alphanumericString(), alphabeticString(), 24);

        AlchemyAssertion<String> assertion = CollectionAssertions.valueInMap(map);

        assertion.check(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueInMapWithBadArgs() throws Exception
    {
        CollectionAssertions.valueInMap(null);
    }

    @Test
    public void testValueInMapWithEmptyMap()
    {
        AlchemyAssertion<Object> assertion = CollectionAssertions.valueInMap(Collections.emptyMap());
        assertThat(assertion, notNullValue());

        for (String string : strings)
        {
            try
            {
                assertion.check(string);
                fail("expected exception here");
            }
            catch (FailedAssertionException ex)
            {
                continue;
            }
            catch (Exception ex)
            {
                fail("Unexpected exception: " + ex);
            }
        }
    }

    @Test
    public void testElementInCollection()
    {
        AlchemyAssertion<String> assertion = CollectionAssertions.elementInCollection(strings);
        assertThat(assertion, notNullValue());

        String anyValue = anyOf(strings);
        assertion.check(anyValue);

        //Empty Collections should be ok
        CollectionAssertions.elementInCollection(Collections.emptyList());

    }

    @Test(expected = FailedAssertionException.class)
    public void testElementInCollectionWithBadArgs1() throws Exception
    {
        AlchemyAssertion<String> assertion = CollectionAssertions.elementInCollection(strings);

        String randomValue = one(hexadecimalString(20));

        assertion.check(randomValue);
    }

    @DontRepeat
    @Test(expected = FailedAssertionException.class)
    public void testElementInCollectionWithBadArgs2() throws Exception
    {
        AlchemyAssertion<String> assertion = CollectionAssertions.elementInCollection(strings);
        assertion.check(null);

    }

    @DontRepeat
    @Test(expected = FailedAssertionException.class)
    public void testElementInCollectionWithBadArgs3() throws Exception
    {
        CollectionAssertions.elementInCollection(null);
    }

    @Test
    public void testCollectionOfSize()
    {
        int size = strings.size();
        AlchemyAssertion<? super Collection<String>> instance = CollectionAssertions.collectionOfSize(size);
        instance.check(strings);
    }

    @Test(expected = FailedAssertionException.class)
    public void testCollectionOfSizeWithBadArgs1() throws Exception
    {
        int size = strings.size();
        AlchemyAssertion<? super Collection<String>> instance = CollectionAssertions.collectionOfSize(size);

        strings.add(one(alphabeticString()));

        instance.check(strings);
    }

    @Test(expected = FailedAssertionException.class)
    public void testCollectionOfSizeWithBadArgs2() throws Exception
    {
        int size = strings.size();
        AlchemyAssertion<? super Collection<String>> instance = CollectionAssertions.collectionOfSize(size);

        strings.add(one(alphabeticString()));

        checkThat(strings).is(CollectionAssertions.<List<String>>collectionOfSize(strings.size()));
    }

    @DontRepeat
    @Test(expected = IllegalArgumentException.class)
    public void testCollectionOfSizeWithBadArgs3()
    {
        int badSize = one(negativeIntegers());

        CollectionAssertions.collectionOfSize(badSize);
    }

    @Test
    public void testEmptyCollection()
    {
        AlchemyAssertion<Collection<String>> instance = CollectionAssertions.emptyCollection();

        Collection<String> emptyCollection = new ArrayList<>();
        instance.check(emptyCollection);
    }

    @DontRepeat
    @Test(expected = FailedAssertionException.class)
    public void testEmptyCollectionWithBadArgs() throws Exception
    {
        AlchemyAssertion<Collection<String>> instance = CollectionAssertions.emptyCollection();

        instance.check(strings);
    }

    @Test
    public void testEmptyList()
    {
        AlchemyAssertion<List<String>> instance = CollectionAssertions.emptyList();

        List<String> emptyList = new LinkedList<>();
        instance.check(emptyList);
    }

    @DontRepeat
    @Test(expected = FailedAssertionException.class)
    public void testEmptyListWithBadArgs() throws Exception
    {
        AlchemyAssertion<List<String>> instance = CollectionAssertions.emptyList();

        instance.check(strings);
    }

    @Test
    public void testEmptySet()
    {
        AlchemyAssertion<Set<String>> instance = CollectionAssertions.emptySet();

        Set<String> emptySet = new HashSet<>();
        instance.check(emptySet);

    }

    @DontRepeat
    @Test
    public void testEmptySetWithBadArgs() throws Exception
    {
        AlchemyAssertion<Set<String>> instance = CollectionAssertions.emptySet();
        Set<String> nonEmptySet = new HashSet<>(strings);
        instance.check(nonEmptySet);
    }

    private <T> T anyOf(List<T> list)
    {
        int index = one(integers(0, list.size() - 1));
        return list.get(index);
    }

    private <T> T anyOf(Collection<T> strings)
    {
        return anyOf(new ArrayList<>(strings));
    }
}
