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

import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;
import tech.sirwellington.alchemy.generator.DateGenerators;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@RunWith(AlchemyTestRunner.class)
@Repeat
public class DateAssertionsTest
{

    @Before
    public void setUp()
    {
    }
    
    @DontRepeat
    @Test
    public void testCannotInstantiate()
    {
        assertThrows(() -> new DateAssertions())
            .isInstanceOf(IllegalAccessException.class);
        
        assertThrows(() -> DateAssertions.class.newInstance())
            .isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testInThePast() throws InterruptedException
    {
        Date startTime = new Date();

        AlchemyAssertion<Date> instance = DateAssertions.inThePast();
        assertThat(instance, notNullValue());

        Date past = one(DateGenerators.pastDates());
        instance.check(past);

        Date future = one(DateGenerators.futureDates());
        assertThrows(() -> instance.check(future))
                .isInstanceOf(FailedAssertionException.class);

        Thread.sleep(1);
        //Start time should presentDate be past as well
        instance.check(startTime);
    }

    @Test
    public void testAfter()
    {
        Date referenceDate = new Date();

        AlchemyAssertion<Date> instance = DateAssertions.after(referenceDate);
        assertThat(instance, notNullValue());

        //The reference date is not after itself
        assertThrows(() -> instance.check(referenceDate))
                .isInstanceOf(FailedAssertionException.class);

        //The past is not after the reference date
        Date past = one(DateGenerators.pastDates());
        assertThrows(() -> instance.check(past))
                .isInstanceOf(FailedAssertionException.class);

        //The future should be after the reference date
        Date future = one(DateGenerators.futureDates());
        instance.check(future);

    }

    @Test
    public void testBefore()
    {
        Date referenceDate = new Date();

        AlchemyAssertion<Date> instance = DateAssertions.before(referenceDate);
        assertThat(instance, notNullValue());

        //The reference date is not before itself
        assertThrows(() -> instance.check(referenceDate))
                .isInstanceOf(FailedAssertionException.class);

        //The future is not before the reference date
        Date future = one(DateGenerators.after(referenceDate));
        assertThrows(() -> instance.check(future))
                .isInstanceOf(FailedAssertionException.class);

        //The past is before the reference date
        Date past = one(DateGenerators.before(referenceDate));
        instance.check(past);

    }

    @Test
    public void testInTheFuture()
    {
        Date startTime = new Date();

        AlchemyAssertion<Date> instance = DateAssertions.inTheFuture();
        assertThat(instance, notNullValue());

        //Future is ahead of the presentDate
        Date future = one(DateGenerators.futureDates());
        instance.check(future);

        //Past is behind the presentDate
        Date past = one(DateGenerators.pastDates());
        assertThrows(() -> instance.check(past))
                .isInstanceOf(IllegalArgumentException.class);

        //The start time is not in the future
        assertThrows(() -> instance.check(startTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
