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
import tech.sirwellington.alchemy.test.junit.runners.GenerateInteger;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateInteger.Type.RANGE;

/**
 *
 * @author SirWellington
 */
@Repeat(100)
@RunWith(AlchemyTestRunner.class)
public class AddressAssertionsTest 
{
    
    @GenerateInteger(value = RANGE, min = 0, max = 99_999)
    private Integer zip;
    
    @GenerateInteger(value = RANGE, min = 100_000, max = Integer.MAX_VALUE)
    private Integer badZip;
    

    @Before
    public void setUp() throws Exception
    {
        
        setupData();
    }


    private void setupData() throws Exception
    {
        
    }

    private String zipToString(int zip)
    {
        if (zip < 90_000)
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
        AlchemyAssertion<Integer> assertion = AddressAssertions.validZipCode();
        assertThat(assertion, notNullValue());
        
        assertion.check(zip);
    }

    @Test
    public void testInvalidZipCode()
    {
        AlchemyAssertion<Integer> assertion = AddressAssertions.validZipCode();
        
        assertThrows(() -> assertion.check(badZip)).isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testValidZipCodeString()
    {
        AlchemyAssertion<String> assertion = AddressAssertions.validZipCodeString();
        assertThat(assertion, notNullValue());

        String string = zipToString(zip);
        assertion.check(string);
    }

    @Test
    public void testValidZipCodeStringWithInvalid()
    {
        AlchemyAssertion<String> assertion = AddressAssertions.validZipCodeString();

        String string = zipToString(badZip);
        assertThrows(() -> assertion.check(string));
    }

}