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

import java.util.List;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static tech.sirwellington.alchemy.arguments.Assertions.nonEmptyString;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.listOf;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class ArgumentsTest
{

    private String argument;

    @Before
    public void setUp()
    {
        argument = one(alphabeticString());
    }

    @Test
    public void testConstructorThrows()
    {
        System.out.println("testConstructorThrows");

        assertThrows(() -> Arguments.class.newInstance());
    }

    @Test
    public void testCheckThat()
    {
        System.out.println("testCheckThat");

        AssertionBuilder<Object, FailedAssertionException> instance = Arguments.checkThat(argument);
        assertThat(instance, notNullValue());
    }

    @Test
    public void testCheckThatWithMultipleArguments()
    {
        System.out.println("testCheckThatWithMultipleArguments");

        List<String> strings = listOf(alphabeticString(), 30);
        String[] stringArray = strings.toArray(new String[strings.size()]);

        AssertionBuilder<String, FailedAssertionException> instance = Arguments.checkThat(argument, stringArray);
        assertThat(instance, notNullValue());
        instance.are(nonEmptyString());

        instance = Arguments.checkThat(argument, new String[0]);
        assertThat(instance, notNullValue());
        instance.are(nonEmptyString());
    }

    @Test
    public void testCheckThatWithMultipleArgumentsWithFailure()
    {
        System.out.println("testCheckThatWithMultipleArgumentsWithFailure");

        AssertionBuilder<String, FailedAssertionException> instance = Arguments.checkThat(argument, new String[1]);
        assertThat(instance, notNullValue());
        assertThrows(() -> instance.are(nonEmptyString()))
                .isInstanceOf(FailedAssertionException.class);
    }

}
