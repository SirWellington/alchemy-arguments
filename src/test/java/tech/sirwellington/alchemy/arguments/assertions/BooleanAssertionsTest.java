package tech.sirwellington.alchemy.arguments.assertions;

import org.junit.jupiter.api.RepeatedTest;
import tech.sirwellington.alchemy.test.AlchemyTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static tech.sirwellington.alchemy.arguments.TestHelpers.assertThrowsFailedAssertion;
import static tech.sirwellington.alchemy.arguments.assertions.BooleanAssertions.falseStatement;
import static tech.sirwellington.alchemy.arguments.assertions.BooleanAssertions.trueStatement;

@AlchemyTest
final class BooleanAssertionsTest {

    @RepeatedTest(100)
    public void testTrueStatement() {
        var assertion = trueStatement();
        assertThat(assertion, notNullValue());

        assertion.check(true);
        assertThrowsFailedAssertion(() -> assertion.check(false));
        assertThrowsFailedAssertion(() -> assertion.check(null));
    }

    @RepeatedTest(100)
    public void testFalseStatement() {
        var assertion = falseStatement();
        assertThat(assertion, notNullValue());

        assertion.check(false);
        assertThrowsFailedAssertion(() -> assertion.check(true));
        assertThrowsFailedAssertion(() -> assertion.check(null));
    }
}
