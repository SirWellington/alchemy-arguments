/*
 * Copyright 2015 Sir Wellington.
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
package sir.wellington.alchemy.arguments;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static sir.wellington.alchemy.test.DataGenerator.alphabeticString;
import static sir.wellington.alchemy.test.DataGenerator.oneOf;
import static sir.wellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class ArgumentsTest
{
    
    private Object argument;
    
    @Before
    public void setUp()
    {
        argument = oneOf(alphabeticString());
    }
    
    @Test
    public void testConstructorThrows()
    {
        System.out.println("testConstructorThrows");
        
        assertThrows(() -> Arguments.class.newInstance())
                ;
    }
    
    @Test
    public void testCheckThat()
    {
        System.out.println("testCheckThat");
        
        AssertionBuilder<Object, FailedAssertionException> instance = Arguments.checkThat(argument);
        assertThat(instance, notNullValue());
    }
    
}
