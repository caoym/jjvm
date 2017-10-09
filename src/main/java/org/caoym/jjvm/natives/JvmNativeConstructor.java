package org.caoym.jjvm.natives;

import org.caoym.jjvm.lang.JvmClass;
import org.caoym.jjvm.lang.JvmMethod;
import org.caoym.jjvm.runtime.Env;

import java.lang.reflect.Constructor;

/**
 *  native 类的构造器方法
 */
public class JvmNativeConstructor implements JvmMethod {

    private final Constructor constructor;
    private final JvmClass clazz;

    public JvmNativeConstructor(JvmClass clazz, Constructor constructor){
        this.clazz = clazz;
        this.constructor = constructor;
    }
    @Override
    public void call(Env env, Object thiz, Object... args) throws Exception {
        assert (thiz instanceof JvmNativeObject);
        Object object = constructor.newInstance(args);
        ((JvmNativeObject) thiz).setNativeObject(object);
    }

    @Override
    public int getParameterCount() {
        return constructor.getParameterCount();
    }

    @Override
    public String getName() {
        return "<init>";
    }
}
