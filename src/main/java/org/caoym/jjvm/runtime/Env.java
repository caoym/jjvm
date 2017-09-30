package org.caoym.jjvm.runtime;
import org.caoym.jjvm.VirtualMachine;


/**
 * 线程上下文
 */
public class Env {
    /**
     * 虚拟机栈
     */
    private JvmStack stack = new JvmStack();
    /**
     * 当前虚拟机
     */
    private VirtualMachine vm;

    public Env(VirtualMachine vm){
        this.vm = vm;
    }

    public JvmStack getStack() {
        return stack;
    }

    public VirtualMachine getVm() {
        return vm;
    }
}
