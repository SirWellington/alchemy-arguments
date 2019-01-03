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

@file:JvmName("Checks")

package tech.sirwellington.alchemy.arguments

import tech.sirwellington.alchemy.annotations.access.Internal
import tech.sirwellington.alchemy.annotations.arguments.Optional

/**
 * This class contains commonly used checks on Objects and Strings.
 *
 *
 * This is different from from what actual [Assertions][AlchemyAssertion] use.

 * @author SirWellington
 */

/**
 * Checks if the provided [string] is null or empty
 */
@Internal
internal fun isNullOrEmpty(string: String?): Boolean
{
    return string?.isNullOrEmpty() ?: true
}

/**
 * Checks if the provided [collection] is null or empty
 */
@Internal
internal fun isNullOrEmpty(collection: Collection<*>?): Boolean
{
    return collection?.isEmpty() ?: true
}

/**
 * Ensures the reference is not null, and throws an exception otherwise.
 */
@Throws(IllegalArgumentException::class)
@JvmOverloads
internal fun checkNotNull(reference: Any?, message: String = "")
{
    checkThat(reference != null, message)
}

@Throws(IllegalStateException::class)
@Internal
internal fun checkState(predicate: Boolean, message: String)
{
    if (!predicate)
    {
        throw IllegalStateException(message)
    }
}

@Throws(IllegalArgumentException::class)
@JvmOverloads
@Internal
internal fun checkThat(predicate: Boolean, message: String = "")
{
    if (!predicate)
    {
        throw IllegalArgumentException(message)
    }
}

@Throws(IllegalArgumentException::class)
@Internal
internal fun checkNotNullOrEmpty(string: String?)
{
    checkThat(!isNullOrEmpty(string))
}

@Throws(IllegalArgumentException::class)
@Internal
internal fun checkNotNullOrEmpty(string: String?, message: String)
{
    checkThat(!isNullOrEmpty(string), message)
}

/**
 * Checks if the argument object is null.

 * @param object
 * *
 * @return true if `object` is null, false otherwise.
 * *
 * @see anyAreNull
 * @see allAreNull
 */
@Internal
internal fun isNull(@Optional `object`: Any?): Boolean
{
    return `object` == null
}

/**
 * Checks if the argument object has a valid reference (it is not null).

 * @param object
 *
 * @return true if object is not null, false if it is.
 */
@Internal
internal fun notNull(@Optional reference: Any?): Boolean
{
    return !isNull(reference)
}

/**
 * Checks if any of the argument objects are null.

 * @param objects
 *
 * @return true if any of the argument objects are null, false otherwise.
 */
@Internal
internal fun anyAreNull(vararg objects: Any?): Boolean
{
    if (isNull(objects) || objects.isEmpty())
    {
        return true
    }

    return objects.any { isNull(it) }
}

/**
 * Checks if all of the objects are null.

 * @param objects
 *
 * @return true if all the argument objects are null, false otherwise.
 */
@Internal
internal fun allAreNull(vararg objects: Any?): Boolean
{
    if (isNullOrEmpty(objects.toList()))
    {
        return true
    }

    return objects.all { isNull(it) }
}


/**

 * @param string
 *
 * @return
 *
 * @see isNullOrEmpty
 */
@Internal
internal fun notNullOrEmpty(@Optional string: String?): Boolean
{
    return !isNullOrEmpty(string)
}

/**
 * Checks if any of the argument strings are null or empty. Returns true if even one of the argument
 * strings are null or empty.

 * @param strings
 *
 * @return
 *
 * @see isNullOrEmpty
 * @see allAreNullOrEmpty
 */
@Internal
internal fun anyAreNullOrEmpty(@Optional vararg strings: String?): Boolean
{
    if (isNull(strings))
    {
        return true
    }

    return strings.any { isNullOrEmpty(it) }
}

/**
 * Checks if all of the arguments are null or empty. If even one of the arguments is not
 * null or empty, than this returns false.

 * @param strings
 *
 * @return true if all of the argument strings are empty or null, false otherwise.
 *
 * @see anyAreNullOrEmpty
 */
@Internal
internal fun allAreNullOrEmpty(@Optional vararg strings: String?): Boolean
{
    if (isNull(strings))
    {
        return true
    }

    return strings.all { isNullOrEmpty(it) }
}
