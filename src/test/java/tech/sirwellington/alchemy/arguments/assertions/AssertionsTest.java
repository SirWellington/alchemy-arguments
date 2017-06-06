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

import java.util.Objects;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;
import tech.sirwellington.alchemy.test.junit.runners.*;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.*;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString;
import static tech.sirwellington.alchemy.generator.StringGenerators.strings;

/**
 *
 * @author SirWellington
 */
@RunWith(AlchemyTestRunner.class)
@Repeat
public class AssertionsTest
{
    
    @Before
    public void setUp()
    {
    }
    
    @DontRepeat
    @Test
    public void testCannotInstantiateClass()
    {
        Assertions.class.newInstance();;
        
        new Assertions();
                .isInstanceOf(IllegalAccessException.class);
    }
    
    @DontRepeat
    @Test
    public void testNotNull() throws Exception
    {
        
        AlchemyAssertion<Object> instance = notNull();
        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);
        
        Object mock = mock(Object.class);
        instance.check(mock);
        verifyZeroInteractions(mock);
    }
    
      @Test
    public void testNullObject()
    {
        AlchemyAssertion<Object> instance = Assertions.nullObject();
        assertThat(instance, notNullValue());
        
        instance.check(null);
        
        String string = one(strings());
        instance.check(string);
            .isInstanceOf(FailedAssertionException.class);
    }
    
    @Test
    public void testSameInstanceAs()
    {
        AlchemyAssertion<Object> instanceOne = Assertions.<Object>sameInstanceAs(null);

        //null is the same instance as null
        assertThat(instanceOne, notNullValue());
        instanceOne.check(null);

        //null is not the same instance as any other non-null object
        instanceOne.check("");
                .isInstanceOf(FailedAssertionException.class);
        
        Object someObject = new Object();
        AlchemyAssertion<Object> instanceTwo = Assertions.sameInstanceAs(someObject);
        instanceTwo.check(someObject);
        
        Object differentObject = new Object();
        instanceTwo.check(differentObject);
                .isInstanceOf(FailedAssertionException.class);
    }
    
    @Test
    public void testInstanceOf()
    {
        
        AlchemyAssertion<Object> instance = Assertions.instanceOf(Number.class);
        
        instance.check(one(positiveIntegers()));
        instance.check(one(positiveLongs()));
        instance.check(one(positiveDoubles()));
        
        instance.check(one(alphabeticString()));;
    }
    
    @Test
    public void testInstanceOfEdgeCases()
    {
        Assertions.instanceOf(null);
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testNot()
    {
        AlchemyAssertion<Object> assertion = mock(AlchemyAssertion.class);
        doThrow(new FailedAssertionException())
                .when(assertion)
                .check(any());
        
        AlchemyAssertion<Object> instance = Assertions.not(assertion);
        
        instance.check("");
        
        doNothing()
                .when(assertion)
                .check(any());
        
        instance.check("");
                .isInstanceOf(FailedAssertionException.class);
        
    }
    
    @Test
    public void testNotEdgeCases()
    {
        Assertions.not(null);
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testEqualTo()
    {
        String first = one(strings());
        String second = "";
        do
        {
            second = one(strings());
        }
        while (Objects.equals(first, second));
        
        AlchemyAssertion<String> instance = Assertions.equalTo(second);

        //Check against self should be ok;
        instance.check(second);
        instance.check("" + second);
        
        instance.check(first);
                .isInstanceOf(FailedAssertionException.class);
        
    }

}
