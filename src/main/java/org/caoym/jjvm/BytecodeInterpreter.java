package org.caoym.jjvm;

import org.caoym.jjvm.opcodes.Operation;

/**
 * Created by caoyangmin on 2017/9/5.
 * 字节码解析器
 */
public class BytecodeInterpreter {

    private JvmClass currentClass;
    private JvmMethod currentMethod;

    public void run(StackFrame frame) {
        int pc = 0;
        Operation[] operations = currentMethod.getOperations();
        while (true){
            Operation op = operations[pc];
            op.call(frame);
        }
    }

}
