package org.caoym.jjvm.opcode;

import org.caoym.jjvm.runtime.Env;
import org.caoym.jjvm.runtime.StackFrame;

public interface OpcodeInvoker {
    public void invoke(Env env, StackFrame frame) throws Exception ;
}
