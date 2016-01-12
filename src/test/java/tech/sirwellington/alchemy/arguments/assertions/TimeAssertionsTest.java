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

import java.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;
import tech.sirwellington.alchemy.generator.TimeGenerators;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.negativeIntegers;
import static tech.sirwellington.alchemy.generator.TimeGenerators.futureInstants;
import static tech.sirwellington.alchemy.generator.TimeGenerators.pastInstants;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@Repeat
@RunWith(AlchemyTestRunner.class)
public class TimeAssertionsTest
{

    @Before
    public void setUp()
    {
    }
    
    @DontRepeat
    @Test
    public void testCannotInstantiate()
    {
        assertThrows(() -> new TimeAssertions())
            .isInstanceOf(IllegalAccessException.class);
        
        assertThrows(() -> TimeAssertions.class.newInstance())
            .isInstanceOf(IllegalAccessException.class);
    }
    
    @Test
    public void testInThePast() throws InterruptedException
    {
        Instant startTime = Instant.now();

        AlchemyAssertion<Instant> instance = TimeAssertions.inThePast();
        assertThat(instance, notNullValue());

        // The past is indeed in the past
        Instant past = TimeGenerators.pastInstants().get();
        instance.check(past);
        
        //The recent past should be fine too
        Instant recentPast = Instant.now().minusMillis(1);
        instance.check(recentPast);

        //The futureInstants is not in the pastInstants
        Instant future = TimeGenerators.futureInstants().get();
        assertThrows(() -> instance.check(future))
                .isInstanceOf(FailedAssertionException.class);

        Thread.sleep(1);
        //The start time should now be in the past
        instance.check(startTime);

    }

    @Test
    public void testBefore()
    {
        Instant startTime = Instant.now();

        AlchemyAssertion<Instant> instance = TimeAssertions.before(startTime);
        assertThat(instance, notNullValue());

        //The start time is not before itself
        assertThrows(() -> instance.check(startTime))
                .isInstanceOf(FailedAssertionException.class);

        //The past is before the present
        Instant past = one(TimeGenerators.pastInstants());
        instance.check(past);

        //The future is not before the present
        Instant future = one(TimeGenerators.futureInstants());
        assertThrows(() -> instance.check(future))
                .isInstanceOf(FailedAssertionException.class);

        //Edge case
        assertThrows(() -> TimeAssertions.before(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testInTheFuture()
    {
        Instant startTime = Instant.now();

        AlchemyAssertion<Instant> instance = TimeAssertions.inTheFuture();
        assertThat(instance, notNullValue());

        //The start time is not in the future
        assertThrows(() -> instance.check(startTime))
                .isInstanceOf(FailedAssertionException.class);

        //The future is indeed in the future
        Instant future = one(TimeGenerators.futureInstants());
        instance.check(future);

        //The past is not in the future
        Instant past = one(TimeGenerators.pastInstants());
        assertThrows(() -> instance.check(past))
                .isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testAfter()
    {
        Instant startTime = Instant.now();

        AlchemyAssertion<Instant> instance = TimeAssertions.after(startTime);
        assertThat(instance, notNullValue());

        //The start time is not after itself
        assertThrows(() -> instance.check(startTime))
                .isInstanceOf(FailedAssertionException.class);

        //The future is indeed after the start time
        Instant future = one(TimeGenerators.futureInstants());
        instance.check(future);

        //The past is not after the start time
        Instant past = one(TimeGenerators.pastInstants());
        assertThrows(() -> instance.check(past))
                .isInstanceOf(FailedAssertionException.class);

        //Edge case
        assertThrows(() -> TimeAssertions.after(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testRightNow()
    {
        AlchemyAssertion<Instant> assertion = TimeAssertions.rightNow();
        assertThat(assertion, notNullValue());
        
        assertion.check(Instant.now());
        
        Instant recentPast = Instant.now().minusMillis(50);
        assertThrows(() -> assertion.check(recentPast))
            .isInstanceOf(FailedAssertionException.class);
        
        
        Instant past = one(pastInstants());
        assertThrows(() -> assertion.check(past))
            .isInstanceOf(FailedAssertionException.class);
        
        Instant future = one(futureInstants());
        assertThrows(() -> assertion.check(future))
            .isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testNowWithinDelta()
    {
        long marginOfErrorInMillis = 10L;
        
        AlchemyAssertion<Instant> assertion = TimeAssertions.nowWithinDelta(marginOfErrorInMillis);
        assertThat(assertion, notNullValue());
        
        assertion.check(Instant.now());
        
        Instant past = one(pastInstants());
        assertThrows(() -> assertion.check(past))
            .isInstanceOf(FailedAssertionException.class);
        
        Instant future = one(futureInstants());
        assertThrows(() -> assertion.check(future))
            .isInstanceOf(FailedAssertionException.class);
    }
    
    @DontRepeat
    @Test
    public void testNowWithinDeltaWithBadArguments()
    {
        int negative = one(negativeIntegers());
        assertThrows(() -> TimeAssertions.nowWithinDelta(negative))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testEpochRightNow()
    {
        AlchemyAssertion<Long> assertion = TimeAssertions.epochRightNow();
        assertThat(assertion, notNullValue());
        
        assertion.check(Instant.now().toEpochMilli());
        
        Instant past = one(pastInstants());
        assertThrows(() -> assertion.check(past.toEpochMilli()))
            .isInstanceOf(FailedAssertionException.class);
        
        Instant future = one(futureInstants());
        assertThrows(() -> assertion.check(future.toEpochMilli()))
            .isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testEpochNowWithinDelta()
    {
        long marginOfErrorInMillis = 10L;

        AlchemyAssertion<Long> assertion = TimeAssertions.epochNowWithinDelta(marginOfErrorInMillis);
        assertThat(assertion, notNullValue());
        
        assertion.check(Instant.now().toEpochMilli());
        
        Instant past = one(pastInstants());
        assertThrows(() -> assertion.check(past.toEpochMilli()))
            .isInstanceOf(FailedAssertionException.class);
        
        Instant future = one(futureInstants());
        assertThrows(() -> assertion.check(future.toEpochMilli()))
            .isInstanceOf(FailedAssertionException.class);
    }

    @DontRepeat
    @Test
    public void testEpochNowWithinDeltaBadArguments()
    {
        int negative = one(negativeIntegers());
        assertThrows(() -> TimeAssertions.epochNowWithinDelta(negative))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
