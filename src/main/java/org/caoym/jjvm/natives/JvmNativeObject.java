package org.caoym.jjvm.natives;

import org.caoym.jjvm.lang.JvmObject;

public class JvmNativeObject implements JvmObject{

    private Object object;
    private final JvmNativeClass clazz;

    public JvmNativeObject(JvmNativeClass clazz){
        this.clazz = clazz;
    }

    public Object getNativeObject() {
        return object;
    }

    public void setNativeObject(Object object) {
        this.object = object;
    }
}
