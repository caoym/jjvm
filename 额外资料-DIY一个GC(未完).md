# 原文信息

	地址 https://shipilev.net/jvm/diy-gc/#_epsilon_gc

	感谢
		```
		Aleksey Shipilёv, JVM/Performance Geek, redhat logo
		Shout out at Twitter: @shipilev
		Questions, comments, suggestions: aleksey@shipilev.net

		This post is also available in ePUB and mobi.
		Thanks to Richard Startin, Roman Kennke, Cyrille Chépélov and others for reviews, edits and helpful suggestions!
		```
		
# 正文

## 简介
	
	构建任何语言的 runtime 部分，都是一次有趣的练习。至少，在第一次时是如此。但如果要建立一个可靠的、高性能的、易观察和调试、容易查错的runtime系统，则是一件非常挑战的事情。
	
	建立一个简单 GC 看起来很简单，我们将在这里进行一次。Roman Kennke 在 2019年度 FOSDEM (自由及开源软件开发者欧洲会议，Free and Open source Software Developers' European Meeting，简称FOSDEM)
	发表了一次演讲并展示了一个 "如何在20分钟内开发一个GC" 的 demo，他使用了一个早期版本的 GC。实际代码附有很多说明，本文来自于其中一部分。
	
	如果对 GC 有一定了解，会对阅读本文很有帮助。这里一些关于概念和Hotspot Specifics 的内容，但更深入的内容请参看 [GC Handbook](http://gchandbook.org/) 的第一章，也可以参看 [Wikipedia article](https://en.wikipedia.org/wiki/Tracing_garbage_collection)
	
### 1.建立 Blocks

	参照着已有的 GC 实现再做一个新的 GC，有现成可借用/参考的部分，容易很多。

#### 1.1. Epsilon GC

	[JEP 318: "Epsilon: A No-Op Garbage Collector (Experimental)" ](https://openjdk.java.net/jeps/318)在 OpenJDK 11 被引入。目的是提供一个最小实现，用于对回收没有需要/被禁用 的场景。更多信息请参看 JEP。

	从这个实现的角度，GC 这个用词不太恰当，用“自动化内存管理”更合适。它包含内存分配和回收两部分。Epsilon GC 只包含其中的内存分配功能，没有涉及回收部分，因此在它之上建立实际的 GC 会比较容易。
	
##### 1.1.1. 分配
		
	Epsilon GC 最完善的部分是[分配地址的处理](http://hg.openjdk.java.net/jdk/jdk/file/6c96d42ec3e7/src/hotspot/share/gc/epsilon/epsilonHeap.cpp#l173)。
	它给请求方提供强制对齐后的内存块，或按请求的size分配 TLAB[Thread-Local Allocation Buffer](https://shipilev.net/jvm/anatomy-quarks/4-tlab-allocation/)。
	实现本身没对 TLABs 做太多扩充，主要因为没有回收机制。
	
##### 1.1.2. 内存屏障

	有些 GC 强制要求 runtime 和 应用
	
	Epsilon 不需要内存屏障，但 runtime 和 编译器 仍需要知道它是必选的。从前插入内存屏障的实现很琐碎，从 OpenJDK 11开始，根据[JEP-304: "Garbage Collection Interface"](https://openjdk.java.net/jeps/304)，变得简单多了。
	需要留意的是，Epsilon 没有实现内存屏障，它将所有工作，包括load、store、CAS、arraycpoy，都委托给基础的内存屏障。如果我们要构建一个不需要内存屏障的GC，直接重用Epsilon的就好。
	
##### 1.1.3. 监控用的钩子
	
	GC中最后一个细碎繁琐的部分是 JVM 监控，例如 MX bean、诊断命令支持等，Epsilon在[这里](http://hg.openjdk.java.net/jdk/jdk/file/tip/src/hotspot/share/gc/epsilon/epsilonMonitoringSupport.cpp)提供了实现。
	
#### 1.2 Runtime 和 GC
	
##### 1.2.1 根节点
	
	GC 需要知道哪些 Heap 上的对象被引用。这要通过 根节点 来判断，包括线程栈、本地变量(包括 JIT 生成的代码)、JNI类和classloader、JNI handle等。
	
##### 1.2.2. 对象遍历
	
	GC 也需要遍历Java对象。但因为这是常见功能，因此 GC 的公共实现部分已经提供了。
	具体可参看文中后续部分 obj->oop_iterate。
	
##### 1.2.3 移动对象

	GC 需要记录被移动对象的新地址。主要有以下几种方式：
	
	1.复用对象的"mark word"字段(Serial, Parallel 等算法)
		当 STW 时，所有对象访问都受控，Java Thread对该字段也不可见。因此可复用该字段。
	2.维护独立移动表(ZGC, C4 等算法)
		该方式会将 GC 同 runtime/其他应用 完全隔离，只有 GC 能感知 移动表 的存在。
		这也是为何 并发 GC 经常使用该方式。
	3.Java对象增加额外字段记录(Shenandoah 等算法)
		[Shenandoah](http://openjdk.java.net/jeps/189)，结合以上两种方式：使用 runtime/应用 已有的头部字段，同时用额外字段记录了移动字段的信息。
	
##### 1.2.4 标记对象
	
	GC 需要记录可达的对象，主要有以下几种方式：
	
	1.复用对象的"mark word"字段(Serial, Parallel 等算法)

### 2. Grand Plan


### 3. Implementing GC Core
	
	完整实现在[这里](https://shipilev.net/jvm/diy-gc/webrev/)，本文逐段来说。
	
#### 3.1 序言
	
```
	{
		GCTraceTime(Info, gc) time("Step 0: Prologue", NULL);

		// 提交标记 bitmap ，好处是：
		// 	- 如果没 GC 不消耗内存
		//	- bitmap不可达的部分，会map到0页，以提高处理heap的效率
		if (!os::commit_memory((char*)_bitmap_region.start(), _bitmap_region.byte_size(), false)) {
			log_warning(gc)("Could not commit native memory for marking bitmap, GC failed");
			return;
		}

		// 本算法不会解析heap，但希望回收 thread 的 TLAB 
		ensure_parsability(true);

		// 告诉 runtime 要做 GC 了
		CodeCache::gc_prologue();
		BiasedLocking::preserve_marks();

		// 清理 派生指针
		DerivedPointerTable::clear();
	}
```
	
#### 3.2. 标记

		如果做好标记，STW 将很简单。所以标记也是很多 GC 算法实现的第一步骤。
		
```
	{
		GCTraceTime(Info, gc) time("Step 1: Mark", NULL);

		// 标记栈和闭包会处理大部分工作。
		//	- 闭包会扫描出废弃的引用、标记，并将最近标记的对象压入栈，以便后续处理
		EpsilonMarkStack stack;
		EpsilonScanOopClosure cl(&stack, &_bitmap);

		// 标记根节点
		process_roots(&cl);
		stat_reachable_roots = stack.size();

		// 扫描heap剩余部分
		while (!stack.is_empty()) {
			oop obj = stack.pop();
			obj->oop_iterate(&cl);
			stat_reachable_heap++;
		}

		// 标记完成后，所有派生指针都被找到
		DerivedPointerTable::set_active(false);
	}
```
	
	
	
	
	
	