package zxf.java.dynamic.proxy.jdk.parttern1;

import zxf.java.dynamic.proxy.jdk.parttern1.log.LoggedProxyFactory;

public class TestJDK {
    public static void main(String[] args) {
        IReader myReader = LoggedProxyFactory.create(new MyReader(), IReader.class);
        System.out.println(myReader.read("myFile"));

        IExecutor myExecutor = LoggedProxyFactory.create(new MyExecutor(), IExecutor.class);
        System.out.println(myExecutor.execute("myTask-1"));
    }
}
