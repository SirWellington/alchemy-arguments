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

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class DynamicExceptionSupplierTest
{

    private Class<FakeExceptionWithMessage> exceptionClass;
    private String message;

    private DynamicExceptionSupplier<FakeExceptionWithMessage> instance;

    private FailedAssertionException assertionException;

    @Before
    public void setUp()
    {
        message = one(alphabeticString());
        assertionException = new FailedAssertionException(one(alphabeticString()));
        exceptionClass = FakeExceptionWithMessage.class;

        instance = new DynamicExceptionSupplier<>(exceptionClass, message);
    }

    @Test
    public void testApplyWithNoMessageOrCause()
    {
        System.out.println("testApplyWithNoMessageOrCause");

        DynamicExceptionSupplier<FakeException> instance = new DynamicExceptionSupplier<>(FakeException.class, null);

        FakeException result = instance.apply(assertionException);
        assertThat(result, notNullValue());
        assertThat(result.getCause(), nullValue());
        assertThat(result.getMessage(), isEmptyOrNullString());

        instance = new DynamicExceptionSupplier<>(FakeException.class, message);
        result = instance.apply(assertionException);
        assertThat(result, notNullValue());
        assertThat(result.getMessage(), isEmptyOrNullString());
        assertThat(result.getCause(), nullValue());
    }

    @Test
    public void testApplyWithMessage()
    {
        System.out.println("testApplyWithMessage");

        FakeExceptionWithMessage result = instance.apply(assertionException);
        assertThat(result, notNullValue());
        assertThat(result.getCause(), nullValue());
        assertThat(result.getMessage(), is(message));

        result = instance.apply(null);
        assertThat(result, notNullValue());
        assertThat(result.getCause(), nullValue());
        assertThat(result.getMessage(), is(message));

        instance = new DynamicExceptionSupplier<>(FakeExceptionWithMessage.class, null);
        result = instance.apply(assertionException);
        assertThat(result, notNullValue());
        assertThat(result.getCause(), nullValue());
        assertThat(result.getMessage(), isEmptyOrNullString());

    }

    @Test
    public void testApplyWithCause()
    {
        System.out.println("testApplyWithCause");

        DynamicExceptionSupplier<FakeExceptionWithThrowable> instance;
        instance = new DynamicExceptionSupplier<>(FakeExceptionWithThrowable.class, message);

        FakeExceptionWithThrowable result = instance.apply(assertionException);
        assertThat(result, notNullValue());
        assertThat(result.getCause(), notNullValue());
        assertThat(result.getCause(), instanceOf(FailedAssertionException.class));

        result = instance.apply(null);
        assertThat(result, notNullValue());
        assertThat(result.getCause(), nullValue());
        assertThat(result.getMessage(), isEmptyOrNullString());

        instance = new DynamicExceptionSupplier<>(FakeExceptionWithThrowable.class, null);

        result = instance.apply(assertionException);
        assertThat(result, notNullValue());
        assertThat(result.getCause(), is(assertionException));

    }

    @Test
    public void testApplyWithMessageAndCause()
    {
        System.out.println("testApplyWithMessageAndCause");

        DynamicExceptionSupplier<FakeExceptionWithBoth> instance;
        instance = new DynamicExceptionSupplier<>(FakeExceptionWithBoth.class, message);

        FakeExceptionWithBoth result = instance.apply(assertionException);
        assertThat(result, notNullValue());
        assertThat(result.getMessage(), is(message));
        assertThat(result.getCause(), notNullValue());
        assertThat(result.getCause(), instanceOf(FailedAssertionException.class));

        instance = new DynamicExceptionSupplier<>(FakeExceptionWithBoth.class, null);
        result = instance.apply(assertionException);
        assertThat(result, notNullValue());
        assertThat(result.getCause(), notNullValue());
        assertThat(result.getCause(), instanceOf(FailedAssertionException.class));

    }

    @Test
    public void testWhenInvokationFails() throws Exception
    {
        System.out.println("testWhenInvokationFails");

        DynamicExceptionSupplier<FakeExceptionThatThrowsOnConstruct> instance;
        instance = new DynamicExceptionSupplier<>(FakeExceptionThatThrowsOnConstruct.class, message);

        FakeExceptionThatThrowsOnConstruct result = instance.apply(assertionException);
        assertThat(result, nullValue());
    }

    @Test
    public void testToString()
    {
        System.out.println("testToString");

        assertThat(instance.toString(), notNullValue());
        assertThat(instance.toString(), not(isEmptyOrNullString()));

    }

    private static class FakeException extends Exception
    {

        public FakeException()
        {
        }
    }

    private static class FakeExceptionWithMessage extends Exception
    {

        public FakeExceptionWithMessage()
        {
        }

        public FakeExceptionWithMessage(String message)
        {
            super(message);
        }

    }

    private static class FakeExceptionWithThrowable extends Exception
    {

        public FakeExceptionWithThrowable()
        {
        }

        public FakeExceptionWithThrowable(Throwable cause)
        {
            super(cause);
        }

    }

    private static class FakeExceptionWithBoth extends Exception
    {

        public FakeExceptionWithBoth()
        {
        }

        public FakeExceptionWithBoth(String message)
        {
            super(message);
        }

        public FakeExceptionWithBoth(String message, Throwable cause)
        {
            super(message, cause);
        }

        public FakeExceptionWithBoth(Throwable cause)
        {
            super(cause);
        }

    }

    private static class FakeExceptionThatThrowsOnConstruct extends Exception
    {

        public FakeExceptionThatThrowsOnConstruct()
        {
            throw new RuntimeException("This is a test");
        }

    }
}
