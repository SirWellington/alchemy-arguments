package tech.sirwellington.alchemy.arguments.assertions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;
import tech.sirwellington.alchemy.generator.NumberGenerators;
import tech.sirwellington.alchemy.test.AlchemyTest;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AlchemyTest
final class AddressAssertionsTest {

    private static final int ITERATIONS = 100;

    private String zip;
    private String badZip;
    private final Random random = new Random();

    @BeforeAll
    void setUp() {
        setupData();
    }

    private void setupData() {
        int zipVal = one(integers(0, 99999));
        zip = String.format("%05d", zipVal);

        int badZipVal = one(integers(100000, Integer.MAX_VALUE));
        badZip = String.valueOf(badZipVal);
    }

    @Test
    void testCannotInstantiate() {
        assertThrows(() -> AddressAssertions.class.getDeclaredConstructor().newInstance())
            .isInstanceOf(IllegalAccessException.class);
    }

    @RepeatedTest(ITERATIONS)
    void testValidZipCode() {
        var assertion = AddressAssertions.validZipCode();
        assertThat(assertion, notNullValue());

        assertion.check(zip);
    }

    @RepeatedTest(ITERATIONS)
    void testInvalidZipCode() {
        var assertion = AddressAssertions.validZipCode();
        assertThrows(() -> assertion.check(badZip))
                          .isInstanceOf(FailedAssertionException.class);
    }

    @RepeatedTest(ITERATIONS)
    void testValidZipCodeString() {
        var assertion = AddressAssertions.validZipCodeString();
        assertThat(assertion, notNullValue());
        assertion.check(zip);
    }

    @RepeatedTest(ITERATIONS)
    void testValidZipCodeStringWithInvalid() {
        var assertion = AddressAssertions.validZipCodeString();
        assertThrows(() -> assertion.check(badZip))
                          .isInstanceOf(FailedAssertionException.class);
    }
}
