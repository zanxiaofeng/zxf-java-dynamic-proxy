package zxf.java.dynamic.proxy.jdk.parttern1.log;

import java.lang.reflect.Proxy;

public class LoggedProxyFactory {
    public static <T> T create(Object proxied, Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(LoggedProxyFactory.class.getClassLoader(),
                new Class[]{interfaceType}, new LogInvocationHandler(proxied));
    }
}
