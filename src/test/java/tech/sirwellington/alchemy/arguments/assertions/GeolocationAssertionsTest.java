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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat;
import tech.sirwellington.alchemy.test.junit.runners.GenerateDouble;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateDouble.Type.RANGE;

/**
 *
 * @author SirWellington
 */
@Repeat(100)
@RunWith(AlchemyTestRunner.class)
public class GeolocationAssertionsTest
{

    @GenerateDouble(value = RANGE, min = -90.0, max = 90.0)
    private Double latitude;

    @GenerateDouble(value = RANGE, min = -180.0, max = 180.0)
    private Double longitude;

    @GenerateDouble(value = RANGE, min = 91.0, max = Double.MAX_VALUE)
    private Double badLatitude;

    @GenerateDouble(value = RANGE, min = -Double.MAX_VALUE, max = -181.0)
    private Double badLongitude;

    @Before
    public void setUp() throws Exception
    {

    }
    
    @DontRepeat
    @Test
    public void testCannotInstantiate()
    {
        assertThrows(() -> new GeolocationAssertions());
    }

    @Test
    public void testValidLatitude()
    {
        AlchemyAssertion<Double> assertion = GeolocationAssertions.validLatitude();
        assertion.check(latitude);
    }

    @Test
    public void testValidLatitudeWithInvalid()
    {
        AlchemyAssertion<Double> assertion = GeolocationAssertions.validLatitude();
        assertThrows(() -> assertion.check(badLatitude));
    }

    @Test
    public void testValidLongitude()
    {
        AlchemyAssertion<Double> assertion = GeolocationAssertions.validLongitude();
        assertion.check(longitude);
    }

    @Test
    public void testValidLongitudeWithInvalid()
    {
        AlchemyAssertion<Double> assertion = GeolocationAssertions.validLongitude();
        assertThrows(() -> assertion.check(badLongitude));
    }

}
