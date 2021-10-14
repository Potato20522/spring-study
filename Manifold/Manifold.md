## Manifold框架概述

## 相关站点

[官网](http://manifold.systems/)

[官网文档](http://manifold.systems/docs.html)

[github](https://github.com/manifold-systems/manifold)

## 用途速览

### 元编程 Meta-programming

Manifold实现了类型安全的元编程，可以访问其他类型的代码：GraphQL，JSON，XML，YAML，CSV，甚至是其他编程语言，比如JavaScript

**GraphQL**：

```java
var query = MovieQuery.builder(Action).build();
var result = query.request("http://com.example/graphql").post();
var actionMovies = result.getMovies();
for (var movie : actionMovies) {
  out.println(
    "Title: " + movie.getTitle() + "\n" +
    "Genre: " + movie.getGenre() + "\n" +
    "Year: " + movie.getReleaseDate().getYear() + "\n");
}
```

**JSON:**

```java
// From User.json
User user = User.builder("myid", "mypassword", "Scott")
  .withGender(male)
  .withDob(LocalDate.of(1987, 6, 15))
  .build();
User.request("http://api.example.com/users").postOne(user);
```

### 扩展方法

在已经存在的java类、字符串、List、文件中添加方法，达到消除样板代码的目的

```java
String greeting = "hello";
greeting.myMethod(); // Add your own methods to String!
```

### 添加属性

去除getter/setter，类似于lombok，类似于C#中的{get;set}

```java
public interface Book {
  @var String title; // no more boilerplate code!
}
// refer to it directly by name
book.title = "Daisy";     // calls setter
String name = book.title; // calls getter 
book.title += " chain";   // calls getter & setter
```

可以进行类型的自动推断。

以前的代码这样写：

```java
Actor person = result.getMovie().getLeadingRole().getActor();
Likes likes = person.getLikes();
likes.setCount(likes.getCount() + 1);
```

用了Manifold后可以这样：

```java
result.movie.leadingRole.actor.likes.count++;
```

### 运算符重载

支持算术运算符、关系运算符、索引运算符和单元运算符的重载

```java
// BigDecimal expressions
if (bigDec1 > bigDec2) {
  BigDecimal result = bigDec1 + bigDec2;
  ...
}
// Implement operators for any type
MyType value = myType1 + myType2;
```

### 单位

#### 单位表达式

带单位的运算

```java
import static manifold.science.util.UnitConstants.*; // kg, m, s, ft, etc
...
Length distance = 100 mph * 3 hr;
Force f = 5.2 kg m/s/s; // same as 5.2 N
Mass infant = 9 lb + 8.71 oz;
```

#### Ranges

和单位表达式配合使用

```java
// imports the `to`, `step`, and other "binding" constants
import static manifold.collections.api.range.RangeFun.*;
...
for (int i: 1 to 5) {
  out.println(i);
}

for (Mass m: 0kg to 10kg step 22r unit g) {
  out.println(m);
}
```

#### Science

```java
import static manifold.science.util.UnitConstants.*; // kg, m, s, ft, etc.
...
Velocity rate = 65mph;
Time time = 1min + 3.7sec;
Length distance = rate * time;
```

### 预处理器

类似于C语言中的#define

```java
#if JAVA_8_OR_LATER
  @Override
  public void setTime(LocalDateTime time) {...}
#else
  @Override
  public void setTime(Calendar time) {...}
#endif
```

### Structural Typing

统一不同的API，用类型安全接口访问这些API

```java
Map<String, Object> map = new HashMap<>();
MyThingInterface thing = (MyThingInterface) map; // O_o
thing.setFoo(new Foo());
Foo foo = thing.getFoo();
out.println(thing.getClass()); // prints "java.util.HashMap"
```

### 类型安全的反射

Jailbreak访问私有方法，可以避免写繁琐的Java反射代码

```java
@Jailbreak Foo foo = new Foo();
// Direct, *type-safe* access to *all* foo's members
foo.privateMethod(x, y, z);
foo.privateField = value;
```

### 检查异常处理

不用写try catch了

```java
List<String> strings = ...;
List<URL> urls = list
  .map(URL::new) // No need to handle the MalformedURLException!
  .collect(Collectors.toList());
```

### 字符串模板

```java
int hour = 15;
// Simple variable access with '$'
String result = "The hour is $hour"; // Yes!!!
// Use expressions with '${}'
result = "It is ${hour > 12 ? hour-12 : hour} o'clock";
```

### Java模板引擎

```java
List<User> users = ...;
String content = abc.example.UserSample.render(users);
```

模板文件：abc/example/UserSample.html.mtl

```html
<%@ import java.util.List %>
<%@ import com.example.User %>
<%@ params(List<User> users) %>
<html lang="en">
<body>
<% users.stream()
   .filter(user -> user.getDateOfBirth() != null)
   .forEach(user -> { %>
    User: ${user.getName()} <br>
    DOB: ${user.getDateOfBirth()} <br>
<% }); %>
</body>
</html>
```

### 对IDEA和Android Studio.支持

安装 Manifold plugin插件就行