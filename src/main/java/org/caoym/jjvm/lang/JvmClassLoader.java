package org.caoym.jjvm.lang;

public interface JvmClassLoader {
    public JvmClass loadClass(String className) throws ClassNotFoundException;
}
