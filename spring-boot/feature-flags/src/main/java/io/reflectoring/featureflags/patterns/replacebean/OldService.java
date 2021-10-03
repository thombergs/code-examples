package io.reflectoring.featureflags.patterns.replacebean;

class OldService implements Service {
    @Override
    public String doSomething() {
        return "old value";
    }
}
