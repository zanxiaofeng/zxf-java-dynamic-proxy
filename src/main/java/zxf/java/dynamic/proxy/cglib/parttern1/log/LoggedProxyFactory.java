package zxf.java.dynamic.proxy.cglib.parttern1.log;

import net.sf.cglib.proxy.Enhancer;

import java.util.Arrays;
import java.util.stream.Collectors;

public class LoggedProxyFactory {
    public static <T> T create(Class<T> classType, Object[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(classType);
        enhancer.setCallback(new LogMethodInterceptor());
        Class[] argTypes = Arrays.stream(args)
                .map(Object::getClass)
                .collect(Collectors.toList())
                .toArray(new Class[]{});
        return (T) enhancer.create(argTypes, args);
    }
}
