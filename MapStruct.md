# 学习资源

推荐教程 https://www.baeldung.com/mapstruct

官网 https://mapstruct.org/

使用案例：https://github.com.cnpmjs.org/mapstruct/mapstruct-examples.git

# 基本使用

## MapStruct是什么

用来处理实体类转Dto，MapStruct是个**注解处理器**，它在**编译期**生成实体类转Dto的具体逻辑

## 加入依赖

1.4.2.Final是当前最新版本

```xml
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.4.2.Final</version> 
</dependency>
```

maven插件

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.5.1</version>
    <configuration>
        <source>1.8</source>
        <target>1.8</target>
        <annotationProcessorPaths>
            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>1.4.2.Final</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

## 映射：字段名和属性都一致

**源实体类和目标类**

```java
public class SimpleSource {
    private String name;
    private String description;
    // getters and setters
}
 
public class SimpleDestination {
    private String name;
    private String description;
    // getters and setters
}
```

**映射接口**

```java
@Mapper
public interface SimpleSourceDestinationMapper {
    SimpleDestination sourceToDestination(SimpleSource source);
    SimpleSource destinationToSource(SimpleDestination destination);
}
```

可以看出这个接口里的映射方法，从源到目标，从目标到源都有

**执行mvn clean install**,在/target/generated-sources/annotations/下生成实现类

```java
public class SimpleSourceDestinationMapperImpl
  implements SimpleSourceDestinationMapper {
    @Override
    public SimpleDestination sourceToDestination(SimpleSource source) {
        if ( source == null ) {
            return null;
        }
        SimpleDestination simpleDestination = new SimpleDestination();
        simpleDestination.setName( source.getName() );
        simpleDestination.setDescription( source.getDescription() );
        return simpleDestination;
    }
    @Override
    public SimpleSource destinationToSource(SimpleDestination destination){
        if ( destination == null ) {
            return null;
        }
        SimpleSource simpleSource = new SimpleSource();
        simpleSource.setName( destination.getName() );
        simpleSource.setDescription( destination.getDescription() );
        return simpleSource;
    }
}
```

测试

```java
public class SimpleSourceDestinationMapperIntegrationTest {
    private SimpleSourceDestinationMapper mapper
      = Mappers.getMapper(SimpleSourceDestinationMapper.class);//注意这个，获取mapper
    @Test
    public void givenSourceToDestination_whenMaps_thenCorrect() {
        SimpleSource simpleSource = new SimpleSource();
        simpleSource.setName("SourceName");
        simpleSource.setDescription("SourceDescription");
        SimpleDestination destination = mapper.sourceToDestination(simpleSource);
 
        assertEquals(simpleSource.getName(), destination.getName());
        assertEquals(simpleSource.getDescription(), 
          destination.getDescription());
    }
    @Test
    public void givenDestinationToSource_whenMaps_thenCorrect() {
        SimpleDestination destination = new SimpleDestination();
        destination.setName("DestinationName");
        destination.setDescription("DestinationDescription");
        SimpleSource source = mapper.destinationToSource(destination);
        assertEquals(destination.getName(), source.getName());
        assertEquals(destination.getDescription(),
          source.getDescription());
    }
}
```

## MapStruct注入Spring容器⭐

最好是注入容器

```java
@Mapper(componentModel = "spring")
public interface SimpleSourceDestinationMapper
```

当需要在映射接口中@Autowired其他 Spring 组件时，**必须使用抽象类而不是接口**：

```java
@Mapper(componentModel = "spring")
public abstract class SimpleDestinationMapperUsingInjectedService {

    @Autowired
    protected SimpleService simpleService;//注意不能设为私有，因为MapStruct编译生成的子类要使用它

    @Mapping(target = "name", expression = "java(simpleService.enrichName(source.getName()))")
    public abstract SimpleDestination sourceToDestination(SimpleSource source);
}
```

componentModel = "spring"的作用就相当于我们之前写的@Component注解,目的是将这个映射接口加入Spring容器，方便再其他地方通过依赖注入的形式使用，比如：

```java
@RestCointroller
public HelloController{
    @Autowired
    SimpleSourceDestinationMapper simpleSourceDestinationMapper;
    //......
}
```



## 映射：字段名和属性不一致时

这里主要是@Mappings注解

```java
public class EmployeeDTO {
    private int employeeId;
    private String employeeName;
    // getters and setters
}
public class Employee {
    private int id;
    private String name;
    // getters and setters
}
```

映射接口,字段名和属性不一致时,通过@Mapping来明确指定

```java
@Mapper
public interface EmployeeMapper {
    @Mappings({
      @Mapping(target="employeeId", source="entity.id"),
      @Mapping(target="employeeName", source="entity.name")
    })
    EmployeeDTO employeeToEmployeeDTO(Employee entity);
    @Mappings({
      @Mapping(target="id", source="dto.employeeId"),
      @Mapping(target="name", source="dto.employeeName")
    })
    Employee employeeDTOtoEmployee(EmployeeDTO dto);
}
```

测试一下

```java
@Test
public void givenEmployeeDTOwithDiffNametoEmployee_whenMaps_thenCorrect() {
    EmployeeDTO dto = new EmployeeDTO();
    dto.setEmployeeId(1);
    dto.setEmployeeName("John");

    Employee entity = mapper.employeeDTOtoEmployee(dto);

    assertEquals(dto.getEmployeeId(), entity.getId());
    assertEquals(dto.getEmployeeName(), entity.getName());
}
```

## 映射：嵌套

EmployeeDTO中一个属性依赖DivisionDTO的情况

```java
public class EmployeeDTO {
    private int employeeId;
    private String employeeName;
    private DivisionDTO division;
    // getters and setters omitted
}
public class Employee {
    private int id;
    private String name;
    private Division division;
    // getters and setters omitted
}
public class Division {
    private int id;
    private String name;
    // default constructor, getters and setters omitted
}
```

再次添加两个方法到 EmployeeMapper 中

```java
DivisionDTO divisionToDivisionDTO(Division entity);

Division divisionDTOtoDivision(DivisionDTO dto);
```

测试使用：

```java
@Test
public void givenEmpDTONestedMappingToEmp_whenMaps_thenCorrect() {
    EmployeeDTO dto = new EmployeeDTO();
    dto.setDivision(new DivisionDTO(1, "Division1"));
    Employee entity = mapper.employeeDTOtoEmployee(dto);
    assertEquals(dto.getDivision().getId(), 
                 entity.getDivision().getId());
    assertEquals(dto.getDivision().getName(), 
                 entity.getDivision().getName());
}
```

## 映射：Date和String

```java
public class Employee {
    // other fields
    private Date startDt;
    // getters and setters
}
public class EmployeeDTO {
    // other fields
    private String employeeStartDt;
    // getters and setters
}
```

接口：

```java
@Mappings({
  @Mapping(target="employeeId", source = "entity.id"),
  @Mapping(target="employeeName", source = "entity.name"),
  @Mapping(target="employeeStartDt", source = "entity.startDt",
           dateFormat = "dd-MM-yyyy HH:mm:ss")})
EmployeeDTO employeeToEmployeeDTO(Employee entity);
@Mappings({
  @Mapping(target="id", source="dto.employeeId"),
  @Mapping(target="name", source="dto.employeeName"),
  @Mapping(target="startDt", source="dto.employeeStartDt",
           dateFormat="dd-MM-yyyy HH:mm:ss")})
Employee employeeDTOtoEmployee(EmployeeDTO dto);
```

测试：

```java
private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
@Test
public void givenEmpStartDtMappingToEmpDTO_whenMaps_thenCorrect() throws ParseException {
    Employee entity = new Employee();
    entity.setStartDt(new Date());
    EmployeeDTO dto = mapper.employeeToEmployeeDTO(entity);
    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
 
    assertEquals(format.parse(dto.getEmployeeStartDt()).toString(),
      entity.getStartDt().toString());
}
@Test
public void givenEmpDTOStartDtMappingToEmp_whenMaps_thenCorrect() throws ParseException {
    EmployeeDTO dto = new EmployeeDTO();
    dto.setEmployeeStartDt("01-04-2016 01:00:00");
    Employee entity = mapper.employeeDTOtoEmployee(dto);
    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
 
    assertEquals(format.parse(dto.getEmployeeStartDt()).toString(),
      entity.getStartDt().toString());
}
```

## 自定义方法映射：抽象类代替接口

有时候，我们写的Mapper接口，由于一般的接口方法没有方法体（static和default除外），遇到复杂的dto转entity场景还是有点力不从心，这时就可以时**抽象类**来代替这个接口，因为抽象类的方法可以写方法体，这里面可以写一些复杂的逻辑。

实体类：

```java
public class Transaction {
    private Long id;
    private String uuid = UUID.randomUUID().toString();
    private BigDecimal total;

    //standard getters
}
```

和匹配的 DTO：

```java
public class TransactionDTO {

    private String uuid;
    private Long totalInCents;

    // standard getters and setters
}
```

这里棘手的部分是将BigDecimal转换为Long totalInCents。

@Mappe标注的抽象类：

```java
@Mapper
abstract class TransactionMapper {

    //主要实现
    public TransactionDTO toTransactionDTO(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setUuid(transaction.getUuid());
        transactionDTO.setTotalInCents(transaction.getTotal()
          .multiply(new BigDecimal("100")).longValue());
        return transactionDTO;
    }
    
    //将Collection映射到List
    public abstract List<TransactionDTO> toTransactionDTO(Collection<Transaction> transactions);
}
```

和接口一样，在**编译时，自动生成**了这个抽象类的子类：

```java
@Generated
class TransactionMapperImpl extends TransactionMapper {
    //非abstract方法，就使用父类的
    //自动生成abstract方法的方法体
    @Override
    public List<TransactionDTO> toTransactionDTO(Collection<Transaction> transactions) {
        if ( transactions == null ) {
            return null;
        }

        List<TransactionDTO> list = new ArrayList<>();
        for ( Transaction transaction : transactions ) {
            list.add( toTransactionDTO( transaction ) );
        }

        return list;
    }
}
```

## 映射前后的额外逻辑：@BeforeMapping、@AfterMapping

**用于标记在映射逻辑之前和之后调用的方法**

将此**行为应用于所有映射的超类型的**场景中

父类：

```java
public class Car {
    private int id;
    private String name;
}
```

两个子类：

```java
public class BioDieselCar extends Car {
}
public class ElectricCar extends Car {
}
```

Dto和枚举:

```java
public class CarDTO {
    private int id;
    private String name;
    private FuelType fuelType;
}
public enum FuelType {
    ELECTRIC, BIO_DIESEL
}
```

Mapper:

```java
@Mapper
public abstract class CarsMapper {
    @BeforeMapping
    protected void enrichDTOWithFuelType(Car car, @MappingTarget CarDTO carDto) {
        if (car instanceof ElectricCar) {
            carDto.setFuelType(FuelType.ELECTRIC);
        }
        if (car instanceof BioDieselCar) { 
            carDto.setFuelType(FuelType.BIO_DIESEL);
        }
    }

    @AfterMapping
    protected void convertNameToUpperCase(@MappingTarget CarDTO carDto) {
        carDto.setName(carDto.getName().toUpperCase());
    }

    public abstract CarDTO toCarDto(Car car);
}
```

**@MappingTarget**标在方法参数前，在**@BeforeMapping 的**情况下，在映射逻辑执行之前和在@AfterMapping注释方法的情况下，在执行映射逻辑之前填充目标映射DTO。

编译后生成的mapper子类:

```java
@Generated
public class CarsMapperImpl extends CarsMapper {
    @Override
    public CarDTO toCarDto(Car car) {
        if (car == null) {
            return null;
        }
        CarDTO carDTO = new CarDTO();
        enrichDTOWithFuelType(car, carDTO);
        carDTO.setId(car.getId());
        carDTO.setName(car.getName());
        convertNameToUpperCase(carDTO);
        return carDTO;
    }
}
```

## 支持 Lombok⭐

因为Lombok也是在编译时生成Java代码的，所以这里需要配置一下

maven插件中添加如下：

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.5.1</version>
    <configuration>
        <source>1.8</source>
        <target>1.8</target>
        <annotationProcessorPaths>
            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>1.4.2.Final</version>
            </path>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>1.18.4</version>
            </path>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok-mapstruct-binding</artifactId>
                <version>0.2.0</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

实体类：

```java
@Getter
@Setter
public class Car {
    private int id;
    private String name;
}
```

Dto:

```java
@Getter
@Setter
public class CarDTO {
    private int id;
    private String name;
}
```

mapper接口：

```java
@Mapper
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);
    CarDTO carToCarDTO(Car car);
}
```

## 空字段的映射处理

@Mapping注解的defaultExpression属性来指定一个表达式，如果源字段为null ，则该表达式确定目标字段的值

实体类：

```java
public class Person {
    private int id;
    private String name;
}
```

Dto:

```java
public class PersonDTO {
    private int id;
    private String name;
}
```

如果源实体的id字段为null，我们希望生成一个随机id并将其分配给目标，并保持其他属性值不变：

```java
@Mapper
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);
    
    @Mapping(target = "id", source = "person.id", 
      defaultExpression = "java(java.util.UUID.randomUUID().toString())")
    PersonDTO personToPersonDTO(Person person);
}
```

测试：

```java
@Test
public void givenPersonEntitytoPersonWithExpression_whenMaps_thenCorrect() 
    Person entity  = new Person();
    entity.setName("Micheal");
    PersonDTO personDto = PersonMapper.INSTANCE.personToPersonDTO(entity);
    assertNull(entity.getId());
    assertNotNull(personDto.getId());
    assertEquals(personDto.getName(), entity.getName());
}
```

# 进阶使用

## 自定义方法映射：qualifiedByName属性

来看一个计算体重指数（BMI）的实体类

Dto:

```java
public class UserBodyImperialValuesDTO {
    private int inch;
    private int pound;
    // constructor, getters, and setters
}
```

实体类：

```java
public class UserBodyValues {
    private double kilogram;
    private double centimeter;
    // constructor, getters, and setters
}
```

mapper接口：**qualifiedByName属性指向@Named注解的值来自定义额外的逻辑**

```java
@Mapper
public interface UserBodyValuesMapper {
    UserBodyValuesMapper INSTANCE = Mappers.getMapper(UserBodyValuesMapper.class);
    
    //Dto转entity
    @Mapping(source = "inch", target = "centimeter", qualifiedByName = "inchToCentimeter")
    public UserBodyValues userBodyValuesMapper(UserBodyImperialValuesDTO dto);
    
    @Named("inchToCentimeter") 
    public static double inchToCentimeter(int inch) { 
        return inch * 2.54; 
    }
}
```

测试：

```java
UserBodyImperialValuesDTO dto = new UserBodyImperialValuesDTO();
dto.setInch(10);

UserBodyValues obj = UserBodyValuesMapper.INSTANCE.userBodyValuesMapper(dto);

assertNotNull(obj);
assertEquals(25.4, obj.getCentimeter(), 0);
```

## 自定义注解来实现映射方法

自定义注解：@PoundToKilogramMapper

```java
@Qualifier
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface PoundToKilogramMapper {
}
```

将@PoundToKilogramMapper注解添加到上述案例的poundToKilogram方法中：

```java
@PoundToKilogramMapper
public static double poundToKilogram(int pound) {
    return pound * 0.4535;
}
```

然后用qualifiedBy 指向这个注解的方法：

```java
@Mapper
public interface UserBodyValuesMapper {
    UserBodyValuesMapper INSTANCE = Mappers.getMapper(UserBodyValuesMapper.class);

    @Mapping(source = "pound", target = "kilogram", qualifiedBy = PoundToKilogramMapper.class)
    public UserBodyValues userBodyValuesMapper(UserBodyImperialValuesDTO dto);

    @PoundToKilogramMapper
    public static double poundToKilogram(int pound) {
        return pound * 0.4535;
    }
}
```

测试：

```java
UserBodyImperialValuesDTO dto = new UserBodyImperialValuesDTO();
dto.setPound(100);

UserBodyValues obj = UserBodyValuesMapper.INSTANCE.userBodyValuesMapper(dto);

assertNotNull(obj);
assertEquals(45.35, obj.getKilogram(), 0);
```

## 忽略属性

由于 MapStruct 在编译时运行，因此它可以比基于反射或动态代理的框架更快。**如果映射不完整**，它还**生成错误报告**——也就是说，如果不是所有的目标属性都被映射：

```
Warning:(X,X) java: Unmapped target property: "propertyName".
```

例子：

```java
public class DocumentDTO {
    private int id;
    private String title;
    private String text;
    private List<String> comments;//Dto独有
    private String author;//Dto独有
}
public class Document {
    private int id;
    private String title;
    private String text;
    private Date modificationTime;//实体类独有
}
```



```java
@Mapper
public interface DocumentMapper {
    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    DocumentDTO documentToDocumentDTO(Document entity);
    Document documentDTOToDocument(DocumentDTO dto);
}
```

由于实体类和Dto属性不完全对的上，在编译时，会爆警告，解决：

### 忽略指定字段：ignore属性

```java
@Mapper
public interface DocumentMapperMappingIgnore {

    DocumentMapperMappingIgnore INSTANCE =
      Mappers.getMapper(DocumentMapperMappingIgnore.class);

    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "author", ignore = true)
    DocumentDTO documentToDocumentDTO(Document entity);

    @Mapping(target = "modificationTime", ignore = true)
    Document documentDTOToDocument(DocumentDTO dto);
}
```

对于字段很多时，这种写法就不方便了，需要定义忽略策略

### 忽略策略⭐

ReportingPolicy有三种策略：

- ERROR： 字段只要有对不上的，直接编译失败
- WARN（默认）：编译时爆警告
- IGNORE：字段对不上的就忽略

**写法一、为单个的映射接口添加忽略策略：**

```java
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentMapperUnmappedPolicy {
    // mapper methods
}
```

**写法二、为所有接口添加忽略策略：**

先写个配置

```java
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IgnoreUnmappedMapperConfig {
}
```

再在映射接口上引入配置：

```java
@Mapper(config = IgnoreUnmappedMapperConfig.class)
public interface DocumentMapperWithConfig { 
    // mapper methods 
}
```

**写法三、在注解处理器配置里添加忽略策略**（推荐）

这样可以针对整个项目代码中所有的mapper接口添加忽略策略

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>${maven-compiler-plugin.version}</version>
            <configuration>
                <source>${maven.compiler.source}</source>
                <target>${maven.compiler.target}</target>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${org.mapstruct.version}</version>
                    </path>
                </annotationProcessorPaths>
                <compilerArgs>
                    <compilerArg>
                        -Amapstruct.unmappedTargetPolicy=IGNORE
                    </compilerArg>
                </compilerArgs>
            </configuration>
        </plugin>
    </plugins>
</build>
```

**加载顺序**

以上几种写法有加载顺序之分，从高到底如下：

- 忽略映射器方法级别的特定字段
- 映射器上的策略
- 共享 MapperConfig
- 全局配置

## 多个实体类合并到一个目标类

源实体类1：

```java
class Customer {
    private String firstName;
    private String lastName;
    // getters and setters

}
```

源实体类2：

```java
class Address {
    private String street;
    private String postalcode;
    private String county;
    // getters and setters

}
```

目标类Dto:

```java
class DeliveryAddress {
    private String forename;
    private String surname;
    
    private String street;
    private String postalcode;
    private String county;
    // getters and setters
}
```

可以看出Dto的属性来源于两个实体类，映射到Dto的写法如下：

映射接口

```java
@Mapper
interface DeliveryAddressMapper {

    @Mapping(source = "customer.firstName", target = "forename")
    @Mapping(source = "customer.lastName", target = "surname")
    @Mapping(source = "address.street", target = "street")
    @Mapping(source = "address.postalcode", target = "postalcode")
    @Mapping(source = "address.county", target = "county")
    DeliveryAddress from(Customer customer, Address address);

}
```

测试一下

```java
// given a customer
Customer customer = new Customer().setFirstName("Max")
    .setLastName("Powers");

// and some address
Address homeAddress = new Address().setStreet("123 Some Street")
    .setCounty("Nevada")
    .setPostalcode("89123");

// when calling DeliveryAddressMapper::from
DeliveryAddress deliveryAddress = deliveryAddressMapper.from(customer, homeAddress);

// then a new DeliveryAddress is created, based on the given customer and his home address
assertEquals(deliveryAddress.getForename(), customer.getFirstName());
assertEquals(deliveryAddress.getSurname(), customer.getLastName());
assertEquals(deliveryAddress.getStreet(), homeAddress.getStreet());
assertEquals(deliveryAddress.getCounty(), homeAddress.getCounty());
assertEquals(deliveryAddress.getPostalcode(), homeAddress.getPostalcode());
```

不限于两个源实体类。**任意多的都可以**

## 更新已存在的目标对象属性

有时候，我们的Dto对象已经创建好了，只想更新一下里面的字段，这就需要@MappingTarget注解

```java
@Mapper
interface DeliveryAddressMapper {
    //源实体类：Address    Dto: DeliveryAddress
    @Mapping(source = "address.postalcode", target = "postalcode")
    @Mapping(source = "address.county", target = "county")
    DeliveryAddress updateAddress(@MappingTarget DeliveryAddress deliveryAddress, Address address);

}
```

测试一下

```java
// given a delivery address
DeliveryAddress deliveryAddress = new DeliveryAddress().setForename("Max")
  .setSurname("Powers")
  .setStreet("123 Some Street")
  .setCounty("Nevada")
  .setPostalcode("89123");

// and some new address
Address newAddress = new Address().setStreet("456 Some other street")
  .setCounty("Arizona")
  .setPostalcode("12345");

// when calling DeliveryAddressMapper::updateAddress
DeliveryAddress updatedDeliveryAddress = deliveryAddressMapper.updateAddress(deliveryAddress, newAddress);

// then the *existing* delivery address is updated
assertSame(deliveryAddress, updatedDeliveryAddress);

assertEquals(deliveryAddress.getStreet(), newAddress.getStreet());
assertEquals(deliveryAddress.getCounty(), newAddress.getCounty());
assertEquals(deliveryAddress.getPostalcode(), newAddress.getPostalcode());
```

## 集合映射

有时候，需要从List<实体类>映射到List\<Dto> ，或者实体类中有个成员属性是List<实体类>，需要映射到的Dto里也有List\<Dto>，在之前的处理中，我们需要遍历List集合，或者用stream api（本质上也是遍历集合），写起来麻烦，执行效率不高。

### List映射⭐

#### List<实体类> 《===》List\<Dto>

源实体类

```java
public class Employee {
    private String firstName;
    private String lastName;

    // constructor, getters and setters
}
```

目标 DTO：

```java
public class EmployeeDTO {

    private String firstName;
    private String lastName;

    // getters and setters
}
```

接下来，写个映射接口：

```java
@Mapper
public interface EmployeeMapper {
    List<EmployeeDTO> map(List<Employee> employees);
}
```

看看**编译时自动生成的**实现类

```java
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public List<EmployeeDTO> map(List<Employee> employees) {
        if (employees == null) {
            return null;
        }

        List<EmployeeDTO> list = new ArrayList<EmployeeDTO>(employees.size());
        for (Employee employee : employees) {
            list.add(employeeToEmployeeDTO(employee));
        }

        return list;
    }

    protected EmployeeDTO employeeToEmployeeDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());

        return employeeDTO;
    }
}
```

#### 属性不一致时

当Dto是这样的呢：

```java
public class EmployeeFullNameDTO {
    private String fullName; //fullName=firstName+lastName
    // getter and setter
}
```

需要这样写映射接口：

```java
@Mapper
public interface EmployeeFullNameMapper {

    List<EmployeeFullNameDTO> map(List<Employee> employees);

    default EmployeeFullNameDTO map(Employee employee) {
        EmployeeFullNameDTO employeeInfoDTO = new EmployeeFullNameDTO();
        employeeInfoDTO.setFullName(employee.getFirstName() + " " + employee.getLastName());

        return employeeInfoDTO;
    }
}
```

### Set映射

实体类和Dto还是上一节的例子，映射接口这样写：

```java
@Mapper
public interface EmployeeMapper {

    Set<EmployeeDTO> map(Set<Employee> employees);
}
```

MapStruct 将自动生成的代码：

```java
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public Set<EmployeeDTO> map(Set<Employee> employees) {
        if (employees == null) {
            return null;
        }

        Set<EmployeeDTO> set = 
          new HashSet<EmployeeDTO>(Math.max((int)(employees.size() / .75f ) + 1, 16));
        for (Employee employee : employees) {
            set.add(employeeToEmployeeDTO(employee));
        }

        return set;
    }

    protected EmployeeDTO employeeToEmployeeDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());

        return employeeDTO;
    }
}
```

### Map映射

```java
@Mapper
public interface EmployeeMapper {

    Map<String, EmployeeDTO> map(Map<String, Employee> idEmployeeMap);
}
```

MapStruct 自动生成的代码

```java
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public Map<String, EmployeeDTO> map(Map<String, Employee> idEmployeeMap) {
        if (idEmployeeMap == null) {
            return null;
        }

        Map<String, EmployeeDTO> map = new HashMap<String, EmployeeDTO>(Math.max((int)(idEmployeeMap.size() / .75f) + 1, 16));

        for (java.util.Map.Entry<String, Employee> entry : idEmployeeMap.entrySet()) {
            String key = entry.getKey();
            EmployeeDTO value = employeeToEmployeeDTO(entry.getValue());
            map.put(key, value);
        }

        return map;
    }

    protected EmployeeDTO employeeToEmployeeDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());

        return employeeDTO;
    }
}
```

## 成员属性List映射⭐

@Mapper注解有一个collectionMappingStrategy属性，可以有这些值：ACCESSOR_ONLY（默认）、SETTER_PREFERRED、ADDER_PREFERRED或TARGET_IMMUTABLE。

### ACCESSOR_ONLY

默认的，只要Dto有get、set方法就行

源实体类：

```java
public class Company {
    private List<Employee> employees;
   // getter and setter
}
```

DTO：

```java
public class CompanyDTO {

    private List<EmployeeDTO> employees;

    public List<EmployeeDTO> getEmployees() {
        return employees;
    }

    //set方法，ACCESSOR_ONLY用到这个
    public void setEmployees(List<EmployeeDTO> employees) {
        this.employees = employees;
    }

    //add方法，ADDER_PREFERRED用到
    public void addEmployee(EmployeeDTO employeeDTO) {
        if (employees == null) {
            employees = new ArrayList<>();
        }

        employees.add(employeeDTO);
    }
}
```

Mapper:

```java
@Mapper
public interface EmployeeMapper {

    Map<String, EmployeeDTO> map(Map<String, Employee> idEmployeeMap);
}
```



```java
@Mapper(uses = EmployeeMapper.class)
public interface CompanyMapper {
    CompanyDTO map(Company company);
}
```

生成的代码：

```java
public class CompanyMapperImpl implements CompanyMapper {

    private final EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);

    @Override
    public CompanyDTO map(Company company) {
        if (company == null) {
            return null;
        }

        CompanyDTO companyDTO = new CompanyDTO();

        companyDTO.setEmployees(employeeMapper.map(company.getEmployees()));

        return companyDTO;
    }
}
```

### ADDER_PREFERRED

```java
@Mapper
public interface EmployeeMapper {
    EmployeeDTO map(Employee employee);
    List map(List employees);
    Set map(Set employees);
    Map<String, EmployeeDTO> map(Map<String, Employee> idEmployeeMap);
}
```

这里Mapper嵌套了

```java
@Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        uses = EmployeeMapper.class)
public interface CompanyMapperAdderPreferred {
    CompanyDTO map(Company company);
}
```

生成的代码：

```java
public class CompanyMapperAdderPreferredImpl implements CompanyMapperAdderPreferred {

    private final EmployeeMapper employeeMapper = Mappers.getMapper( EmployeeMapper.class );

    @Override
    public CompanyDTO map(Company company) {
        if ( company == null ) {
            return null;
        }

        CompanyDTO companyDTO = new CompanyDTO();

        if ( company.getEmployees() != null ) {
            for ( Employee employee : company.getEmployees() ) {
                companyDTO.addEmployee( employeeMapper.map( employee ) );
            }
        }

        return companyDTO;
    }
}
```

。。。。

## 对象属性深拷贝

