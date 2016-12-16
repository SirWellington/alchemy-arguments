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
import tech.sirwellington.alchemy.annotations.arguments.Optional;

/**
 * This class contains commonly used checks on Objects and Strings.
 * <p>
 * This is different from from what actual {@linkplain AlchemyAssertion Assertions} use.
 *
 * @author SirWellington
 */
@NonInstantiable
public final class Checks
{

    private final static Logger LOG = LoggerFactory.getLogger(Checks.class);

    private Checks() throws IllegalAccessException
    {
        throw new IllegalAccessException("not meant to be instantiated");
    }

    
    /**
     * Checks if the argument object is null.
     * 
     * @param object
     * @return true if {@code object} is null, false otherwise.
     * @see #anyAreNull(java.lang.Object...) 
     * @see #allAreNull(java.lang.Object...) 
     */
    public static boolean isNull(@Optional Object object)
    {
        return object == null;
    }
    
    /**
     * Checks if the argument object has a valid reference (it is not null).
     * 
     * @param object
     * @return true if object is not null, false if it is.
     */
    public static boolean notNull(@Optional Object object)
    {
        return !isNull(object);
    }
    
    /**
     * Checks if any of the argument objects are null.
     * 
     * @param objects
     * @return true if any of the argument objects are null, false otherwise.
     */
    public static boolean anyAreNull(Object... objects)
    {
        if (isNull(objects) || objects.length == 0)
        {
            return true;
        }
        
        for (Object object : objects)
        {
            if (isNull(object))
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Checks if all of the objects are null.
     * 
     * @param objects
     * @return true if all the argument objects are null, false otherwise.
     */
    public static boolean allAreNull(Object... objects)
    {
        if (isNull(objects))
        {
            return true;
        }
        
        for (Object object : objects)
        {
            if (!isNull(object))
            {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Checks if the string is {@code null} or {@linkplain String#isEmpty() empty}.
     * 
     * @param string
     * @return true if the string is null, true if it is empty, false otherwise.
     * @see #notNullOrEmpty(java.lang.String) 
     */
    public static boolean isNullOrEmpty(@Optional String string)
    {
        if (isNull(string))
        {
            return true;
        }
        
        return string.isEmpty();
    }
    
    /**
     * 
     * @param string
     * @return 
     * @see #isNullOrEmpty(java.lang.String) 
     */
    public static boolean notNullOrEmpty(@Optional String string)
    {
        return !isNullOrEmpty(string);
    }
    
    /**
     * Checks if any of the argument strings are null or empty. Returns true if even one of the argument
     * strings are null or empty.
     * 
     * @param strings
     * @return 
     * @see #isNullOrEmpty(java.lang.String) 
     * @see #allAreNullOrEmpty(java.lang.String...) 
     */
    public static boolean anyAreNullOrEmpty(@Optional String...strings)
    {
        if (isNull(strings))
        {
            return true;
        }
        
        for (String string : strings)
        {
            if (isNullOrEmpty(string))
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Checks if all of the arguments are null or empty. If even one of the arguments is not 
     * null or empty, than this returns false.
     * 
     * @param strings
     * @return true if all of the argument strings are empty or null, false otherwise.
     * @see #anyAreNullOrEmpty(java.lang.String...) 
     */
    public static boolean allAreNullOrEmpty(@Optional String... strings)
    {
        if (isNull(strings))
        {
            return true;
        }
        
        for (String string : strings)
        {
            if (notNullOrEmpty(string))
            {
                return false;
            }
        }
        
        return true;
    }
    
    @tech.sirwellington.alchemy.annotations.access.Internal
    @NonInstantiable
    public static class Internal
    {

        private Internal() throws IllegalAccessException
        {
            throw new IllegalAccessException("not meant to be instantiated");
        }

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
        
        public static void checkNotNullOrEmpty(String string) throws IllegalArgumentException
        {
            checkThat(!isNullOrEmpty(string));
        }
        
        public static void checkNotNullOrEmpty(String string, String message) throws IllegalArgumentException
        {
            checkThat(!isNullOrEmpty(string), message);
        }

    }

}
