package org.caoym.jjvm;

import com.sun.tools.classfile.ConstantPoolException;

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
    private Stack stack = new Stack();
    /**
     * 当前虚拟机
     */
    private VirtualMachine vm;

    public Env(VirtualMachine vm){
        this.vm = vm;
    }

    public Stack getStack() {
        return stack;
    }

    public VirtualMachine getVm() {
        return vm;
    }
}
