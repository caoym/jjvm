package org.caoym.jjvm;

import com.sun.tools.classfile.*;
import org.caoym.jjvm.opcodes.Opcode;

/**
 * 字节码方法（区别于 native 方法）
 */
public class JvmOpcodeMethod implements JvmMethod {

    private ClassFile classFile;
    private Method method;
    private Opcode[] opcodes;
    private Code_attribute codeAttribute;

    public JvmOpcodeMethod(ClassFile classFile, Method method)
    {
        this.classFile = classFile;
        this.method = method;
        codeAttribute = (Code_attribute)method.attributes.get("Code");
        opcodes = BytecodeInterpreter.parseCodes(codeAttribute.code);

    }


    public Opcode[] getOpcodes() {
        return opcodes;
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
        Opcode[] operations = getOpcodes();
        while (pc != -1){
            Opcode op = operations[pc];
            op.call(env, frame);
            pc++;
        }
        return null;
    }

    public AccessFlags getAccessFlags() {
        return method.access_flags;
    }
}
