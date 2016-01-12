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
import static tech.sirwellington.alchemy.arguments.Checks.Internal.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;
import static tech.sirwellington.alchemy.arguments.assertions.NumberAssertions.greaterThan;

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
    
    /**
     * Ensures that the {@link Instant} represents exactly Right now, at the time of checking. It does so
     * within a margin-of-error of 5 milliseconds. This should be acceptable for most modern processors.
     * Use {@link #nowWithinDelta(long) } for more fine-grained deltas.
     * 
     * @return 
     * @see #nowWithinDelta(long) 
     */
    public static AlchemyAssertion<Instant> rightNow() 
    {
        return nowWithinDelta(5L);
    }
    
    /**
     * Ensures that an {@link Instant} is {@link Instant#now() }, within the specified margin of error.
     * 
     * @param marginOfErrorInMillis The Acceptable Margin-Of-Error, in Milliseconds. The instant must be within this delta.
     * 
     * @return
     * 
     * @throws IllegalArgumentException If the marginOfError is {@code < 0}.
     * 
     * @see #rightNow() 
     */
    public static AlchemyAssertion<Instant> nowWithinDelta(long marginOfErrorInMillis) throws IllegalArgumentException
    {
        checkThat(marginOfErrorInMillis >= 0, "millis must be non-negative.");
        
        return instant ->
        {
            long now = Instant.now().toEpochMilli();
            notNull().check(instant);
            
            long epoch = instant.toEpochMilli();
            long difference = Math.abs(epoch - now);
            
            if (difference > marginOfErrorInMillis)
            {
                throw new FailedAssertionException(
                    "Time difference of " + difference + " exceeded margin-of-error of " + marginOfErrorInMillis + "ms");
            }
            
        };
    }
    
    public static AlchemyAssertion<Long> epochRightNow()
    {
        return epochNowWithinDelta(5L);
    }
    
    public static AlchemyAssertion<Long> epochNowWithinDelta(long marginOfErrorInMillis) throws IllegalArgumentException
    {
        checkThat(marginOfErrorInMillis >= 0, "millis must be non-negative.");
        
        return epoch ->
        {
            long now = Instant.now().toEpochMilli();
            greaterThan(0L).check(epoch);
            
            long difference = Math.abs(epoch - now);
            
            if (difference > marginOfErrorInMillis)
            {
                throw new FailedAssertionException(
                    "Time difference of " + difference + " exceeded margin-of-error of " + marginOfErrorInMillis + "ms");
            }
            
        };
    }

}
