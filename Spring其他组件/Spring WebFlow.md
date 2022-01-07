# 介绍

[Spring Web Flow](https://spring.io/projects/spring-webflow#overview)

Spring Web Flow基于Spring MVC，目标是成为管理Web应用页面**流程**的最佳方案，使用 XML来配置。。。



在Spring Web Flow中，一个流是由一个个“状态”组装而成的一系列步骤。进入都每一个“状态”时，通常会有对应的视图展示给到用户。在视图中，用户的事件将交由“状态”做处理。这些事件可以触发到其他状态的转换，从而导致视图切换。