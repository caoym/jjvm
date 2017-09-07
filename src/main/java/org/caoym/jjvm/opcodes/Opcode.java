package org.caoym.jjvm.opcodes;

import org.caoym.jjvm.Env;
import org.caoym.jjvm.StackFrame;

@FunctionalInterface
public interface Opcode {
    public void call(Env env, StackFrame frame) throws Exception;
}
