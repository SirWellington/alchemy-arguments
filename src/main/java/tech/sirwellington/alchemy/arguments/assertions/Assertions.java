/*
 * Copyright © 2026. Sir Wellington.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package tech.sirwellington.alchemy.arguments.assertions;

import java.util.List;
import java.util.Objects;

import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.annotations.arguments.Optional;
import tech.sirwellington.alchemy.annotations.arguments.Required;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;

import static tech.sirwellington.alchemy.arguments.internal.Checks.*;

/**
 * Common {@link AlchemyAssertion Alchemy Asssertions}.
 *
 * @author SirWellington
 */
@NonInstantiable
public final class Assertions {

    private Assertions() throws IllegalAccessException {
        throw new IllegalAccessException("cannot directly instantiate");
    }

    /**
     * Asserts the argument is not null.
     *
     * @param <A> The type to check.
     * @return a chainable assertion
     */
    public static <A> AlchemyAssertion<A> notNull() {
        return reference -> {
            if (reference == null) {
                failAssertion("Argument is null");
            }
        };
    }

    /**
     * Asserts that the argument is null.
     * This is the opposite of {@link #notNull()}.
     *
     * @param <A> The type to check.
     * @return a chainable assertion
     */
    public static <A> AlchemyAssertion<A> nullObject() {
        return reference -> {
            if (reference != null) {
                failAssertion("Expecting null argument but instead: [{0}]", reference);
            }
        };
    }

    /**
     * Asserts that the argument is the same instance as {@link other}. This assertion uses {@code ==}.
     *
     * @param other The object to compare against.
     * @param <A>   The type of the object to check.
     * @return A chainable assertion.
     */
    public static <A> AlchemyAssertion<A> sameInstanceAs(@Optional A other) {
        return arg -> {
            if (arg == null && other == null) {
                return;
            }

            if (arg != other) {
                failAssertion("Expected {0} to be the same instance as {1}", arg, other);
            }
        };
    }

    /**
     * Asserts that an argument is an {@code instanceof} the specified class.
     * The comparison is done using {@link Class#isInstance(Object)}. The comparison respects the
     * inheritance hierarchy, so that:
     * {@snippet :
     * Integer obj = 5;
     * checkThat(obj).is(instanceOf(Integer.class)); // true
     * checkThat(obj).is(instanceOf(Number.class)); // true
     * checkThat(obj).is(instanceOf(Object.class)); // true
     * checkThat(obj).is(instanceOf(String.class)); // false - fails the assertion
     *}
     *
     * @param clazz The class to check against.
     * @param <A>   The type of the object.
     * @return A chainable assertion
     */
    public static <A> AlchemyAssertion<A> instanceOf(@Required Class<?> clazz) {
        checkNotNull(clazz, "class parameter cannot be null");
        return obj -> {
            notNull().check(obj);
            if (!clazz.isInstance(obj)) {
                failAssertion("Expected Object [{0}] to be of type [{1}]", obj, clazz);
            }
        };
    }

    /**
     * Asserts that the argument is {@link Objects#equals(Object, Object)}  equal to} {@link other}.
     *
     * @param other The object to compare against.
     * @param <A>   Type of the argument.
     * @return A chainable assertion.
     */
    public static <A> AlchemyAssertion<A> equalTo(@Optional A other) {
        return arg -> {
            if (!Objects.equals(arg, other)) {
                failAssertion("Expected [{0}] to be equal to [{1}]", arg, other);
            }
        };
    }

    /**
     * Runs the inverse of another {@link AlchemyAssertion}. This allows you to creat an expression such as:
     * {@snippet :
     * import static tech.sirwellington.alchemy.arguments.assertions.StringAssertions.stringWithWhitespace;
     *
     * var filename = "./text-file.txt";
     * checkThat(filename)
     *   .is(not(stringWithWhitespace()))
     *   .is(not(equalTo("info.txt")));
     *}
     *
     * @param assertion The assertion to negate.
     * @param <A>       Type of the object being checked.
     * @return A chainable assertion.
     */
    public static <A> AlchemyAssertion<A> not(@Required AlchemyAssertion<A> assertion) {
        checkNotNull(assertion, "assertion cannot be null");
        return arg -> {
            try {
                assertion.check(arg);
            } catch (FailedAssertionException _) {
                return;
            }
            failAssertion("not() assertion failed. Expected [{0}] to fail, but it passed", assertion);
        };
    }

    public static <A> AlchemyAssertion<A> and(@Required AlchemyAssertion<A> other) {
        checkNotNull(other, "assertion cannot be null");

        return arg -> {

        };
    }

    /**
     * Combines multiple {@link AlchemyAssertion Assertions} into one.
     * For example, a {@code validAge} assertion could be constructed dynamically using:
     * {@snippet :
     * AlchemyAssertion<Integer> validAge = combine(List.of(
     *   notNull(),
     *   positiveInteger(),
     *   greaterThanOrEqualTo(10),
     *   lessThanOrEqualTo(140)
     * ));
     *
     * var age = user.getAge();
     * checkThat(age)is(validAge);
     *}
     * <br>
     * This allows you to <strong>combine and store</strong> multiple {@link AlchemyAssertion assertions} that
     * are frequently used together to perform argument checks.
     *
     * @param assertions The list assertions to include.
     * @param <Argument> The type of the argument being checked.
     * @return A chainable assertion.
     */
    public static <Argument> AlchemyAssertion<Argument> combine(
        @Required List<AlchemyAssertion<Argument>> assertions
    ) {
        checkNotNullOrEmpty(assertions, "assertions cannot be null or empty");

        return argument -> {
            for (var assertion : assertions) {
                assertion.check(argument);
            }
        };
    }

}