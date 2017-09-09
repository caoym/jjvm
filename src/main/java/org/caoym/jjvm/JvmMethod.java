package org.caoym.jjvm;

import com.sun.tools.classfile.AccessFlags;

public interface JvmMethod {
    /**
     * 执行对象或者类方法
     * 方法被调用时，会产生一个新的栈帧，并推入当前线程的栈
     * 方法执行结束后，栈帧被退出，同时期返回值被推入上一个栈帧（当前方法的调用者）的操作数栈
     */
    public void call(Env env, Object thiz, Object ...args) throws Exception ;
}
