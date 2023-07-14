package io.reflectoring.archunit;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

public class ArchUnitExamples {

    public void referenceDeprecatedClass() {
        Dep dep = new Dep();
    }

    @Deprecated
    public class Dep {

    }

    public void thisMethodCallsTheWrongBigDecimalConstructor() {
        BigDecimal value = new BigDecimal(123.0);

        // BigDecimal value = new BigDecimal("123.0"); // works!
    }

    public void instantiateLocalDatetime() {
        var clock = Clock.systemDefaultZone();
        LocalDateTime localDate = LocalDateTime.now(clock);
        // The below line will fail the ArchUnit test
        // LocalDateTime localDateWrong = LocalDateTime.now();
    }

}
