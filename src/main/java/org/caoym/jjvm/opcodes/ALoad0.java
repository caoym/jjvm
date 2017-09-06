package org.caoym.jjvm.opcodes;

import org.caoym.jjvm.Stack;
import org.caoym.jjvm.StackFrame;

/**
 * 指令 aload_0
 * 将第一个引用类型局部变量推送至栈顶
 */
public class ALoad0 implements Operation{

    @Override
    public void call(Stack stack, StackFrame frame) {
        frame.push(frame.getLocalVariables().get(0));
    }
}
