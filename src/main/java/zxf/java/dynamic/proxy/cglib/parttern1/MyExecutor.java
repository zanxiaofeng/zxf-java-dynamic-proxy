package zxf.java.dynamic.proxy.cglib.parttern1;

public class MyExecutor {
    private String prefix = "";

    public MyExecutor() {
        System.out.println("MyExecutor::Ctor");
    }

    public MyExecutor(String prefix) {
        this.prefix = prefix;
        System.out.println("MyExecutor::Ctor " + prefix);
    }

    public String execute(String taskId) {
        return "Finished of " + prefix + taskId;
    }
}
