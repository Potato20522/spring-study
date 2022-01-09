来源：[Spring Boot项目使用maven-assembly-plugin根据不同环境打包成tar.gz或者zip - 简书 (jianshu.com)](https://www.jianshu.com/p/287bc88799cb)

# spring-boot-assembly

1. 在spring boot项目中使用maven profiles和maven assembly插件根据不同环境打包成tar.gz或者zip
2. 将spring boot项目中的配置文件提取到外部config目录中
3. 将spring boot项目中的启动jar包移动到boot目录中
4. 将spring boot项目中的第三方依赖jar包移动到外部lib目录中
5. bin目录中是启动，停止，重启服务命令
6. 打包后的目录结构类似于tomcat/maven目录结构

## 主要插件

1. **maven-assembly-plugin**
2. maven-jar-plugin
3. spring-boot-maven-plugin
4. maven-dependency-plugin
5. maven-resources-plugin

## maven-assembly-plugin配置

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-assembly-plugin</artifactId>
    <!-- assembly版本 -->
    <version>2.2.1</version>
    <executions>
        <!-- 若要同时打多个包（如windows和linux不同系统的包），可配置多个execution，此处只打zip，因此配置一个 -->
        <execution>
            <!-- id标识，唯一即可 -->
            <id>make-wrapper-win-zip</id>
            <!-- 设置package阶段进行 -->
            <phase>package</phase>
            <goals>
                <!-- 只运行一次 -->
                <goal>single</goal>
            </goals>
            <configuration>
                <!-- 输出的最终名称，自动添加格式后缀（如zip），当前示例为jsw-test.zip -->
                <finalName>jsw-test</finalName>
                <!-- 配置是否添加id到名称中，若为true，当前示例中，则为jsw-test-zip.zip，false即不添加，只是jsw-test.zip；
                若同时打多个包，则可设为true，分别添加id以作区分-->
                <appendAssemblyId>true</appendAssemblyId>
                <!-- 打包的输出目录，可自定义，${project.build.directory}为编译输出目录，即target目录 -->
                <outputDirectory>${project.build.directory}</outputDirectory>
                <descriptors>
                    <!-- 使用的描述符，按此描述进行打包，此处配置一个zip.xml表示打zip包 -->
                    <descriptor>src/main/assembly/wrapper-win-zip.xml</descriptor>
                </descriptors>
            </configuration>
        </execution>
    </executions>
</plugin>
```

assembly插件在pom.xml的配置比较简单，回答几个问题即可：

- 在什么时候打包：phase，
- 打包出来的名称是什么:finalName
- 是否添加id到名称后缀:appendAssemblyId
- 打包后输出到哪里:outputDirectory
- 使用哪个描述符进行打包操作:descriptor

可参考[maven官网的assembly](https://links.jianshu.com/go?to=http%3A%2F%2Fmaven.apache.org%2Fplugins%2Fmaven-assembly-plugin%2Fusage.html)

## 描述符wrapper-win-zip.xml配置

前面讲到要使用一个描述符进行打包操作，即wrapper-win-zip.xml，此类文件可统一存放在目录src/main/assembly中，以便统一管理。wrapper-win-zip.xml的格式如下所示：

其中format打包格式有这么几种，可以同时写多个：

- **"zip"** - Creates a ZIP file format
- **"tar"** - Creates a TAR format
- **"tar.gz"** or **"tgz"** - Creates a gzip'd TAR format
- **"tar.bz2"** or **"tbz2"** - Creates a bzip'd TAR format
- **"tar.snappy"** - Creates a snappy'd TAR format
- **"tar.xz"** or **"txz"** - Creates a xz'd TAR format
- **"jar"** - Creates a JAR format
- **"dir"** - Creates an exploded directory format
- **"war"** - Creates a WAR format

```xml
<assembly>
    <!-- id标识，唯一即可，若pom中的appendAssemblyId设置为true，则会添加此id作为后缀 -->
    <id>wrapper-win</id>
    <formats>
        <!-- 打包的格式-->
        <format>zip</format>
    </formats>
    <!-- 打包的文件不包含项目目录，压缩包下直接是文件 -->
    <includeBaseDirectory>false</includeBaseDirectory>
    <!-- 配置依赖的输出 -->
    <dependencySets>
        <dependencySet>
            <!-- 是否把当前项目的输出jar包并使用，true则会把当前项目输出为jar包到输出目录,false不输出 -->
            <useProjectArtifact>false</useProjectArtifact>
            <!-- 依赖输出目录，相对输出目录的根目录，当前示例把依赖输出到lib目录 -->
            <outputDirectory>/lib</outputDirectory>
        </dependencySet>
    </dependencySets>
    <!-- 文件输出 -->
    <fileSets>
        <fileSet>
            <!-- 源目录，此处是把编译出来的class文件都输出到根目录下的classes目录-->
            <directory>${project.build.directory}/classes</directory>
            <!-- 输出目录 -->
            <outputDirectory>/classes</outputDirectory>
        </fileSet>
        <fileSet>
            <!-- 此处是把wrapper文件全部输出到根目录下的wrapper目录-->
            <directory>install/wrapper/windows</directory>
            <outputDirectory>/wrapper</outputDirectory>
        </fileSet>
    </fileSets>
</assembly>
```

详细参考[官网assembly的配置说明](http://maven.apache.org/plugins/maven-assembly-plugin/assembly.html)
说明一下，按上述的配置，使用maven命令进行打包（`mvn package`），在target目录会输出的是一个**jsw-test-wrapper-win**包，当前此包名称不影响程序运行，读者可自行个性，包下面直接是三个文件夹（classes,lib,wrapper）。至此，即可以使用maven打出自定义的zip包。

# 根据不同环境打包成tar.gz或者zip

来源：https://www.jianshu.com/p/287bc88799cb

1. 在spring boot项目中使用maven profiles和maven assembly插件根据不同环境打包成tar.gz或者zip
2. 将spring boot项目中的配置文件提取到外部config目录中
3. 将spring boot项目中的启动jar包移动到boot目录中
4. 将spring boot项目中的第三方依赖jar包移动到外部lib目录中
5. bin目录中是启动，停止，重启服务命令
6. 打包后的目录结构类似于tomcat/maven目录结构

## 1.maven-assembly-plugin 配置assembly.xml文件路径

```xml
<plugin>
    <artifactId>maven-assembly-plugin</artifactId>
    <version>3.1.0</version>
    <configuration>
        <descriptors>
            <descriptor>src/main/assembly/assembly.xml</descriptor>
        </descriptors>
    </configuration>
    <executions>
        <execution>
            <id>make-assembly</id>
            <phase>package</phase>
            <goals>
                <goal>single</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## 2.assembly.xml打包配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<assembly>
    <!-- 可自定义，这里指定的是项目环境 -->
    <!-- spring-boot-assembly-local-1.0.RELEASE.tar.gz  -->
    <id>${profileActive}-${project.version}</id>

    <!-- 打包的类型，如果有N个，将会打N个类型的包 -->
    <formats>
        <format>tar.gz</format>
        <!--<format>zip</format>-->
    </formats>

    <includeBaseDirectory>true</includeBaseDirectory>

    <fileSets>
        <!--
            0755->即用户具有读/写/执行权限，组用户和其它用户具有读写权限；
            0644->即用户具有读写权限，组用户和其它用户具有只读权限；
        -->

        <!-- 将src/bin目录下的所有文件输出到打包后的bin目录中 -->
        <fileSet>
            <directory>${basedir}/src/bin</directory>
            <outputDirectory>bin</outputDirectory>
            <fileMode>0755</fileMode>
            <includes>
                <include>**.sh</include>
                <include>**.bat</include>
            </includes>
        </fileSet>

        <!-- 指定输出target/classes中的配置文件到config目录中 -->
        <fileSet>
            <directory>${basedir}/target/classes</directory>
            <outputDirectory>config</outputDirectory>
            <fileMode>0644</fileMode>
            <includes>
                <include>application.yml</include>
                <include>application-${profileActive}.yml</include>
                <include>mapper/**/*.xml</include>
                <include>static/**</include>
                <include>templates/**</include>
                <include>*.xml</include>
                <include>*.properties</include>
            </includes>
        </fileSet>

        <!-- 将第三方依赖打包到lib目录中 -->
        <fileSet>
            <directory>${basedir}/target/lib</directory>
            <outputDirectory>lib</outputDirectory>
            <fileMode>0755</fileMode>
        </fileSet>

        <!-- 将项目启动jar打包到boot目录中 -->
        <fileSet>
            <directory>${basedir}/target</directory>
            <outputDirectory>boot</outputDirectory>
            <fileMode>0755</fileMode>
            <includes>
                <include>${project.build.finalName}.jar</include>
            </includes>
        </fileSet>

        <!-- 包含根目录下的文件 -->
        <fileSet>
            <directory>${basedir}</directory>
            <includes>
                <include>NOTICE</include>
                <include>LICENSE</include>
                <include>*.md</include>
            </includes>
        </fileSet>
    </fileSets>

</assembly>
```

## 3.spring-boot-maven-plugin 排除启动jar包中依赖的jar包

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <layout>ZIP</layout>
        <includes>
            <!-- 项目启动jar包中排除依赖包 -->
            <include>
                <groupId>non-exists</groupId>
                <artifactId>non-exists</artifactId>
            </include>
        </includes>
    </configuration>
</plugin>
```

## 4.maven-jar-plugin 自定义maven jar打包内容

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>3.1.0</version>
    <configuration>
        <archive>
            <manifest>
                <!-- 项目启动类 -->
                <mainClass>Application</mainClass>
                <!-- 依赖的jar的目录前缀 -->
                <classpathPrefix>../lib</classpathPrefix>
                <addClasspath>true</addClasspath>
            </manifest>
        </archive>
        <includes>
            <!-- 只打包指定目录的文件 -->
            <include>io/geekidea/springboot/**</include>
        </includes>
    </configuration>
</plugin>    

```

## 5.maven-dependency-plugin 复制项目的依赖包到指定目录

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <version>3.1.0</version>
    <executions>
        <execution>
            <phase>prepare-package</phase>
            <goals>
                <goal>copy-dependencies</goal>
            </goals>
            <configuration>
                <outputDirectory>target/lib</outputDirectory>
                <overWriteReleases>false</overWriteReleases>
                <overWriteSnapshots>false</overWriteSnapshots>
                <overWriteIfNewer>true</overWriteIfNewer>
                <includeScope>compile</includeScope>
            </configuration>
        </execution>
    </executions>
</plugin>    
```

## 6.maven-resources-plugin

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-resources-plugin</artifactId>
    <version>3.1.0</version>
</plugin>

<resource>
    <directory>src/main/resources</directory>
    <filtering>true</filtering>
    <includes>
        <include>application.yml</include>
        <include>application-${profileActive}.yml</include>
        <include>mapper/**/*.xml</include>
        <include>static/**</include>
        <include>templates/**</include>
        <include>*.xml</include>
        <include>*.properties</include>
    </includes>
</resource>
```

## 7.maven profiles配置

```xml
<!--MAVEN打包选择运行环境-->
<!-- 1:local(默认) 本地 2:dev:开发环境 3:test 4:uat 用户验收测试 5.pro:生产环境-->
<profiles>
    <profile>
        <id>local</id>
        <properties>
            <profileActive>local</profileActive>
        </properties>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
    </profile>
    <profile>
        <id>dev</id>
        <properties>
            <profileActive>dev</profileActive>
        </properties>
        <activation>
            <activeByDefault>false</activeByDefault>
        </activation>
    </profile>
    <profile>
        <id>test</id>
        <properties>
            <profileActive>test</profileActive>
        </properties>
        <activation>
            <activeByDefault>false</activeByDefault>
        </activation>
    </profile>
    <profile>
        <id>uat</id>
        <properties>
            <profileActive>uat</profileActive>
        </properties>
        <activation>
            <activeByDefault>false</activeByDefault>
        </activation>
    </profile>
    <profile>
        <id>prod</id>
        <properties>
            <profileActive>prod</profileActive>
        </properties>
        <activation>
            <activeByDefault>false</activeByDefault>
        </activation>
    </profile>
</profiles>
```

## 8.阿里云仓库配置

```xml
<repositories>
    <!--阿里云仓库-->
    <repository>
        <id>aliyun</id>
        <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    </repository>
</repositories>
```

## 项目源码结构

```css
├─bin
│      restart.sh
│      shutdown.sh
│      startup.bat
│      startup.sh
│
├─logs
│      springboot-assembly.log
│
├─main
│  ├─assembly
│  │      assembly.xml
│  │
│  ├─java
│  │  └─io
│  │      └─geekidea
│  │          └─springboot
│  │              └─assembly
│  │                      Application.java
│  │                      HelloController.java
│  │                      HelloService.java
│  │                      HelloServiceImpl.java
│  │
│  └─resources
│      │  application-dev.yml
│      │  application-local.yml
│      │  application-prod.yml
│      │  application-test.yml
│      │  application-uat.yml
│      │  application.yml
│      │
│      ├─mapper
│      │  │  test.xml
│      │  │
│      │  └─hello
│      │          hello.xml
│      │
│      ├─static
│      │      index.html
│      │
│      └─templates
│              test.txt
│
└─test
```

## 项目打包

```bash
mvn clean package
```

使用maven assembly插件打包local环境后的压缩包,target目录下

```css
spring-boot-assembly-local-1.0.RELEASE.tar.gz
```

linux解压tar.gz

```bash
tar -zxvf spring-boot-assembly-local-1.0.RELEASE.tar.gz
```

解压后的目录结构

```css
└─spring-boot-assembly
    │  LICENSE
    │  NOTICE
    │  README.md
    │
    ├─bin
    │      restart.sh
    │      shutdown.sh
    │      startup.bat
    │      startup.sh
    │
    ├─boot
    │      spring-boot-assembly.jar
    │
    ├─config
    │  │  application-local.yml
    │  │  application.yml
    │  │
    │  ├─mapper
    │  │  │  test.xml
    │  │  │
    │  │  └─hello
    │  │          hello.xml
    │  │
    │  ├─static
    │  │      index.html
    │  │
    │  └─templates
    │          test.txt
    │
    └─lib
            classmate-1.4.0.jar
            fastjson-1.2.54.jar
            hibernate-validator-6.0.13.Final.jar
            jackson-annotations-2.9.0.jar
            jackson-core-2.9.7.jar
            jackson-databind-2.9.7.jar
            jackson-datatype-jdk8-2.9.7.jar
            jackson-datatype-jsr310-2.9.7.jar
            jackson-module-parameter-names-2.9.7.jar
            javax.annotation-api-1.3.2.jar
            jboss-logging-3.3.2.Final.jar
            jul-to-slf4j-1.7.25.jar
            log4j-api-2.11.1.jar
            log4j-to-slf4j-2.11.1.jar
            logback-classic-1.2.3.jar
            logback-core-1.2.3.jar
            slf4j-api-1.7.25.jar
            snakeyaml-1.23.jar
            spring-aop-5.1.2.RELEASE.jar
            spring-beans-5.1.2.RELEASE.jar
            spring-boot-2.1.0.RELEASE.jar
            spring-boot-autoconfigure-2.1.0.RELEASE.jar
            spring-boot-starter-2.1.0.RELEASE.jar
            spring-boot-starter-json-2.1.0.RELEASE.jar
            spring-boot-starter-logging-2.1.0.RELEASE.jar
            spring-boot-starter-tomcat-2.1.0.RELEASE.jar
            spring-boot-starter-web-2.1.0.RELEASE.jar
            spring-context-5.1.2.RELEASE.jar
            spring-core-5.1.2.RELEASE.jar
            spring-expression-5.1.2.RELEASE.jar
            spring-jcl-5.1.2.RELEASE.jar
            spring-web-5.1.2.RELEASE.jar
            spring-webmvc-5.1.2.RELEASE.jar
            tomcat-embed-core-9.0.12.jar
            tomcat-embed-el-9.0.12.jar
            tomcat-embed-websocket-9.0.12.jar
            validation-api-2.0.1.Final.jar

```

window启动,会打开浏览器，访问项目测试路径

```bash
bin/startup.bat
```

linux启动，停止，重启

```bash
sh bin/startup.sh   启动项目
sh bin/shutdown.sh  停止服务
sh bin/restart.sh   重启服务
```

startup.sh 脚本中的主要内容

配置项目名称及项目启动jar名称，默认项目名称与启动jar名称一致

```
APPLICATION="spring-boot-assembly"
APPLICATION_JAR="${APPLICATION}.jar"
```

JAVA_OPTION配置

JVM Configuration
-Xmx256m:设置JVM最大可用内存为256m,根据项目实际情况而定，建议最小和最大设置成一样。
-Xms256m:设置JVM初始内存。此值可以设置与-Xmx相同,以避免每次垃圾回收完成后JVM重新分配内存
-Xmn512m:设置年轻代大小为512m。整个JVM内存大小=年轻代大小 + 年老代大小 + 持久代大小。持久代一般固定大小为64m,所以增大年轻代,将会减小年老代大小。此值对系统性能影响较大,Sun官方推荐配置为整个堆的3/8
-XX:MetaspaceSize=64m:存储class的内存大小,该值越大触发Metaspace GC的时机就越晚
-XX:MaxMetaspaceSize=320m:限制Metaspace增长的上限，防止因为某些情况导致Metaspace无限的使用本地内存，影响到其他程序
-XX:-OmitStackTraceInFastThrow:解决重复异常不打印堆栈信息问题

```bash
JAVA_OPT="-server -Xms256m -Xmx256m -Xmn512m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=256m"
JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow"
```

执行启动命令：后台启动项目,并将日志输出到项目根目录下的logs文件夹下

```bash
nohup java ${JAVA_OPT} -jar ${BASE_PATH}/boot/${APPLICATION_JAR} --spring.config.location=${CONFIG_DIR} > ${LOG_PATH} 2>&1 &
```

最终执行jar包的命令

```bash
nohup java -server -Xms256m -Xmx256m -Xmn512m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=256m -XX:-OmitStackTraceInFastThrow -jar /opt/spring-boot-assembly/boot/spring-boot-assembly.jar --spring.config.location=/opt/spring-boot-assembly/config/ > /opt/spring-boot-assembly/logs/spring-boot-assembly.log 2>&1 &
```

- nohup：在后台运行jar包，然后将运行日志输出到指定位置
- -server：指定JVM参数
- -jar /opt/spring-boot-assembly/boot/spring-boot-assembly.jar：指定启动的jar包
- 启动命令中指定的启动jar包路径，配置文件路径，日志路径都是绝对路径
- 可在任何位置执行start.sh,shutdown.sh,restart.sh脚本
- --spring.config.location：指定配置文件目录或者文件名称，如果是目录，以/结束
- \> /opt/spring-boot-assembly/logs/spring-boot-assembly.log：指定日志输出路径
- 2>&1 & ：将正常的运行日志和错误日志合并输入到指定日志，并在后台运行

shutdown.sh停服脚本，实现方式：找到当前项目的PID，然后kill -9

```bash
PID=$(ps -ef | grep "${APPLICATION_JAR}" | grep -v grep | awk '{ print $2 }')
kill -9 ${PID}
```