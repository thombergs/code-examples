package io.reflectoring.archunit;

import java.math.BigDecimal;

public class ArchUnitExamples {

    public void reference_deprecated_class() {
        Dep dep = new Dep();
    }

    @Deprecated
    public class Dep {

    }

    public void this_method_calls_the_wrong_BigDecimal_constructor() {
        BigDecimal value = new BigDecimal(123.0);

        // BigDecimal value = new BigDecimal("123.0"); // works!
    }

}
