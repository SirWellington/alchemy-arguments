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
import tech.sirwellington.alchemy.annotations.arguments.NonNull
import tech.sirwellington.alchemy.annotations.arguments.Required
import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.Checks.Internal.checkNotNull
import tech.sirwellington.alchemy.arguments.Checks.Internal.checkThat
import tech.sirwellington.alchemy.arguments.FailedAssertionException
import java.lang.String.format
import java.time.Instant

/**

 * @author SirWellington
 */
@NonInstantiable
class TimeAssertions @Throws(IllegalAccessException::class)
internal constructor()
{

    init
    {
        throw IllegalAccessException("cannot instantiate")
    }

    companion object
    {

        private val LOG = LoggerFactory.getLogger(TimeAssertions::class.java!!)

        /**
         * Asserts that the [Instant] is in the past. Note that the present is constantly recalculated in order to
         * stay current.

         * @return
         */

        fun inThePast(): AlchemyAssertion<Instant>
        {
            return AlchemyAssertion { argument ->
                //Recalculate the present on each call to stay current
                val present = Instant.now()
                if (!argument.isBefore(present))
                {
                    throw FailedAssertionException(format("Expected Timestamp %s to be in the past. Now: %s", argument, present))
                }
            }
        }


        fun before(@NonNull expected: Instant): AlchemyAssertion<Instant>
        {
            checkNotNull(expected, "time cannot be null")

            return AlchemyAssertion { argument ->
                Assertions.notNull<Any>().check(argument)

                if (!argument.isBefore(expected))
                {
                    throw FailedAssertionException(format("Expected Timestamp to be before %s", expected.toString()))
                }
            }
        }


        fun inTheFuture(): AlchemyAssertion<Instant>
        {
            return AlchemyAssertion { argument ->
                //Recalculate the present on each call to stay current
                val present = Instant.now()
                if (!argument.isAfter(present))
                {
                    throw FailedAssertionException(format("Expected Timestamp %s to be in the future. Now: %s", argument, present))
                }
            }
        }


        fun after(@NonNull expected: Instant): AlchemyAssertion<Instant>
        {
            checkNotNull(expected, "time cannot be null")

            return AlchemyAssertion { argument ->
                Assertions.notNull<Any>().check(argument)

                if (!argument.isAfter(expected))
                {
                    throw FailedAssertionException(format("Expected Timestamp to be after %s", expected.toString()))
                }
            }
        }

        /**
         * Ensures that the [Instant] represents exactly Right now, at the time of checking. It does so
         * within a margin-of-error of 5 milliseconds. This should be acceptable for most modern processors.
         * Use [.nowWithinDelta] for more fine-grained deltas.

         * @return
         *
         * @see .nowWithinDelta
         */

        fun rightNow(): AlchemyAssertion<Instant>
        {
            return nowWithinDelta(5L)
        }

        /**
         * Ensures that an [Instant] is [Instant.now], within the specified margin of error.

         * @param marginOfErrorInMillis The Acceptable Margin-Of-Error, in Milliseconds. The instant must be within this delta.
         *
         *
         * @return
         *
         * @throws IllegalArgumentException If the marginOfError is `< 0`.
         *
         *
         * @see .rightNow
         */
        @Throws(IllegalArgumentException::class)

        fun nowWithinDelta(marginOfErrorInMillis: Long): AlchemyAssertion<Instant>
        {
            checkThat(marginOfErrorInMillis >= 0, "millis must be non-negative.")

            return AlchemyAssertion { instant ->
                val now = Instant.now().toEpochMilli()
                notNull<Any>().check(instant)

                val epoch = instant.toEpochMilli()
                val difference = Math.abs(epoch - now)

                if (difference > marginOfErrorInMillis)
                {
                    throw FailedAssertionException(
                            "Time difference of " + difference + " exceeded margin-of-error of " + marginOfErrorInMillis + "ms")
                }

            }
        }

        /**
         * Asserts that the [Instant] is equal to another, within the acceptable boundaries.

         * @param instant The instant to compare against
         *
         * @param deltaMillis The acceptable delta, in milliseconds.
         *
         * @return
         */

        fun equalToInstantWithinDelta(@Required instant: Instant, deltaMillis: Long): AlchemyAssertion<Instant>
        {
            checkNotNull(instant, "instant cannot be null")
            val delta = Math.abs(deltaMillis)

            return AlchemyAssertion { argument ->
                notNull<Any>().check(argument)

                var difference = argument.toEpochMilli() - instant.toEpochMilli()
                difference = Math.abs(difference)

                if (difference > delta)
                {
                    throw FailedAssertionException("Delta should not exceed " + delta + "ms, but is " + difference + "ms")
                }
            }
        }

        /**
         * Epoch version of [.rightNow].

         * @return
         */

        fun epochRightNow(): AlchemyAssertion<Long>
        {
            return epochNowWithinDelta(5L)
        }

        /**
         * Epoch version of [.nowWithinDelta].

         * @param marginOfErrorInMillis The Acceptable Margin-Of-Error, in Milliseconds. The instant must be within this delta.
         *
         *
         * @return
         *
         * @throws IllegalArgumentException If the marginOfError is `< 0`.
         */
        @Throws(IllegalArgumentException::class)

        fun epochNowWithinDelta(marginOfErrorInMillis: Long): AlchemyAssertion<Long>
        {
            checkThat(marginOfErrorInMillis >= 0, "millis must be non-negative.")

            return AlchemyAssertion { epoch ->
                val now = Instant.now().toEpochMilli()
                greaterThan(0L).check(epoch)

                val difference = Math.abs(epoch!! - now)

                if (difference > marginOfErrorInMillis)
                {
                    throw FailedAssertionException(
                            "Time difference of " + difference + " exceeded margin-of-error of " + marginOfErrorInMillis + "ms")
                }

            }
        }
    }

}
