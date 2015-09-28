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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the main entry-point for the Library. From here you can do:
 *
 * <pre>
 * {@code
 * checkThat(zipCode)
 * .usingException(ex -> new InvalidZipCodeException(zipCode))
 * .is(notNull())
 * .is(positiveInteger())
 * .is(intAtLeast(10000))
 * .is(intAtMost(99999));
 * }
 * </pre>
 *
 * @author SirWellington
 */
public final class Arguments
{

    private final static Logger LOG = LoggerFactory.getLogger(Arguments.class);

    private Arguments()
    {
    }

    /**
     * Begin assertions on an argument.
     *
     * @param <Argument> The type of the argument
     * @param argument   The argument itself
     *
     * @return An object that allows building assertions on the argument.
     */
    public static <Argument> AssertionBuilder<Argument, FailedAssertionException> checkThat(Argument argument)
    {
        return AssertionBuilderImpl.checkThat(argument);
    }

}
