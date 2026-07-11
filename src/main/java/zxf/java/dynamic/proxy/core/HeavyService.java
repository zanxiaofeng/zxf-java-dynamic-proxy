package zxf.java.dynamic.proxy.core;

/**
 * 模拟一个【初始化昂贵】的服务（例如加载大模型、建立连接池、读大文件）。
 * <p>
 * 注意：cglib 通过【生成子类】来做代理，创建代理实例时必然 super() 调用本类构造器，
 * 所以本类构造器本身不能承载"昂贵初始化"——昂贵逻辑放在 {@link #init()} 里，
 * 由 {@link zxf.java.dynamic.proxy.cglib.pattern3.LazyLoaderDemo} 的 loadObject 首次触发，
 * 这样 LazyLoader"只初始化一次"的价值才体现得出来。
 */
public class HeavyService {

    private boolean initialized = false;

    public HeavyService() {
        System.out.println("HeavyService::ctor（轻量，仅占位）");
    }

    /** 真正昂贵的初始化，刻意延迟。 */
    public HeavyService init() {
        if (!initialized) {
            System.out.println("HeavyService::init —— 模拟昂贵初始化完成");
            initialized = true;
        }
        return this;
    }

    public String work(String input) {
        return "heavy result of " + input;
    }
}
