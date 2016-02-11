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

 
package tech.sirwellington.alchemy.arguments.assertions;


import java.util.regex.Pattern;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;

import static tech.sirwellington.alchemy.arguments.Checks.Internal.isNullOrEmpty;


/**
 * Assertions made on Data relating to people.
 * + Emails
 * + Addresses
 * + Names
 * + Etc.
 * 
 * @author SirWellington
 */
@NonInstantiable
public final class PeopleAssertions
{
    
    private static final Pattern PATTERN = Pattern.compile("^.+@.+\\..+$");

    PeopleAssertions() throws IllegalAccessException
    {
        throw new IllegalAccessException("cannot instantiate");
    }

    /**
     * This Assertion performs basic validation of Emails
     * using the following pattern:
     * {@code "^.+@.+\\..+$"}. The intent of this Assertion is too keep it simple and 
     * prevent flagrant violations. The only way to truly know whether an email is valid is to send
     * a message to it.
     *
     * @return
     */
    public static AlchemyAssertion<String> validEmailAddress()
    {
        
        return (String email) ->
        {
            if (isNullOrEmpty(email))
            {
                throw new FailedAssertionException("Email is null or empty");
            }
            
            if (!PATTERN.matcher(email).matches())
            {
                throw new FailedAssertionException("Invalid Email Address: " + PATTERN);
            }
        };
    }

}
