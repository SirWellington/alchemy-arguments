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
import org.mockito.Mockito;
import tech.sirwellington.alchemy.test.AlchemyTest;
import tech.sirwellington.alchemy.test.ThrowableAssertion;
import tech.sirwellington.alchemy.test.generation.GenerateString;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.combine;
import static tech.sirwellington.alchemy.test.generation.GenerateString.Type.ALPHABETIC;

/**
 * Tests for {@link AlchemyAssertion}.
 *
 * @author SirWellington
 */
@DisplayName("Alchemy Assertion Tests")
@AlchemyTest
class AlchemyAssertionTest {

    private AlchemyAssertion<String> first;
    private List<AlchemyAssertion<String>> otherAssertions;

    @GenerateString(ALPHABETIC)
    private String argument;

    @BeforeEach
    void setUp() {
        first = Mockito.mock();

        // Generate 3–5 fake assertions (similar to Kotlin test)
        otherAssertions = Arrays.asList(
            Mockito.mock(),
            Mockito.mock(),
            Mockito.mock()
        );
    }

    @Test
    @DisplayName("testOther: chained 'and' should check all assertions")
    void testOther() {
        AlchemyAssertion<String> assertion = first;

        for (AlchemyAssertion<String> element : otherAssertions) {
            assertion = assertion.and(element);
        }

        // Execute the chain
        var finalAssertion = assertion;
        assertDoesNotThrow(() -> finalAssertion.check(argument));

        // Verify each was called
        verify(first).check(any());
        for (var a : otherAssertions) {
            verify(a).check(any());
        }
    }

    @Test
    @DisplayName("testOtherWithBadArgs: 'and' rejects null")
    void testOtherWithBadArgs() {
        ThrowableAssertion.assertThrows(
            () -> first.and(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("testCombineWithMultiple: combine() validates all")
    void testCombineWithMultiple() {
        var args = otherAssertions.toArray(new AlchemyAssertion[0]);
        AlchemyAssertion<String> combined = combine(first, args);

        assertDoesNotThrow(() -> combined.check(argument));

        verify(first).check(any());
        for (var a : otherAssertions) {
            verify(a).check(argument);
        }
    }

    @Test
    @DisplayName("testCombineWithMultipleWithBadArgs: combine() rejects null")
    void testCombineWithMultipleWithBadArgs() {
        ThrowableAssertion.assertThrows(
            () -> combine(null, first)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("testCombineWithSingle: single assertion is returned as-is")
    void testCombineWithSingle() {
        AlchemyAssertion<String> combined = combine(first);
        assertDoesNotThrow(() -> combined.check(argument));
        verify(first).check(argument);

        // Ensure no other assertions were checked (they weren't even passed)
        for (var a : otherAssertions) {
            verify(a, never()).check(any());
        }
    }

}
