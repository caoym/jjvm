package org.caoym.jjvm.natives;

import org.caoym.jjvm.lang.JvmClass;
import org.caoym.jjvm.lang.JvmMethod;
import org.caoym.jjvm.runtime.Env;
import org.caoym.jjvm.runtime.StackFrame;

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

        StackFrame frame = env.getStack().newFrame(clazz, this);
        Object object = constructor.newInstance(JvmNativeObject.multiUnwrap(args));
        ((JvmNativeObject) thiz).setNativeObject(object);
        //将返回值推入调用者的操作数栈
        frame.setReturn(null, "void");
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
