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

@file:JvmName("NetworkAssertions")

package tech.sirwellington.alchemy.arguments.assertions

import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.FailedAssertionException
import java.net.URL

/**

 * @author SirWellington
 */

/** The maximum allowable Port number  */
private val MAX_PORT = 65535

/**
 * Checks that a URL is valid, according to the [URL] class.

 * @return
 */

fun validURL(): AlchemyAssertion<String>
{
    return AlchemyAssertion { string ->
        nonEmptyString().check(string)

        try
        {
            URL(string)
        }
        catch (ex: Exception)
        {
            throw FailedAssertionException("Invalid URL: " + string, ex)
        }
    }
}


/**
 * Asserts that a Port number is valid and acceptable.

 * @return
 *
 * @see [https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers](https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers)
 */

fun validPort(): AlchemyAssertion<Int>
{
    return AlchemyAssertion { port ->
        if (port <= 0)
        {
            throw FailedAssertionException("Network port must be > 0")
        }

        if (port > MAX_PORT)
        {
            throw FailedAssertionException("Network port must <" + MAX_PORT)
        }
    }
}