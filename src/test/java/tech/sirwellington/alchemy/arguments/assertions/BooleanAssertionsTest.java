/*
 * Copyright 2015 Aroma Tech.
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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@Repeat(10)
@RunWith(AlchemyTestRunner.class)
public class BooleanAssertionsTest 
{

    @Before
    public void setUp()
    {
    }
    
    @DontRepeat
    @Test
    public void testCannotInstantiate()
    {
        assertThrows(() -> BooleanAssertions.class.newInstance())
            .isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testTrueStatement()
    {
        AlchemyAssertion<Boolean> assertion = BooleanAssertions.trueStatement();
        assertThat(assertion, notNullValue());
        
        assertion.check(true);
        
        assertThrows(() -> assertion.check(false))
            .isInstanceOf(FailedAssertionException.class);
        
        assertThrows(() -> assertion.check(null))
            .isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testFalseStatement()
    {
        AlchemyAssertion<Boolean> assertion = BooleanAssertions.falseStatement();
        assertThat(assertion, notNullValue());
        
        assertion.check(false);
        
        assertThrows(() -> assertion.check(true))
            .isInstanceOf(FailedAssertionException.class);
        
        assertThrows(() -> assertion.check(null))
            .isInstanceOf(FailedAssertionException.class);
    }

}