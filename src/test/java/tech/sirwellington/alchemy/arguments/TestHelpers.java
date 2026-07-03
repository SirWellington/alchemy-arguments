package tech.sirwellington.alchemy.arguments;

import tech.sirwellington.alchemy.annotations.arguments.Required;
import tech.sirwellington.alchemy.arguments.internal.Checks;
import tech.sirwellington.alchemy.test.ExceptionOperation;
import tech.sirwellington.alchemy.test.ThrowableAssertion;

import java.util.List;

import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;

public class TestHelpers {
    public static ThrowableAssertion assertThrowsFailedAssertion(ExceptionOperation op) {
        return ThrowableAssertion.assertThrows(op).isInstanceOf(FailedAssertionException.class);
    }

    @Required
    public static <E> E randomElementFrom(@Required List<E> list) {
        Checks.checkNotNullOrEmpty(list);
        var index = one(integers(0, list.size()));
        return list.get(index);
    }
}
