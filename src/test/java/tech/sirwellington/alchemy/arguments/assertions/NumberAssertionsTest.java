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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tech.sirwellington.alchemy.test.AlchemyTest;

import static tech.sirwellington.alchemy.arguments.assertions.NumberAssertions.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.*;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;

/**
 * Tests for {@link NumberAssertions}.
 *
 * @author SirWellington
 */
@DisplayName("NumberAssertions Tests")
@AlchemyTest
final class NumberAssertionsTest {

    //==============================
    //INTEGER TESTS
    //==============================

    @Test
    @DisplayName("testNumberBetweenInts: numberBetween assertion works correctly for integers")
    void testNumberBetweenInts() {
        // Given
        int min = one(integers(Integer.MIN_VALUE, Integer.MAX_VALUE - 10));
        int max = one(integers(min+1, Integer.MAX_VALUE));
        // When
        var instance = numberBetween(min, max);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var goodNumbers = integers(min, max);
        var belowMin = integers(Integer.MIN_VALUE, min);
        // Then
        Tests.runTests(instance, belowMin, goodNumbers);

        // Given
        var aboveMin = integers(max+1, Integer.MAX_VALUE);
        // Then
        Tests.runTests(instance, aboveMin, goodNumbers);
    }

    @Test
    @DisplayName("testNumberBetweenIntsEdgeCases: numberBetween handles invalid min/max")
    void testNumberBetweenIntsEdgeCases() {
        int min = one(integers(Integer.MIN_VALUE, Integer.MAX_VALUE - 10));
        int max = one(integers(min, Integer.MAX_VALUE));

        assertThrows(() -> numberBetween(max, min))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("testIntLessThan: lessThan assertion works correctly for integers")
    void testIntLessThan() {
        // Given
        int upperBound = one(integers(-1000, 1000));
        var instance = lessThan(upperBound);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var badNumbers = integers(0, 100).mapping(x -> upperBound + x);
        var goodNumbers = smallPositiveIntegers().mapping(x -> upperBound - x);
        // Then
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    @DisplayName("testIntLessThanEdgeCases: lessThan handles invalid input")
    void testIntLessThanEdgeCases() {
        assertThrows(() -> lessThan(Integer.MIN_VALUE))
            .isIllegalArgumentException();
    }

    @Test
    @DisplayName("testIntLessThanOrEqualTo: lessThanOrEqualTo assertion works correctly")
    void testIntLessThanOrEqualTo() {
        // Given
        var upperBound = one(integers(-1000, 1000));
        var instance = lessThanOrEqualTo(upperBound);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var badNumbers = smallPositiveIntegers().mapping(x -> upperBound + x);
        var goodNumbers = integers(0, 1000).mapping(x -> upperBound - x);
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    @DisplayName("testIntGreaterThan: greaterThan assertion works correctly")
    void testIntGreaterThan() {
        // Given
        var lowerBound = one(integers(-1000, 1000));
        var instance = greaterThan(lowerBound);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var badNumbers = integers(
            lowerBound- one(smallPositiveIntegers()),
            lowerBound
        );
        var goodNumbers = integers(
            lowerBound + 1,
            lowerBound + one(integers(2, 1000))
        );
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    @DisplayName("testIntGreaterThanEdgeCases: greaterThan handles invalid input")
    void testIntGreaterThanEdgeCases() {
        assertThrows(() -> greaterThan(Integer.MAX_VALUE))
            .isIllegalArgumentException();
    }

    @Test
    @DisplayName("testIntGreaterThanOrEqualTo: greaterThanOrEqualTo assertion works correctly")
    void testIntGreaterThanOrEqualTo() {
        // Given
        var inclusiveLowerBound = one(integers(-1000, 1000));
        var instance = greaterThanOrEqualTo(inclusiveLowerBound);

        // Then
        Tests.checkForNullCase(instance);
        instance.check(inclusiveLowerBound);

        // Given
        var goodNumbers = integers(1, 100).mapping(x -> inclusiveLowerBound + x);
        var badNumbers = integers(25, 1000).mapping(x -> inclusiveLowerBound - x);
        // Then
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    @DisplayName("testPositiveInteger: positiveInteger assertion works correctly")
    void testPositiveInteger() {
        // Given
        var instance = positiveInteger();
        // Then
        Tests.checkForNullCase(instance);
        // Given
        var badNumbers = negativeIntegers();
        var goodNumbers = positiveIntegers();
        // Then
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    @DisplayName("testNegativeInteger: negativeInteger assertion works correctly")
    void testNegativeInteger() {
        // Given
        var instance = negativeInteger();
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var badNumbers = positiveIntegers();
        var goodNumbers = negativeIntegers();
        // Then
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    //==============================
    //LONG TESTS
    //==============================

    @Test
    @DisplayName("testLongGreaterThan: greaterThan assertion works correctly for longs")
    void testLongGreaterThan() {
        // Given
        var lowerBound = one(longs(-100000L, 100000L));
        // When
        var instance = greaterThan(lowerBound);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var badNumbers = longs(0, 1000L).mapping(x -> lowerBound - x);
        var goodNumbers = smallPositiveLongs().mapping(x -> lowerBound + x);
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    @DisplayName("testLongGreaterThanEdgeCases: greaterThan handles invalid input for longs")
    void testLongGreaterThanEdgeCases() {
        assertThrows(() -> greaterThan(Long.MAX_VALUE))
            .isIllegalArgumentException();
    }

    @Test
    @DisplayName("testLongGreaterThanOrEqualTo: greaterThanOrEqualTo assertion works correctly")
    void testLongGreaterThanOrEqualTo() {
        // Given
        var inclusiveLowerBound = one(longs(-10_000L, 10_000L));
        // When
        var instance = greaterThanOrEqualTo(inclusiveLowerBound);
        // Then
        Tests.checkForNullCase(instance);
        var badNumbers = longs(Long.MIN_VALUE, inclusiveLowerBound);
        var goodNumbers = longs(inclusiveLowerBound, Long.MAX_VALUE);
        // Then
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    @DisplayName("testLongLessThan: lessThan assertion works correctly for longs")
    void testLongLessThan() {
        // Given
        var upperBound = one(longs(-10000L, 100000L));
        // Whne
        var instance = lessThan(upperBound);
        // Then
        Tests.checkForNullCase(instance);
        // Given
        var badNumbers = longs(0, 10000L).mapping(x -> upperBound + x);
        var goodNumber = smallPositiveLongs().mapping(x -> upperBound - x);
        // Then
        Tests.runTests(instance, badNumbers, goodNumber);
    }

    @Test
    @DisplayName("testLongLessThanEdgeCases: lessThan handles invalid input for longs")
    void testLongLessThanEdgeCases() {
        assertThrows(() -> lessThan(Long.MIN_VALUE))
            .isIllegalArgumentException();
    }

    @Test
    @DisplayName("testLongLessThanOrEqualTo: lessThanOrEqualTo assertion works correctly")
    void testLongLessThanOrEqualTo() {
        // Given
        var lowerBound = one(longs(-10000L, 100000L));
        // When
        var instance = lessThanOrEqualTo(lowerBound);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var badNumbers = smallPositiveLongs().mapping(x -> lowerBound + x);
        var goodNumbers = longs(0, 1000L).mapping(x -> lowerBound - x);
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    @DisplayName("testNumberBetweenLongs: numberBetween assertion works correctly for longs")
    void testNumberBetweenLongs() {
        // Given
        var min = one(longs(Long.MIN_VALUE+1, Long.MAX_VALUE - 10L));
        var max = one(longs(min+1, Long.MAX_VALUE));
        var instance = numberBetween(min, max);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var goodNumbers = longs(min, max);
        var belowMin = positiveLongs().mapping(x -> min - x);
        // Then
        Tests.runTests(instance, belowMin, goodNumbers);

        // Given
        var aboveMax = positiveIntegers().mapping(x -> max + x);
        // Then
        Tests.runTests(instance, aboveMax, goodNumbers);
    }

    @Test
    @DisplayName("testNumberBetweenLongsEdgeCases: numberBetween handles invalid min/max for longs")
    void testNumberBetweenLongsEdgeCases() {
        var min = one(longs(Long.MIN_VALUE, Long.MAX_VALUE - 10L));
        var max = one(longs(min, Long.MAX_VALUE));

        assertThrows(() -> numberBetween(max, min))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("testPositiveLong: positiveLong assertion works correctly")
    void testPositiveLong() {
        // Given
        var instance = positiveLong();
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var goodNumbers = positiveLongs();
        var badNumbers =longs(Long.MIN_VALUE, 0L);
        // Then
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    @DisplayName("testNegativeLong: negativeLong assertion works correctly")
    void testNegativeLong() {
        // Given
        var instance = negativeLong();
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var badNumbers = positiveLongs();
        var goodNumbers = longs(Long.MIN_VALUE, 0L);
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    //==============================
    //DOUBLE TESTS
    //==============================

    @Test
    @DisplayName("testDoubleLessThan: lessThan assertion works correctly for doubles")
    void testDoubleLessThan() {
        // Given
        var upperBound = one(doubles(-10000.0, 100000.0));
        var instance = lessThan(upperBound);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var badNumbers = doubles(0.0, 10000.0).mapping(x -> upperBound + x);
        var goodNumbers = doubles(1.0, 100.0).mapping(x -> upperBound - x);
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    @DisplayName("testDoubleLessThanEdgeCases: lessThan handles invalid input for doubles")
    void testDoubleLessThanEdgeCases() {
        assertThrows(() -> lessThan(-Double.MAX_VALUE))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("testDoubleLessThanWithDelta: lessThan with delta works correctly")
    void testDoubleLessThanWithDelta() {
        // Given
        var upperBound = one(doubles(-10000.0, 100000.0));
        var delta = one(doubles(1.0, 10.0));
        var instance = lessThan(upperBound, delta);
        // Then
        Tests.checkForNullCase(instance);
        // Given
        var badNumbers = doubles(upperBound, Double.MAX_VALUE);
        var goodNumbers = doubles(-Double.MAX_VALUE, upperBound+delta);
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    @DisplayName("testDoubleLessThanOrEqualTo: lessThanOrEqualTo assertion works correctly")
    void testDoubleLessThanOrEqualTo() {
        // Given
        var upperBound = one(doubles(0.0, Double.MAX_VALUE/2));
        var instance = lessThanOrEqualTo(upperBound);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var badNumbers = doubles(upperBound+0.1, Double.MAX_VALUE);
        var goodNumbers = doubles(-Double.MAX_VALUE, upperBound);
        // Then
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    @DisplayName("testDoubleLessThanOrEqualToWithDelta: lessThanOrEqualTo with delta works correctly")
    void testDoubleLessThanOrEqualToWithDelta() {
        // Given
        var upperBound = one(doubles(0.0, Double.MAX_VALUE/2));
        var delta = one(doubles(1.0, 100.0));
        var instance = lessThanOrEqualTo(upperBound, delta);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var badNumbers = doubles(upperBound+delta+0.1, Double.MAX_VALUE);
        var goodNumbers = doubles(-Double.MAX_VALUE, upperBound);
        // Then
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    @DisplayName("testDoubleGreaterThan: greaterThan assertion works correctly for doubles")
    void testDoubleGreaterThan() {
        // Given
        var lowerBound = one(doubles(-100000.0, 100000.0));
        // When
        var instance = greaterThan(lowerBound);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var badNumbers = doubles(-Double.MAX_VALUE, lowerBound);
        var goodNumbers = doubles(lowerBound + 0.1, Double.MAX_VALUE);
        // Then
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    @DisplayName("testDoubleGreaterThanEdgeCases: greaterThan handles invalid input for doubles")
    void testDoubleGreaterThanEdgeCases() {
        assertThrows(() -> greaterThan(Double.MAX_VALUE))
            .isIllegalArgumentException();
    }

    @Test
    @DisplayName("testDoubleGreaterThanWithDelta: greaterThan with delta works correctly")
    void testDoubleGreaterThanWithDelta() {
        // Given
        var lowerBound = one(doubles(-100000.0, 100000.0));
        var delta = one(doubles(1.0, 100.0));
        var instance = greaterThan(lowerBound, delta);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var badNumbers = doubles(-Double.MAX_VALUE, lowerBound - delta);
        var goodNumbers = doubles(lowerBound, Double.MAX_VALUE);
        // Then
        Tests.runTests(instance, badNumbers, goodNumbers);
    }

    @Test
    @DisplayName("testDoubleGreaterThanOrEqualTo: greaterThanOrEqualTo assertion works correctly")
    void testDoubleGreaterThanOrEqualTo() {
        // Given
        var inclusiveLowerBound = one(doubles(-10000.0, 10000.0));
        var instance = greaterThanOrEqualTo(inclusiveLowerBound);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var goodNumbers = doubles(inclusiveLowerBound, Double.MAX_VALUE);
        var badNumbers = doubles(-Double.MAX_VALUE, inclusiveLowerBound);
        // Then
        Tests.runTests(instance, badNumbers, goodNumbers);
    }
}