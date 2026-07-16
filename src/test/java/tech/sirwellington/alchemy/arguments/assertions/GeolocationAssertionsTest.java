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
import tech.sirwellington.alchemy.test.generation.GenerateDouble;

import static tech.sirwellington.alchemy.arguments.TestHelpers.assertThrowsFailedAssertion;
import static tech.sirwellington.alchemy.arguments.assertions.GeolocationAssertions.validLatitude;
import static tech.sirwellington.alchemy.arguments.assertions.GeolocationAssertions.validLongitude;
import static tech.sirwellington.alchemy.test.generation.GenerateDouble.Type.RANGE;

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

    @GenerateDouble(value = RANGE, min = MIN_LATITUDE, max = MAX_LATITUDE)
    private double latitude;
    @GenerateDouble(value = RANGE, min = MAX_LATITUDE + 1, max = Double.MAX_VALUE)
    private double latitudeTooHigh;
    @GenerateDouble(value = RANGE, min = -Double.MAX_VALUE, max = MIN_LATITUDE-1)
    private double latitudeTooLow;

    @GenerateDouble(value = RANGE, min = MIN_LONGITUDE, max = MAX_LONGITUDE)
    private double longitude;
    @GenerateDouble(value = RANGE, min = -Double.MAX_VALUE, max = MIN_LONGITUDE - 1)
    private double longitudeTooLow;
    @GenerateDouble(value = RANGE, min = MAX_LONGITUDE+1, max = Double.MAX_VALUE)
    private double longitudeTooHigh;


    //==============================
    // LATITUDE VALIDATION
    //==============================

    @Test
    @DisplayName("testValidLatitude: validLatitude assertion works correctly")
    void testValidLatitude() {
        var assertion = validLatitude();
        Tests.checkForNullCase(assertion);
        assertion.check(latitude);
        // Boundary cases
        assertion.check(MIN_LATITUDE);
        assertion.check(MAX_LATITUDE);
    }

    @Test
    @DisplayName("testValidLatitudeWithInvalid: validLatitude rejects out-of-range latitudes")
    void testValidLatitude_WithInvalid() {
        var assertion = GeolocationAssertions.validLatitude();
        Tests.checkForNullCase(assertion);

        assertThrowsFailedAssertion(() -> assertion.check(latitudeTooHigh));
        assertThrowsFailedAssertion(() -> assertion.check(latitudeTooLow));
    }

    //==============================
    // LONGITUDE VALIDATION
    //==============================

    @Test
    @DisplayName("testValidLongitude: validLongitude assertion works correctly")
    void testValidLongitude() {
        var assertion = validLongitude();
        Tests.checkForNullCase(assertion);

        assertion.check(longitude);

        // Boundary cases
        assertion.check(MIN_LONGITUDE);
        assertion.check(MAX_LONGITUDE);
    }

    @Test
    @DisplayName("testValidLongitudeWithInvalid: validLongitude rejects out-of-range longitudes")
    void testValidLongitude_WithInvalid() {
        var assertion = validLongitude();
        assertThrowsFailedAssertion(() -> assertion.check(longitudeTooHigh));
        assertThrowsFailedAssertion(() -> assertion.check(longitudeTooLow));
    }
}
