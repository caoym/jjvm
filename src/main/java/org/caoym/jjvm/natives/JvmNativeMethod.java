package org.caoym.jjvm.natives;

import org.caoym.jjvm.lang.JvmClass;
import org.caoym.jjvm.runtime.Env;
import org.caoym.jjvm.lang.JvmMethod;
import org.caoym.jjvm.runtime.StackFrame;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 包装 native 方法
 */
public class JvmNativeMethod implements JvmMethod {

    private final JvmClass clazz;
    private final Method method;
    private final String name;

    public JvmNativeMethod(JvmClass clazz, Method method){
        this.clazz = clazz;
        this.method = method;
        this.name = method.getName();
    }
    @Override
    public void call(Env env, Object thiz, Object... args) throws Exception {
        assert thiz instanceof JvmNativeObject;
        StackFrame frame = env.getStack().newFrame(clazz, this);
        Object object = ((JvmNativeObject) thiz).getNativeObject();
        Object res = method.invoke(object, JvmNativeObject.multiUnwrap(args));
        // 非基础类型，需要用JvmNativeObject包装
        res = JvmNativeObject.wrap(res, method.getReturnType(), clazz.getClassLoader());
        //将返回值推入调用者的操作数栈
        frame.setReturn(res, method.getReturnType().getName());
    }

    @Override
    public int getParameterCount() {
        return method.getParameterCount();
    }

    @Override
    public String getName() {
        return name;
    }

    public Method getNativeMethod() {
        return method;
    }
}
