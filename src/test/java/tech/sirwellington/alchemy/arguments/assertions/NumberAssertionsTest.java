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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tech.sirwellington.alchemy.arguments.TestHelpers.assertThrowsFailedAssertion;
import static tech.sirwellington.alchemy.arguments.assertions.NumberAssertions.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
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
        int min = one(integers(Integer.MIN_VALUE, Integer.MAX_VALUE - 10));
        int max = one(integers(min, Integer.MAX_VALUE));

        var instance = numberBetween(min, max);
        assertNotNull(instance, "numberBetween should return a non-null assertion");

        // Test null case
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Test good numbers within range
        int goodNumber = one(integers(min, max));
        instance.check(goodNumber);

        // Test number below minimum
        long belowMin = (long) min - one(positiveIntegers());
        if (belowMin < min) {
            assertThrowsFailedAssertion(() -> instance.check((int) belowMin));
        }

        // Test number above maximum
        int aboveMax = max + one(positiveIntegers());
        if (aboveMax > max) {
            assertThrowsFailedAssertion(() -> instance.check(aboveMax));
        }
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
        int upperBound = one(integers(-1000, 1000));
        var instance = lessThan(upperBound);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check((Integer) null));

        // Test bad numbers (>= upperBound)
        int badNumber = upperBound + one(integers(0, 100));
        assertThrowsFailedAssertion(() -> instance.check(badNumber));

        // Test good numbers (< upperBound)
        int goodNumber = upperBound - one(smallPositiveIntegers());
        instance.check(goodNumber);
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
        int upperBound = one(integers(-1000, 1000));
        var instance = lessThanOrEqualTo(upperBound);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Test bad numbers (> upperBound)
        int badNumber = upperBound + one(smallPositiveIntegers());
        assertThrowsFailedAssertion(() -> instance.check(badNumber));

        // Test good numbers (<= upperBound)
        int goodNumber = upperBound - one(integers(0, 1000));
        instance.check(goodNumber);

        // Test equality case
        instance.check(upperBound);
    }

    @Test
    @DisplayName("testIntGreaterThan: greaterThan assertion works correctly")
    void testIntGreaterThan() {
        int lowerBound = one(integers(-1000, 1000));
        var instance = greaterThan(lowerBound);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Test bad numbers (<= lowerBound)
        int badNumber = lowerBound - one(smallPositiveIntegers());
        assertThrowsFailedAssertion(() -> instance.check(badNumber));

        // Test good numbers (> lowerBound)
        int goodNumber = lowerBound + one(integers(2, 1000));
        instance.check(goodNumber);
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
        var inclusiveLowerBound = one(integers(-1000, 1000));
        var instance = greaterThanOrEqualTo(inclusiveLowerBound);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Test equality case
        instance.check(inclusiveLowerBound);

        // Test good numbers (> lowerBound)
        int amountToAdd = one(integers(40, 100));
        instance.check(inclusiveLowerBound + amountToAdd);

        // Test bad numbers (< lowerBound)
        int amountToSubtract = one(integers(50, 100));
        int badValue = inclusiveLowerBound - amountToSubtract;
        assertThrowsFailedAssertion(() -> instance.check(badValue));
    }

    @Test
    @DisplayName("testPositiveInteger: positiveInteger assertion works correctly")
    void testPositiveInteger() {
        var instance = positiveInteger();

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        var goodNumber = one(positiveIntegers());
        instance.check(goodNumber);

        var badNumber = one(negativeIntegers());
        assertThrowsFailedAssertion(() -> instance.check(badNumber));
    }

    @Test
    @DisplayName("testNegativeInteger: negativeInteger assertion works correctly")
    void testNegativeInteger() {
        var instance = negativeInteger();

        assertNotNull(instance);

        int negative = one(negativeIntegers());
        instance.check(negative);

        int positive = one(positiveIntegers());
        assertThrowsFailedAssertion(() -> instance.check(positive));
        assertThrowsFailedAssertion(() -> instance.check(null));
    }

    //==============================
    //LONG TESTS
    //==============================

    @Test
    @DisplayName("testLongGreaterThan: greaterThan assertion works correctly for longs")
    void testLongGreaterThan() {
        var lowerBound = one(longs(-100000L, 100000L));
        var instance = greaterThan(lowerBound);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Test bad numbers (<= lowerBound)
        long badNumber = lowerBound - one(longs(0, 1000L));
        assertThrowsFailedAssertion(() -> instance.check(badNumber));

        // Test good numbers (> lowerBound)
        long goodNumber = lowerBound + one(smallPositiveLongs());
        instance.check(goodNumber);
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
        var inclusiveLowerBound = one(longs(-10_000L, 10_000L));
        var instance = greaterThanOrEqualTo(inclusiveLowerBound);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Test good numbers (>= lowerBound)
        long goodNumber = one(longs(inclusiveLowerBound, Long.MAX_VALUE));
        instance.check(goodNumber);

        // Test bad numbers (< lowerBound)
        long badNumber = one(longs(Long.MIN_VALUE, inclusiveLowerBound));
        assertThrowsFailedAssertion(() -> instance.check(badNumber));
    }

    @Test
    @DisplayName("testLongLessThan: lessThan assertion works correctly for longs")
    void testLongLessThan() {
        var upperBound = one(longs(-10000L, 100000L));
        var instance = lessThan(upperBound);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Test bad numbers (>= upperBound)
        long badNumber = upperBound + one(longs(0, 10000L));
        assertThrowsFailedAssertion(() -> instance.check(badNumber));

        // Test good numbers (< upperBound)
        long goodNumber = upperBound - one(smallPositiveLongs());
        instance.check(goodNumber);
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
        var lowerBound = one(longs(-10000L, 100000L));
        var instance = lessThanOrEqualTo(lowerBound);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check((Long) null));

        // Test bad numbers (> lowerBound)
        long badNumber = lowerBound + one(smallPositiveLongs());
        assertThrowsFailedAssertion(() -> instance.check(badNumber));

        // Test good numbers (<= lowerBound)
        long goodNumber = lowerBound - one(longs(0, 1000L));
        instance.check(goodNumber);

        // Test equality case
        instance.check(lowerBound);
    }

    @Test
    @DisplayName("testNumberBetweenLongs: numberBetween assertion works correctly for longs")
    void testNumberBetweenLongs() {
        long min = one(longs(Long.MIN_VALUE, Long.MAX_VALUE - 10L));
        long max = one(longs(min, Long.MAX_VALUE));

        var instance = numberBetween(min, max);
        assertNotNull(instance);

        // Test null case
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Test good numbers within range
        long goodNumber = one(longs(min, max));
        instance.check(goodNumber);

        // Test number below minimum
        long belowMin = min - one(positiveLongs());
        if (belowMin < min) {
            assertThrowsFailedAssertion(() -> instance.check(belowMin));
        }

        // Test number above maximum
        long aboveMax = max + one(positiveIntegers());
        if (aboveMax > max) {
            assertThrowsFailedAssertion(() -> instance.check(aboveMax));
        }
    }

    @Test
    @DisplayName("testNumberBetweenLongsEdgeCases: numberBetween handles invalid min/max for longs")
    void testNumberBetweenLongsEdgeCases() {
        long min = one(longs(Long.MIN_VALUE, Long.MAX_VALUE - 10L));
        long max = one(longs(min, Long.MAX_VALUE));

        assertThrows(() -> numberBetween(max, min))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("testPositiveLong: positiveLong assertion works correctly")
    void testPositiveLong() {
        var instance = positiveLong();

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        long goodNumber = one(positiveLongs());
        instance.check(goodNumber);

        long badNumber = one(longs(Long.MIN_VALUE, 0L));
        assertThrowsFailedAssertion(() -> instance.check(badNumber));
    }

    @Test
    @DisplayName("testNegativeLong: negativeLong assertion works correctly")
    void testNegativeLong() {
        var instance = negativeLong();

        assertNotNull(instance);

        long negative = one(longs(Long.MIN_VALUE, 0L));
        instance.check(negative);

        long positive = one(positiveLongs());
        assertThrowsFailedAssertion(() -> instance.check(positive));
        assertThrowsFailedAssertion(() -> instance.check(null));
    }

    //==============================
    //DOUBLE TESTS
    //==============================

    @Test
    @DisplayName("testDoubleLessThan: lessThan assertion works correctly for doubles")
    void testDoubleLessThan() {
        double upperBound = one(doubles(-10000.0, 100000.0));
        var instance = lessThan(upperBound);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Test bad numbers (>= upperBound)
        double badNumber = upperBound + one(doubles(0.0, 10000.0));
        assertThrowsFailedAssertion(() -> instance.check(badNumber));

        // Test good numbers (< upperBound)
        double goodNumber = upperBound - one(doubles(1.0, 100.0));
        instance.check(goodNumber);
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
        double upperBound = one(doubles(-10000.0, 100000.0));
        double delta = one(doubles(1.0, 10.0));
        var instance = lessThan(upperBound, delta);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Test bad numbers (>= upperBound + delta)
        double badNumber = upperBound + delta + 1.0;
        assertThrowsFailedAssertion(() -> instance.check(badNumber));

        // Test good numbers (< upperBound - delta)
        double goodNumber = upperBound - delta;
        instance.check(goodNumber);
    }

    @Test
    @DisplayName("testDoubleLessThanOrEqualTo: lessThanOrEqualTo assertion works correctly")
    void testDoubleLessThanOrEqualTo() {
        double upperBound = one(doubles(0.0, Double.MAX_VALUE / 2));
        var instance = lessThanOrEqualTo(upperBound);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Test bad numbers (> upperBound)
        double badNumber = upperBound + 0.1;
        assertThrowsFailedAssertion(() -> instance.check(badNumber));

        // Test good numbers (<= upperBound)
        double goodNumber = one(doubles(-Double.MAX_VALUE, upperBound));
        instance.check(goodNumber);
    }

    @Test
    @DisplayName("testDoubleLessThanOrEqualToWithDelta: lessThanOrEqualTo with delta works correctly")
    void testDoubleLessThanOrEqualToWithDelta() {
        var upperBound = one(doubles(0.0, Double.MAX_VALUE / 2));
        var delta = one(doubles(1.0, 100.0));
        var instance = lessThanOrEqualTo(upperBound, delta);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Test bad numbers (> upperBound)
        double badNumber = upperBound + delta + 0.1;
        assertThrowsFailedAssertion(() -> instance.check(badNumber));

        // Test good numbers (<= upperBound - delta)
        double goodNumber = upperBound - delta;
        instance.check(goodNumber);
    }

    @Test
    @DisplayName("testDoubleGreaterThan: greaterThan assertion works correctly for doubles")
    void testDoubleGreaterThan() {
        double lowerBound = one(doubles(-100000.0, 100000.0));
        var instance = greaterThan(lowerBound);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Test bad numbers (<= lowerBound)
        double badNumber = one(doubles(-Double.MAX_VALUE, lowerBound));
        assertThrowsFailedAssertion(() -> instance.check(badNumber));

        // Test good numbers (> lowerBound)
        double goodNumber = lowerBound + 0.1;
        instance.check(goodNumber);
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
        double lowerBound = one(doubles(-100000.0, 100000.0));
        double delta = one(doubles(1.0, 100.0));
        var instance = greaterThan(lowerBound, delta);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Test bad numbers (<= lowerBound - delta)
        double badNumber = one(doubles(-Double.MAX_VALUE, lowerBound - delta));
        assertThrowsFailedAssertion(() -> instance.check(badNumber));

        // Test good numbers (> lowerBound + delta)
        double goodNumber = lowerBound;
        instance.check(goodNumber);
    }

    @Test
    @DisplayName("testDoubleGreaterThanOrEqualTo: greaterThanOrEqualTo assertion works correctly")
    void testDoubleGreaterThanOrEqualTo() {
        double inclusiveLowerBound = one(doubles(-10000.0, 10000.0));
        var instance = greaterThanOrEqualTo(inclusiveLowerBound);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Test good numbers (>= lowerBound)
        double goodNumber = one(doubles(inclusiveLowerBound, Double.MAX_VALUE));
        instance.check(goodNumber);

        // Test bad numbers (< lowerBound)
        double badNumber = one(doubles(-Double.MAX_VALUE, inclusiveLowerBound));
        assertThrowsFailedAssertion(() -> instance.check(badNumber));
    }
}