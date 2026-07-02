/*
 * Copyright © 2026. SirWellington.
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

package tech.sirwellington.alchemy.arguments.internal;

import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.arguments.Optional;

import java.util.Collection;

/**
 * This class contains commonly used checks on Objects and Strings.
 *
 * <p>This is different from what actual {@link tech.sirwellington.alchemy.arguments.AlchemyAssertion} use.</p>
 *
 * @author SirWellington
 */
@Internal
public final class Checks {

    private Checks() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class should not be instantiated.");
    }

    /**
     * Checks if the provided string is null or empty.
     */
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Checks if the provided collection is null or empty.
     */
    public static <T> boolean isNullOrEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Ensures the reference is not null, and throws an exception otherwise.
     */
    public static void checkNotNull(Object reference) throws IllegalArgumentException {
        checkThat(reference != null);
    }

    public static void checkNotNull(Object reference, String message) throws IllegalArgumentException {
        checkThat(reference != null, message);
    }

    /**
     * Checks if the predicate is true and throws an IllegalStateException otherwise.
     */
    public static void checkState(boolean predicate, String message) throws IllegalStateException {
        if (!predicate) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * Throws IllegalArgumentException if the predicate is false.
     */
    public static void checkThat(boolean predicate) throws IllegalArgumentException {
        if (!predicate) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkThat(boolean predicate, String message) throws IllegalArgumentException {
        if (!predicate) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks that the string is not null or empty.
     */
    public static void checkNotNullOrEmpty(String string) throws IllegalArgumentException {
        checkThat(!isNullOrEmpty(string));
    }

    public static void checkNotNullOrEmpty(String string, String message) throws IllegalArgumentException {
        checkThat(!isNullOrEmpty(string), message);
    }

    /**
     * Checks if the argument object is null.
     *
     * @param object the object to check
     * @return true if {@code object} is null, false otherwise.
     */
    public static boolean isNull(@Optional Object object) {
        return object == null;
    }

    /**
     * Checks if the argument object has a valid reference (it is not null).
     *
     * @param reference the object to check
     * @return true if object is not null, false if it is.
     */
    public static boolean notNull(@Optional Object reference) {
        return !isNull(reference);
    }

    /**
     * Checks if any of the argument objects are null.
     *
     * @param objects the objects to check (may be null or empty)
     * @return true if {@code objects} is null/empty or any of them are null
     */
    public static boolean anyAreNull(@Optional Object... objects) {
        if (objects == null || objects.length == 0) {
            return true;
        }

        for (Object obj : objects) {
            if (obj == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if all the objects are null.
     *
     * @param objects the objects to check
     * @return true if all of the argument objects are null, false otherwise.
     */
    public static boolean allAreNull(@Optional Object... objects) {
        if (objects == null || objects.length == 0) {
            return true;
        }

        for (Object obj : objects) {
            if (obj != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param string the string to check
     * @return true if {@code string} is not null and not empty, false otherwise.
     */
    public static boolean notNullOrEmpty(@Optional String string) {
        return !isNullOrEmpty(string);
    }

    /**
     * Checks if any of the argument strings are null or empty. Returns true if even one of the
     * argument strings are null or empty.
     *
     * @param strings the strings to check (may be null or empty)
     * @return true if {@code strings} is null, or any element is null/empty
     */
    public static boolean anyAreNullOrEmpty(@Optional String... strings) {
        if (strings == null || strings.length == 0) {
            return true;
        }

        for (String s : strings) {
            if (isNullOrEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if all of the arguments are null or empty. If even one of the arguments is not
     * null or empty, then this returns false.
     *
     * @param strings the strings to check (may be null or empty)
     * @return true if all of the argument strings are null/empty, false otherwise.
     */
    public static boolean allAreNullOrEmpty(@Optional String... strings) {
        if (strings == null) {
            return true;
        }

        for (String s : strings) {
            if (!isNullOrEmpty(s)) {
                return false;
            }
        }
        return true;
    }
}
