package org.caoym.jjvm.runtime;

public interface OpcodeInvoker {
    public void invoke(Env env, StackFrame frame) throws Exception ;
}
