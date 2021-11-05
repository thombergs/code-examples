package io.reflectoring.featureflags.patterns.replacemethod;

import org.springframework.stereotype.Component;

@Component
class OldService implements Service {
    @Override
    public int doSomething() {
        return 1;
    }

    public int doSomethingElse(){
        return 2;
    }

}
