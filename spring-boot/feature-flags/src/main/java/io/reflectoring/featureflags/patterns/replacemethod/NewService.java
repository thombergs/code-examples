package io.reflectoring.featureflags.patterns.replacemethod;

import org.springframework.stereotype.Component;

@Component
class NewService implements Service {
    @Override
    public String doSomething() {
        return "new value";
    }

}
