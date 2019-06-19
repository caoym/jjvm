# JJvm

这是一个Java实现的JAVA虚拟机，它会非常简单，实际上简单的只够运行HelloWorld。虽然简单，但尽量符合 JVM 标准，目前主要参考依据是[《Java虚拟机规范 （Java SE 7 中文版）》](http://www.iteye.com/topic/1117824)。

非原创，原项目来自 https://github.com/caoym/jjvm ，
	运行方式有改动(原项目运行方式在我的环境不work)
	pc和opcode运行时处理有修改(原实现无法支持条件跳转; 且增加一些其他opcode)

## 参考项目
	java版jvm实现( https://github.com/zachaxy/JVM )
	go版jvm实现( https://github.com/zxh0/jvmgo-book )


# 用法

## 环境
SUN jdk1.8 (OpenJdk1.8 运行缺少一些jar，用SUN JDK重装后解决)

```shell
$java -version
java version "1.8.0_211"
Java(TM) SE Runtime Environment (build 1.8.0_211-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.211-b12, mixed mode)
```

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
### Sample1
基本HelloWorld

```shell
#编译 sample1
$javac org/caoym/jjvm/JJvm.java -XDignore.symbol.file=true
$javac org/caoym/jjvm/JJvm.java -Xlint:unchecked
$javac org/caoym/samples/sample1/HelloWorld.java 

#运行
$java org.caoym.jjvm.JJvm . org.caoym.samples.sample1.HelloWorld
```

### Sample2
带有Interface、父子类继承的Helloworld

```shell
#编译 sample2
$javac org/caoym/jjvm/JJvm.java -XDignore.symbol.file=true
$javac org/caoym/jjvm/JJvm.java -Xlint:unchecked
$javac org/caoym/samples/sample2/Main.java 

#运行(不加运行参数如inputVar1，会报错)
$java org.caoym.jjvm.JJvm . org.caoym.samples.sample2.Main inputVar1         
```

### Sample3
带有条件跳转的Helloworld

```shell
#编译 sample3
$javac org/caoym/jjvm/JJvm.java -XDignore.symbol.file=true
$javac org/caoym/jjvm/JJvm.java -Xlint:unchecked
$javac org/caoym/samples/sample3/HelloWorld.java 

#运行
$java org.caoym.jjvm.JJvm . org.caoym.samples.sample3.HelloWorld
```

## FAQ
### error: package jdk.internal.org.objectweb.asm does not exist
```shell
默认 javac 不读取 rt.jar 中 classes内容，只读取符号表
	符号表一般只包括标准API和部分其他API，如 com.sun., com.oracle. and sun.*
	
编译选项加上 -XDignore.symbol.file=true
	javac org/caoym/jjvm/JJvm.java -XDignore.symbol.file=true
```

### 未能自动清理旧class文件，重新编译前手动清理一下，执行
```shell
$find . -name *.class -type f -print -exec rm -rf {} \;
```
