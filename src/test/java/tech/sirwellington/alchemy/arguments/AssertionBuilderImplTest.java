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
package tech.sirwellington.alchemy.arguments;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import tech.sirwellington.alchemy.arguments.assertions.StringAssertions;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static tech.sirwellington.alchemy.arguments.AssertionBuilderImpl.checkThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.listOf;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@Repeat(100)
@RunWith(AlchemyTestRunner.class)
public class AssertionBuilderImplTest
{

    @Mock
    private AlchemyAssertion assertion;

    @Mock
    private ExceptionMapper<SQLException> exceptionMapper;

    private String argument;
    private List<String> arguments;

    private AssertionBuilderImpl<String, FailedAssertionException> instance;

    private FailedAssertionException assertException;

    @Before
    public void setUp()
    {
        argument = one(alphabeticString());
        arguments = asList(argument);

        instance = AssertionBuilderImpl.checkThat(arguments);

        assertException = new FailedAssertionException(one(alphabeticString()));
    }

    @Test
    public void testCheckThat()
    {
        Object mockArgument = mock(Object.class);
        instance = checkThat(arguments);
        assertThat(instance, notNullValue());
        verifyZeroInteractions(mockArgument);

        instance = checkThat(null);
        assertThat(instance, notNullValue());
    }

    @Test
    public void testThrowingWhenExceptionIsNotWrapped()
    {
        when(exceptionMapper.apply(assertException))
                .thenReturn(new SQLException(one(alphabeticString())));

        doThrow(assertException)
                .when(assertion)
                .check(argument);

        assertThrows(() -> instance.throwing(exceptionMapper).is(assertion))
                .isInstanceOf(SQLException.class);

        verify(exceptionMapper).apply(assertException);
        verify(assertion).check(argument);
    }

    @Test
    public void testThrowingWhenExceptionIsWrapped()
    {
        when(exceptionMapper.apply(assertException))
                .thenReturn(new SQLException(one(alphabeticString()), assertException));

        doThrow(assertException)
                .when(assertion)
                .check(argument);

        assertThrows(() -> instance.throwing(exceptionMapper).is(assertion))
                .isInstanceOf(SQLException.class)
                .hasCauseInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testThrowingExceptionClass()
    {
        when(exceptionMapper.apply(assertException))
                .thenReturn(new SQLException());

        doThrow(assertException)
                .when(assertion)
                .check(argument);

        assertThrows(() -> instance.throwing(SQLException.class).is(assertion))
                .isInstanceOf(SQLException.class)
                .hasCauseInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testIsWhenAssertionFails() throws Exception
    {
        doThrow(assertException)
                .when(assertion)
                .check(argument);

        assertThrows(() -> instance.is(assertion))
                .isInstanceOf(FailedAssertionException.class)
                .hasMessage(assertException.getMessage());
    }

    @Test
    public void testIsWhenAssertionPasses() throws Exception
    {
        doNothing()
                .when(assertion)
                .check(argument);

        instance.is(assertion);
        verify(assertion).check(argument);
    }

    @Test
    public void testIsWhenAssertionThrowsUnexpectedException()
    {
        doThrow(new RuntimeException())
                .when(assertion)
                .check(argument);

        assertThrows(() -> instance.is(assertion))
                .isInstanceOf(FailedAssertionException.class)
                .hasCauseInstanceOf(RuntimeException.class);

    }

    @Test
    public void testUsingMessage()
    {
        String embeddedExceptionMessage = one(alphabeticString());
        String overrideMessage = one(alphabeticString());

        doThrow(new FailedAssertionException(embeddedExceptionMessage))
                .when(assertion)
                .check(argument);

        assertThrows(() -> instance.is(assertion))
                .isInstanceOf(FailedAssertionException.class)
                .hasMessage(embeddedExceptionMessage);

        assertThrows(() -> instance.usingMessage(overrideMessage).is(assertion))
                .isInstanceOf(FailedAssertionException.class)
                .hasMessage(overrideMessage);

    }

    @Test
    public void testChecksWithMultipleArguments()
    {
        arguments = listOf(alphabeticString());
        //No Exceptions expected
        AssertionBuilderImpl.checkThat(arguments)
                .are(StringAssertions.nonEmptyString());

        arguments.add("");
        //Test 'is'
        assertThrows(() -> AssertionBuilderImpl.checkThat(arguments).is(StringAssertions.nonEmptyString()))
                .isInstanceOf(FailedAssertionException.class);

        //Test 'are' as well
        assertThrows(() -> AssertionBuilderImpl.checkThat(arguments).are(StringAssertions.nonEmptyString()))
                .isInstanceOf(FailedAssertionException.class);

    }
    
    @Test
    public void testOverrideMessagePreservedWithCustomException()
    {
        doThrow(FailedAssertionException.class)
                .when(assertion)
                .check(argument);
        
        String overrideMessage = one(alphabeticString());
        
        AssertionBuilder<String, IOException> newInstance = instance.usingMessage(overrideMessage)
            .throwing(IOException.class);
        
        assertThrows(() -> newInstance.is(assertion))
            .isInstanceOf(IOException.class)
            .hasMessage(overrideMessage);
    }
    
    @Test
    public void testOverrideMessagePreservedWithCustomExceptionReversed()
    {
        doThrow(FailedAssertionException.class)
                .when(assertion)
                .check(argument);
        
        String overrideMessage = one(alphabeticString());
        
        AssertionBuilder<String, IOException> newInstance = instance.throwing(IOException.class)
            .usingMessage(overrideMessage);
        
        assertThrows(() -> newInstance.is(assertion))
            .isInstanceOf(IOException.class)
            .hasMessage(overrideMessage);
    }
}
