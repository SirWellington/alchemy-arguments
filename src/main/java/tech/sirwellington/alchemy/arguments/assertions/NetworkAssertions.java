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

import java.net.URI;

import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;

import static tech.sirwellington.alchemy.arguments.assertions.StringAssertions.nonEmptyString;
import static tech.sirwellington.alchemy.arguments.internal.Checks.failAssertion;

/**

 * @author SirWellington
 */
@NonInstantiable
public final class NetworkAssertions {
    /**
     * The maximum allowable Port number
     */
    private static final int MAX_PORT = 65535;

    private NetworkAssertions() throws IllegalAccessException {
        throw new IllegalAccessException("cannot directly instantiate");
    }


    /**
     * Checks that a URL is valid, according to the [URL] class.
     * @return A chainable assertion.
     */

    public static AlchemyAssertion<String> validURL() {
        return string -> {
            nonEmptyString().check(string);
            try {
                var _ = new URI(string).toURL();
            } catch (Exception ex) {
                failAssertion("Invalid URL: {0} | [{1}]", string, ex);
            }
        };
    }

    /**
     * Asserts that the port number is valid and acceptable.
     * <strong>It does not check if the port is currently open.</strong>
     * @return A chainable assertion.
     * @see <a href ="https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers">Wikipedia</a>
     */
    public static AlchemyAssertion<Integer> validPort() {
        return port -> {
            if (port <= 0) {
                failAssertion("Network port must be > 0, but was [{0}]", port);
            }

            if (port > MAX_PORT) {
                failAssertion("Network port must be less than [{0}], but was [{1}]", MAX_PORT, port);
            }
        };
    }
}