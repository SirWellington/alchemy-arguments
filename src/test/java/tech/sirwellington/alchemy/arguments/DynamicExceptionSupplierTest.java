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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString;

/**
 *
 * @author SirWellington
 */
@Repeat(25)
@RunWith(AlchemyTestRunner.class)
public class DynamicExceptionSupplierTest
{

    private Class<FakeExceptionWithMessage> exceptionClass;
    private String overrideMessage;

    private DynamicExceptionSupplier<FakeExceptionWithMessage> instance;

    private FailedAssertionException assertionException;

    @Before
    public void setUp()
    {
        overrideMessage = one(alphabeticString());
        assertionException = new FailedAssertionException(one(alphabeticString()));
        exceptionClass = FakeExceptionWithMessage.class;

        instance = new DynamicExceptionSupplier<>(exceptionClass, overrideMessage);
    }

    @Test
    public void testApplyWithNoMessageOrCause()
    {
        DynamicExceptionSupplier<FakeException> instance = new DynamicExceptionSupplier<>(FakeException.class, null);

        FakeException result = instance.apply(assertionException);
        assertThat(result, notNullValue());
        assertThat(result.getCause(), nullValue());
        assertThat(result.getMessage(), isEmptyOrNullString());

        instance = new DynamicExceptionSupplier<>(FakeException.class, overrideMessage);
        result = instance.apply(assertionException);
        assertThat(result, notNullValue());
        assertThat(result.getMessage(), isEmptyOrNullString());
        assertThat(result.getCause(), nullValue());
    }

    @Test
    public void testApplyWithMessage()
    {
        FakeExceptionWithMessage result = instance.apply(assertionException);
        assertThat(result, notNullValue());
        assertThat(result.getCause(), nullValue());
        assertThat(result.getMessage(), is(overrideMessage));

        result = instance.apply(null);
        assertThat(result, notNullValue());
        assertThat(result.getCause(), nullValue());
        assertThat(result.getMessage(), is(overrideMessage));

        instance = new DynamicExceptionSupplier<>(FakeExceptionWithMessage.class, null);
        result = instance.apply(assertionException);
        assertThat(result, notNullValue());
        assertThat(result.getCause(), nullValue());
        assertThat(result.getMessage(), is(assertionException.getMessage()));

    }

    @Test
    public void testApplyWithCause()
    {
        DynamicExceptionSupplier<FakeExceptionWithThrowable> instance;
        instance = new DynamicExceptionSupplier<>(FakeExceptionWithThrowable.class, overrideMessage);

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
        DynamicExceptionSupplier<FakeExceptionWithBoth> instance;
        instance = new DynamicExceptionSupplier<>(FakeExceptionWithBoth.class, overrideMessage);

        FakeExceptionWithBoth result = instance.apply(assertionException);
        assertThat(result, notNullValue());
        assertThat(result.getMessage(), is(overrideMessage));
        assertThat(result.getCause(), notNullValue());
        assertThat(result.getCause(), instanceOf(FailedAssertionException.class));

        instance = new DynamicExceptionSupplier<>(FakeExceptionWithBoth.class, null);
        result = instance.apply(assertionException);
        assertThat(result, notNullValue());
        assertThat(result.getCause(), notNullValue());
        assertThat(result.getCause(), instanceOf(FailedAssertionException.class));

    }

    @Test
    public void testApplyWithCauseButNoMessageConstructor() throws Exception
    {
        String expectedMessage = assertionException.getMessage();

        DynamicExceptionSupplier<FakeExceptionWithMessage> instance;
        instance = new DynamicExceptionSupplier<>(FakeExceptionWithMessage.class, "");

        FakeExceptionWithMessage result = instance.apply(assertionException);

        assertThat(result, notNullValue());
        assertThat(result.getMessage(), is(expectedMessage));
        assertThat(result.getCause(), nullValue());
    }

    @Test
    public void testWhenInvocationFails() throws Exception
    {
        DynamicExceptionSupplier<FakeExceptionThatThrowsOnConstruct> instance;
        instance = new DynamicExceptionSupplier<>(FakeExceptionThatThrowsOnConstruct.class, overrideMessage);

        FakeExceptionThatThrowsOnConstruct result = instance.apply(assertionException);
        assertThat(result, nullValue());
    }
   
    @Test
    public void testGetExceptionClass()
    {
        Class<FakeExceptionWithMessage> result = instance.getExceptionClass();
        assertThat(result, is(sameInstance(exceptionClass)));
    }

    @Test
    public void testToString()
    {
        assertThat(instance.toString(), notNullValue());
        assertThat(instance.toString(), not(isEmptyOrNullString()));

    }

    //Private Fake Exception types used for Testing.
    @Internal
    private static class FakeException extends Exception
    {

        public FakeException()
        {
        }
    }

    @Internal
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

    @Internal
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

    @Internal
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

    @Internal
    private static class FakeExceptionThatThrowsOnConstruct extends Exception
    {

        public FakeExceptionThatThrowsOnConstruct()
        {
            throw new RuntimeException("This is a test");
        }

    }
}
