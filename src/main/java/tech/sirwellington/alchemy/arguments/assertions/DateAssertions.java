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
import tech.sirwellington.alchemy.annotations.arguments.Required;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;

import java.util.Date;

import static java.text.MessageFormat.format;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;
import static tech.sirwellington.alchemy.arguments.internal.Checks.checkNotNull;
import static tech.sirwellington.alchemy.arguments.internal.Checks.failAssertion;

/**
 * Assertions for {@link Date Dates}.
 *
 * @author SirWellington
 */
@NonInstantiable
public final class DateAssertions {

    private DateAssertions() throws IllegalAccessException {
        throw new IllegalAccessException("cannot directly instantiate");
    }

    /**
     * Asserts that the argument date is before the specified date.
     * @param expected The date to check against.
     * @return A chainable assertion.
     */
    public static AlchemyAssertion<Date> before(@Required Date expected) {
        checkNotNull(expected, "date cannot be null");

        return date -> {
            notNull().check(date);
            if (!date.before(expected)) {
                failAssertion("Expected date to be before [{0}] but was [{1}]", expected, date);
            }
        };
    }

    /**
     * Asserts that the argument date is after the specified date.
     * @param expected The date to check against.
     * @return A chainable assertion.
     */
    public static AlchemyAssertion<Date> after(@Required Date expected) {
        checkNotNull(expected, "date cannot be null");

        return date -> {
            notNull().check(date);
            if (!date.after(expected)) {
                failAssertion("Expected date to be after [{0}] but was [{1}]", expected, date);
            }
        };
    }

    /**
     * Asserts that the argument date is in the past.
     * @return A chainable assertion.
     */
    public static AlchemyAssertion<Date> inThePast() {
        return date -> {
            // Recalculate "now" each time we are called
            var present = new Date();
            if (!date.before(present)) {
                throw new FailedAssertionException(
                    format("Expected Date {0} to be in the past", date)
                );
            }
        };
    }

    /**
     * Asserts that the argument date is in the future.
     * @return A chainable assertion.
     */
    public static AlchemyAssertion<Date> inTheFuture() {
        return date -> {
            var present = new Date();
            if (!date.after(present)) {
                failAssertion("Expected Date to be in the future: [{0}]", date);
            }
        };
    }

}
