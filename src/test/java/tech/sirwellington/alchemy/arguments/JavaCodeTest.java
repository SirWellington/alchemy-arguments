/*
 * Copyright 2017 RedRoma, Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.arguments.assertions.Assertions;
import tech.sirwellington.alchemy.arguments.assertions.NumberAssertions;
import tech.sirwellington.alchemy.test.junit.runners.*;

import static org.mockito.Mockito.*;
import static tech.sirwellington.alchemy.arguments.Arguments.*;
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

        checkThat(string).is(nonEmptyString());
    }

    @Test
    public void testPositiveInt() throws Exception
    {
        checkThat(positiveNumber)
                .isA(NumberAssertions.positiveInteger());
    }

    @Test(expected = FailedAssertionException.class)
    public void testPositiveIntWithBadArg() throws Exception
    {
        checkThat(negativeNumber)
                .isA(NumberAssertions.positiveInteger());
    }

    @Test
    public void testNegativeInt() throws Exception
    {
        checkThat(negativeNumber)
                .isA(NumberAssertions.negativeInteger());
    }

    @Test(expected = FailedAssertionException.class)
    public void testNegativeIntWithBadArg() throws Exception
    {
        checkThat(negativeNumber)
                .is(NumberAssertions.positiveInteger());
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

}
