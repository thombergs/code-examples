package io.reflectoring.featureflags.patterns.replacemodule.newmodule;

import io.reflectoring.featureflags.patterns.replacemodule.Service2;
import org.springframework.stereotype.Component;

@Component
public class NewService2 implements Service2 {

    @Override
    public int doSomethingElse() {
        return 42;
    }
}
