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

import tech.sirwellington.alchemy.arguments.AlchemyAssertion;

import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.StringAssertions.*;


/**
 * A Library assertion intended to check the validity of address components.
 *
 * <p>You will find checks for:
 * <ul>
 *   <li>Zip Codes</li>
 *   <li>States</li>
 *   <li>Countries</li>
 * </ul></p>
 *
 * @author SirWellington
 */
public final class AddressAssertions {

    private AddressAssertions() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class should not be instantiated.");
    }

    /**
     * Checks that a string can represent a valid zip code.
     * <p>Appropriately, a Zip Code does not necessarily have to consist only of digits.</p>
     *
     * @return an {@link AlchemyAssertion} for ZIP codes (4–5 characters long)
     */
    public static AlchemyAssertion<String> validZipCode() {
        return zip -> checkThat(zip)
                            .usingMessage("zip must consist of 4-5 characters")
                            .isA(stringWithLengthGreaterThanOrEqualTo(4))
                            .isA(stringWithLengthLessThanOrEqualTo(5));
    }

    /**
     * Checks that a ZIP code string is:
     * <ul>
     *   <li>Not null</li>
     *   <li>Represents an Integer number (e.g., {@code "90012"})</li>
     *   <li>Exactly 5 digits long (e.g., {@code "01693"})</li>
     *   <li>In the valid numeric range [00000, 99999]</li>
     * </ul>
     *
     * @return an {@link AlchemyAssertion} for strict ZIP code strings
     */
    public static AlchemyAssertion<String> validZipCodeString() {
        return zip -> {
            nonEmptyString().check(zip);
            integerString().check(zip);
            stringWithLength(5);
            validZipCode().check(zip);
        };
    }
}
