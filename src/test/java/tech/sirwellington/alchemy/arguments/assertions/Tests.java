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

package tech.sirwellington.alchemy.arguments.assertions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;

/**
 *
 * @author SirWellington
 */
@Internal
@NonInstantiable
public class Tests
{

    private final static Logger LOG = LoggerFactory.getLogger(Tests.class);

    public static <T> void runTests(AlchemyAssertion<T> assertion,
                                    AlchemyGenerator<T> badArguments,
                                    AlchemyGenerator<T> goodArguments)
    {
        assertThat(assertion, notNullValue());

        T badArgument = one(badArguments);
        assertion.check(badArgument);
                .isInstanceOf(FailedAssertionException.class);

        T goodArgument = one(goodArguments);
        assertion.check(goodArgument);
    }

    public static void checkForNullCase(AlchemyAssertion assertion)
    {
        assertion.check(null);
                .isInstanceOf(FailedAssertionException.class)
                .containsInMessage("null");
    }
}
