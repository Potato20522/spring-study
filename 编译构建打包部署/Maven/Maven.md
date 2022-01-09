# 1.概述

目前就是用来方便导入jar包的

Maven的核心思想：**约定大于配置**

- 有约束，但不要去违反

Maven会规定你该如何去编写java代码，必须要按照这个规范来

# 2.下载安装

官网：https://maven.apache.org/download.cgi

下载完成后，解压即可；

电脑上的所有环境都放在一个文件夹下，方便管理

# 3.环境变量

系统变量

- M2_HOME  maven目录下的bin目录
- MAVEN_HOME maven的目录
- 在系统的path中配置%MAVEN_HOME%\bin

# 4.修改配置文件

1.阿里云镜像

```xml
<mirror>
      <id>alimaven</id>
      <name>aliyun maven</name>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
      <mirrorOf>*,!jeecg,!jeecg-snapshots</mirrorOf>
</mirror>
```

```xml
<mirror>
      <id>alimaven</id>
      <name>aliyun maven</name>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
      <mirrorOf>central</mirrorOf>
</mirror>
```



2.本地仓库

建立一个仓库

```xml
<localRepository>F:\ShangGuiGuJavaEE\Environment\apache-maven-3.6.3\repository</localRepository>
```

# 5.在IDEA中使用Maven

1.启动IDEA

2.创建一个mavenWeb项目

![image-20200730195500899](C:\Users\123\AppData\Roaming\Typora\typora-user-images\image-20200730195500899.png)

3.等待项目 初始化完毕

BUID SUCCESS

4.IDEA项目创建后，看一下设置中的maven

5.ok了



# 6.pom.xml文件

具体依赖的jar包

```xml
<dependencies>
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.11</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```

会自动帮你导入这个jar包所依赖的jar包



maven由于它的约定大于配置，我们之后可能遇到我们写的配置文件，无法被导出或生效的问题，解决方案：

在build中配置resources，来防止物品们资源导出失败的问题

```xml
    <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
        </resources>
    </build>
```

```xml
<build>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>false</filtering>
        </resource>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>false</filtering>
        </resource>
    </resources>
</build>
```
# 7.遇到的问题

1.maven  3.6.2

unable to import maven project:See logs for details

解决办法，降级为3.6.1

2.TomCat闪退

3.IDEA中每次重复配置maven

在IDEA的全局默认配置中去配置



