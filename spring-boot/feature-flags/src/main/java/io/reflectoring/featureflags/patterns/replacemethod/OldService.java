package io.reflectoring.featureflags.patterns.replacemethod;

import org.springframework.stereotype.Component;

@Component
class OldService implements Service {
    @Override
    public String doSomething() {
        return "old value";
    }

    public int doSomethingElse(){
        return 2;
    }

}
