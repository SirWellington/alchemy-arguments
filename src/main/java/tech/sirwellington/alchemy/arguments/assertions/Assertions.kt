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
import tech.sirwellington.alchemy.annotations.arguments.Optional
import tech.sirwellington.alchemy.annotations.arguments.Required
import tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern
import tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern.Role.CONCRETE_BEHAVIOR
import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.Checks
import tech.sirwellington.alchemy.arguments.FailedAssertionException

/**
 * Common [Alchemy Assertions][AlchemyAssertion].

 * @author SirWellington
 */
@StrategyPattern(role = CONCRETE_BEHAVIOR)
@NonInstantiable
class Assertions @Throws(IllegalAccessException::class)
internal constructor()
{

    init
    {
        throw IllegalAccessException("cannot instantiate class")
    }

    companion object
    {

        /**
         * Asserts that the argument is not null.

         * @param <A>
         *
         *
         * @return
         *
         * @see .nullObject
        </A> */
        @JvmStatic
        fun <A> notNull(): AlchemyAssertion<A>
        {
            return AlchemyAssertion { reference ->
                if (reference == null)
                {
                    throw FailedAssertionException("Argument is null")
                }
            }
        }

        /**
         * Asserts that the argument is null.
         * This is the opposite of [.notNull].

         * @param <A>
         *
         * @return
         *
         * @see .notNull
        </A>
         */
        @JvmStatic
        fun <A> nullObject(): AlchemyAssertion<A>
        {
            return AlchemyAssertion { reference ->

                if (reference != null)
                {
                    throw FailedAssertionException("Argument is not null: $reference")
                }
            }
        }

        /**
         * Asserts that the argument is the same instance as `other`.

         * @param <A>
         *
         * @param other
         *
         *
         * @return
        </A> */
        @JvmStatic
        fun <A> sameInstanceAs(@Optional other: Any?): AlchemyAssertion<A>
        {
            return AlchemyAssertion block@ { argument ->

                if (argument == null && other == null)
                {
                    return@block
                }

                if (argument !== other)
                {
                    throw FailedAssertionException("Expected $argument to be the same instance as $other")
                }
            }
        }

        /**
         * Asserts that an argument is an `instanceOf` the specified class. This Assertion respects the inheritance
         * hierarchy, so
         * <pre>
         * Integer instanceOf Object
         * Integer instanceOf Number
         * Integer instanceOf Integer
         * </pre>
         *
         * will pass, but
         *
         * <pre>
         * Integer instanceOf Double
         * Integer instanceOf String
         * </pre>
         *
         * will fail.

         * @param <A>
         *
         * @param classOfExpectedType
         *
         * @return
        </A> */
        @JvmStatic
        fun <A> instanceOf(classOfExpectedType: Class<*>): AlchemyAssertion<A>
        {
            Checks.Internal.checkNotNull(classOfExpectedType, "class cannot be null")

            return AlchemyAssertion { argument ->

                notNull<Any>().check(argument)

                if (!classOfExpectedType.isInstance(argument))
                {
                    throw FailedAssertionException("Expected Object of type: $classOfExpectedType")
                }
            }
        }

        /**
         * Asserts that the argument is [equal to][Object.equals] `other`.

         * @param <A>
         *
         * @param other
         *
         *
         * @return
        </A> */
        @JvmStatic
        fun <A> equalTo(@Optional other: A): AlchemyAssertion<A>
        {
            return AlchemyAssertion { argument ->

                if (argument != other)
                {
                    throw FailedAssertionException("Expected $argument to be equal to $other")
                }
            }
        }

        /**
         * Runs the inverse on another [AlchemyAssertion]. This allows you to create expressions such as:

         * <pre>
         * `checkThat(filename)
         * .is(not( stringWithWhitespace() ))
         * .is(not( equalTo("info.txt") ));
        ` *
        </pre> *

         * @param <A>
         *
         * @param assertion
         *
         *
         * @return
        </A> */
        @JvmStatic
        fun <A> not(@Required assertion: AlchemyAssertion<A>): AlchemyAssertion<A>
        {
            Checks.Internal.checkNotNull(assertion, "missing assertion")

            return AlchemyAssertion block@ { argument ->

                try
                {
                    assertion.check(argument)
                }
                catch (ex: FailedAssertionException)
                {
                    return@block
                }

                throw FailedAssertionException("Expected assertion to fail, but it passed: $assertion")

            }
        }
    }

}
