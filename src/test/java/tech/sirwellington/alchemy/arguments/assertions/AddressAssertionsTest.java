/*
 * Copyright 2016 RedRoma, Inc..
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
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;

/**
 *
 * @author SirWellington
 */
@Repeat(100)
@RunWith(AlchemyTestRunner.class)
public class AddressAssertionsTest 
{
    
    private String zip;
    
    private String badZip;
    
    

    @Before
    public void setUp() throws Exception
    {
        
        setupData();
    }


    private void setupData() throws Exception
    {
        zip = zipToString(one(integers(0, 100_000)));
        badZip = zipToString(one(integers(100_000, Integer.MAX_VALUE)));
    }

    private String zipToString(int zip)
    {
        if (zip < 99_999)
        {
            return String.format("%05d", zip);
        }
        else 
        {
            return String.valueOf(zip);
        }
    }

    @Test
    public void testValidZipCode()
    {
        AlchemyAssertion<String> assertion = AddressAssertions.validZipCode();
        assertThat(assertion, notNullValue());
        
        assertion.check(zip);
    }

    @Test(expected = FailedAssertionException.class)
    public void testInvalidZipCode()
    {
        AlchemyAssertion<String> assertion = AddressAssertions.validZipCode();
        
        assertion.check(badZip);
    }

    @Test
    public void testValidZipCodeString()
    {
        AlchemyAssertion<String> assertion = AddressAssertions.validZipCodeString();
        assertThat(assertion, notNullValue());

        assertion.check(zip);
    }

    @Test
    public void testValidZipCodeStringWithInvalid()
    {
        AlchemyAssertion<String> assertion = AddressAssertions.validZipCodeString();

        assertion.check(badZip);;
    }

}