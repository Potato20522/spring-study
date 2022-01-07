## 概述

[地图结构弹簧扩展 0.1.0 参考指南 (mapstruct.org)](https://mapstruct.org/documentation/spring-extensions/reference/html/)

**依赖**

```xml
...
<properties>
    <org.mapstruct.extensions.spring.version>0.1.0</org.mapstruct.extensions.spring.version>
</properties>
...
<dependencies>
    <dependency>
        <groupId>org.mapstruct.extensions.spring</groupId>
        <artifactId>mapstruct-spring-annotations</artifactId>
        <version>${org.mapstruct.extensions.spring.version}</version>
    </dependency>
</dependencies>
...
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.mapstruct.extensions.spring</groupId>
                        <artifactId>mapstruct-spring-extensions</artifactId>
                        <version>${org.mapstruct.extensions.spring.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
...
```

## 使用

### Mappers as Converters

Converter是SpringBoot的转换器

```java
@Mapper
public interface CarMapper extends Converter<Car, CarDto> {
    @Mapping(target = "seats", source = "seatConfiguration")
    CarDto convert(Car car);
}
```

再依赖注入：

```java
...
    @Autowired
    private ConversionService conversionService;
...
    Car car = ...;
    CarDto carDto = conversionService.convert(car, CarDto.class);

```

All this can be achieved already with MapStruct’s core functionality. However, when a Mapper wants to [invoke](https://mapstruct.org/documentation/stable/reference/html/#invoking-other-mappers) another one, it can’t take the route via the , because the latter’s method does not match the signature that MapStruct expects for a mapping method. Thus, the developer still has to add every invoked Mapper to the invoking Mapper’s element. This creates (aside from a potentially long list) a tight coupling between Mappers that the wants to avoid.`ConversionService``convert``uses``ConversionService`

This is where MapStruct Spring Extensions can help. Including the two artifacts in your build will generate an Adapter class that *can* be used by an invoking Mapper. Let’s say that the above CarMapper is accompanied by a SeatConfigurationMapper:

```java
@Mapper
public interface SeatConfigurationMapper extends Converter<SeatConfiguration, SeatConfigurationDto> {
    @Mapping(target = "seatCount", source = "numberOfSeats")
    @Mapping(target = "material", source = "seatMaterial")
    SeatConfigurationDto convert(SeatConfiguration seatConfiguration);
}
```

The generated Adapter class will look like this:

```java
@Component
public class ConversionServiceAdapter {
  private final ConversionService conversionService;

  public ConversionServiceAdapter(@Lazy final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  public CarDto mapCarToCarDto(final Car source) {
    return conversionService.convert(source, CarDto.class);
  }

  public SeatConfigurationDto mapSeatConfigurationToSeatConfigurationDto(
      final SeatConfiguration source) {
    return conversionService.convert(source, SeatConfigurationDto.class);
  }
}
```

Since this class' methods match the signature that MapStruct expects, we can now add it to the CarMapper:

```java
@Mapper(uses = ConversionServiceAdapter.class)
public interface CarMapper extends Converter<Car, CarDto> {
    @Mapping(target = "seats", source = "seatConfiguration")
    CarDto convert(Car car);
}
```

### Custom Names

By default, the generated class will be located in the package and receive the name . Typically, you will want to change these names, most often at least the package. This can be accomplished by adding the annotation on any class within your regular source code. One natural candidate would be your [shared configuration](https://mapstruct.org/documentation/stable/reference/html/#shared-configurations) if you use this:`org.mapstruct.extensions.spring.converter``ConversionServiceAdapter``SpringMapperConfig`

```java
import org.mapstruct.MapperConfig;
import org.mapstruct.extensions.spring.SpringMapperConfig;
import org.mapstruct.extensions.spring.example.adapter.MyAdapter;

@MapperConfig(componentModel = "spring", uses = MyAdapter.class)
@SpringMapperConfig(conversionServiceAdapterPackage ="org.mapstruct.extensions.spring.example.adapter", conversionServiceAdapterClassName ="MyAdapter")
public interface MapperSpringConfig {
}
```

Note: If you do *not* specify the element, the generated Adapter class will reside in the same package as the annotated Config.`conversionServiceAdapterPackage`

### Specifying The Conversion Service Bean Name

If your application has multiple beans, you will need to specify the bean name. The allows you to specify it using the property.`ConversionService``SpringMapperConfig``conversionServiceBeanName`

```java
import org.mapstruct.MapperConfig;
import org.mapstruct.extensions.spring.SpringMapperConfig;

@MapperConfig(componentModel = "spring", uses = ConversionServiceAdapter.class)
@SpringMapperConfig(conversionServiceBeanName = "myConversionService")
public interface MapperSpringConfig {
}
```

### External Conversions

Spring ships with a variety of [builtin conversions](https://github.com/spring-projects/spring-framework/tree/main/spring-core/src/main/java/org/springframework/core/convert/support), e.g. to or to . In order to use these (or your own conversions from another module) in the same fashion, you can add them as to your :`String``Locale``Object``Optional``externalConversions``SpringMapperConfig`

```java
import org.mapstruct.MapperConfig;
import org.mapstruct.extensions.spring.ExternalConversion;
import org.mapstruct.extensions.spring.SpringMapperConfig;

import java.util.Locale;

@MapperConfig(componentModel = "spring")
@SpringMapperConfig(
    externalConversions = @ExternalConversion(sourceType = String.class, targetType = Locale.class))
public interface MapstructConfig {}
```

The processor will add the corresponding methods to the generated adapter so MapStruct can use them in the same fashion as the ones for the Converter Mappers in the same module:

```java
import java.lang.String;
import java.util.Locale;
import javax.annotation.Generated;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.extensions.spring.converter.ConversionServiceAdapterGenerator",
    date = "2021-06-25T18:51:21.585Z"
)
@Component
public class ConversionServiceAdapter {
  private final ConversionService conversionService;

  public ConversionServiceAdapter(@Lazy final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  public Locale mapStringToLocale(final String source) {
    return conversionService.convert(source, Locale.class);
  }
}
```



## Spring Type Conversion

[Spring Type Conversion(Spring类型转换) - 海渊 - 博客园 (cnblogs.com)](https://www.cnblogs.com/liuenyuan1996/p/11066202.html)

[Spring Type Conversion(Spring类型转换源码探究) - 海渊 - 博客园 (cnblogs.com)](https://www.cnblogs.com/liuenyuan1996/p/11071845.html)

[Core Technologies (spring.io)](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#core-convert-Converter-API)

```java
package org.springframework.core.convert.converter;

public interface Converter<S, T> {

    T convert(S source);
}
```



```java
package org.springframework.core.convert.support;

final class StringToInteger implements Converter<String, Integer> {

    public Integer convert(String source) {
        return Integer.valueOf(source);
    }
}
```

