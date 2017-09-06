package org.caoym.jjvm;

import com.sun.tools.classfile.ConstantPool;

import java.util.ArrayList;

/**
 * 虚拟机栈
 * 每个虚拟机线程持有一个独立的栈
 */
public class Stack {

    private StackFrame[] frames;

    public StackFrame newFrame(ConstantPool constant_pool) {
        StackFrame frame = new StackFrame(constant_pool);
        frames.push(frame);
        return frame;
    }
}
