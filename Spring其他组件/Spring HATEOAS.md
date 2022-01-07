# 介绍

[Spring HATEOAS](https://spring.io/projects/spring-hateoas)

[spring hateoas初体验_Hello World ^—^-CSDN博客](https://blog.csdn.net/w57685321/article/details/82894803)

HATEOAS是Hypertext As The Engine Of Application State的缩写。在Richardson Maturity Model中, 它是REST的最高级形态。

在介绍 HATEOAS 之前，先介绍一下 Richardson 提出的 REST 成熟度模型。该模型把 REST 服务按照成熟度划分成 4 个层次：

第一个层次（Level 0）的 Web 服务只是使用 HTTP 作为传输方式，实际上只是远程方法调用（RPC）的一种具体形式。SOAP 和 XML-RPC 都属于此类。
第二个层次（Level 1）的 Web 服务引入了资源的概念。每个资源有对应的标识符和表达。
第三个层次（Level 2）的 Web 服务使用不同的 HTTP 方法来进行不同的操作，并且使用 HTTP 状态码来表示不同的结果。如 HTTP GET 方法来获取资源，HTTP DELETE 方法来删除资源。
第四个层次（Level 3）的 Web 服务使用 HATEOAS。在资源的表达中包含了链接信息。客户端可以根据链接来发现可以执行的动作。
该模型将REST划作了由低到高四个等级，等级越高，RESTful就越成熟。关于restful层次的网上也有很多例子，这里就不赘述了。
需要注意的是，熟透了东西不一定好，甚至可能烂了，所以，项目中对于RESTful层级的选择要灵活把控，现在最常用的就是level2这个层次。

关于level2中有个比较常出错的地方，URI中不应该包含动词。 因为"资源"表示一种实体，所以应该是名词，URI不应该有动词，动词应该放在HTTP协议中。举例来说，某个URI是/posts/show/1，其中show是动词，这个URI就设计错了，正确的写法应该是/posts/1，然后用GET方法表示show。如果某些动作是HTTP动词表示不了的，你就应该把动作做成一种资源。比如网上汇款，从账户1向账户2汇款500元，错误的URI是：POST /accounts/1/transfer/500/to/2,正确的写法是把动词transfer改成名词transaction，然后以参数的方式注明其它参数
POST /accounts/transaction?from=1&to=2&amount=500.00
RESTful API最好做到Hypermedia（HATEOAS），即返回结果中提供链接，连向其他API方法，使得用户不查文档，也知道下一步应该做什么。

HATEOAS又是什么鬼？
我们知道REST是使用标准的HTTP方法来操作资源的，但仅仅因此就理解成带CURD的Web数据库架构就太过于简单了。 这种说法忽略了一个核心概念: “超媒体即应用状态引擎（hypermedia as the engine of application state）”。 超媒体是什么? 当你浏览Web网页时，从一个连接跳到一个页面，再从另一个连接跳到另外一个页面，就是利用了超媒体的概念: 把一个个把资源链接起来。
要达到这个目的，就要求在表述格式里边加入链接来引导客户端。在《RESTFul Web Services》一书中，作者把这种具有链接的特性成为连通性。
RESTful API最好做到Hypermedia,或HATEOAS，即返回结果中提供链接，连向其他API方法，使得用户不查文档，也知道下一步应该做什么。比如，当用户向api.example.com的根目录发出请求，会得到这样一个文档。

```json
{"link": {
  "rel":   "collection https://www.example.com/zoos",
  "href":  "https://api.example.com/zoos",
  "title": "List of zoos",
  "type":  "application/vnd.yourformat+json"
}}
```


上面代码表示，文档中有一个link属性，用户读取这个属性就知道下一步该调用什么API了。rel表示这个API与当前网址的关系（collection关系，并给出该collection的网址），href表示API的路径，title表示API的标题，type表示返回类型。
Hypermedia API的设计被称为HATEOAS。Github的API就是这种设计，访问api.github.com会得到一个所有可用API的网址列表。

```json
{
  "current_user_url": "https://api.github.com/user",
  "authorizations_url": "https://api.github.com/authorizations",
  // ...
}
```


从上面可以看到，如果想获取当前用户的信息，应该去访问api.github.com/user，然后就得到了下面结果。

```json
{
  "message": "Requires authentication",
  "documentation_url": "https://developer.github.com/v3"
}
```

