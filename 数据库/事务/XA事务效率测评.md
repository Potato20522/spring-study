## 测试目的

比较多数据源情况下，XA事务带来的效率影响

## 测试环境

电脑：研发本地电脑

数据库：postgresql, RTCloudPatientDB数据库和RTCloudTPSApplicationDB数据库

项目： Java后端，WebTps项目 

用到的框架：ORM框架：Mybtais，事务框架：Atomikos，单元测试框架：Junit5，依赖注入框架：SpringBoot

## 测试用例

Patient和Lock同处于RTCloudPatientDB数据库，Patient和Ae同处于RTCloudTPSApplicationDB数据库

这样可以对比XA事务在同一数据库和不同数据库时执行的效率

- 保存Patient和Lock，各100条数据
- 保存Patient和Ae，各100条数据

保存逻辑的方法，入参是各100条数据，如下

构造patient数据：

```java
ReflectUtil.setId(patientEntity, idGeneratorGateway.newLong());//雪花Id
patientEntity.setCRC(111L);
patientEntity.setPatientType(PatientType.Real);
patientEntity.setCreationTime(LocalDateTime.now());
patientEntities.add(patientEntity);
```

构造lock数据：

```java
ReflectUtil.setId(lockEntity, idGeneratorGateway.newLong());
lockEntity.setCreationTime(LocalDateTime.now());
lockEntity.setLockTypes(1L);
lockEntity.setLockObjectId(1L);
lockEntity.setUserId(1L);
lockEntities.add(lockEntity);
```

构造ae数据

```java
AeEntity aeEntity = ReflectUtil.newInstance(AeEntity.class);
ReflectUtil.setId(aeEntity, idGeneratorGateway.newLong());
aeEntity.setCreationTime(LocalDateTime.now());
aeEntity.setActive(false);
aeEntities.add(aeEntity);
```

## 单元测试

计时器写在单元测试方法中，包住上述保存逻辑的方法

保存Patient和Lock

```java
@RepeatedTest(5)
void savePatientAndLock() {
  List<PatientEntity> patientEntities = transactionTime.generatePatientList(0, count);
  List<LockEntity> lockEntities = transactionTime.generateLockList(0, count);
  long start = System.currentTimeMillis();
  transactionTime.savePatientAndLock(patientEntities,lockEntities);
  long end = System.currentTimeMillis();
  log.info("savePatientAndLock:{} ms",end-start);
}
```

保存Patient和Ae

```java
@RepeatedTest(5)
void savePatientAndAe() {
    List<PatientEntity> patientEntities = transactionTime.generatePatientList(0, count);
    List<AeEntity> aeEntities = transactionTime.generateAeList(0, count);
    long start = System.currentTimeMillis();
    transactionTime.savePatientAndAe(patientEntities,aeEntities);
    long end = System.currentTimeMillis();
    log.info("savePatientAndAe:{} ms",end-start);
}
```



## 测试结果

|                                                  | Patient和Lock处于同一个库   |                      | Patient和Ae处于不同的库   |                    |
| ------------------------------------------------ | --------------------------- | -------------------- | ------------------------- | ------------------ |
|                                                  | savePatientAndLock(各100条) | deletePatientAndLock | savePatientAndAe(各100条) | deletePatientAndAe |
| 先执行savePatientAndLock，再执行savePatientAndAe |                             |                      |                           |                    |
| 时间（ms）测试 -1                                | 164                         | 8                    | 49                        | 10                 |
| 时间（ms）测试 -2                                | 183                         | 7                    | 39                        | 9                  |
| 时间（ms）测试 -3                                | 178                         | 6                    | 55                        | 9                  |
| 先执行savePatientAndAe，再执行savePatientAndLock |                             |                      |                           |                    |
| 时间（ms）测试 -4                                | 34                          | 7                    | 143                       | 10                 |
| 时间（ms）测试 -5                                | 32                          | 5                    | 148                       | 9                  |
| 时间（ms）测试 -6                                | 42                          | 5                    | 166                       | 12                 |



| 重复跑5次，取最后一次         |                    |                      |                  |                    |
| ----------------------------- | ------------------ | -------------------- | ---------------- | ------------------ |
|                               | savePatientAndLock | deletePatientAndLock | savePatientAndAe | deletePatientAndAe |
| 时间（ms）测试 -7             | 27                 | 2                    | 26               | 2                  |
| 时间（ms）测试 -8             | 30                 | 4                    | 28               | 5                  |
| 重复跑5次，取最后一次，雪花Id |                    |                      |                  |                    |
| 时间（ms）测试 -9             | 25                 |                      | 25               |                    |
| 时间（ms）测试 -10            | 41                 |                      | 26               |                    |
| 时间（ms）测试 -11            | 24                 |                      | 39               |                    |





| 下面是重新计时（计时器写在单元测试中） |                    |                  |
| -------------------------------------- | ------------------ | ---------------- |
| 重复跑5次，取最后一次，雪花Id，无事务  |                    |                  |
|                                        | savePatientAndLock | savePatientAndAe |
| 时间（ms）测试 -12                     | 31                 | 30               |
| 时间（ms）测试 -13                     | 28                 | 29               |
| 时间（ms）测试 -14                     | 32                 | 32               |
| 重复跑5次，取最后一次，雪花Id，XA事务  |                    |                  |
| 时间（ms）测试 -15                     | 30                 | 53               |
| 时间（ms）测试 -16                     | 29                 | 54               |
| 时间（ms）测试 -17                     | 29                 | 56               |



## 结论

去除单元测试执行顺序、没有预热、计时器位置不对带来的影响。

在各插入100条数据的情况下

**多数据库同时批量插入时，XA事务执行时间多了80%（30ms到53ms）；同一数据库内，XA事务则几乎没有影响**

