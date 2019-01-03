/*
 * Copyright © 2019. Sir Wellington.
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

@file:JvmName("Assertions")

package tech.sirwellington.alchemy.arguments.assertions

import tech.sirwellington.alchemy.annotations.arguments.Optional
import tech.sirwellington.alchemy.annotations.arguments.Required
import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.FailedAssertionException
import tech.sirwellington.alchemy.arguments.checkNotNull

/**
 * Common [Alchemy Assertions][AlchemyAssertion].

 * @author SirWellington
 */


/**
 * Asserts that the argument is not null.

 * @param <A>
 *
 *
 * @return
 *
 * @see .nullObject
</A> */
fun <A : Any?> notNull(): AlchemyAssertion<A>
{
    return AlchemyAssertion { reference ->
        if (reference == null)
        {
            throw FailedAssertionException("Argument is null")
        }
    }
}

fun <A : Any?> nonNullReference(): AlchemyAssertion<A>
{
    return notNull()
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

fun <A : Any?> nullObject(): AlchemyAssertion<A>
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

fun <A : Any?> sameInstanceAs(@Optional other: A): AlchemyAssertion<A>
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
 */

fun <A : Any?> instanceOf(classOfExpectedType: Class<*>): AlchemyAssertion<A>
{
    checkNotNull(classOfExpectedType, "class cannot be null")

    return AlchemyAssertion { argument ->

        notNull<Any>().check(argument)

        if (!classOfExpectedType.isInstance(argument))
        {
            throw FailedAssertionException("Expected Object of type: $classOfExpectedType")
        }
    }
}

inline fun <reified A : Any> instanceOf(): AlchemyAssertion<A>
{
    return instanceOf(A::class.java)
}


/**
 * Asserts that the argument is [equal to][Object.equals] `other`.

 * @param <A>
 *
 * @param other
 *
 *
 * @return
 */

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
 */
fun <A> not(@Required assertion: AlchemyAssertion<A>): AlchemyAssertion<A>
{
    checkNotNull(assertion, "missing assertion")

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

/**
 * Chains two [assertions][AlchemyAssertion] together.
 *
 * For example a `validAge` assertion could be constructed dynamically using:
 *
 * ```
 * AlchemyAssertion<Integer> validAge = positiveInteger()
 *                                      .and(greaterThanOrEqualTo(1))
 *                                      .and(lessThanOrEqualTo(120))l
 *
 * checkThat(age).isA(validAge);
 *
 * ```
 *
 * Note that due to the limitations of the type-inference in the Java Compiler, the first
 * [assertion][AlchemyAssertion] that you make must match the type of the argument.
 *
 * For example,
 * ```
 * notNull().and(positiveInteger())
 *      .check(age);
 * ```
 *
 * would not work because `notNull` references a vanilla `Object`.
 *
 * @param other The other assertion to check against
 *
 * @see combine
 * @see checkThat
 */
@Required
@Throws(IllegalArgumentException::class)
fun <A> AlchemyAssertion<A>.and(@Required other: AlchemyAssertion<A>): AlchemyAssertion<A>
{
    checkNotNull(other, "assertion cannot be null")

    return AlchemyAssertion { argument ->
        this.check(argument)
        other.check(argument)
    }
}


/**
 * Combines multiple [assertions][AlchemyAssertion] into one.
 *
 * For example, a `validAge` assertion could be constructed dynamically using:
 * ```
 * AlchemyAssertion<Integer> validAge = combine(notNull(),
 *                                              greaterThanOrEqualTo(1),
 *                                              lessThanOrEqualTo(120),
 *                                              positiveInteger());
 *
 * checkThat(age).is(validAge);
 * ```
 *
 * This allows you to **combine and store** [assertions][AlchemyAssertion] that are commonly
 * used together ot perform argument checks.
 *
 * @param first The first assertion to include.
 * @param others The rest of the assertions to include.
 *
 * @see .and
 */
fun <T> combine(@Required first: AlchemyAssertion<T>, vararg others: AlchemyAssertion<T>): AlchemyAssertion<T>
{
    checkNotNull(first, "the first AlchemyAssertion cannot be null")
    checkNotNull(others, "null varargs")

    return AlchemyAssertion { argument ->
        first.check(argument)

        for (assertion in others)
        {
            assertion.check(argument)
        }
    }
}