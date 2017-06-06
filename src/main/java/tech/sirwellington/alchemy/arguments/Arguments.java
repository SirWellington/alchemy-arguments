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
package tech.sirwellington.alchemy.arguments;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.annotations.arguments.Optional;
import tech.sirwellington.alchemy.annotations.designs.FluidAPIDesign;

import static java.util.Arrays.asList;

/**
 * This is the main entry-point for the Library.
 * <br>
 * From here you can do:
 * <pre>
 * 
 * {@code
 * checkThat(zipCode)
 *      .throwing(ex -> new InvalidZipCodeException(zipCode))
 *      .is(notNull())
 *      .is(positiveInteger())
 *      .is(greaterThanOrEqualTo(10000))
 *      .is(lessThan(99999));
 * }
 * </pre>
 *
 * @author SirWellington
 */
@NonInstantiable
@FluidAPIDesign
public final class Arguments
{

    private final static Logger LOG = LoggerFactory.getLogger(Arguments.class);

    Arguments() throws IllegalAccessException
    {
        throw new IllegalAccessException("cannot instantiate class");
    }

    /**
     * Begin assertions on a single argument.
     *
     * @param <Argument> The type of the argument
     * @param argument   The argument itself, which may be {@code null}.
     *
     * @return An object that allows building assertions on the argument.
     */
    public static <Argument> AssertionBuilder<Argument, FailedAssertionException> checkThat(@Optional Argument argument)
    {
        return AssertionBuilderImpl.checkThat(asList(argument));
    }

    /**
     * Begin assertions on multiple arguments.
     *
     * @param <Argument> The type of the arguments
     * @param argument   The first argument, which may be {@code null}.
     * @param others     The rest of the argument to perform checks on.
     *
     * @return An object that allows building assertions on the arguments.
     */
    public static <Argument> AssertionBuilder<Argument, FailedAssertionException> checkThat(@Optional Argument argument, Argument... others)
    {
        List<Argument> listOfArguments = new ArrayList<>();
        //Argument may be null, but an Array List permits null values.
        listOfArguments.add(argument);
        
        if (others != null && others.length > 0)
        {
            listOfArguments.addAll(asList(others));
        }
        
        return AssertionBuilderImpl.checkThat(listOfArguments);
    }

}
