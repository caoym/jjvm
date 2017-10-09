package org.caoym.jjvm.lang;

/**
 * Created by caoyangmin on 2017/9/29.
 */
public interface JvmObject {
    /**
     * 获取父类实例
     */
    JvmObject getSuper();

    /**
     * 获取实例的类
     */
    JvmClass getClazz();
}
