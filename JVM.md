# 内存的几个概念

[Difference in Used, Committed, and Max Heap Memory | Baeldung](https://www.baeldung.com/java-heap-used-committed-max)

**RSS**

常驻内存集，为进程分配的内存大小，包括共享库的内存，不包括进入交换分区的内存，这里RSS就是实际进程占用的内存

**最大内存和自适应内存**

-Xms设定堆内存初始值 -Xmx设置堆内存最大值，如果不设定这两个参数，默认值就取决于操作系统，RAM，JVM本身

![Intial Size](JVM.assets/Intial-Size.png)

**已使用的内存**

used Heap: jvm中活动对象占用的内存，当used 接近committed的时候，heap就会grow up，-Xmx设置了Commit上限

![Used Space](JVM.assets/Used-Space.png)

**提交的内存大小**

Committed Size 

提交的内存大小 始终大于或等于已使用的内存，-Xmx设置了Commit上限。

表示可供java虚拟机使用的内存量，提交的内存可能会随时间的推移而变化（增加或者减少）。JVM可能会向OS释放内存，同时已提交的内存可能小于Init，已提交的内存将始终大于或等于Used Heap（ORACLE 官方文档中可以看到）。init是启动时后JVM向OS申请的内存，max是能够使用的最大边界值。注意这里说的都是虚拟内存，所以理论上整个操作系统**commited的内存为物理内存加上交换空间的大小**，换句话说如果commited超过物理内存的话，多余的部分就会被换出到磁盘。这也就是为什么jvm提交的内存可能会比实际进程占用的内存更大的原因。







## **一、认识JVM的内存**  

[JVM内存分析和优化 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/59569813)

通常我们说的分析java内存，指的是分析java的**堆内存**（heap memory），稍微扩展点，还可能涉及到持久带内存（jre7以前）或者Metaspace（jre8以后）。一般的垃圾回收等参数优化，都是集中到堆内存。

除了对内存，jvm的运行还需要其他**本地内存**（native memory），包括栈内存（Thread），编译器内存，垃圾回收器内存，代码、类等静态数据内存，以及三方代码占用的内存等等。
由于分析技术和考察的内容不同，我们从两个不同的分析层面，将内存分析分为“堆内存分析”和“本地内存分析”。

内存占用是个比较复杂的概念，不能简单的理解为当前进程占用了多少内存。在系统空闲内存很多的时候，最大化利用内存可以提高性能。在一个受限容器里面，限制进程可用的最大内存是一个好主意。

**内存分析至少包含如下几个方面的任务：**

a. 如何规划和限制进程占用的最大内存。
b. 分析内存占用的原因，解决内存泄露问题。
c. 如何根据实际运行环境和应用场景，合理设置内存参数，最大化性能。

## **二、堆内存分析**

对内存的分析的资料比较多，一般比较常用。比如使用jmap和jhat将内存dump出来并分析对象实例，或者结合MemoryAnalyser等工具分析。
JVM还提供了jstat， jvisualvm等工具查看内存增长的趋势、变化情况，以及各个块的区域

## **三、本地内存分析**

本地内存分析比较复杂，这里只简单的介绍如何查看本地内存的使用。

通过jstat命令查看本地内存：

- Java Heap : reserved和commited一般对应-Xms128m -Xmx128m两个参数。

Class : reserved由虚拟机管理，通过一定算法计算出来。 commited一般和-XX:MaxMetaspaceSize=32m大小相关，但是可能超过-XX:MaxMetaspaceSize=32m的值。JIT运行的时候，也会导致reserved和commited的增长。

- Thread： 线程大小。和线程数量成正比。
- Code：JIT运行时用的大小。 reserved和commited分别对应-XX:InitialCodeCacheSize=16m和-XX:ReservedCodeCacheSize=16m。
- 其他： 主要是JVM相关的内容。 可以不用关心。

观察本地内存，需要在JVM启动参数里面增加如下参数：

```text
-XX:NativeMemoryTracking=summary
```

然后通过jcmd查看：

```text
     jcmd 17755 VM.native_memory summary
```

下面是命令执行结果的示例：

```text
Native Memory Tracking:
Total: reserved=1592030KB, committed=307938KB
-                 Java Heap (reserved=131072KB, committed=131072KB)
                            (mmap: reserved=131072KB, committed=131072KB)
-                     Class (reserved=1075815KB, committed=29363KB)
                            (classes #4823)
                            (malloc=615KB #6672)
                            (mmap: reserved=1075200KB, committed=28748KB)
-                    Thread (reserved=67106KB, committed=67106KB)
                            (thread #66)
                            (stack: reserved=66820KB, committed=66820KB)
                            (malloc=209KB #341)
                            (arena=77KB #131)
-                      Code (reserved=251936KB, committed=14296KB)
                            (malloc=2336KB #3508)
                            (mmap: reserved=249600KB, committed=11960KB)
-                        GC (reserved=21624KB, committed=21624KB)
                            (malloc=21192KB #206)
                            (mmap: reserved=432KB, committed=432KB)
-                  Compiler (reserved=227KB, committed=227KB)
                            (malloc=96KB #273)
                            (arena=131KB #3)
-                  Internal (reserved=2166KB, committed=2166KB)
                            (malloc=2134KB #7251)
                            (mmap: reserved=32KB, committed=32KB)
-                    Symbol (reserved=6697KB, committed=6697KB)
                            (malloc=4963KB #46864)
                            (arena=1734KB #1)
-    Native Memory Tracking (reserved=1031KB, committed=1031KB)
                            (malloc=9KB #106)
                            (tracking overhead=1022KB)
-               Arena Chunk (reserved=195KB, committed=195KB)
                            (malloc=195KB)
-                   Unknown (reserved=34160KB, committed=34160KB)
                            (mmap: reserved=34160KB, committed=34160KB)
```

## **四、内存分析和建议**

\1. JAVA内存测试主要关注堆内存， jstat命令输出为主； JAVA的本地内存以jcmd命令的输出为主(jcmd的committed比RSS大，但表示了进程可能使用的内存上限）。其他内存关注的必要性不是很大。

\2. 如果关注top命令的RES内存，建议设置如下参数然后在进行测试， 防止linux系统的内存管理机制和JVM的内存优化导致数据不准确（他们的目标都是最大化利用内存，而不是限制内存使用）

```text
   -server -Djava.compiler=none -Xms128m -Xmx128m -XX:InitialCodeCacheSize=16m -XX:ReservedCodeCacheSize=16m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=64m -Dsun.reflect.inflationThreshold=2147483647
```

根据这个参数设置， 一个进程占用的RES最大值为： 128m（堆） + 16m（JIT）+ 128m（线程） + 64m（Class元数据）+ 64m（其他，包括Compiler/Internal/Symbol/Unknown等JVM内存） = 400m

对于实际运行参数的建议：

实际运行的参数应该尽可能利用内存，初始即分配合理的内存，允许一定的扩展，下面的参数比较可取：

```text
-server -Xms256m -Xmx512m -XX:InitialCodeCacheSize=32m -XX:ReservedCodeCacheSize=64m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Dsun.reflect.inflationThreshold=2147483647
```

分配较大的初始内存不意味着一定需要这么多的内存才能够运行，运行环境设置较大的内存是很有意义的，特别是64位的操作系统。-Dsun.reflect.inflationThreshold=2147483647可以选择不配置，但是如果大量使用反射，

不配置可能非常多的内存，相反，配置的话会带来一定的计算性能开销，实测的结果是建议配置的。

**参考资料**

\1. Oracle 提供的内存分析工具：（包括JVM内存）

[https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr007.html#BABGFCDA](https://link.zhihu.com/?target=https%3A//docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr007.html%23BABGFCDA)

\2. 监控JVM堆和其他内存的工具jstat（主要是堆内存和Metaspace等）

[https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstat.html](https://link.zhihu.com/?target=https%3A//docs.oracle.com/javase/8/docs/technotes/tools/unix/jstat.html)

\3. 解释JVM内存占用机制和内存模型的很好参考

[http://www.ibm.com/developerworks/java/library/j-nativememory-linux/](https://link.zhihu.com/?target=http%3A//www.ibm.com/developerworks/java/library/j-nativememory-linux/)

\4. oracle jre 8的内存模型变化（Metaspace）

[http://www.infoq.com/articles/Java-PERMGEN-Removed](https://link.zhihu.com/?target=http%3A//www.infoq.com/articles/Java-PERMGEN-Removed)

[https://blogs.oracle.com/poonam/entry/about_g1_garbage_collector_permanent](https://link.zhihu.com/?target=https%3A//blogs.oracle.com/poonam/entry/about_g1_garbage_collector_permanent)

\5. jvm启动参数参考(包含GC优化参数)

[https://docs.oracle.com/javase/8/docs/technotes/tools/windows/java.html](https://link.zhihu.com/?target=https%3A//docs.oracle.com/javase/8/docs/technotes/tools/windows/java.html)

\6. 解释RES的意义和java内存的关系[http://stackoverflow.com/questions/561245/virtual-memory-usage-from-java-under-linux-too-much-memory-used](https://link.zhihu.com/?target=http%3A//stackoverflow.com/questions/561245/virtual-memory-usage-from-java-under-linux-too-much-memory-used)

\7. 解释JIT占用的内存已经相关配置参数

[https://docs.oracle.com/javase/8/embedded/develop-apps-platforms/codecache.htm](https://link.zhihu.com/?target=https%3A//docs.oracle.com/javase/8/embedded/develop-apps-platforms/codecache.htm)

\8. 了解反射带来的一些内存泄露问题

[http://stackoverflow.com/questions/6435985/getmethod-is-caching-and-cause-a-memory-leak](https://link.zhihu.com/?target=http%3A//stackoverflow.com/questions/6435985/getmethod-is-caching-and-cause-a-memory-leak)

\9. JVM committed内存和RSS内存的关系

解释为什么java committed总和大于RSS: [http://stackoverflow.com/questions/31071019/does-rss-tracks-reserved-or-commited-memory](https://link.zhihu.com/?target=http%3A//stackoverflow.com/questions/31071019/does-rss-tracks-reserved-or-commited-memory)

解释为什么java committed总和小于RSS：(J ，两种情况都会发生) [http://stackoverflow.com/questions/31173374/why-does-a-jvm-report-more-committed-memory-than-the-linux-process-resident-set](https://link.zhihu.com/?target=http%3A//stackoverflow.com/questions/31173374/why-does-a-jvm-report-more-committed-memory-than-the-linux-process-resident-set)

\10. 使用反射的时候可能存在本地内存的使用说明

[http://www-01.ibm.com/support/docview.wss?uid=swg21566549](https://link.zhihu.com/?target=http%3A//www-01.ibm.com/support/docview.wss%3Fuid%3Dswg21566549)（有点老和错误）

[https://blogs.oracle.com/buck/entry/inflation_system_properties](https://link.zhihu.com/?target=https%3A//blogs.oracle.com/buck/entry/inflation_system_properties) （Oracle JDK，测试结果这里面的是正确的）

\11. JIT相关资料

[http://www.oraclejavamagazine-digital.com/javamagazine_open/20120506#pg50](https://link.zhihu.com/?target=http%3A//www.oraclejavamagazine-digital.com/javamagazine_open/20120506%23pg50)

[http://www.oracle.com/technetwork/articles/java/architect-evans-pt1-2266278.html](https://link.zhihu.com/?target=http%3A//www.oracle.com/technetwork/articles/java/architect-evans-pt1-2266278.html)

\12. JRE8垃圾回收优化指南

[https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/index.html](https://link.zhihu.com/?target=https%3A//docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/index.html)

[https://docs.oracle.com/javase/](https://link.zhihu.com/?target=https%3A//docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/cms.html%23concurrent_mark_sweep_cms_collector)