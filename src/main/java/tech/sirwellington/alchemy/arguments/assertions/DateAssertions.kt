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

import tech.sirwellington.alchemy.annotations.access.NonInstantiable
import tech.sirwellington.alchemy.annotations.arguments.Required
import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.FailedAssertionException
import tech.sirwellington.alchemy.arguments.checkNotNull
import java.util.Date

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

        fun inThePast(): AlchemyAssertion<Date>
        {
            return AlchemyAssertion { date ->
                //Recalculate now each time we are called
                val present = Date()
                //Check that argument is before present
                if (!date.before(present))
                {
                    throw FailedAssertionException("Expected Date [$date] to be in the past")
                }
            }
        }


        fun before(@Required expected: Date): AlchemyAssertion<Date>
        {
            checkNotNull(expected, "date cannot be null")

            return AlchemyAssertion { date ->

                notNull<Any>().check(date)

                if (!date.before(expected))
                {
                    throw FailedAssertionException("Expected Date to be before $expected")
                }
            }
        }


        fun inTheFuture(): AlchemyAssertion<Date>
        {
            return AlchemyAssertion { date ->
                //Now must stay current
                val present = Date()

                //Check that argument is after present
                if (!date.after(present))
                {
                    throw FailedAssertionException("Expected Date [$date] to be in the future")
                }
            }
        }


        fun after(@Required expected: Date): AlchemyAssertion<Date>
        {
            checkNotNull(expected, "date cannot be null")

            return AlchemyAssertion { date ->

                notNull<Any>().check(date)

                if (!date.after(expected))
                {
                    throw FailedAssertionException("Expected Date [$date] to be after [$expected]")
                }
            }
        }
    }

}
