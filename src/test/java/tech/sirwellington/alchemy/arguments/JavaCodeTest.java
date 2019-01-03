/*
 * Copyright © 2019. Sir Wellington.
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

package tech.sirwellington.alchemy.arguments;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.arguments.assertions.Assertions;
import tech.sirwellington.alchemy.test.junit.runners.*;

import static org.mockito.Mockito.*;
import static tech.sirwellington.alchemy.arguments.Arguments.*;
import static tech.sirwellington.alchemy.arguments.assertions.CollectionAssertions.*;
import static tech.sirwellington.alchemy.arguments.assertions.NumberAssertions.negativeInteger;
import static tech.sirwellington.alchemy.arguments.assertions.NumberAssertions.positiveInteger;
import static tech.sirwellington.alchemy.arguments.assertions.StringAssertions.*;

/**
 * @author SirWellington
 */
@RunWith(AlchemyTestRunner.class)
public class JavaCodeTest
{
    @GenerateString
    private String string;

    @GenerateInteger(GenerateInteger.Type.POSITIVE)
    private Integer positiveNumber;

    @GenerateInteger(GenerateInteger.Type.NEGATIVE)
    private Integer negativeNumber;

    @Test
    public void testNonEmptyString() throws Exception
    {
        AlchemyAssertion<String> assertion = nonEmptyString();
        assertion.check(string);

        checkThat(string).isA(nonEmptyString());
    }

    @Test
    public void testPositiveInt() throws Exception
    {
        checkThat(positiveNumber)
                .isA(positiveInteger());
    }

    @Test(expected = FailedAssertionException.class)
    public void testPositiveIntWithBadArg() throws Exception
    {
        checkThat(negativeNumber)
                .isA(positiveInteger());
    }

    @Test
    public void testNegativeInt() throws Exception
    {
        checkThat(negativeNumber)
                .isA(negativeInteger());
    }

    @Test(expected = FailedAssertionException.class)
    public void testNegativeIntWithBadArg() throws Exception
    {
        checkThat(negativeNumber)
                .isA(positiveInteger());
    }

    @Test
    public void testCombine() throws Exception
    {
        AlchemyAssertion<String> first = mock(AlchemyAssertion.class);
        AlchemyAssertion<String> second = mock(AlchemyAssertion.class);

        AlchemyAssertion<String> combined = Assertions.combine(first, second);

        combined.check(string);

        verify(first).check(string);
        verify(second).check(string);
    }

    @Test
    public void testEmptyCollections() throws Exception
    {
        checkThat(Collections.emptyList())
                .isA(emptyList());

        checkThat(Collections.emptySet())
                .isA(emptySet());

        checkThat(Collections.emptyMap())
                .isA(emptyMap());
    }

    @Test(expected = FailedAssertionException.class)
    public void testNonEmptyList() throws Exception
    {
        checkThat(Collections.emptyList())
                .isA(nonEmptyList());
    }


    @Test(expected = FailedAssertionException.class)
    public void testNonemptySet() throws Exception
    {
        checkThat(Collections.emptySet())
                .isA(nonEmptySet());
    }

    @Test
    public void testCheckStringNotEmpty() throws Exception
    {
        checkThat(string)
                .isA(nonEmptyString());
    }
}
