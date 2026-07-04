/*
 * Copyright © 2026. Sir Wellington.
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

package tech.sirwellington.alchemy.arguments.assertions;

import org.junit.jupiter.api.Assertions;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tech.sirwellington.alchemy.arguments.TestHelpers.assertThrowsFailedAssertion;

/**
 *
 * @author SirWellington
 */
@Internal
@NonInstantiable
public final class Tests {

    private Tests() {
        throw new IllegalStateException("cannot instantiate");
    }

    public static <T> void runTests(AlchemyAssertion<T> assertion,
                                    AlchemyGenerator<T> badArguments,
                                    AlchemyGenerator<T> goodArguments) {
        assertNotNull(assertion, "assertion should be non-null");

        var badArgument = badArguments.get();
        assertThrowsFailedAssertion(() -> assertion.check(badArgument));

        var goodArgument = goodArguments.get();
        Assertions.assertDoesNotThrow(() -> assertion.check(goodArgument));
    }

    public static void checkForNullCase(AlchemyAssertion<?> assertion) {
        assertThrowsFailedAssertion(() -> assertion.check(null));
    }
}
