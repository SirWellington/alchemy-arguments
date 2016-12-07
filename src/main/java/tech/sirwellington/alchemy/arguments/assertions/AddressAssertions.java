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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;

/**
 * A Library assertion intended to check the validity of address components.
 * <p>
 * You will find checks for:
 * <pre>
 * + Zip Codes
 * + States
 * + Countries
 * </pre>
 * @author SirWellington
 */
@NonInstantiable
public final class AddressAssertions
{
    private final static Logger LOG = LoggerFactory.getLogger(AddressAssertions.class);
    
    AddressAssertions() throws IllegalAccessException
    {
        throw new IllegalAccessException("cannot instantiate");
    }
    
    /**
     * Checks that a number can represent a valid zip code.
     *
     * @return
     */
    public static AlchemyAssertion<Integer> validZipCode()
    {
        return zip ->
        {
            if (zip <= 0)
            {
                throw new FailedAssertionException("Zip Code must be > 0");
            }
            
            if (zip >= 100_000)
            {
                throw new FailedAssertionException("Zip Code must be a 5 digit number");
            }
        };
    }
    
    /**
     * Checks that a ZipCode is:
     * <pre>
     * + Not null
     * + Represents an Integer number (eg, 90012)
     * + Is 5 digits (eg, 01693)
     * + Is in the range (00000)...(99999)
     * </pre>
     *
     * @return
     */
    public static AlchemyAssertion<String> validZipCodeString()
    {
        return zip ->
        {
            StringAssertions.nonEmptyString().check(zip);
            StringAssertions.integerString().check(zip);
            StringAssertions.stringWithLength(5);
            validZipCode().check(Integer.valueOf(zip));
        };
    }
    
}
