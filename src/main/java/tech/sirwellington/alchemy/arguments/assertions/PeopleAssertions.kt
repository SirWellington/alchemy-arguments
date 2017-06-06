/*
 * Copyright 2015 SirWellington Tech.
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


import tech.sirwellington.alchemy.annotations.access.NonInstantiable
import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.Checks.Internal.isNullOrEmpty
import tech.sirwellington.alchemy.arguments.FailedAssertionException
import java.util.regex.Pattern


/**
 * Assertions made on Data relating to people.
 * + Emails
 * + Addresses
 * + Names
 * + Etc.

 * @author SirWellington
 */
@NonInstantiable
class PeopleAssertions @Throws(IllegalAccessException::class)
internal constructor()
{

    init
    {
        throw IllegalAccessException("cannot instantiate")
    }

    companion object
    {

        private val PATTERN = Pattern.compile("^.+@.+\\..+$")

        /**
         * This Assertion performs basic validation of Emails
         * using the following pattern:
         * `"^.+@.+\\..+$"`. The intent of this Assertion is too keep it simple and
         * prevent flagrant violations. The only way to truly know whether an email is valid is to send
         * a message to it.

         * @return
         */

        fun validEmailAddress(): AlchemyAssertion<String>
        {

            return AlchemyAssertion { email: String ->
                if (isNullOrEmpty(email))
                {
                    throw FailedAssertionException("Email is null or empty")
                }

                if (!PATTERN.matcher(email).matches())
                {
                    throw FailedAssertionException("Invalid Email Address: " + PATTERN)
                }
            }
        }
    }

}
