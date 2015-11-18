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

import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.negativeIntegers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.smallPositiveIntegers;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString;
import static tech.sirwellington.alchemy.generator.StringGenerators.strings;
import static tech.sirwellington.alchemy.generator.StringGenerators.stringsFromFixedList;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@Repeat(5_000)
@RunWith(AlchemyTestRunner.class)
public class StringAssertionsTest
{

    @Before
    public void setUp()
    {
    }

    @DontRepeat
    @Test
    public void testCannotInstantiateClass()
    {
        assertThrows(() -> StringAssertions.class.newInstance());

        assertThrows(() -> new StringAssertions())
                .isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testAnEmptyString()
    {
        AlchemyAssertion<String> instance = StringAssertions.anEmptyString();

        AlchemyGenerator<String> badArguments = alphabeticString();
        AlchemyGenerator<String> goodArguments = stringsFromFixedList(null, "");

        Tests.runTests(instance, badArguments, goodArguments);
    }

    @Test
    public void testAStringWithLengthGreaterThan()
    {

        int minAccepted = one(integers(2, 10_100));
        AlchemyAssertion<String> instance = StringAssertions.aStringWithLengthGreaterThan(minAccepted);

        AlchemyGenerator<String> badArguments = () ->
        {
            int length = one(integers(1, minAccepted));
            return one(alphabeticString(length));
        };

        AlchemyGenerator<String> goodArguments = () ->
        {
            int length = minAccepted + one(smallPositiveIntegers());
            return one(alphabeticString(length));
        };

        Tests.runTests(instance, badArguments, goodArguments);

        badArguments = strings(minAccepted);
        Tests.runTests(instance, badArguments, goodArguments);
    }

    @Test
    public void testAStringWithLengthGreaterThanEdgeCases()
    {
        assertThrows(() -> StringAssertions.aStringWithLengthGreaterThan(Integer.MAX_VALUE))
                .isInstanceOf(IllegalArgumentException.class);

        int badArgument = one(integers(Integer.MIN_VALUE, 1));
        assertThrows(() -> StringAssertions.aStringWithLengthGreaterThan(badArgument))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testAStringWithLengthLessThan()
    {
        int upperBound = one(integers(2, 1_000));
        AlchemyAssertion<String> instance = StringAssertions.aStringWithLengthLessThan(upperBound);

        AlchemyGenerator<String> badArguments = () ->
        {
            int length = one(integers(upperBound + 1, upperBound * 2));
            return one(strings(length));
        };

        AlchemyGenerator<String> goodArugments = () ->
        {
            int length = one(integers(1, upperBound));
            return one(strings(length));
        };

        Tests.runTests(instance, badArguments, goodArugments);
    }

    @Test
    public void testAStringWithLengthLessThanEdgeCases()
    {
        int badArgument = one(integers(Integer.MIN_VALUE, 1));
        assertThrows(() -> StringAssertions.aStringWithLengthLessThan(badArgument))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testAStringThatMatches()
    {
        String letter = one(alphabeticString()).substring(0, 1);
        Pattern pattern = Pattern.compile(".*" + letter + ".*");
        AlchemyAssertion<String> instance = StringAssertions.aStringThatMatches(pattern);
        AlchemyGenerator<String> badArguments = () -> alphabeticString().get().replaceAll(letter, "");
        AlchemyGenerator<String> goodArguments = () -> alphabeticString().get() + letter;
        Tests.runTests(instance, badArguments, goodArguments);
    }

    @Test
    public void testAStringThatMatchesEdgeCases()
    {
        assertThrows(() -> StringAssertions.aStringThatMatches(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testANonEmptyString() throws Exception
    {
        AlchemyAssertion<String> instance = StringAssertions.aNonEmptyString();
        assertThat(instance, notNullValue());

        String arg = one(alphabeticString());
        instance.check(arg);

        assertThrows(() -> instance.check(""))
                .isInstanceOf(FailedAssertionException.class);

        assertThrows(() -> instance.check(null))
                .isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testAStringWithLength() throws Exception
    {
        assertThrows(() -> StringAssertions.aStringWithLength(one(negativeIntegers())))
                .isInstanceOf(IllegalArgumentException.class);

        int expectedLength = one(integers(5, 25));
        AlchemyAssertion<String> instance = StringAssertions.aStringWithLength(expectedLength);
        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        String arg = one(alphabeticString(expectedLength));
        instance.check(arg);

        String tooShort = one(alphabeticString(expectedLength - 1));
        assertThrows(() -> instance.check(tooShort)).
                isInstanceOf(FailedAssertionException.class);

        String tooLong = one(strings(expectedLength + 1));
        assertThrows(() -> instance.check(tooLong))
                .isInstanceOf(FailedAssertionException.class);

    }

    @Test
    public void testAStringWithLengthEdgeCases() throws Exception
    {
        int badArgument = one(negativeIntegers());

        assertThrows(() -> StringAssertions.aStringWithLength(badArgument))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testAStringWithLengthGreaterThanOrEqualToWithBadArgs() throws Exception
    {
        int negativeNumber = one(negativeIntegers());

        assertThrows(() -> StringAssertions.aStringWithLengthGreaterThanOrEqualTo(negativeNumber))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testAStringWithLengthGreaterThanOrEqualTo() throws Exception
    {
        int expectedSize = one(integers(10, 100));

        AlchemyAssertion<String> instance = StringAssertions.aStringWithLengthGreaterThanOrEqualTo(expectedSize);
        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        String goodString = one(strings(expectedSize));
        instance.check(goodString);

        int amountToAdd = one(integers(1, 5));
        String anotherGoodString = one(strings(expectedSize + amountToAdd));
        instance.check(anotherGoodString);

        int amountToSubtract = one(integers(1, 5));
        String badString = one(strings(expectedSize - amountToSubtract));
        assertThrows(() -> instance.check(badString))
                .isInstanceOf(FailedAssertionException.class);

    }

    @Test
    public void testAStringWithLengthLessThanOrEqualToWithBadArgs() throws Exception
    {
        int negativeNumber = one(negativeIntegers());

        assertThrows(() -> StringAssertions.aStringWithLengthLessThanOrEqualTo(negativeNumber))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testAStringWithLengthLessThanOrEqualTo() throws Exception
    {
        int expectedSize = one(integers(5, 100));
        AlchemyAssertion<String> instance = StringAssertions.aStringWithLengthLessThanOrEqualTo(expectedSize);

        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        AlchemyGenerator<String> goodStrings = strings(expectedSize);

        int amountToAdd = one(integers(5, 10));
        AlchemyGenerator<String> badStrings = strings(expectedSize + amountToAdd);

        Tests.runTests(instance, badStrings, goodStrings);
    }

    @Test
    public void testAStringWithNoWhitespace() throws Exception
    {
        AlchemyAssertion<String> instance = StringAssertions.aStringWithNoWhitespace();
        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        AlchemyGenerator<String> goodString = alphabeticString();
        AlchemyGenerator<String> badStrings = () -> goodString 
                                                    + one(stringsFromFixedList(" ", "\n", "\t"))
                                                    + goodString;

        Tests.runTests(instance, badStrings, goodString);
    }

    @Test
    public void testAStringWithLengthBetween() throws Exception
    {
        int minimumLength = one(integers(10, 100));
        int maximumLength = one(integers(minimumLength + 1, 1_000));

        AlchemyAssertion<String> instance = StringAssertions.aStringWithLengthBetween(minimumLength, maximumLength);
        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        AlchemyGenerator<String> tooShort = () ->
        {
            //Sad cases
            int stringTooShortLength = minimumLength - one(integers(1, 9));
            String stringTooShort = one(strings(stringTooShortLength));
            return stringTooShort;
        };

        AlchemyGenerator<String> tooLong = () ->
        {
            int stringTooLongLength = maximumLength + one(smallPositiveIntegers());
            String stringTooLong = one(strings(stringTooLongLength));
            return stringTooLong;
        };

        AlchemyGenerator<String> goodStrings = () ->
        {
            int length = one(integers(minimumLength, maximumLength));
            return one(strings(length));
        };

        Tests.runTests(instance, tooLong, goodStrings);

        goodStrings = strings(minimumLength);
        Tests.runTests(instance, tooShort, goodStrings);

    }

    @Test
    public void testAStringWithLengthBetweenEdgeCases() throws Exception
    {

        int minimumLength = one(integers(10, 100));
        int maximumLength = one(integers(minimumLength, 1_000));

        assertThrows(() -> StringAssertions.aStringWithLengthBetween(maximumLength, minimumLength))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> StringAssertions.aStringWithLengthBetween(-minimumLength, maximumLength))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testAStringBeginningWith()
    {
        String string = one(strings(20));
        String prefix = one(strings(4));

        AlchemyAssertion<String> instance = StringAssertions.aStringBeginningWith(prefix);

        //Happy Cases
        instance.check(prefix + string);
        instance.check(prefix);

        //Sad Cases
        assertThrows(() -> instance.check(null))
                .isInstanceOf(FailedAssertionException.class);

        assertThrows(() -> instance.check(string))
                .isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testAStringBeginningWithEdgeCases()
    {
        assertThrows(() -> StringAssertions.aStringBeginningWith(null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> StringAssertions.aStringBeginningWith(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
