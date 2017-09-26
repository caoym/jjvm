package org.caoym.jjvm.opcodes;

import org.caoym.jjvm.runtime.Env;
import org.caoym.jjvm.runtime.StackFrame;

@FunctionalInterface
public interface Opcode {
    public void call(Env env, StackFrame frame) throws Exception;
}
