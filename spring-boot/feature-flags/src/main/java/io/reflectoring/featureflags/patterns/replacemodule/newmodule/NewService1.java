package io.reflectoring.featureflags.patterns.replacemodule.newmodule;

import io.reflectoring.featureflags.patterns.replacemodule.Service1;
import org.springframework.stereotype.Component;

@Component
public class NewService1 implements Service1 {
    @Override
    public int doSomething() {
        return 42;
    }
}
