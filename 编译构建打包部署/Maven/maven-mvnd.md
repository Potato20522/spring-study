# maven-mvnd

众所周知，`Maven`和`Gradle`几乎包含了所有Java项目的构建。而目前绝大部分的Java项目都是使用[Maven](https://so.csdn.net/so/search?q=Maven&spm=1001.2101.3001.7020)的方式，Maven对比Gradle的`劣势很明显`，就是`太慢了`(在国内，由于网络原因，其实Gradle比Maven慢得多)！

但是现在，Maven也可以变得更快了！

`maven-mvnd`是`Apache Maven团队`借鉴了`Gradle`和`Takari`的优点，衍生出来的更快的构建工具，maven的`强化版`！

maven-mvnd 在设计上，使用一个或多个守护进程来构建服务，以此来达到并行的目的！同时，maven-mvnd `内置了maven`，因此我可以在`maven 过渡到 maven-mvnd`的过程中实现 `无缝切换`！不必再安装maven或进行复杂的配置更改。

官方仓库地址： https://github.com/apache/maven-mvnd

# 使用步骤

## 从GitHub下载压缩包

点击对应开发环境的版本进行下载即可

window，下载 `mvnd-0.7.1-windows-amd64.zip` 版本

## 解压，配置环境变量

1、把下载的压缩包解压后，配置 其中bin 目录地址到系统 path 环境变量中，如何配置环境变量不在此详述

2、配置环境变量是为了在 `cmd` 的任意地址，可以识别到 `bin` 下的 `mvnd` 命令

3、配置完成，输入 mvnd -version 查看版本信息

```cmd
mvnd -version
```

输出如下信息代表安装成功！

```
C:\WINDOWS\system32>mvnd -version
mvnd native client 0.7.1-windows-amd64 (97c587c11383a67b5bd0ff8388bd94c694b91c1e)
Terminal: org.jline.terminal.impl.jansi.win.JansiWinSysTerminal
Apache Maven 3.8.3 (ff8e977a158738155dc465c6a97ffaf31982d739)
Maven home: D:\Code\mvnd-0.7.1-windows-amd64\mvn
Java version: 1.8.0_102, vendor: Oracle Corporation, runtime: D:\JAVA\jdk\jre
Default locale: zh_CN, platform encoding: GBK
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"

```

## 如何使用

mvnd 与 maven 命令几乎没有任何不同，可以通过查看`mvnd -help` 查看

举个例子、如要打包安装，则把 `mvn clean install` 替换为 `mvnd clean install` 即可

##  配置使用原先已存在的 maven 的仓库！

可以修改 `mvnd 解压目录`下 `conf` 里的 `mvnd.properties` 文件，
拉到最后面，放开 `maven.setting` 注释，把值改成自己的maven仓库地址即可，如下

```
maven.settings=D://maven//apache-maven-3.6.3//conf//settings.xml
```

# 打包速度对比

这里使用一个普通 Java 项目来实验对比，分别使用 `maven` 和 `maven-mvnd` 进行打包，

```bash
# maven 打包命令
mvn clean install -DskipTests
# mvnd 打包命令
mvnd clean install -DskipTests
1234
```

结果如下，速度提升 `3 倍` 左右 ！
速度提升没有网上传言的 8 倍那么夸张