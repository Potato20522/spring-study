## 默认值和常数

在target属性为null时才会使用默认值

```java
@Mapper(uses = StringListMapper.class)
public interface SourceTargetMapper {

    SourceTargetMapper INSTANCE = Mappers.getMapper( SourceTargetMapper.class );

    @Mapping(target = "stringProperty", source = "stringProp", defaultValue = "undefined")
    @Mapping(target = "longProperty", source = "longProp", defaultValue = "-1")
    @Mapping(target = "stringConstant", constant = "Constant Value")
    @Mapping(target = "integerConstant", constant = "14")
    @Mapping(target = "longWrapperConstant", constant = "3001")
    @Mapping(target = "dateConstant", dateFormat = "dd-MM-yyyy", constant = "09-01-2014")
    @Mapping(target = "stringListConstants", constant = "jack-jill-tom")
    Target sourceToTarget(Source s);
}
```

## expression 

```java
@Mapper
public interface SourceTargetMapper {

    SourceTargetMapper INSTANCE = Mappers.getMapper( SourceTargetMapper.class );

    @Mapping(target = "timeAndFormat",
         expression = "java( new org.sample.TimeAndFormat( s.getTime(), s.getFormat() ) )")
    Target sourceToTarget(Source s);
}
```



```java
imports org.sample.TimeAndFormat;

@Mapper( imports = TimeAndFormat.class )
public interface SourceTargetMapper {

    SourceTargetMapper INSTANCE = Mappers.getMapper( SourceTargetMapper.class );

    @Mapping(target = "timeAndFormat",
         expression = "java( new TimeAndFormat( s.getTime(), s.getFormat() ) )")
    Target sourceToTarget(Source s);
}
```

## Default Expressions

在target属性为null时才会使用Default Expressions

```java
imports java.util.UUID;

@Mapper( imports = UUID.class )
public interface SourceTargetMapper {

    SourceTargetMapper INSTANCE = Mappers.getMapper( SourceTargetMapper.class );

    @Mapping(target="id", source="sourceId", defaultExpression = "java( UUID.randomUUID().toString() )")
    Target sourceToTarget(Source s);
}
```

## 定义返回类型

```java
@Mapper( uses = FruitFactory.class )
public interface FruitMapper {

    @BeanMapping( resultType = Apple.class )
    Fruit map( FruitDto source );

}
```



```java
public class FruitFactory {

    public Apple createApple() {
        return new Apple( "Apple" );
    }

    public Banana createBanana() {
        return new Banana( "Banana" );
    }
}
```

## null值映射策略



