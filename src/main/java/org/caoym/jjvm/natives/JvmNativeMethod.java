package org.caoym.jjvm.natives;

import org.caoym.jjvm.Env;
import org.caoym.jjvm.JvmMethod;

import java.lang.reflect.Method;

/**
 * Created by caoyangmin on 2017/9/7.
 */
public class JvmNativeMethod implements JvmMethod {

    private Method method;

    public JvmNativeMethod(Method method){
        this.method = method;
    }
    @Override
    public Object call(Env env, Object thiz, Object... args) throws Exception {
        return method.invoke(thiz, args);
    }

    public Method getNativeMethod() {
        return method;
    }
}
