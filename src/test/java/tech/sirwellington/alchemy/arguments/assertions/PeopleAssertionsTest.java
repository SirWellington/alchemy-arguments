/*
 * Copyright 2016 Aroma Tech.
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
import tech.sirwellington.alchemy.generator.PeopleGenerators;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;


/**
 *
 * @author SirWellington
 */
@Repeat(100)
@RunWith(AlchemyTestRunner.class)
public class PeopleAssertionsTest 
{

    private String email;
    private String badEmail;
    
    @Before
    public void setUp() throws Exception
    {
        
        setupData();
        setupMocks();
    }

    private void setupData() throws Exception
    {
        email = one(PeopleGenerators.emails());
        badEmail = one(alphabeticString());
    }

    private void setupMocks() throws Exception
    {

    }
    
    @DontRepeat
    @Test
    public void testConstructor() 
    {
        assertThrows(() -> new PeopleAssertions())
            .isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testValidEmailAddress()
    {
        AlchemyAssertion<String> instance = PeopleAssertions.validEmailAddress();
        assertThat(instance, notNullValue());
        
        instance.check(email);
        
        assertThrows(() -> instance.check(badEmail))
            .isInstanceOf(FailedAssertionException.class);
    }
    
    @DontRepeat
    @Test
    public void testValidEmailAddressWithEmptyArgs()
    {
        AlchemyAssertion<String> instance = PeopleAssertions.validEmailAddress();
        
        assertThrows(() -> instance.check(null))
            .isInstanceOf(FailedAssertionException.class);
        
        assertThrows(() -> instance.check(""))
            .isInstanceOf(FailedAssertionException.class);
    }
    
}