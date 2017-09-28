package org.caoym.jjvm.lang.natives;

import org.caoym.jjvm.lang.JvmObject;

/**
 * 包装 native 方法
 */

public class JvmNativeObject implements JvmObject{
    /**
     * the native object
     */
    private Object object;

    public JvmNativeObject(Object object){
        this.object = object;
    }

    @Override
    public void putField(String name, Object value) throws NoSuchFieldException, IllegalAccessException {
        getClass().getField(name).set(this.object, value);
    }

    @Override
    public Object getField(String name) throws NoSuchFieldException, IllegalAccessException {
        return getClass().getField(name).get(this.object);
    }
}
