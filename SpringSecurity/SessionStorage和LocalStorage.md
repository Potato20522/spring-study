## 简介

在HTML5之前，开发人员一般是通过使用Cookie在客户端保存一些简单的信息的。在HTML5发布后，提供了一种新的客户端本地保存数据的方法，那就是Web Storage，它也被分为：LocalStorage和SessionStorage，它允许通过JavaScript在Web浏览器中以键值对的形式保存数据。而相比Cookie有如下优点：

1. 拥有更大的存储容量，Cookie是4k，Web Storage为5M。
2. 操作数据相比Cookie更简单。
3. 不会随着每次请求发送到服务端。

## 区别

区别：localStorage生命周期是永久，除非用户清除localStorage信息，否则这些信息将永远存在；sessionStorage生命周期为当前窗口或标签页，一旦窗口或标签页被永久关闭了，那么所有通过它存储的数据也就被清空了。

LocalStorage和SessionStorage之间的主要区别在于浏览器窗口和选项卡之间的数据共享方式不同。

LocalStorage可跨浏览器窗口和选项卡间共享。就是说如果在多个选项卡和窗口中打开了一个应用程序，而一旦在其中一个选项卡或窗口中更新了LocalStorage，则在所有其他选项卡和窗口中都会看到更新后的LocalStorage数据。

但是，SessionStorage数据独立于其他选项卡和窗口。如果同时打开了两个选项卡，其中一个更新了SessionStorage，则在其他选项卡和窗口中不会反映出来。举个例子：假设用户想要通过两个浏览器选项卡预订两个酒店房间。由于这是单独的会话数据，因此使用SessionStorage是酒店预订应用程序的理想选择。

## 安全性说明

Web Storage的存储对象是独立于域名的，也就是说不同站点下的Web应用有着自己独立的存储对象，互相间是无法访问的，在这一点上SessionStorage和LocalStorage是相同的。

举个例子：部署在abc.com上的Web应用无法访问xyz.com的Web Storage存储对象。

同样，对于子域名也是一样，尽管[www.grapecity.com.cn和gcdn.grapecity.com.cn](http://www.grapecity.com.xn--cngcdn-j76j.grapecity.com.cn/) 同属 grapecity.com.cn 主域下，但它们相互不能访问对方的存储对象。

另外，不仅对子域名相互独立，对于针对使用http和https协议间也是不同的，所以这一点也需要注意。

## 应对跨站点脚本攻击（XSS）

首先，什么是XSS攻击？

XSS是将一段恶意脚本添加到网页上，通过浏览器加载而执行从而达到攻击并获得隐私信息的目的。

LocalStorage和SessionStorage在这一点上都容易受到XSS攻击。攻击者可直接向存储对象添加恶意脚本并执行。因此不太建议把一些敏感的个人信息存储在Web Storage中，例如：

- 用户名密码
- 信用卡资料
- JsonWeb令牌
- API密钥
- SessionID

### 如何避免攻击

- 尽量不要用同一域名部署多个Web应用程序，如果有这种场景请尽量使用子域名部署应用，因为一旦多应用使用统一的域名，这将会对所有的用户共享Web存储对象。
- 一旦将数据存储在LocalStorage中，开发人员在用户将其清除之前无法对其进行任何控制。如果希望在会话结束后自动删除数据，请使用SessionStorage。
- 从WebStorage读取出的数据都要验证、编码和转义。
- 在保存进WebStorage前将数据加密。

## 小结

虽然WebStorage很好用，还是建议你在如下的情况下使用：

- 没有敏感数据
- 数据尺寸小于 5MB
- 高性能并不重要