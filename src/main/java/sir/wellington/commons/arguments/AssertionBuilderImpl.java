/*
 * Copyright 2015 SirWellington.
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
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sir.wellington.alchemy.annotations.concurrency.Immutable;
import sir.wellington.alchemy.annotations.patterns.FluidAPIPattern;
import sir.wellington.alchemy.annotations.patterns.StrategyPattern;
import static sir.wellington.alchemy.annotations.patterns.StrategyPattern.Role.CLIENT;
import static sir.wellington.commons.arguments.ExceptionMapper.IDENTITY;

/**
 *
 * @author SirWellington
 */
@FluidAPIPattern
@StrategyPattern(role = CLIENT)
@Immutable
final class AssertionBuilderImpl<Argument, Ex extends Throwable> implements AssertionBuilder<Argument, Ex>
{

    private final static Logger LOG = LoggerFactory.getLogger(AssertionBuilderImpl.class);

    private final Assertion<Argument> assertion;
    private final ExceptionMapper<Ex> exceptionMapper;
    private final Argument argument;
    private final String overrideMessage;

    private AssertionBuilderImpl(Assertion<Argument> assertion,
                                 ExceptionMapper<Ex> exceptionMapper,
                                 String overrideMessage,
                                 Argument argument)
    {
        this.assertion = assertion;
        this.exceptionMapper = exceptionMapper;
        this.overrideMessage = overrideMessage;
        this.argument = argument;
    }

    @Override
    public AssertionBuilder<Argument, Ex> usingMessage(String message)
    {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(message), "error message is empty");

        return new AssertionBuilderImpl<>(assertion, exceptionMapper, message, argument);
    }

    static <Argument> AssertionBuilderImpl<Argument, FailedAssertionException> checkThat(Argument argument)
    {
        return new AssertionBuilderImpl<>(null, IDENTITY, "", argument);
    }

    @Override
    public <Ex extends Throwable> AssertionBuilderImpl<Argument, Ex> usingException(ExceptionMapper<Ex> exceptionMapper)
    {
        Preconditions.checkNotNull(exceptionMapper, "exceptionMapper is null");

        return new AssertionBuilderImpl<>(null, exceptionMapper, "", argument);
    }

    @Override
    public AssertionBuilderImpl<Argument, Ex> is(Assertion<Argument> assertion) throws Ex
    {
        Preconditions.checkNotNull(assertion, "assertion is null");

        AssertionBuilderImpl<Argument, Ex> newBuilder = new AssertionBuilderImpl<>(assertion, exceptionMapper, overrideMessage, argument);

        //Check this assertion
        newBuilder.checkAssertion();
        //Return the new one to allow further assertions on this argument
        return newBuilder;
    }

    private void checkAssertion() throws Ex
    {
        Preconditions.checkState(assertion != null, "no assertion found");
        Preconditions.checkState(exceptionMapper != null, "no exceptionMapper found");

        FailedAssertionException caught = null;

        try
        {
            assertion.check(argument);
        }
        catch (FailedAssertionException ex)
        {
            caught = ex;
            if (!Strings.isNullOrEmpty(overrideMessage))
            {
                caught.changeMessage(overrideMessage);
            }
        }
        catch (RuntimeException ex)
        {
            handleUnexpectedException(ex);
        }

        if (exceptionOccured(caught))
        {
            handleFailedAssertion(caught);
        }

    }

    private boolean exceptionOccured(FailedAssertionException caught)
    {
        return caught != null;
    }

    private void handleUnexpectedException(RuntimeException ex) throws Ex
    {
        LOG.warn("Assertion {} threw an unexpected exception. Only {} Exceptions are acceptable for Assertions.",
                 assertion,
                 FailedAssertionException.class.getSimpleName(),
                 ex);

        FailedAssertionException wrappedException = new FailedAssertionException("wrapping unexpected exception", ex);
        handleFailedAssertion(wrappedException);
    }

    private void handleFailedAssertion(FailedAssertionException caught) throws Ex
    {
        Ex mappedEx = exceptionMapper.apply(caught);

        if (mappedEx != null)
        {
            throw mappedEx;
        }
        else
        {
            LOG.warn("Exception Mapper did not return a throwable. Swallowing exception", caught);
        }
    }

}
