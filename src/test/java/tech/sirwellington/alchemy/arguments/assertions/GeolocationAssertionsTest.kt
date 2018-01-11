/*
 * Copyright © 2018. Sir Wellington.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
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
import tech.sirwellington.alchemy.arguments.failedAssertion
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
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
    private var latitude: Double = 0.0

    @GenerateDouble(value = RANGE, min = -180.0, max = 180.0)
    private var longitude: Double = 0.0

    @GenerateDouble(value = RANGE, min = 91.0, max = java.lang.Double.MAX_VALUE)
    private var badLatitude: Double = 0.0

    @GenerateDouble(value = RANGE, min = -java.lang.Double.MAX_VALUE, max = -181.0)
    private var badLongitude: Double = 0.0

    @Before
    @Throws(Exception::class)
    fun setUp()
    {

    }

    @Test
    fun testValidLatitude()
    {
        val assertion = validLatitude()
        assertion.check(latitude)
    }

    @Test
    fun testValidLatitudeWithInvalid()
    {
        val assertion = validLatitude()
        assertThrows { assertion.check(badLatitude) }.failedAssertion()
    }

    @Test
    fun testValidLongitude()
    {
        val assertion = validLongitude()
        assertion.check(longitude)
    }

    @Test
    fun testValidLongitudeWithInvalid()
    {
        val assertion = validLongitude()
        assertThrows { assertion.check(badLongitude) }.failedAssertion()
    }

}
