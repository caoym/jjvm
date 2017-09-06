package org.caoym.jjvm;

import com.sun.tools.classfile.ConstantPoolException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.concurrent.Callable;

/**
 * Created by caoyangmin on 2017/9/5.
 */
public class VirtualMachine {

    /**
     * 类搜索路径
     */
    private Path classPath;
    /**
     * 启动类
     */
    private String className;

    /**
     * 方法区（Method Area）
     * 存储运行时类信息
     */
    private Hashtable<Path, JvmClass> methodArea = new Hashtable<Path, JvmClass>();

    public VirtualMachine(Path classPath, String className){
        this.className = className;
        this.classPath = classPath;
    }

    public JvmClass findClass(String className) throws IOException, ConstantPoolException {
        String fileName = classPath + "/"+className.replace(".", "/")+".class";
        Path path = Paths.get(fileName);
        JvmClass found;
        if((found = methodArea.get(path)) != null){
            return found;
        }
        methodArea.put(path, found = JvmClass.read(path));
        return found;
    }

    public void run(String className, String[] args) throws Exception {
        Env env = new Env(this);
        JvmClass clazz = findClass(className);
        if(clazz == null){
            throw new ClassNotFoundException("class "+className+" not found");
        }
        //找到入口方法
        JvmMethod method = clazz.getStaticMethod("main", "([Ljava/lang/String;)V");
        //执行路口方法
        method.call(env, args);
    }

}
