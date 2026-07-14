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

import java.util.regex.Pattern;

import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;

import static tech.sirwellington.alchemy.arguments.internal.Checks.failAssertion;
import static tech.sirwellington.alchemy.arguments.internal.Checks.isNullOrEmpty;

/**
 * Assertions made on Data about people.
 * + Emails
 * + Addresses
 * + Names
 * + Etc.
 *
 * @author SirWellington
 */
@NonInstantiable
public final class PeopleAssertions {

    private static Pattern PATTERN = Pattern.compile("^.+@.+\\..+$");

    private PeopleAssertions() throws IllegalAccessException {
        throw new IllegalAccessException("cannot directly instantiate");
    }

    /**
     * This Assertion performs basic validation of Emails
     * using the following pattern:
     * {@code "^.+@.+\\..+$"}. The intent of this Assertion is to keep it simple and
     * prevent flagrant violations. The only way to truly know whether an email is valid is to send
     * a message to it.
     *
     * @return A chainable assertion
     */
    public static AlchemyAssertion<String> validEmailAddress() {

        return email -> {

            if (isNullOrEmpty(email)) {
                failAssertion("Email is null or empty");
            }

            if (!PATTERN.matcher(email).matches()) {
                failAssertion("Invalid Email Address: " + PATTERN);
            }
        };
    }
}