package org.caoym.jjvm.natives;

import org.caoym.jjvm.runtime.Env;
import org.caoym.jjvm.lang.JvmMethod;
import org.caoym.jjvm.runtime.StackFrame;

import java.lang.reflect.Method;

/**
 * 包装 native 方法
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

    @Override
    public int getParameterCount() {
        return method.getParameterCount();
    }

    public Method getNativeMethod() {
        return method;
    }
}
