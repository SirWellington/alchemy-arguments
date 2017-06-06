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

package tech.sirwellington.alchemy.arguments;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.test.junit.runners.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.listOf;
import static tech.sirwellington.alchemy.generator.NumberGenerators.positiveIntegers;
import static tech.sirwellington.alchemy.generator.StringGenerators.*;

/**
 *
 * @author SirWellington
 */
@RunWith(AlchemyTestRunner.class)
@Repeat
public class ChecksTest
{

    private AlchemyGenerator<String> strings;
    
    private String string;
    private Object object;
    private String[] varArgs;
    
    @Before
    public void setUp()
    {
        strings = alphanumericString();
        string = one(strings);
        object = one(strings);
        
        List<String> listOfStrings = listOf(strings);
        varArgs = listOfStrings.toArray(new String[0]);
    }

    @DontRepeat
    @Test
    public void testCannotInstantiate()
    {
        Checks.class.newInstance();
            .isInstanceOf(IllegalAccessException.class);

        Checks.Internal.class.newInstance();
            .isInstanceOf(IllegalAccessException.class);
    }

    @DontRepeat
    @Test
    public void testCheckNotNull()
    {
        Checks.Internal.checkNotNull("");
        Checks.Internal.checkNotNull(this);

        Checks.Internal.checkNotNull(null);
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testCheckNotNullWithMessage()
    {
        String message = one(alphabeticString());
        Checks.Internal.checkNotNull("", message);
        Checks.Internal.checkNotNull(this, message);

        Checks.Internal.checkNotNull(null, message);
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(message);
    }

    @Test
    public void testCheckThat()
    {
        Checks.Internal.checkThat(true);

        Checks.Internal.checkThat(false);
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testCheckThatWithMessage()
    {
        String message = one(alphabeticString());

        Checks.Internal.checkThat(true, message);

        Checks.Internal.checkThat(false, message);
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(message);
    }

    @Test
    public void testCheckState()
    {
        String message = one(alphabeticString());
        Checks.Internal.checkState(true, message);

        Checks.Internal.checkState(false, message);
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(message);
    }

    @Test
    public void testIsNullOrEmptyString()
    {
        assertThat(Checks.Internal.isNullOrEmpty((String) null), is(true));
        assertThat(Checks.Internal.isNullOrEmpty(""), is(true));
        assertThat(Checks.Internal.isNullOrEmpty(" "), is(false));

        String string = one(alphabeticString());
        assertThat(Checks.Internal.isNullOrEmpty(string), is(false));
    }

    @Test
    public void testIsNullOrEmptyCollection()
    {
        assertThat(Checks.Internal.isNullOrEmpty(Collections.EMPTY_LIST), is(true));
        assertThat(Checks.Internal.isNullOrEmpty(Collections.EMPTY_SET), is(true));
        assertThat(Checks.Internal.isNullOrEmpty((Collection) null), is(true));

        List<Integer> numbers = listOf(positiveIntegers());
        List<String> strings = listOf(strings(10));

        assertThat(Checks.Internal.isNullOrEmpty(numbers), is(false));
        assertThat(Checks.Internal.isNullOrEmpty(strings), is(false));
    }

    @Test
    public void testCheckNotNullOrEmpty()
    {
        String string = one(strings());
        Checks.Internal.checkNotNullOrEmpty(string);

        String emptyString = "";
        Checks.Internal.checkNotNullOrEmpty(emptyString);
            .isInstanceOf(IllegalArgumentException.class);

        String nullString = null;
        Checks.Internal.checkNotNullOrEmpty(nullString);
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testCheckNotNullOrEmptyWithMessage()
    {
        String string = one(strings());
        String message = one(alphabeticString());
        Checks.Internal.checkNotNullOrEmpty(string, message);

        Checks.Internal.checkNotNullOrEmpty("", message);
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(message);

        Checks.Internal.checkNotNullOrEmpty(null, message);
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(message);

    }

    @Test
    public void testIsNull()
    {
        assertTrue(Checks.isNull(null));
        assertFalse(Checks.isNull(string));
        assertFalse(Checks.isNull(object));
    }

    @Test
    public void testNotNull()
    {
        assertTrue(Checks.notNull(object));
        assertTrue(Checks.notNull(string));
        assertFalse(Checks.notNull(null));
    }

    @Test
    public void testAnyAreNull()
    {
        assertTrue(Checks.anyAreNull());
        assertTrue(Checks.anyAreNull((Object) null));
        assertTrue(Checks.anyAreNull(string, null));
        assertTrue(Checks.anyAreNull(one(strings), one(strings), null));
        
        assertFalse(Checks.anyAreNull((Object) varArgs));
        assertFalse(Checks.anyAreNull(varArgs[0], varArgs[1], varArgs[2]));
        
    }

    @Test
    public void testAllAreNull()
    {
        assertTrue(Checks.allAreNull());
        assertTrue(Checks.allAreNull((Object) null));
        assertTrue(Checks.allAreNull(null, null));
        assertTrue(Checks.allAreNull(null, null, null));
        
        assertFalse(Checks.allAreNull((Object) varArgs));
        assertFalse(Checks.allAreNull(null, string));
        assertFalse(Checks.allAreNull(null, string, string));
        assertFalse(Checks.allAreNull(null, string, string, object));
    }

    @Test
    public void testIsNullOrEmpty()
    {
        assertTrue(Checks.isNullOrEmpty(""));
        assertTrue(Checks.isNullOrEmpty(null));
        
        assertFalse(Checks.isNullOrEmpty(string));
    }

    @Test
    public void testNotNullOrEmpty()
    {
        assertTrue(Checks.notNullOrEmpty(string));
        
        assertFalse(Checks.notNullOrEmpty(null));
        assertFalse(Checks.notNullOrEmpty(""));
    }

    @Test
    public void testAnyAreNullOrEmpty()
    {
        assertTrue(Checks.anyAreNullOrEmpty(string, null));
        assertTrue(Checks.anyAreNullOrEmpty(null, string, string));
        assertTrue(Checks.anyAreNullOrEmpty(null, string, one(strings)));
        assertTrue(Checks.anyAreNullOrEmpty(null, null, null));
        
        assertFalse(Checks.anyAreNullOrEmpty(varArgs));
        assertFalse(Checks.anyAreNullOrEmpty(string, string, string));
    }

    @Test
    public void testAllAreNullOrEmpty()
    {
        assertTrue(Checks.allAreNullOrEmpty());
        assertTrue(Checks.allAreNullOrEmpty(""));
        assertTrue(Checks.allAreNullOrEmpty("", ""));
        assertTrue(Checks.allAreNullOrEmpty("", "", null));
        assertTrue(Checks.allAreNullOrEmpty((String) null));
        assertTrue(Checks.allAreNullOrEmpty(null, null, null));
        assertTrue(Checks.allAreNullOrEmpty(null, null, null, ""));
        
        assertFalse(Checks.allAreNullOrEmpty(string, null));
        assertFalse(Checks.allAreNullOrEmpty(string, ""));
        assertFalse(Checks.allAreNullOrEmpty(string, string, string, string, ""));
        assertFalse(Checks.allAreNullOrEmpty(string, string, string, object.toString(), ""));
    }

}
