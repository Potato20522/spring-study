# 参考文献

[https://www.reactive-streams.org](https://www.reactive-streams.org/)

[反应式流 Reactive Streams 入门介绍 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/95966853)

[完美解释 Javascript 响应式编程原理_weixin_34166472的博客-CSDN博客](https://blog.csdn.net/weixin_34166472/article/details/87987454)

# 响应式编程

## 现状痛点

JDK的异步处理，一直相对较弱，这方面也有很强的第三方框架

Java中异步流处理的顶级概念：Reactive Streams，翻译为反应式流或响应式流

异步编程大家都有了解，Java里典型的多线程处理就是异步编程。而异步编程时，存在很多难题，比如典型的**回调地狱(Callback Hell)**，一层套一层的回调函数简直是个灾难，这里列出几个异步编程常见的问题：

1. 超时、异常处理困难
2. 难以重构
3. 多个异步任务协同处理

为了解决异步编程过程中出现的种种难题，人们提出了各种各样方法来规避这些问题，这些方法称为**反应式编程**(Reactive Programming)，就像面向对象编程，函数式编程一样，反应式编程也是另一种编程范式。

## 响应式编程概念

反应式编程，本质上是**对数据流或某种变化所作出的反应**，但是这个变化什么时候发生是未知的，所以他是一种**基于异步、回调的方式**在处理问题。

Reactive Programming = Streams + Operations
**Streams代表被处理的数据节点，Operations代表那些异步处理**



当越来越多的开发人员使用这种编程思想时，自然而然需要一套**统一的规范**。由此，2013年底Netflix，Pivotal和Lightbend中的工程师们，启动了**Reactive Streams**项目，希望为异步流(包含背压)处理提供标准，它包括针对运行时环境（**JVM和JavaScript**）以及网络协议的工作。



## Java方面

对于Java程序员，Reactive Streams是一个API。Reactive Streams为我们提供了Java中的Reactive Programming的通用API。

Reactive Streams非常类似于JPA或JDBC。两者都是API规范，实际使用时需要使用API对应的具体实现。例如，从JDBC规范中，有DataSource接口，而Oracle JDBC实现了DataSource接口。Microsoft的SQL Server JDBC实现也实现了DataSource接口。

就像JPA或JDBC一样，Reactive Streams为我们提供了一个我们可以编写代码的API接口，而无需担心底层实现，在GitHub上可以查看API的源码。

Reactive Streams API的范围是找到一组最小的接口，方法和协议，这些接口，方法和协议将描述必要的操作和实体，**从而实现具有非阻塞背压的异步数据流**。

从代码结构上看，它主要包含两部分：`reactive-streams`和`reactive-streams-tck`。其中TCK意为技术兼容包（Technology Compatibility Kit ），为实现Reactive Streams接口提供帮助。

`Reactive Streams API`中仅仅包含了如下四个接口：

```JAVA
//发布者
public interface Publisher <T> {
    public void subscribe(Subscriber <? super T> s);
}
//订阅者
public interface Subscriber <T> {
    public void onSubscribe(Subscription  s);
    public void onNext(T t);
    public void onError(Throwable  t);
    public void onComplete();
}
//表示Subscriber消费Publisher发布的一个消息的生命周期
public interface Subscription {
    public void request(long n);
    public void cancel();
}
//处理器，表示一个处理阶段，它既是订阅者也是发布者，并且遵守两者的契约
public interface Processor<T, R> extends Subscriber<T>, Publisher<R> {

}
```

## JavaScript方面

[reactive-streams/reactive-streams-js: Reactive Streams standardization for JavaScript (github.com)](https://github.com/reactive-streams/reactive-streams-js/)

reactive-streams-js这个框架就是实现了Reactive Streams规范

还有reactiveX

## Reactive Streams的目标

Reactive Streams的主要目标有这两个：

1. 管理跨异步边界的流数据交换 - 即将元素传递到另一个线程或线程池；
2. 确保接收方不会强制缓冲任意数量的数据，为了使线程之间的队列有界，引入了回压(Back Pressure)。

传统异步编程的写法，不同任务分别在不同的线程中执行，协调这些线程执行的先后顺序、线程间的依赖顺序是一件非常麻烦的事情，而Reactive Streams就是为了解决该问题。

另外，Reactive Streams规范引入了回压(Back Pressure)，可以动态控制线程间消息交换的速率，避免生产者产生过多的消息，消费者消费不完等类似问题。

## 重要概念

### Reactive

这是个形容词，翻译为**反应的**，我喜欢叫它响应的，这个词乍一看相当奇怪，这里尝试做一下解释。

事实上，在某些语境下，reactive也会被翻译为**被动**，而`Reactive Streams`是基于消息驱动的（也可以说是事件驱动的），当消息产生时，系统被动接受消息，并作出反馈，而非主动处理。因此，我们也可以这样理解：**被动地接收消息后，作出相应的反应动作**，这个行为称之为**反应式**。

### Streams

这是个名词，翻译为**数据流**，反应式编程的核心思想，体现在了这个单词上。

流的定义：随着时间顺序排列的一组序列。一切皆是流(Everything is a stream)。我们可以把一组数据抽象为流(可以想象流是一个数组)，把对流中节点的逻辑处理，抽象成对节点的一步一步的处理，围绕该节点做加工处理，最终获得结果。

这跟工厂车间的流水线非常相似，发布者将半成品放到传送带上，经过层层处理后，得到成品送到订阅者手中。

而异步特性，是体现在每一步的处理过程中的，每一步处理都是消息驱动的，不阻塞应用程序，被动获得结果后继续进行下一步。



响应式编程，在处理流中节点时，各个步骤都使用**异步的、消息驱动**的方式处理任务，才会节省性能。



- 传统的命令式编程范式以控制流为核心，通过**顺序、分支和循环三种控制结构**来完成不同的行为。

- 在反应式编程中，应用程序从以逻辑为中心转换为了**以数据为中心**，这也是**命令式到声明式的转换**。

### 非阻塞、异步

反义词是阻塞、同步，**目前在Java中，大多数应用程序是同步的，即暴力创建线程，线程阻塞时，一直等待直到有结果返回**。

异步最吸引人的地方在于资源的充分利用，不把资源浪费在等待的时间上，代价是增加了程序的复杂度，而**Reactive Streams封装了这些复杂性，使其变得简单**。

### 背压(back-pressure)

背压是从流体动力学中借用的类比, 在维基百科的定义是：抵抗所需流体通过管道的阻力或力。在软件环境中，可以调整定义：通过软件抵抗所需数据流的阻力或力量。

背压是为了解决这个问题的：上游组件了过量的消息，导致下游组件无法及时处理，从而导致程序崩溃。

![图片](Reactive Streams.assets/640)

对于正遭受压力的组件来说，无论是灾难性地失败，还是不受控地丢弃消息，都是不可接受的。既然它既不能应对压力，又不能直接做失败处理，那么它就应该向其上游组件传达其正在遭受压力的事实，并让它们降低负载。

这种背压（back-pressure）是一种重要的反馈机制，使得系统得以优雅地响应负载，而不是在负载下崩溃。相反，如果下游组件比较空闲，则可以向上游组件发出信号，请求获得更多的调用。

[[知乎\]如何形象的描述反应式编程中的背压(Backpressure)机制 - 简书 (jianshu.com)](https://www.jianshu.com/p/4e02c35152a9)

## 与Java1.8、Java1.9的关系

Reactive Streams不要求必须使用Java8，Reactive Streams也不是Java API的一部分，到了Java9就是了。

但是使用Java8中lambda表达式的存在，可以发挥Reactive Streams规范的强大特性，比如Reactive Streams的实现`Project Reactor`项目的当前版本，就要求最低使用Java1.8。

Java8中的**Stream**和**Reactive Streams**对比

- 它们都使用了流式处理的思想，围绕数据流处理数据，即完成了从命令式到声明式的转换，使数据处理更方便。

- 不同的地方在于，Java8中的`Stream`是同步的、阻塞的，`Reactive Streams`是异步的、非阻塞的。

当使用Java1.9时， Reactive Streams已成为官方Java 9 API的一部分，**Java9中Flow类下的内容与Reactive Streams完全一致**。

## Reactive Streams的具体实现框架

Reactive Streams的实现现在比较多了，David Karnok在Advanced Reactive Java这边文章中[[译\]响应式编程笔记一：响应式总览_weixin_30702887的博客-CSDN博客](https://blog.csdn.net/weixin_30702887/article/details/96010786)，将这些实现分解成几代，也可以侧面了解反应式编程的发展史。

这些仓库是reactive-streams官方展示的不同编程语言的实现

[reactive-streams (github.com)](https://github.com/reactive-streams)

### RxJava

RxJava是ReactiveX项目中的Java实现。ReactiveX项目实现了很多语言，比如JavaScript，.NET（C＃），Scala，Clojure，C ++，Ruby，Python，PHP，Swift等。

RxJava早于Reactive Streams规范。虽然RxJava 2.0+确实实现了Reactive Streams API规范，单使用的术语略有不同。

https://github.com/ReactiveX/RxJava

RxJava也被称为第二代Reactive Streams框架[akarnokd.blogspot.co.uk](https://akarnokd.blogspot.co.uk/2016/03/operator-fusion-part-1.html)

### Reactor⭐

Reactor是Pivotal提供的Java实现，它作为Spring Framework 5的重要组成部分，是WebFlux采用的默认反应式框架。

### Akka Streams

Akka Streams完全实现了Reactive Streams规范，但**Akka Streams API与Reactive Streams API完全分离**。

### Ratpack

Ratpack是一组用于构建现代高性能HTTP应用程序的Java库。Ratpack使用Java 8，Netty和Reactive原则。可以将RxJava或Reactor与Ratpack一起使用。

### Vert.x

Vert.x是一个Eclipse Foundation项目，它是JVM的多语言事件驱动的应用程序框架。Vert.x中的反应支持与Ratpack类似。Vert.x允许我们使用RxJava或其Reactive Streams API的实现。

## 小结

在Reactive Streams之前，各种反应库无法实现互操作性。早期版本的`RxJava`与`Project Reactor`的早期版本不兼容。

另外，反应式编程无法大规模普及，一个很重要的原因是并不是所有库都支持反应式编程，当一些类库只能同步调用时，就无法达到节约性能的作用了。

Reactive Streams的推出统一了反应式编程的规范，并且已经被Java9集成。由此，不同的库可以互操作了，互操作性是一个重要的多米诺骨牌。

例如，MongoDB实现了Reactive Streams驱动程序后，我们可以使用Reactor或RxJava来使用MongoDB中的数据。

