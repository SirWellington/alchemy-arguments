package sir.wellington.commons.arguments;

/**
 * Assertions analyze input arguments for validity. You can always supply your own.
 *
 * @param <A> The type of argument an assertion checks
 * 
 * @author SirWellington
 */
@FunctionalInterface
public interface Assertion<A>
{

    /**
     * Checks the argument for validity.
     *
     * @param argument The argument to check
     *
     * @throws FailedAssertionException When the argument-check fails. Note that
     *                                  {@link FailedAssertionException} already extends
     *                                  {@link IllegalArgumentException} Don't throw anything else
     *                                  for assertion check failures.
     */
    void check(A argument) throws FailedAssertionException;

}
