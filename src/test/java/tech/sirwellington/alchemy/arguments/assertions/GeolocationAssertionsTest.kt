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

package tech.sirwellington.alchemy.arguments.assertions

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat
import tech.sirwellington.alchemy.test.junit.runners.GenerateDouble
import tech.sirwellington.alchemy.test.junit.runners.GenerateDouble.Type.RANGE
import tech.sirwellington.alchemy.test.junit.runners.Repeat

/**

 * @author SirWellington
 */
@Repeat(100)
@RunWith(AlchemyTestRunner::class)
class GeolocationAssertionsTest
{

    @GenerateDouble(value = RANGE, min = -90.0, max = 90.0)
    private val latitude: Double

    @GenerateDouble(value = RANGE, min = -180.0, max = 180.0)
    private val longitude: Double

    @GenerateDouble(value = RANGE, min = 91.0, max = java.lang.Double.MAX_VALUE)
    private val badLatitude: Double

    @GenerateDouble(value = RANGE, min = -java.lang.Double.MAX_VALUE, max = -181.0)
    private val badLongitude: Double

    @Before
    @Throws(Exception::class)
    fun setUp()
    {

    }

    @DontRepeat
    @Test
    fun testCannotInstantiate()
    {
        assertThrows { GeolocationAssertions() }
    }

    @Test
    fun testValidLatitude()
    {
        val assertion = GeolocationAssertions.validLatitude()
        assertion.check(latitude)
    }

    @Test
    fun testValidLatitudeWithInvalid()
    {
        val assertion = GeolocationAssertions.validLatitude()
        assertThrows { assertion.check(badLatitude) }
    }

    @Test
    fun testValidLongitude()
    {
        val assertion = GeolocationAssertions.validLongitude()
        assertion.check(longitude)
    }

    @Test
    fun testValidLongitudeWithInvalid()
    {
        val assertion = GeolocationAssertions.validLongitude()
        assertThrows { assertion.check(badLongitude) }
    }

}
