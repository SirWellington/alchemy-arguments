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

/**
 * Assertions analyze input arguments for validity. You can always supply your own.
 *
 * @param <A> The type of argument an assertion checks
 *
 * @author SirWellington
 */
@FunctionalInterface
public interface Assertion<A>
{

    /**
     * Asserts that the argument for validity.
     *
     * @param argument The argument to validate
     *
     * @throws FailedAssertionException When the argument-check fails. Note that
     *                                  {@link FailedAssertionException} already extends
     *                                  {@link IllegalArgumentException}. Any other kinds of
     *                                  exceptions thrown will be wrapped in a
     *                                  {@link FailedAssertionException}.
     */
    void check(A argument) throws FailedAssertionException;

}
