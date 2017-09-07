package org.caoym.jjvm;

import com.sun.tools.classfile.AccessFlags;
import com.sun.tools.classfile.ClassFile;
import com.sun.tools.classfile.Method;
import org.caoym.jjvm.opcodes.Operation;

/**
 * 字节码方法（区别于 native 方法）
 */
public class JvmOpcodeMethod implements JvmMethod {

    private ClassFile classFile;
    private Method method;
    private Operation[] operations;

    public JvmOpcodeMethod(ClassFile classFile, Method method)
    {
        this.classFile = classFile;
        this.method = method;
        operations = new Operation[0];
    }

    public AccessFlags getAccessFlags()
    {
        return method.access_flags;
    }

    public Operation[] getOperations() {
        return operations;
    }

    /**
     * 解释执行方法的字节码
     */
    public Object call(Env env, Object thiz, Object ...args) throws Exception {

        // 程序计数器（Program Counter）, 保存 Java 虚拟机正在执行的 字节码指令的地址.
        int pc = 0;

        // 每次方法调用都产生一个新的栈帧，并入栈，方法退出后出栈
        StackFrame frame = env.getStack().newFrame(classFile.constant_pool);

        //执行字节码
        Operation[] operations = getOperations();
        while (pc != -1){
            Operation op = operations[pc];
            op.call(env, frame, new int[]{});
            pc++;
        }
        return null;
    }
}
