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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
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
@RunWith(AlchemyTestRunner.class)
@Repeat
public class ChecksTest
{

    @Before
    public void setUp()
    {
    }

    @DontRepeat
    @Test
    public void testCheckNotNull()
    {
        Checks.Internal.checkNotNull("");
        Checks.Internal.checkNotNull(this);

        assertThrows(() -> Checks.Internal.checkNotNull(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testCheckNotNullWithMessage()
    {
        String message = one(alphabeticString());
        Checks.Internal.checkNotNull("", message);
        Checks.Internal.checkNotNull(this, message);

        assertThrows(() -> Checks.Internal.checkNotNull(null, message))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(message);
    }

    @Test
    public void testCheckThat()
    {
        Checks.Internal.checkThat(true);

        assertThrows(() -> Checks.Internal.checkThat(false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testCheckThatWithMessage()
    {
        String message = one(alphabeticString());

        Checks.Internal.checkThat(true, message);

        assertThrows(() -> Checks.Internal.checkThat(false, message))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(message);
    }

    @Test
    public void testCheckState()
    {
        String message = one(alphabeticString());
        Checks.Internal.checkState(true, message);

        assertThrows(() -> Checks.Internal.checkState(false, message))
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

}
