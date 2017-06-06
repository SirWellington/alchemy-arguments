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

import java.util.Date
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tech.sirwellington.alchemy.annotations.access.NonInstantiable
import tech.sirwellington.alchemy.annotations.arguments.NonNull
import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.FailedAssertionException

import java.lang.String.format
import tech.sirwellington.alchemy.arguments.Checks.Internal.checkNotNull

/**

 * @author SirWellington
 */
@NonInstantiable
class DateAssertions @Throws(IllegalAccessException::class)
internal constructor()
{

    init
    {
        throw IllegalAccessException("cannot instantiate")
    }

    companion object
    {

        private val LOG = LoggerFactory.getLogger(DateAssertions::class.java!!)

        @JvmStatic
        fun inThePast(): AlchemyAssertion<Date>
        {
            return AlchemyAssertion { argument ->
                //Recalculate now each time we are called
                val present = Date()
                //Check that argument is before present
                if (!argument.before(present))
                {
                    throw FailedAssertionException(format("Expected Date %s to be in the past", argument))
                }
            }
        }

        @JvmStatic
        fun before(@NonNull expected: Date): AlchemyAssertion<Date>
        {
            checkNotNull(expected, "date cannot be null")

            return AlchemyAssertion { argument ->
                Assertions.notNull<Any>().check(argument)

                if (!argument.before(expected))
                {
                    throw FailedAssertionException(format("Expected Date to be before %s",
                                                          expected.toInstant().toString()))
                }
            }
        }

        @JvmStatic
        fun inTheFuture(): AlchemyAssertion<Date>
        {
            return AlchemyAssertion { argument ->
                //Now must stay current
                val present = Date()

                //Check that argument is after present
                if (!argument.after(present))
                {
                    throw FailedAssertionException(format("Expected Date to be in the future", argument))
                }
            }
        }

        @JvmStatic
        fun after(@NonNull expected: Date): AlchemyAssertion<Date>
        {
            checkNotNull(expected, "date cannot be null")

            return AlchemyAssertion { date ->
                Assertions.notNull<Any>().check(date)

                if (!date.after(expected))
                {
                    throw FailedAssertionException(format("Expected Date to be after %s", expected.toInstant().toString()))
                }
            }
        }
    }

}
