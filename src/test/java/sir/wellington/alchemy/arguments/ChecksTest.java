/*
 * Copyright 2015 Sir Wellington.
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
package sir.wellington.alchemy.arguments;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static sir.wellington.alchemy.arguments.Checks.checkNotNull;
import static sir.wellington.alchemy.arguments.Checks.checkState;
import static sir.wellington.alchemy.arguments.Checks.checkThat;
import static sir.wellington.alchemy.arguments.Checks.isNullOrEmpty;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.listOf;
import static tech.sirwellington.alchemy.generator.NumberGenerators.positiveIntegers;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString;
import static tech.sirwellington.alchemy.generator.StringGenerators.strings;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class ChecksTest
{

    @Before
    public void setUp()
    {
    }

    @Test
    public void testCheckNotNull_Object()
    {
        System.out.println("testCheckNotNull_Object");

        checkNotNull("");
        checkNotNull(this);

        assertThrows(() -> checkNotNull(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testCheckNotNull_Object_String()
    {
        System.out.println("testCheckNotNull_Object_String");

        String message = one(alphabeticString());
        checkNotNull("", message);
        checkNotNull(this, message);

        assertThrows(() -> checkNotNull(null, message))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(message);
    }

    @Test
    public void testCheckThat_boolean()
    {
        System.out.println("testCheckThat_boolean");

        checkThat(true);

        assertThrows(() -> checkThat(false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testCheckThat_boolean_String()
    {
        System.out.println("testCheckThat_boolean_String");

        String message = one(alphabeticString());

        checkThat(true, message);

        assertThrows(() -> checkThat(false, message))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(message);
    }

    @Test
    public void testCheckState()
    {
        System.out.println("testCheckState");

        String message = one(alphabeticString());
        checkState(true, message);

        assertThrows(() -> checkState(false, message))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(message);
    }

    @Test
    public void testIsNullOrEmpty_String()
    {
        System.out.println("testIsNullOrEmpty_String");

        assertThat(isNullOrEmpty((String) null), is(true));
        assertThat(isNullOrEmpty(""), is(true));
        assertThat(isNullOrEmpty(" "), is(false));

        String string = one(alphabeticString());
        assertThat(isNullOrEmpty(string), is(false));
    }

    @Test
    public void testIsNullOrEmpty_Collection()
    {
        System.out.println("testIsNullOrEmpty_Collection");

        assertThat(isNullOrEmpty(Collections.EMPTY_LIST), is(true));
        assertThat(isNullOrEmpty(Collections.EMPTY_SET), is(true));
        assertThat(isNullOrEmpty((Collection) null), is(true));

        List<Integer> numbers = listOf(positiveIntegers());
        List<String> strings = listOf(strings(10));

        assertThat(isNullOrEmpty(numbers), is(false));
        assertThat(isNullOrEmpty(strings), is(false));
    }

}
