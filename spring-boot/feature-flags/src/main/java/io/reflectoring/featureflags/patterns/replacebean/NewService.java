package io.reflectoring.featureflags.patterns.replacebean;

class NewService implements Service {
    @Override
    public String doSomething() {
        return "new value";
    }
}
