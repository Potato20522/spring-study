# 简介

参考：

https://www.yiibai.com/guava/

[Google Guava官方教程（中文版） | Google Guava 中文教程 (gitbooks.io)](https://wizardforcel.gitbooks.io/guava-tutorial/content/1.html)

[google/guava: Google core libraries for Java (github.com)](https://github.com/google/guava)

https://github.com/google/guava/wiki

https://guava.dev/releases/snapshot-jre/api/docs/

[Guava快速入门 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/20637960)

[Guava - 拯救垃圾代码，写出优雅高效，效率提升N倍 - 程序猿阿朗 - 博客园 (cnblogs.com)](https://www.cnblogs.com/niumoo/p/13888994.html)

Guava是谷歌提供的开源Java库，提供了用于集合，缓存，支持原语，并发，常见注解，字符串处理，I/O和验证的实用工具类。

**在Java 8中，已经可以看到不少API就是从Guava中原封不动的借鉴而来。**



Guava的好处：

- 标准化 - Guava库是由谷歌托管。
- 高效 - 可靠，快速和有效的扩展JAVA标准库
- 优化 -Guava库经过高度的优化。

函数式编程 -增加JAVA功能和处理能力。

实用程序 - 提供了经常需要在应用程序开发的许多实用程序类。

验证 -提供标准的故障安全验证机制。

最佳实践 - 强调最佳的做法。

包介绍

| Package                                                      | Description                                                  |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [com.google.common.annotations](https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/annotations/package-summary.html) | 常用注解                                                     |
| [com.google.common.base](https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/base/package-summary.html) | 基础工具库和接口                                             |
| [com.google.common.cache](https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/cache/package-summary.html) | 缓存工具                                                     |
| [com.google.common.collect](https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/collect/package-summary.html) | java中collection 集合相关工具                                |
| [com.google.common.escape](https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/escape/package-summary.html) | Interfaces, utilities, and simple implementations of escapers and encoders. |
| [com.google.common.eventbus](https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/eventbus/package-summary.html) | 消息总线                                                     |
| [com.google.common.graph](https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/graph/package-summary.html) | 图（节点和边）相关的API                                      |
| [com.google.common.hash](https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/hash/package-summary.html) | 哈希函数相关                                                 |
| [com.google.common.html](https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/html/package-summary.html) | HTML解析                                                     |
| [com.google.common.io](https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/io/package-summary.html) | IO工具类                                                     |
| [com.google.common.math](https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/math/package-summary.html) | 基本类型和BigInteger的数学运算工具                           |
| [com.google.common.net](https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/net/package-summary.html) | 处理IP地址工具类                                             |
| [com.google.common.primitives](https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/primitives/package-summary.html) | 处理8种基本类型、void、无符号类型                            |
| [com.google.common.reflect](https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/reflect/package-summary.html) | 反射工具                                                     |
| [com.google.common.util.concurrent](https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/util/concurrent/package-summary.html) | 并发工具                                                     |
| [com.google.common.xml](https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/xml/package-summary.html) | XML解析工具                                                  |

# 安装

```xml
<!-- https://mvnrepository.com/artifact/com.google.guava/guava -->
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>31.0.1-jre</version>
</dependency>
```

还提供了安卓版本的

```xml
<!-- https://mvnrepository.com/artifact/com.google.guava/guava -->
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>31.0.1-android</version>
</dependency>
```

# 基本工具

## 非空判断Optional

JDK 中也有Optional，推荐用JDK自带的，二者用法差不多，JDK就是从Guava的Optional 借鉴过去的吧。

不同的是Guava的Optional 是抽象类，JDK的Optional 是final类

先看**Guava的Optional**

```java
public Integer sum(Optional<Integer> a, Optional<Integer> b){
    //Optional.isPresent - checks the value is present or not
    System.out.println("First parameter is present: " + a.isPresent());

    System.out.println("Second parameter is present: " + b.isPresent());

    //Optional.or - returns the value if present otherwise returns
    //the default value passed.
    Integer value1 = a.or(0);

    //Optional.get - gets the value, value should be present
    Integer value2 = b.get();

    return value1 + value2;
}
@Test
void test01() {

    Integer value1 =  null;
    Integer value2 = 10;
    //Optional.fromNullable - allows passed parameter to be null.
    Optional<Integer> a = Optional.fromNullable(value1);
    //Optional.of - throws NullPointerException if passed parameter is null
    Optional<Integer> b = Optional.of(value2);
}
```

JDK的Optional

```java
    public Integer sum(Optional<Integer> a, Optional<Integer> b){
        //Optional.isPresent - checks the value is present or not
        System.out.println("First parameter is present: " + a.isPresent());

        System.out.println("Second parameter is present: " + b.isPresent());

        //Optional.or - returns the value if present otherwise returns
        //the default value passed.
        Integer value1 = a.orElse(0);

        //Optional.get - gets the value, value should be present
        Integer value2 = b.get();

        return value1 + value2;
    }
    @Test
    void test01() {

        Integer value1 =  null;
        Integer value2 = 10;
        Optional<Integer> a = Optional.ofNullable(value1);
        Optional<Integer> b = Optional.of(value2);
    }
```

| S.N. |                          方法及说明                          |
| :--: | :----------------------------------------------------------: |
|  1   | **static <T> Optional<T> absent()** 返回没有包含的参考Optional的实例。 |
|  2   | **abstract Set<T> asSet()** 返回一个不可变的单集的唯一元素所包含的实例(如果存在);否则为一个空的不可变的集合。 |
|  3   | **abstract boolean equals(Object object)** 返回true如果对象是一个Optional实例，无论是包含引用彼此相等或两者都不存在。 |
|  4   | **static <T> Optional<T> fromNullable(T nullableReference)** 如果nullableReference非空，返回一个包含引用Optional实例;否则返回absent()。 |
|  5   |     **abstract T get()** 返回所包含的实例，它必须存在。      |
|  6   |       **abstract int hashCode()** 返回此实例的哈希码。       |
|  7   | **abstract boolean isPresent()** 返回true，如果这支架包含一个(非空)的实例。 |
|  8   | **static <T> Optional<T> of(T reference)** 返回包含给定的非空引用Optional实例。 |
|  9   | **abstract Optional<T> or(Optional<? extends T> secondChoice)** 返回此Optional，如果它有一个值存在; 否则返回secondChoice。 |
|  10  | **abstract T or(Supplier<? extends T> supplier)** 返回所包含的实例(如果存在); 否则supplier.get()。 |
|  11  | **abstract T or(T defaultValue)** 返回所包含的实例(如果存在);否则为默认值。 |
|  12  | **abstract T orNull()** 返回所包含的实例(如果存在);否则返回null。 |
|  13  | **static <T> Iterable<T> presentInstances(Iterable<? extends Optional<? extends T>> optionals)** 从提供的optionals返回每个实例的存在的值，从而跳过absent()。 |
|  14  |   **abstract String toString()** 返回此实例的字符串表示。    |
|  15  | **abstract <V> Optional<V> transform(Function<? super T,V> function)** 如果实例存在，则它被转换给定的功能;否则absent()被返回。 |

## 预期值判断Preconditions

Preconditions提供静态方法来检查方法或构造函数，被调用是否给定适当的参数。它检查的先决条件。其方法失败抛出IllegalArgumentException。

### 检查是否非空

```java
String param = "未读代码";
String name = Preconditions.checkNotNull(param);
System.out.println(name); // 未读代码
String param2 = null;
String name2 = Preconditions.checkNotNull(param2); // NullPointerException
System.out.println(name2);
```

使用 `Preconditions.checkNotNull` 进行非空判断，好处为觉得有两个，一是语义清晰代码优雅；二是你也可以自定义报错信息，这样如果参数为空，报错的信息清晰，可以直接定位到具体参数。

看看源码：

```java
@CanIgnoreReturnValue
public static <T> T checkNotNull(@CheckForNull T reference) {
    if (reference == null) {
        throw new NullPointerException();
    } else {
        return reference;
    }
}

@CanIgnoreReturnValue
public static <T> T checkNotNull(@CheckForNull T reference, @CheckForNull Object errorMessage) {
    if (reference == null) {
        throw new NullPointerException(String.valueOf(errorMessage));
    } else {
        return reference;
    }
}
```

### 预期值判断

和非空判断类似，可以比较当前值和预期值，如果不相等可以自定义报错信息抛出。

```java
String param = "www.wdbyte.com2";
String wdbyte = "www.wdbyte.com";
Preconditions.checkArgument(wdbyte.equals(param), "[%s] 404 NOT FOUND", param);
// java.lang.IllegalArgumentException: [www.wdbyte.com2] 404 NOT FOUND
```

### 是否越界

`Preconditions` 类还可以用来检查数组和集合的元素获取是否越界。

```java
// Guava 中快速创建ArrayList
List<String> list = Lists.newArrayList("a", "b", "c", "d");
// 开始校验
int index = Preconditions.checkElementIndex(5, list.size());
// java.lang.IndexOutOfBoundsException: index (5) must be less than size (4)
```

代码中快速创建 List 的方式也是 Guava 提供的，后面会详细介绍 Guava 中集合创建的超多姿势。



# 集合

## 不可变的集合

注：Java9中也开始出现了通过of()方法来创建不可变的集合,

创建一个**不能删除、不能修改、不能增加元素**的集合

总的来说有下面几个优点：

1. 线程安全，因为不能修改任何元素，可以随意多线程使用且没有并发问题。
2. 可以无忧的提供给第三方使用，反正修改不了。
3. 减少内存占用，因为不能改变，所以内部实现可以最大程度节约内存占用。
4. 可以用作常量集合。



```java
// 创建方式1：of
ImmutableSet<String> immutableSet = ImmutableSet.of("a", "b", "c");
immutableSet.forEach(System.out::println);
// a
// b
// c

// 创建方式2：builder
ImmutableSet<String> immutableSet2 = ImmutableSet.<String>builder()
    .add("hello")
    .add(new String("未读代码"))
    .build();
immutableSet2.forEach(System.out::println);
// hello
// 未读代码

// 创建方式3：从其他集合中拷贝创建
ArrayList<String> arrayList = new ArrayList();
arrayList.add("www.wdbyte.com");
arrayList.add("https");
ImmutableSet<String> immutableSet3 = ImmutableSet.copyOf(arrayList);
immutableSet3.forEach(System.out::println);
// www.wdbyte.com
// https
```

都可以正常打印遍历结果，但是如果进行增删改，会直接报 `UnsupportedOperationException` .

其实 JDK 中也提供了一个不可变集合，可以像下面这样创建。

```java
ArrayList<String> arrayList = new ArrayList();
arrayList.add("www.wdbyte.com");
arrayList.add("https");
// JDK Collections 创建不可变 List
List<String> list = Collections.unmodifiableList(arrayList);
list.forEach(System.out::println);// www.wdbyte.com https
list.add("未读代码"); // java.lang.UnsupportedOperationException
```

### 注意事项

1. 使用 Guava 创建的不可变集合是拒绝 `null` 值的，因为在 Google 内部调查中，95% 的情况下都不需要放入 `null` 值。

2. 使用 JDK 提供的不可变集合创建成功后，原集合添加元素会体现在不可变集合中，而 Guava 的不可变集合不会有这个问题。

   ```java
   List<String> arrayList = new ArrayList<>();
   arrayList.add("a");
   arrayList.add("b");
   List<String> jdkList = Collections.unmodifiableList(arrayList);
   ImmutableList<String> immutableList = ImmutableList.copyOf(arrayList);
   arrayList.add("ccc");
   jdkList.forEach(System.out::println);// result: a b ccc
   System.out.println("-------");
   immutableList.forEach(System.out::println);// result: a b
   ```

3. 如果不可变集合的元素是引用对象，那么引用对象的属性是可以更改的。

**其他不可变集合**

不可变集合除了上面演示的 `set` 之外，还有很多不可变集合，下面是 Guava 中不可变集合和其他集合的对应关系。

| **可变集合接口**                                             | 属于JDK还是Guava | **不可变版本**                                               |
| :----------------------------------------------------------- | :--------------- | :----------------------------------------------------------- |
| Collection                                                   | JDK              | [`ImmutableCollection`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableCollection.html) |
| List                                                         | JDK              | [`ImmutableList`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableList.html) |
| Set                                                          | JDK              | [`ImmutableSet`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableSet.html) |
| SortedSet/NavigableSet                                       | JDK              | [`ImmutableSortedSet`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableSortedSet.html) |
| Map                                                          | JDK              | [`ImmutableMap`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableMap.html) |
| SortedMap                                                    | JDK              | [`ImmutableSortedMap`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableSortedMap.html) |
| [Multiset](http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#Multiset) | Guava            | [`ImmutableMultiset`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableMultiset.html) |
| SortedMultiset                                               | Guava            | [`ImmutableSortedMultiset`](http://docs.guava-libraries.googlecode.com/git-history/release12/javadoc/com/google/common/collect/ImmutableSortedMultiset.html) |
| [Multimap](http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#Multimap) | Guava            | [`ImmutableMultimap`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableMultimap.html) |
| ListMultimap                                                 | Guava            | [`ImmutableListMultimap`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableListMultimap.html) |
| SetMultimap                                                  | Guava            | [`ImmutableSetMultimap`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableSetMultimap.html) |
| [BiMap](http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#BiMap) | Guava            | [`ImmutableBiMap`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableBiMap.html) |
| [ClassToInstanceMap](http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#ClassToInstanceMap) | Guava            | [`ImmutableClassToInstanceMap`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableClassToInstanceMap.html) |
| [Table](http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#Table) | Guava            | [`ImmutableTable`](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableTable.html) |



## 集合操作工厂

其实这里只会介绍一个创建方法，但是为什么还是单独拿出来介绍了呢？看下去你就会**大呼好用**。虽然 JDK 中已经提供了大量的集合相关的操作方法，用起来也是非常的方便，但是 Guava 还是增加了一些十分好用的方法，保证让你用上一次就爱不释手，

### 创建集合。

```java
// 创建一个 ArrayList 集合
List<String> list1 = Lists.newArrayList();
// 创建一个 ArrayList 集合，同时塞入3个数据
List<String> list2 = Lists.newArrayList("a", "b", "c");
// 创建一个 ArrayList 集合，容量初始化为10
List<String> list3 = Lists.newArrayListWithCapacity(10);

LinkedList<String> linkedList1 = Lists.newLinkedList();
CopyOnWriteArrayList<String> cowArrayList = Lists.newCopyOnWriteArrayList();

HashMap<Object, Object> hashMap = Maps.newHashMap();
ConcurrentMap<Object, Object> concurrentMap = Maps.newConcurrentMap();
TreeMap<Comparable, Object> treeMap = Maps.newTreeMap();

HashSet<Object> hashSet = Sets.newHashSet();
HashSet<String> newHashSet = Sets.newHashSet("a", "a", "b", "c");
```

Guava 为每一个集合都添加了工厂方法创建方式，上面已经展示了部分集合的工厂方法创建方式。是不是十分的好用呢。而且可以在创建时直接扔进去几个元素，这个简直太赞了，再也不用一个个 `add` 了。

### 集合交集并集差集

过于简单，直接看代码和输出结果吧。

```java
Set<String> newHashSet1 = Sets.newHashSet("a", "a", "b", "c");
Set<String> newHashSet2 = Sets.newHashSet("b", "b", "c", "d");

// 交集
SetView<String> intersectionSet = Sets.intersection(newHashSet1, newHashSet2);
System.out.println(intersectionSet); // [b, c]

// 并集
SetView<String> unionSet = Sets.union(newHashSet1, newHashSet2);
System.out.println(unionSet); // [a, b, c, d]

// newHashSet1 中存在，newHashSet2 中不存在
SetView<String> setView = Sets.difference(newHashSet1, newHashSet2);
System.out.println(setView); // [a]
```

## 有数量的集合

这个真的太有用了，因为我们经常会需要设计可以计数的集合，或者 value 是 `List` 的 `Map` 集合，如果说你不太明白，看下面这段代码，是否某天夜里你也这样写过。

1. 统计相同元素出现的次数（下面的代码我已经尽可能精简写法了）。

   JDK 原生写法：

   ```java
   // Java 统计相同元素出现的次数。
   List<String> words = Lists.newArrayList("a", "b", "c", "d", "a", "c");
   Map<String, Integer> countMap = new HashMap<String, Integer>();
   for (String word : words) {
       Integer count = countMap.get(word);
       count = (count == null) ? 1 : ++count;
       countMap.put(word, count);
   }
   countMap.forEach((k, v) -> System.out.println(k + ":" + v));
   /**
    * result:
    * a:2
    * b:1
    * c:2
    * d:1
    */
   ```

   尽管已经尽量优化代码，代码量还是不少的，那么在 Guava 中有什么不一样呢？在 Guava. 中主要是使用 `HashMultiset` 类，看下面。

   ```java
   ArrayList<String> arrayList = Lists.newArrayList("a", "b", "c", "d", "a", "c");
   HashMultiset<String> multiset = HashMultiset.create(arrayList);
   multiset.elementSet().forEach(s -> System.out.println(s + ":" + multiset.count(s)));
   /**
    * result:
    * a:2
    * b:1
    * c:2
    * d:1
    */
   ```

   是的，只要把元素添加进去就行了，不用在乎是否重复，最后都可以使用 `count` 方法统计重复元素数量。看着舒服，写着优雅，`HashMultiset` 是 Guava 中实现的 `Collection` 类，可以轻松统计元素数量。

2. 一对多，value 是 `List` 的 `Map` 集合。

   假设一个场景，需要把很多动物按照种类进行分类，我相信最后你会写出类似的代码。

   JDK 原生写法：

   ```java
   HashMap<String, Set<String>> animalMap = new HashMap<>();
   HashSet<String> dogSet = new HashSet<>();
   dogSet.add("旺财");
   dogSet.add("大黄");
   animalMap.put("狗", dogSet);
   HashSet<String> catSet = new HashSet<>();
   catSet.add("加菲");
   catSet.add("汤姆");
   animalMap.put("猫", catSet);
   System.out.println(animalMap.get("猫")); // [加菲, 汤姆]
   ```

   最后一行查询猫得到了猫类的 "加菲" 和 ”汤姆“。这个代码简直太烦做了，如果使用 Guava 呢？

   ```java
   // use guava
   HashMultimap<String, String> multimap = HashMultimap.create();
   multimap.put("狗", "大黄");
   multimap.put("狗", "旺财");
   multimap.put("猫", "加菲");
   multimap.put("猫", "汤姆");
   System.out.println(multimap.get("猫")); // [加菲, 汤姆]
   ```

   HashMultimap 可以扔进去重复的 key 值，最后获取时可以得到所有的 value 值，可以看到输出结果和 JDK 写法上是一样的，但是代码已经无比清爽。

# 字符串操作

作为开发中最长使用的数据类型，字符串操作的增强可以让开发更加高效。

## 字符拼接

JDK 8 中其实已经内置了字符串拼接方法，但是它只是简单的拼接，没有额外操作，比如过滤掉 null 元素，去除前后空格等。先看一下 JDK 8 中字符串拼接的几种方式。

```java
// JDK 方式一
ArrayList<String> list = Lists.newArrayList("a", "b", "c", null);
String join = String.join(",", list);
System.out.println(join); // a,b,c,null
// JDK 方式二
String result = list.stream().collect(Collectors.joining(","));
System.out.println(result); // a,b,c,null
// JDK 方式三
StringJoiner stringJoiner = new StringJoiner(",");
list.forEach(stringJoiner::add);
System.out.println(stringJoiner.toString()); // a,b,c,null
```

可以看到 null 值也被拼接到了字符串里，这有时候不是我们想要的，那么使用 Guava 有什么不一样呢？

```java
ArrayList<String> list = Lists.newArrayList("a", "b", "c", null);
String join = Joiner.on(",").skipNulls().join(list);
System.out.println(join); // a,b,c

String join1 = Joiner.on(",").useForNull("空值").join("旺财", "汤姆", "杰瑞", null);
System.out.println(join1); // 旺财,汤姆,杰瑞,空值
```

可以看到使用 `skipNulls()` 可以跳过空值，使用 `useFornull(String)` 可以为空值自定义显示文本。

## 字符串分割

JDK 中是自带字符串分割的，我想你也一定用过，那就是 String 的 split 方法，但是这个方法有一个问题，就是如果最后一个元素为空，那么就会丢弃，奇怪的是第一个元素为空却不会丢弃，这就十分迷惑，下面通过一个例子演示这个问题。

```
String str = ",a,,b,";
String[] splitArr = str.split(",");
Arrays.stream(splitArr).forEach(System.out::println);
System.out.println("------");
/**
 *
 * a
 * 
 * b
 * ------
 */
```

你也可以自己测试下，最后一个元素不是空，直接消失了。

如果使用 Guava 是怎样的操作方式呢？Guava 提供了 Splitter 类，并且有一系列的操作方式可以直观的控制分割逻辑。

```java
String str = ",a ,,b ,";
Iterable<String> split = Splitter.on(",")
    .omitEmptyStrings() // 忽略空值
    .trimResults() // 过滤结果中的空白
    .split(str);
split.forEach(System.out::println);
/**
 * a
 * b
 */
```

# 缓存

在开发中我们可能需要使用小规模的缓存，来提高访问速度。这时引入专业的缓存中间件可能又觉得浪费。现在可以了， Guava 中提供了简单的缓存类，且可以根据预计容量、过期时间等自动过期已经添加的元素。即使这样我们也要预估好可能占用的内存空间，以防内存占用过多。

现在看一下在 Guava 中缓存该怎么用。

```java
@Test
public void testCache() throws ExecutionException, InterruptedException {

    CacheLoader cacheLoader = new CacheLoader<String, Animal>() {
        // 如果找不到元素，会调用这里
        @Override
        public Animal load(String s) {
            return null;
        }
    };
    LoadingCache<String, Animal> loadingCache = CacheBuilder.newBuilder()
        .maximumSize(1000) // 容量
        .expireAfterWrite(3, TimeUnit.SECONDS) // 过期时间
        .removalListener(new MyRemovalListener()) // 失效监听器
        .build(cacheLoader); //
    loadingCache.put("狗", new Animal("旺财", 1));
    loadingCache.put("猫", new Animal("汤姆", 3));
    loadingCache.put("狼", new Animal("灰太狼", 4));

    loadingCache.invalidate("猫"); // 手动失效

    Animal animal = loadingCache.get("狼");
    System.out.println(animal);
    Thread.sleep(4 * 1000);
    // 狼已经自动过去，获取为 null 值报错
    System.out.println(loadingCache.get("狼"));
    /**
     * key=猫,value=Animal{name='汤姆', age=3},reason=EXPLICIT
     * Animal{name='灰太狼', age=4}
     * key=狗,value=Animal{name='旺财', age=1},reason=EXPIRED
     * key=狼,value=Animal{name='灰太狼', age=4},reason=EXPIRED
     *
     * com.google.common.cache.CacheLoader$InvalidCacheLoadException: CacheLoader returned null for key 狼.
     */
}

/**
 * 缓存移除监听器
 */
class MyRemovalListener implements RemovalListener<String, Animal> {

    @Override
    public void onRemoval(RemovalNotification<String, Animal> notification) {
        String reason = String.format("key=%s,value=%s,reason=%s", notification.getKey(), notification.getValue(), notification.getCause());
        System.out.println(reason);
    }
}

class Animal {
    private String name;
    private Integer age;

    @Override
    public String toString() {
        return "Animal{" +
            "name='" + name + '\'' +
            ", age=" + age +
            '}';
    }

    public Animal(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
```

这个例子中主要分为 CacheLoader、MyRemovalListener、LoadingCache。

CacheLoader 中重写了 `load` 方法，这个方法会在查询缓存没有命中时被调用，我这里直接返回了 `null`，其实这样会在没有命中时抛出 `CacheLoader returned null for key` 异常信息。

MyRemovalListener 作为缓存元素失效时的监听类，在有元素缓存失效时会自动调用 `onRemoval` 方法，这里需要注意的是这个方法是同步方法，如果这里耗时较长，会阻塞直到处理完成。

LoadingCache 就是缓存的主要操作对象了，常用的就是其中的 `put` 和 `get` 方法了。

1

