package tech.sirwellington.alchemy.arguments;

import tech.sirwellington.alchemy.test.ExceptionOperation;
import tech.sirwellington.alchemy.test.ThrowableAssertion;

public class TestHelpers {
    public static ThrowableAssertion assertThrowsFailedAssertion(ExceptionOperation op) {
        return ThrowableAssertion.assertThrows(op).isInstanceOf(FailedAssertionException.class);
    }
}
