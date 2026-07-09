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
import tech.sirwellington.alchemy.generator.StringGenerators;
import tech.sirwellington.alchemy.test.AlchemyTest;
import tech.sirwellington.alchemy.test.generation.GenerateString;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;
import static tech.sirwellington.alchemy.test.generation.GenerateString.Type.ALPHABETIC;

/**
 * Tests for {@link DynamicExceptionSupplier}
 *
 * @author SirWellington
 */
@DisplayName("DynamicExceptionSupplier Tests")
@AlchemyTest
final class DynamicExceptionSupplierTest {

    @GenerateString(ALPHABETIC)
    private String overrideMessage;
    private FailedAssertionException causingException;

    @BeforeEach
    void setup() {
        causingException = new FailedAssertionException(
            StringGenerators.alphabeticStrings().get()
        );
    }

    @Test
    void testApply_NoMessage_NoCause_NoOverrideMessage() {
        // Given
        var instance = new DynamicExceptionSupplier<>(
            FakeException.class,
            null
        );

        // When
        var result = instance.apply(causingException);

        // Then
        assertThat(result, notNullValue());
        assertThat(result.getCause(), nullValue());
        assertThat(result.getMessage(), emptyOrNullString());
    }
    @Test
    void testApply_Type_NoMessage_NoCause_UsingOverrideMessage() {
        // Given
        var instance = new DynamicExceptionSupplier<>(
            FakeException.class,
            overrideMessage
        );

        // When
        var result = instance.apply(causingException);

        // Then
        assertThat(result, notNullValue());
        assertThat(result.getCause(), nullValue());
        assertThat(result.getMessage(), emptyOrNullString());
    }

    @Test
    void testApply_Type_HasMessage_NoCause_UsingOverrideMessage() {
        // Given
        var instance = new DynamicExceptionSupplier<>(
            FakeExceptionWithMessage.class,
            overrideMessage
        );

        // When
        var result = instance.apply(causingException);

        // Then
        assertThat(result, notNullValue());
        assertThat(result.getMessage(), equalTo(overrideMessage));
        assertThat(result.getCause(), nullValue());
    }

    @Test
    void testApply_Type_HasMessage_NoCause_NoOverrideMessage() {
        // Given
        var instance = new DynamicExceptionSupplier<>(
            FakeExceptionWithMessage.class,
            null
        );

        // When
        var result = instance.apply(causingException);

        // Then
        assertThat(result, notNullValue());
        assertThat(result.getMessage(),  equalTo(causingException.getMessage()));
        assertThat(result.getCause(), nullValue());
    }

    @Test
    void testApply_Type_NoMessage_HasCause_UsingOverrideMessage() {
        // Given
        var instance = new DynamicExceptionSupplier<>(
            FakeExceptionWithThrowable.class,
            overrideMessage
        );

        // When
        var result = instance.apply(causingException);

        // Then
        assertThat(result, notNullValue());
        assertThat(result.getCause(), notNullValue());
        assertThat(
            result.getCause(),
            instanceOf(FailedAssertionException.class)
        );
        assertThat(
            result.getMessage(),
            containsString(causingException.getMessage())
        );
    }

    @Test
    void testApply_Type_NoMessage_HasCause_NoOverrideMessage() {
        // Given
        var instance = new DynamicExceptionSupplier<>(
            FakeExceptionWithThrowable.class,
            null
        );

        // When
        var result = instance.apply(causingException);

        // Then
        assertThat(result, notNullValue());
        assertThat(result.getCause(), notNullValue());
        assertThat(
            result.getCause(),
            instanceOf(FailedAssertionException.class)
        );
        assertThat(
            result.getMessage(),
            containsString(causingException.getMessage())
        );
    }

    @Test
    void testApply_Type_HasMessage_HasCause_UsingOverrideMessage() {
        // Given
        var instance = new DynamicExceptionSupplier<>(
            FakeExceptionWithBoth.class,
            overrideMessage
        );

        // When
        var result = instance.apply(causingException);

        // Then
        assertThat(result, notNullValue());
        assertThat(result.getCause(), notNullValue());
        assertThat(result.getCause(), equalTo(causingException));
        assertThat(result.getMessage(), equalTo(overrideMessage));
    }

    @Test
    void testApply_Type_HasMessage_HasCause_NoOverrideMessage() {
        // Given
        var instance = new DynamicExceptionSupplier<>(
            FakeExceptionWithBoth.class,
            null
        );

        // When
        var result = instance.apply(causingException);

        // Then
        assertThat(result, notNullValue());
        assertThat(result.getCause(), notNullValue());
        assertThat(result.getCause(), equalTo(causingException));
        assertThat(
            result.getMessage(),
            containsString(causingException.getMessage())
        );
    }

    @Test
    void testWhenInvocationFails() {
        // Given
        var instance = new DynamicExceptionSupplier<>(
            FakeExceptionThatThrowsOnConstruct.class,
            overrideMessage
        );

        // When
        var result = instance.apply(causingException);

        // Then
        assertThat(result, nullValue());
    }

    @Test
    void testGetExceptionClass() {
        // Given
        var exceptionClass = FakeException.class;
        var instance = new DynamicExceptionSupplier<>(
            exceptionClass,
            null
        );

        // When
        var result = instance.getExceptionClass();

        // Then
        assertThat(result, equalTo(exceptionClass));
    }

    @Test
    void testToString() {
        // Given
        var instance = new DynamicExceptionSupplier<>(
            FakeExceptionWithBoth.class,
            overrideMessage
        );

        // When
        var string = instance.toString();

        // Then
        assertThat(string, not(emptyOrNullString()));
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
