package zxf.java.dynamic.proxy.cglib.parttern1.log;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LogMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object proxied, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        String argsStr = Arrays.stream(objects == null ? new Object[]{} : objects)
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        System.out.println("LogMethodInterceptor::invoke, " + method.getName() + ": " + argsStr);
        return methodProxy.invokeSuper(proxied, objects);

    }
}
