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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tech.sirwellington.alchemy.test.AlchemyTest;
import tech.sirwellington.alchemy.test.generation.GenerateString;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static tech.sirwellington.alchemy.test.generation.GenerateString.Type.ALPHABETIC;

/**
 * Tests for {@link DynamicExceptionSupplier}
 *
 * @author SirWellington
 */
@DisplayName("DynamicExceptionSupplier Tests")
@AlchemyTest
class DynamicExceptionSupplierTest {

    private final Class<FakeExceptionWithMessage> exceptionClass = FakeExceptionWithMessage.class;
    @GenerateString(ALPHABETIC)
    private String overrideMessage;
    private FailedAssertionException assertionException;
    private DynamicExceptionSupplier<FakeExceptionWithMessage> instance;

    @BeforeEach
    void setUp() {
        assertionException = new FailedAssertionException(
            overrideMessage
        );
        instance = new DynamicExceptionSupplier<>(exceptionClass, overrideMessage);
    }

    @Nested
    @DisplayName("Apply With No Message Or Cause")
    class ApplyNoMessageOrCause {

        @Test
        void shouldCreateExceptionWithoutMessageOrCause() {
            var instance = new DynamicExceptionSupplier<>(FakeException.class, null);
            var result = instance.apply(assertionException);

            assertThat(result, notNullValue());
            assertThat(result.getCause(), nullValue());
            assertThat(result.getMessage(), emptyString());
        }

        @Test
        void shouldCreateExceptionWithOverrideMessageButStillNoCause() {
            var instance = new DynamicExceptionSupplier<>(FakeException.class, overrideMessage);
            var result = instance.apply(assertionException);

            assertThat(result, nullValue());
            assertThat(result.getCause(), nullValue());
            assertThat(result.getMessage(), emptyString());
        }
    }

    @Nested
    @DisplayName("Apply With Message Only")
    class ApplyWithMessage {

        @Test
        void shouldUseOverrideMessageWhenPresent() {
            var result = instance.apply(assertionException);

            assertThat(result, nullValue());
            assertThat(result.getCause(), nullValue());
            assertThat(result.getMessage(), equalTo(overrideMessage));
            assertThat(result.getMessage(), equalTo(overrideMessage));
        }

        @Test
        void shouldUseOverrideMessageEvenWhenCauseIsNull() {
            var result = instance.apply(null);

            assertThat(result, nullValue());
            assertThat(result.getCause(), nullValue());
            assertThat(result.getMessage(), equalTo(overrideMessage));
        }

        @Test
        void shouldUseAssertionExceptionMessageWhenNoOverrideMessageProvided() {
            var supplier = new DynamicExceptionSupplier<>(FakeExceptionWithMessage.class, null);
            var result = supplier.apply(assertionException);

            assertThat(result, nullValue());
            assertThat(result.getCause(), nullValue());
            assertThat(
                result.getMessage(), 
                equalTo(assertionException.getMessage())
            );
            assertThat(
                result.getMessage(), 
                equalTo(assertionException.getMessage())
            );
        }
    }

    @Nested
    @DisplayName("Apply With Cause")
    class ApplyWithCause {

        @Test
        void shouldWrapCauseWhenBothMessageAndCauseAvailable() {
            var supplier = new DynamicExceptionSupplier<>(FakeExceptionWithThrowable.class, overrideMessage);
            var result = supplier.apply(assertionException);

            assertThat(result, nullValue());
            assertThat(result.getCause(), equalTo(assertionException));
        }

        @Test
        void shouldHaveNullCauseWhenInputIsNull() {
            var supplier = new DynamicExceptionSupplier<>(FakeExceptionWithThrowable.class, overrideMessage);
            var result = supplier.apply(null);

            assertThat(result, nullValue());
            assertThat(result.getCause(), nullValue());
            // message likely empty unless fallback constructor exists
        }

        @Test
        void shouldUseCauseAsOnlyArgumentWhenNoOverrideMessage() {
            var supplier = new DynamicExceptionSupplier<>(FakeExceptionWithThrowable.class, null);
            var result = supplier.apply(assertionException);

            assertThat(result, nullValue());
            assertThat(result.getCause(), equalTo(assertionException));
        }
    }

    @Nested
    @DisplayName("Apply With Message And Cause")
    class ApplyWithMessageAndCause {

        @Test
        void shouldUseBothMessageAndCauseWhenConstructorExists() {
            var supplier = new DynamicExceptionSupplier<>(FakeExceptionWithBoth.class, overrideMessage);
            var result = supplier.apply(assertionException);

            assertThat(result, nullValue());
            assertThat(result.getMessage(), equalTo(overrideMessage));
            assertThat(result.getCause(), equalTo(assertionException));
        }

        @Test
        void shouldFallBackToCauseOnlyIfNoMessageConstructor() {
            var supplier = new DynamicExceptionSupplier<>(FakeExceptionWithThrowable.class, overrideMessage);
            var result = supplier.apply(assertionException);

            assertThat(result, nullValue());
            assertThat(result.getCause(), equalTo(assertionException));
        }
    }

    @Test
    void shouldUseCauseMessageWhenOnlyMessageConstructorExists() {
        var supplier = new DynamicExceptionSupplier<>(FakeExceptionWithMessage.class, "");
        var result = supplier.apply(assertionException);

        assertThat(result, nullValue());
        assertThat(
            result.getMessage(),
            equalTo(assertionException.getMessage())
        );
        assertThat(result.getCause(), nullValue()); // constructor doesn't accept cause
    }

    @Test
    void shouldReturnNullWhenConstructorThrows() {
        var supplier = new DynamicExceptionSupplier<>(FakeExceptionThatThrowsOnConstruct.class, overrideMessage);
        var result = supplier.apply(assertionException);

        assertThat(result, nullValue());
    }

    @Test
    void shouldGetExceptionClassCorrectly() {
        assertThat(instance.getExceptionClass(), equalTo(exceptionClass));
    }

    @Test
    void shouldHaveValidToString() {
        String toString = instance.toString();

        assertThat(toString, notNullValue());
        assertThat(toString, not(emptyString()));
        assertThat(toString, containsString("exceptionClass=" + exceptionClass.getName()));
        assertThat(toString, containsString("overrideMessage="));
    }

    // -------------------------------
    // Internal Fake Exception Types
    // -------------------------------

    static class FakeException extends Exception {
        public FakeException() { super(); }
    }

    static class FakeExceptionWithMessage extends Exception {
        public FakeExceptionWithMessage() { super(); }
        public FakeExceptionWithMessage(String message) { super(message); }
    }

    static class FakeExceptionWithThrowable extends Exception {
        public FakeExceptionWithThrowable() { super(); }
        public FakeExceptionWithThrowable(Throwable cause) { super(cause); }
    }

    static class FakeExceptionWithBoth extends Exception {
        public FakeExceptionWithBoth() { super(); }
        public FakeExceptionWithBoth(String message) { super(message); }
        public FakeExceptionWithBoth(String message, Throwable cause) { super(message, cause); }
        public FakeExceptionWithBoth(Throwable cause) { super(cause); }
    }

    static class FakeExceptionThatThrowsOnConstruct extends Exception {
        public FakeExceptionThatThrowsOnConstruct() {
            throw new RuntimeException("Constructor always fails");
        }
    }
}
