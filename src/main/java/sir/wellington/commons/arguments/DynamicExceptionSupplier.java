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
import com.google.common.base.Strings;
import java.lang.reflect.Constructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wellington
 */
final class DynamicExceptionSupplier<Ex extends Throwable> implements ExceptionMapper<Ex>
{

    private static final Logger LOG = LoggerFactory.getLogger(DynamicExceptionSupplier.class);

    private final Class<Ex> exceptionClass;
    private final String message;

    DynamicExceptionSupplier(Class<Ex> exceptionClass, String message)
    {
        Preconditions.checkNotNull(exceptionClass, "missing exceptionClass");
        this.exceptionClass = exceptionClass;
        this.message = message;
    }

    @Override
    public Ex apply(FailedAssertionException cause)
    {
        Ex instance = tryToCreateInstance(cause);

        return instance;
    }

    private Ex tryToCreateInstance(FailedAssertionException cause)
    {
        try
        {
            if (Strings.isNullOrEmpty(message))
            {
                if (cause == null)
                {
                    return exceptionClass.newInstance();
                }
                else
                {

                    Constructor<Ex> constructor = exceptionClass.getConstructor(Throwable.class);
                    if (constructor != null)
                    {
                        return constructor.newInstance(cause);
                    }
                }
            }
            else
            {
                if (cause == null)
                {
                    Constructor<Ex> constructor = exceptionClass.getConstructor(String.class);

                    if (constructor != null)
                    {
                        return constructor.newInstance(message);
                    }
                }
                else
                {
                    Constructor<Ex> constructor = exceptionClass.getConstructor(String.class, Throwable.class);
                    if (constructor != null)
                    {
                        return constructor.newInstance(message, cause);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            LOG.error("Failed to initialize instance of Exception type {}", exceptionClass, ex);
        }

        return null;
    }

    @Override
    public String toString()
    {
        return "DynamicExceptionSupplier{" + "exceptionClass=" + exceptionClass + ", message=" + message + '}';
    }

}
