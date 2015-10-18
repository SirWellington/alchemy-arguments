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
package sir.wellington.alchemy.arguments;

import tech.sirwellington.alchemy.annotations.arguments.NonNull;
import tech.sirwellington.alchemy.annotations.arguments.Nullable;
import tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern;
import static tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern.Role.INTERFACE;

/**
 * {@linkplain AlchemyAssertion Alchemy Assertions} analyze arguments for validity. You can always
 * supply your own assertions to make for powerful custom argument checks.
 *
 * @param <A> The type of argument an assertion checks
 *
 * @author SirWellington
 */
@FunctionalInterface
@StrategyPattern(role = INTERFACE)
public interface AlchemyAssertion<A>
{

    /**
     * Asserts that the argument for validity.
     *
     * @param argument The argument to validate
     *
     * @throws FailedAssertionException When the argument-check fails. Note that
     *                                  {@link FailedAssertionException} already extends
     *                                  {@link IllegalArgumentException}. Any other kinds of
     *                                  exceptions thrown will be wrapped in a
     *                                  {@link FailedAssertionException}.
     */
    void check(@Nullable A argument) throws FailedAssertionException;

    /**
     * Allows combinations of multiple {@linkplain AlchemyAssertion assertions} in one.
     *
     * For example, a {@code validAge} assertion could be constructed dynamically using:
     * <pre>
     *
     * {@code
     * AlchemyAssertion<Integer> validAge = positiveInteger()
     *      .and(greaterThanOrEqualTo(1))
     *      .and(lessThanOrEqualTo(120))
     *
     * checkThat(age)
     * .is(validAge);
     * }
     *
     * </pre>
     *
     * This allows you to save and store Assertions that are commonly used together to perform
     * argument checks, and to do so at runtime.
     *
     *
     * @param other
     *
     * @return
     */
    @NonNull
    default AlchemyAssertion<A> and(@NonNull AlchemyAssertion<A> other) throws IllegalArgumentException
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
     *
     * For example, a {@code validAge} assertion could be constructed dynamically using:
     * <pre>
     *
     * {@code
     *   AlchemyAssertion<Integer> validAge = combine
     * (
     *      notNull(),
     *      greaterThanOrEqualTo(1),
     *      lessThanOrEqualTo(120),
     *      positiveInteger()
     * );
     *
     * checkThat(age)
     *      .is(validAge);
     * }
     *
     * </pre>
     *
     * This allows you to combine and store Assertions that are commonly used together to perform
     * argument checks.
     *
     * @param first
     *
     * @param other
     *
     * @param <T>
     *
     * @return
     */
    static <T> AlchemyAssertion<T> combine(@NonNull AlchemyAssertion<T> first, AlchemyAssertion<T>... other)
    {
        Checks.checkNotNull(first, "first Assertion cannot be null");

        return (argument) ->
        {
            first.check(argument);

            for (AlchemyAssertion<T> assertion : other)
            {
                assertion.check(argument);
            }
        };
    }

}
