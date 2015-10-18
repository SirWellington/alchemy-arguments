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
package sir.wellington.alchemy.arguments;

import static java.util.Arrays.asList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.listOf;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class AssertionTest
{

    @Mock
    private FakeAssertion<Object> first;

    private FakeAssertion[] otherAssertions;

    private Object argument;

    @Before
    public void setUp()
    {
        List<FakeAssertion<Object>> assertions = listOf(() -> mock(FakeAssertion.class));
        otherAssertions = assertions.toArray(new FakeAssertion[0]);
        argument = one(alphabeticString());
    }

    @Test
    public void testCheck()
    {
    }

    @Test
    public void testCombineWithMultiple()
    {
        System.out.println("testCombineWithMultiple");

        AlchemyAssertion<Object> multipleAssertions = AlchemyAssertion.combine(first, otherAssertions);

        multipleAssertions.check(argument);

        verify(first).check(argument);

        asList(otherAssertions)
                .forEach(a -> verify(a).check(argument));
    }

    @Test
    public void testCombineWithSingle()
    {
        System.out.println("testCombineWithSingle");

        AlchemyAssertion<Object> multipleAssertions = AlchemyAssertion.combine(first);
        multipleAssertions.check(argument);

        verify(first).check(argument);
        asList(otherAssertions)
                .forEach(a -> verify(a, never()).check(argument));
    }

    private static class FakeAssertion<T> implements AlchemyAssertion<T>
    {

        @Override
        public void check(T argument) throws FailedAssertionException
        {
            //ok
        }

    }

}
