# 默认映射规则

- **同名同类型**的属性，自动映射
- 自动类型转换
  - 基本类型和包装类之间互转
  - 基本类型（包括包装类）与String之间互转
  - 日期类型与String之间互转

# 套娃映射补充

直接用点就可以

```java
 @Mapper
 public interface CustomerMapper {

     @Mapping( target = "name", source = "record.name" )
     @Mapping( target = ".", source = "record" )
     @Mapping( target = ".", source = "account" )
     Customer customerDtoToCustomer(CustomerDto customerDto);
 }
```

# 数据传输

## @context

```java
public abstract CarDto toCar(Car car, @Context Locale translationLocale);
protected OwnerManualDto translateOwnerManual(OwnerManual ownerManual, @Context Locale locale) {
    // manually implemented logic to translate the OwnerManual with the given
    Locale
}
```



```java
//GENERATED CODE
public CarDto toCar(Car car, Locale translationLocale) {
    if ( car == null ) {
        return null;
    }
    CarDto carDto = new CarDto();
    carDto.setOwnerManual( translateOwnerManual( car.getOwnerManual(),translationLocale );
    // more generated mapping code
    return carDto;
}
```



# @BeanMapping

批量忽略

- ignoreByDefault:忽略mapstruct的默认映射行为，避免不必要的赋值、避免属性覆盖，只映射了配置了@Mapping的那些属性

