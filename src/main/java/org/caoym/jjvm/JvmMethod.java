package org.caoym.jjvm;

import com.sun.tools.classfile.AccessFlags;

public interface JvmMethod {
    /**
     * 执行对象或者类方法
     */
    public Object call(Env env, Object thiz, Object ...args) throws Exception ;
    public AccessFlags getAccessFlags();
}
