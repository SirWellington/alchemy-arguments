/*
 * Copyright 2015 SirWellington Tech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.sirwellington.alchemy.arguments.assertions

import org.slf4j.LoggerFactory
import tech.sirwellington.alchemy.annotations.access.NonInstantiable
import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.Checks
import tech.sirwellington.alchemy.arguments.FailedAssertionException
import java.lang.Math.abs

/**

 * @author SirWellington
 */
@NonInstantiable
class NumberAssertions @Throws(IllegalAccessException::class)
internal constructor()
{

    init
    {
        throw IllegalAccessException("cannot instantiate")
    }

    companion object
    {

        private val LOG = LoggerFactory.getLogger(NumberAssertions::class.java!!)

        /**
         * Asserts that an integer is `>` the supplied value.

         * @param exclusiveLowerBound The argument must be `> exclusiveLowerBound`.
         *
         *
         * @return
         */

        fun greaterThan(exclusiveLowerBound: Int): AlchemyAssertion<Int>
        {
            Checks.Internal.checkThat(exclusiveLowerBound != Integer.MAX_VALUE, "Integers cannot exceed " + Integer.MAX_VALUE)
            return AlchemyAssertion { number: Int ->
                Assertions.notNull<Any>().check(number)

                val isWithinBounds = number > exclusiveLowerBound
                if (!isWithinBounds)
                {
                    throw FailedAssertionException("Number must be > " + exclusiveLowerBound)
                }
            }
        }

        /**
         * Asserts that a long is `> exclusiveLowerBound`.

         * @param exclusiveLowerBound The argument must be `>` this value.
         *
         *
         * @return
         */

        fun greaterThan(exclusiveLowerBound: Long): AlchemyAssertion<Long>
        {
            Checks.Internal.checkThat(exclusiveLowerBound != java.lang.Long.MAX_VALUE, "Longs cannot exceed " + java.lang.Long.MAX_VALUE)
            return AlchemyAssertion { number: Long ->
                Assertions.notNull<Any>().check(number)

                val isWithinBounds = number > exclusiveLowerBound
                if (!isWithinBounds)
                {
                    throw FailedAssertionException("Number must be > " + exclusiveLowerBound)
                }
            }
        }

        /**
         * Asserts that a double is `> exclusiveLowerBound` within `delta` margin of error.

         * @param exclusiveLowerBound The argument is expected to be `>` this value.
         *
         * @param delta The allowable margin of error for the `>` operation.
         *
         *
         * @return
         */
        @JvmOverloads fun greaterThan(exclusiveLowerBound: Double, delta: Double = 0.0): AlchemyAssertion<Double>
        {
            Checks.Internal.checkThat(exclusiveLowerBound < java.lang.Double.MAX_VALUE, "Doubles cannot exceed " + java.lang.Double.MAX_VALUE)

            return AlchemyAssertion { number ->
                Assertions.notNull<Any>().check(number)

                val isWithinBounds = number!! + abs(delta) > exclusiveLowerBound
                if (!isWithinBounds)
                {
                    throw FailedAssertionException("Number must be > $exclusiveLowerBound +- $delta")
                }
            }
        }


        /**
         * Asserts that an integer is `>=` the supplied value.

         * @param inclusiveLowerBound The argument integer must be `>= inclusiveLowerBound`
         *
         *
         * @return
         */

        fun greaterThanOrEqualTo(inclusiveLowerBound: Int): AlchemyAssertion<Int>
        {
            return AlchemyAssertion { number: Int ->
                Assertions.notNull<Any>().check(number)

                val isWithinBounds = number >= inclusiveLowerBound
                if (!isWithinBounds)
                {
                    throw FailedAssertionException("Number must be greater than or equal to " + inclusiveLowerBound)
                }
            }
        }

        /**
         * Asserts that a long is `>= inclusiveLowerBound`.

         * @param inclusiveLowerBound The argument integer must be `>= inclusiveUpperBound`
         *
         *
         * @return
         */

        fun greaterThanOrEqualTo(inclusiveLowerBound: Long): AlchemyAssertion<Long>
        {
            return AlchemyAssertion { number: Long ->
                Assertions.notNull<Any>().check(number)

                val isWithinBounds = number >= inclusiveLowerBound
                if (!isWithinBounds)
                {
                    throw FailedAssertionException("Number must be greater than or equal to " + inclusiveLowerBound)
                }
            }
        }

        /**
         * Asserts that a double is `>= inclusiveLowerBound` within `delta` margin-of-error.

         * @param inclusiveLowerBound The argument double must be `>= inclusiveLowerBound` within the margin of error.
         *
         * @param delta The allowable margin-of-error for the `>= ` comparison.
         *
         *
         * @return
         */
        @JvmOverloads fun greaterThanOrEqualTo(inclusiveLowerBound: Double, delta: Double = 0.0): AlchemyAssertion<Double>
        {
            return AlchemyAssertion { number: Double ->
                Assertions.notNull<Any>().check(number)

                val isWithinBounds = number + abs(delta) >= inclusiveLowerBound
                if (!isWithinBounds)
                {
                    throw FailedAssertionException("Number must be >= $inclusiveLowerBound +- $delta")
                }
            }
        }

        /**
         * Asserts that an integer is positive, or `> 0`

         * @return
         */

        fun positiveInteger(): AlchemyAssertion<Int>
        {
            return AlchemyAssertion { number: Int ->
                Assertions.notNull<Any>().check(number)
                if (number <= 0)
                {
                    throw FailedAssertionException("Expected positive integer: " + number)
                }
            }
        }

        /**
         * Asserts that an integer is negative, or `< 0`.

         * @return
         */

        fun negativeInteger(): AlchemyAssertion<Int>
        {
            return lessThan(0)
        }

        /**
         * Asserts that an integer is `<=` the supplied value.

         * @param inclusiveUpperBound The argument must be `<= inclusiveUpperBound`.
         *
         *
         * @return
         */

        fun lessThanOrEqualTo(inclusiveUpperBound: Int): AlchemyAssertion<Int>
        {
            return AlchemyAssertion { number: Int ->
                Assertions.notNull<Any>().check(number)

                val isWithinBounds = number <= inclusiveUpperBound
                if (!isWithinBounds)
                {
                    throw FailedAssertionException("Number must be less than or equal to " + inclusiveUpperBound)
                }
            }
        }

        /**
         * Asserts that a long is `<=` the supplied value.

         * @param inclusiveUpperBound The argument must be `<= inclusiveUpperBound`.
         *
         *
         * @return
         */

        fun lessThanOrEqualTo(inclusiveUpperBound: Long): AlchemyAssertion<Long>
        {
            return AlchemyAssertion { number: Long ->
                Assertions.notNull<Any>().check(number)

                val isWithinBounds = number <= inclusiveUpperBound
                if (!isWithinBounds)
                {
                    throw FailedAssertionException("Number must be less than or equal to " + inclusiveUpperBound)
                }
            }
        }

        /**
         * Asserts that a double is `<=` the supplied value, within a `delta` margin-of-error.

         * @param inclusiveUpperBound
         *
         * @param delta The allowable margin-of-error in the `<= ` comparison
         *
         *
         * @return
         */
        @JvmOverloads fun lessThanOrEqualTo(inclusiveUpperBound: Double, delta: Double = 0.0): AlchemyAssertion<Double>
        {
            return AlchemyAssertion { number ->
                Assertions.notNull<Any>().check(number)

                val isWithinBounds = number!! - abs(delta) <= inclusiveUpperBound
                if (!isWithinBounds)
                {
                    throw FailedAssertionException("Number must be <= $inclusiveUpperBound +- $delta")
                }
            }
        }

        /**
         * Asserts that a Long is positive, or `> 0`

         * @return
         */

        fun positiveLong(): AlchemyAssertion<Long>
        {
            return AlchemyAssertion { number: Long ->
                Assertions.notNull<Any>().check(number)
                if (number <= 0)
                {
                    throw FailedAssertionException("Expected positive long: " + number)
                }
            }
        }

        /**
         * Asserts that a Long is negative, or `< 0`.

         * @return
         */

        fun negativeLong(): AlchemyAssertion<Long>
        {
            return lessThan(0L)
        }

        /**
         * Asserts than an integer is `<` the supplied value.

         * @param exclusiveUpperBound The argument must be `< exclusiveUpperBound`.
         *
         *
         * @return
         */

        fun lessThan(exclusiveUpperBound: Int): AlchemyAssertion<Int>
        {
            Checks.Internal.checkThat(exclusiveUpperBound != Integer.MIN_VALUE, "Ints cannot be less than " + Integer.MIN_VALUE)
            return AlchemyAssertion { number: Int ->
                Assertions.notNull<Any>().check(number)

                val isWithinBounds = number < exclusiveUpperBound
                if (!isWithinBounds)
                {
                    throw FailedAssertionException("Number must be < " + exclusiveUpperBound)
                }
            }
        }

        /**
         * Asserts than a long is `<` the supplied value.

         * @param exclusiveUpperBound The argument must be `< exclusiveUpperBound`.
         *
         *
         * @return
         */

        fun lessThan(exclusiveUpperBound: Long): AlchemyAssertion<Long>
        {
            Checks.Internal.checkThat(exclusiveUpperBound != java.lang.Long.MIN_VALUE, "Longs cannot be less than " + java.lang.Long.MIN_VALUE)
            return AlchemyAssertion { number: Long ->
                Assertions.notNull<Any>().check(number)

                val isWithinBounds = number < exclusiveUpperBound
                if (!isWithinBounds)
                {
                    throw FailedAssertionException("Number must be < " + exclusiveUpperBound)
                }
            }
        }

        /**
         * Asserts that a double is `<` the supplied value, within `delta` margin-of-error.

         * @param exclusiveUpperBound The argument must be `< exclusiveUpperBound`.
         *
         * @param delta The allowable margin-of-error.
         *
         *
         * @return
         */
        @JvmOverloads fun lessThan(exclusiveUpperBound: Double, delta: Double = 0.0): AlchemyAssertion<Double>
        {
            Checks.Internal.checkThat(exclusiveUpperBound > -java.lang.Double.MAX_VALUE, "Doubles cannot be less than " + -java.lang.Double.MAX_VALUE)

            return AlchemyAssertion { number: Double ->
                Assertions.notNull<Any>().check(number)

                val isWithinBounds = number - abs(delta) < exclusiveUpperBound
                if (!isWithinBounds)
                {
                    throw FailedAssertionException("Number must be < " + exclusiveUpperBound)
                }
            }
        }

        /**
         * Asserts that an integer argument is in the specified (inclusive) range.

         * @param min The lower bound for the range, inclusive
         *
         * @param max The upper bound for the range, inclusive
         *
         *
         * @return
         *
         *
         * @throws IllegalArgumentException If `min >= max`. `min` should always be less than `max`.
         */
        @Throws(IllegalArgumentException::class)

        fun numberBetween(min: Int, max: Int): AlchemyAssertion<Int>
        {
            Checks.Internal.checkThat(min < max, "Minimum must be less than Max.")
            return AlchemyAssertion { number: Int ->
                Assertions.notNull<Any>().check(number)

                val isWithinRange = number >= min && number <= max

                if (!isWithinRange)
                {
                    val message = String.format("Expected a number between %d and %d but got %d instead", min, max, number)
                    throw FailedAssertionException(message)
                }
            }
        }

        /**
         * Asserts that a long argument is in the specified (inclusive) range.

         * @param min The lower bound for the range, inclusive
         *
         * @param max The upper bound for the range, inclusive
         *
         *
         * @return
         *
         *
         * @throws IllegalArgumentException If `min >= max`. `min` should always be less
         */
        @Throws(IllegalArgumentException::class)

        fun numberBetween(min: Long, max: Long): AlchemyAssertion<Long>
        {
            Checks.Internal.checkThat(min < max, "Minimum must be less than Max.")
            return AlchemyAssertion { number: Long ->
                Assertions.notNull<Any>().check(number)

                val isWithinRange = number >= min && number <= max
                if (!isWithinRange)
                {
                    val message = String.format("Expected a number between %d and %d but got %d instead", min, max, number)
                    throw FailedAssertionException(message)
                }
            }
        }
    }

}
/**
 * Asserts that a double is `> exclusiveLowerBound`, with no allowable margin of error.

 * @param exclusiveLowerBound The argument is expected to be `>` this value.
 *
 *
 * @return
 */
/**
 * Asserts that a double is `>= inclusiveLowerBound` with no allowable margin of error.

 * @param inclusiveLowerBound The argument double must be `>= inclusiveLowerBound`
 *
 *
 * @return
 */
/**
 * Asserts that a double is `<=` the supplied value, with no allowable margin-of-error.

 * @param inclusiveUpperBound The argument must be `<= inclusiveUpperBound`.
 *
 *
 * @return
 */
/**
 * Asserts that a double is `<` the supplied value, with no allowable margin-of-error.

 * @param exclusiveUpperBound The argument must be `< exclusiveUpperBound`
 *
 *
 * @return
 */
