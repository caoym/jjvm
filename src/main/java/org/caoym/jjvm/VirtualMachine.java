package org.caoym.jjvm;

import sun.jvm.hotspot.oops.AccessFlags;

import java.nio.file.Path;
import java.util.Hashtable;

public class VirtualMachine {
    /**
     * 初始类
     * 包含 main 方法的类
     */
    private String initialClass;
    /**
     * 类加载器
     */
    private JvmClassLoader classLoader;
    /**
     * 方法区（Method Area）
     * 存储运行时类信息
     */
    private Hashtable<String, JvmClass> methodArea = new Hashtable<String, JvmClass>();

    public VirtualMachine(Path classPath, String initialClass){
        classLoader = new JvmClassLoader(classPath);
        this.initialClass = initialClass;
    }

    public JvmClass findClass(String className) throws ClassNotFoundException {
        JvmClass found = methodArea.get(className);
        if(found == null){
            found = classLoader.loadClass(className);
            methodArea.put(className, found);
        }
        return found;
    }

    /**
     * 执行虚拟机
     * @param args
     * @throws Exception
     */
    public void run(String[] args) throws Exception {
        Env env = new Env(this);
        JvmClass clazz = findClass(initialClass);
        //找到入口方法
        JvmMethod method = clazz.getMethod(
                "main",
                "([Ljava/lang/String;)V",
                (int)(AccessFlags.JVM_ACC_STATIC|AccessFlags.JVM_ACC_PUBLIC));
        //执行入口方法
        method.call(env, clazz, args);
    }

}
