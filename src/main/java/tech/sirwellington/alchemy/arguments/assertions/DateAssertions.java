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

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.annotations.arguments.NonNull;
import tech.sirwellington.alchemy.annotations.arguments.Required;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;

import static java.lang.String.format;
import static tech.sirwellington.alchemy.arguments.Checks.Internal.checkNotNull;

/**
 *
 * @author SirWellington
 */
@NonInstantiable
public final class DateAssertions
{

    private final static Logger LOG = LoggerFactory.getLogger(DateAssertions.class);

    DateAssertions() throws IllegalAccessException
    {
        throw new IllegalAccessException("cannot instantiate");
    }

    public static AlchemyAssertion<Date> inThePast()
    {
        return new AlchemyAssertion<Date>()
        {
            @Override
            public void check(Date argument) throws FailedAssertionException
            {
                //Recalculate now each time we are called
                Date present = new Date();
                //Check that argument is before present
                if (!argument.before(present))
                {
                    throw new FailedAssertionException(format("Expected Date %s to be in the past", argument));
                }
            }
        };
    }

    public static AlchemyAssertion<Date> before(@NonNull final Date expected)
    {
        checkNotNull(expected, "date cannot be null");

        return new AlchemyAssertion<Date>()
        {
            @Override
            public void check(Date argument) throws FailedAssertionException
            {
                Assertions.notNull().check(argument);

                if (!argument.before(expected))
                {
                    throw new FailedAssertionException(format("Expected Date to be before %s",
                                                              expected.toString()));
                }
            }
        };
    }

    public static AlchemyAssertion<Date> inTheFuture()
    {
        return new AlchemyAssertion<Date>()
        {
            @Override
            public void check(Date argument) throws FailedAssertionException
            {
                //Now must stay current
                Date present = new Date();

                //Check that argument is after present
                if (!argument.after(present))
                {
                    throw new FailedAssertionException(format("Expected Date to be in the future", argument));
                }
            }
        };
    }

    public static AlchemyAssertion<Date> after(@Required final Date expected)
    {
        checkNotNull(expected, "date cannot be null");

        return new AlchemyAssertion<Date>()
        {
            @Override
            public void check(Date date) throws FailedAssertionException
            {
                Assertions.notNull().check(date);

                if (!date.after(expected))
                {
                    throw new FailedAssertionException(format("Expected Date to be after %s", expected.toString()));
                }
            }
        };
    }

}
