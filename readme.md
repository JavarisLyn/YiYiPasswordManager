# 开发过程中遇到的一些问题，在此记录
## 和服务端连接常见问题
### https://blog.csdn.net/Liu7073/article/details/106637118/
### 关闭防火墙！
---
## OkHTTP的同步和异步方法
### [同步方法](https://www.iteye.com/blog/xuanzhui-2284601)
---
## 自定义对象转为Json格式
### https://www.jianshu.com/p/ef6e829f2907
---

## 加密解密部分的坑
### [看这篇文章，服务端和客户端填充方式不同会导致解密失败！！](https://www.jianshu.com/p/a4a6c6b465b0)
---

## 布局坑
### com.google.android.material.textfield.TextInputLayout而不要使用android.support.design.widget.TextInputLayout
### [详见此文](https://stackoverflow.com/questions/38423809/android-support-design-widget-textinputlayout-could-not-be-instantiated)
---

## 图片存储
### [java中用byte[]mysql中用blob](https://www.cnblogs.com/jerrylz/p/5814460.html)
---

## SpringBoot中实体的属性定义
### 遵守规范，驼峰命名，否则springboot端接收到数据可能为null
---

## ParameType有多个
### dao可以不写ParameType，若Param为对象，可以用a.b的形式来说明
### 用HashMap包装dao层会接收到null
---