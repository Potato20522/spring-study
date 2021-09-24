# DAO 和Repository 对比

https://www.baeldung.com/java-dao-vs-repository

## DAO

DAO：Data Access Object 数据访问对象，最接近持久化数据，以数据库表为中心

来看一个实例，有一个User这个表的增删改查，如下

 User domain：

```java
public class User {
    private Long id;
    private String userName;
    private String firstName;
    private String email;

    // getters and setters
}
```

UserDao接口：

```java
public interface UserDao {
    void create(User user);
    User read(Long id);
    void update(User user);
    void delete(String userName);
}
```

UserDao实现类：

```java
public class UserDaoImpl implements UserDao {
  //JPA里的EntityManager
    private final EntityManager entityManager;
    
    @Override
    public void create(User user) {
        entityManager.persist(user);
    }

    @Override
    public User read(long id) {
        return entityManager.find(User.class, id);
    }

    // ...
}
```

## Repository 

Repository 是一种用于封装存储、检索和搜索行为的机制，它模拟对象集合，使用类似集合的接口在域和数据映射层之间进行调解，以访问域对象。

Repository 类似于DAO，但处在更高的级别，更接近业务逻辑。

Repository 使用DAO从数据库中增删改查

还是看User这个表的增删改查，使用Repository 来实现.

UserRepository接口：

```java
public interface UserRepository {
    User get(Long id);
    void add(User user);
    void update(User user);
    void remove(User user);
}
```

和UserDao功能一样的，但是注意这里add、update、remove用的是user对象，而不是参数

UserRepository实现类：

```java
public class UserRepositoryImpl implements UserRepository {
    private UserDaoImpl userDaoImpl;
    
    @Override
    public User get(Long id) {
        User user = userDaoImpl.read(id);
        return user;
    }

    @Override
    public void add(User user) {
        userDaoImpl.create(user);
    }

    // ...
}
```

到目前为止，我们可以说 DAO 和Repository 的实现看起来非常相似，因为这里的User类是一个贫血域。而且，Repository 只是数据访问层 (DAO) 之上的另一层。

所以，DAO用来访问数据，Repository 实现业务逻辑

## 依赖多个DAO的Repository 

保存推文信息的Tweet类：

```java
public class Tweet {
    private String email;
    private String tweetText;    
    private Date dateCreated;

    // getters and setters
}
```

DAO:

```java
public interface TweetDao {
    List<Tweet> fetchTweets(String email);    
}

public class TweetDaoImpl implements TweetDao {
    @Override
    public List<Tweet> fetchTweets(String email) {
        List<Tweet> tweets = new ArrayList<Tweet>();
        
        //call Twitter API and prepare Tweet object
        
        return tweets;
    }
}
```



```
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'flywayInitializer' defined in class path resource [org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$FlywayConfiguration.class]: Invocation of init method failed; nested exception is org.flywaydb.core.api.FlywayException: Found non-empty schema(s) "public" but no schema history table. Use baseline() or set baselineOnMigrate to true to initialize the schema history table.

```

