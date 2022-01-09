# 无需Dockerfile构建docker镜像

[How To Configure Java Heap Size Inside a Docker Container | Baeldung](https://www.baeldung.com/ops/docker-jvm-heap-size)

## 方法一：spring-boot-maven-plugin

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <groupId>com.baeldung.docker</groupId>
  <artifactId>heapsizing-demo</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <!-- dependencies... -->
  <build> 
    <plugins> 
      <plugin> 
        <groupId>org.springframework.boot</groupId> 
        <artifactId>spring-boot-maven-plugin</artifactId> 
        <configuration>
          <image>
            <name>heapsizing-demo</name>
          </image>
   <!-- 
    for more options, check:
    https://docs.spring.io/spring-boot/docs/2.4.2/maven-plugin/reference/htmlsingle/#build-image 
   -->
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

执行maven命令：

```
./mvnw clean spring-boot:build-image
```

运行：

```
docker run --rm -ti -p 8080:8080 \
  -e JAVA_TOOL_OPTIONS="-Xms20M -Xmx20M" \
  --memory=1024M heapsizing-demo:0.0.1-SNAPSHOT
```

## 方法二：Google JIB

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <!-- dependencies, ... -->

    <build>
        <plugins>
            <!-- [ other plugins ] -->
            <plugin>
                <groupId>com.google.cloud.tools</groupId>
                <artifactId>jib-maven-plugin</artifactId>
                <version>2.7.1</version>
                <configuration>
                    <to>
                        <image>heapsizing-demo-jib</image>
                    </to>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

执行maven命令：

```
mvn clean install && mvn jib:dockerBuild
```

运行：

```
docker run --rm -ti -p 8080:8080 \
-e JAVA_TOOL_OPTIONS="-Xms50M -Xmx50M" heapsizing-demo-jib
```



# Springboot docker分层打包

[SpringBoot2.3.0 + Docker实现分层打包_我的小地方-CSDN博客](https://blog.csdn.net/ttzommed/article/details/106759670)

[Spring Boot 分层构建 Docker 镜像实战_公众号：Java后端-CSDN博客](https://blog.csdn.net/qq_37217713/article/details/114255240)

SpringBoot默认使用`org.springframework.boot:spring-boot-maven-plugin` Maven插件把项目编译成jar包。默认编译的jar包是一个整体，通过`java -jar`命令可直接启动。结合docker后，我们可以通过`DockerFile`或者`Docker Compose`方式打包成Docker镜像。但每次Maven会将SpringBoot项目文件编译出一个全量jar包在target文件夹下，其jar包内包含我们自己写的代码和依赖的第三方jar包，常常一个jar包在100M上下，这导致在结合docker打包的情况下，每次`docker push`都会上传全量的jar包。最近SpringBoot2.3.0发布，更新包含了支持分层打包，下面我们看看SpringBoot结合Docker如何实现分层打包.

Springboot的maven插件中加入配置支持分层打包：

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <layers>
                    <enabled>true</enabled>
                </layers>
            </configuration>
        </plugin>
    </plugins>
</build>
```

执行maven打包命令，先打成jar包，再用解压缩软件打开这个jar包，进入BOOT-INF目录，可以看出多了两个文件：**classpath.idx**和**layers.idx**

- **classpath.idx：** 文件列出了依赖的 jar 包列表，到时候会按照这个顺序载入。

- **layers.idx：** 文件清单，记录了所有要被复制到 Dokcer 镜像中的文件信息。

根据官方介绍，在构建 Docker 镜像前需要从 Jar 中提起出对应的分层文件到 Jar 外面，可用使用下面命令列出可以从分层 Jar 中提取出的文件夹信息：

```
java -Djarmode=layertools -jar target/springboot-layer-0.0.1.jar list
```

执行了这个命令后，会显示如下内容，放心，不会进行解压缩jar包

```
dependencies
spring-boot-loader
snapshot-dependencies
application
```

上面即是使用分层工具提取 Jar 的内容后生成的文件夹，其中各个文件夹作用是：

- **dependencies：** 存储项目正常依赖 Jar 的文件夹。

- **snapshot-dependencies：** 存储项目快照依赖 Jar 的文件夹。
- **resources：** 用于存储静态资源的文件夹。
- **application：** 用于存储应用程序类相关文件的文件夹。

到目前为止，我们目的就是获取上面这四个文件夹的信息。

创建测试的 SpringBoot 项目，并且在 pom.xml 中开启镜像分层。

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <layers>
                    <enabled>true</enabled>
                </layers>
            </configuration>
        </plugin>
    </plugins>
</build>
```

编写代码......

分层镜像构建脚本文件 Dockerfile

```dockerfile
FROM openjdk:8-jdk as builder
WORKDIR application
COPY target/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract
FROM openjdk:8-jdk
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/application/ ./
EXPOSE 8081
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
```

**说明：**

- **TZ：** 时区设置，而 Asia/Shanghai 表示使用中国上海时区。
- **-Djarmode=layertools：** 指定构建 Jar 的模式。
- **extract：** 从 Jar 包中提取构建镜像所需的内容。
- **-from=builder** 多级镜像构建中，从上一级镜像复制文件到当前镜像中。

