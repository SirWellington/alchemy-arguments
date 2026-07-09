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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.sirwellington.alchemy.test.AlchemyTest;
import tech.sirwellington.alchemy.test.ThrowableAssertion;
import tech.sirwellington.alchemy.test.generation.GenerateList;
import tech.sirwellington.alchemy.test.generation.GenerateString;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.StringAssertions.nonEmptyString;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;

/**
 * Tests for {@link Arguments}.
 *
 * @author SirWellington
 */
@DisplayName("Arguments Utility Tests")
@AlchemyTest
@ExtendWith(MockitoExtension.class)
class ArgumentsTest {

    @GenerateString
    private String argument = "Hello";

    @GenerateList(String.class)
    private List<String> strings = List.of();

    @Test
    @DisplayName("testConstructorThrows: Arguments should be non-instantiable")
    void testConstructorThrows() {
        assertThrows(
            () -> Arguments.class.getDeclaredConstructor().newInstance()
        ).isInstanceOf(InvocationTargetException.class)
            .hasCauseInstanceOf(IllegalAccessException.class);
    }

    @Test
    @DisplayName("testCheckThat: single argument returns non-null instance")
    void testCheckThat() {
        var instance = checkThat(argument);
        assertNotNull(instance, "checkThat should return a non-null assertion");
    }

    @Test
    @DisplayName("testCheckThatWithMultipleArguments: multiple args work correctly (including empty)")
    void testCheckThatWithMultipleArguments() {
        // Test with multiple strings
        final var instance1 = checkThat(argument, strings.toArray(new String[0]));
        assertNotNull(instance1);
        assertDoesNotThrow(
            () -> instance1.are(nonEmptyString())
        );

        // Test with only first arg and zero additional (i.e., single string)
        final var instance2 = checkThat(argument, new String[0]);
        assertNotNull(instance2);
        assertDoesNotThrow(() -> instance2.are(nonEmptyString()));
    }

    @Test
    @DisplayName("testCheckThatWithMultipleArgumentsWithFailure: assertion failure on null element")
    void testCheckThatWithMultipleArgumentsWithFailure() {
        var instance = checkThat(argument, new String[]{null});

        assertNotNull(instance, "instance should be non-null before checking");

        assertThrows(
            () -> instance.are(nonEmptyString())
        ).isInstanceOf(FailedAssertionException.class)
             .containsInMessage("empty");
    }

}
