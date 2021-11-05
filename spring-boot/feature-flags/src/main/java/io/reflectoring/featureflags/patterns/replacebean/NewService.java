package io.reflectoring.featureflags.patterns.replacebean;

class NewService implements Service {
    @Override
    public int doSomething() {
        return 42;
    }
}
