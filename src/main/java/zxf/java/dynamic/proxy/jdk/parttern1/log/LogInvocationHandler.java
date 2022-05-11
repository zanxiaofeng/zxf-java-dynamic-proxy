package zxf.java.dynamic.proxy.jdk.parttern1.log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LogInvocationHandler implements InvocationHandler {
    private Object proxied;

    public LogInvocationHandler(Object proxied) {
        this.proxied = proxied;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String argsStr = Arrays.stream(args == null ? new Object[]{} : args)
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        System.out.println("LogInvocationHandler::invoke, " + method.getName() + ": " + argsStr);
        return method.invoke(proxied, args);
    }
}
