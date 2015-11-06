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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author SirWellington
 */
@RunWith(AlchemyTestRunner.class)
public class AssertionBuilderTest
{

    @Mock
    private AlchemyAssertion assertion;

    @Spy
    private FakeInstance instance;

    @Before
    public void setUp() throws Throwable
    {
        when(instance.are(any()))
                .thenCallRealMethod();
    }

    @Test
    public void testAreCallsIs() throws Throwable
    {
        instance.are(assertion);
        verify(instance).is(assertion);
    }

    private static class FakeInstance<A> implements AssertionBuilder<A, Throwable>
    {

        @Override
        public AssertionBuilder<A, Throwable> usingMessage(String message)
        {
            return this;
        }

        @Override
        public <Ex extends Throwable> AssertionBuilder<A, Ex> throwing(ExceptionMapper<Ex> exceptionMapper)
        {
            return (AssertionBuilder<A, Ex>) this;
        }

        @Override
        public AssertionBuilder<A, Throwable> is(AlchemyAssertion<A> assertion) throws Throwable
        {
            return this;
        }

    }

}
