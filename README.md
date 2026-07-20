# zxf-java-dynamic-proxy

Java 动态代理（Dynamic Proxy）专题 demo，覆盖 **JDK 代理 / cglib / ByteBuddy** 三种实现、它们的能力对比与边界，以及落地的 **AOP 声明式增强**缩影。

- **JDK**：21
- **构建**：Maven（`mvn clean test` 全绿，21 个 JUnit 测试）

---

## 一、这个项目 demo 了什么

动态代理的核心思想：**在运行期生成一个"包装"目标对象的代理类，在不修改目标代码的前提下，对方法调用做统一增强**（日志、计时、事务、缓存、权限、远程调用……）。它是 Spring AOP、MyBatis Mapper、RPC Stub、Hibernate 懒加载等框架的底层基石。

本项目用同一组业务类，演示：

| 主题 | 包 | 说明 |
|------|-----|------|
| JDK 动态代理基础 | `jdk/pattern1` | `Proxy` + `InvocationHandler`，基于**接口** |
| JDK 异常兜底 / 返回值改写 | `jdk/pattern2` | 代理对**异常**和**返回值**的控制力 |
| JDK 环绕计时 / 方法过滤 | `jdk/pattern3` | pointcut 雏形（哪些方法增强） |
| cglib 基础 | `cglib/pattern1` | `Enhancer` + `MethodInterceptor`，基于**子类**，可代理无接口类 |
| cglib CallbackFilter | `cglib/pattern2` | 同一代理的不同方法路由到不同 Callback |
| cglib FixedValue / LazyLoader | `cglib/pattern3` | 固定返回、懒加载（虚拟代理） |
| 本质对比 / final 边界 / 性能 | `comparison` | JDK vs cglib 的机制、能力边界、性能实测 |
| ByteBuddy 现代实现 | `bytebuddy` | cglib 的工业级替代（Mockito、Hibernate 等框架的默认字节码实现） |
| AOP 实战缩影 | `aop` | 注解驱动 = 声明式增强（迷你 Spring AOP） |

---

## 二、三种实现的本质对比

|  | JDK 动态代理 | cglib | ByteBuddy |
|---|---|---|---|
| 机制 | 运行期生成**实现接口**的类 | 生成目标类的**子类** | 生成目标类的**子类**（也支持接口/重定义） |
| 要求 | 目标必须实现接口 | 目标类可继承（非 final） | 同 cglib |
| final 方法 | 不适用 | 拦截不到 | 拦截不到 |
| final 类 | 不适用 | 无法代理 | 无法代理（子类方案都怕 final） |
| 调用方式 | 反射 `Method.invoke` | `MethodProxy`（FastClass） | 注解绑定（`@SuperCall` 等） |
| JDK 16+ 兼容 | 原生 | 需要 `--add-opens` | 原生 |
| 工业地位 | JDK 自带 | 经典，渐被替代 | 现代主流 |

> 运行 `comparison/NatureComparison`、`comparison/FinalBoundary`、`comparison/PerformanceComparison` 可直观看到上述差异。

---

## 三、目录结构

```
src/main/java/zxf/java/dynamic/proxy/
├── core/        公共目标类（被代理对象），所有 pattern 复用
│   ├── IReader / MyReader                  接口 + 实现
│   ├── IExecutor / MyExecutor
│   ├── ICalculator / CalculatorImpl        含 divide 抛异常，演示异常增强
│   ├── Calculator                          无接口类（体现"无需接口"）
│   ├── PrefixExecutor                      无接口 + 构造器参数
│   ├── HeavyService                        演示 LazyLoader 的昂贵初始化
│   ├── WithFinalMethod / FinalClass        final 边界
├── jdk/        JDK 动态代理（pattern1~3）
├── cglib/      cglib 动态代理（pattern1~3）
├── comparison/ 本质对比 / final 边界 / 性能对比
├── bytebuddy/  ByteBuddy 现代实现
└── aop/        @Logged/@Timed 注解 → 声明式增强
```

每个 pattern 都带一个可运行的 `main`，例如：

```bash
mvn exec:java -Dexec.mainClass=zxf.java.dynamic.proxy.aop.TestAOP
```

---

## 四、最佳实践（本项目刻意体现的要点）

### 1. 选型优先看"能力需求"，而非性能
- 要代理的对象**有接口** → JDK 代理足够（零额外依赖）。
- 要代理的对象**没有接口**，或需要 per-method 多回调策略 → cglib / ByteBuddy。
- 新项目优先 **ByteBuddy**：JDK 16+ 原生可用、无需 `--add-opens`、API 更现代。cglib 仍可用但维护停滞。

> ⚠️ **破除迷思**："cglib 一定比 JDK 快"是过时认知。实测 JDK 21 上 cglib 3.3.0 经常**慢于** JDK 代理（见 `comparison/PerformanceComparison`）。选型不应由微小性能差决定。

### 2. 记住 final / private 的边界
- 子类方案（cglib / ByteBuddy）**拦截不到 `final` 方法**、**无法代理 `final` 类**，`private` 方法也不拦截。
- 设计可被代理的目标类时，避免给要增强的方法加 `final`。

### 3. 工厂用泛型约束 + 不可变
- 工厂方法用 `<T>` 把返回类型与入参类型绑定（见 `LoggedProxyFactory`），把类型错误提前到编译期。
- 拦截器持有的 target 用 `final`，避免被意外替换。

### 4. 构造器参数必须显式传类型
- cglib `Enhancer.create(Class[], Object[])` 要**显式传 `Class[]`**，不要用 `args[i].getClass()` 去猜——基本类型装箱后类型不匹配会 `NoSuchMethodError`。

### 5. 增强逻辑要幂等、要处理基础方法
- 代理对象上的 `toString()/hashCode()/equals()` 也会进拦截器，应当**显式放行**（见 `TimingHandler`），否则容易递归或污染输出。
- 对方法做条件过滤（pointcut 雏形），只增强真正需要的业务方法。

### 6. 异常增强要拿真实异常
- `Method.invoke` 抛的是 `InvocationTargetException`，**真实异常在 `getCause()` 里**（见 `ExceptionShieldHandler`）。容错/重试/降级逻辑应基于 cause。

### 7. 返回值改写必须兼容声明类型
- 代理会按目标方法的**声明返回类型**做拆箱/类型校验：`int` 方法只能返回 `Integer`（返回 `null` 会在拆箱时抛 NPE），返回 `String` 会 `ClassCastException`。需要改写类型时，应在 **String 等引用返回**上做（如脱敏、格式化）。

### 8. JDK 16+ 下 cglib 的运行参数
- cglib 3.3.0 在 JDK 16+ 需要打开 `java.base/java.lang` 才能运行期 `defineClass`。本项目通过：
  - `.mvn/jvm.config`（供 `mvn exec:java` 用）
  - `pom.xml` 中 surefire `<argLine>`（供测试用）
  
  统一提供 `--add-opens=java.base/java.lang=ALL-UNNAMED`。**这也是新项目优先 ByteBuddy 的原因之一。**

### 9. demo 的可验证性
- 别只靠 `main` 肉眼看输出。本项目为每个 pattern 配了 JUnit 断言（捕获 stdout + 校验返回值），保证"增强真的发生了"。`src/test/java` 下与 main 包结构对应。

### 10. demo 里为什么用 System.out 而不是日志框架
- 本项目把 stdout 当作**被观测的演示行为**（测试用 `StdoutCapture` 断言增强真的发生了），这是刻意取舍。
- 真实项目请勿照搬：日志请用 SLF4J（如 Lombok `@Slf4j` + 占位符），不要用 `System.out.println` 当日志。

---

## 五、环境要求与运行

### 必须使用完整 JDK 21（含 javac）
本机 `/usr/lib/jvm/` 下的 `java-*-21` 只是 **JRE，没有 javac**。构建前请：

```bash
export JAVA_HOME=/home/davis/.jdks/ms-21.0.10
export PATH="$JAVA_HOME/bin:$PATH"
```

### 常用命令

```bash
mvn clean test                 # 编译 + 跑全部 21 个测试
mvn exec:java -Dexec.mainClass=zxf.java.dynamic.proxy.cglib.pattern1.TestCGLibProxy
mvn exec:java -Dexec.mainClass=zxf.java.dynamic.proxy.comparison.PerformanceComparison
mvn exec:java -Dexec.mainClass=zxf.java.dynamic.proxy.bytebuddy.TestByteBuddy
mvn exec:java -Dexec.mainClass=zxf.java.dynamic.proxy.aop.TestAOP
```

直接用 `java -cp ... TestXxx` 运行 cglib demo 时，记得带上：
`--add-opens java.base/java.lang=ALL-UNNAMED`（`mvn exec:java` 已由 `.mvn/jvm.config` 自动处理）。

---

## 六、延伸阅读

- JDK `java.lang.reflect.Proxy` / `InvocationHandler`
- cglib `Enhancer` / `Callback`（`MethodInterceptor`、`FixedValue`、`LazyLoader`、`Dispatcher`、`NoOp`）/ `CallbackFilter`
- ByteBuddy `ByteBuddy.subclass(...).method(...).intercept(MethodDelegation.to(...))`
- Spring AOP（`@Aspect`、pointcut、advice）= 本项目 `aop` 包的工业化版本
- JEP 416 / 强封装（Strong Encapsulation）对字节码操作库的影响
