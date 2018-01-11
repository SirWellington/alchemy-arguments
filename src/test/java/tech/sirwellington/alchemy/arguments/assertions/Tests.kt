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

import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat
import tech.sirwellington.alchemy.annotations.access.Internal
import tech.sirwellington.alchemy.annotations.access.NonInstantiable
import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.failedAssertion
import tech.sirwellington.alchemy.generator.AlchemyGenerator
import tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows

/**

 * @author SirWellington
 */
@Internal
@NonInstantiable
object Tests
{

    fun <T> runTests(assertion: AlchemyAssertion<T>,
                     badArguments: AlchemyGenerator<T>,
                     goodArguments: AlchemyGenerator<T>)
    {
        assertThat(assertion, notNullValue())

        val badArgument = one(badArguments)
        assertThrows { assertion.check(badArgument) }.failedAssertion()
        val goodArgument = one(goodArguments)
        assertion.check(goodArgument)
    }

    fun checkForNullCase(assertion: AlchemyAssertion<*>)
    {
        assertThrows { assertion.check(null) }
                .failedAssertion()
    }
}
