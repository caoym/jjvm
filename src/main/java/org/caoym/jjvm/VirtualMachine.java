package org.caoym.jjvm;

import java.util.concurrent.Callable;

/**
 * Created by caoyangmin on 2017/9/5.
 */
public class VirtualMachine {

    public void run(String className, String[] args) throws Exception {
        Env env = new Env();
        JvmClass clazz = env.getClass(className);
        //找到入口方法
        JvmMethod method = clazz.getStaticMethod("main", "([Ljava/lang/String;)V");
        //执行路口方法
        method.call(args);
    }

}
