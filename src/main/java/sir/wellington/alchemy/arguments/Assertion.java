/*
 * Copyright 2015 SirWellington.
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

import com.google.common.base.Preconditions;
import sir.wellington.alchemy.annotations.patterns.StrategyPattern;
import static sir.wellington.alchemy.annotations.patterns.StrategyPattern.Role.INTERFACE;

/**
 * Assertions analyze input arguments for validity. You can always supply your own.
 *
 * @param <A> The type of argument an assertion checks
 *
 * @author SirWellington
 */
@FunctionalInterface
@StrategyPattern(role = INTERFACE)
public interface Assertion<A>
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
    void check(A argument) throws FailedAssertionException;

    /**
     * Allows you to create a single {@link Assertion} that is composed of multiple Assertions.
     *
     * For example, a {@code validAge} assertion could be constructed dynamically using:
     *
     * <pre>
     *
     * {@code
     *  Assertion<Integer> validAge = Assertion.multipleAssertions(
     *     nonNull(),
     *     greaterThanOrEqualTo(1),
     *     lessThanOrEqualTo(120)
     *  );
     *
     * checkThat(age)
     *      .is(validAge);
     * }
     *
     * </pre>
     *
     * This allows you to save and store Assertions that are commonly used together to perform
     * argument checks, and to do so at runtime.
     *
     * @param <T>
     *
     * @param assertions
     *
     * @return
     */
    static <T> Assertion<T> multipleAssertions(Assertion<T>... assertions)
    {
        Preconditions.checkArgument(assertions != null, "no assertions");
        Preconditions.checkArgument(assertions.length > 0, "no assertions");

        return (argument) ->
        {
            for (Assertion<T> assertion : assertions)
            {
                assertion.check(argument);
            }
        };
    }

}
