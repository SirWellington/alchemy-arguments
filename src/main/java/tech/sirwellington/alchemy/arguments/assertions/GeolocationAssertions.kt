/*
  * Copyright 2017 SirWellington Tech..
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

@file:JvmName("GeolocationAssertions")

package tech.sirwellington.alchemy.arguments.assertions


import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.checkThat

/**
 * Assertions for testing Geo-Location data, like latitude and longitude.
 *
 * @author SirWellington
 */

/**
 * Checks that a Latitude is in the valid range [-90...90] (inclusive).
 *
 * @return
 */

fun validLatitude(): AlchemyAssertion<Double>
{
    return AlchemyAssertion { lat ->

        checkThat(lat)
                .usingMessage("Latitude must be between -90 and 90, but was " + lat!!)
                .isA(lessThanOrEqualTo(90.0))
                .isA(greaterThanOrEqualTo(-90.0))
    }

}

/**
 * Checks that a Longitude is in the valid range [-180...180](inclusive).
 *
 * @return
 */
fun validLongitude(): AlchemyAssertion<Double>
{
    return AlchemyAssertion { lon ->

        checkThat<Double>(lon)
                .usingMessage("Longitude must be between -180 and 180, but was " + lon!!)
                .isA(greaterThanOrEqualTo(-180.0))
                .isA(lessThanOrEqualTo(180.0))
    }
}