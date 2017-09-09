package org.caoym.jjvm.natives;

import com.sun.tools.classfile.AccessFlags;
import org.caoym.jjvm.Env;
import org.caoym.jjvm.JvmMethod;
import org.caoym.jjvm.StackFrame;

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
    public void call(Env env, Object thiz, Object... args) throws Exception {
        StackFrame frame = env.getStack().newFrame();
        Object res = method.invoke(thiz, args);
        //将返回值推入调用者的操作数栈
        frame.setReturn(res, method.getReturnType().getName());
    }

    public Method getNativeMethod() {
        return method;
    }
}
