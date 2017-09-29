package org.caoym.jjvm.lang;

import org.caoym.jjvm.runtime.Env;

/**
 * Jvm 内部的 Class 表示
 */
public interface JvmClass {

    /**
     * 创建实例
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Object newInstance(Env env) throws InstantiationException, IllegalAccessException;
    /**
     * 获取方法
     * @param name 方法名，如`main`
     * @param desc 方法类型描述，如`([Ljava/lang/String;)V`
     * @param flags 访问控制标识 {@see sun.jvm.hotspot.oops.AccessFlags}
     * @return
     * @throws NoSuchMethodException
     */
    public JvmMethod getMethod(String name, String desc, int flags) throws NoSuchMethodException;

    /**
     * 获取属性
     * @param name 属性名
     * @return
     * @throws NoSuchFieldException
     */
    public JvmField getField(String name) throws NoSuchFieldException, IllegalAccessException;
}
