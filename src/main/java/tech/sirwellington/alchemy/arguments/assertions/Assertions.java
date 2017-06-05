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

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.annotations.arguments.Optional;
import tech.sirwellington.alchemy.annotations.arguments.Required;
import tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.Checks;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;

import static java.lang.String.format;
import static tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern.Role.CONCRETE_BEHAVIOR;

/**
 * Common {@linkplain AlchemyAssertion Alchemy Assertions}.
 *
 * @author SirWellington
 */
@StrategyPattern(role = CONCRETE_BEHAVIOR)
@NonInstantiable
public final class Assertions
{

    private final static Logger LOG = LoggerFactory.getLogger(Assertions.class);

    Assertions() throws IllegalAccessException
    {
        throw new IllegalAccessException("cannot instantiate class");
    }

    /**
     * Asserts that the argument is not null.
     *
     * @param <A>
     *
     * @return
     * @see #nullObject() 
     */
    public static <A> AlchemyAssertion<A> notNull()
    {
        return new AlchemyAssertion<A>()
        {
            @Override
            public void check(A argument) throws FailedAssertionException
            {
                if (argument == null)
                {
                    throw new FailedAssertionException("Argument is null");
                }
            }
        };
    }
    
    /**
     * Asserts that the argument is null. 
     * This is the opposite of {@link #notNull() }.
     * 
     * @param <A>
     * @return 
     * @see #notNull() 
     */
    public static <A> AlchemyAssertion<A> nullObject()
    {
        return reference ->
        {
            if (reference != null)
            {
                throw new FailedAssertionException("Argument is not null: " + reference);
            }
        };
    }

    /**
     * Asserts that the argument is the same instance as {@code other}.
     *
     * @param <A>
     * @param other
     *
     * @return
     */
    public static <A> AlchemyAssertion<A> sameInstanceAs(@Optional Object other)
    {
        return (argument) ->
        {
            if (argument == null && other == null)
            {
                return;
            }

            if (argument != other)
            {
                throw new FailedAssertionException("Expected " + argument + " to be the same instance as " + other);
            }
        };
    }

    /**
     * Asserts that an argument is an {@code instanceOf} the specified class. This Assertion respects the inheritance
     * hierarchy, so
     *
     * <pre>
     *
     * Integer instanceOf Object
     * Integer instanceOf Number
     * Integer instanceOf Integer
     * </pre>
     *
     * will pass, but
     *
     * <pre>
     *
     * Integer instanceOf Double
     * Integer instanceOf String
     * </pre>
     *
     * will fail.
     *
     * @param <A>
     * @param classOfExpectedType
     * @return
     */
    public static <A> AlchemyAssertion<A> instanceOf(Class<?> classOfExpectedType)
    {
        Checks.Internal.checkNotNull(classOfExpectedType, "class cannot be null");

        return (argument) ->
        {
            notNull().check(argument);

            if (!classOfExpectedType.isInstance(argument))
            {
                throw new FailedAssertionException("Expected Object of type: " + classOfExpectedType);
            }
        };
    }

    /**
     * Asserts that the argument is {@linkplain Object#equals(java.lang.Object) equal to} {@code other}.
     *
     * @param <A>
     * @param other
     *
     * @return
     */
    public static <A> AlchemyAssertion<A> equalTo(@Optional A other)
    {
        return (argument) ->
        {
            if (!Objects.equals(argument, other))
            {
                throw new FailedAssertionException(format("Expected %s to be equal to %s", argument, other));
            }
        };
    }

    /**
     * Runs the inverse on another {@link AlchemyAssertion}. This allows you to create expressions such as:
     *
     * <pre>
     * {@code
     * checkThat(filename)
     *      .is(not( stringWithWhitespace() ))
     *      .is(not( equalTo("info.txt") ));
     * }
     * </pre>
     *
     * @param <A>
     * @param assertion
     *
     * @return
     */
    public static <A> AlchemyAssertion<A> not(@Required AlchemyAssertion<A> assertion)
    {
        Checks.Internal.checkNotNull(assertion, "missing assertion");

        return (argument) ->
        {
            try
            {
                assertion.check(argument);
            }
            catch (FailedAssertionException ex)
            {
                return;
            }

            throw new FailedAssertionException("Expected assertion to fail, but it passed: " + assertion);

        };
    }

}
