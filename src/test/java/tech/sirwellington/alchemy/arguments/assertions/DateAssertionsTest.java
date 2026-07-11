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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tech.sirwellington.alchemy.generator.DateGenerators;
import tech.sirwellington.alchemy.test.AlchemyTest;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tech.sirwellington.alchemy.arguments.TestHelpers.assertThrowsFailedAssertion;
import static tech.sirwellington.alchemy.arguments.assertions.DateAssertions.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;

/**
 * Tests for {@link DateAssertions}.
 *
 * @author SirWellington
 */
@DisplayName("DateAssertions Tests")
@AlchemyTest
final class DateAssertionsTest {

    @Test
    @DisplayName("testCannotInstantiate: DateAssertions should be non-instantiable")
    void testCannotInstantiate() {
        assertThrows(
            () -> DateAssertions.class.getDeclaredConstructor().newInstance()
        ).isInstanceOf(InvocationTargetException.class)
         .hasCauseInstanceOf(IllegalAccessException.class);
    }

    @Test
    @DisplayName("testInThePast: inThePast assertion works correctly")
    void testInThePast() throws InterruptedException {
        var startTime = new Date();

        var instance = inThePast();
        assertNotNull(instance, "inThePast should return a non-null assertion");

        var past = one(DateGenerators.pastDates());
        instance.check(past);

        var future = one(DateGenerators.futureDates());
        assertThrowsFailedAssertion(() -> instance.check(future));

        Thread.sleep(1);
        instance.check(startTime);
    }

    @Test
    @DisplayName("testAfter: after assertion works correctly")
    void testAfter() {
        var referenceDate = new Date();

        var instance = after(referenceDate);
        assertNotNull(instance, "after should return a non-null assertion");

        assertThrowsFailedAssertion(() -> instance.check(referenceDate));

        var past = one(DateGenerators.pastDates());
        assertThrows(() -> instance.check(past));

        var future = one(DateGenerators.futureDates());
        instance.check(future);
    }

    @Test
    @DisplayName("testBefore: before assertion works correctly")
    void testBefore() {
        var referenceDate = new Date();

        var instance = before(referenceDate);
        assertNotNull(instance, "before should return a non-null assertion");

        assertThrowsFailedAssertion(() -> instance.check(referenceDate));

        var future = one(DateGenerators.futureDates());
        assertThrowsFailedAssertion(() -> instance.check(future));

        var past = one(DateGenerators.pastDates());
        instance.check(past);
    }

    @Test
    @DisplayName("testInTheFuture: inTheFuture assertion works correctly")
    void testInTheFuture() {
        var startTime = new Date();

        var instance = inTheFuture();
        assertNotNull(instance, "inTheFuture should return a non-null assertion");

        var future = one(DateGenerators.futureDates());
        instance.check(future);

        var past = one(DateGenerators.pastDates());
        assertThrowsFailedAssertion(() -> instance.check(past));
        assertThrowsFailedAssertion(() -> instance.check(startTime));
    }

}
