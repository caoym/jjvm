package org.caoym.jjvm.opcodes;

import org.caoym.jjvm.Stack;
import org.caoym.jjvm.StackFrame;

/**
 * iconst_0
 * 将 int 类型数据 0 压入到操作数栈中
 */
public class IConst0 implements Operation{

    @Override
    public void call(Stack stack, StackFrame frame) {

    }
}
