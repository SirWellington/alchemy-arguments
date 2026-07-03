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

import org.junit.jupiter.api.Test;
import tech.sirwellington.alchemy.arguments.assertions.Assertions;
import tech.sirwellington.alchemy.test.AlchemyTest;
import tech.sirwellington.alchemy.test.generation.GenerateInteger;
import tech.sirwellington.alchemy.test.generation.GenerateString;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.CollectionAssertions.*;
import static tech.sirwellington.alchemy.arguments.assertions.NumberAssertions.negativeInteger;
import static tech.sirwellington.alchemy.arguments.assertions.NumberAssertions.positiveInteger;
import static tech.sirwellington.alchemy.arguments.assertions.StringAssertions.nonEmptyString;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;

/**
 * @author SirWellington
 */
@AlchemyTest
public final class JavaCodeTest {
    
    @GenerateString
    private String string;

    @GenerateInteger(GenerateInteger.Type.POSITIVE)
    private Integer positiveNumber;

    @GenerateInteger(GenerateInteger.Type.NEGATIVE)
    private Integer negativeNumber;

    @Test
    public void testNonEmptyString() {
        AlchemyAssertion<String> assertion = nonEmptyString();
        assertion.check(string);

        checkThat(string).isA(nonEmptyString());
    }

    @Test
    public void testPositiveInt() {
        checkThat(positiveNumber)
            .isA(positiveInteger());
    }

    @Test
    public void testPositiveIntWithBadArg() {
        assertThrows(
            () -> checkThat(negativeNumber).isA(positiveInteger())
        ).isInstanceOf(FailedAssertionException.class);
        
    }

    @Test
    public void testNegativeInt() {
        checkThat(negativeNumber)
            .isA(negativeInteger());
    }

    @Test
    public void testNegativeIntWithBadArg() {
        assertThrows(
            () -> checkThat(negativeNumber).isA(positiveInteger())
        ).isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testCombine() {
        AlchemyAssertion<String> first = mock(AlchemyAssertion.class);
        AlchemyAssertion<String> second = mock(AlchemyAssertion.class);

        AlchemyAssertion<String> combined = Assertions.combine(first, second);

        combined.check(string);

        verify(first).check(string);
        verify(second).check(string);
    }

    @Test
    public void testEmptyCollections() {
        checkThat(Collections.emptyList())
            .isA(emptyList());

        checkThat(Collections.emptySet())
            .isA(emptySet());

        checkThat(Collections.emptyMap())
            .isA(emptyMap());
    }

    @Test
    public void testNonEmptyList() {
        final var emptyList = List.of();
        assertThrows(
            () -> checkThat(emptyList).isA(nonEmptyList())
        ).isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testNonemptySet() {
        final var emptySet = Set.of();
        assertThrows(
            () -> checkThat(emptySet).isA(nonEmptySet())
        );
    }

    @Test
    public void testCheckStringNotEmpty() {
        checkThat(string)
            .isA(nonEmptyString());
    }
}
