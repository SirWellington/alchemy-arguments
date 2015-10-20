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

import java.sql.SQLException;
import static java.util.Arrays.asList;
import java.util.List;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import static tech.sirwellington.alchemy.arguments.AssertionBuilderImpl.checkThat;
import static tech.sirwellington.alchemy.arguments.Assertions.nonEmptyString;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.listOf;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
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
        System.out.println("testCheckThat");

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
        System.out.println("testThrowingWhenExceptionIsNotWrapped");

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
        System.out.println("testThrowingWhenExceptionIsWrapped");

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
        System.out.println("testThrowingExceptionClass");

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
        System.out.println("testIsWhenAssertionFails");

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
        System.out.println("testIsWhenAssertionPasses");

        doNothing()
                .when(assertion)
                .check(argument);

        instance.is(assertion);
        verify(assertion).check(argument);
    }

    @Test
    public void testIsWhenAssertionThrowsUnexpectedException()
    {
        System.out.println("testIsWhenAssertionThrowsUnexpectedException");

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
        System.out.println("testUsingMessage");

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
        System.out.println("testChecksWithMultipleArguments");

        arguments = listOf(alphabeticString());
        //No Exceptions expected
        AssertionBuilderImpl.checkThat(arguments)
                .are(nonEmptyString());

        arguments.add("");
        //Test 'is'
        assertThrows(() -> AssertionBuilderImpl.checkThat(arguments).is(nonEmptyString()))
                .isInstanceOf(FailedAssertionException.class);

        //Test 'are' as well
        assertThrows(() -> AssertionBuilderImpl.checkThat(arguments).are(nonEmptyString()))
                .isInstanceOf(FailedAssertionException.class);

    }
}
