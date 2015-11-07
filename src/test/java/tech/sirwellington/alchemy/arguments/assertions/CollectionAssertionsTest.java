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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.listOf;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.mapOf;
import static tech.sirwellington.alchemy.generator.NumberGenerators.negativeIntegers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.positiveIntegers;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString;
import static tech.sirwellington.alchemy.generator.StringGenerators.hexadecimalString;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@RunWith(AlchemyTestRunner.class)
@Repeat
public class CollectionAssertionsTest
{

    @Before
    public void setUp()
    {
    }

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

        List<String> strings = listOf(alphabeticString());
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

        List<String> strings = listOf(alphabeticString());
        instance.check(strings);

        assertThrows(() -> instance.check(null))
                .isInstanceOf(FailedAssertionException.class);

        assertThrows(() -> instance.check(Collections.emptyList()))
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

        List<String> strings = listOf(alphabeticString());
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
        List<String> strings = listOf(alphabeticString());
        String string = strings.stream().findAny().orElseGet(alphabeticString());

        AlchemyAssertion<List<String>> instance = CollectionAssertions.listContaining(string);
        assertThat(instance, notNullValue());
        instance.check(strings);

        List<String> hex = listOf(hexadecimalString(100));

        assertThrows(() -> instance.check(hex))
                .isInstanceOf(FailedAssertionException.class);
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
    public void testMapWithKeyAndValue()
    {
        Map<Integer, String> map = mapOf(positiveIntegers(), alphabeticString(), 100);

        Map.Entry<Integer, String> anyEntry = map.entrySet().stream().findAny().get();

        AlchemyAssertion<Map<Integer, String>> instance;
        instance = CollectionAssertions.mapWithKeyAndValue(anyEntry.getKey(), anyEntry.getValue());
        assertThat(instance, notNullValue());

        //Should pass OK
        instance.check(map);

        Map<Integer, String> badMap = mapOf(negativeIntegers(), hexadecimalString(100), 100);
        assertThrows(() -> instance.check(badMap));
    }

}
