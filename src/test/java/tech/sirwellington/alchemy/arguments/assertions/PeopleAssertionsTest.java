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


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.sirwellington.alchemy.generator.PeopleGenerators;
import tech.sirwellington.alchemy.test.AlchemyTest;
import tech.sirwellington.alchemy.test.generation.GenerateString;

import static tech.sirwellington.alchemy.arguments.TestHelpers.assertThrowsFailedAssertion;
import static tech.sirwellington.alchemy.arguments.assertions.PeopleAssertions.validEmailAddress;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.StringGenerators.strings;
import static tech.sirwellington.alchemy.test.generation.GenerateString.Type.ALPHABETIC;

/**
 * @author SirWellington
 */
@AlchemyTest
final class PeopleAssertionsTest {

    @GenerateString(ALPHABETIC)
    private String badEmail;

    private String email;

    @BeforeEach
    void setUp() {
        email = one(PeopleGenerators.emailAddresses());
    }

    @Test
    void testValidEmailAddress() {
        // Given
        var instance = validEmailAddress();
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var badEmails = strings();
        var goodEmails = PeopleGenerators.emailAddresses();
        // Then
        Tests.runTests(instance, badEmails, goodEmails);
    }

    @Test
    void testValidEmailAddressWithEmptyArgs() {
        var instance = validEmailAddress();

        assertThrowsFailedAssertion(() ->instance.check(null));
        assertThrowsFailedAssertion(() ->instance.check(""));
    }

}