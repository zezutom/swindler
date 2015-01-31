package org.zezutom.swindler;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import java.lang.reflect.Method;

/**
 * Created by tom on 30/01/2015.
 */
public class Mock<T> {

    public static<T> T mock(Class<T> clazz) {

        try {
            ProxyFactory factory = new ProxyFactory();
            factory.setSuperclass(clazz);

            Class aClass = factory.createClass();
            final T instance = (T) aClass.newInstance();

            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Class " + clazz.getName() + " couldn't be mocked.");
        }
    }

    public static<T> ReturnValue when(T mock, String methodPattern, Object... params) {
        return new ReturnValue(mock, methodPattern, params);
    }

    static class ReturnValue<T> {

        private T mock;

        private String methodPattern;

        private Object[] params;

        ReturnValue(T mock, String methodPattern, Object... params) {
            this.mock = mock;
            this.methodPattern = methodPattern;
            this.params = params;
        }

        public void thenReturn(final Object value) {
            MethodHandler handler = new MethodHandler() {
                @Override
                public Object invoke(Object self, Method overridden, Method proceed, Object[] args) throws Throwable {
                    boolean isMatch = false;
                    final String signature = overridden.toString();

                    if (signature.endsWith(methodPattern)) {
                        if (params == null || params.length == 0) {
                            isMatch = true;
                        } else {
                            for (int i = 0; i < params.length; i++) {
                                if (i >= args.length) break;

                                if (params[i] == args[i]) {
                                    isMatch = true;
                                    break;
                                }
                            }
                        }
                    }
                    return (isMatch) ? value : proceed.invoke(mock, args);
                }
            };
            ((ProxyObject) mock).setHandler(handler);
        }
    }
}
