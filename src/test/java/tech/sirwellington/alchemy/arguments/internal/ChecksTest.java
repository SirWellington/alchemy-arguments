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

package tech.sirwellington.alchemy.arguments.internal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.generator.CollectionGenerators;
import tech.sirwellington.alchemy.test.AlchemyTest;
import tech.sirwellington.alchemy.test.generation.GenerateString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticStrings;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;
import static tech.sirwellington.alchemy.test.generation.GenerateString.Type.ALPHABETIC;

/**
 * Tests for {@link Checks} — null, emptiness, state checks.
 *
 * @author SirWellington
 */
@DisplayName("Checks Utility Tests")
@AlchemyTest
@ExtendWith(MockitoExtension.class)
class ChecksTest {

    private AlchemyGenerator<String> strings;
    @GenerateString(ALPHABETIC)
    private String string;
    private Object object;
    private String[] varArgs;

    @GenerateString
    private String message;

    @BeforeEach
    void setUp() {
        strings = alphabeticStrings();
        string = one(strings);
        object = one(strings);

        var listOfStrings = CollectionGenerators.listOf(strings, 5);
        varArgs = listOfStrings.toArray(new String[0]);
    }

    @Test
    @DisplayName("testCheckNotNull: should succeed on non-null, throw IllegalArgumentException on null")
    void testCheckNotNull() {
        Checks.checkNotNull("");
        Checks.checkNotNull(this);

        assertThrows(() -> Checks.checkNotNull(null))
            .isIllegalArgumentException();
    }

    @Test
    @DisplayName("testCheckNotNullWithMessage: should use custom message on failure")
    void testCheckNotNullWithMessage() {
        Checks.checkNotNull("", message);
        Checks.checkNotNull(this, message);

        assertThrows(() -> Checks.checkNotNull(null, message))
            .isIllegalArgumentException()
            .hasMessage(message);
    }

    @Test
    @DisplayName("testCheckThat: should succeed on true, throw IllegalArgumentException on false")
    void testCheckThat() {
        Checks.checkThat(true);

        assertThrows(() -> Checks.checkThat(false))
            .isIllegalArgumentException();
    }

    @Test
    @DisplayName("testCheckThatWithMessage: should use custom message when failing")
    void testCheckThatWithMessage() {
        Checks.checkThat(true, message);

        assertThrows(() -> Checks.checkThat(false, message))
            .isIllegalArgumentException()
            .hasMessage(message);
    }

    @Test
    @DisplayName("testCheckState: should succeed when condition true, IllegalStateException if false")
    void testCheckState() {
        Checks.checkState(true, message);

        assertThrows(() -> Checks.checkState(false, message))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(message);
    }

    @Test
    @DisplayName("testIsNullOrEmptyString: handles null and empty strings correctly")
    void testIsNullOrEmptyString() {
        assertTrue(Checks.isNullOrEmpty((String) null));
        assertTrue(Checks.isNullOrEmpty(""));
        assertFalse(Checks.isNullOrEmpty(" ")); // space is not empty
        assertFalse(Checks.isNullOrEmpty(string));
    }

    @Test
    @DisplayName("testIsNullOrEmptyCollection: handles null, empty collections correctly")
    void testIsNullOrEmptyCollection() {
        assertTrue(Checks.isNullOrEmpty(Collections.emptyList()));
        assertTrue(Checks.isNullOrEmpty(Collections.emptySet()));
        assertTrue(Checks.isNullOrEmpty((Collection<?>) null));

        var numbers = new ArrayList<>();
        numbers.add(42);
        assertFalse(Checks.isNullOrEmpty(numbers));

        var strings = new ArrayList<>();
        strings.add("one");
        strings.add("two");
        assertFalse(Checks.isNullOrEmpty(strings));
    }

    @Test
    @DisplayName("testCheckNotNullOrEmptyString: should pass for non-null/non-empty, fail otherwise")
    void testCheckNotNullOrEmptyString() {
        Checks.checkNotNullOrEmpty(string);

        assertThrows(
            () -> Checks.checkNotNullOrEmpty("")
        ).isIllegalArgumentException();

        assertThrows(
            () -> Checks.checkNotNullOrEmpty((String) null)
        ).isIllegalArgumentException();
    }

    @Test
    @DisplayName("testCheckNotNullOrEmptyStringWithMessage: uses custom message on error")
    void testCheckNotNullOrEmptyStringWithMessage() {
        Checks.checkNotNullOrEmpty(string, message);

        assertThrows(() -> Checks.checkNotNullOrEmpty("", message))
            .isIllegalArgumentException()
            .hasMessage(message);

        assertThrows(() -> Checks.checkNotNullOrEmpty((String) null, message))
            .isIllegalArgumentException()
            .hasMessage(message);
    }

    @Test
    @DisplayName("testCheckNotNullOrEmptyCollection: should pass for non-null/non-empty, fail otherwise")
    void testCheckNotNullOrEmptyCollection() {
        var list = CollectionGenerators.listOf(strings);
        Checks.checkNotNullOrEmpty(list);

        assertThrows(
            () -> Checks.checkNotNullOrEmpty(List.of())
        ).isIllegalArgumentException()
         .containsInMessage("empty");

        assertThrows(
            () -> Checks.checkNotNullOrEmpty((List<?>) null)
        ).isIllegalArgumentException()
         .containsInMessage("empty");
    }

    @Test
    @DisplayName("testCheckNotNullOrEmptyCollectionWithMessage: uses custom message on error")
    void testCheckNotNullOrEmptyCollectionWithMessage() {
        var list = CollectionGenerators.listOf(strings);
        Checks.checkNotNullOrEmpty(list, message);

        assertThrows(() -> Checks.checkNotNullOrEmpty(List.of(), message))
            .isIllegalArgumentException()
            .hasMessage(message);

        assertThrows(() -> Checks.checkNotNullOrEmpty((List<?>) null, message))
            .isIllegalArgumentException()
            .hasMessage(message);
    }

    @Test
    @DisplayName("testIsNull: true only for null references")
    void testIsNull() {
        assertTrue(Checks.isNull(null));
        assertFalse(Checks.isNull(string));
        assertFalse(Checks.isNull(object));
    }

    @Test
    @DisplayName("testNotNull: opposite of isNull")
    void testNotNull() {
        assertTrue(Checks.notNull(object));
        assertTrue(Checks.notNull(string));
        assertFalse(Checks.notNull(null));
    }

    @Test
    @DisplayName("testAnyAreNull: true if at least one argument is null")
    void testAnyAreNull() {
        assertTrue(Checks.anyAreNull((Object[]) null));
        assertTrue(Checks.anyAreNull((Object) null));
        assertTrue(Checks.anyAreNull(string, null));
        assertTrue(Checks.anyAreNull(one(strings), one(strings), null));
        assertFalse(Checks.anyAreNull(varArgs));
    }

    @Test
    @DisplayName("testAllAreNull: true only if *all* args are null")
    void testAllAreNull() {
        assertTrue(Checks.allAreNull());
        assertTrue(Checks.allAreNull((Object) null));
        assertTrue(Checks.allAreNull(null, null));
        assertTrue(Checks.allAreNull(null, null, null));

        assertFalse(Checks.allAreNull(varArgs));
        assertFalse(Checks.allAreNull(null, string));
        assertFalse(Checks.allAreNull(null, string, object));
    }

    @Test
    @DisplayName("testIsNullOrEmpty (string overload): empty or null")
    void testIsNullOrEmptyStringOverload() {
        assertTrue(Checks.isNullOrEmpty(""));
        assertTrue(Checks.isNullOrEmpty((String) null));

        assertFalse(Checks.isNullOrEmpty(string));
    }

    @Test
    @DisplayName("testNotNullOrEmpty: true if non-null and not empty")
    void testNotNullOrEmpty() {
        assertTrue(Checks.notNullOrEmpty(string));

        assertFalse(Checks.notNullOrEmpty(null));
        assertFalse(Checks.notNullOrEmpty(""));
    }

    @Test
    @DisplayName("testAnyAreNullOrEmpty: at least one string is null or empty")
    void testAnyAreNullOrEmpty() {
        assertTrue(Checks.anyAreNullOrEmpty(string, null));
        assertTrue(Checks.anyAreNullOrEmpty(null, string, string));
        assertTrue(Checks.anyAreNullOrEmpty(null, string, one(strings)));
        assertTrue(Checks.anyAreNullOrEmpty(null, null, null));

        assertFalse(Checks.anyAreNullOrEmpty(varArgs)); // all non-null and non-empty
    }

    @Test
    @DisplayName("testAllAreNullOrEmpty: *all* strings must be null or empty")
    void testAllAreNullOrEmpty() {
        assertTrue(Checks.allAreNullOrEmpty());
        assertTrue(Checks.allAreNullOrEmpty(""));
        assertTrue(Checks.allAreNullOrEmpty("", ""));
        assertTrue(Checks.allAreNullOrEmpty("", "", null));
        assertTrue(Checks.allAreNullOrEmpty((String) null));

        assertFalse(Checks.allAreNullOrEmpty(string, null));
        assertFalse(Checks.allAreNullOrEmpty(string, ""));
        assertFalse(Checks.allAreNullOrEmpty(string, string, one(strings)));
    }
}
