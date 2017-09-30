package org.caoym.jjvm.lang;

import org.caoym.jjvm.runtime.Env;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class JvmOpcodeObject{

    private JvmOpcodeClass clazz;
    public JvmOpcodeObject(JvmOpcodeClass clazz){
        this.clazz = clazz;
    }
    /**
     * 对应"<init>":()V
     * @throws Exception
     */
    public void init(Env env)throws Exception{
        JvmMethod method = clazz.getMethod("<init>", "()V", 0);
        method.call(env, this);
    }

    public void putField(String name, Object value) throws NoSuchFieldException, IllegalAccessException {
        throw new NotImplementedException();
    }

    public Object getField(String name) throws NoSuchFieldException, IllegalAccessException {
        throw new NotImplementedException();
    }
}
