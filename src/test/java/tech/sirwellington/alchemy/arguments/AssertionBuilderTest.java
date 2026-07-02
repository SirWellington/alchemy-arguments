/*
 * Copyright © 2026. Sir Wellington.
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.sirwellington.alchemy.annotations.arguments.Required;
import tech.sirwellington.alchemy.test.AlchemyTest;

import static org.mockito.Mockito.*;

/**
 * Tests for {@link AssertionBuilder} behavior — specifically, delegation from `are()` and `` `is`() `` to `isA()`.
 *
 * @author SirWellington
 */
@DisplayName("AssertionBuilder Delegation Tests")
@AlchemyTest
@ExtendWith(MockitoExtension.class)
class AssertionBuilderTest {

    @Mock
    private AlchemyAssertion<Object> assertion;

    private FakeInstance<Object> instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        instance = spy(new FakeInstance<>());
        doCallRealMethod().when(instance).are(any());
    }

    @Test
    @DisplayName("testAreCallsIsA: should delegate are() → isA()")
    void testAreCallsIsA() {
        var _ = instance.are(assertion);

        verify(instance, times(1)).isA(eq(assertion));
    }

    @Test
    @DisplayName("testIsCallsIsA: should delegate `is`() → isA()")
    void testIsCallsIsA() {
        var _ = instance.is(assertion);

        verify(instance, times(1)).isA(eq(assertion));
    }

    // -------------------------------
    // Fake implementation of AssertionBuilder
    // -------------------------------

    private static class FakeInstance<A> implements AssertionBuilder<A, Throwable> {

        @Override
        public AssertionBuilder<A, Throwable> isA(@Required AlchemyAssertion<A> assertion) {
            return this;
        }

        @Override
        public AssertionBuilder<A, Throwable> are(AlchemyAssertion<A> assertion) {
            // Original Kotlin: return isA(assertion)
            return isA(assertion);
        }

        @Override
        public AssertionBuilder<A, Throwable> is(AlchemyAssertion<A> assertion) {
            return isA(assertion);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <Ex extends Throwable> AssertionBuilder<A, Ex> throwing(Class<Ex> exceptionClass) {
            return (AssertionBuilder<A, Ex>) this;
        }

        @Override
        public AssertionBuilder<A, Throwable> usingMessage(String message) {
            // In real impl: returns new builder with message override
            return this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <Ex extends Throwable> AssertionBuilder<A, Ex> throwing(
            tech.sirwellington.alchemy.arguments.ExceptionMapper<Ex> exceptionMapper) {
            return (AssertionBuilder<A, Ex>) this;
        }
    }
}
