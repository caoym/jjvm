package org.caoym.jjvm.lang;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class JvmOpcodeObject implements JvmObject {

    public JvmOpcodeObject(){

    }
    /**
     * 对应"<init>":()V
     * @throws Exception
     */
    public void init()throws Exception{
        throw new NotImplementedException();
    }

    @Override
    public void putField(String name, Object value) throws NoSuchFieldException, IllegalAccessException {
        throw new NotImplementedException();
    }

    @Override
    public Object getField(String name) throws NoSuchFieldException, IllegalAccessException {
        throw new NotImplementedException();
    }
}
