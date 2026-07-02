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

import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.annotations.arguments.Required;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;

import java.time.Instant;

import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;
import static tech.sirwellington.alchemy.arguments.internal.Checks.*;

/**
 * Factory methods for {@link AlchemyAssertion}s on {@link Instant} and epoch millisecond values.
 *
 * @author SirWellington
 */
@NonInstantiable
public final class TimeAssertions {

    private TimeAssertions() throws IllegalAccessException {
        throw new IllegalAccessException("Cannot instantiate TimeAssertions");
    }

    /**
     * Asserts that the given {@link Instant} is in the past (strictly before current time).
     * Note: {@code now} is recalculated on each invocation to remain up-to-date.
     */
    public static AlchemyAssertion<Instant> inThePast() {
        return instant -> {
            var present = Instant.now();
            if (!instant.isBefore(present)) {
                failAssertion(
                    "Expected Timestamp [{0}] to be in the past. Now: [{1}]",
                    instant,
                    present
                );
            }
        };
    }

    /**
     * Asserts that the given {@link Instant} is strictly before the specified {@code expected}.
     *
     * @param expected the reference time (must not be null)
     */
    public static AlchemyAssertion<Instant> before(@Required Instant expected) {
        checkNotNull(expected, "expected time cannot be null");

        return argument -> {
            notNull().check(argument);
            if (!argument.isBefore(expected)) {
                failAssertion("Expected Timestamp to be before {0}", expected);
            }
        };
    }

    /**
     * Asserts that the given {@link Instant} is in the future (strictly after current time).
     */
    public static AlchemyAssertion<Instant> inTheFuture() {
        return instant -> {
            var present = Instant.now();
            if (!instant.isAfter(present)) {
                failAssertion(
                    "Expected Timestamp [{0}] to be in the future. Now: [{1}]",
                    instant,
                    present
                );
            }
        };
    }

    /**
     * Asserts that the given {@link Instant} is strictly after the specified {@code expected}.
     *
     * @param expected the reference time (must not be null)
     */
    public static AlchemyAssertion<Instant> after(@Required Instant expected) {
        checkNotNull(expected, "expected time cannot be null");

        return argument -> {
            notNull().check(argument);
            if (!argument.isAfter(expected)) {
                failAssertion("Expected Timestamp to be after [{0}]", expected);
            }
        };
    }

    /**
     * Asserts that the given {@link Instant} is "right now", within ±5 ms tolerance.
     *
     * @see #nowWithinDelta(long)
     */
    public static AlchemyAssertion<Instant> rightNow() {
        return nowWithinDelta(5L);
    }

    /**
     * Asserts that the given {@link Instant} matches current time within the specified margin of error.
     *
     * @param marginOfErrorInMillis acceptable delta (≥ 0)
     * @throws IllegalArgumentException if {@code marginOfErrorInMillis < 0}
     */
    public static AlchemyAssertion<Instant> nowWithinDelta(long marginOfErrorInMillis) {
        checkThat(marginOfErrorInMillis >= 0, "millis must be non-negative");

        return instant -> {
            notNull().check(instant);

            long nowMillis = Instant.now().toEpochMilli();
            long epoch = instant.toEpochMilli();
            long difference = Math.abs(epoch - nowMillis);

            if (difference > marginOfErrorInMillis) {
                failAssertion(
                    "Time difference of {0} ms exceeded margin-of-error of {1} ms",
                    difference, marginOfErrorInMillis
                );
            }
        };
    }

    /**
     * Asserts that the given {@link Instant} is equal to another instant within a tolerance (in milliseconds).
     *
     * @param instant     the reference instant (must not be null)
     * @param deltaMillis acceptable absolute difference in milliseconds (±delta)
     */
    public static AlchemyAssertion<Instant> equalToInstantWithinDelta(
        @Required Instant instant, long deltaMillis) {

        checkNotNull(instant, "instant cannot be null");
        var delta = Math.abs(deltaMillis);

        return argument -> {
            notNull().check(argument);
            var difference = Math.abs(argument.toEpochMilli() - instant.toEpochMilli());
            if (difference > delta) {
                failAssertion(
                    "Delta should not exceed {0} ms, but is {1} ms",
                    delta, difference
                );
            }
        };
    }

    /**
     * Asserts that the given epoch millisecond value ("now") matches current system time within ±5 ms.
     *
     * @see #epochNowWithinDelta(long)
     */
    public static AlchemyAssertion<Long> epochRightNow() {
        return epochNowWithinDelta(5L);
    }

    /**
     * Asserts that the given epoch millisecond value is equal to {@link Instant#now()} within a tolerance.
     *
     * @param marginOfErrorInMillis acceptable delta (≥ 0)
     * @throws IllegalArgumentException if {@code marginOfErrorInMillis < 0}
     */
    public static AlchemyAssertion<Long> epochNowWithinDelta(long marginOfErrorInMillis) {
        checkThat(marginOfErrorInMillis >= 0, "marginOfErrorInMillis must be non-negative");

        return epoch -> {
            long nowMillis = Instant.now().toEpochMilli();
            // Ensure positive and valid (e.g., not 0 for safety, though strictly only needs to be ≥ 0)
            if (epoch <= 0L) {
                failAssertion("Epoch must be > 0, got {0}", epoch);
            }

            long difference = Math.abs(epoch - nowMillis);
            if (difference > marginOfErrorInMillis) {
                failAssertion(
                    "Time difference of {0} ms exceeded margin-of-error of {1} ms",
                    difference,
                    marginOfErrorInMillis
                );
            }
        };
    }
}
