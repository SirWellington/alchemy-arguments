/*
 * Copyright © 2018. Sir Wellington.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tech.sirwellington.alchemy.arguments;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.concurrency.Immutable;
import tech.sirwellington.alchemy.annotations.designs.FluidAPIDesign;
import tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern;

import static tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern.Role.CLIENT;
import static tech.sirwellington.alchemy.arguments.Checks.isNullOrEmpty;
import static tech.sirwellington.alchemy.arguments.ExceptionMapper.IDENTITY;

/**
 * @author SirWellington
 */
@FluidAPIDesign
@StrategyPattern(role = CLIENT)
@Immutable
@Internal
final class AssertionBuilderImpl<Argument, Ex extends Throwable> implements AssertionBuilder<Argument, Ex>
{

    private final static Logger LOG = LoggerFactory.getLogger(AssertionBuilderImpl.class);

    private final AlchemyAssertion<Argument> assertion;
    private final ExceptionMapper<Ex> exceptionMapper;
    @Immutable
    private final List<Argument> arguments;
    private final String overrideMessage;

    private AssertionBuilderImpl(AlchemyAssertion<Argument> assertion,
                                 ExceptionMapper<Ex> exceptionMapper,
                                 String overrideMessage,
                                 List<Argument> arguments)
    {
        this.assertion = assertion;
        this.exceptionMapper = exceptionMapper;
        this.overrideMessage = overrideMessage;
        this.arguments = arguments;
    }

    @Override
    public AssertionBuilder<Argument, Ex> usingMessage(String message)
    {
        Checks.checkThat(!isNullOrEmpty(message), "error message is empty");

        ExceptionMapper<Ex> newExceptionMapper;
        if (exceptionMapper instanceof DynamicExceptionSupplier)
        {
            newExceptionMapper = createUpdatedDynamicExceptionMapperWithMessage(message);
        }
        else
        {
            newExceptionMapper = this.exceptionMapper;
        }

        return new AssertionBuilderImpl<>(assertion, newExceptionMapper, message, arguments);
    }

    static <Argument> AssertionBuilderImpl<Argument, FailedAssertionException> checkThat(List<Argument> arguments)
    {
        return new AssertionBuilderImpl<>(null, IDENTITY, "", arguments);
    }

    @Override
    public <Ex extends Throwable> AssertionBuilderImpl<Argument, Ex> throwing(ExceptionMapper<Ex> exceptionMapper)
    {
        Checks.checkNotNull(exceptionMapper, "exceptionMapper is null");

        return new AssertionBuilderImpl<>(null, exceptionMapper, overrideMessage, arguments);
    }

    @Override
    public <Ex extends Throwable> AssertionBuilder<Argument, Ex> throwing(Class<Ex> exceptionClass)
    {
        Checks.checkNotNull(exceptionClass);

        return this.throwing(new DynamicExceptionSupplier<>(exceptionClass, overrideMessage));
    }

    @Override
    public AssertionBuilderImpl<Argument, Ex> is(AlchemyAssertion<Argument> assertion) throws Ex
    {
        Checks.checkNotNull(assertion, "assertion is null");

        AssertionBuilderImpl<Argument, Ex> newBuilder = new AssertionBuilderImpl<>(assertion, exceptionMapper, overrideMessage, arguments);

        //Check this assertion
        newBuilder.checkAssertion();
        //Return the new one to allow further assertions on this argument
        return newBuilder;
    }

    @Override
    public AssertionBuilder<Argument, Ex> isA(AlchemyAssertion<Argument> assertion) throws Ex
    {
        return is(assertion);
    }

    @Override
    public AssertionBuilder<Argument, Ex> are(AlchemyAssertion<Argument> assertion) throws Ex
    {
        return is(assertion);
    }

    private void checkAssertion() throws Ex
    {
        Checks.checkState(assertion != null, "no assertion found");
        Checks.checkState(exceptionMapper != null, "no exceptionMapper found");

        FailedAssertionException caught = null;

        try
        {
            for (Argument argument : arguments)
            {
                assertion.check(argument);
            }
        }
        catch (FailedAssertionException ex)
        {
            caught = ex;
            if (!Checks.isNullOrEmpty(overrideMessage))
            {
                caught.changeMessage(overrideMessage);
            }
        }
        catch (RuntimeException ex)
        {
            handleUnexpectedException(ex);
        }

        if (exceptionOccurred(caught))
        {
            handleFailedAssertion(caught);
        }

    }

    private boolean exceptionOccurred(FailedAssertionException caught)
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

    private ExceptionMapper<Ex> createUpdatedDynamicExceptionMapperWithMessage(String message)
    {
        DynamicExceptionSupplier<Ex> dynamicExceptionMapper = (DynamicExceptionSupplier<Ex>) exceptionMapper;
        Class<Ex> exceptionClass = dynamicExceptionMapper.getExceptionClass();

        return new DynamicExceptionSupplier<>(exceptionClass, message);
    }

}
