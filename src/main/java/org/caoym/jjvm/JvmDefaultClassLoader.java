package org.caoym.jjvm;

import org.caoym.jjvm.lang.JvmClass;
import org.caoym.jjvm.lang.JvmClassLoader;
import org.caoym.jjvm.opcode.JvmOpcodeClass;
import org.caoym.jjvm.natives.JvmNativeClass;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 虚拟机的引导类加载器
 *
 */
public class JvmDefaultClassLoader implements JvmClassLoader {

    /**
     * 类搜索路径
     */
    private Path classPath;

    public JvmDefaultClassLoader(Path classPath) {
        this.classPath = classPath;
    }
    public JvmClass loadClass(String className) throws ClassNotFoundException{
        //
        String fileName = classPath + "/"+className.replace(".", "/")+".class";
        Path path = Paths.get(fileName);
        //如果文件存在，加载文件字节码
        //否则尝试通过虚拟机宿主加载指定类，并将加载后的类当做 native 类
        if(Files.exists(path)){
             return JvmOpcodeClass.read(this, path);
        }else{
            return new JvmNativeClass(this, Class.forName(className.replace("/",".")));
        }
    }
}
