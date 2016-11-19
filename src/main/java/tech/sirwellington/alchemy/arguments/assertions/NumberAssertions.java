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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.Checks;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;

import static java.lang.Math.abs;

/**
 *
 * @author SirWellington
 */
@NonInstantiable
public final class NumberAssertions
{

    private final static Logger LOG = LoggerFactory.getLogger(NumberAssertions.class);

    NumberAssertions() throws IllegalAccessException
    {
        throw new IllegalAccessException("cannot instantiate");
    }

    /**
     * Asserts that an integer is {@code >} the supplied value.
     *
     * @param exclusiveLowerBound The argument must be {@code > exclusiveLowerBound}.
     *
     * @return
     */
    public static AlchemyAssertion<Integer> greaterThan(int exclusiveLowerBound)
    {
        Checks.Internal.checkThat(exclusiveLowerBound != Integer.MAX_VALUE, "Integers cannot exceed " + Integer.MAX_VALUE);
        return (Integer integer) ->
        {
            Assertions.notNull().check(integer);
            if (integer <= exclusiveLowerBound)
            {
                throw new FailedAssertionException("Number must be > " + exclusiveLowerBound);
            }
        };
    }

    /**
     * Asserts that a long is {@code > exclusiveLowerBound}.
     *
     * @param exclusiveLowerBound The argument must be {@code >} this value.
     *
     * @return
     */
    public static AlchemyAssertion<Long> greaterThan(long exclusiveLowerBound)
    {
        Checks.Internal.checkThat(exclusiveLowerBound != Long.MAX_VALUE, "Longs cannot exceed " + Long.MAX_VALUE);
        return (Long number) ->
        {
            Assertions.notNull().check(number);
            if (number <= exclusiveLowerBound)
            {
                throw new FailedAssertionException("Number must be > " + exclusiveLowerBound);
            }
        };
    }
    
    /**
     * Asserts that a double is {@code > exclusiveLowerBound}, with no allowable margin of error.
     * 
     * @param exclusiveLowerBound The argument is expected to be {@code >} this value.
     * 
     * @return 
     */
    public static AlchemyAssertion<Double> greaterThan(double exclusiveLowerBound)
    {
        return greaterThan(exclusiveLowerBound, 0.0);
    }
    
    /**
     * Asserts that a double is {@code > exclusiveLowerBound} within {@code delta} margin of error.
     * 
     * @param exclusiveLowerBound The argument is expected to be {@code >} this value.
     * @param delta The allowable margin of error for the {@code >} operation.
     * 
     * @return 
     */
    public static AlchemyAssertion<Double> greaterThan(double exclusiveLowerBound, double delta)
    {
        Checks.Internal.checkThat(exclusiveLowerBound < Double.MAX_VALUE, "Doubles cannot exceed " + Double.MAX_VALUE);
        
        return number ->
        {
            Assertions.notNull().check(number);
            
            boolean isWithinBounds = number + abs(delta) > exclusiveLowerBound;
            if (!isWithinBounds)
            {
                throw new FailedAssertionException("Number must be > " + exclusiveLowerBound + " +- " + delta);
            }
        };
    }
    

    /**
     * Asserts that an integer is {@code >=} the supplied value.
     *
     * @param inclusiveLowerBound The argument integer must be {@code >= inclusiveLowerBound}
     *
     * @return
     */
    public static AlchemyAssertion<Integer> greaterThanOrEqualTo(int inclusiveLowerBound)
    {
        return (Integer number) ->
        {
            Assertions.notNull().check(number);
            if (number < inclusiveLowerBound)
            {
                throw new FailedAssertionException("Number must be greater than or equal to " + inclusiveLowerBound);
            }
        };
    }

    /**
     * Asserts that a long is {@code >= inclusiveLowerBound}.
     *
     * @param inclusiveLowerBound The argument integer must be {@code >= inclusiveUpperBound}
     *
     * @return
     */
    public static AlchemyAssertion<Long> greaterThanOrEqualTo(long inclusiveLowerBound)
    {
        return (Long number) ->
        {
            Assertions.notNull().check(number);
            if (number < inclusiveLowerBound)
            {
                throw new FailedAssertionException("Number must be greater than or equal to " + inclusiveLowerBound);
            }
        };
    }
    
    /**
     * Asserts that a double is {@code >= inclusiveLowerBound} with no allowable margin of error.
     * 
     * @param inclusiveLowerBound The argument double must be {@code >= inclusiveLowerBound}
     * 
     * @return 
     */
    public static AlchemyAssertion<Double> greaterThanOrEqualTo(double inclusiveLowerBound)
    {
        return greaterThanOrEqualTo(inclusiveLowerBound, 0.0);
    }
    
    /**
     * Asserts that a double is {@code >= inclusiveLowerBound} within {@code delta} margin-of-error.
     * 
     * @param inclusiveLowerBound The argument double must be {@code >= inclusiveLowerBound} within the margin of error.
     * @param delta The allowable margin-of-error for the {@code >= } comparison.
     * 
     * @return 
     */
    public static AlchemyAssertion<Double> greaterThanOrEqualTo(double inclusiveLowerBound, double delta)
    {
        return (Double number) ->
        {
            Assertions.notNull().check(number);
            
            boolean isWithinBounds = number + abs(delta) >= inclusiveLowerBound;
            if(!isWithinBounds)
            {
                throw new FailedAssertionException("Number must be >= " + inclusiveLowerBound + " +- " + delta);
            }
        };
    }

    /**
     * Asserts that an integer is positive, or {@code > 0}
     *
     * @return
     */
    public static AlchemyAssertion<Integer> positiveInteger()
    {
        return (Integer number) ->
        {
            Assertions.notNull().check(number);
            if (number <= 0)
            {
                throw new FailedAssertionException("Expected positive integer: " + number);
            }
        };
    }
    
    /**
     * Asserts that an integer is negative, or {@code < 0}.
     * 
     * @return 
     */
    public static AlchemyAssertion<Integer> negativeInteger()
    {
        return lessThan(0);
    }

    /**
     * Asserts that an integer is {@code <=} the supplied value.
     *
     * @param inclusiveUpperBound The argument must be {@code <= inclusiveUpperBound}.
     *
     * @return
     */
    public static AlchemyAssertion<Integer> lessThanOrEqualTo(int inclusiveUpperBound)
    {
        return (Integer number) ->
        {
            Assertions.notNull().check(number);
            if (number > inclusiveUpperBound)
            {
                throw new FailedAssertionException("Number must be less than or equal to " + inclusiveUpperBound);
            }
        };
    }

    /**
     * Asserts that a long is {@code <=} the supplied value.
     *
     * @param inclusiveUpperBound The argument must be {@code <= inclusiveUpperBound}.
     *
     * @return
     */
    public static AlchemyAssertion<Long> lessThanOrEqualTo(long inclusiveUpperBound)
    {
        return (Long number) ->
        {
            Assertions.notNull().check(number);
            if (number > inclusiveUpperBound)
            {
                throw new FailedAssertionException("Number must be less than or equal to " + inclusiveUpperBound);
            }
        };
    }
    
    /**
     * Asserts that a double is {@code <=} the supplied value, with no allowable margin-of-error.
     *
     * @param inclusiveUpperBound The argument must be {@code <= inclusiveUpperBound}.
     *
     * @return
     */
    public static AlchemyAssertion<Double> lessThanOrEqualTo(double inclusiveUpperBound)
    {
        return lessThanOrEqualTo(inclusiveUpperBound, 0.0);
    }
    
    /**
     * Asserts that a double is {@code <=} the supplied value, within a {@code delta} margin-of-error.
     * 
     * @param inclusiveUpperBound
     * @param delta The allowable margin-of-error in the {@code <= } comparison
     *
     * @return
     */
    public static AlchemyAssertion<Double> lessThanOrEqualTo(double inclusiveUpperBound, double delta)
    {
        return number ->
        {
            Assertions.notNull().check(number);
            
            boolean isWithinBounds = number - abs(delta) <= inclusiveUpperBound;
            if(!isWithinBounds)
            {
                throw new FailedAssertionException("Number must be <= " + inclusiveUpperBound + " +- " + delta);
            }
        };
    }

    /**
     * Asserts that a Long is positive, or {@code > 0}
     *
     * @return
     */
    public static AlchemyAssertion<Long> positiveLong()
    {
        return (Long number) ->
        {
            Assertions.notNull().check(number);
            if (number <= 0)
            {
                throw new FailedAssertionException("Expected positive long: " + number);
            }
        };
    }
    
    /**
     * Asserts that a Long is negative, or {@code < 0}.
     * 
     * @return 
     */
    public static AlchemyAssertion<Long> negativeLong()
    {
        return lessThan(0L);
    }

    /**
     * Asserts than an integer is {@code <} the supplied value.
     *
     * @param exclusiveUpperBound The argument must be {@code < exclusiveUpperBound}.
     *
     * @return
     */
    public static AlchemyAssertion<Integer> lessThan(int exclusiveUpperBound)
    {
        Checks.Internal.checkThat(exclusiveUpperBound != Integer.MIN_VALUE, "Ints cannot be less than " + Integer.MIN_VALUE);
        return (Integer number) ->
        {
            Assertions.notNull().check(number);
            if (number >= exclusiveUpperBound)
            {
                throw new FailedAssertionException("Number must be < " + exclusiveUpperBound);
            }
        };
    }

    /**
     * Asserts than a long is {@code <} the supplied value.
     *
     * @param exclusiveUpperBound The argument must be {@code < exclusiveUpperBound}.
     *
     * @return
     */
    public static AlchemyAssertion<Long> lessThan(long exclusiveUpperBound)
    {
        Checks.Internal.checkThat(exclusiveUpperBound != Long.MIN_VALUE, "Longs cannot be less than " + Long.MIN_VALUE);
        return (Long number) ->
        {
            Assertions.notNull().check(number);
            if (number >= exclusiveUpperBound)
            {
                throw new FailedAssertionException("Number must be < " + exclusiveUpperBound);
            }
        };
    }
  
    /**
     * Asserts that a double is {@code <} the supplied value, with no allowable margin-of-error.
     * 
     * @param exclusiveUpperBound The argument must be {@code < exclusiveUpperBound}
     * 
     * @return 
     */
    public static AlchemyAssertion<Double> lessThan(double exclusiveUpperBound)
    {
        return lessThan(exclusiveUpperBound, 0.0);
    }
    
    /**
     * Asserts that a double is {@code <} the supplied value, within {@code delta} margin-of-error.
     * 
     * @param exclusiveUpperBound The argument must be {@code < exclusiveUpperBound}.
     * @param delta The allowable margin-of-error.
     * 
     * @return 
     */
    public static AlchemyAssertion<Double> lessThan(double exclusiveUpperBound, double delta)
    {
        Checks.Internal.checkThat(exclusiveUpperBound > -Double.MAX_VALUE, "Doubles cannot be less than " + -Double.MAX_VALUE);
        
        return (Double number) ->
        {
            Assertions.notNull().check(number);
            
            boolean isWithinBounds = number - abs(delta) < exclusiveUpperBound;
            if(!isWithinBounds)
            {
                throw new FailedAssertionException("Number must be < " + exclusiveUpperBound);
            }
        };
    }

    /**
     * Asserts that an integer argument is in the specified (inclusive) range.
     *
     * @param min The lower bound for the range, inclusive
     * @param max The upper bound for the range, inclusive
     *
     * @return
     *
     * @throws IllegalArgumentException If {@code min >= max}. {@code min} should always be less than {@code max}.
     */
    public static AlchemyAssertion<Integer> numberBetween(int min, int max) throws IllegalArgumentException
    {
        Checks.Internal.checkThat(min < max, "Minimum must be less than Max.");
        return (Integer number) ->
        {
            Assertions.notNull().check(number);
            if (number < min || number > max)
            {
                String message = String.format("Expected a number between %d and %d but got %d instead", min, max, number);
                throw new FailedAssertionException(message);
            }
        };
    }

    /**
     * Asserts that a long argument is in the specified (inclusive) range.
     *
     * @param min The lower bound for the range, inclusive
     * @param max The upper bound for the range, inclusive
     *
     * @return
     *
     * @throws IllegalArgumentException If {@code min >= max}. {@code min} should always be less
     */
    public static AlchemyAssertion<Long> numberBetween(long min, long max) throws IllegalArgumentException
    {
        Checks.Internal.checkThat(min < max, "Minimum must be less than Max.");
        return (Long number) ->
        {
            Assertions.notNull().check(number);
            if (number < min || number > max)
            {
                String message = String.format("Expected a number between %d and %d but got %d instead", min, max, number);
                throw new FailedAssertionException(message);
            }
        };
    }

}
