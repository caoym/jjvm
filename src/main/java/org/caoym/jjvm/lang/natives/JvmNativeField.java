package org.caoym.jjvm.lang.natives;

import org.caoym.jjvm.lang.JvmClass;
import org.caoym.jjvm.lang.JvmField;
import org.caoym.jjvm.runtime.Env;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Field;

/**
 * Created by caoyangmin on 2017/9/29.
 */
public class JvmNativeField implements JvmField{

    JvmNativeClass nativeClass;
    Field filed;
    public JvmNativeField(JvmNativeClass nativeClass, Field field){
        this.nativeClass = nativeClass;
        this.filed = field;
    }
    @Override
    public Object get(Env env, Object thiz) throws IllegalAccessException {
        return filed.get(thiz);
    }

    @Override
    public void set(Env env, Object thiz,  Object value) throws IllegalAccessException {
        filed.set(thiz, value);
    }
}
