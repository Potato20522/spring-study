参考来源：

https://www.cnblogs.com/df888/p/15013017.html

https://www.jianshu.com/p/477f2ded7ccc

https://blog.csdn.net/ryo1060732496/article/details/80823696

官网文档：https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests

# 介绍

参数化测试：输入不同的测试用例，多次运行测试方法

**@ParameterizedTest 开启参数化测试**

**测试用例来源注解：**

@ValueSource

@NullSource、@EmptySource、@NullAndEmptySource

@EnumSource

@MethodSource

@CsvSource
@CsvFileSource

@ArgumentsSource

# @ValueSource

`@ValueSource`是最简单的参数化方式，传入数组，支持以下数据类型：

- `short`
- `byte`
- `int`
- `long`
- `float`
- `double`
- `char`
- `boolean`
- `java.lang.String`
- `java.lang.Class`

示例：

```java
@ParameterizedTest
@ValueSource(ints = { 1, 2, 3 })
void testWithValueSource(int argument) {
    assertTrue(argument > 0 && argument < 4);
}
```


```java
@ParameterizedTest
@ValueSource(strings = { "racecar", "radar", "able was I ere I saw elba" })
void palindromes(String candidate) {
    assertTrue(StringUtils.isPalindrome(candidate));
}
```



# Null and Empty Sources

涉及三个注解：@NullSource、@EmptySource、@NullAndEmptySource

- `@NullSource` 值为null

  > 不能用在类型的测试方法。

- `@EmptySource` 值为空，根据测试方法的参数类决定数据类型，支持`java.lang.String`, `java.util.List`, `java.util.Set`, `java.util.Map`, 基元类型数组 (`int[]`, `char[][]`等), 对象数组 (`String[]`, `Integer[][]`等)

- `@NullAndEmptySource` 结合了前面两个

```java
@ParameterizedTest
@NullSource
@EmptySource
@ValueSource(strings = { " ", "   ", "\t", "\n" })
void nullEmptyAndBlankStrings(String text) {
    assertTrue(text == null || text.trim().isEmpty());
}
```

等价于：

```java
@ParameterizedTest
@NullAndEmptySource
@ValueSource(strings = { " ", "   ", "\t", "\n" })
void nullEmptyAndBlankStrings(String text) {
    assertTrue(text == null || text.trim().isEmpty());
}
```

# @EnumSource

参数化的值为枚举类型。

示例：

```java
@ParameterizedTest
@EnumSource
void testWithEnumSourceWithAutoDetection(ChronoUnit unit) {
    assertNotNull(unit);
}
```

其中的ChronoUnit是个日期枚举类。

ChronoUnit是接口TemporalUnit的实现类，如果测试方法的参数为TemporalUnit，那么需要给`@EnumSource`加上值：

```java
@ParameterizedTest
@EnumSource(ChronoUnit.class)
void testWithEnumSource(TemporalUnit unit) {
    assertNotNull(unit);
}
```

因为JUnit5规定了`@EnumSource`的默认值的类型必须是枚举类型。

names属性用来指定使用哪些特定的枚举值：

```java
@ParameterizedTest
@EnumSource(names = { "DAYS", "HOURS" })
void testWithEnumSourceInclude(ChronoUnit unit) {
    assertTrue(EnumSet.of(ChronoUnit.DAYS, ChronoUnit.HOURS).contains(unit));
}
```

mode属性用来指定使用模式，比如排除哪些枚举值：

```java
@ParameterizedTest
@EnumSource(mode = EXCLUDE, names = { "ERAS", "FOREVER" })
void testWithEnumSourceExclude(ChronoUnit unit) {
    assertFalse(EnumSet.of(ChronoUnit.ERAS, ChronoUnit.FOREVER).contains(unit));
}
```

比如采用正则匹配：

```java
@ParameterizedTest
@EnumSource(mode = MATCH_ALL, names = "^.*DAYS$")
void testWithEnumSourceRegex(ChronoUnit unit) {
    assertTrue(unit.name().endsWith("DAYS"));
}
```

# @MethodSource

可以调用测试类或外部类的一个或多个工厂方法。

此类工厂方法必须返回**流**、**可迭代、迭代器或参数数组**。比如：Stream, DoubleStream, LongStream, IntStream, Collection, Iterator, Iterable, 对象数组, 或者基元类型数组

此外，这种工厂方法**不能接受任何参数**。
测试类中的工厂方法必须是静态的，除非用@TestInstance(Lifecycle.PER_CLASS)注释测试类;
然而，外部类中的工厂方法必须始终是静态的。



如果只需要一个参数，可以返回参数类型实例的 Stream(流)，如下面的示例所示。

```java
@ParameterizedTest
@MethodSource("stringProvider")
void testWithSimpleMethodSource(String argument) {
    assertNotNull(argument);
}

static Stream<String> stringProvider() {
    return Stream.of("foo", "bar");
}
```

## 自动搜索

如果没有通过@MethodSource显式提供工厂方法名称，JUnit Jupiter将会**搜索与当前的@ParameterizedTest方法同名的工厂方法**。

下面的示例演示了这一点。

ps: 这是一个很人性化的设计，但是反过来说，这个设计意义不大。（会导致不规范，后期全部要维护两套。）

```java
@ParameterizedTest
@MethodSource
void testWithSimpleMethodSourceHavingNoValue(String argument) {
    assertNotNull(argument);
}

static Stream<String> testWithSimpleMethodSourceHavingNoValue() {
    return Stream.of("foo", "bar");
}
```

## 原始类型的流

原始类型的流(DoubleStream、IntStream和LongStream)也得到了支持，如下例所示。

```java
@ParameterizedTest
@MethodSource("range")
void testWithRangeMethodSource(int argument) {
    assertNotEquals(9, argument);
}

static IntStream range() {
    return IntStream.range(0, 20).skip(10);
}
```

## 多个参数

如果测试方法声明多个参数，您需要返回参数实例的集合或流，如下所示。
注意， `Arguments.of(Object…)` 是在参数接口中定义的静态工厂方法。

```java
@ParameterizedTest
@MethodSource("stringIntAndListProvider")
void testWithMultiArgMethodSource(String str, int num, List<String> list) {
    assertEquals(3, str.length());
    assertTrue(num >=1 && num <=2);
    assertEquals(2, list.size());
}

static Stream<Arguments> stringIntAndListProvider() {
    return Stream.of(
        Arguments.of("foo", 1, Arrays.asList("a", "b")),
        Arguments.of("bar", 2, Arrays.asList("x", "y"))
    );
}
```

## 外部的静态工厂方法

一个外部的静态工厂方法可以通过提供其**完全限定的方法名称**来引用，可以调用外部的工厂方法，如下面的示例所示。

```java
package example;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ExternalMethodSourceDemo {

    @ParameterizedTest
    @MethodSource("example.StringsProviders#blankStrings")
    void testWithExternalMethodSource(String blankString) {
        // test with blank string
    }
}

class StringsProviders {

    static Stream<String> blankStrings() {
        return Stream.of("", " ", " \n ");
    }
}
```

# @CsvSource

参数化的值为csv格式的数据（默认逗号分隔），比如：

```java
@ParameterizedTest
@CsvSource({
    "apple,         1",
    "banana,        2",
    "'lemon, lime', 0xF1"
})
void testWithCsvSource(String fruit, int rank) {
    assertNotNull(fruit);
    assertNotEquals(0, rank);
}
```

@CsvSource注解的delimiter属性可以设置分隔字符。delimiterString属性可以设置分隔字符串（String而非char）

| Example Input                                                | Resulting Argument List       |
| :----------------------------------------------------------- | :---------------------------- |
| `@CsvSource({ "apple, banana" })`                            | `"apple"`, `"banana"`         |
| `@CsvSource({ "apple, 'lemon, lime'" })`                     | `"apple"`, `"lemon, lime"`    |
| `@CsvSource({ "apple, ''" })`                                | `"apple"`, `""`               |
| `@CsvSource({ "apple, " })`                                  | `"apple"`, `null`             |
| `@CsvSource(value = { "apple, banana, NIL" }, nullValues = "NIL")` | `"apple"`, `"banana"`, `null` |
| `@CsvSource(value = { " apple , banana" }, ignoreLeadingAndTrailingWhitespace = false)` | `" apple "`, `" banana"`      |



```java
@ParameterizedTest(name = "[{index}] {arguments}")
@CsvSource(useHeadersInDisplayName = true, textBlock = """
    FRUIT,         RANK
    apple,         1
    banana,        2
    'lemon, lime', 0xF1
    strawberry,    700_000
    """)
void testWithCsvSource(String fruit, int rank) {
    // ...
}
```

```
[1] FRUIT = apple, RANK = 1
[2] FRUIT = banana, RANK = 2
[3] FRUIT = lemon, lime, RANK = 0xF1
[4] FRUIT = strawberry, RANK = 700_000
```



注意，如果把null赋值给基本数据类型，那么会报异常`ArgumentConversionException`。

# @CsvFileSource

顾名思义，选择本地csv文件作为数据来源。

```java
@ParameterizedTest
@CsvFileSource(resources = "/two-column.csv", numLinesToSkip = 1)
void testWithCsvFileSourceFromClasspath(String country, int reference) {
    assertNotNull(country);
    assertNotEquals(0, reference);
}

@ParameterizedTest
@CsvFileSource(files = "src/test/resources/two-column.csv", numLinesToSkip = 1)
void testWithCsvFileSourceFromFile(String country, int reference) {
    assertNotNull(country);
    assertNotEquals(0, reference);
}

```

delimiter属性可以设置分隔字符。delimiterString属性可以设置分隔字符串（String而非char）。**需要特别注意的是，`#`开头的行会被认为是注释而略过。**

two-column.csv:

```csv
COUNTRY, REFERENCE
Sweden, 1
Poland, 2
"United States of America", 3
France, 700_000
```

# @ArgumentsSource

自定义ArgumentsProvider，也就是通过ArgumentsProvider对象来给测试方法入参赋值。

```java
@ParameterizedTest
@ArgumentsSource(MyArgumentsProvider.class)
void testWithArgumentsSource(String argument) {
    assertNotNull(argument);
}
```

实现ArgumentsProvider接口

```java
public class MyArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of("apple", "banana").map(Arguments::of);
    }
}
```

MyArgumentsProvider必须是外部类或者static内部类。

# 参数类型转换

## 隐式转换

JUnit Jupiter会对String类型进行隐式转换。比如：

```java
@ParameterizedTest
@ValueSource(strings = "SECONDS")
void testWithImplicitArgumentConversion(ChronoUnit argument) {
    assertNotNull(argument.name());
}
```

更多转换示例：

| Target Type                | Example                                                      |
| :------------------------- | :----------------------------------------------------------- |
| `boolean`/`Boolean`        | `"true"` → `true`                                            |
| `byte`/`Byte`              | `"15"`, `"0xF"`, or `"017"` → `(byte) 15`                    |
| `char`/`Character`         | `"o"` → `'o'`                                                |
| `short`/`Short`            | `"15"`, `"0xF"`, or `"017"` → `(short) 15`                   |
| `int`/`Integer`            | `"15"`, `"0xF"`, or `"017"` → `15`                           |
| `long`/`Long`              | `"15"`, `"0xF"`, or `"017"` → `15L`                          |
| `float`/`Float`            | `"1.0"` → `1.0f`                                             |
| `double`/`Double`          | `"1.0"` → `1.0d`                                             |
| `Enum` subclass            | `"SECONDS"` → `TimeUnit.SECONDS`                             |
| `java.io.File`             | `"/path/to/file"` → `new File("/path/to/file")`              |
| `java.lang.Class`          | `"java.lang.Integer"` → `java.lang.Integer.class` *(use `$` for nested classes, e.g. `"java.lang.Thread$State"`)* |
| `java.lang.Class`          | `"byte"` → `byte.class` *(primitive types are supported)*    |
| `java.lang.Class`          | `"char[]"` → `char[].class` *(array types are supported)*    |
| `java.math.BigDecimal`     | `"123.456e789"` → `new BigDecimal("123.456e789")`            |
| `java.math.BigInteger`     | `"1234567890123456789"` → `new BigInteger("1234567890123456789")` |
| `java.net.URI`             | `"https://junit.org/"` → `URI.create("https://junit.org/")`  |
| `java.net.URL`             | `"https://junit.org/"` → `new URL("https://junit.org/")`     |
| `java.nio.charset.Charset` | `"UTF-8"` → `Charset.forName("UTF-8")`                       |
| `java.nio.file.Path`       | `"/path/to/file"` → `Paths.get("/path/to/file")`             |
| `java.time.Duration`       | `"PT3S"` → `Duration.ofSeconds(3)`                           |
| `java.time.Instant`        | `"1970-01-01T00:00:00Z"` → `Instant.ofEpochMilli(0)`         |
| `java.time.LocalDateTime`  | `"2017-03-14T12:34:56.789"` → `LocalDateTime.of(2017, 3, 14, 12, 34, 56, 789_000_000)` |
| `java.time.LocalDate`      | `"2017-03-14"` → `LocalDate.of(2017, 3, 14)`                 |
| `java.time.LocalTime`      | `"12:34:56.789"` → `LocalTime.of(12, 34, 56, 789_000_000)`   |
| `java.time.MonthDay`       | `"--03-14"` → `MonthDay.of(3, 14)`                           |
| `java.time.OffsetDateTime` | `"2017-03-14T12:34:56.789Z"` → `OffsetDateTime.of(2017, 3, 14, 12, 34, 56, 789_000_000, ZoneOffset.UTC)` |
| `java.time.OffsetTime`     | `"12:34:56.789Z"` → `OffsetTime.of(12, 34, 56, 789_000_000, ZoneOffset.UTC)` |
| `java.time.Period`         | `"P2M6D"` → `Period.of(0, 2, 6)`                             |
| `java.time.YearMonth`      | `"2017-03"` → `YearMonth.of(2017, 3)`                        |
| `java.time.Year`           | `"2017"` → `Year.of(2017)`                                   |
| `java.time.ZonedDateTime`  | `"2017-03-14T12:34:56.789Z"` → `ZonedDateTime.of(2017, 3, 14, 12, 34, 56, 789_000_000, ZoneOffset.UTC)` |
| `java.time.ZoneId`         | `"Europe/Berlin"` → `ZoneId.of("Europe/Berlin")`             |
| `java.time.ZoneOffset`     | `"+02:30"` → `ZoneOffset.ofHoursMinutes(2, 30)`              |
| `java.util.Currency`       | `"JPY"` → `Currency.getInstance("JPY")`                      |
| `java.util.Locale`         | `"en"` → `new Locale("en")`                                  |
| `java.util.UUID`           | `"d043e930-7b3b-48e3-bdbe-5a3ccfb833db"` → `UUID.fromString("d043e930-7b3b-48e3-bdbe-5a3ccfb833db")` |

也可以把String转换为自定义对象：

```java
@ParameterizedTest
@ValueSource(strings = "42 Cats")
void testWithImplicitFallbackArgumentConversion(Book book) {
    assertEquals("42 Cats", book.getTitle());
}
```



```java
public class Book {

    private final String title;

    private Book(String title) {
        this.title = title;
    }

    public static Book fromTitle(String title) {
        return new Book(title);
    }

    public String getTitle() {
        return this.title;
    }
}
```

JUnit Jupiter会找到`Book.fromTitle(String)`方法，然后把`@ValueSource`的值传入进去，进而把String类型转换为Book类型。转换的factory方法既可以是接受单个String参数的构造方法，也可以是接受单个String参数并返回目标类型的普通方法。

## 显式转换

显式转换需要使用@ConvertWith注解：

```java
@ParameterizedTest
@EnumSource(ChronoUnit.class)
void testWithExplicitArgumentConversion(
        @ConvertWith(ToStringArgumentConverter.class) String argument) {

    assertNotNull(ChronoUnit.valueOf(argument));
}
```

并实现ArgumentConverter：

```java
public class ToStringArgumentConverter extends SimpleArgumentConverter {

    @Override
    protected Object convert(Object source, Class<?> targetType) {
        assertEquals(String.class, targetType, "Can only convert to String");
        if (source instanceof Enum<?>) {
            return ((Enum<?>) source).name();
        }
        return String.valueOf(source);
    }
}

```

如果只是简单类型转换，实现TypedArgumentConverter即可：

```java
public class ToLengthArgumentConverter extends TypedArgumentConverter<String, Integer> {

    protected ToLengthArgumentConverter() {
        super(String.class, Integer.class);
    }

    @Override
    protected Integer convert(String source) {
        return source.length();
    }

}

```

JUnit Jupiter只内置了一个JavaTimeArgumentConverter，通过`@JavaTimeConversionPattern`使用：

```java
@ParameterizedTest
@ValueSource(strings = { "01.01.2017", "31.12.2017" })
void testWithExplicitJavaTimeConverter(
        @JavaTimeConversionPattern("dd.MM.yyyy") LocalDate argument) {

    assertEquals(2017, argument.getYear());
}

```

# 参数聚合

测试方法的多个参数可以聚合为一个ArgumentsAccessor参数，然后通过get来取值，示例：

```java
@ParameterizedTest
@CsvSource({
    "Jane, Doe, F, 1990-05-20",
    "John, Doe, M, 1990-10-22"
})
void testWithArgumentsAccessor(ArgumentsAccessor arguments) {
    Person person = new Person(arguments.getString(0),
                               arguments.getString(1),
                               arguments.get(2, Gender.class),
                               arguments.get(3, LocalDate.class));

    if (person.getFirstName().equals("Jane")) {
        assertEquals(Gender.F, person.getGender());
    }
    else {
        assertEquals(Gender.M, person.getGender());
    }
    assertEquals("Doe", person.getLastName());
    assertEquals(1990, person.getDateOfBirth().getYear());
}

```

也可以自定义Aggregator：

```java
public class PersonAggregator implements ArgumentsAggregator {
    @Override
    public Person aggregateArguments(ArgumentsAccessor arguments, ParameterContext context) {
        return new Person(arguments.getString(0),
                          arguments.getString(1),
                          arguments.get(2, Gender.class),
                          arguments.get(3, LocalDate.class));
    }
}

```

然后通过`@AggregateWith`来使用：

```java
@ParameterizedTest
@CsvSource({
    "Jane, Doe, F, 1990-05-20",
    "John, Doe, M, 1990-10-22"
})
void testWithArgumentsAggregator(@AggregateWith(PersonAggregator.class) Person person) {
    // perform assertions against person
}

```

## 利用参数聚合简化代码

定义注解并表上@AggregateWith(PersonAggregator.class) 

PersonAggregator实现了ArgumentsAggregator接口

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@AggregateWith(PersonAggregator.class)
public @interface CsvToPerson {
}

```

```java
@ParameterizedTest
@CsvSource({
    "Jane, Doe, F, 1990-05-20",
    "John, Doe, M, 1990-10-22"
})
void testWithCustomAggregatorAnnotation(@CsvToPerson Person person) {
    // perform assertions against person
}

```

# 自定义显示名字

参数化测试生成的test，JUnit Jupiter给定了默认名字，我们可以通过name属性进行自定义。

示例：

```java
@DisplayName("Display name of container")
@ParameterizedTest(name = "{index} ==> the rank of ''{0}'' is {1}")
@CsvSource({ "apple, 1", "banana, 2", "'lemon, lime', 3" })
void testWithCustomDisplayNames(String fruit, int rank) {
}
```

结果：

```
Display name of container ✔
├─ 1 ==> the rank of 'apple' is 1 ✔
├─ 2 ==> the rank of 'banana' is 2 ✔
└─ 3 ==> the rank of 'lemon, lime' is 3 ✔
```

