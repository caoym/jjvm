package org.caoym.jjvm.lang;

public interface JvmObject{

    void putField(String name, Object value) throws NoSuchFieldException, IllegalAccessException;
    Object getField(String name) throws NoSuchFieldException, IllegalAccessException;
}
