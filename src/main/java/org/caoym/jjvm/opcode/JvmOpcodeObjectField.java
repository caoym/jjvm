package org.caoym.jjvm.lang;

import org.caoym.jjvm.runtime.Env;
import com.sun.tools.classfile.Field;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class JvmOpcodeObjectField implements JvmField{
    private JvmOpcodeClass clazz;
    private Field field;

    public JvmOpcodeObjectField(JvmOpcodeClass clazz, Field field){
        this.clazz = clazz;
        this.field = field;
    }
    @Override
    public void set(Env env, Object thiz, Object value) throws IllegalAccessException {
        throw new NotImplementedException();
    }

    @Override
    public Object get(Env env, Object thiz) throws IllegalAccessException {
        throw new NotImplementedException();
    }
}
