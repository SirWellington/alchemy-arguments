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

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.annotations.arguments.NonNull;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;

import static java.lang.String.format;
import static tech.sirwellington.alchemy.arguments.Checks.Internal.checkNotNull;

/**
 *
 * @author SirWellington
 */
@NonInstantiable
public final class TimeAssertions
{

    private final static Logger LOG = LoggerFactory.getLogger(TimeAssertions.class);

    TimeAssertions() throws IllegalAccessException
    {
        throw new IllegalAccessException("cannot instantiate");
    }

    /**
     * Asserts that the {@link Instant} is in the past. Note that the present is constantly recalculated in order to
     * stay current.
     *
     * @return
     */
    public static AlchemyAssertion<Instant> inThePast()
    {
        return argument ->
        {
            //Recalculate the present on each call to stay current
            Instant present = Instant.now();
            if (!argument.isBefore(present))
            {
                throw new FailedAssertionException(format("Expected Timestamp %s to be in the past", argument));
            }
        };
    }

    public static AlchemyAssertion<Instant> before(@NonNull Instant expected)
    {
        checkNotNull(expected, "time cannot be null");

        return argument ->
        {
            Assertions.notNull().check(argument);

            if (!argument.isBefore(expected))
            {
                throw new FailedAssertionException(format("Expected Timestamp to be before %s", expected.toString()));
            }
        };
    }

    public static AlchemyAssertion<Instant> inTheFuture()
    {
        return argument ->
        {
            //Recalculate the present on each call to stay current
            Instant present = Instant.now();
            if (!argument.isAfter(present))
            {
                throw new FailedAssertionException(format("Expected Timestamp %s to be in the future", argument));
            }
        };
    }

    public static AlchemyAssertion<Instant> after(@NonNull Instant expected)
    {
        checkNotNull(expected, "time cannot be null");

        return argument ->
        {
            Assertions.notNull().check(argument);

            if (!argument.isAfter(expected))
            {
                throw new FailedAssertionException(format("Expected Timestamp to be after %s", expected.toString()));
            }
        };
    }

}
