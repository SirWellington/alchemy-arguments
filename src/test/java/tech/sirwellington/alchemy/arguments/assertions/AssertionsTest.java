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

package tech.sirwellington.alchemy.arguments.assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.Arguments;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;
import tech.sirwellington.alchemy.generator.StringGenerators;
import tech.sirwellington.alchemy.test.AlchemyTest;
import tech.sirwellington.alchemy.test.generation.GenerateDouble;
import tech.sirwellington.alchemy.test.generation.GenerateInteger;
import tech.sirwellington.alchemy.test.generation.GenerateLong;
import tech.sirwellington.alchemy.test.generation.GenerateString;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static tech.sirwellington.alchemy.arguments.TestHelpers.assertThrowsFailedAssertion;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.instanceOf;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;
import static tech.sirwellington.alchemy.test.generation.GenerateInteger.Type.POSITIVE;
import static tech.sirwellington.alchemy.test.generation.GenerateString.Type.ALPHABETIC;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AlchemyTest
class AssertionsTest {

    @GenerateString(ALPHABETIC)
    private String string;

    @GenerateInteger(POSITIVE)
    private int positiveInt;

    @GenerateLong(GenerateLong.Type.POSITIVE)
    private long positiveLong;

    @GenerateDouble(GenerateDouble.Type.POSITIVE)
    private double positiveDouble;

    @Test
    void testNotNull() {
        var instance = Assertions.notNull();
        assertThat(instance, notNullValue());

        var mock = new Object();
        instance.check(mock);
    }

    @Test
    void testNonNullReference() {
        var instance = Assertions.notNull();
        assertThat(instance, notNullValue());
        assertThrowsFailedAssertion(() -> instance.check(null));
    }

    @Test
    void testNullObject() {
        var instance = Assertions.nullObject();
        instance.check(null);
        assertThrowsFailedAssertion(() -> instance.check(string));
    }

    @Test
    void testSameInstanceAs() {
        var instanceOne = Assertions.sameInstanceAs(null);

        assertThat(instanceOne, notNullValue());
        instanceOne.check(null);

        assertThrowsFailedAssertion(() -> instanceOne.check(new Object()));

        var someObject = new Object();
        var instanceTwo = Assertions.sameInstanceAs(someObject);
        instanceTwo.check(someObject);

        var newObject = new Object();
        assertThrowsFailedAssertion(() -> instanceTwo.check(newObject));
    }

    @Test
    void testInstanceOf() {
        Arguments.checkThat(positiveInt).is(instanceOf(Number.class));
        Arguments.checkThat(positiveInt).is(instanceOf(Integer.class));
        Arguments.checkThat(positiveInt).is(instanceOf(Object.class));
        assertThrowsFailedAssertion(
            () -> Arguments.checkThat(positiveInt).is(instanceOf(String.class))
        );
        assertThrowsFailedAssertion(
            () -> Arguments.checkThat(string).is(instanceOf(Number.class))
        );
    }

    @Test
    void testInstanceOfEdgeCases() {
        var assertion = instanceOf(Number.class);
        assertThrows(() -> assertion.check(null));
    }

    @Test
    void testNot() {
        // Given
        AlchemyAssertion<Object> mock = mock();
        doThrow(FailedAssertionException.class)
            .when(mock)
                .check(any());

        var instance = Assertions.not(mock);

        // When
        instance.check("");
        // Then
        verify(mock).check("");

        // When, no exception
        AlchemyAssertion<Object> successMock = mock();
        // Then, exception
        assertThrowsFailedAssertion(() -> Assertions.not(successMock).check(""));
        verify(successMock).check("");
    }

    @Test
    void testNot_EdgeCases() {
        assertThrows(() -> Assertions.not(null))
            .isIllegalArgumentException();
    }

    @Test
    void testEqualTo() {
        var first = string;
        var second = "";

        do {
            second = StringGenerators.strings().get();
        } while (first.equals(second));

        final var instance = Assertions.equalTo(second);

        instance.check(second); // standard equal
        instance.check(new StringBuilder(second).toString()); // copy equals
        assertThrowsFailedAssertion(() -> instance.check(first)); // not equal ❌
    }
}
