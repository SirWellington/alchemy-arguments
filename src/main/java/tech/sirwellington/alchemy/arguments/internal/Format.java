package tech.sirwellington.alchemy.arguments.internal;

import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;

import static java.text.MessageFormat.format;

@Internal
@NonInstantiable
public class Format {

    private Format() throws IllegalAccessException {
        throw new IllegalAccessException("cannot directly instantiate");
    }

}
