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
import static sir.wellington.alchemy.test.DataGenerator.alphabeticString;
import static sir.wellington.alchemy.test.DataGenerator.listOf;
import static sir.wellington.alchemy.test.DataGenerator.oneOf;

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
        argument = oneOf(alphabeticString());
    }

    @Test
    public void testCheck()
    {
    }

    @Test
    public void testCombineWithMultiple()
    {
        System.out.println("testCombineWithMultiple");

        Assertion<Object> multipleAssertions = Assertion.combine(first, otherAssertions);

        multipleAssertions.check(argument);

        verify(first).check(argument);

        asList(otherAssertions)
                .forEach(a -> verify(a).check(argument));
    }

    @Test
    public void testCombineWithSingle()
    {
        System.out.println("testCombineWithSingle");

        Assertion<Object> multipleAssertions = Assertion.combine(first);
        multipleAssertions.check(argument);

        verify(first).check(argument);
        asList(otherAssertions)
                .forEach(a -> verify(a, never()).check(argument));
    }

    private static class FakeAssertion<T> implements Assertion<T>
    {

        @Override
        public void check(T argument) throws FailedAssertionException
        {
            //ok
        }

    }

}
