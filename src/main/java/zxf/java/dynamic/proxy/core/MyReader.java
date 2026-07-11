package zxf.java.dynamic.proxy.core;

/**
 * IReader 的真实实现（被代理对象 / target）。
 */
public class MyReader implements IReader {
    @Override
    public String read(String fileName) {
        return "Content of " + fileName;
    }
}
