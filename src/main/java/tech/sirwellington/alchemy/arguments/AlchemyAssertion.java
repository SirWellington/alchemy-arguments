/*
 * Copyright © 2018. Sir Wellington.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
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
import tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern;

import static tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern.Role.INTERFACE;

/**
 * {@linkplain AlchemyAssertion Alchemy Assertions} analyze arguments for validity.
 * <p>
 * Creating your own {@link AlchemyAssertion} is as simple as implementing this functional interface, which can be done in a
 * lambda.
 * <p>
 * Alchemy Assertions do have a naming convention:
 * <p>
 * <pre>
 *
 * + Do <b>not</b> begin with "is". For example, isPosition(), isEmpty().
 * + Named after the condition that is expected to be true. For example, greaterThan(5), nonEmptyString().
 * + As long as necessary to allow for an English-legible translation. For example, lessThanOrEqualTo(40). Don't use abbreviations.
 *
 * </pre>
 *
 * @param <Argument> The type of argument an assertion checks
 * @author SirWellington
 */
@StrategyPattern(role = INTERFACE)
public interface AlchemyAssertion<Argument>
{

    /**
     * Asserts the validity of the argument.
     *
     * @param argument The argument to validate
     * @throws FailedAssertionException When the argument-check fails. Note that
     *                                  {@link FailedAssertionException} already extends
     *                                  {@link IllegalArgumentException}, allowing you to write a {@code catch} clause for
     *                                  {@code IllegalArgumentException}.
     */
    void check(@Optional Argument argument) throws FailedAssertionException;

}
