package org.caoym.jjvm.lang;

import org.caoym.jjvm.runtime.Env;

/**
 * Jvm 内部的 Class 表示
 */
public interface JvmClass {

    /**
     * 分配实例的内存空间，但不执行实例的构造函数
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public JvmObject newInstance(Env env) throws InstantiationException, IllegalAccessException;
    /**
     * 获取方法
     * @param name 方法名，如`main`
     * @param desc 方法类型描述，如`([Ljava/lang/String;)V`
     * @return
     * @throws NoSuchMethodException
     */
    public JvmMethod getMethod(String name, String desc) throws NoSuchMethodException;

    /**
     * 获取方法
     * @param name 方法名，如`main`
     * @param desc 方法类型描述，如`([Ljava/lang/String;)V`
     */
    public boolean hasMethod(String name, String desc);

    /**
     * 获取属性
     * @param name 属性名
     * @return
     * @throws NoSuchFieldException
     */
    public JvmField getField(String name) throws NoSuchFieldException, IllegalAccessException;

    /**
     * 获取当前类的 class loader
     * @return
     */
    public JvmClassLoader getClassLoader();

    /**
     * 返回父类
     */
    public JvmClass getSuperClass() throws ClassNotFoundException;

    /**
     * 返回类名
     */
    public String getName();
}
