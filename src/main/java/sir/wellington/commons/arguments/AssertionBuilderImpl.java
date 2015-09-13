/*
 * Copyright 2015 Wellington.
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
package sir.wellington.commons.arguments;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static sir.wellington.commons.arguments.ExceptionMapper.IDENTITY;

/**
 *
 * @author SirWellington
 */
final class AssertionBuilderImpl<Argument, Ex extends Throwable> implements AssertionBuilder<Argument, Ex>
{

    private final static Logger LOG = LoggerFactory.getLogger(AssertionBuilderImpl.class);

    private final Assertion<Argument> assertion;
    private final ExceptionMapper<Ex> exceptionMapper;
    private final Argument argument;

    private AssertionBuilderImpl()
    {
        assertion = null;
        exceptionMapper = null;
        argument = null;
    }

    private AssertionBuilderImpl(Assertion<Argument> assertion, ExceptionMapper<Ex> exceptionMapper, Argument argument)
    {
        this.assertion = assertion;
        this.exceptionMapper = exceptionMapper;
        this.argument = argument;
    }

    static <Argument> AssertionBuilderImpl<Argument, FailedAssertionException> checkThat(Argument argument)
    {
        return new AssertionBuilderImpl<>(null, IDENTITY, argument);
    }

    @Override
    public <Ex extends Throwable> AssertionBuilderImpl<Argument, Ex> usingException(ExceptionMapper<Ex> exceptionMapper)
    {
        Preconditions.checkNotNull(exceptionMapper, "exceptionMapper is null");

        return new AssertionBuilderImpl<>(null, exceptionMapper, argument);
    }

    @Override
    public AssertionBuilderImpl<Argument, Ex> is(Assertion<Argument> assertion) throws Ex
    {
        Preconditions.checkNotNull(assertion, "assertion is null");

        AssertionBuilderImpl<Argument, Ex> newBuilder = new AssertionBuilderImpl<>(assertion, exceptionMapper, argument);

        //Check this assertion
        newBuilder.checkAssertion();
        //Return the new one to allow further assertions on this argument
        return newBuilder;
    }

    private void checkAssertion() throws Ex
    {
        Preconditions.checkState(assertion != null, "no assertion found");
        Preconditions.checkState(exceptionMapper != null, "no exceptionMapper found");

        try
        {
            assertion.check(argument);
        }
        catch (FailedAssertionException ex)
        {
            Ex mappedEx = exceptionMapper.apply(ex);

            if (mappedEx != null)
            {
                throw mappedEx;
            }
            else
            {
                LOG.warn("Exception Mapper did not return a throwable. Swallowing exception", ex);
            }

        }
    }

}
