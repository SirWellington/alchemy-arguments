/*
 * Copyright 2017 SirWellington Tech..
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

@file:JvmName("AddressAssertions")

package tech.sirwellington.alchemy.arguments.assertions


import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.checkThat


/**
 * A Library assertion intended to check the validity of address components.
 *
 *
 * You will find checks for:
 * <pre>
 * + Zip Codes
 * + States
 * + Countries
 * </pre>
 *
 * @author SirWellington
 */

/**
 * Checks that a number can represent a valid zip code.
 * Apparently a Zip Code does not necessarily have to be a digit.
 *
 * @return
 */
fun validZipCode(): AlchemyAssertion<String>
{
    return AlchemyAssertion { zip ->

        checkThat(zip)
                .usingMessage("zip must consist of 4-5 characters")
                .isA(stringWithLengthGreaterThanOrEqualTo(4))
                .isA(stringWithLengthLessThanOrEqualTo(5))
    }
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
fun validZipCodeString(): AlchemyAssertion<String>
{
    return AlchemyAssertion { zip ->

        nonEmptyString().check(zip)
        integerString().check(zip)
        stringWithLength(5)

        validZipCode().check(zip)
    }
}
