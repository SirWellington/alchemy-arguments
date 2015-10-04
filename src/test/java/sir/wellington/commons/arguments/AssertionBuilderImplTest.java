/*
 * Copyright 2015 Wellington.
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
package sir.wellington.commons.arguments;

import java.sql.SQLException;
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
import static sir.wellington.alchemy.test.DataGenerator.alphabeticString;
import static sir.wellington.alchemy.test.DataGenerator.oneOf;
import static sir.wellington.alchemy.test.junit.ThrowableAssertion.assertThrows;
import static sir.wellington.commons.arguments.AssertionBuilderImpl.checkThat;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class AssertionBuilderImplTest
{

    @Mock
    private Assertion assertion;

    @Mock
    private ExceptionMapper<SQLException> exceptionMapper;

    private String argument;

    private AssertionBuilderImpl<String, FailedAssertionException> instance;

    private FailedAssertionException assertException;

    @Before
    public void setUp()
    {
        argument = oneOf(alphabeticString());

        instance = AssertionBuilderImpl.checkThat(argument);

        assertException = new FailedAssertionException(oneOf(alphabeticString()));
    }

    @Test
    public void testCheckThat()
    {
        System.out.println("testCheckThat");

        Object mockArgument = mock(Object.class);
        instance = checkThat(argument);
        assertThat(instance, notNullValue());
        verifyZeroInteractions(mockArgument);

        instance = checkThat(null);
        assertThat(instance, notNullValue());
    }

    @Test
    public void testUsingExceptionWhenNotWrapped()
    {
        System.out.println("testUsingExceptionWhenNotWrapped");

        when(exceptionMapper.apply(assertException))
                .thenReturn(new SQLException(oneOf(alphabeticString())));

        doThrow(assertException)
                .when(assertion)
                .check(argument);

        assertThrows(() -> instance.usingException(exceptionMapper).is(assertion))
                .isInstanceOf(SQLException.class);

        verify(exceptionMapper).apply(assertException);
        verify(assertion).check(argument);
    }

    @Test
    public void testUsingExceptionWhenWrapped()
    {
        System.out.println("testUsingExceptionWhenWrapped");

        when(exceptionMapper.apply(assertException))
                .thenReturn(new SQLException(oneOf(alphabeticString()), assertException));

        doThrow(assertException)
                .when(assertion)
                .check(argument);

        assertThrows(() -> instance.usingException(exceptionMapper).is(assertion))
                .isInstanceOf(SQLException.class)
                .hasCauseInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testUsingExceptionClass()
    {
        System.out.println("testUsingExceptionClass");

        when(exceptionMapper.apply(assertException))
                .thenReturn(new SQLException());

        doThrow(assertException)
                .when(assertion)
                .check(argument);

        assertThrows(() -> instance.usingException(SQLException.class).is(assertion))
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
    public void testUsingException()
    {
        System.out.println("testUsingException");

        String embeddedExceptionMessage = oneOf(alphabeticString());
        String overrideMessage = oneOf(alphabeticString());

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
}
