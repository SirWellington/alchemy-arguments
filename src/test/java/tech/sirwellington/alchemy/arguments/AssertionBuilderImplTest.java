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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.sirwellington.alchemy.generator.CollectionGenerators;
import tech.sirwellington.alchemy.test.AlchemyTest;
import tech.sirwellington.alchemy.test.generation.GenerateList;
import tech.sirwellington.alchemy.test.generation.GenerateString;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static tech.sirwellington.alchemy.arguments.assertions.StringAssertions.nonEmptyString;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticStrings;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;
import static tech.sirwellington.alchemy.test.generation.GenerateString.Type.ALPHABETIC;

/**
 * Tests for {@link AssertionBuilderImpl}.
 *
 * @author SirWellington
 */
@DisplayName("AssertionBuilderImpl Tests")
@AlchemyTest
@ExtendWith(MockitoExtension.class)
class AssertionBuilderImplTest {

    @Mock
    private AlchemyAssertion<String> assertion;

    @Mock
    private ExceptionMapper<IOException> exceptionMapper;

    @GenerateString(ALPHABETIC)
    private String argument;

    private List<String> arguments;

    @GenerateString(ALPHABETIC)
    private String errorMessage;

    private AssertionBuilderImpl<String, FailedAssertionException> instance;
    private FailedAssertionException assertException;

    @BeforeEach
    void setUp() {
        arguments = List.of(argument);
        instance = AssertionBuilderImpl.checkThat(arguments);

        assertException = new FailedAssertionException(errorMessage);
    }

    @Test
    @DisplayName("testCheckThat: creates valid builder for non-null and null inputs")
    void testCheckThat() {
        // Non-null input
        var instance = AssertionBuilderImpl.checkThat(arguments);
        assertNotNull(instance);

        assertThrows(
            () -> AssertionBuilderImpl.checkThat(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("testThrowingWhenExceptionIsNotWrapped")
    void testThrowingWhenExceptionIsNotWrapped() {
        var ex = new IOException(errorMessage);
        when(exceptionMapper.apply(assertException))
            .thenReturn(ex);

        doThrow(assertException).when(assertion).check(any());

        assertThrows(
            () -> instance.throwing(exceptionMapper).isA(assertion)
        ).isInstanceOf(IOException.class);

        verify(exceptionMapper).apply(assertException);
        verify(assertion).check(any());
    }

    @Test
    @DisplayName("testThrowingWhenExceptionIsWrapped")
    void testThrowingWhenExceptionIsWrapped() {
        var wrapped = new IOException(errorMessage, assertException);
        when(exceptionMapper.apply(assertException))
            .thenReturn(wrapped);

        doThrow(assertException).when(assertion).check(any());

        assertThrows(
            () -> instance.throwing(exceptionMapper).isA(assertion)
        ).isInstanceOf(IOException.class)
         .hasCauseInstanceOf(FailedAssertionException.class);

        verify(exceptionMapper).apply(assertException);
        verify(assertion).check(any());
    }

    @Test
    @DisplayName("testThrowingExceptionClass")
    void testThrowingExceptionClass() {
        doThrow(assertException).when(assertion).check(argument);

        assertThrows(() -> instance.throwing(IOException.class).is(assertion))
            .isInstanceOf(IOException.class)
            .hasCauseInstanceOf(FailedAssertionException.class);

        verify(assertion).check(any());
    }

    @Test
    @DisplayName("testIsWhenAssertionFails")
    void testIsWhenAssertionFails() {
        doThrow(assertException).when(assertion).check(argument);

        assertThrows(() -> instance.isA(assertion))
            .isInstanceOf(FailedAssertionException.class)
            .hasMessage(assertException.getMessage());

        verify(assertion).check(argument);
    }

    @Test
    @DisplayName("testIsWhenAssertionPasses")
    void testIsWhenAssertionPasses() {
        doNothing().when(assertion).check(any());

        instance.isA(assertion);

        verify(assertion).check(any());
    }

    @Test
    @DisplayName("testIsWhenAssertionThrowsUnexpectedException")
    void testIsWhenAssertionThrowsUnexpectedException() {
        var unexpected = new RuntimeException();
        doThrow(unexpected).when(assertion).check(argument);

        assertThrows(() -> instance.isA(assertion))
            .isInstanceOf(FailedAssertionException.class)
            .hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("testUsingMessage")
    void testUsingMessage() {
        var embeddedMessage = alphabeticStrings().get();
        var overrideMsg = alphabeticStrings().get();

        doThrow(new FailedAssertionException(embeddedMessage))
            .when(assertion)
            .check(argument);

        assertThrows(() -> instance.isA(assertion))
            .isInstanceOf(FailedAssertionException.class)
            .hasMessage(embeddedMessage);

        assertThrows(
            () -> instance.usingMessage(overrideMsg).isA(assertion)
        ).isInstanceOf(FailedAssertionException.class)
         .hasMessage(overrideMsg);
    }

    @Test
    @DisplayName("testChecksWithMultipleArguments")
    void testChecksWithMultipleArguments() {
        // Given
        var arguments = CollectionGenerators.listOf(alphabeticStrings());
        // Then
        // No exceptions expected
        AssertionBuilderImpl.checkThat(arguments)
                            .are(nonEmptyString());

        // When - Empty string
        arguments.add("");

        // Then
        assertThrows(
            () -> AssertionBuilderImpl.checkThat(arguments).are(nonEmptyString())
        ).isInstanceOf(FailedAssertionException.class);
        assertThrows(
            () -> AssertionBuilderImpl.checkThat(arguments).is(nonEmptyString())
        ).isInstanceOf(FailedAssertionException.class);

    }

    @Test
    @DisplayName("testOverrideMessagePreservedWithCustomException")
    void testOverrideMessagePreservedWithCustomException() {
        var overrideMsg = alphabeticStrings().get();

        doThrow(new FailedAssertionException("original"))
            .when(assertion)
            .check(argument);

        var newInstance = instance
            .usingMessage(overrideMsg)
            .throwing(IOException.class);

        assertThrows(
            () -> newInstance.isA(assertion)
        ).isInstanceOf(IOException.class)
         .hasMessage(overrideMsg);
    }

    @Test
    @DisplayName("testOverrideMessagePreservedWithCustomExceptionReversed")
    void testOverrideMessagePreservedWithCustomExceptionReversed() {
        // Given
        var overrideMsg = alphabeticStrings().get();
        doThrow(new FailedAssertionException("original"))
            .when(assertion)
            .check(argument);

        // When
        var newInstance = instance.throwing(IOException.class)
                                  .usingMessage(overrideMsg);
        // Then
        assertThrows(
            () -> newInstance.isA(assertion)
        ).hasMessage(overrideMsg)
         .isInstanceOf(IOException.class)
         .hasCauseInstanceOf(FailedAssertionException.class);
    }
}
