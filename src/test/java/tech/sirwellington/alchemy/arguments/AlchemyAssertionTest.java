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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.listOf;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@RunWith(AlchemyTestRunner.class)
public class AlchemyAssertionTest
{

    @Spy
    private FakeAssertion<Object> first;

    private FakeAssertion[] otherAssertions;

    private Object argument;

    @Before
    public void setUp()
    {
        List<FakeAssertion<Object>> assertions = listOf(() -> spy(FakeAssertion.class));

        otherAssertions = assertions.toArray(new FakeAssertion[assertions.size()]);
        argument = one(alphabeticString());
    }

    @Test
    public void testOther()
    {
        AlchemyAssertion<Object> assertion = first;

        for (AlchemyAssertion<Object> element : otherAssertions)
        {
            assertion = assertion.and(element);
        }

        assertion.check(argument);
        verify(first).check(argument);
        asList(otherAssertions).forEach(a -> verify(a).check(argument));
    }

    @Test
    public void testOtherWithBadArgs()
    {
        assertThrows(() -> first.and(null));
    }

    @Test
    public void testCombineWithMultiple()
    {
        AlchemyAssertion<Object> multipleAssertions = AlchemyAssertion.combine(first, otherAssertions);

        multipleAssertions.check(argument);

        verify(first).check(argument);

        asList(otherAssertions)
                .forEach(a -> verify(a).check(argument));
    }

    @Test
    public void testCombineWithMultipleWithBadArgs()
    {
        assertThrows(() -> AlchemyAssertion.combine(null, (AlchemyAssertion[]) null));
        assertThrows(() -> AlchemyAssertion.combine(first, (AlchemyAssertion[]) null));

    }

    @Test
    public void testCombineWithSingle()
    {
        AlchemyAssertion<Object> multipleAssertions = AlchemyAssertion.combine(first);
        multipleAssertions.check(argument);

        verify(first).check(argument);
        asList(otherAssertions)
                .forEach(a -> verify(a, never()).check(argument));
    }

    static class FakeAssertion<T> implements AlchemyAssertion<T>
    {

        @Override
        public void check(T argument) throws FailedAssertionException
        {
            //ok
        }

    }

}
