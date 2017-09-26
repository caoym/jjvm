package org.caoym.jjvm.runtime;

import com.sun.tools.classfile.ConstantPoolException;
import org.caoym.jjvm.VirtualMachine;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;

/**
 * Created by caoyangmin on 2017/9/5.
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
