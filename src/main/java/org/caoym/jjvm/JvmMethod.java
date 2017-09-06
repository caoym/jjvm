package org.caoym.jjvm;

import com.sun.tools.classfile.ClassFile;
import com.sun.tools.classfile.Method;

import java.util.concurrent.Callable;

/**
 * Created by caoyangmin on 2017/9/6.
 */
public class JvmMethod{

    private ClassFile classFile;
    private Method method;

    public JvmMethod(ClassFile classFile, Method method)
    {
        this.classFile = classFile;
        this.method = method;
    }

    public Object call(Object ...args) throws Exception {
        BytecodeInterpreter interpreter = new BytecodeInterpreter();
     
        return null;
    }

    public Operation[] getOperations() {

        return operations;
    }
}
