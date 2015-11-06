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

import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.arguments.assertions.Assertions;

/**
 * This is an internal class, used to check arguments passed to the library by clients. This is completely separate from
 * what actual {@link Assertions} use.
 *
 * @author SirWellington
 */
@tech.sirwellington.alchemy.annotations.access.Internal
@NonInstantiable
public final class Checks
{

    private final static Logger LOG = LoggerFactory.getLogger(Checks.class);

    private Checks() throws IllegalAccessException
    {
        throw new IllegalAccessException("not meant to be instantiated");
    }

    @tech.sirwellington.alchemy.annotations.access.Internal
    @NonInstantiable
    public static class Internal
    {

        public static boolean isNullOrEmpty(String string)
        {
            return string == null || string.isEmpty();
        }

        public static boolean isNullOrEmpty(Collection<?> collection)
        {
            return collection == null || collection.isEmpty();
        }

        public static void checkNotNull(Object reference) throws IllegalArgumentException
        {
            checkNotNull(reference, "");
        }

        public static void checkNotNull(Object reference, String message) throws IllegalArgumentException
        {
            checkThat(reference != null, message);
        }

        public static void checkState(boolean predicate, String message) throws IllegalStateException
        {
            if (!predicate)
            {
                throw new IllegalStateException(message);
            }
        }

        public static void checkThat(boolean predicate) throws IllegalArgumentException
        {
            checkThat(predicate, "");
        }

        public static void checkThat(boolean predicate, String message) throws IllegalArgumentException
        {
            if (!predicate)
            {
                throw new IllegalArgumentException(message);
            }
        }

    }

}
