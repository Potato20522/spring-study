参考：https://www.cnblogs.com/zhangzhixi/p/15068221.html

# 简介

Velocity Tools 是 Velocity模板引擎的一个子项目，用于将 Velocity 与 Web开发环境集成的工具包。

VelocityTools项目分为两个部分：`GenericTools`和`VelocityView` .

- GenericTools : `GenericTools`是一组类，它们提供在标准Velocity项目中使用工具的基本基础结构，以及在通用Velocity模板中使用的一组工具。例如 : DateTool、NumberTool和RenderTool很多其他可用的工具
- Velocity view : 包括所有的通用工具结构和在web应用程序的视图层中使用Velocity的专用工具。这包括用于处理Velocity模板请求的`VelocityViewServlet`或`VelocityLayoutServlet`、用于在JSP中嵌入Velocity的`VelocityViewTag`和用于在Velocity模板中嵌入JSP标记库的Maven插件。这里流行的工具是LinkTool和ParameterTool。

