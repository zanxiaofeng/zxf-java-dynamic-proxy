package zxf.java.dynamic.proxy.core;

/**
 * 业务接口：读操作。
 * JDK 动态代理必须基于接口，所以 JDK 代理的 demo 都会用到这种接口。
 */
public interface IReader {
    String read(String fileName);
}
