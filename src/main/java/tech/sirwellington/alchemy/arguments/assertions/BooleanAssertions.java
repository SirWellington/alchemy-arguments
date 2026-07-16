/*
 * Copyright © 2026. Sir Wellington.
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
package tech.sirwellington.alchemy.arguments.assertions;

import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;

import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;
import static tech.sirwellington.alchemy.arguments.internal.Checks.failAssertion;

/**
 * Assertions when dealing with booleans.
 * <br>
 * {@snippet :
 * var user = getUser(request.userId);
 * checkThat(user.hasMembership())
 *   .throwing(UnauthorizedException.class)
 *   .usingMessage("customer must have a membership to perform this request")
 *   .isA(trueStatement());
 * }
 * @author SirWellington
 */
@NonInstantiable
public final class BooleanAssertions {

    private BooleanAssertions() throws IllegalAccessException {
        throw new IllegalAccessException("cannot directly instantiate");
    }

    /**
     * Checks that the condition is {@code true}.
     * @return A chainable assertion.
     */
    public static AlchemyAssertion<Boolean> trueStatement() {
        return condition -> {
            notNull().check(condition);
            if (!condition) {
                failAssertion("Expecting condition to be true but was false");
            }
        };
    }

    /**
     * Checks that the condition is {@code false}.
     * @return A chainable assertion.
     */
    public static AlchemyAssertion<Boolean> falseStatement() {
        return condition -> {
            notNull().check(condition);
            if (condition) {
                failAssertion("Expecting condition to be false but was true");
            }
        };
    }
}