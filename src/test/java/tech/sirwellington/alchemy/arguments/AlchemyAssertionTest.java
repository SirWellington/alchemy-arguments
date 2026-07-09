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
import tech.sirwellington.alchemy.test.AlchemyTest;
import tech.sirwellington.alchemy.test.generation.GenerateString;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.combine;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;
import static tech.sirwellington.alchemy.test.generation.GenerateString.Type.ALPHABETIC;

/**
 * Tests for {@link AlchemyAssertion}.
 *
 * @author SirWellington
 */
@DisplayName("Alchemy Assertion Tests")
@AlchemyTest
final class AlchemyAssertionTest {

    private List<AlchemyAssertion<String>> assertions;
    private AlchemyAssertion<String> first;

    @GenerateString(ALPHABETIC)
    private String argument;

    @BeforeEach
    void setUp() {
        assertions = Arrays.asList(
            mock(),
            mock(),
            mock()
        );
        first = assertions.getFirst();

        assertions.forEach(a -> {
            doCallRealMethod()
                .when(a)
                .and(any());
        });
    }

    @Test
    @DisplayName("testAnd: chained 'and' should check all assertions")
    void testAnd() {
        AlchemyAssertion<String> assertion = null;

        for (var a : assertions) {
            if (assertion == null) {
                assertion = a;
                continue;
            }
            assertion = assertion.and(a);
        }

        // Execute the chain
        var finalAssertion = assertion;
        assertDoesNotThrow(() -> finalAssertion.check(argument));

        // Verify each was called
        for (var a : assertions) {
            verify(a, times(1)).check(any());
        }
    }

    @Test
    @DisplayName("testAnd_WithBadArgs: 'and' rejects null")
    void testAnd_WithBadArgs() {
        assertThrows(
            () -> first.and(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("testCombineWithMultiple: combine() validates all")
    void testCombineWithMultiple() {
        var combined = combine(assertions);

        assertDoesNotThrow(() -> combined.check(argument));

        verify(first).check(any());
        for (var a : assertions) {
            verify(a).check(argument);
        }
    }

    @Test
    @DisplayName("testCombineWithMultipleWithBadArgs: combine() rejects null")
    void testCombineWithMultipleWithBadArgs() {
        assertThrows(
            () -> combine(null)
        ).isInstanceOf(IllegalArgumentException.class);
        assertThrows(
            () -> combine(Collections.emptyList())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("testCombineWithSingle: single assertion is returned as-is")
    void testCombineWithSingle() {
        // Given
        var combined = combine(List.of(first));
        // Then
        assertDoesNotThrow(() -> combined.check(argument));
        verify(first).check(argument);

        // Ensure no other assertions were checked (they weren't even passed)
        for (var a : assertions.subList(1, assertions.size())) {
            verify(a, never()).check(any());
        }
    }

}
