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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tech.sirwellington.alchemy.test.AlchemyTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tech.sirwellington.alchemy.arguments.TestHelpers.assertThrowsFailedAssertion;
import static tech.sirwellington.alchemy.arguments.assertions.NetworkAssertions.validPort;
import static tech.sirwellington.alchemy.arguments.assertions.NetworkAssertions.validURL;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.NetworkGenerators.httpsURLs;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.negativeIntegers;
import static tech.sirwellington.alchemy.generator.StringGenerators.strings;

/**
 * Tests for {@link NetworkAssertions}.
 *
 * @author SirWellington
 */
@DisplayName("NetworkAssertions Tests")
@AlchemyTest
final class NetworkAssertionsTest {

    private static final int MAX_PORT = 65535;

    //==============================
    // URL VALIDATION
    //==============================

    @Test
    @DisplayName("testValidURL: validURL assertion works correctly")
    void testValidURL() {
        var url = one(httpsURLs());
        String urlString = url.toString();

        var assertion = validURL();
        assertNotNull(assertion);

        // Valid cases
        assertion.check(urlString);

        // Invalid case
        assertThrowsFailedAssertion(() -> assertion.check(one(strings())));
    }

    @Test
    @DisplayName("testValidPort: validPort assertion works correctly")
    void testValidPort() {
        var port = one(integers(1, MAX_PORT));
        var assertion = validPort();

        assertNotNull(assertion);

        // Valid cases
        assertion.check(port);
        assertion.check(1);
        assertion.check(MAX_PORT);

        // Invalid cases: negative and out-of-range
        int negative = one(negativeIntegers());
        assertThrowsFailedAssertion(() -> assertion.check(negative));

        int tooHigh = one(integers(MAX_PORT + 1, Integer.MAX_VALUE));
        assertThrowsFailedAssertion(() -> assertion.check(tooHigh));
    }

    @Test
    @DisplayName("testValidPortEdgeCases: validPort handles boundary values")
    void testValidPortEdgeCases() {
        var assertion = validPort();

        assertThrowsFailedAssertion(() -> assertion.check(0));
    }
}
