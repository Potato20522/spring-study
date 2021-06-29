# 介绍

Jackson（com.fasterxml.jackson）是SpringBoot自带的JSON解析库，项目中直接使用就行，不需要手动添加依赖。

Jackson可以将Java对象转换为json对象和xml文档，同样也可以将json、xml转换为Java对象。Jackson依赖的jar包较少，简单易用性能高，社区活跃，更新速度快。

# ObjectMapper

[Jackson之ObjectMapper对象的使用_blwinner的专栏-CSDN博客](https://blog.csdn.net/blwinner/article/details/99942211)

**示例**

```java
public class Car {
    private String brand = null;
    private int doors = 0;

    public String getBrand() { return this.brand; }
    public void   setBrand(String brand){ this.brand = brand;}

    public int  getDoors() { return this.doors; }
    public void setDoors (int doors) { this.doors = doors; }
}
...........................................................
ObjectMapper objectMapper = new ObjectMapper();
String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";
try {
    Car car = objectMapper.readValue(carJson, Car.class);
    System.out.println("car brand = " + car.getBrand());
    System.out.println("car doors = " + car.getDoors());
} catch (IOException e) {
    e.printStackTrace();
}

```

如例所见, `readValue()`方法的第一个参数是JSON数据源(字符串, 流或者文件), 第二个参数是解析目标Java类, 这里传入的是`Car.class`

## 反序列化

### JSON==>Java对象

要想从JSON正确的读取到Java对象, 那么了解Jackson是怎么从JSON对象映射到Java对象就非常重要
默认情况下, Jackson映射一个JSON对象的属性到Java对象, 是用JSON属性的名字在Java对象中查找匹配的`getter/setter`方法. Jackson移除了`getter/setter`方法名中的`get/set`字符部分, 并把方法名剩余字符的第一个字符小写, 得到的就是JSON属性名.
在上一节的例子中, JSON属性的名称是`brand`, 匹配了Java类中名为`getBrand()/setBrand()`的`getter/setter`方法. JSON属性的名称是`engineNumber`, 匹配了`getEngineNumber()/setEngineNumber()`
如果想用其他方法来匹配JSON对象属性和Java对象属性, 可以自定义序列化/反序列化过程, 或者使用其他的[Jackson注解](https://blog.csdn.net/blwinner/article/details/98532847)

**1、JSON字符串-->Java对象**

**ObjectMapper.readValue()**

```java
ObjectMapper objectMapper = new ObjectMapper();
String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 4 }";
Reader reader = new StringReader(carJson);
Car car = objectMapper.readValue(reader, Car.class);

```

**2、JSON文件-->Java对象**

```java
ObjectMapper objectMapper = new ObjectMapper();
File file = new File("data/car.json");
Car car = objectMapper.readValue(file, Car.class);

```

**3、URL获取JSON数据-->Java对象**

```java
ObjectMapper objectMapper = new ObjectMapper();
URL url = new URL("file:data/car.json");
Car car = objectMapper.readValue(url, Car.class);

```

这个例子使用了文件URL, 当然也可以使用HTTP URL

**4、Java InputStream获取JSON数据-->Java对象**

```java
ObjectMapper objectMapper = new ObjectMapper();
InputStream input = new FileInputStream("data/car.json");
Car car = objectMapper.readValue(input, Car.class);

```

**5、字节数组获取JSON数据-->Java对象**

```java
ObjectMapper objectMapper = new ObjectMapper();
String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";
byte[] bytes = carJson.getBytes("UTF-8");
Car car = objectMapper.readValue(bytes, Car.class);

```

**6、JSON数组字符串-->Java对象数组**

`ObjectMapper`也可以从JSON数组字符串中读取一组Java对象

```java
String jsonArray = "[{\"brand\":\"ford\"}, {\"brand\":\"Fiat\"}]";
ObjectMapper objectMapper = new ObjectMapper();
Car[] cars2 = objectMapper.readValue(jsonArray, Car[].class);

```

**注意`Car`类数组作为`readValue()`方法第二个参数的传入方式, 告诉`ObjectMapper`期望从JSON读取一组`Car`实例**
当然了, JSON源不仅是字符串, 也可以是文件, URL, InputStream, Reader等等

**7、JSON数组字符串-->Java List对象**

```java
String jsonArray = "[{\"brand\":\"ford\"}, {\"brand\":\"Fiat\"}]";
ObjectMapper objectMapper = new ObjectMapper();
List<Car> cars1 = objectMapper.readValue(jsonArray, new TypeReference<List<Car>>(){});

```

**注意传递给`readValue()`的`TypeReference`类型参数. 这个参数告诉Jackson读取一"列"Car对象**

- Jackson通过反射来生成Java对象, 但是模板会擦除类型, 所以这里用`TypeReference`进行包装

**8、JSON字符串-->Java Map对象**

`ObjectMapper`可以从JSON字符串读取一个`Java Map`, **当你不知道要提取的JSON的格式的时候**非常有用. 一般会把JSON对象读取到一个Java`Map对象`中. 每一个JSON对象的属性都会变成Java`Map`中的键值对.

```java
String jsonObject = "{\"brand\":\"ford\", \"doors\":5}";
ObjectMapper objectMapper = new ObjectMapper();
Map<String, Object> jsonMap = objectMapper.readValue(jsonObject, new TypeReference<Map<String,Object>>(){});

```

### 特殊情况

**1、忽略Java对象没有的JSON属性**

有时候你要读取的JSON数据的属性要多于你的Java对象的属性, 默认情况下Jackson这时会抛出异常, 含义是无法在Java对象中找到未知属性XXX，但是, 我们有时候又需要允许JSON属性多于要产生的Java对象的属性. 比如, 你想从一个`REST服务`获取JSON数据, 但是它包含的内容远多于你所需要的. 这是, 通过配置Jackson的**Feature**使能可以让你忽略那些多余的属性

```java
objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
```

**注意 : 也可以用`ObjectMapper.enabled()/disabled()`方法进行配置**



**2、JSON属性值为NULL且对应Java原生类型产生的异常**

可以配置`ObjectMapper`的`Feature`, 使其在JSON字符串包含的属性值是`null`, 且该属性对应的Java对象的属性是原生类型(`primitive type: int, long, float, double等`)时, 反序列化失败抛出异常.
修改Car类的定义:

```java
@Data
public class Car {
    private String brand = null;
    private int doors = 0;
}

```

**注意属性`doors`的类型是Java原生类型`int`(而不是`Integer`)**
现在假设有一个JSON字符串要匹配Car类:

```json
{ "brand":"Toyota", "doors":null }
```

**注意属性`doors`的值是`null`**

Java中的原生数据类型的值不能是`null`. 所以ObjectMapper默认会忽略`null`值的原生类型的属性, 不过, 你也可以配置`ObjectMapper`的`Feature`让它抛出异常, 如下:

```java
ObjectMapper objectMapper = new ObjectMapper();

objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
```

通过设置`FAIL_ON_NULL_FOR_PRIMITIVES`属性是`true`, 你会在试图把`null`值的JSON属性解析为Java原生类型属性时抛出异常,如下

```java
ObjectMapper objectMapper = new ObjectMapper();
objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
String carJson = "{ \"brand\":\"Toyota\", \"doors\":null }";
Car car = objectMapper.readValue(carJson, Car.class);

```

抛出的异常信息如下:

```java
Exception in thread "main" com.fasterxml.jackson.databind.exc.MismatchedInputException:
    Cannot map `null` into type int
    (set DeserializationConfig.DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES to 'false' to allow)
 at [Source: (String)
    "{ "brand":"Toyota", "doors":null }"; line: 1, column: 29] (through reference chain: jackson.Car["doors"])

```

### 定制反序列化过程

有时候, 我们需要用不同于`ObjectMapper`默认的方式来反序列化JSON字符串到一个Java对象. 这时, 可以添加一个自定义序列化器(`custom deserializer`)到`ObjectMapper`, 让它可以按照你设定的方式进行反序列化.
下面是用`ObjectMapper`注册和使用自定义反序列化器的示例:

```java
String json = "{ \"brand\" : \"Ford\", \"doors\" : 6 }";
//定义一个模型,该模型使用`CarDeserializer`作为反序列化器
SimpleModule module = new SimpleModule("CarDeserializer", new Version(3, 1, 8, null, null, null));
//指定反序列化器作用的Java类(Car类)
module.addDeserializer(Car.class, new CarDeserializer(Car.class));

ObjectMapper mapper = new ObjectMapper();
//为ObjectMapper添加一个序列化/反序列化的模型
mapper.registerModule(module);
//反序列化Car类
Car car = mapper.readValue(json, Car.class);

```

下面是这里的反序列化器`CarDeserializer`的定义:

```java
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CarDeserializer extends StdDeserializer<Car> {

    public CarDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Car deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException {
        Car car = new Car();
        while(!parser.isClosed()){
            JsonToken jsonToken = parser.nextToken();

            if(JsonToken.FIELD_NAME.equals(jsonToken)){
                String fieldName = parser.getCurrentName();
                System.out.println(fieldName);

                jsonToken = parser.nextToken();

                if("brand".equals(fieldName)){
                    car.setBrand(parser.getValueAsString());
                } else if ("doors".equals(fieldName)){
                    car.setDoors(parser.getValueAsInt());
                }
            }
        }
        return car;
    }
}
```

## 序列化

### Java对象==>JSON

`ObjectMapper`实例也可以用来从一个对象生成JSON数据. 可以使用下列方法:

- writeValue()
- writeValueAsString()
- writeValueAsBytes()
  下面是一个把`Car`对象序列化为JSON的例子:

```java
ObjectMapper objectMapper = new ObjectMapper();

Car car = new Car();
car.brand = "BMW";
car.doors = 4;

objectMapper.writeValue(new FileOutputStream("data/output-2.json"), car);
```

这里首先创建了一个`ObjectMapper`实例和一个`Car`实例, 然后调用`ObjectMapper`的`writeValue()`方法把`Car`实例转换为JSON后输出到`FileOutputStream`
`writeValueAsString()` 和`writeValueAsBytes()`也可以从对象生成JSON, 并且返回一个`String`或`Byte数组`的JSON, 如下:

```java
ObjectMapper objectMapper = new ObjectMapper();

Car car = new Car();
car.brand = "BMW";
car.doors = 4;

String json = objectMapper.writeValueAsString(car);
System.out.println(json);

```

结果：

```json
{"brand":"BMW","doors":4}
```

### 自定义序列化

有时候你又不想用Jackson默认的序列化过程把一个Java对象变成JSON. 比如, 你可能在JSON中使用和Java对象不同的属性名称, 或者你想完全忽略某些属性
Jackson可以为`ObjectMapper`设置一个`自定义序列化器(custom serializer)` 这个序列化器会注册为序列化某个实际的类, 然后在`ObjectMapper`执行序列化时调用, 比如序列化`Car对象`
下例展示了如何为`Car类`注册一个`自定义序列化器`:

```java
//用Car类初始化序列化器CarSerializer 
CarSerializer carSerializer = new CarSerializer(Car.class);
ObjectMapper objectMapper = new ObjectMapper();
//新建一个序列化模型, 序列化器使用CarSerializer类
SimpleModule module = new SimpleModule("CarSerializer", new Version(2, 1, 3, null, null, null));
//注册carSerializer用于序列化Car
module.addSerializer(Car.class, carSerializer);

objectMapper.registerModule(module);

Car car = new Car();
car.setBrand("Mercedes");
car.setDoors(5);

String carJson = objectMapper.writeValueAsString(car);

```



```json
{"producer":"Mercedes","doorCount":5}
```

下面是`CarSerializer`类定义:

```java
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CarSerializer extends StdSerializer<Car> {

    protected CarSerializer(Class<Car> t) {
        super(t);
    }
	@override
    public void serialize(Car car, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("producer", car.getBrand());
        jsonGenerator.writeNumberField("doorCount", car.getDoors());
        jsonGenerator.writeEndObject();
    }
}

```

**注意 : `serialize`的第二个参数是一个`JsonGenerator`实例, 你可以使用该实例序列化一个对象, 这里是`Car`对象**

## Jackson的日期格式化

默认情况下, Jackson会把一个`java.util.Date`对象序列化为一个`long`型值, 也就是从`1970-01-1`到现在的毫秒数, 当然, Jackson也支持把日期格式化为字符串.

### Date到long

首先看看Jackson默认的把`Date`序列化为`long`的过程, 如下是一个包含`Date`类型属性的Java类:

```java
public class Transaction {
    private String type = null;
    private Date date = null;

    public Transaction() {
    }

    public Transaction(String type, Date date) {
        this.type = type;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

```

用`ObjectMapper`序列化`Transaction`类对象的过程和其他Java对象一样:

```java
Transaction transaction = new Transaction("transfer", new Date());

ObjectMapper objectMapper = new ObjectMapper();
String output = objectMapper.writeValueAsString(transaction);

System.out.println(output);

```

序列化结果:

```json
{"type":"transfer","date":1516442298301}
```

**属性`date`被序列化为了一个`long`型整数**

### Date到String

`long`型的序列化可读性很差, 因而Jackson提供了文本格式的日期序列化. 可以为`ObjectMapper`指定一个`SimpleDateFormat`实例来带格式提取Jackson日期

```java
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
objectMapper2.setDateFormat(dateFormat);

String output2 = objectMapper2.writeValueAsString(transaction);
System.out.println(output2);

```



```java
{"type":"transfer","date":"2018-01-20"}

```

可以看到, 属性`date`被格式化为一个字符串

## Jackson的树模型

Jackson内置了一个树模型(`tree model`)可以用来表示一个JSON对象. 这个树模型非常有用, 比如你不知道收到的JSON数据的结构, 或者你不想新建一个Java类来表示这个JSON数据, 或者你想在使用或者转发JSON数据前对它进行操作.
Jackson的树模型由`JsonNode`类实现. 你可以用`ObjectMapper`实例把JSON解析为`JsonNode`模型, 就像反序列化出一个自定义类对象一样.
下面举例示范`JsonNode`的用法.

### Jackson树模型示例

```java
String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";
ObjectMapper objectMapper = new ObjectMapper();
try {
    JsonNode jsonNode = objectMapper.readValue(carJson, JsonNode.class);
} catch (IOException e) {
    e.printStackTrace();
}

```

这里, 我们用`JsonNode.class`代替了`Car.class`对JSON字符串进行解析.
`ObjectMapper`提供了更简单的方法得到`JsonNode` : `readTree()`, 该方法返回的就是一个`JsonNode`, 如下:

```java
String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";
ObjectMapper objectMapper = new ObjectMapper();
try {
    JsonNode jsonNode = objectMapper.readTree(carJson);
} catch (IOException e) {
    e.printStackTrace();
}

```

### JsonNode类

`JsonNode`提供了非常灵活和动态访问的方式, 可以像访问Java对象那样导航浏览JSON
如果把JSON解析为一个`JsonNode`实例(或一个`JsonNode`实例树), 就可以浏览`JsonNode`树模型, 如下例中用`JsonNode`访问JSON中的属性,数组,对象等等:

```java
String carJson =
        "{ \"brand\" : \"Mercedes\", \"doors\" : 5," +
        "  \"owners\" : [\"John\", \"Jack\", \"Jill\"]," +
        "  \"nestedObject\" : { \"field\" : \"value\" } }";
ObjectMapper objectMapper = new ObjectMapper();
try {
    JsonNode jsonNode = objectMapper.readValue(carJson, JsonNode.class);

    JsonNode brandNode = jsonNode.get("brand");
    String brand = brandNode.asText();
    System.out.println("brand = " + brand);

    JsonNode doorsNode = jsonNode.get("doors");
    int doors = doorsNode.asInt();
    System.out.println("doors = " + doors);

    JsonNode array = jsonNode.get("owners");
    JsonNode jsonNode = array.get(0);
    String john = jsonNode.asText();
    System.out.println("john  = " + john);

    JsonNode child = jsonNode.get("nestedObject");
    JsonNode childField = child.get("field");
    String field = childField.asText();
    System.out.println("field = " + field);

} catch (IOException e) {
    e.printStackTrace();
}

```

上面的JSON中包含了一个名为`owner`的数组属性和一个名为`nestedObject`的对象属性
不管是访问一个属性, 还是数组, 还是内嵌的对象, 都可以用`JsonNode`的`get()`方法. 为`get()`传入一个字符串就可以访问一个`JsonNode`实例的一个属性, 传入一个索引就是访问`JsonNode`实例代表的数组, 索引代表了你要访问的数组元素位置.

# Jackson注解

[Jackson Annotation Examples | Baeldung](https://www.baeldung.com/jackson-annotations)

[More Jackson Annotations | Baeldung](https://www.baeldung.com/jackson-advanced-annotations)

[Jackson之注解大全_blwinner的专栏-CSDN博客_jackson 注解](https://blog.csdn.net/blwinner/article/details/98532847)

## 序列化注解

### @JsonAnyGetter

**作用**：把可变的`Map`类型属性当做标准属性。

**说人话**：`Map`的一对对的Key/Value，变成JSON中其他属性同级的，**不是套娃**

下例中，`ExtendableBean`实体有一个`name`属性和一组`kay/value`格式的可扩展属性:

```java
class ExtendableBean {
    public String name;
    public Map<String, String> properties;
    @JsonAnyGetter
    public Map<String, String> getProperties() {
        return properties;
    }
    public ExtendableBean(String name) {
        this.name = name;
        properties = new HashMap<>();
    }
    public void add(String key, String value){
        properties.put(key, value);
    }
}

```

**说明:** `name`属性访问级别是`public`, 是为了省略`get/set`方法, 简化示例

下面是把`ExtendableBean`实体序列化的过程:

```java
private static void whenSerializingUsingJsonAnyGetter_thenCorrect(){
        ExtendableBean bean = new ExtendableBean("My bean");
        bean.add("attr1", "val1");
        bean.add("attr2", "val2");
        String result = null;
        try {
            result = new ObjectMapper().writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }

```

序列化后的结果: `{"name":"My bean","attr2":"val2","attr1":"val1"}`

### @JsonGetter

该注解是`@JsonProperty`的两个作用中的一个, 用来标记一个方法是`getter`方法
下例中, 指定方法`getTheName()`是属性`name`属性的`getter`方法

**说人话**：get方法命名不规范时就这么干

`````java
public class MyBean {
    public int id;
    private String name;
 
    @JsonGetter("name")
    public String getTheName() {
        return name;
    }
}

`````

下面是序列化过程:

```java
public void whenSerializingUsingJsonGetter_thenCorrect()
  throws JsonProcessingException {
    MyBean bean = new MyBean(1, "My bean");
    String result = new ObjectMapper().writeValueAsString(bean);
}

```

### @JsonPropertyOrder

可以指定实体属性序列化后的顺序

说人话：没啥用，强迫症必备

```java
@JsonPropertyOrder({ "name", "id" })
public class MyBean {
    public int id;
    public String name;
}

```

- 序列化后的结果:`{ "name":"My bean", "id":1}`
- 该注解有一个参数`alphabetic`, 如果为`true`, 表示按字母顺序序列化,此时输出结果:`{ "id":1, "name":"My bean"}`

### @JsonRawValue

该注解可以让Jackson在序列化时把属性的值原样输出
下面的例子中, 我们给实体属性`attrs`赋值一个json字符串

说人话：JSON套娃时，实体类可以不用套娃了

```java
public class RawBean {
    public String name;
    @JsonRawValue
    public String attrs;
}
public void whenSerializingUsingJsonRawValue_thenCorrect()
  throws JsonProcessingException {  
    RawBean bean = new RawBean("My bean", "{\"attr\":false}");
    String result = new ObjectMapper().writeValueAsString(bean);
}
```

输出结果是: `{"name":"Mybean","attrs":{"attr":false}}`

###  @JsonValue

该注解作用于一个方法, 并且只用被注解的方法序列化整个实体对象

说人话：标在属性或者方法上面，只**序列化这个属性**

```java
class ExtendableBean {
    public String name;
   ...........
   //把注解换成JsonValue
    @JsonValue
    public Map<String, String> getProperties() {
        return properties;
    }
	..........
}
```

序列化过程不变, 则结果是: `{"attr2":"val2","attr1":"val1"}`
**可见, 属性`name`没有被序列化**

### @JsonRootName

作用：再包一层，名称由value指定

如果`wrapping`是使能(`enabled`), 那么该注解用来指定`root wrapper`的名称
`wrapping`(包装)的含义是如果序列化实体`User`的结果是

```java
{
    "id": 1,
    "name": "John"
}

```

那么`wrapping`后的效果如下:

```java
{
    "User": {
        "id": 1,
        "name": "John"
    }
}

```

下面看一个例子, 我们用该注解指明包装实体(`wrapper entity`)的包装器名称:

```java
@JsonRootName(value = "user")
public class UserWithRoot {
    public int id;
    public String name;
}
```

外面包一层，默认名称是实体类名，但是注解的`value`属性把包装器名称改为了`user`

```java
private static void whenSerializingUsingJsonRootName_thenCorrect(){
    UserWithRoot user = new UserWithRoot();
    user.id = 1;
    user.name = "jackma";
    try {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        String result = objectMapper.writeValueAsString(user);
        System.out.println(result);
    } catch (JsonProcessingException e) {
        e.printStackTrace();
    }
}

```

序列化的结果:`{"user":{"id":1,"name":"jackma"}}`

从`Jackson .2.4`版本开始, 新增了一个可选参数`namespace`, 该属性对json没效果, 但是对xml起作用, 修改本例的实体例:

```java
@JsonRootName(value = "user", namespace = "alibaba")
class UserWithRoot {
    public int id;
    public String name;
}

```

用`XmlMapper`序列化:

```java
private static void whenSerializingUsingJsonRootName_thenCorrect(){
		..............
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
		..............

```

序列化结果:

```xml
<user xmlns="alibaba">
	<id xmlns="">1</id>
	<name xmlns="">jackma</name>
</user>

```





## 其他注解

### @JsonTypeId

该注解作用于属性, 使得该属性不再是普通属性, 其值代表bean类的类型ID(`TypeId), 可以用它来描述多态时实体类对象的实际类型

```java
public class TypeIdBean {
    private int id;
    @JsonTypeId
    private String name;
 
    // constructor, getters and setters
}

```

序列化过程:

```java
mapper.enableDefaultTyping(DefaultTyping.NON_FINAL);
TypeIdBean bean = new TypeIdBean(6, "Type Id Bean");
String jsonString = mapper.writeValueAsString(bean);

```

["Type Id Bean",{"id":6}]

- `mapper.enableDefaultTyping(DefaultTyping.NON_FINAL)`的作用是在序列化结果中显示实体类类型属性
- 结果是一个Json对象, 其中`"Type Id Bean"`是实体类ID的描述, `{"id":6}`是类的属性值