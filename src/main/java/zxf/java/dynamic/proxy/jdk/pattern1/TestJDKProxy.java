package zxf.java.dynamic.proxy.jdk.pattern1;

import zxf.java.dynamic.proxy.core.IExecutor;
import zxf.java.dynamic.proxy.core.IReader;
import zxf.java.dynamic.proxy.core.MyExecutor;
import zxf.java.dynamic.proxy.core.MyReader;

/**
 * pattern1 —— JDK 动态代理：给接口方法加日志。
 * <p>
 * 运行后能看到每次方法调用前后都打印了一行日志，
 * 而我们【没有手写任何代理类】，代理对象是 JDK 在运行期用字节码生成的。
 */
public class TestJDKProxy {
    public static void main(String[] args) {
        IReader reader = LoggedProxyFactory.create(new MyReader(), IReader.class);
        System.out.println(reader.read("myFile"));

        IExecutor executor = LoggedProxyFactory.create(new MyExecutor(), IExecutor.class);
        System.out.println(executor.execute("myTask-1"));
    }
}
