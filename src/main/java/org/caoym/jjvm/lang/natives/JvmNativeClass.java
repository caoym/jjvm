package org.caoym.jjvm.lang.natives;

import jdk.internal.org.objectweb.asm.Type;
import org.caoym.jjvm.lang.JvmClass;
import org.caoym.jjvm.lang.JvmField;
import org.caoym.jjvm.lang.JvmMethod;
import org.caoym.jjvm.runtime.Env;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 包装 native 类
 */
public class JvmNativeClass implements JvmClass {

    private Class nativeClass;
    private HashMap<String, JvmNativeMethod> methods = new HashMap<>();

    public JvmNativeClass(Class nativeClass){
        this.nativeClass = nativeClass;
        for (Method method : nativeClass.getMethods()) {
            String key = method.getName()+":"+Type.getMethodDescriptor(method);
            methods.put(key, new JvmNativeMethod(method));
        }
    }

    @Override
    public Object newInstance(Env env) throws InstantiationException, IllegalAccessException {
        return nativeClass.newInstance();
    }

    @Override
    public JvmMethod getMethod(String name, String desc, int flags) throws NoSuchMethodException {
        JvmNativeMethod found = methods.get(name+":"+desc);
        if(found == null){
            throw new NoSuchMethodException(name+":"+desc+" not exist");
        }
        int modifiers = found.getNativeMethod().getModifiers();
        if((modifiers|flags) != modifiers){
            throw new NoSuchMethodException(name+":"+desc+" with flags not exist");
        }
        return found;
    }

    @Override
    public JvmField getField(String name) throws NoSuchFieldException, IllegalAccessException {
        return new JvmNativeField(this, nativeClass.getField(name));
    }

    public Class getNativeClass() {
        return nativeClass;
    }
}
