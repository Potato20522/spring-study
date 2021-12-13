# slf4j-api

参考来源: https://www.cnblogs.com/lujiango/p/8573411.html

**slf4j**:Simple Logging Facade for Java，为java提供的简单日志Facade。Facade门面，更底层一点说就是接口。它允许用户以自己的喜好，在工程中通过slf4j接入不同的日志系统。

因此slf4j入口就是众多接口的集合，它不负责具体的日志实现，只在编译时负责寻找合适的日志系统进行绑定。具体有哪些接口，全部都定义在slf4j-api中。查看slf4j-api源码就可以发现，里面除了public final class LoggerFactory类之外，都是接口定义。因此**slf4j-api本质就是一个接口定义**。

它只提供一个核心slf4j api(就是slf4j-api.jar包)，这个包**只有日志的接口，并没有实现**，所以如果要使用就得再给它提供一个实现了些接口的日志包，比 如：log4j,common logging,jdk log日志实现包等，但是这些日志实现又不能通过接口直接调用，实现上他们根本就和slf4j-api不一致，因此**slf4j又增加了一层来转换各日志实 现包的使用，比如slf4j-log4j12等**。



