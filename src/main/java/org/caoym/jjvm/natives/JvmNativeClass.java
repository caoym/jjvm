package org.caoym.jjvm.natives;

import jdk.internal.org.objectweb.asm.Type;
import org.caoym.jjvm.JvmClass;
import org.caoym.jjvm.JvmMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by caoyangmin on 2017/9/7.
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
    public Object getField(String name, String type, int flags) throws NoSuchFieldException, IllegalAccessException {
        Field filed = nativeClass.getField(name);
        return filed.get(nativeClass);
    }
}
