package zxf.java.dynamic.proxy.cglib.parttern1;

import zxf.java.dynamic.proxy.cglib.parttern1.log.LoggedProxyFactory;

public class TestCGLib {
    public static void main(String[] args) {
        MyExecutor myExecutor1 = LoggedProxyFactory.create(MyExecutor.class, new Object[]{});
        System.out.println(myExecutor1.execute("myTask-1"));

        MyExecutor myExecutor2 = LoggedProxyFactory.create(MyExecutor.class, new Object[]{"prefix-"});
        System.out.println(myExecutor2.execute("myTask-2"));
    }
}
