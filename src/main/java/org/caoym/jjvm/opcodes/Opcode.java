package org.caoym.jjvm.opcodes;

import org.caoym.jjvm.Env;
import org.caoym.jjvm.StackFrame;

@FunctionalInterface
public interface Operation {
    public void call(Env env, StackFrame frame, int[] operands) throws Exception;
}
