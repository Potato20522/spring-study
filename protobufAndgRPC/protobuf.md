# Protocol Buffers入门

[Protocol Buffers](https://developers.google.cn/protocol-buffers)

https://developers.google.cn/protocol-buffers/docs/javatutorial

Protocol Buffers 是语言中立的，数据序列化用的，类比于JSON

- 在`.proto`文件中定义消息格式
- 使用ProtoBuf编译器编译出Java代码
- 使用ProtoBuf API来写入和读取消息

## 编写proto文件

新建一个addressbook.proto文件：

```protobuf
syntax = "proto2";
package tutorial;
option java_multiple_files = true;//为每个类单独生成文件
option java_package = "com.example.tutorial.protos"; //指定包名称
option java_outer_classname = "AddressBookProtos";//生成的class名称，大驼峰

message Person {
  optional string name = 1;
  optional int32 id = 2;
  optional string email = 3;

  enum PhoneType {
    MOBILE = 0;
    HOME = 1;
    WORK = 2;
  }

  message PhoneNumber {
    optional string number = 1;
    optional PhoneType type = 2 [default = HOME];
  }
  
  repeated PhoneNumber phones = 4;
}

message AddressBook {
  repeated Person people = 1;
}
```

- 属性后面的=1，=2，=3是二进制编码中的唯一标识，1-15使用一个字节，再往上就2个或更多的字节

- **optional** 可选属性，可以设置默认值，不设默认值就使用系统的默认值，比如int32默认为0.
- **repeated** 该出现可重复任意多次（包括0次），重复值的顺序将保留ProtoBuf 中。将重复字段视为动态大小的**数组**。

- **required** 必须提供该字段的值，否则消息将被视为“未初始化”。尝试构建未初始化的消息将抛出一个`RuntimeException`. 解析未初始化的消息将抛出一个`IOException`. 除此之外，必填字段的行为与可选字段完全相同。

proto3 文档：https://developers.google.cn/protocol-buffers/docs/proto3

## 编译proto文件生成java文件

编译器下载：https://developers.google.cn/protocol-buffers/docs/downloads

```
protoc -I=$SRC_DIR --java_out=$DST_DIR $SRC_DIR/addressbook.proto
```



在`com/example/tutorial/protos/` 生成了 .java文件。

可以看出，每个外层的message都生成了单独的.java文件，内层的message以static class的形式生成了，每个message还生成了xxxOrBuilder接口

![image-20210727134143817](images/protobuf/image-20210727134143817.png)

![image-20210727135017214](images/protobuf/image-20210727135017214.png)

继承和实现关系如下：

![image-20210727135152042](images/protobuf/image-20210727135152042.png)



## ProtoBuf  API

### 介绍

- 每个消息都生成了一个类文件(称为消息类)，每个类中都有自己的Builder类，Builder类用于创建该外部类的实例
- 消息类为消息的每个字段都生成了getter方法
- Builder类为消息的每个字段都生成了getter和setter方法
- 对于optional修饰的字段，消息类和Builder类都生成了public boolean hasXx()的方法
- 对于repeated修饰的字段，生成了List类型的getter和setter方法

下面是Person消息类的getter方法：

```java
// required string name = 1;
public boolean hasName();
public String getName();

// required int32 id = 2;
public boolean hasId();
public int getId();

// optional string email = 3;
public boolean hasEmail();
public String getEmail();

// repeated .tutorial.Person.PhoneNumber phones = 4;
public List<PhoneNumber> getPhonesList();
public int getPhonesCount();
public PhoneNumber getPhones(int index);
```

下面是Person.Builder的getter和setter

```java
// required string name = 1;
public boolean hasName();
public java.lang.String getName();
public Builder setName(String value);
public Builder clearName();

// required int32 id = 2;
public boolean hasId();
public int getId();
public Builder setId(int value);
public Builder clearId();

// optional string email = 3;
public boolean hasEmail();
public String getEmail();
public Builder setEmail(String value);
public Builder clearEmail();

// repeated .tutorial.Person.PhoneNumber phones = 4;
public List<PhoneNumber> getPhonesList();
public int getPhonesCount();
public PhoneNumber getPhones(int index);
public Builder setPhones(int index, PhoneNumber value);
public Builder addPhones(PhoneNumber value);
public Builder addAllPhones(Iterable<PhoneNumber> value);
public Builder clearPhones();
```

### Message类和Builder类对比

Message类是不可变的，类似于String类，Builder类用于创建Message对象，类似于StringBuilder

根据Builder创建Person类：

```java
Person john =
  Person.newBuilder()
    .setId(1234)
    .setName("John Doe")
    .setEmail("jdoe@example.com")
    .addPhones(
      Person.PhoneNumber.newBuilder()
        .setNumber("555-4321")
        .setType(Person.PhoneType.HOME))
    .build();
```

### 方法

除了getter,setter和has方法，还有如下方法，用来检查或操作Message：

- `isInitialized()`: 检查是否已设置所有必填字段。

- toString() 
- mergeFrom(Message other) :（仅Builder）将other内容合并到此消息中，覆盖单一标量字段，合并复合字段，并连接重复字段。

- `clear()`：（仅Builder）清空所有字段

### 解析和序列化

每个ProtoBuf都有二进制格式写入和读取消息的方法：https://developers.google.cn/protocol-buffers/docs/reference/java/com/google/protobuf/Message

- `byte[] toByteArray();`: 序列化消息并返回一个包含其原始字节的字节数组。
- `static Person parseFrom(byte[] data);`: 从给定的字节数组中解析一条消息。
- `void writeTo(OutputStream output);`: 序列化消息并将其写入`OutputStream`.
- `static Person parseFrom(InputStream input);`: 读取并解析来自`InputStream`.

## 写入Message

利用Builder来构建Person对象，并写入输出流

```java
import com.example.tutorial.protos.AddressBook;
import com.example.tutorial.protos.Person;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;

class AddPerson {
  // This function fills in a Person message based on user input.
  static Person PromptForAddress(BufferedReader stdin,
                                 PrintStream stdout) throws IOException {
    Person.Builder person = Person.newBuilder();

    stdout.print("Enter person ID: ");
    person.setId(Integer.valueOf(stdin.readLine()));

    stdout.print("Enter name: ");
    person.setName(stdin.readLine());

    stdout.print("Enter email address (blank for none): ");
    String email = stdin.readLine();
    if (email.length() > 0) {
      person.setEmail(email);
    }

    while (true) {
      stdout.print("Enter a phone number (or leave blank to finish): ");
      String number = stdin.readLine();
      if (number.length() == 0) {
        break;
      }

      Person.PhoneNumber.Builder phoneNumber =
        Person.PhoneNumber.newBuilder().setNumber(number);

      stdout.print("Is this a mobile, home, or work phone? ");
      String type = stdin.readLine();
      if (type.equals("mobile")) {
        phoneNumber.setType(Person.PhoneType.MOBILE);
      } else if (type.equals("home")) {
        phoneNumber.setType(Person.PhoneType.HOME);
      } else if (type.equals("work")) {
        phoneNumber.setType(Person.PhoneType.WORK);
      } else {
        stdout.println("Unknown phone type.  Using default.");
      }

      person.addPhones(phoneNumber);
    }

    return person.build();
  }

  // Main function:  Reads the entire address book from a file,
  //   adds one person based on user input, then writes it back out to the same
  //   file.
  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("Usage:  AddPerson ADDRESS_BOOK_FILE");
      System.exit(-1);
    }

    AddressBook.Builder addressBook = AddressBook.newBuilder();

    // Read the existing address book.
    try {
      addressBook.mergeFrom(new FileInputStream(args[0]));
    } catch (FileNotFoundException e) {
      System.out.println(args[0] + ": File not found.  Creating a new file.");
    }

    // Add an address.
    addressBook.addPerson(
      PromptForAddress(new BufferedReader(new InputStreamReader(System.in)),
                       System.out));

    // Write the new address book back to disk.
    FileOutputStream output = new FileOutputStream(args[0]);
    addressBook.build().writeTo(output);
    output.close();
  }
}
```

## 读取Message

```java
import com.example.tutorial.protos.AddressBook;
import com.example.tutorial.protos.Person;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;

class ListPeople {
  // Iterates though all people in the AddressBook and prints info about them.
  static void Print(AddressBook addressBook) {
    for (Person person: addressBook.getPeopleList()) {
      System.out.println("Person ID: " + person.getId());
      System.out.println("  Name: " + person.getName());
      if (person.hasEmail()) {
        System.out.println("  E-mail address: " + person.getEmail());
      }

      for (Person.PhoneNumber phoneNumber : person.getPhonesList()) {
        switch (phoneNumber.getType()) {
          case MOBILE:
            System.out.print("  Mobile phone #: ");
            break;
          case HOME:
            System.out.print("  Home phone #: ");
            break;
          case WORK:
            System.out.print("  Work phone #: ");
            break;
        }
        System.out.println(phoneNumber.getNumber());
      }
    }
  }

  // Main function:  Reads the entire address book from a file and prints all
  //   the information inside.
  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("Usage:  ListPeople ADDRESS_BOOK_FILE");
      System.exit(-1);
    }

    // Read the existing address book.
    AddressBook addressBook =
      AddressBook.parseFrom(new FileInputStream(args[0]));

    Print(addressBook);
  }
}
```



## 扩展ProtoBuf 

由proto生成的java文件，如果我们想更改这个Java文件的话，要考虑兼容性，为此，官方建议我们遵守一些规则:

- 不得更改现有字段的标签标号
- 不得添加或删除任何必填字段
- 可以删除可选或重复字段
- 可以添加新的可选或重复字段，使用新的标签号，这个新的标签号不能和之前的重复，也不能和之前被删除的标签号重复



## 高级用法

ProtoBuf 不仅仅提供了builder和序列化，还提供了反射的特性，可以将**ProtoBuf 消息与JSON转换**

https://developers.google.cn/protocol-buffers/docs/reference/java



# grpc

https://www.grpc.io/docs/

https://www.grpc.io/docs/languages/java/

## 入门



















































# ProtoBuf 与 gRPC

https://www.cnblogs.com/makor/p/protobuf-and-grpc.html

**ProtoBuf**

ProtoBuf 是一种数据表达方式，根据 G 家自己的描述，应该叫做**数据交换格式**，注意这里使用的是 **交换** 字眼，也就是说着重于在数据的传输上，有别于 TOML 和 XML 较常用于配置（当然 WebService 一套也是用于数据交换）。

在使用 ProtoBuf 之后，很多时候，我都希望能够用它来替换 json 和 XML，因为相比较于这些工具，ProtoBuf 的优势比较明显。例如 json 虽然表达方便，语法清晰，但是，有一个硬伤就是没有 schema，对于 Client-Server 的应用/服务来说，这就意味着双方需要使用其他方式进行沟通 schema，否则将无法正确的交流；相比之下，XML 确实提供了强大的 Schema 支持，但是，可能因为年纪更大的缘故，XML 自身的语法啰嗦，更别说定义它的 Schema 了，一句话概括，那就是非常得不现代。

# springboot 集成 grpc

https://blog.csdn.net/weixin_40395050/article/details/96971708

gRPC 一开始由 google 开发，是一款语言中立、平台中立、开源的远程过程调用(RPC)系统。

在 gRPC 里客户端应用可以像调用本地对象一样直接调用另一台不同的机器上服务端应用的方法，使得您能够更容易地创建分布式应用和服务。与许多 RPC 系统类似，gRPC 也是基于以下理念：定义一个服务，指定其能够被远程调用的方法（包含参数和返回类型）。在服务端实现这个接口，并运行一个 gRPC 服务器来处理客户端调用。在客户端拥有一个存根能够像服务端一样的方法。


![在这里插入图片描述](images/protobuf/20190723102627853.jpg)

**特性**
基于HTTP/2
HTTP/2 提供了连接多路复用、双向流、服务器推送、请求优先级、首部压缩等机制。可以节省带宽、降低TCP链接次数、节省CPU，帮助移动设备延长电池寿命等。gRPC 的协议设计上使用了HTTP2 现有的语义，请求和响应的数据使用HTTP Body 发送，其他的控制信息则用Header 表示。

IDL使用ProtoBuf
gRPC使用ProtoBuf来定义服务，ProtoBuf是由Google开发的一种数据序列化协议（类似于XML、JSON、hessian）。ProtoBuf能够将数据进行序列化，并广泛应用在数据存储、通信协议等方面。压缩和传输效率高，语法简单，表达力强。

多语言支持（C, C++, Python, PHP, Nodejs, C#, Objective-C、Golang、Java）
gRPC支持多种语言，并能够基于语言自动生成客户端和服务端功能库。目前已提供了C版本grpc、Java版本grpc-java 和 Go版本grpc-go，其它语言的版本正在积极开发中，其中，grpc支持C、C++、Node.js、Python、Ruby、Objective-C、PHP和C#等语言，grpc-java已经支持Android开发。
