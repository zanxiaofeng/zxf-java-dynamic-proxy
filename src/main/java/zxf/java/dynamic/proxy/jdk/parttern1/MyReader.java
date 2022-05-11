package zxf.java.dynamic.proxy.jdk.parttern1;

public class MyReader implements IReader {
    @Override
    public String read(String fileName) {
        return "Content of " + fileName;
    }
}
