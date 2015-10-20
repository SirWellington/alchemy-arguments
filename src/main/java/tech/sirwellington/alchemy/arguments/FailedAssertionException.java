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

import tech.sirwellington.alchemy.annotations.access.Internal;

/**
 *
 * An exception that is thrown when an argument assertion fails. This exception is a sub-type of
 * {@link IllegalArgumentException}.
 *
 * @author SirWellington
 */
public class FailedAssertionException extends IllegalArgumentException
{

    private String message = "";

    public FailedAssertionException()
    {
    }

    public FailedAssertionException(String message)
    {
        super(message);
        this.message = message;
    }

    public FailedAssertionException(String message, Throwable cause)
    {
        super(message, cause);
        this.message = message;
    }

    public FailedAssertionException(Throwable cause)
    {
        super(cause);
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    @Internal
    void changeMessage(String message)
    {
        this.message = message;
    }

}
