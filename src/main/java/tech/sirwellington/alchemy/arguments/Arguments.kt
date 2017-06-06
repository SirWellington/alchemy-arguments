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

@file:JvmName("Arguments")

package tech.sirwellington.alchemy.arguments

import tech.sirwellington.alchemy.annotations.arguments.Optional
import java.util.Arrays.asList

/**
 * This is the main entry-point for the Library.
 * <br></br>
 * From here you can do:
 * <pre>

 * `checkThat(zipCode)
 * .throwing(ex -> new InvalidZipCodeException(zipCode))
 * .is(notNull())
 * .is(positiveInteger())
 * .is(greaterThanOrEqualTo(10000))
 * .is(lessThan(99999));
 *
 * </pre>
 *
 * @author SirWellington
 */

/**
 * Begin assertions on a single argument.

 * @param <Argument> The type of the argument
 * *
 * @param argument   The argument itself, which may be `null`.
 * *
 * *
 * @return An object that allows building assertions on the argument.
</Argument>
 */
fun <Argument> checkThat(@Optional argument: Argument): AssertionBuilder<Argument, FailedAssertionException>
{
    return AssertionBuilderImpl.checkThat(asList(argument))
}

/**
 * Begin assertions on multiple arguments.

 * @param <Argument> The type of the arguments
 * *
 * @param argument   The first argument, which may be `null`.
 * *
 * @param others     The rest of the argument to perform checks on.
 * *
 * *
 * @return An object that allows building assertions on the arguments.
</Argument> */
fun <Argument> checkThat(@Optional argument: Argument?, vararg others: Argument?): AssertionBuilder<Argument, FailedAssertionException>
{
    val listOfArguments = mutableListOf<Argument?>()
    //Argument may be null, but an Array List permits null values.
    listOfArguments.add(argument)

    if (others.isNotEmpty())
    {
        listOfArguments.addAll(asList(*others))
    }

    return AssertionBuilderImpl.checkThat(listOfArguments)
}