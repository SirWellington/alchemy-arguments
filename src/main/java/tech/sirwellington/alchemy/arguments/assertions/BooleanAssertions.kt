/*
 * Copyright © 2019. Sir Wellington.
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

@file:JvmName("BooleanAssertions")

package tech.sirwellington.alchemy.arguments.assertions


import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.FailedAssertionException

/**

 * @author SirWellington
 */


fun trueStatement(): AlchemyAssertion<Boolean>
{
    return AlchemyAssertion { b ->

        notNull<Any>().check(b)

        if ((!b))
        {
            throw FailedAssertionException("Condition not met")
        }
    }
}


fun falseStatement(): AlchemyAssertion<Boolean>
{
    return AlchemyAssertion { b ->

        notNull<Any>().check(b)

        trueStatement().check((!b))
    }
}