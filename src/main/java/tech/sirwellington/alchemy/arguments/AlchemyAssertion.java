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
package tech.sirwellington.alchemy.arguments;

import tech.sirwellington.alchemy.annotations.arguments.Optional;
import tech.sirwellington.alchemy.annotations.arguments.Required;
import tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern;

import static tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern.Role.INTERFACE;

/**
 * {@linkplain AlchemyAssertion Alchemy Assertions} analyze arguments for validity. 
 * <p>
 * Creating your own {@link AlchemyAssertion} is as simple as implementing this functional interface, which can be done in a
 * lambda.
 * <p>
 * Alchemy Assertions do have a naming convention:
 * 
 * <pre>
 * 
 * + Do <b>not</b> begin with "is". For example, isPosition(), isEmpty().
 * + Named after the condition that is expected to be true. For example, greaterThan(5), nonEmptyString().
 * + As long as necessary to allow for an English-legible translation. For example, lessThanOrEqualTo(40). Don't use abbreviations.
 * 
 * </pre>
 * 
 * @param <A> The type of argument an assertion checks
 *
 * @author SirWellington
 */
@StrategyPattern(role = INTERFACE)
public interface AlchemyAssertion<A>
{

    /**
     * Asserts the validity of the argument.
     *
     * @param argument The argument to validate
     *
     * @throws FailedAssertionException When the argument-check fails. Note that
     *                                  {@link FailedAssertionException} already extends
     *                                  {@link IllegalArgumentException}, allowing you to write a {@code catch} clause for
     *                                  {@code IllegalArgumentException}.
     */
    void check(@Optional A argument) throws FailedAssertionException;

    /**
     * Chains two {@linkplain AlchemyAssertion assertions} together.
     *<p>
     * For example, a {@code validAge} assertion could be constructed dynamically using:
     * <pre>
     *
     * {@code
     * AlchemyAssertion<Integer> validAge = positiveInteger()
     *      .and(greaterThanOrEqualTo(1))
     *      .and(lessThanOrEqualTo(120))
     *
     *  checkThat(age)
     *      .is(validAge);
     * }
     *
     * </pre>
     *
     * This allows you to save and store Assertions that are commonly used together to perform
     * argument checks, and to do so at runtime.
     * <p>
     * Note that due to limitations of the Java Compiler, the first
     * {@linkplain AlchemyAssertion Assertion} that you make must match the type of the argument.
     * <p>
     * For example,
     * <pre>
     * {@code
     *  notNull()
     *      .and(positiveInteger())
     *      .check(age);
     * }
     * </pre>
     * would not work because notNull references a vanilla {@code Object}.
     * <p>
     * The {@linkplain #combine(tech.sirwellington.alchemy.arguments.AlchemyAssertion, tech.sirwellington.alchemy.arguments.AlchemyAssertion...) combine} 
     * function does not have these limitations.
     *
     * @param other
     *
     * @return
     * 
     * @see #combine(tech.sirwellington.alchemy.arguments.AlchemyAssertion, tech.sirwellington.alchemy.arguments.AlchemyAssertion...)  
     * @see Arguments#checkThat(java.lang.Object) 
     */
    @Required
    default AlchemyAssertion<A> and(@Required AlchemyAssertion<A> other) throws IllegalArgumentException
    {
        Checks.checkNotNull(other, "assertion cannot be null");

        return argument ->
        {
            this.check(argument);
            other.check(argument);
        };
    }

    /**
     * Combines multiple {@linkplain AlchemyAssertion assertions} into one.
     * <p>
     * For example, a {@code validAge} assertion could be constructed dynamically using:
     * <pre>
     *
     * {@code
     *   AlchemyAssertion<Integer> validAge = combine
     *   (
     *      notNull(),
     *      greaterThanOrEqualTo(1),
     *      lessThanOrEqualTo(120),
     *      positiveInteger()
     *   );
     *
     * checkThat(age)
     *      .is(validAge);
     * }
     *
     * </pre>
     *
     * This allows you to <b>combine and store</b> Assertions that are commonly used together to perform
     * argument checks.
     *
     * @param first
     * @param other
     * @param <T>
     *
     * @return
     * 
     * @see #and(tech.sirwellington.alchemy.arguments.AlchemyAssertion) 
     */
    static <T> AlchemyAssertion<T> combine(@Required AlchemyAssertion<T> first, AlchemyAssertion<T>... other)
    {
        Checks.checkNotNull(first, "the first AlchemyAssertion cannot be null");
        Checks.checkNotNull(other, "null varargs");

        return (argument) ->
        {
            first.check(argument);
            
            for (AlchemyAssertion<T> assertion : other)
            {
                if (assertion != null)
                {
                    assertion.check(argument);
                }
            }
        };
    }

}
