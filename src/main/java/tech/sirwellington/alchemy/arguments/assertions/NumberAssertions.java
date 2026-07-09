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
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;

import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;
import static tech.sirwellington.alchemy.arguments.internal.Checks.checkThat;
import static tech.sirwellington.alchemy.arguments.internal.Checks.failAssertion;

/**
 * Factory methods for {@link AlchemyAssertion}s on numeric types.
 *
 * @author SirWellington
 */
@NonInstantiable
public final class NumberAssertions {

    private NumberAssertions() throws IllegalAccessException {
        throw new IllegalAccessException("Cannot instantiate NumberAssertions");
    }

    /**
     * Asserts that an integer is strictly greater than the supplied value.
     */
    public static AlchemyAssertion<Integer> greaterThan(int exclusiveLowerBound) {
        if (exclusiveLowerBound == Integer.MAX_VALUE) {
            failAssertion("Integers cannot exceed {0}", Integer.MAX_VALUE);
        }

        return number -> {
            notNull().check(number);

            if (number <= exclusiveLowerBound) {
                failAssertion("Number must be > {0}", exclusiveLowerBound);
            }
        };
    }

    /**
     * Asserts that a long is strictly greater than the supplied value.
     */
    public static AlchemyAssertion<Long> greaterThan(long exclusiveLowerBound) {
        if (exclusiveLowerBound == Long.MAX_VALUE) {
            failAssertion("Longs cannot exceed {0}", Long.MAX_VALUE);
        }

        return number -> {
            notNull().check(number);

            if (number <= exclusiveLowerBound) {
                failAssertion("Number must be > {0}", exclusiveLowerBound);
            }
        };
    }

    /**
     * Asserts that a double is strictly greater than the supplied value within a margin of error.
     *
     * @param exclusiveLowerBound The lower bound (exclusive)
     * @param delta               The tolerance; added to {@code number} for comparison
     */
    public static AlchemyAssertion<Double> greaterThan(double exclusiveLowerBound, double delta) {
        if (Double.compare(exclusiveLowerBound, Double.MAX_VALUE) == 0) {
            failAssertion("Doubles cannot exceed {0}", Double.MAX_VALUE);
        }

        return number -> {
            notNull().check(number);

            // Use Math.abs(delta) to ensure symmetry in tolerance
            if (number + Math.abs(delta) <= exclusiveLowerBound) {
                failAssertion("Number must be > {0} ± {1}", exclusiveLowerBound, delta);
            }
        };
    }

    /**
     * Overload of {@link #greaterThan(double, double)} with default zero delta.
     */
    public static AlchemyAssertion<Double> greaterThan(double exclusiveLowerBound) {
        return greaterThan(exclusiveLowerBound, 0.0);
    }

    /**
     * Asserts that an integer is greater than or equal to the supplied value.
     */
    public static AlchemyAssertion<Integer> greaterThanOrEqualTo(int inclusiveLowerBound) {
        return number -> {
            notNull().check(number);

            if (number < inclusiveLowerBound) {
                failAssertion("Number must be greater than or equal to {0}", inclusiveLowerBound);
            }
        };
    }

    /**
     * Asserts that a long is greater than or equal to the supplied value.
     */
    public static AlchemyAssertion<Long> greaterThanOrEqualTo(long inclusiveLowerBound) {
        return number -> {
            notNull().check(number);

            if (number < inclusiveLowerBound) {
                failAssertion("Number must be greater than or equal to {0}", inclusiveLowerBound);
            }
        };
    }

    /**
     * Asserts that a double is greater than or equal to the supplied value within margin of error.
     */
    public static AlchemyAssertion<Double> greaterThanOrEqualTo(double inclusiveLowerBound, double delta) {
        return number -> {
            notNull().check(number);

            if (number + Math.abs(delta) < inclusiveLowerBound) {
                failAssertion("Number must be >= {0} ± {1}", inclusiveLowerBound, delta);
            }
        };
    }

    /**
     * Overload of {@link #greaterThanOrEqualTo(double, double)} with default zero delta.
     */
    public static AlchemyAssertion<Double> greaterThanOrEqualTo(double inclusiveLowerBound) {
        return greaterThanOrEqualTo(inclusiveLowerBound, 0.0);
    }

    /**
     * Asserts that an integer is positive (> 0).
     */
    public static AlchemyAssertion<Integer> positiveInteger() {
        return number -> {
            notNull().check(number);

            if (number <= 0) {
                failAssertion("Expected positive integer: {0}", number);
            }
        };
    }

    /**
     * Asserts that an integer is negative (< 0).
     */
    public static AlchemyAssertion<Integer> negativeInteger() {
        return lessThan(0);
    }

    /**
     * Asserts that a long is positive (> 0).
     */
    public static AlchemyAssertion<Long> positiveLong() {
        return number -> {
            notNull().check(number);

            if (number <= 0) {
                failAssertion("Expected positive long: {0}", number);
            }
        };
    }

    /**
     * Asserts that a long is negative (< 0).
     */
    public static AlchemyAssertion<Long> negativeLong() {
        return lessThan(0L);
    }

    /**
     * Asserts that an integer is less than or equal to the supplied value.
     */
    public static AlchemyAssertion<Integer> lessThanOrEqualTo(int inclusiveUpperBound) {
        return number -> {
            notNull().check(number);

            if (number > inclusiveUpperBound) {
                failAssertion("Number must be less than or equal to {0}", inclusiveUpperBound);
            }
        };
    }

    /**
     * Asserts that a long is less than or equal to the supplied value.
     */
    public static AlchemyAssertion<Long> lessThanOrEqualTo(long inclusiveUpperBound) {
        return number -> {
            notNull().check(number);

            if (number > inclusiveUpperBound) {
                failAssertion("Number must be less than or equal to {0}", inclusiveUpperBound);
            }
        };
    }

    /**
     * Asserts that a double is less than or equal to the supplied value within margin of error.
     */
    public static AlchemyAssertion<Double> lessThanOrEqualTo(double inclusiveUpperBound, double delta) {
        return number -> {
            notNull().check(number);

            if (number - Math.abs(delta) > inclusiveUpperBound) {
                failAssertion("Number must be <= {0} ± {1}", inclusiveUpperBound, delta);
            }
        };
    }

    /**
     * Overload of {@link #lessThanOrEqualTo(double, double)} with default zero delta.
     */
    public static AlchemyAssertion<Double> lessThanOrEqualTo(double inclusiveUpperBound) {
        return lessThanOrEqualTo(inclusiveUpperBound, 0.0);
    }

    /**
     * Asserts that an integer is strictly less than the supplied value.
     */
    public static AlchemyAssertion<Integer> lessThan(int exclusiveUpperBound) {
        if (exclusiveUpperBound == Integer.MIN_VALUE) {
            failAssertion("Ints cannot be less than {0}", Integer.MIN_VALUE);
        }

        return number -> {
            notNull().check(number);

            if (number >= exclusiveUpperBound) {
                failAssertion("Number must be < {0}", exclusiveUpperBound);
            }
        };
    }

    /**
     * Asserts that a long is strictly less than the supplied value.
     */
    public static AlchemyAssertion<Long> lessThan(long exclusiveUpperBound) {
        if (exclusiveUpperBound == Long.MIN_VALUE) {
            failAssertion("Longs cannot be less than {0}", Long.MIN_VALUE);
        }

        return number -> {
            notNull().check(number);

            if (number >= exclusiveUpperBound) {
                failAssertion("Number must be < {0}", exclusiveUpperBound);
            }
        };
    }

    /**
     * Asserts that a double is strictly less than the supplied value within margin of error.
     */
    public static AlchemyAssertion<Double> lessThan(double exclusiveUpperBound, double delta) {
        checkThat(exclusiveUpperBound > -Double.MAX_VALUE, "Doubles cannot be less than {0}");
        return number -> {
            notNull().check(number);

            if (number - Math.abs(delta) >= exclusiveUpperBound) {
                failAssertion("Number must be < {0}", exclusiveUpperBound);
            }
        };
    }

    /**
     * Overload of {@link #lessThan(double, double)} with default zero delta.
     */
    public static AlchemyAssertion<Double> lessThan(double exclusiveUpperBound) {
        return lessThan(exclusiveUpperBound, 0.0);
    }

    /**
     * Asserts that an integer is in the inclusive range [min, max].
     *
     * @throws IllegalArgumentException if min ≥ max
     */
    public static AlchemyAssertion<Integer> numberBetween(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Minimum must be less than Max.");
        }

        return number -> {
            notNull().check(number);

            if (number < min || number > max) {
                failAssertion("Expected a number between {0} and {1} but got {2} instead", min, max, number);
            }
        };
    }

    /**
     * Asserts that a long is in the inclusive range [min, max].
     *
     * @throws IllegalArgumentException if min ≥ max
     */
    public static AlchemyAssertion<Long> numberBetween(long min, long max) {
        if (min >= max) {
            throw new IllegalArgumentException("Minimum must be less than Max.");
        }

        return number -> {
            notNull().check(number);

            if (number < min || number > max) {
                failAssertion("Expected a number between {0} and {1} but got {2} instead", min, max, number);
            }
        };
    }
}
