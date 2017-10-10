package org.caoym.jjvm.natives;

import org.caoym.jjvm.lang.JvmClass;
import org.caoym.jjvm.lang.JvmClassLoader;
import org.caoym.jjvm.lang.JvmObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;

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

    @Override
    public JvmObject getSuper() {
        throw new NotImplementedException();
    }

    @Override
    public JvmClass getClazz() {
        return clazz;
    }

    static public Object wrap(Object object, Class clazz, JvmClassLoader classLoader){
        if(object == null){
            return null;
        }
        // 非基础类型，需要用JvmNativeObject包装
        String primitiveTypes[] = {
                byte.class.getName(),
                short.class.getName(),
                int.class.getName(),
                long.class.getName(),
                char.class.getName(),
                float.class.getName(),
                double.class.getName(),
                boolean.class.getName()
        };
        Arrays.sort(primitiveTypes);
        if(Arrays.binarySearch(primitiveTypes, clazz.getName()) <0 ){
            JvmNativeObject wrap = new JvmNativeObject(new JvmNativeClass(classLoader, clazz));
            wrap.setNativeObject(object);
            return wrap;
        }
        return object;
    }

    static public Object unwrap(Object object){
        if(object instanceof JvmNativeObject){
            return ((JvmNativeObject) object).getNativeObject();
        }
        return object;
    }
    static public Object[] multiUnwrap(Object[] objects){
        Object[] res = new Object[objects.length];
        for (int i=0; i<objects.length; i++) {
            res[i] = unwrap(objects[i]);
        }
        return res;
    }
}
