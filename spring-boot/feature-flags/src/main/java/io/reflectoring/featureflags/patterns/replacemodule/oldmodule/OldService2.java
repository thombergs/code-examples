package io.reflectoring.featureflags.patterns.replacemodule.oldmodule;

import io.reflectoring.featureflags.patterns.replacemodule.Service2;
import org.springframework.stereotype.Component;

@Component
public class OldService2 implements Service2 {

    @Override
    public int doSomethingElse() {
        return 1;
    }
}
