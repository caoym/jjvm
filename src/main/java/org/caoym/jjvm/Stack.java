package org.caoym.jjvm;

import com.sun.tools.classfile.ConstantPool;

/**
 * 虚拟机栈
 * 每个虚拟机线程持有一个独立的栈
 */
public class Stack {

    private SlotsStack frames;

    public StackFrame newFrame(ConstantPool constant_pool) {
        StackFrame frame = new StackFrame(constant_pool);
        frames.push(frame, 1);
        return frame;
    }
}
