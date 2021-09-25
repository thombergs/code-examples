package io.reflectoring.featureflags.patterns.replacebean;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class FeatureFlaggedBean<T> implements FactoryBean<T> {

    private final Class<T> targetClass;
    private final FeatureFlagEvaluation featureFlagEvaluation;
    private final T beanWhenTrue;
    private final T beanWhenFalse;

    public FeatureFlaggedBean(Class<T> targetClass, FeatureFlagEvaluation featureFlagEvaluation, T beanWhenTrue, T beanWhenFalse) {
        this.targetClass = targetClass;
        this.featureFlagEvaluation = featureFlagEvaluation;
        this.beanWhenTrue = beanWhenTrue;
        this.beanWhenFalse = beanWhenFalse;
    }

    @Override
    public T getObject() {

        InvocationHandler invocationHandler = (proxy, method, args) -> {
            if (featureFlagEvaluation.evaluate()) {
                return method.invoke(beanWhenTrue, args);
            } else {
                return method.invoke(beanWhenFalse, args);
            }
        };

        Object proxy = Proxy.newProxyInstance(targetClass.getClassLoader(), new Class[]{targetClass}, invocationHandler);

        return (T) proxy;
    }

    @Override
    public Class<?> getObjectType() {
        return targetClass;
    }
}
