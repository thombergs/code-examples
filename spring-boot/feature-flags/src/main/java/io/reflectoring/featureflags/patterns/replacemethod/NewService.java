package io.reflectoring.featureflags.patterns.replacemethod;

import org.springframework.stereotype.Component;

@Component
class NewService implements Service {
    @Override
    public int doSomething() {
        return 42;
    }

}
