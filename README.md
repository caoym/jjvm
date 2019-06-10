# JJvm

这是一个Java实现的JAVA虚拟机，它会非常简单，实际上简单的只够运行HelloWorld。虽然简单，但尽量符合 JVM 标准，目前主要参考依据是[《Java虚拟机规范 （Java SE 7 中文版）》](http://www.iteye.com/topic/1117824)。

非原创，原项目来自 https://github.com/caoym/jjvm ，代码暂无改动，运行方式有改动(原项目运行方式在我的环境不work)


# 用法

## 环境
SUN jdk1.8 (OpenJdk1.8 运行缺少一些jar，用SUN JDK重装后解决)

### 设置java环境
将以下内容加入 /etc/profile ，保存后执行 source /etc/profile 使之生效

```shell
JAVA_HOME=/usr/java/jdk1.8.0_211-amd64
JRE_HOME=$JAVA_HOME/jre
PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin
CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib
export JAVA_HOME JRE_HOME PATH CLASSPATH
```

## 编译和运行
可以通过以下编译命令运行：

```shell
#编译
$javac org/jvm/jjvm/JJvm.java -XDignore.symbol.file=true -Xlint:unchecked
$javac org/jvm/samples/HelloWorld.java 

#运行
$java org.jvm.jjvm.JJvm . org.jvm.samples.HelloWorld
```

## FAQ
###  error: package jdk.internal.org.objectweb.asm does not exist

默认 javac 不读取 rt.jar 中 classes内容，只读取符号表
	- 符号表一般只包括标准API和部分其他API，如 com.sun., com.oracle. and sun.*
编译选项加上 -XDignore.symbol.file=true
javac org/jvm/jjvm/JJvm.java -XDignore.symbol.file=true

