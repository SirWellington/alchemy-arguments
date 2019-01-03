/*
 * Copyright © 2019. Sir Wellington.
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

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.concurrency.Immutable;

import static tech.sirwellington.alchemy.arguments.Checks.checkNotNull;
import static tech.sirwellington.alchemy.arguments.Checks.isNullOrEmpty;

/**
 * This class uses an Exception class to dynamically create an appropriate wrapper exception.
 *
 * @author SirWellington
 */
@Internal
@Immutable
final class DynamicExceptionSupplier<Ex extends Throwable> implements ExceptionMapper<Ex>
{

    private static final Logger LOG = LoggerFactory.getLogger(DynamicExceptionSupplier.class);

    private final Class<Ex> exceptionClass;
    private final String overrideMessage;

    DynamicExceptionSupplier(Class<Ex> exceptionClass, String overrideMessage)
    {
        checkNotNull(exceptionClass, "missing exceptionClass");

        this.exceptionClass = exceptionClass;
        this.overrideMessage = overrideMessage;
    }

    @Override
    public Ex apply(FailedAssertionException cause)
    {
        return tryToCreateInstance(cause);
    }

    private Ex tryToCreateInstance(FailedAssertionException cause)
    {
        try
        {
            if (haveOverrideMessageAndACause(overrideMessage, cause))
            {
                if (throwableClassHasMessageAndCauseConstructor())
                {
                    return exceptionClass.getConstructor(String.class, Throwable.class)
                                         .newInstance(overrideMessage, cause);
                }
                else if (throwableClassHasCauseConstructor())
                {
                    return exceptionClass.getConstructor(Throwable.class)
                                         .newInstance(cause);
                }
                else if (throwableClassHasMessageConstructor())
                {
                    return exceptionClass.getConstructor(String.class)
                                         .newInstance(overrideMessage);
                }

            }

            if (haveOnlyACause(overrideMessage, cause))
            {
                if (throwableClassHasCauseConstructor())
                {
                    return exceptionClass.getConstructor(Throwable.class).newInstance(cause);
                }

                if (throwableClassHasMessageConstructor())
                {
                    String message = cause.getMessage();
                    return exceptionClass.getConstructor(String.class).newInstance(message);
                }
            }

            if (haveOnlyAnOverrideMessage(overrideMessage, cause))
            {
                if (throwableClassHasMessageConstructor())
                {
                    return exceptionClass.getConstructor(String.class).newInstance(overrideMessage);
                }
            }

        }
        catch (Exception ex)
        {
            LOG.error("Failed to initialize instance of Exception type {}", exceptionClass, ex);
        }

        try
        {
            if (hasDefaultConstructor())
            {
                return exceptionClass.newInstance();
            }
        }
        catch (Exception ex)
        {
            LOG.warn("Failed to create instance of {} using default constructor", exceptionClass.getName());
        }

        return null;
    }

    @Override
    public String toString()
    {
        return "DynamicExceptionSupplier{" + "exceptionClass=" + exceptionClass + ", overrideMessage=" + overrideMessage + '}';
    }

    private boolean hasConstructorWithArguments(Class<?>... classes) throws NoSuchMethodException, SecurityException
    {
        try
        {
            Constructor<Ex> constructor = exceptionClass.getConstructor(classes);
            return constructor != null;
        }
        catch (NoSuchMethodException ex)
        {
            return false;
        }
    }

    private boolean hasDefaultConstructor() throws NoSuchMethodException, SecurityException
    {
        return hasConstructorWithArguments();
    }

    private boolean throwableClassHasCauseConstructor() throws NoSuchMethodException, SecurityException
    {
        return hasConstructorWithArguments(Throwable.class);
    }

    private boolean throwableClassHasMessageConstructor() throws NoSuchMethodException, SecurityException
    {
        return hasConstructorWithArguments(String.class);
    }

    private boolean throwableClassHasMessageAndCauseConstructor() throws NoSuchMethodException, SecurityException
    {
        return hasConstructorWithArguments(String.class, Throwable.class);
    }

    private boolean haveOnlyAnOverrideMessage(String message, FailedAssertionException cause)
    {
        return !isNullOrEmpty(message) && cause == null;
    }

    private boolean haveOnlyACause(String message, FailedAssertionException cause)
    {
        return cause != null && isNullOrEmpty(message);
    }

    private boolean haveOverrideMessageAndACause(String message, FailedAssertionException cause)
    {
        return cause != null && !isNullOrEmpty(message);
    }

    @Internal
    Class<Ex> getExceptionClass()
    {
        return this.exceptionClass;
    }

}
