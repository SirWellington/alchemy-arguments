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
import static tech.sirwellington.alchemy.arguments.assertions.GeolocationAssertions.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.doubles;

/**
 * Tests for {@link GeolocationAssertions}.
 *
 * @author SirWellington
 */
@DisplayName("GeolocationAssertions Tests")
@AlchemyTest
final class GeolocationAssertionsTest {

    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;

    //==============================
    // LATITUDE VALIDATION
    //==============================

    @Test
    @DisplayName("testValidLatitude: validLatitude assertion works correctly")
    void testValidLatitude() {
        var latitude = one(doubles(MIN_LATITUDE, MAX_LATITUDE));
        var assertion = validLatitude();

        assertNotNull(assertion);
        assertion.check(latitude);

        // Boundary cases
        assertion.check(MIN_LATITUDE);
        assertion.check(MAX_LATITUDE);
    }

    @Test
    @DisplayName("testValidLatitudeWithInvalid: validLatitude rejects out-of-range latitudes")
    void testValidLatitudeWithInvalid() {
        var assertion = GeolocationAssertions.validLatitude();

        var tooHigh = one(doubles(MAX_LATITUDE + 0.1, Double.MAX_VALUE));
        assertThrowsFailedAssertion(() -> assertion.check(tooHigh));

        var tooLow = one(doubles(Double.MIN_VALUE, MIN_LATITUDE - 0.1));
        assertThrowsFailedAssertion(() -> assertion.check(tooLow));
    }

    //==============================
    // LONGITUDE VALIDATION
    //==============================

    @Test
    @DisplayName("testValidLongitude: validLongitude assertion works correctly")
    void testValidLongitude() {
        var longitude = one(doubles(MIN_LONGITUDE, MAX_LONGITUDE));
        var assertion = validLongitude();

        assertNotNull(assertion);
        assertion.check(longitude);

        // Boundary cases
        assertion.check(MIN_LONGITUDE);
        assertion.check(MAX_LONGITUDE);
    }

    @Test
    @DisplayName("testValidLongitudeWithInvalid: validLongitude rejects out-of-range longitudes")
    void testValidLongitudeWithInvalid() {
        var assertion = validLongitude();

        var tooHigh = one(doubles(MAX_LONGITUDE + 0.1, Double.MAX_VALUE));
        assertThrowsFailedAssertion(() -> assertion.check(tooHigh));

        var tooLow = one(doubles(Double.MIN_VALUE, MIN_LONGITUDE - 0.1));
        assertThrowsFailedAssertion(() -> assertion.check(tooLow));
    }
}
