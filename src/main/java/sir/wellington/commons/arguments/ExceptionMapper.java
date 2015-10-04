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

import java.util.function.Function;

/**
 * An {@code ExceptionMapper} decided how to handle a {@link FailedAssertionException}.
 *
 * It can :
 * <ol>
 * <li> Supply a new Exception Type that wraps the {@code cause}
 * <li> Supply a new Exception Type that ignores the {@code cause}
 * <li> Return null, which causes no exception to be thrown, essentially ignoring the assertion.
 * </ol>
 *
 * Behavior #3 may change in the future, as there is no clear use-case for skipping the assertions
 * this way.
 *
 * @author SirWellington
 *
 * @param <Ex>
 */
public interface ExceptionMapper<Ex extends Throwable> extends Function<FailedAssertionException, Ex>
{

    /**
     * This identity instance passes the same {@link FailedAssertionException} thrown by the
     * {@link Assertion}.
     */
    ExceptionMapper<FailedAssertionException> IDENTITY = ex -> ex;

    /**
     * Decide how to map the causing exception. You can either return a new Exception that wraps the
     * causing exception, or ignore it all-together. You can use the {@link #IDENTITY} to just
     * re-throw the {@link FailedAssertionException}.
     *
     * @param cause The exception thrown by the {@link Assertion}
     *
     * @return Never return a null Exception
     *
     * @see #IDENTITY
     */
    @Override
    public Ex apply(FailedAssertionException cause);

}
