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

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tech.sirwellington.alchemy.arguments.TestHelpers.assertThrowsFailedAssertion;
import static tech.sirwellington.alchemy.arguments.assertions.TimeAssertions.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.TimeGenerators.futureInstants;
import static tech.sirwellington.alchemy.generator.TimeGenerators.pastInstants;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;

/**
 * Tests for {@link TimeAssertions}.
 *
 * @author SirWellington
 */
@DisplayName("TimeAssertions Tests")
@AlchemyTest
final class TimeAssertionsTest {

    @Test
    @DisplayName("testCannotInstantiate: TimeAssertions should be non-instantiable")
    void testCannotInstantiate() {
        assertThrows(
            () -> TimeAssertions.class.getDeclaredConstructor().newInstance()
        ).isInstanceOf(InvocationTargetException.class)
         .hasCauseInstanceOf(IllegalAccessException.class)
         .containsInMessage("cannot directly instantiate");
    }

    @Test
    @DisplayName("testInThePast: inThePast assertion works correctly")
    void testInThePast() throws InterruptedException {
        var startTime = Instant.now();

        var instance = inThePast();
        assertNotNull(instance, "inThePast should return a non-null assertion");

        var past = one(pastInstants());
        instance.check(past);

        var future = one(futureInstants());
        assertThrowsFailedAssertion(() -> instance.check(future));

        Thread.sleep(1);
        instance.check(startTime);
    }

    @Test
    @DisplayName("testBefore: before assertion works correctly")
    void testBefore() {
        // Given
        var startTime = Instant.now();
        // When
        var instance = before(startTime);
        // Then
        assertNotNull(instance, "before should return a non-null assertion");
        assertThrowsFailedAssertion(() -> instance.check(startTime));

        // The past is before the present
        var past = one(pastInstants());
        instance.check(past);

        // The future is not before now, should fail
        var future = one(futureInstants());
        assertThrowsFailedAssertion(
            () -> instance.check(future)
        );
    }

    @Test
    @DisplayName("testBefore: before assertion handles bad parameters")
    void testBefore_HandlesBadArguments() {
        // Given
        var instance = before(Instant.now());

        // Then
        assertThrowsFailedAssertion(
            () -> instance.check(null)
        );
        assertThrows(
            () -> before(null)
        ).isIllegalArgumentException();
    }

    @Test
    @DisplayName("testInTheFuture: inTheFuture assertion works correctly")
    void testInTheFuture() {
        var startTime = Instant.now();

        var instance = inTheFuture();
        assertNotNull(instance, "inTheFuture should return a non-null assertion");

        var future = one(futureInstants());
        instance.check(future);

        var past = one(pastInstants());
        assertThrowsFailedAssertion(() -> instance.check(past));
        assertThrowsFailedAssertion(() -> instance.check(startTime));
    }

    @Test
    @DisplayName("testAfter: after assertion works correctly")
    void testAfter() {
        var referenceTime = Instant.now();

        var instance = after(referenceTime);
        assertNotNull(instance, "after should return a non-null assertion");

        assertThrowsFailedAssertion(() -> instance.check(referenceTime));

        var past = one(pastInstants());
        assertThrows(() -> instance.check(past));

        var future = one(futureInstants());
        instance.check(future);
    }

    @Test
    @DisplayName("testRightNow: rightNow assertion works correctly")
    void testRightNow() {
        var instance = rightNow();
        assertNotNull(instance, "rightNow should return a non-null assertion");

        var now = Instant.now();
        instance.check(now);

        var past = one(pastInstants());
        assertThrowsFailedAssertion(() -> instance.check(past));

        var future = one(futureInstants());
        assertThrowsFailedAssertion(() -> instance.check(future));
    }

    @Test
    @DisplayName("testNowWithinDelta: nowWithinDelta assertion works correctly")
    void testNowWithinDelta() {
        var delta = 100L;

        var instance = nowWithinDelta(delta);
        assertNotNull(instance, "nowWithinDelta should return a non-null assertion");

        var now = Instant.now();
        instance.check(now);

        var past = now.minus(50L, ChronoUnit.MILLIS);
        instance.check(past);

        var farPast = now.minus(1000L, ChronoUnit.MILLIS);
        assertThrowsFailedAssertion(() -> instance.check(farPast));
    }

    @Test
    @DisplayName("testNowWithinDeltaWithBadArguments: nowWithinDelta handles bad arguments")
    void testNowWithinDeltaWithBadArguments() {
        assertThrows(
            () -> nowWithinDelta(-1L)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("testEqualToInstantWithinDelta: equalToInstantWithinDelta works correctly")
    void testEqualToInstantWithinDelta() {
        var baseTime = Instant.now();
        var delta = 100L;

        var instance = equalToInstantWithinDelta(baseTime, delta);
        assertNotNull(instance, "equalToInstantWithinDelta should return a non-null assertion");

        var sameTime = baseTime;
        instance.check(sameTime);

        var closeTime = baseTime.plus(50L, ChronoUnit.MILLIS);
        instance.check(closeTime);

        var farTime = baseTime.plus(1000L, ChronoUnit.MILLIS);
        assertThrowsFailedAssertion(() -> instance.check(farTime));
    }

    @Test
    @DisplayName("testEqualToInstantWithinDeltaWhenDifferenceExceeded: equalToInstantWithinDelta fails when difference exceeds delta")
    void testEqualToInstantWithinDeltaWhenDifferenceExceeded() {
        var baseTime = Instant.now();
        var delta = 10L;

        var instance = equalToInstantWithinDelta(baseTime, delta);

        var distantTime = baseTime.plus(100L, ChronoUnit.MILLIS);
        assertThrowsFailedAssertion(() -> instance.check(distantTime));
    }

    @Test
    @DisplayName("testEqualToInstantWithinDeltaWithBadArgs: equalToInstantWithinDelta handles bad arguments")
    void testEqualToInstantWithinDeltaWithBadArgs() {
        assertThrows(
            () -> equalToInstantWithinDelta(null, 100L)
        ).isInstanceOf(IllegalArgumentException.class);

        assertNotNull(
            equalToInstantWithinDelta(Instant.now(), -10L)
        );
    }

    @Test
    @DisplayName("testEpochRightNow: epochRightNow assertion works correctly")
    void testEpochRightNow() {
        var instance = epochRightNow();
        assertNotNull(instance, "epochRightNow should return a non-null assertion");

        var now = System.currentTimeMillis();
        instance.check(now);

        assertThrowsFailedAssertion(() -> instance.check(0L));
    }

    @Test
    @DisplayName("testEpochNowWithinDelta: epochNowWithinDelta assertion works correctly")
    void testEpochNowWithinDelta() {
        var delta = 100L;

        var instance = epochNowWithinDelta(delta);
        assertNotNull(instance, "epochNowWithinDelta should return a non-null assertion");

        var now = System.currentTimeMillis();
        instance.check(now);

        var past = now - 50L;
        instance.check(past);

        var farPast = now - 1000L;
        assertThrowsFailedAssertion(() -> instance.check(farPast));
    }

    @Test
    @DisplayName("testEpochNowWithinDeltaBadArguments: epochNowWithinDelta handles bad arguments")
    void testEpochNowWithinDeltaBadArguments() {
        assertThrows(
            () -> epochNowWithinDelta(-1L)
        ).isInstanceOf(IllegalArgumentException.class);
    }

}