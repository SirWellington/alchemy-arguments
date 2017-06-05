 /*
  * Copyright 2016 RedRoma, Inc..
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


package tech.sirwellington.alchemy.arguments.assertions;


import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;

import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.NumberAssertions.greaterThanOrEqualTo;
import static tech.sirwellington.alchemy.arguments.assertions.NumberAssertions.lessThanOrEqualTo;

/**
 * Assertions for testing Geo-Location data, like latitude and longitude.
 * 
 * @author SirWellington
 */
@NonInstantiable
public final class GeolocationAssertions
{
    
    GeolocationAssertions() throws IllegalAccessException
    {
        throw new IllegalAccessException("cannot instantiate");
    }
    
    /**
     * Checks that a Latitude is in the valid range [-90...90] (inclusive).
     * 
     * @return 
     */
    public static AlchemyAssertion<Double> validLatitude()
    {
        return new AlchemyAssertion<Double>()
        {
            @Override
            public void check(Double latitude) throws FailedAssertionException
            {
                checkThat(latitude)
                        .usingMessage("Latitude must be between -90 and 90, but was " + latitude)
                        .is(lessThanOrEqualTo(90.0))
                        .is(greaterThanOrEqualTo(-90.0));
            }
        };

    }
    
    /**
     * Checks that a Longitude is in the valid range [-180...180](inclusive).
     * 
     * @return 
     */
    public static AlchemyAssertion<Double> validLongitude()
    {
        return new AlchemyAssertion<Double>()
        {
            @Override
            public void check(Double longitude) throws FailedAssertionException
            {
                checkThat(longitude)
                        .usingMessage("Longitude must be between -180 and 180, but was " + longitude)
                        .is(greaterThanOrEqualTo(-180.0))
                        .is(lessThanOrEqualTo(180.0));
            }
        };
    }
    
}
