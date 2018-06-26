/*
 * Copyright © 2018. Sir Wellington.
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

package tech.sirwellington.alchemy.arguments;

import java.util.*;

import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.annotations.arguments.Optional;
import tech.sirwellington.alchemy.annotations.designs.FluidAPIDesign;

/**
 * This is the main entry-point for the Library.
 * <br></br>
 * From here you can do:
 * <pre>
 *
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
@NonInstantiable
@FluidAPIDesign
public final class Arguments
{

    Arguments() throws IllegalAccessException
    {
        throw new IllegalAccessException("cannot instantiate");
    }

    public static <Argument> AssertionBuilder<Argument, FailedAssertionException> checkThat(@Optional Argument argument)
    {
        return AssertionBuilderImpl.checkThat(Collections.singletonList(argument));
    }

    public static <Argument> AssertionBuilder<Argument, FailedAssertionException> checkThat(@Optional Argument argument, @Optional Argument... others)
    {
        List<Argument> listOfArguments = new ArrayList<>();
        listOfArguments.add(argument);

        if (others.length > 0)
        {
            listOfArguments.addAll(Arrays.asList(others));
        }

        return AssertionBuilderImpl.checkThat(listOfArguments);
    }
}
